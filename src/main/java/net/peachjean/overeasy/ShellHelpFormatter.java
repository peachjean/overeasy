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
		String format = "%" + length + "." + length + "s %-60.60s%n";
		for (String str : env.commandList()) {
			out.write(String.format(format, str, env.getCommand(str).getHelpHeader()));
		}
	}

	public void printHelp(Command cmd){
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(cmd.getUsage(), cmd.getHelpHeader(), cmd.getOptions(), cmd.getHelpFooter());
    }
}
