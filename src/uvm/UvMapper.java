package uvm;

import java.util.*;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	public static boolean SCALE_QUADS_TO_DISPLAY = true;
	
	public static int MAX_NUM_QUADS_TO_LOAD = 20000, MAX_USAGES_PER_IMG = 3;
	public static int MAX_NUM_IMGS_TO_LOAD = 40000, MIN_ALLOWED_IMG_AREA = 200;

	public static String IMAGE_DIR = "data/";
	public static String OUTPUT_DIR = "warp/";
	public static String DATA_FILE = "data/noTriangle.txt";

	public static String CONVERT_CMD = "/usr/local/bin/convert -resize ";
	public static String CONVERT_ARGS = " -matte -mattecolor transparent -virtual-pixel transparent -interpolate Spline +distort BilinearForward ";

	public void settings() {

		size(1000,1000);
	}

	public void setup() {

		List<UvImage> ads = UvImage.fromFolder(this, IMAGE_DIR, MAX_NUM_IMGS_TO_LOAD);
		List<Quad> quads = Quad.fromData(this, DATA_FILE);

		int processed = assignImages(ads, quads);

		System.out.println("\nProcessed " + processed + "/" + quads.size() + " Quads");

		Quad.drawAll(quads);
	}

	// Loop over Quads assigning best fiting ad image to each
	protected int assignImages(List<UvImage> images, List<Quad> quads) {

		int successes = 0;
		if (images != null) {

			for (int i = 0; i < quads.size(); i++) {
				
				Quad quad = quads.get(i);
				
				// System.out.println("Q:" + q.aspectRatio());
				UvImage bestImg = getBestFit(images, quad);

				if (!quad.image(bestImg)) {
					
					System.err.println("Quad#"+quad.id+" unable to assign image: "+bestImg.imageOut+"/"+bestImg.imageOut+"\n");
					bestImg.usedCount = MAX_USAGES_PER_IMG; // if failed, don't re-use
					if (++quad.tries < 3) // max 3 tries for any quad
						i--; // retry 
					else 
						System.err.println("Giving up on Quad#"+quad.id+"\n"+quad);
					continue;
				}
				
				System.out.print(".");
				if (++successes % 80 == 79)
					System.out.println();
			}
		}

		return successes;
	}

	private UvImage getBestFit(List<UvImage> images, Quad quad) {

		UvImage bestImg = null;
		float bestDist = Float.MAX_VALUE;

		for (UvImage image : images) {

			if (image.usedCount < MAX_USAGES_PER_IMG) {
				
				// System.out.println("I" + i + ": " + img.aspectRation());
				float cdistance = distance(image, quad);

				// System.out.println(cdistance + " " + bestDist);
				if (cdistance < bestDist) {
					// System.out.println("NEW BEST!  "+cdistance);
					bestImg = image;
					bestDist = cdistance;
				}
			}
		}

		if (bestImg == null) {
			System.err.println("[WARN] No image found for Quad#" + quad.id);
		}
		
		return bestImg;
	}

	public float distance(UvImage img, Quad q) {

		return Math.abs(img.aspectRation() - q.aspectRatio());
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
