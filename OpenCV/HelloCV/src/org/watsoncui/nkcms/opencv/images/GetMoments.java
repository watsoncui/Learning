package org.watsoncui.nkcms.opencv.images;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class GetMoments {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	private String dirName;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			(new GetMoments("/home/watsoncui/test")).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GetMoments(String dirName) {
		this.dirName = dirName;
	}
	
	public void run() throws IOException {
		PrintWriter pw1 = new PrintWriter(new FileWriter(new File("/home/watsoncui/pw1.txt")));
		PrintWriter pw2 = new PrintWriter(new FileWriter(new File("/home/watsoncui/pw2.txt")));
		PrintWriter pw3 = new PrintWriter(new FileWriter(new File("/home/watsoncui/pw3.txt")));
		PrintWriter pw4 = new PrintWriter(new FileWriter(new File("/home/watsoncui/pw4.txt")));
		List<String> fileNames = fileList(dirName);
		for(String fileName:fileNames) {
			pw1.print(fileName.substring(0, fileName.lastIndexOf('/')) + '\t' + fileName + "\t");
			pw2.print(fileName.substring(0, fileName.lastIndexOf('/')) + '\t' + fileName + "\t");
			pw3.print(fileName.substring(0, fileName.lastIndexOf('/')) + '\t' + fileName + "\t");
			pw4.print(fileName.substring(0, fileName.lastIndexOf('/')) + '\t' + fileName + "\t");
			
			Mat mat = Highgui.imread(fileName,Highgui.CV_LOAD_IMAGE_GRAYSCALE);
			
			Moments moments = Imgproc.moments(mat);
			
			pw1.print(moments.get_m00());
			pw1.print('\t');
			pw1.print(moments.get_m01());
			pw1.print('\t');
			pw1.print(moments.get_m02());
			pw1.print('\t');
			pw1.print(moments.get_m03());
			pw1.print('\t');
			pw1.print(moments.get_m10());
			pw1.print('\t');
			pw1.print(moments.get_m11());
			pw1.print('\t');
			pw1.print(moments.get_m12());
			pw1.print('\t');
			pw1.print(moments.get_m20());
			pw1.print('\t');
			pw1.print(moments.get_m21());
			pw1.print('\t');
			pw1.println(moments.get_m30());
			
			pw2.print(moments.get_mu02());
			pw2.print('\t');
			pw2.print(moments.get_mu03());
			pw2.print('\t');
			pw2.print(moments.get_mu11());
			pw2.print('\t');
			pw2.print(moments.get_mu12());
			pw2.print('\t');
			pw2.print(moments.get_mu20());
			pw2.print('\t');
			pw2.print(moments.get_mu21());
			pw2.print('\t');
			pw2.println(moments.get_mu30());
			
			pw3.print(moments.get_nu02());
			pw3.print('\t');
			pw3.print(moments.get_nu03());
			pw3.print('\t');
			pw3.print(moments.get_nu11());
			pw3.print('\t');
			pw3.print(moments.get_nu12());
			pw3.print('\t');
			pw3.print(moments.get_nu20());
			pw3.print('\t');
			pw3.print(moments.get_nu21());
			pw3.print('\t');
			pw3.println(moments.get_nu30());
			
			Mat hu = new Mat();
			Imgproc.HuMoments(moments, hu);
			
			pw4.print(hu.get(0, 0)[0]);
			for(int i = 1; i < 7; i++) {
				pw4.print('\t');
				pw4.print(hu.get(i, 0)[0]);
			}
			pw4.println();
		}
		
		pw1.close();
		pw2.close();
		pw3.close();
		pw4.close();
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
}
