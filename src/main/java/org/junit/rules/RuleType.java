// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.rules;

/**
 * Indicates the type of rule to apply.
 */
public enum RuleType {
	/**
	 * Executed before a collection of tests before any
	 * {@link org.junit.BeforeClass} methods.
	 */
	AROUND_BEFORE_CLASSES {
		@Override public boolean isClassRuleType() { return true; }
	},
	
	/**
	 * Executed for a test before any {@link org.junit.Before} methods.
	 */
	AROUND_BEFORES,
	
	AROUND_VERIFICATIONS,

	AROUND_TEST;

	/**
	 * @return True if this rule type is only used for class rules
	 */
	public boolean isClassRuleType() {
		return false;
	}
}
