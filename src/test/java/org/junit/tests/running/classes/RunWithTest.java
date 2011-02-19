package org.junit.tests.running.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Factory;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.RunnerFactory;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.RunnerBuilder;
import org.junit.tests.running.classes.RunWithTest.ThrowingRunnerFactory;

public class RunWithTest {

	private static String log = "";

	public static class LoggingRunner extends Runner {
		public LoggingRunner(Class<?> klass) {
			log+= "initialize";
		}

		@Override
		public void run(RunNotifier notifier) {
			log+= "run";
		}

		@Override
		public int testCount() {
			log+= "count";
			return 0;
		}

		@Override
		public Description getDescription() {
			log+= "plan";
			return Description.createSuiteDescription("example");
		}		
	}
	
	@RunWith(LoggingRunner.class)
	public static class ExampleTest {
	}
	
	@Test public void run() {
		JUnitCore.runClasses(ExampleTest.class);
		assertTrue(log.contains("plan"));
		assertTrue(log.contains("initialize"));
		assertTrue(log.contains("run"));
	}

	public static class SubExampleTest extends ExampleTest {
	}
	
	@Test public void runWithExtendsToSubclasses() {
		JUnitCore.runClasses(SubExampleTest.class);
		assertTrue(log.contains("run"));
	}
	
	public static class RunnerWithoutProperConstructor extends Runner {
		@Override
		public Description getDescription() {
			return null;
		}

		@Override
		public void run(RunNotifier notifier) {
			// do nothing
		}
	}
	
	@RunWith(RunnerWithoutProperConstructor.class)
	public static class TestWithRunnerWithoutProperConstructor {		
	}
	
	@Test
	public void characterizeErrorMessageFromRunnerWithoutProperConstructor() {
		assertEquals(
				"Custom runner class RunnerWithoutProperConstructor "
				+ "should have a public constructor with signature "
				+ "RunnerWithoutProperConstructor(Class testClass)",
				JUnitCore.runClasses(TestWithRunnerWithoutProperConstructor.class)
				        .getFailures().get(0).getMessage());
	}

	public static class RunnerThatThrowsInConstructor extends Runner {
		public RunnerThatThrowsInConstructor(Class<?> klass) {
			throw new RuntimeException("expected");
		}

		@Override
		public Description getDescription() {
			return null;
		}

		@Override
		public void run(RunNotifier notifier) {
		}
	}
	
	@RunWith(RunnerThatThrowsInConstructor.class)
	public static class TestWithRunnerThatThrowsInConstructor {
	}
	
	@Test
	public void messagePropagatedWhenRunnerConstructorThrows() {
		assertEquals(
				"expected",
				JUnitCore.runClasses(TestWithRunnerThatThrowsInConstructor.class)
				        .getFailures().get(0).getMessage());
	}

	public static class SurferDudeRunnerFactory implements RunnerFactory {
		public Runner createRunner(Class<?> testClass, RunnerBuilder builder) {
			return new SufferDudeLoggingRunner(testClass, "Dude, ");
		}
	}

	@Factory(SurferDudeRunnerFactory.class)
	public static class SufferDudeLoggingRunner extends Runner {
		private final String fPrefix;

		public SufferDudeLoggingRunner(Class<?> klass, String prefix) {
			fPrefix= prefix;
			log+= fPrefix+"initialize";
		}

		@Override
		public void run(RunNotifier notifier) {
			log+= fPrefix+"run";
		}

		@Override
		public int testCount() {
			log+= fPrefix+"count";
			return 0;
		}

		@Override
		public Description getDescription() {
			log+= fPrefix+"plan";
			return Description.createSuiteDescription("example");
		}		
	}
	
	@RunWith(SufferDudeLoggingRunner.class)
	public static class ExampleTestWithRunnerFactory {
	}
	
	@Test public void runnerFactory() {
		JUnitCore.runClasses(ExampleTestWithRunnerFactory.class);
		assertTrue(log.contains("Dude, plan"));
		assertTrue(log.contains("Dude, initialize"));
		assertTrue(log.contains("Dude, run"));
	}

	public static class ThrowingRunnerFactory implements RunnerFactory {
		public Runner createRunner(Class<?> testClass, RunnerBuilder builder) {
			throw new RuntimeException("expected exception from factory");
		}
	}

	@Factory(ThrowingRunnerFactory.class)
	public static class RunnerThatUsesFactoryThatThrows extends Runner {
		private RunnerThatUsesFactoryThatThrows() {
		}

		@Override
		public Description getDescription() {
			return null;
		}

		@Override
		public void run(RunNotifier notifier) {
		}
	}
	
	@RunWith(RunnerThatUsesFactoryThatThrows.class)
	public static class TestWithRunnerThatUsesFactoryThatThrows {
	}
	
	@Test
	public void messagePropagatedWhenRunnerFactoryThrows() {
		assertEquals(
				"expected exception from factory",
				JUnitCore.runClasses(TestWithRunnerThatUsesFactoryThatThrows.class)
				        .getFailures().get(0).getMessage());
	}
}
