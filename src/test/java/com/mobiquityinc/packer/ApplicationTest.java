package com.mobiquityinc.packer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.mobiquityinc.exception.APIException;

public class ApplicationTest {

	@Test(expected = NullPointerException.class)
	public void test1_expectNullPointerException() throws APIException, IOException {
		Packer.pack(null);
	}
	
	@Test(expected = APIException.class)
	public void test2_expectFileNotFoundException() throws APIException, IOException {
		Packer.pack("input.pdf");
	}

	@Test
	public void test3_expectSuccessResult() throws APIException, IOException {

		String expectedResult = "4\n" + "-\n" + "2,7" + "\n8,9\n";
		String result = Packer.pack("input.txt");
		assertEquals(expectedResult, result);

	}
}
