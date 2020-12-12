package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelperTest {

	@Test
	public void verify_getCount(){
		List<String> empNames = Arrays.asList("Andy", "Linsey");
		final long actual = Helper.getCount(empNames);
		assertEquals(2, actual);
	}

	@Test
	public void verify_getMergedList(){
		List<String> empNames = Arrays.asList("Andy", "Linsey");
		final String actual = Helper.getMergedList(empNames);
		assertEquals("Andy, Linsey", actual);
	}
}
