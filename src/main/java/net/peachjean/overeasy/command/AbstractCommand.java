// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy.command;

import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public abstract class AbstractCommand implements Command{
    private String name;
    protected Completer completer = new NullCompleter();
    
    
    public AbstractCommand(String name){
        this.name = name;
    }

	@Override
    public String getHelpHeader() {
        return "Options:";
    }

	@Override
    public String getHelpFooter() {
        return null;
    }

	@Override
    public String getName() {
        return name;
    }


	@Override
    public Options getOptions() {
        Options opts =  new Options();
        opts.addOption("v", "verbose", false, "show verbose output");
        return opts;
    }
    
	@Override
    public String getUsage(){
        return getName() + " [OPTION ...] [ARGS ...]";
    }
    
    protected static void logv(CommandLine cmd, String log){
        if(cmd.hasOption("v")){
            System.out.println(log);
        }
    }
    
    protected static void log(CommandLine cmd, String log){
            System.out.println(log);
    }

	@Override
    public Completer getCompleter() {
        return this.completer;
    }
}
