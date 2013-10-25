package net.peachjean.overeasy;

import java.io.IOException;
import java.io.Writer;

import net.peachjean.overeasy.command.Command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

public class ShellHelpFormatter
{	public void printCommandHelp(final Environment env, final CommandLine cmd)
	{
		Command command = env.getCommand(cmd.getArgs()[0]);
		logv(cmd, "Get Help for command: " + command.getName() + "(" + command.getClass().getName() + ")");
		printHelp(command);
	}

	private void logv(final CommandLine cmd, final String log)
	{
		if(cmd.hasOption("v")){
			System.out.println(log);
		}
	}

	public void printGlobalHelp(final Environment env, final Writer out) throws IOException
	{
		out.write("\n");
		out.write("Available Commands\n");
		out.write("------------------\n");
		int length = 0;
		for (String str : env.commandList()) {
			if(str.length() > length)
			{
				length = str.length();
			}
		}
		int messageLength = 79 - length;
		String format = "%" + length + "." + length + "s  %-" + messageLength + "." + messageLength + "s%n";
		String helpHeader;
		String line;
		int n;

		for (String str : env.commandList()) {
			helpHeader =  env.getCommand(str).getHelpHeader();

			do
			{
				if (helpHeader.length() <= 60)
				{
					line = helpHeader;
					helpHeader = "";
				}
				else
				{
					n = helpHeader.substring(0, messageLength).lastIndexOf(" ");
					line = helpHeader.substring(0, n);
					helpHeader = helpHeader.substring(n + 1);
				}
				System.out.print(String.format(format, str, line));
				str = "";
			}
			while (helpHeader.length() > 0);
		}
	}

	public void printHelp(Command cmd){
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(cmd.getUsage(), cmd.getHelpHeader(), cmd.getOptions(), cmd.getHelpFooter());
    }
}
