// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When a {@link Runner} class is annotated with <code>&#064;Factory</code>, JUnit
 * will create an instance of the class it references to create the {@code Runner}.
 * Having a factory for a runner makes it easier to write unit tests the runner,
 * since you can avoid doing work in the runner constructor.
 *  
 * @author kcooney@google.com (Kevin Cooney)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Factory {
	/**
	 * @return a Factory class (must have a public no-arg constructor)
	 */
	Class<? extends RunnerFactory> value();
}
