/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.trajectory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite.
 *
 * @author Jared341
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  com.team254.lib.trajectory.TrajectoryGeneratorTest.class,
  com.team254.lib.trajectory.SplineTest.class,
  com.team254.lib.trajectory.PathGeneratorTest.class,
  com.team254.lib.trajectory.io.SerializationDeserializationTest.class})
public class TrajectoryLibTestSuite {

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

}
