package org.watsoncui.nkcms.opencv.images;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

public class Character {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
		
	private List<Mat> charMatList;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Character c = new Character();
		c.tryToWrite();
	}

	public Character () {
		
	}
	
	public void tryToWrite() {
		Mat mat = Mat.zeros(25,23,CvType.CV_8UC1);
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				mat.put(i, j, 255);
			}
		}

		Core.putText(mat, "B", new Point(2,22), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 0));
		Highgui.imwrite("/home/watsoncui/B.jpg", mat);
	}
	
	public Map<Byte, Mat> charToMat(String charList) {
		List<MatWithInfo> matInfoList = new ArrayList<MatWithInfo>();
		Map<Byte, Mat> charMatMap = new HashMap<Byte, Mat>();
		int length = charList.length();
		if(length > 0) {
			int theMaxCol = 0;
			int theMinCol = Constants.EDGELENGTH;
			int theMaxRow = 0;
			int theMinRow = Constants.EDGELENGTH;
			for(int i = 0; i < length; i++) {
				Mat mat = whiteImg(Constants.EDGELENGTH, CvType.CV_8UC1);
				Core.putText(mat, String.valueOf(charList.charAt(i)), new Point(10, Constants.EDGELENGTH - 10), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 0));
				int colMin = Constants.EDGELENGTH;
				int colMax = 0;
				int rowMin = Constants.EDGELENGTH;
				int rowMax = 0;
				for(int row = 0; row < Constants.EDGELENGTH; row++) {
					for(int col = 0; col < Constants.EDGELENGTH; col++) {
						if(mat.get(row, col)[0] < 1) {
							if(row > rowMax) {
								rowMax = row;
							}
							if(row < rowMin) {
								rowMin = row;
							}
							if(col > colMax) {
								colMax = col;
							}
							if(col < colMin) {
								colMin = col;
							}
						}
					}
				}
				MatWithInfo matInfo = new MatWithInfo();
				matInfo.mat = mat;
				matInfo.colMax = colMax;
				matInfo.colMin = colMin;
				matInfo.rowMax = rowMax;
				matInfo.rowMin = rowMin;
				matInfoList.add(matInfo);
				
				if(rowMax > theMaxRow) {
					theMaxRow = rowMax;
				}
				if(rowMin < theMinRow) {
					theMinRow = rowMin;
				}
				if(colMax > theMaxCol) {
					theMaxCol = colMax;
				}
				if(colMin < theMinCol) {
					theMinCol = colMin;
				}
			}
			
		}
		return charMatMap;
	}
	
	private Mat whiteImg(int edgeLength, int type) {
		Mat mat = new Mat(edgeLength, edgeLength, type);
		for(int i = 0; i < edgeLength; i++) {
			for(int j = 0; j < edgeLength; j++) {
				mat.put(i, j, 255);
			}
		}
		return mat;
	}
	
	public Map<Byte, int[]> MatToCode(Map<Byte, Mat> charMatMap) {
		Map<Byte, int[]> charCodeMap = new HashMap<Byte, int[]>();
		
		return charCodeMap;
	}
	
	class MatWithInfo {
		public Mat mat;
		public int rowMax;
		public int rowMin;
		public int colMax;
		public int colMin;
	}
}
