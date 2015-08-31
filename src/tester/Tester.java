package tester;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import problem.ArmConfig;
import problem.Obstacle;
import problem.ProblemSpec;

public class Tester {
	/** The maximum distance the base can move in one step */
	public static final double MAX_BASE_STEP = 0.001;
	/** The maximum change in joint angle in one step (radians) */
	public static final double MAX_JOINT_STEP = 0.1 * Math.PI / 180.0;
	/** Length of each link */
	public static final double LINK_LENGTH = 0.05;
	/** Minimum joint angle in radians */
	public static final double MIN_JOINT_ANGLE = -150.0 * Math.PI / 180.0;
	/** Maximum joint angle in radians */
	public static final double MAX_JOINT_ANGLE = 150 * Math.PI / 180;
	/** The workspace bounds */
	public static final Rectangle2D BOUNDS = new Rectangle2D.Double(0, 0, 1, 1);
	/** The default value for maximum error */
	public static final double DEFAULT_MAX_ERROR = 1e-5;
	
	/** Remembers the specifications of the problem. */
	private ProblemSpec ps = new ProblemSpec();
	/** The maximum error allowed by this Tester */
	private double maxError;
	/** The workspace bounds, with allowable error. */
	private Rectangle2D lenientBounds;
	
	/**
	 * Creates a new Rectangle2D that is grown by delta in each direction
	 * compared to the given Rectangle2D.
	 *
	 * @param rect
	 *            the Rectangle2D to expand.
	 * @param delta
	 *            the amount to expand by.
	 * @return a Rectangle2D expanded by delta in each direction.
	 */
	public static Rectangle2D grow(Rectangle2D rect, double delta) {
		return new Rectangle2D.Double(rect.getX() - delta, rect.getY() - delta,
				rect.getWidth() + delta * 2, rect.getHeight() + delta * 2);
	}
	
	/**
	 * Constructor. Creates a Tester with the default value for maximum error.
	 */
	public Tester() {
		this(DEFAULT_MAX_ERROR);
	}

	/**
	 * Constructor. Creates a Tester with the given maximum error.
	 *
	 * @param maxError
	 *            the maximum allowable error.
	 */
	public Tester(double maxError) {
		this.maxError = maxError;
		lenientBounds = grow(BOUNDS, maxError);
	}
	
	/**
	 * Checks that the first configuration in the solution path is the initial
	 * configuration.
	 */
	public boolean testInitialFirst(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Initial state", testNo));
		if (!hasInitialFirst()) {
			System.out.println("FAILED: "
					+ "Solution path must start at initial state.");
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}

	/**
	 * Returns whether the first cfg is the initial cfg.
	 *
	 * @return whether the first cfg is the initial cfg.
	 */
	public boolean hasInitialFirst() {
		double dist = ps.getPath().get(0).maxDistance(ps.getInitialState());
		return dist <= maxError && dist >= 0;
	}

	/**
	 * Checks that the last configuration in the solution path is the goal
	 * configuration.
	 */
	public boolean testGoalLast(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Goal state", testNo));
		if (!hasGoalLast()) {
			System.out.println("FAILED: Solution path must end at goal state.");
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}

	/**
	 * Returns whether the last cfg is the goal cfg.
	 *
	 * @return whether the last cfg is the goal cfg.
	 */
	public boolean hasGoalLast() {
		List<ArmConfig> path = ps.getPath();
		double dist = path.get(path.size() - 1).maxDistance(ps.getGoalState());
		return dist <= maxError && dist >= 0;
	}
	
	/**
	 * Returns a copy of list where each value is incremented by delta.
	 *
	 * @param list
	 *            the list of integers to add to.
	 * @return a copy of list where each value is incremented by delta.
	 */
	public List<Integer> addToAll(List<Integer> list, int delta) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i : list) {
			result.add(i + delta);
		}
		return result;
	}
	
	/**
	 * Checks that the steps in between configurations do not exceed the maximum
	 * primitive step distance.
	 */
	public boolean testValidSteps(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Step sizes", testNo));
		List<Integer> badSteps = getInvalidSteps();
		if (!badSteps.isEmpty()) {
			System.out.println(String.format(
					"FAILED: Step size limit exceeded for %d of %d step(s).",
					badSteps.size(), ps.getPath().size() - 1));
			if (verbose) {
				System.out.println("Starting line for each invalid step:");
				System.out.println(addToAll(badSteps, 2));
			}
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}

	/**
	 * Returns the preceding path indices of any invalid steps.
	 *
	 * @return the preceding path indices of any invalid steps.
	 */
	public List<Integer> getInvalidSteps() {
		List<Integer> badSteps = new ArrayList<Integer>();
		List<ArmConfig> path = ps.getPath();
		ArmConfig state = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			ArmConfig nextState = path.get(i);
			if (!isValidStep(state, nextState)) {
				badSteps.add(i - 1);
			}
			state = nextState;
		}
		return badSteps;
	}
	
