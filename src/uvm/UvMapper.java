package uvm;

import java.util.ArrayList;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	public static int MAX_USAGES_PER_IMG = 2, MAX_NUM_IMGS_TO_LOAD = 10000; 
	public static int MIN_ALLOWED_QUAD_AREA = 400, MIN_ALLOWED_IMG_AREA = 400;
	
	public static String IMAGE_DIR = "data/";
	public static String OUTPUT_DIR = "warp/";
	public static String DATA_FILE = "data/noTriangle.txt";
	
	public static boolean ROUNT_DATA_TO_INTS = false;
	public static String CONVERT_CMD = "/usr/local/bin/convert -resize ";
	public static String CONVERT_ARGS = " -matte -mattecolor transparent -virtual-pixel transparent -interpolate Spline +distort BilinearForward ";

	public void settings() {

		size(this.displayHeight, this.displayHeight);
	}

	public void setup() {

		ArrayList<UvImage> ads = UvImage.loadFolder(this, IMAGE_DIR, MAX_NUM_IMGS_TO_LOAD);
		ArrayList<Quad> quads = Quad.fromData(this, DATA_FILE);
		
		int processed = assignImages(ads, quads);
		
		System.out.println("\nProcessed "+processed+"/"+ quads.size() + " Quads");
		
		Quad.drawAll(quads);
	}

	// Loop over Quads assigning best fiting ad image to each 
	protected int assignImages(ArrayList<UvImage> images, ArrayList<Quad> quads) {

		int processed = 0;
		for (Quad quad : quads) {

			//System.out.println("Q:" + q.aspectRatio());
			UvImage bestImg = null;
			float bestDist = Float.MAX_VALUE;
			
			for (UvImage image: images) {
				
				if (image.usedCount < MAX_USAGES_PER_IMG) {
					// System.out.println("I" + i + ": " + img.aspectRation());
					float cdistance = distance(image, quad);
	
					//System.out.println(cdistance + " " + bestDist);
					if (cdistance < bestDist) {
							//System.out.println("NEW BEST!  "+cdistance);
							bestImg = image;
							bestDist = cdistance;
					}
				}
			}
			
			if (bestImg == null) {
				//System.err.println("[WARN] No image found for Quad#" + q.id);
				continue;
			}
			
			quad.image(bestImg);
			processed++;
		}
		
		return processed;
	}


	public float distance(UvImage img, Quad q) {

		return Math.abs(img.aspectRation() - q.aspectRatio());
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
