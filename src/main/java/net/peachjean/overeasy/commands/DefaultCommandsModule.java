package net.peachjean.overeasy.commands;

import net.peachjean.overeasy.command.Command;
import net.peachjean.overeasy.inject.OverEasyModule;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class DefaultCommandsModule extends OverEasyModule
{
	@Override
	protected void configure()
	{
		bindCommand("env", Env.class);
		bindCommand("exit", Exit.class);
		bindCommand("help", Help.class);
		bindCommand("history", HistoryCmd.class);
	}
}
