// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy.command;

import java.io.IOException;
import java.util.Map;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import net.peachjean.overeasy.Environment;

public interface Command {

    String getHelpHeader();
    String getHelpFooter();
    String getUsage();

    String getName();
    
    void execute(Environment env, CommandLine cmd, ConsoleReader reader) throws IOException;
    
    Options getOptions();
    
    Completer getCompleter();
}
