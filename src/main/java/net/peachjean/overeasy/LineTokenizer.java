package net.peachjean.overeasy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class LineTokenizer
{
	String[] splitLine(String line)
	{
		List<String> tokens = new ArrayList<String>();

		final int lineLength = line.length();
		boolean currWithinQuotes = false;
		final StringBuilder currentBuilder = new StringBuilder();
		for(int i = 0; i < lineLength; i++)
		{
			char currentChar = line.charAt(i);
			if(currentChar == '"')
			{
				finalizeToken(tokens, currentBuilder);
				currWithinQuotes = !currWithinQuotes;
			}
			else if(currentChar == '\\')
			{
				char nextChar = line.charAt(i + 1);
				if(nextChar == '"' || nextChar == '\\')
				{
					currentBuilder.append(nextChar);
					i++;
				}
				else
				{
					currentBuilder.append(currentChar);
				}
			}
			else if(!currWithinQuotes && Character.isWhitespace(currentChar))
			{
				finalizeToken(tokens, currentBuilder);
			}
			else
			{
				currentBuilder.append(currentChar);
			}
		}
		finalizeToken(tokens, currentBuilder);
		return tokens.toArray(new String[tokens.size()]);
	}

	private void finalizeToken(List<String> tokens, StringBuilder currentBuilder)
	{
		if(currentBuilder.length() > 0)
		{
			tokens.add(currentBuilder.toString());
			currentBuilder.setLength(0);
		}
	}
}
