package net.peachjean.overeasy;

import net.peachjean.overeasy.Prompt;

public class SimplePrompt implements Prompt
{
	@Override
	public String getPrompt()
	{
		return System.getProperty("user.name") + " > ";
	}
}
