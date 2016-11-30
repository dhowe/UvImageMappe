package uvm;

import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	public static String IMAGE_DIR = "data/";
	public static String OUTPUT_DIR = "warp/";
	public static String DATA_FILE = "data/female_uv_test.txt";

	public static boolean ROUNT_DATA_TO_INTS = true;
	public static String CONVERT_CMD = "/usr/local/bin/convert -resize ";
	public static String CONVERT_ARGS = " -matte -mattecolor transparent -virtual-pixel transparent -interpolate Spline -distort BilinearForward ";

  // float[][] test = { { 200,10,700,100,650,500,316,260 } }; 

	public void settings() {

		size(this.displayHeight, this.displayHeight);
	}

	public void setup() {

		// Load images into UvImage objects
		String[] files = new File(IMAGE_DIR).list();
		ArrayList<UvImage> ads = new ArrayList<UvImage>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].matches(".*\\.(png|gif|jpg|jpeg)"))
				ads.add(new UvImage(this, files[i]));
		}

		// Loop over quads, assigning best fitting ad-image
		for (Quad q : Quad.fromData(this, DATA_FILE)) {

			float distance = Math.abs(10 - q.aspectRatio());
			int idx = 0;
			System.out.println("Q:" + q.aspectRatio());

			for (int i = 0; i < ads.size(); i++) {
				UvImage img = ads.get(i);
				// System.out.println("I" + i + ": " + img.aspectRation());
				float cdistance = Math.abs(img.aspectRation() - q.aspectRatio());
				// System.out.println(i + " " + cdistance + " " + distance);
				if (cdistance <= distance) {
					if (!img.used) {
						idx = i;
						distance = cdistance;
					}
					else {
						System.out.println("Used: " + ads.get(i).imageIn);
					}
				}
			}

			q.image(ads.get(idx));
			ads.get(idx).used = true;
			System.out.println("Image: " + ads.get(idx).imageIn);
			q.draw();
		}
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
