package uvm;

import java.io.File;
import java.util.*;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	ArrayList<Quad> quads = new ArrayList<Quad>();
	ArrayList<UvImage> ads = new ArrayList<UvImage>();

  float[][] test = { { 10,10, 326,10, 316,260, 0,280 } }; // tmp-test

	@Override
	public void settings() {
		size(400, 400);
	}

	@Override
	public void setup() {
		
		// Step #1: load images into UvImage objects
	  String[] files = new File("data").list();
	  for (int i = 0; i < files.length; i++) {
	  	ads.add(new UvImage(this, files[i]));
	  }
	  
		// Step #2: import Leoson's data and create the Quads (test for now)
	  for (int i = 0; i < test.length; i++) {
		  quads.add(new Quad(test[i]));
		}
	  
	  // Step #5: sort
	  quads.sort(new Comparator<Quad>() {
	  	public int compare(Quad q1, Quad q2) {
	  		return q1.compareTo(q2);
	  	};
		});
	  
	  // Step #6: loop over assigning the best fitting ad-image
	  for (Iterator it = quads.iterator(); it.hasNext();) {
			Quad q = (Quad) it.next();
	  	for (Iterator it2 = ads.iterator(); it2.hasNext();) {
				UvImage img = (UvImage) it2.next();
				q.image(img);
			}
		}
	  
	  // Step #7: output dat for each quad (JSON not needed
	  for (Iterator it = quads.iterator(); it.hasNext();) {
			System.out.println(it.next());
	  }

	}

	public void draw() {
		
	}

	public static void main(String[] args) {
		PApplet.main(UvMapper.class.getName());
		 
	}
}
