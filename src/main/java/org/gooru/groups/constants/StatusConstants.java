package org.gooru.groups.constants;

/**
 * @author szgooru on 18-Jul-2018
 */
public class StatusConstants {

	public static final int NOT_STARTED = 0;
	public static final int IN_PROGRESS = 1;
	public static final int INFERRED = 2;
	public static final int ASSERTED = 3;
	public static final int COMPLETED = 4;
	public static final int MASTERED = 5;

	private StatusConstants() {
		throw new AssertionError();
	}
}
