package test;

import org.junit.*;

public class MultSolverTest {

	@Test
	public void testMatrix() {

		int[] images = { 3, 5, 2, 4}; // workers
		int[] quads = { 1004, 1001, 1002, 1000 }; // jobs
		
		MultSolver ms = new MultSolver(images, quads);
		double[][] cm = ms.computeCostMatrix();
		
		for (int i = 0; i < cm.length; i++) {
			for (int j = 0; j < cm[i].length; j++) {
				System.out.print(images[i]+"*"+quads[j]+"="+(int)cm[i][j]+"    ");
				Assert.assertTrue(cm[i][j]==images[i]*quads[j]);
			}
			System.out.println();
		}
	}
	
  
}