	/**
	 * Returns whether the step from s0 to s1 is a valid primitive step.
	 *
	 * @param cfg0
	 *            A configuration.
	 * @param cfg1
	 *            Another configuration.
	 * @return whether the step from s0 to s1 is a valid primitive step.
	 */
	public boolean isValidStep(ArmConfig cfg0, ArmConfig cfg1) {
		if (cfg0.getJointCount() != cfg1.getJointCount()) {
			return false;
		} else if (cfg0.maxAngleDiff(cfg1) > MAX_JOINT_STEP + maxError) {
			return false;
		} else if (cfg0.getBase().distance(cfg1.getBase()) > MAX_BASE_STEP + maxError) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks that joint angles are within the allowable range
	 */
	public boolean testJointAngles(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Joint angle limits", testNo));
		List<Integer> badStates = getInvalidJointAngleStates();
		if (!badStates.isEmpty()) {
			System.out.println(String.format(
					"FAILED: Invalid joint angle for %d of %d state(s).",
					badStates.size(), ps.getPath().size()));
			if (verbose) {
				if (verbose) {
					System.out.println("Line for each invalid cfg:");
					System.out.println(addToAll(badStates, 2));
				}
			}
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}
	
	/**
	 * Returns the path indices of any states with invalid joint angles.
	 *
	 * @return the path indices of any states with invalid joint angles.
	 */
	public List<Integer> getInvalidJointAngleStates() {
		List<Integer> badStates = new ArrayList<Integer>();
		List<ArmConfig> path = ps.getPath();
		for (int i = 0; i < path.size(); i++) {
			if (!hasValidJointAngles(path.get(i))) {
				badStates.add(i);
			}
		}
		return badStates;
	}
	
	/** 
	 * Checks if all joint angles are within the limits
	 * 
	 * @param cfg
	 * 			The configuration to test
	 * @return true if all joint angles are within the limits
	 */
	public boolean hasValidJointAngles(ArmConfig cfg) {
		List<Double> jointAngles = cfg.getJointAngles();
		for (Double angle : jointAngles) {
			if (angle <= MIN_JOINT_ANGLE - maxError) {
				return false;
			} else if (angle >= MAX_JOINT_ANGLE + maxError) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks for collision between arm links
	 */
	public boolean testSelfCollision(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Self collision", testNo));
		List<Integer> badStates = getSelfCollidingStates();
		if (!badStates.isEmpty()) {
			System.out.println(String.format(
					"FAILED: Self collision for %d of %d state(s).",
					badStates.size(), ps.getPath().size()));
			if (verbose) {
				if (verbose) {
					System.out.println("Line for each invalid cfg:");
					System.out.println(addToAll(badStates, 2));
				}
			}
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}
	
	/**
	 * Returns the path indices of any states with self collision.
	 *
	 * @return the path indices of any states with self collision.
	 */
	public List<Integer> getSelfCollidingStates() {
		List<Integer> badStates = new ArrayList<Integer>();
		List<ArmConfig> path = ps.getPath();
		for (int i = 0; i < path.size(); i++) {
			if (hasSelfCollision(path.get(i))) {
				badStates.add(i);
			}
		}
		return badStates;
	}

	/**
	 * Checks if a configuration collides with itself. Uses a naive
	 * method where each link is checked for intersection with other links.
	 * 
	 * @param cfg
	 * 			The arm configuration
	 * @return true if there is a collision
	 */
	public boolean hasSelfCollision(ArmConfig cfg) {
		List<Line2D> links = cfg.getLinks();
		for (int i = 0; i < links.size(); i++) {
			for (int j = 0; j < i - 1; j++) {
				if (links.get(i).intersectsLine(links.get(j))) {
					return true;
				}
			}
		}
		return false;		
	}
	
	/**
	 * Checks that each configuration fits within the workspace bounds.
	 */
	public boolean testBounds(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Bounds", testNo));
		List<Integer> badStates = getOutOfBoundsStates();
		if (!badStates.isEmpty()) {
			System.out.println(String.format("FAILED: %d of %d"
					+ " state(s) go out of the workspace bounds.",
					badStates.size(), ps.getPath().size()));
			if (verbose) {
				System.out.println("Line for each invalid cfg:");
				System.out.println(addToAll(badStates, 2));
			}
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}

	/**
	 * Returns the path indices of any states that are out of bounds.
	 *
	 * @return the path indices of any states that are out of bounds.
	 */
	public List<Integer> getOutOfBoundsStates() {
		List<ArmConfig> path = ps.getPath();
		List<Integer> badStates = new ArrayList<Integer>();
		for (int i = 0; i < path.size(); i++) {
			if (!fitsBounds(path.get(i))) {
				badStates.add(i);
			}
		}
		return badStates;
	}
	
	/**
	 * Returns whether the given configuration fits wholly within the bounds.
	 *
	 * @param cfg
	 *            the configuration to test.
	 * @return whether the given configuration fits wholly within the bounds.
	 */
	public boolean fitsBounds(ArmConfig cfg) {
		if (!lenientBounds.contains(cfg.getBase())) {
			return false;
		}
		List<Line2D> links = cfg.getLinks();
		for (Line2D link : links) {
			if (!lenientBounds.contains(link.getP2())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks that each configuration does not collide with any of the
	 * obstacles.
	 */
	public boolean testCollisions(int testNo, boolean verbose) {
		System.out.println(String.format("Test #%d: Collisions", testNo));
		List<Integer> badStates = getCollidingStates();
		if (!badStates.isEmpty()) {
			System.out.println(String.format(
					"FAILED: %d of %d state(s) collide with obstacles.",
					badStates.size(), ps.getPath().size()));
			if (verbose) {
				System.out.println("Line for each invalid cfg:");
				System.out.println(addToAll(badStates, 2));
			}
			return false;
		} else {
			System.out.println("Passed.");
			return true;
		}
	}

	/**
	 * Returns the path indices of any states that collide with obstacles.
	 *
	 * @return the path indices of any states that collide with obstacles.
	 */
	public List<Integer> getCollidingStates() {
		List<ArmConfig> path = ps.getPath();
		List<Integer> badStates = new ArrayList<Integer>();
		for (int i = 0; i < path.size(); i++) {
			if (hasCollision(path.get(i), ps.getObstacles())) {
				badStates.add(i);
			}
		}
		return badStates;
	}

	/**
	 * Returns whether the given config collides with any of the given
	 * obstacles.
	 *
	 * @param cfg
	 *            the configuration to test.
	 * @param obstacles
	 *            the obstacles to test against.
	 * @return whether the given config collides with any of the given
	 *         obstacles.
	 */
	public boolean hasCollision(ArmConfig cfg, List<Obstacle> obstacles) {
		for (Obstacle o : obstacles) {
			if (hasCollision(cfg, o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether the given config collides with the given obstacle.
	 *
	 * @param cfg
	 *            the configuration to test.
	 * @param o
	 *            the obstacle to test against.
	 * @return whether the given config collides with the given obstacle.
	 */
	public boolean hasCollision(ArmConfig cfg, Obstacle o) {
		Rectangle2D lenientRect = grow(o.getRect(), -maxError);
		List<Line2D> links = cfg.getLinks();
		for (Line2D link : links) {
			if (link.intersects(lenientRect)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Runs a specific test based on its name.
	 */
	public boolean testByName(String testName, int testNo, boolean verbose) {
		switch (testName.toLowerCase()) {
		case "initial":
			return testInitialFirst(testNo, verbose);
		case "goal":
			return testGoalLast(testNo, verbose);
		case "steps":
			return testValidSteps(testNo, verbose);
		case "angles":
			return testJointAngles(testNo, verbose);
		case "self-collision":
			return testSelfCollision(testNo, verbose);
		case "bounds":
			return testBounds(testNo, verbose);
		case "collisions":
			return testCollisions(testNo, verbose);
		default:
			return true;
		}
	}

	/**
	 * Runs all test cases from the command line.
	 *
	 * @param args
	 *            the command line arguments.
	 */
	public static void main(String[] args) {
		double maxError = DEFAULT_MAX_ERROR;
		boolean verbose = false;
		String problemPath = null;
		String solutionPath = null;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i].trim();
			if (arg.equals("-e")) {
				i++;
				if (i < args.length) {
					maxError = Double.valueOf(args[i]);
				}
			} else if (arg.equals("-v")) {
				verbose = true;
			} else {
				if (problemPath == null) {
					problemPath = arg;
				} else {
					solutionPath = arg;
				}
			}
		}
		if (problemPath == null) {
			System.out.println("Usage: tester [-e maxError] [-v] "
					+ "problem-file [solution-file]");
			System.exit(1);
		}
		System.out.println("Test #0: Loading files");
		Tester tester = new Tester(maxError);
		try {
			tester.ps.loadProblem(problemPath);
		} catch (IOException e1) {
			System.out.println("FAILED: Invalid problem file");
			System.out.println(e1.getMessage());
			System.exit(1);
		}

		if (solutionPath != null) {
			try {
				tester.ps.loadSolution(solutionPath);
			} catch (IOException e1) {
				System.out.println("FAILED: Invalid solution file");
				System.out.println(e1.getMessage());
				System.exit(1);
			}

		} else {
			tester.ps.assumeDirectSolution();
		}
		System.out.println("Passed.");

		List<String> testsToRun = new ArrayList<String>();
		if (solutionPath != null) {
			testsToRun.addAll(Arrays.asList(new String[] { "initial", "goal",
					"steps"}));
		}
		testsToRun.addAll(Arrays.asList(new String[] { "angles",
				"self-collision", "bounds", "collisions" }));
		int testNo = 1;
		int numFailures = 0;
		for (String name : testsToRun) {
			if (!tester.testByName(name, testNo, verbose)) {
				numFailures++;
			}
			testNo++;
		}
		System.exit(numFailures);
	}
}
