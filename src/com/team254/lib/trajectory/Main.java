package com.team254.lib.trajectory;

import com.team254.lib.trajectory.io.JavaSerializer;

/**
 *
 * @author Jared341
 */
public class Main {
  public static void main(String[] args) {
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 12.0;
    config.max_jerk = 75.0;
    config.max_vel = 15.0;
    
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(6.5, 0, 0));
    p.addWaypoint(new Path.Waypoint(15, 5, Math.PI / 6.0));
    
    Trajectory[] lr = PathGenerator.generateLeftAndRightFromPath(p, config,
        25.5/12);

    JavaSerializer js = new JavaSerializer();
    String[] names = {"Left", "Right"};
    String serialized = js.serialize("com.team254.frc2014.paths.TestPath", names, lr);
    System.out.print(serialized);
  }
}
