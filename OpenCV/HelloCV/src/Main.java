import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;


public class Main {
	
	public static final String FILELIST = "./fileList.txt";
	public static final int SLICENUM = 10;
	public static final int MINFACE = 2 * SLICENUM;
	public static final int THRESHOLDWIDTH = 50;
	
	public static void main(String[] args) throws IOException {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(FILELIST)));
			PrintWriter pw = new PrintWriter(new FileWriter(new File("result.txt")));
			String line = null;
			MainClass mc = new MainClass();
			while((line = br.readLine()) != null) {
				mc.run(pw, line);
			}
			pw.close();
			br.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}

class MainClass {
	public void run(PrintWriter pw, String fileName) {
		System.out.println("\nProcessing " + fileName);
		CascadeClassifier faceDetector = new CascadeClassifier("./haarcascade_frontalface_alt.xml");
		Mat image = Highgui.imread(fileName, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);	

		if(faceDetections.toArray().length <= 0) {
			System.out.println("No faces found, do nothing");
			return;
		} else {
			for(Rect rect:faceDetections.toArray()) {
				if(rect.width > Main.MINFACE) {
					pw.print(fileName);
					Mat mat = image.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
					int[] arr = squareMat2Arr(mat, Main.SLICENUM);
					for(int i = 1; i < 5; i++) {
						pw.print('\t');
						pw.print(printArr(transBipart(arr, Main.SLICENUM * Main.SLICENUM, i * Main.THRESHOLDWIDTH), Main.SLICENUM * Main.SLICENUM));
					}
					pw.println();
				}				
			}
		}
	}
	
	/**
	 * 将正方形Mat根据给定的分割数量改为数组形式，数组的值为相应Mat区域的均值。
	 * @param mat
	 * @param n
	 * @return
	 */
	private int[] squareMat2Arr(Mat mat, int n) {
		int[] result = new int[n*n];
		int step = mat.rows()/n;
		int begin = (mat.rows()%n)/2;
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				int arrId = i * n + j;
				result[arrId] = 0;
				for(int x = 0; x < step; x++) {
					for(int y = 0; y < step; y++) {
						result[arrId] += (int)mat.get(begin + i * step + x, begin + j * step + y)[0];
					}
				}
				result[arrId]/=(step * step);
			}
		}
		return result;
	}
	
	/**
	 * 根据阈值，将灰度数据转换为0或1
	 * @param data
	 * @param length
	 * @param threshold
	 * @return
	 */
	private int[] transBipart(int[] data, int length, int threshold) {
		int[] bipart = new int[length];
		for(int i = 0; i < length; i++) {
			bipart[i] = (data[i] > threshold)? 1 : 0;
		}
		return bipart;
	}
	
	/**
	 * 转换数组为字符串
	 * @param data
	 * @param length
	 * @return
	 */
	private String printArr(int[] data, int length) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < length; i++) {
			sb.append(data[i]);
		}
		return sb.toString();
	}
}
