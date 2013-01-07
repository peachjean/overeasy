// Copyright (c) 2012 Health Market Science, Inc.

package net.peachjean.overeasy.commands;

import java.util.ListIterator;

import jline.console.ConsoleReader;
import jline.console.history.History.Entry;

import org.apache.commons.cli.CommandLine;

import net.peachjean.overeasy.Environment;
import net.peachjean.overeasy.command.AbstractCommand;

public class HistoryCmd extends AbstractCommand {

    public HistoryCmd() {
        super("history");
    }

    @Override
    public void execute(Environment env, CommandLine cmd, ConsoleReader reader) {
        jline.console.history.History history = reader.getHistory();
        if(cmd.getArgList().isEmpty())
        {
            ListIterator<Entry> it = history.entries();
            while(it.hasNext()){
                Entry entry = it.next();
                System.out.println(entry.value());
            }
        }
        else
        {
            int histArg = Integer.parseInt(cmd.getArgs()[0]);
            Entry entry = history.entries(history.size() - histArg - 1).next();


        }

    }

}
