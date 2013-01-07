package net.peachjean.overeasy;

import java.io.IOException;

import jline.console.ConsoleReader;

public interface SplashScreen
{
	void render(ConsoleReader consoleReader) throws IOException;

	public static final SplashScreen NONE = new SplashScreen()
	{
		@Override
		public void render(final ConsoleReader consoleReader) throws IOException
		{
			// no-op
		}
	};
}
