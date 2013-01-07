// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy.example;

import java.util.Iterator;

import net.peachjean.overeasy.AbstractShell;
import net.peachjean.overeasy.Environment;
import net.peachjean.overeasy.commands.Env;
import net.peachjean.overeasy.commands.Exit;
import net.peachjean.overeasy.commands.Help;
import net.peachjean.overeasy.commands.HistoryCmd;

import com.google.common.collect.PeekingIterator;
import com.google.inject.Binder;
import com.google.inject.Module;

public class ExampleShell extends AbstractShell {
    
    
    public static void main(String[] args) throws Exception{
        System.out.println("StemShell example. Press [TAB] to list available commands.");
        new ExampleShell().run(args);
    }

	@Override
	public Module initialize(final PeekingIterator<String> iterator) throws Exception
	{
		return new Module() {
			@Override
			public void configure(final Binder binder)
			{
				// do nothing
			}
		};
	}


    @Override
    public String getName() {
        return "stemshell-example";
    }

}
