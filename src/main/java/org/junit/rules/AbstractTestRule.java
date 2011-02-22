// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.rules;

import org.junit.ClassRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Abstract implementation of {@link TestRule} that makes it easier to implement rules.
 */
public abstract class AbstractTestRule implements TestRule {

	public final Statement apply(RuleType stage, Statement base, Description description) {
		switch (stage) {
		case AROUND_BEFORE_CLASSES:
			return applyAroundBeforeClasses(base, description);
		case AROUND_BEFORES:
			return applyAroundBefores(base, description);
		case AROUND_VERIFICATIONS:
			return applyAroundVerifications(base, description);
		case AROUND_TEST:
			return applyAroundTest(base, description);
		}
		return base;
	}

	/**
	 * Modifies the method-running {@link Statement} to implement this
	 * test-running rule, around all {@link org.junit.BeforeClass} methods.
	 * The default implementation returns a statement that always throws
	 * an exception that has a message that indicates that the rule
	 * is not supported by {@link ClassRule}.
	 *
	 * @param base The {@link Statement} to be modified
	 * @param description A {@link Description} of the test implemented in {@code base}
	 * @return a new statement, which may be the same as {@code base},
	 * a wrapper around {@code base}, or a completely new Statement.
	 */
	protected Statement applyAroundBeforeClasses(Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				String ruleName= AbstractTestRule.this.getClass().getSimpleName();
				throw new Exception(ruleName + " cannot be used as a class rule");
			}
		};
	}

	/**
	 * Modifies the method-running {@link Statement} to implement this
	 * test-running rule, around all {@link org.junit.Before} methods.
	 * The default implementation returns the passed-in statement.
	 *
	 * @param base The {@link Statement} to be modified
	 * @param description A {@link Description} of the test implemented in {@code base}
	 * @return a new statement, which may be the same as {@code base},
	 * a wrapper around {@code base}, or a completely new Statement.
	 */
	protected Statement applyAroundBefores(Statement base, Description description) {
		return base;
	}
	
	/**
	 * Modifies the method-running {@link Statement} to implement this
	 * test-running rule, after the test has run but before all
	 * {@link org.junit.After} methods.
	 * The default implementation returns the passed-in statement.
	 *
	 * @param base The {@link Statement} to be modified
	 * @param description A {@link Description} of the test implemented in {@code base}
	 * @return a new statement, which may be the same as {@code base},
	 * a wrapper around {@code base}, or a completely new Statement.
	 */
	protected Statement applyAroundVerifications(Statement base, Description description) {
		return base;
	}

	/**
	 * Modifies the method-running {@link Statement} to implement this
	 * test-running rule, immediately around the test.
	 *
	 * @param base The {@link Statement} to be modified
	 * @param description A {@link Description} of the test implemented in {@code base}
	 * @return a new statement, which may be the same as {@code base},
	 * a wrapper around {@code base}, or a completely new Statement.
	 */
	protected Statement applyAroundTest(Statement base, Description description) {
		return base;
	}
}
