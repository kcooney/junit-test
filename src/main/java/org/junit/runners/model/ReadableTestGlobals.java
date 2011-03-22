// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.runners.model;

/**
 * Interface to access global variables for tests. This allows global state
 * to be passed from Runner to Runner without using statics.
 */
public abstract class ReadableTestGlobals {

	abstract Object get(Object key);
	
	public abstract TestGlobals toTestGlobals();
}
