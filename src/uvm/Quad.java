package uvm;

public class Quad implements Comparable<Quad> {

	public float[] points;
	public UvImage image;
	public int id;

	public Quad(float...pts) {
		this.points = pts;
		this.id = ID_GEN++;
	}
	
	public Quad image(UvImage image) {
		this.image = image;
		return this;
	}
	
	public String toString() {
		
		float[] srcDst = new float[16];
		for (int i = 0, j = 0; i < srcDst.length; i++) {
			if (i%4 > 1) srcDst[i] = points[j++];
		}
		srcDst[4] = srcDst[8] = image.width;
		srcDst[9] = srcDst[12] = image.height;
		
		return pointsToString(srcDst);
	}

	public float aspectRatio() {

		return image.aspectRation();
	}

	String pointsToString(float[] srcDst) {
		String s = image.imageName + " -matte -virtual-pixel transparent -interpolate Spline -distort BilinearForward \"";
		for (int i = 0; i < srcDst.length; i++) {
			s += ROUNT_TO_INTS ? ""+Math.round(srcDst[i]) : srcDst[i];
			if (i < srcDst.length -1) {
				s += (i > 0 && i % 4==3) ? ", " : ",";
			}
			else 
				s += "\""; 
		}
		return s;
	}
	
	public int compareTo(Quad o) {

		return aspectRatio() > o.aspectRatio() ? 1 : -1;
	}
	
	public static boolean ROUNT_TO_INTS = true;
	public static int ID_GEN = 0;

}
