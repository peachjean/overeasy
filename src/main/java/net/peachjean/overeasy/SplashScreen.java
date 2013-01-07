package net.peachjean.overeasy;

import java.io.IOException;

import jline.console.ConsoleReader;

public interface SplashScreen
{
	void render(ConsoleReader consoleReader) throws IOException;
}
