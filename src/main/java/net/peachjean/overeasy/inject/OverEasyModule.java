package net.peachjean.overeasy.inject;

import net.peachjean.overeasy.command.Command;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.multibindings.MapBinder;

public abstract class OverEasyModule extends AbstractModule
{
	protected void bindCommand(String name, Class<? extends Command> commandType)
	{
		MapBinder<String, Command> commandMapBinder = MapBinder.newMapBinder(binder(), String.class, Command.class);
		commandMapBinder.addBinding(name).to(commandType);
	}
}
