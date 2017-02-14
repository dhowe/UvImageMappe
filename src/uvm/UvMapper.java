package uvm;

import java.util.*;
import java.text.SimpleDateFormat; 

import processing.core.PApplet;
import processing.core.PImage;

public class UvMapper extends PApplet {

	public static boolean CROP_IMGS_TO_QUADS = false;
	public static boolean STROKE_QUAD_OUTLINES = false;
	public static boolean SCALE_QUADS_TO_DISPLAY = true;
	
	public static boolean CHANGE_ORIGIN_TO_BOTTOM_LEFT = true;
	public static boolean DRAW_QUAD_DEBUG_DATA = false; 
	public static boolean SHOW_PROGRESS_DOTS = true;
	
	public static int MAX_NUM_QUADS_TO_LOAD = 2000, MAX_NUM_IMGS_TO_LOAD = 100; 
	public static int MAX_USAGES_PER_IMG = 1, MIN_ALLOWED_IMG_AREA = 10;

	public static String DATA_FILE = "data/BerthaEyes.txt";
	public static String UV_NAME = "BarthaTest.png";
	public static String IMAGE_DIR = "allImages/", OUTPUT_DIR = "warp/";
	
	public static String CONVERT_CMD = "/usr/local/bin/convert ";
	public static String CONVERT_ARGS = " -matte -mattecolor transparent -virtual-pixel transparent -interpolate Spline +distort BilinearForward ";

	List<Quad> quads;
	
	public void settings() {

		size(1000, 1000);
	}

	public void setup() {

		List<UvImage> ads = UvImage.fromFolder(this, IMAGE_DIR, MAX_NUM_IMGS_TO_LOAD);
		quads = Quad.fromData(this, DATA_FILE);

		int processed = assignImagesWithFitness(ads, quads);
		System.out.println("\nProcessed " + processed + "/" + quads.size() + " Quads");
	}
	
	public void draw() {

		Quad.drawAll(quads);
//		Quad.mouseOver(quads);
	}
	
	public void keyPressed() {
		if (key=='s') saveToFile();
	}
  
	int assignImagesWithFitness(List<UvImage> images, List<Quad> quads) {

		int successes = 0;
		if (images != null) {
			
			int[] result = new ImageQuadSolver(images, quads).execute();
			for (int i = 0; i < result.length; i++) {
			System.err.print(result[i ]+",");
			}

			for (int i = 0; i < quads.size(); i++) {
				
				Quad quad = quads.get(i);				
				UvImage bestImg = images.get(result[i]);
				
				if (bestImg == null) {
					
					System.err.println("Quad#"+quad.id+" null image!\n");
					continue;
				}
				
				if (!quad.assignImage(bestImg)) {
					
					System.err.println("Quad#"+quad.id+" unable to assign image: "+bestImg.warpName+"/"+bestImg.warpName+"\n");
					
					if (++quad.tries < 3) // max 3 tries for any quad
						i--; // retry 
					else 
						System.err.println("Giving up on Quad#"+quad.id+"\n"+quad);
					
					continue;
				}
				
//				System.out.println("Quad#"+quad.id+" gets: "+bestImg);
				
				showProgress(++successes);
			}
		}

		return successes;
	}
	
	// Assigning best fiting ad image to each quad
	int assignImages(List<UvImage> images, List<Quad> quads) {

		int successes = 0;
		if (images != null) {

			for (int i = 0; i < quads.size(); i++) {
				
				Quad quad = quads.get(i);				
				UvImage bestImg = getBestFit(images, quad);
				
				if (bestImg == null) {
					
					System.err.println("Quad#"+quad.id+" null image!\n");
					continue;
				}
				
				if (!quad.assignImage(bestImg)) {
					
					System.err.println("Quad#"+quad.id+" unable to assign image: "+bestImg.warpName+"/"+bestImg.warpName+"\n");
					
					if (++quad.tries < 3) // max 3 tries for any quad
						i--; // retry 
					else 
						System.err.println("Giving up on Quad#"+quad.id+"\n"+quad);
					
					continue;
				}
				
//				System.out.println("Quad#"+quad.id+" gets: "+bestImg);
				
				showProgress(++successes);
			}
		}

		return successes;
	}
	
	public static void showProgress(int x) {
		
		if (SHOW_PROGRESS_DOTS) {
			
			System.out.print(".");
			if (x % 80 == 79)
				System.out.println();
		}
	}

	UvImage getBestFit(List<UvImage> images, Quad quad) {

		UvImage bestImg = null;
		float bestDist = Float.MAX_VALUE;

		//System.out.println("UvMapper.getBestFit()"+quad.id + " area="+quad.area());
		
		for (UvImage image : images) {

			if (image.acceptsQuad(quad)) {
				
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

	void saveToFile() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fname  = (dateFormat.format(new Date()) + "_" + UV_NAME).replaceAll(" ", "_");
		save(fname);
		System.out.println("Wrote " + fname);
	}
	
	float distance(UvImage img, Quad q) {

		return Math.abs(img.aspectRation() - q.aspectRatio());
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
