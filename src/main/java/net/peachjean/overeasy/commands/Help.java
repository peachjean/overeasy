// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy.commands;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.inject.Inject;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import net.peachjean.overeasy.Environment;
import net.peachjean.overeasy.command.AbstractCommand;
import net.peachjean.overeasy.command.Command;

public class Help extends AbstractCommand {
    private Environment env;

	@Inject
    public Help(Environment env) {
        super("help");
        this.env = env;
        
        StringsCompleter strCompleter = new StringsCompleter(this.env.commandList());
        NullCompleter nullCompleter = new NullCompleter();
        Completer completer = new AggregateCompleter(strCompleter, nullCompleter);
        
        this.completer = completer;
        
    }
    

    public void execute(Environment env, CommandLine cmd, ConsoleReader reader) throws IOException
    {
        if (cmd.getArgs().length == 0) {
	        Writer out = reader.getOutput();
	        out.write("\n");
	        out.write("Available Commands\n");
	        out.write("------------------\n");
            for (String str : env.commandList()) {
                out.write(String.format("%10.10s  %-60.60s%n", str, env.getCommand(str).getHelpHeader()));
            }
        } else {
            Command command = env.getCommand(cmd.getArgs()[0]);
            logv(cmd, "Get Help for command: " + command.getName() + "(" + command.getClass().getName() + ")");
            printHelp(command);
        }

    }
    
    private void printHelp(Command cmd){
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(cmd.getUsage(), cmd.getHelpHeader(), cmd.getOptions(), cmd.getHelpFooter());
    }
}
