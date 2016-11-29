package uvm;

import java.io.*;
import java.util.*;

import org.apache.commons.exec.*;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	public static String IMAGE_DIR = "data/";
	public static String OUTPUT_DIR = "warp/";
	public static String DATA_FILE = "data/data.txt";

	public static boolean ROUNT_DATA_TO_INTS = true;
	public static String CONVERT_CMD = "/usr/local/bin/convert -resize ";
	public static String CONVERT_ARGS = " -matte -mattecolor transparent -virtual-pixel transparent -interpolate Spline -distort BilinearForward ";

	ArrayList<Quad> quads = new ArrayList<Quad>();
	ArrayList<UvImage> ads = new ArrayList<UvImage>();
	ArrayList<float[]> data = new ArrayList<float[]>();
	
//	float[][] test = { { 200,10,700,100,650,500,316,260 } }; 

	public void settings() {

		size(800, 700);
	}

	public void setup() {

		// Step #1: load images into UvImage objects
		String[] files = new File(IMAGE_DIR).list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].matches(".*\\.(png|gif|jpg|jpeg)"))
				ads.add(new UvImage(this, files[i]));
		}

		// Step #2: import data and create the Quads 
		ReadTemps(DATA_FILE);
		for (int i = 0; i < data.size(); i++) {	
			quads.add(new Quad(data.get(i)));
		}

		// Step #5: sort the quads
//		quads.sort(new Comparator<Quad>() {
//			public int compare(Quad q1, Quad q2) {
//				return q1.compareTo(q2);
//			};
//		});

		// Step #6: loop over quads, assigning best fitting ad-image (TODO)
		for (Iterator it = quads.iterator(); it.hasNext();) {

			Quad q = (Quad) it.next();
			for (Iterator it2 = ads.iterator(); it2.hasNext();) {

				UvImage img = (UvImage) it2.next();

				if (!img.used) {
					q.image(img);
					img.used = true;
					break;
				}
			}
		}

		// Step #7: Create/exec the commands for each warp op
		for (Iterator it = quads.iterator(); it.hasNext();) {
			
			Quad q = (Quad) it.next();
			String cmd = q.toConvertCommand();			
			System.out.println(cmd + " -> " + exec(cmd));
			q.loadWarp(this);
		}
	}

	public void draw() {

		// Step #8: Draw warped images with quads on top
		for (Iterator it = quads.iterator(); it.hasNext();) {
			Quad q = (Quad) it.next();
			q.draw(this);
		}
	}

	public static int exec(String line) {

		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(10000);
		executor.setWatchdog(watchdog);
		try {
			return executor.execute(cmdLine);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void ReadTemps(String path) {

		// create token1
		String token1 = "";
		// create Scanner inFile1
		try {
			Scanner inFile1 = new Scanner(new File(path));

			List<String> temps = new ArrayList<String>();

			// while loop
			while (inFile1.hasNext()) {
				// find next line
				token1 = inFile1.next();
				temps.add(token1);
			}
			inFile1.close();

			String[] lines = temps.toArray(new String[0]);

			for (String s : lines) {

				String[] pointsS = s.split(",");
				float[] pointsF = new float[pointsS.length];

				for (int i = 0; i < pointsS.length; i++) {
					pointsF[i] = Float.parseFloat(pointsS[i]);
//					System.out.println(pointsF[i]);
				}
				data.add(pointsF);

			}

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
