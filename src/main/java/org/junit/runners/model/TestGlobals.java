// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.runners.model;

/**
 * Immutable collection of test globals.
 */
public class TestGlobals extends ReadableTestGlobals {
	private final ReadableTestGlobals fDelegate;

	/**
	 * Creates an empty test globals object.
	 */
	public TestGlobals() {
		this(null);
	}

	TestGlobals(ReadableTestGlobals delegate) {
		fDelegate= delegate;
	}

	@Override
	Object get(Object key) {
		return fDelegate == null ? null : fDelegate.get(key);
	}

	@Override
	public TestGlobals toTestGlobals() {
		return this;
	}
}
