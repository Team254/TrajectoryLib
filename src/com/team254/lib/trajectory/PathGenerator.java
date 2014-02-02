/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import static com.team254.lib.trajectory.TrajectoryGenerator.SCurvesStrategy;
import static com.team254.lib.trajectory.TrajectoryGenerator.generate;

/**
 * Generate a smooth Trajectory from a Path.
 * 
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class PathGenerator {
  
  public static Trajectory generateFromPath(Path path,
          TrajectoryGenerator.Config config) {
    if (path.getNumWaypoints() < 2) {
      return null;
    }
    
    // Compute the total length of the path by creating splines for each pair
    // of waypoints.
    Spline[] splines = new Spline[path.getNumWaypoints() - 1];
    double[] spline_lengths = new double[splines.length];
    double total_distance = 0;
    for (int i = 0; i < splines.length; ++i) {
      splines[i] = new Spline();
      if (!Spline.reticulateSplines(path.getWaypoint(i), 
              path.getWaypoint(i+1), splines[i])) {
        return null;
      }
      spline_lengths[i] = splines[i].calculateLength();
      total_distance += spline_lengths[i];
    }
    
    // Generate a smooth trajectory over the total distance.
    Trajectory traj = generate(config, SCurvesStrategy, 0.0,
            path.getWaypoint(0).theta, total_distance, 0.0,
            path.getWaypoint(0).theta);
    
    // Assign headings based on the splines.
    int cur_spline = 0;
    double cur_spline_start_pos = 0;
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      double cur_pos = traj.getSegment(i).pos;
      double cur_pos_relative = cur_pos - cur_spline_start_pos;
      
      if (cur_pos_relative <= spline_lengths[cur_spline]) {
        double percentage = cur_pos_relative / spline_lengths[cur_spline];
        traj.getSegment(i).heading = splines[cur_spline].angleAt(percentage);
      } else {
        cur_spline_start_pos = cur_pos;
        ++cur_spline;
      }
    }
    
    return traj;
  }
}
