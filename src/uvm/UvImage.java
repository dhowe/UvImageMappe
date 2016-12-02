package uvm;

import java.io.File;
import java.util.*;

import processing.core.*;

public class UvImage {

	public PImage image;
	public int quadId = -1;
	public int width, height;
	public String imageName, warpName;
	public ArrayList<Float> appliedAreas;

	public UvImage(PImage image, String imageName) {
		
		this.image = image;
		this.imageName = imageName;
		this.width = this.image.width;
		this.height = this.image.height;
		this.appliedAreas = new ArrayList<Float>();
		this.warpName = changeExt(imageName, ".png");
	}

	public String changeExt(String fileName, String ext) {
		
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		return tokens[0] + ext;
	}

	public float aspectRation() {

		return image.width / (float) image.height;
	}

	public String toString() {

		return "{ name: " + imageName + ", width: " + width + ", height: " 
				+ height + (quadId > -1 ? (", quad: " + quadId) : " ") + "}";
	}

	public float area() {

		return width * height;
	}
	
	public static ArrayList<UvImage> loadFolder(PApplet p, String dir) {
		return fromFolder(p, dir, Integer.MAX_VALUE);
	}
	
	public static ArrayList<UvImage> fromFolder(PApplet p, String dir, int maxNum) {

		// Load images into UvImage objects
		String[] files = new File(dir).list();
		
		System.out.println("Found "+files.length+" image files");
		
		ArrayList<UvImage> ads = new ArrayList<UvImage>();

		for (int i = 0; i < files.length; i++) {
			
			if (files[i].matches(".*\\.(png|gif|jpg|jpeg)")) {
				
				//System.out.println("Trying "+dir+"/"+files[i]);
				PImage pimg = p.loadImage(dir+"/"+files[i]);
				if (pimg == null || (p.width * p.height) < UvMapper.MIN_ALLOWED_IMG_AREA) {
					System.out.println("[WARN] "+ (pimg == null ? "Unable to load image: " 
							: "Image too small") +":"+ dir+"/"+files[i]);
					System.exit(1);
					continue;
				}
				ads.add(new UvImage(pimg, files[i]));
				
				UvMapper.showProgress(ads.size());
			}
			if (ads.size() >= maxNum)
				break;
		}

	  // sort the images by area
		ads.sort(new Comparator<UvImage>() {

			public int compare(UvImage img1, UvImage img2) {
				
				if (img1.area() > img2.area())
					return -1;
				else if (img1.area() == img2.area())
					return 0;
				else
					return 1;
			}
		});
		
		System.out.println("\nLoaded "+ads.size()+" images");
		
		return ads;
	}

	// True iff we have more applications remaining 
	// and we haven't accepted a quad with this area before
	public boolean acceptsQuad(Quad quad) {
		
		if (appliedAreas.size() >= UvMapper.MAX_USAGES_PER_IMG)
			return false;
		
		float check = Math.round(quad.area()); // round to get near misses
		boolean accepted = !appliedAreas.contains(check);
		//if (!accepted) System.out.println("Quad#"+quad.id+" rejected for "+imageIn);
		return accepted;
	}
}
