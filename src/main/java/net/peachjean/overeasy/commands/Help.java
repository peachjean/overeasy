// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy.commands;

import java.io.IOException;

import javax.inject.Inject;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;
import net.peachjean.overeasy.Environment;
import net.peachjean.overeasy.ShellHelpFormatter;
import net.peachjean.overeasy.command.AbstractCommand;

import org.apache.commons.cli.CommandLine;

public class Help extends AbstractCommand {
    private final Environment env;
	private final ShellHelpFormatter helpFormatter = new ShellHelpFormatter();

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
	        helpFormatter.printGlobalHelp(env, reader.getOutput());
        } else {
	        helpFormatter.printCommandHelp(env, cmd);
        }

    }


}
