/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.trajectory;

import com.team254.lib.util.ChezyMath;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for Spline class.
 *
 * @author Art Kalb
 * @author Jared341
 */
public class SplineTest {

  public boolean almostEqual(double x, double y) {
    return Math.abs(x - y) < 1E-6;
  }

  public void test(double x0, double y0, double theta0, double x1, double y1,
          double theta1, boolean is_straight) {
    Spline.Type[] types = {Spline.CubicHermite, Spline.QuinticHermite};
    for (Spline.Type type : types) {
      Spline s = new Spline();
      Assert.assertTrue(Spline.reticulateSplines(x0, y0, theta0, x1, y1, theta1,
              s, type));
      System.out.println(s.toString());

      for (double t = 0; t <= 1.0; t += .05) {
        System.out.println("" + t + ", " + s.valueAt(t) + ", " + s.angleAt(t));
      }

      Assert.assertTrue(almostEqual(s.valueAt(0), y0));
      Assert.assertTrue(almostEqual(s.valueAt(1), y1));

      Assert.assertTrue(almostEqual(
              ChezyMath.boundAngleNegPiToPiRadians(s.angleAt(0)),
              ChezyMath.boundAngleNegPiToPiRadians(theta0)));
      Assert.assertTrue(almostEqual(
              ChezyMath.boundAngleNegPiToPiRadians(s.angleAt(1)),
              ChezyMath.boundAngleNegPiToPiRadians(theta1)));

      if (is_straight) {
        double expected = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
        System.out.println("Expected length=" + expected + "; actual="
                + s.calculateLength());
        Assert.assertTrue(almostEqual(s.calculateLength(), expected));
      }

      if (type == Spline.QuinticHermite) {
        // Second derivatives should be 0
        Assert.assertTrue(almostEqual(0, s.angleChangeAt(0)));
        Assert.assertTrue(almostEqual(0, s.angleChangeAt(1)));

        System.out.println("From (" + x0 + "," + y0 + ") to (" + x1 + "," + y1
                + ")");
        for (double t = 0; t <= 1.04; t += .05) {
          System.out.println(t + "\t" + s.angleAt(t));
        }
      }

      // Test arc length estimates
      double length = s.calculateLength();
      System.out.println("Arc Length: " + length);
      double x_last = x0;
      double y_last = y0;
      for (int i = 1; i <= 100; ++i) {
        double percentage = s.getPercentageForDistance(i * length / 100);
        double[] xy = s.getXandY(percentage);
        double measured = Math.sqrt((xy[0] - x_last) * (xy[0] - x_last)
                + (xy[1] - y_last) * (xy[1] - y_last));
        System.out.println(i + "\t" + percentage + "\t" + length / 100 + "\t"
                + measured);
        x_last = xy[0];
        y_last = xy[1];
        Assert.assertTrue(almostEqual(measured / 1E3, length / 100 / 1E3));
      }
    }
  }

  public void expectFailure(double x0, double y0, double theta0, double x1,
          double y1, double theta1) {
    Spline s = new Spline();
    Assert.assertFalse(Spline.reticulateSplines(x0, y0, theta0, x1, y1, theta1,
            s, Spline.CubicHermite));
  }

  public SplineTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testUnitLines() {
    test(0, 0, 0, 1, 0, 0, true);
    test(0, 0, Math.PI / 2, 0, 1, Math.PI / 2, true);
    test(0, 0, Math.PI, -1, 0, Math.PI, true);
    test(0, 0, -Math.PI / 2, 0, -1, -Math.PI / 2, true);
    test(0, 0, Math.PI / 4, Math.sqrt(2) / 2, Math.sqrt(2) / 2, Math.PI / 4, true);
  }

  @Test
  public void testNonUnitLines() {
    test(0, 0, 0, 5, 0, 0, true);
    test(0, 0, Math.PI / 4, Math.sqrt(2), Math.sqrt(2), Math.PI / 4, true);
  }

  @Test
  public void testTranslatedUnitLines() {
    test(1, 1, 0, 2, 1, 0, true);
    test(1, 1, Math.PI / 4, Math.sqrt(2) / 2 + 1, Math.sqrt(2) / 2 + 1, Math.PI / 4,
            true);
  }

  @Test
  public void testTranslatedNonUnitLines() {
    test(1, 1, 0, 6, 1, 0, true);
    test(1, 1, Math.PI / 4, Math.sqrt(2) + 1, Math.sqrt(2) + 1, Math.PI / 4,
            true);
  }

  @Test
  public void testUnitStep() {
    test(0, 0, 0, Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0, false);
  }

  @Test
  public void testNonUnitStep() {
    test(0, 0, 0, 10, 20, 0, false);
  }

  @Test
  public void testTranslatedStep() {
    test(0, 5, 0, 10, 20, 0, false);
  }

  @Test
  public void testRotatedStep() {
    test(0, 0, Math.PI / 4, 0, 1, Math.PI / 4, false);
  }

  @Test
  public void testInvalidInput() {
    expectFailure(0, 0, 0, 0, 0, 0);
    expectFailure(0, 0, -Math.PI / 2, 1, 0, 0);
  }

  @Test
  public void testProblematicSCurve() {
    test(0, 0, 0, 3, 4, Math.PI / 4, false);
  }

  @Test
  public void testRegressionQuintic1() {
    test(0, 0, 0, 10, 5, 0, false);
    test(10, 5, 0, 30, -5, 0, false);
    test(30, -5, 0, 40, 0, 0, false);
  }
}
