import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class Experiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Experiment.matInverse();
	}

	public static void matInverse() {
		Mat mat = Highgui.imread("/home/watsoncui/pics/0.jpg", Highgui.CV_LOAD_IMAGE_COLOR);
		mat.create(mat.cols(), mat.cols(), CvType.CV_32F);
		System.out.println(mat.get(0, 0)[0]);
		Highgui.imwrite("/home/watsoncui/pics/0.new.jpg",mat);
		System.out.println(mat.type());
		System.out.println(CvType.typeToString(mat.type()));
		Highgui.imwrite("/home/watsoncui/pics/0.inverse.jpg",mat.inv());
		Highgui.imwrite("/home/watsoncui/pics/0.inverse2.jpg",mat.inv().inv());
	}
}
