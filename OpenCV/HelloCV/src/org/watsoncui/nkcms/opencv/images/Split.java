package org.watsoncui.nkcms.opencv.images;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Split {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public final static int WIDTH = 30;
	public final static int HEIGHT = 30;
	public final static int THRESHOLD = 100;

	private String dirName;
	private String targetFileName;
	
	private Map<String, int[]> imgCodeMap;
	private Map<String, Mat> imgDataMap;
	
	private Size dsize;
	
	public Split(String dirName, String targetFileName) {
		this.imgCodeMap = new HashMap<String, int[]>();
		this.imgDataMap = new HashMap<String, Mat>();
		this.dsize = new Size(WIDTH, HEIGHT);
		this.dirName = dirName;
		this.targetFileName = targetFileName;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Split split = new Split("/home/watsoncui/test2", "/home/watsoncui/HR5VRBWL8S.jpg");
		split.run();
//		split.processFile("/home/watsoncui/pics/3.jpg");
	}
	
	/**
	 * 一揽子批处理方法
	 */
	public void run() {
		List<String> fileNameList = fileList(dirName);
		for(String fileName:fileNameList) {
			processFile(fileName);
		}
		Mat mat = createPicture(targetFileName);
		Highgui.imwrite(targetFileName + ".surprise.jpg", mat);
	}
	
	/**
	 * 读取源文件，处理为小图像和灰度图像编码
	 * @param fileName
	 */
	public void processFile(String fileName) {
		Mat mat = Highgui.imread(fileName);
		Imgproc.resize(mat, mat, dsize);
		imgDataMap.put(fileName, mat);
		if(mat.channels() != 1) {
			Mat grayMat = new Mat();
			Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);
			int[] code = convertToArr(grayMat, THRESHOLD);
			imgCodeMap.put(fileName, code);
		} else {
			int[] code = convertToArr(mat, THRESHOLD);
			imgCodeMap.put(fileName, code);
		}
	}
	
	/**
	 * 将目标图片转换为小图片的贴片
	 * @param fileName
	 * @return
	 */
	public Mat createPicture(String fileName) {
		Mat freshMat = Highgui.imread(fileName, Highgui.CV_LOAD_IMAGE_COLOR);
		Mat mat = new Mat();
		Imgproc.cvtColor(freshMat, mat, Imgproc.COLOR_BGR2GRAY);
		int width = mat.cols()/WIDTH;
		int height = mat.rows()/HEIGHT;
		Mat resultMat = new Mat(height * HEIGHT, width * WIDTH, freshMat.type());
		for(int i = 0; i < width; i++) {
			for(int j = 0; j< height; j++) {
				Mat submat = mat.submat(j * HEIGHT, (j + 1) * HEIGHT, i * WIDTH, (i + 1) * WIDTH);
				int[] code = convertToArr(submat, THRESHOLD);
				Mat replaceMat = getDataByCode(code);
				resultMat = pastMatOnMat(resultMat, replaceMat, j * HEIGHT, i * WIDTH);
			}
		}
		
		return resultMat;
	}
	
	/**
	 * 获得一个文件夹中的所有文件
	 * @param dirName
	 * @return
	 */
	public List<String> fileList(String dirName) {
		File dir = new File(dirName);
		List<String> strList = new ArrayList<String>();
		if (dir.isFile()) {
			strList.add(dirName);
			return strList;
		} else {
			String[] dirList = dir.list();
			for(String str:dirList) {
				String subDirName = dirName + "/" + str;
				List<String> fileNames = fileList(subDirName);
				for(String fileName:fileNames) {
					strList.add(fileName);
				}
			}
		}
		return strList;
	}
	
	/**
	 * 转换图像数据文件为01整型数组文件
	 * @param src
	 * @param threshold
	 * @return
	 */
	private int[] convertToArr(Mat src, int threshold) {
		int height = src.rows();
		int width = src.cols();
		int[] result = new int[width * height];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				result[i * width + j] = (src.get(i, j)[0] > threshold)? 1 : 0;
			}
		}
		return result;
	}

	/**
	 * 使用待匹配块儿的编码，寻找图片库中最相似的图片数据
	 * @param code
	 * @return
	 */
	private Mat getDataByCode(int[] code) {
		String bestKey = null;
		int matchNum = 0;
		Set<String> keySet = imgCodeMap.keySet();
		for(String key:keySet) {
			int temp = sameNumbers(code, imgCodeMap.get(key));
			if(temp > matchNum) {
				bestKey = key;
				matchNum = temp;
			}
		}
		return imgDataMap.get(bestKey);
	}
	/**
	 * 数组比对，曼哈顿距离
	 * @param src1
	 * @param src2
	 * @return
	 */
	private int sameNumbers(int[] src1, int[] src2) {
		int num  = 0;
		int length = src1.length;
		for(int i = 0; i < length; i++) {
			if(src1[i] == src2[i]) {
				num++;
			}
		}
		return num;
	}
	
	/**
	 * 向大图上贴小图
	 * @param bigMat
	 * @param splitMat
	 * @param beginRow
	 * @param beginCol
	 * @return
	 */
	private Mat pastMatOnMat(Mat bigMat, Mat splitMat, int beginRow, int beginCol) {
		int endRow = splitMat.rows();
		int endCol = splitMat.cols();
		for(int i = 0; i < endRow; i++) {
			for (int j = 0; j < endCol; j++) {
				bigMat.put(beginRow + i, beginCol + j, splitMat.get(i, j));
			}
		}
		return bigMat;
	}
	
}
