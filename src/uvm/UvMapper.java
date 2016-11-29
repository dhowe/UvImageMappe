package uvm;

import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	public static String IMAGE_DIR = "data/";
	public static String OUTPUT_DIR = "warp/";
	public static String DATA_FILE = "data/data.txt";

	public static boolean ROUNT_DATA_TO_INTS = true;
	public static String CONVERT_CMD = "/usr/local/bin/convert -resize ";
	public static String CONVERT_ARGS = " -matte -mattecolor transparent -virtual-pixel transparent -interpolate Spline -distort BilinearForward ";

  // float[][] test = { { 200,10,700,100,650,500,316,260 } }; 

	public void settings() {

		size(800, 700);
	}

	public void setup() {

		// Load images into UvImage objects
		String[] files = new File(IMAGE_DIR).list();
		ArrayList<UvImage> ads = new ArrayList<UvImage>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].matches(".*\\.(png|gif|jpg|jpeg)"))
				ads.add(new UvImage(this, files[i]));
		}

		// Loop over quads, assigning best fitting ad-image (TODO)
		for (Quad q : Quad.fromData(this, DATA_FILE)) {
			for (UvImage img : ads) {
				if (!img.used) {
					
					q.image(img); // scale/warp image
					img.used = true;
					break;
				}
			}
			q.draw();
		}
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
