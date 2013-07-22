package net.peachjean.overeasy;

import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public class LineTokenizerTest
{
	@Test
	public void testSplitLine() throws Exception
	{
		LineTokenizer lineTokenizer = new LineTokenizer();

		doTest("one two three", "one", "two", "three");
		doTest("one -Dtwo=blah three -l frank", "one", "-Dtwo=blah", "three", "-l", "frank");
		doTest("one two \"frank james\"", "one", "two", "frank james");
		doTest("one two \"frank \\\"the man\\\" james\"", "one", "two", "frank \"the man\" james");
	}

	private void doTest(String input, String... expected)
	{
		LineTokenizer lineTokenizer = new LineTokenizer();
		final String[] actualOutput = lineTokenizer.splitLine(input);

		assertThat(actualOutput, arrayContaining(expected));
	}
}
