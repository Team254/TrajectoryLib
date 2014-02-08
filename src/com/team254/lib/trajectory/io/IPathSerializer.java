package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Trajectory;

/**
 * Interface for methods that serialize a Path or Trajectory.
 *
 * @author Jared341
 */
public interface IPathSerializer {

  public String serialize(String filename, String[] names,
          Trajectory[] trajectories);
}
