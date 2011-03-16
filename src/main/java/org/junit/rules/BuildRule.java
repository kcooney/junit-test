// Copyright 2011 Google Inc. All Rights Reserved.

package org.junit.rules;

/**
 * @author kcooney@google.com (Kevin Cooney)
 *
 */
public abstract class BuildRule {

	public abstract TestRule getAdditionalRule();

}
