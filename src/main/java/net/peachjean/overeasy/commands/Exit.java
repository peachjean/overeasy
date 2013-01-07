// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy.commands;

import jline.console.ConsoleReader;

import org.apache.commons.cli.CommandLine;

import net.peachjean.overeasy.Environment;
import net.peachjean.overeasy.command.AbstractCommand;

public class Exit extends AbstractCommand {

    public Exit() {
        super("exit");
    }

    public void execute(Environment env, CommandLine cmd, ConsoleReader reader) {
        System.exit(0);
    }

    @Override
    public String getHelpHeader() {
        return "exit the command shell";
    }

    @Override
    public String getUsage() {
        return this.getName();
    }
    
    

}
