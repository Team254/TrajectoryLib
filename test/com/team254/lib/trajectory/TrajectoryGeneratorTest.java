/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.trajectory;

import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jarussell
 */
public class TrajectoryGeneratorTest {

  static void test(double start_vel, double goal_vel, double goal_distance,
          TrajectoryGenerator.Strategy strategy) {
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 250.0;
    config.max_jerk = 1250.0;
    config.max_vel = 100.0;

    Trajectory traj = TrajectoryGenerator.generate(
            config,
            strategy,
            start_vel,
            -75.0,
            goal_distance,
            goal_vel,
            75.0);

    System.out.print(traj.toString());

    Trajectory.Segment last = traj.getSegment(traj.getNumSegments() - 1);
    Assert.assertFalse(Math.abs(last.pos - goal_distance) > 1.0);
    Assert.assertFalse(Math.abs(last.vel - goal_vel) > 1.0);
    Assert.assertFalse(Math.abs(last.heading - 75.0) > 1.0);
  }

  public TrajectoryGeneratorTest() {
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

  // Zero velocity endpoints
  @Test
  public void testP2PStep() {
    test(100, 100, 120, TrajectoryGenerator.StepStrategy);
  }

  @Test
  public void testP2PShortStep() {
    test(100, 100, 30, TrajectoryGenerator.StepStrategy);
  }

  @Test
  public void testP2PTrapezoid() {
    test(0, 0, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testP2PShortTrapezoid() {
    test(0, 0, 30, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testP2PSCurves() {
    test(0, 0, 120, TrajectoryGenerator.SCurvesStrategy);
  }

  @Test
  public void testP2PShortSCurves() {
    test(0, 0, 30, TrajectoryGenerator.SCurvesStrategy);
  }

  // Non-zero velocity endpoints
  @Test
  public void testRampUp() {
    test(0, 100, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testSlowRampUp() {
    test(0, 50, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testRampDown() {
    test(100, 0, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testSlowRampDown() {
    test(50, 0, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testRampUpDown() {
    test(50, 50, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }

  @Test
  public void testConstantVelTrapezoid() {
    test(100, 100, 120, TrajectoryGenerator.TrapezoidalStrategy);
  }
}
