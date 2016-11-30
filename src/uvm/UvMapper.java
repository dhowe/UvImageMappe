package uvm;

import java.io.File;
import java.util.*;

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
		
		ArrayList<Quad> quads = Quad.fromData(this, DATA_FILE);
		quads.sort(new Comparator<Quad>() {
			public int compare(Quad q1, Quad q2) {
				return q1.aspectRatio() > q2.aspectRatio() ? -1 : 1;
			}
		});

		// Loop over quads, assigning best fitting ad-image
		for (Quad q : quads) {
			
			//System.out.println("Q:" + q.aspectRatio());
			UvImage bestImg = null;
			float bestDist = Float.MAX_VALUE;
			
			System.out.println("START "+bestDist);
			for (UvImage img: ads) {
				
				if (!img.used) {
					// System.out.println("I" + i + ": " + img.aspectRation());
					float cdistance = distance(img, q);
	
					// System.out.println(i + " " + cdistance + " " + distance);
					if (cdistance < bestDist) {
							System.out.println("NEW BEST!  "+cdistance);
							bestImg = img;
							bestDist = cdistance;
					}
				}
			}

			bestImg.used = true;
			q.image(bestImg);
			
			//System.out.println("Image: " + bestImg.imageIn);
			
			q.draw();
		}
	}

	public float distance(UvImage img, Quad q) {

		return Math.abs(img.aspectRation() - q.aspectRatio());
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
