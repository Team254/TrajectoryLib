/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import com.team254.lib.trajectory.PathGenerator.PathProfile;
import static com.team254.lib.trajectory.TrajectoryGeneratorTest.test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class PathGeneratorTest {
  
  static void test(Path path) {
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 250.0;
    config.max_jerk = 1250.0;
    config.max_vel = 100.0;
    Trajectory traj = PathGenerator.generateFromPath(path, config);
    
    // TODO: Check some stuff
    System.out.print(traj.toString());
    System.out.println("Final distance=" +
            traj.getSegment(traj.getNumSegments()-1).pos);
    
    PathProfile path_profile = PathGenerator.makeLeftAndRightTrajectories(traj,
            20.0);
    
    System.out.println("LEFT PROFILE:");
    System.out.println(path_profile.left.toString());
    System.out.println("RIGHT PROFILE:");
    System.out.println(path_profile.right.toString());
  }
  
  public PathGeneratorTest() {
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
  public void testSCurveLikePath() {
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(10*12, 0, 0));
    //test(p);
    p.addWaypoint(new Path.Waypoint(15*12, 5*12, Math.PI/4));
    p.addWaypoint(new Path.Waypoint(20*12, 10*12, Math.PI/4));
    p.addWaypoint(new Path.Waypoint(30*12, 10*12, 0));
    test(p);
  }
}
