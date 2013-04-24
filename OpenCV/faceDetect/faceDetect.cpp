#include <iostream>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <cstdio>

using namespace cv;
using namespace std;

int main(int argc, char *argv[]) {
	String face_cascade_name = "haarcascade_frontalface_alt.xml";
	CascadeClassifier face_cascade;
	
	Mat mat = imread("pudin.jpg");
	Mat grayMat;
	
	vector<Rect> faces;
	
	cvtColor(mat, grayMat, CV_BGR2GRAY);
	face_cascade.detectMultiScale(grayMat, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30,30));
	for(int i = 0; i < faces.size(); i++) {
		Point center(faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5);
		ellipse(mat, center, Size(faces[i].width * 0.5, faces[i].height * 0.5), 0, 0, 360, Scalar(255, 0, 0));
	}
	
	imshow("demo", mat);
	waitKey(0);
	exit(0);
}