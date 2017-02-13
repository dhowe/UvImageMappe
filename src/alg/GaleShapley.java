package alg;

// NEXT: change 2 String[] to UvImage[] and Quad[]
public class GaleShapley {

	private int N, completed = 0;
	private String[][] imagePrefs;
	private String[][] quadPrefs;
	private String[] images;
	private String[] quads;
	private String[] quadPartners;
	private boolean[] imagesPaired;

	public GaleShapley(String[] i, String[] q, String[][] ip, String[][] qp) {
		
		images = i;
		quads = q;
		imagePrefs = ip;
		quadPrefs = qp;
		N = ip.length;
		imagesPaired = new boolean[N];
		quadPartners = new String[N];
	}

	public static String[] computeMatches(String[] i, String[] q, String[][] ip, String[][] qp) {
		
		return new GaleShapley(i, q, ip, qp).compute();
	}
	
	private String[] compute() {
		while (completed < N) {
			int free;
			for (free = 0; free < N; free++)
				if (!imagesPaired[free]) break;

			for (int i = 0; i < N && !imagesPaired[free]; i++) {
				int index = quadIndexOf(imagePrefs[free][i]);
				if (quadPartners[index] == null) {
					quadPartners[index] = images[free];
					imagesPaired[free] = true;
					completed++;
				}
				else {
					String currentPartner = quadPartners[index];
					if (morePreference(currentPartner, images[free], index)) {
						quadPartners[index] = images[free];
						imagesPaired[free] = true;
						imagesPaired[imageIndexOf(currentPartner)] = false;
					}
				}
			}
		}
		return quadPartners;
	}

	/** function to check if women prefers new partner over old assigned partner **/
	private boolean morePreference(String curPartner, String newPartner, int index) {

		for (int i = 0; i < N; i++) {
			if (quadPrefs[index][i].equals(newPartner)) return true;
			if (quadPrefs[index][i].equals(curPartner)) return false;
		}
		return false;
	}

	private int imageIndexOf(String str) {

		for (int i = 0; i < N; i++)
			if (images[i].equals(str)) return i;
		return -1;
	}

	private int quadIndexOf(String str) {

		for (int i = 0; i < N; i++)
			if (quads[i].equals(str)) return i;
		return -1;
	}

	public static void print(String[] quads, String[] quadPartners) {

		System.out.println("Pairs are : ");
		for (int i = 0; i < quads.length; i++) {
			System.out.println(quads[i] + ":"+quadPartners[i]);
		}
	}

	public static void main(String[] args) {

		String[] images = { "I1", "I2", "I3", "I4", "I5" };
		String[] quads = { "Q1", "Q2", "Q3", "Q4", "Q5" };

		String[][] imagePrefs = { { "Q5", "Q2", "Q3", "Q4", "Q1" }, { "Q2", "Q5", "Q1", "Q3", "Q4" }, { "Q4", "Q3", "Q2", "Q1", "Q5" }, { "Q1", "Q2", "Q3", "Q4", "Q5" }, { "Q5", "Q2", "Q3", "Q4", "Q1" } };		
		String[][] quadPrefs = { { "I5", "I3", "I4", "I1", "I2" }, { "I1", "I2", "I3", "I5", "I4" }, { "I4", "I5", "I3", "I2", "I1" }, { "I5", "I2", "I1", "I4", "I3" }, { "I2", "I1", "I4", "I3", "I5" } };

		print(quads, GaleShapley.computeMatches(images, quads, imagePrefs, quadPrefs));
	}
	
}