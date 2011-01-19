// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.runner;

import org.junit.runners.model.RunnerBuilder;

/**
 * A factory for {@link Runner} instances. Classes that implement this interface are
 * the target of the {@link Factory} annotation.<p>
 *
 * Classes that implement this interface must have a public no-argument constructor.
 * 
 * @author kcooney@google.com (Kevin Cooney)
 */
public interface RunnerFactory {

	/**
	 * Creates a runner to run the given class
	 *
	 * @param testClass Class annotated with {@code RunWith}
	 * @param builder Builder for runners
	 * @return Runner
	 */
	Runner createRunner(Class<?> testClass, RunnerBuilder builder);
}
