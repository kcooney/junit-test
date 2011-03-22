// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.runners.model;

/**
 * Provides type-safe access to test globals. {@code TestGlobal} instances are
 * typically private static fields in classes that wish to associate state with
 * a test.
 *
 * @param <T> type of the global variable.
 */
public final class TestGlobal<T> {

	public T get(ReadableTestGlobals globals) {
		@SuppressWarnings("unchecked")
		T value= (T) globals.get(this);

		return value;
	}

	public void put(MutableTestGlobals globals, T value) {
		globals.put(this, value);
	}
}
