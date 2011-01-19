/**
 * 
 */
package org.junit.internal.builders;

import org.junit.runner.Factory;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.RunnerFactory;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class AnnotatedBuilder extends RunnerBuilder {
	private static final String RUNNER_CONSTRUCTOR_ERROR_FORMAT= "Custom runner class %s should have a public constructor with signature %s(Class testClass)";
	private static final String FACTORY_CONSTRUCTOR_ERROR_FORMAT= "Factory class %s for custom runner %s should have a public no-arg constructor";
	
	private RunnerBuilder fSuiteBuilder;

	public AnnotatedBuilder(RunnerBuilder suiteBuilder) {
		fSuiteBuilder= suiteBuilder;
	}

	@Override
	public Runner runnerForClass(Class<?> testClass) throws Exception {
		RunWith annotation= testClass.getAnnotation(RunWith.class);
		if (annotation != null)
			return buildRunner(annotation.value(), testClass);
		return null;
	}

	public Runner buildRunner(Class<? extends Runner> runnerClass,
			Class<?> testClass) throws Exception {
		Factory annotation= runnerClass.getAnnotation(Factory.class);
		if (annotation != null) {
			RunnerFactory factory = buildFactory(annotation.value(), runnerClass);
			return factory.createRunner(testClass, fSuiteBuilder);
		}

		try {
			return runnerClass.getConstructor(Class.class).newInstance(
					new Object[] { testClass });
		} catch (NoSuchMethodException e) {
			try {
				return runnerClass.getConstructor(Class.class,
						RunnerBuilder.class).newInstance(
						new Object[] { testClass, fSuiteBuilder });
			} catch (NoSuchMethodException e2) {
				String simpleName= runnerClass.getSimpleName();
				throw new InitializationError(String.format(
						RUNNER_CONSTRUCTOR_ERROR_FORMAT, simpleName, simpleName));
			}
		}
	}

	private RunnerFactory buildFactory(Class<? extends RunnerFactory> factoryClass,
			Class<? extends Runner> runnerClass) throws Exception {
		try {
			return factoryClass.getConstructor().newInstance();
		} catch (NoSuchMethodException e) {
			String factorySimpleName= factoryClass.getSimpleName();
			String runnerSimpleName= runnerClass.getSimpleName();
			throw new InitializationError(String.format(
					FACTORY_CONSTRUCTOR_ERROR_FORMAT, factorySimpleName, runnerSimpleName));
		}
	}
}