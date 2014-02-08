/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Path.Waypoint;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.io.JavaSerializer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarussell
 */
public class SerializationTest {

  public SerializationTest() {
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
  public void testJavaSerialization() {
    Path p = new Path(10);
    p.addWaypoint(new Waypoint(0, 0, 0));
    p.addWaypoint(new Waypoint(50, 0, 0));
    p.addWaypoint(new Waypoint(150, 50, Math.PI / 4));

    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 250.0;
    config.max_jerk = 1250.0;
    config.max_vel = 100.0;

    Trajectory[] lr = PathGenerator.generateLeftAndRightFromPath(p, config,
            25.0);

    JavaSerializer js = new JavaSerializer();
    String[] names = {"Left", "Right"};
    String serialized = js.serialize("com.team254.testing.Auto1", names, lr);
    System.out.print(serialized);
  }
}
