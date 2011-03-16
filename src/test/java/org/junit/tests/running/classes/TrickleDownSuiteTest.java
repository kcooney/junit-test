// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.tests.running.classes;

import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;
import org.junit.EachBuildRule;
import org.junit.Test;
import org.junit.rules.BuildRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.Statement;

public class TrickleDownSuiteTest {

	@Test
	public void shouldInjectRule() {
		assertThat(testResult(SampleSuite.class), isSuccessful());
	}
	
	@RunWith(Suite.class)
	@SuiteClasses({SampleClass.class})
	public static class SampleSuite {
		@EachBuildRule
		public static BuildRule rule = new BuildRule() {
			@Override
			public TestRule getAdditionalRule() {
				return new TestRule() {
					public Statement apply(
							final Statement base, Description description) {
						return new Statement() {
							@Override
							public void evaluate() throws Throwable {;
								try {
									base.evaluate();
								} catch (ArithmeticException ignored) {
								}
							}
						};
					}
				};
			}
		};
	}
	
	
	@RunWith(JUnit4.class)
	public static class SampleClass {
	
		@Test
		public void throwsArithmeticException() {
			throw new ArithmeticException("expected");
		}
	}
}
