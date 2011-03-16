package org.junit.runners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.EachBuildRule;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.rules.BuildRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.TestClass;

/**
 * Using <code>Suite</code> as a runner allows you to manually
 * build a suite containing tests from many classes. It is the JUnit 4 equivalent of the JUnit 3.8.x
 * static {@link junit.framework.Test} <code>suite()</code> method. To use it, annotate a class
 * with <code>@RunWith(Suite.class)</code> and <code>@SuiteClasses({TestClass1.class, ...})</code>.
 * When you run this class, it will run all the tests in all the suite classes.
 */
public class Suite extends ParentRunner<Runner> {
	/**
	 * Returns an empty suite.
	 */
	public static Runner emptySuite() {
		try {
			return new Suite((Class<?>)null, new Class<?>[0]);
		} catch (InitializationError e) {
			throw new RuntimeException("This shouldn't be possible");
		}
	}
	
	/**
	 * The <code>SuiteClasses</code> annotation specifies the classes to be run when a class
	 * annotated with <code>@RunWith(Suite.class)</code> is run.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public @interface SuiteClasses {
		/**
		 * @return the classes to be run
		 */
		public Class<?>[] value();
	}
	
	private static Class<?>[] getAnnotatedClasses(Class<?> klass) throws InitializationError {
		SuiteClasses annotation= klass.getAnnotation(SuiteClasses.class);
		if (annotation == null)
			throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
		return annotation.value();
	}

	private final List<Runner> fRunners;

	/**
	 * Called reflectively on classes annotated with <code>@RunWith(Suite.class)</code>
	 * 
	 * @param klass the root class
	 * @param builder builds runners for classes in the suite
	 * @throws InitializationError
	 */
	public Suite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		this(builder, klass, getAnnotatedClasses(klass));
	}

	/**
	 * Call this when there is no single root class (for example, multiple class names
	 * passed on the command line to {@link org.junit.runner.JUnitCore}
	 * 
	 * @param builder builds runners for classes in the suite
	 * @param classes the classes in the suite
	 * @throws InitializationError 
	 */
	public Suite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
		this(null, builder.runners(null, classes));
	}
	
	/**
	 * Call this when the default builder is good enough. Left in for compatibility with JUnit 4.4.
	 * @param klass the root of the suite
	 * @param suiteClasses the classes in the suite
	 * @throws InitializationError
	 */
	protected Suite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		this(new AllDefaultPossibilitiesBuilder(true), klass, suiteClasses);
	}
	
	/**
	 * Called by this class and subclasses once the classes making up the suite have been determined
	 * 
	 * @param builder builds runners for classes in the suite
	 * @param klass the root of the suite
	 * @param suiteClasses the classes in the suite
	 * @throws InitializationError
	 */
	protected Suite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		this(klass, wrapWithBuildRules(klass, builder).runners(klass, suiteClasses));
	}
	
	private static RunnerBuilder wrapWithBuildRules(
			Class<?> klass, final RunnerBuilder builder) throws InitializationError {
		try {
			TestClass testClass = new TestClass(klass);
			List<FrameworkField> fields= testClass.getAnnotatedFields(EachBuildRule.class);
			if (fields.isEmpty()) {
				return builder;
			}
			FrameworkField field= fields.get(0);
			BuildRule object= (BuildRule) field.get(null);
			
			final TestRule rule= object.getAdditionalRule();
			
			return new RunnerBuilder() {

				@Override
				public Runner runnerForClass(Class<?> testClass) throws Throwable {
					Runner runner= builder.runnerForClass(testClass);
					if (runner instanceof BlockJUnit4ClassRunner) {
						((BlockJUnit4ClassRunner) runner).addRule(rule);
					}
					return runner;
				}
			};
		} catch (IllegalArgumentException e) {
			throw new InitializationError(e);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
	}

	/**
	 * Called by this class and subclasses once the runners making up the suite have been determined
	 * 
	 * @param klass root of the suite
	 * @param runners for each class in the suite, a {@link Runner}
	 * @throws InitializationError 
	 */
	protected Suite(Class<?> klass, List<Runner> runners) throws InitializationError {
		super(klass);
		fRunners = runners;
	}
	
	@Override
	protected List<Runner> getChildren() {
		return fRunners;
	}
	
	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	@Override
	protected void runChild(Runner runner, final RunNotifier notifier) {
		runner.run(notifier);
	}
}
