// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.runners.model;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Mutable collection of test globals. Values that are not explicitly added
 * to this object are obtained from a backing {@link ReadableTestGlobals}
 * instance. Values put into this object are stored in this object,
 * but do not affect the backing {@link ReadableTestGlobals} instance.<p>
 *
 * This allows a runner to change or add global variables, and pass the new
 * global variables to child runners.
 */
public class MutableTestGlobals extends ReadableTestGlobals {
	private final Map<Object, Object> globals = new IdentityHashMap<Object, Object>();
	private final ReadableTestGlobals fInheritedGlobals;

	public MutableTestGlobals(ReadableTestGlobals inheritedGlobals) {
		fInheritedGlobals= inheritedGlobals;
	}

	@Override
	Object get(Object key) {
		Object value= globals.get(key);
		if (value == null && fInheritedGlobals != null) {
			value= fInheritedGlobals.get(key);
		}
		return value;
	}

	/**
	 * Sets a value for a given key. Can be used to override a value
	 * stored in the {@code ReadableTestGlobals} instance passed to
	 * the constructor.
	 *
	 * @param key
	 * @param value
	 */
	void put(Object key, Object value) {
		globals.put(key, value);
	}

	@Override
	public TestGlobals toTestGlobals() {
		return new TestGlobals(this);
	}
}
