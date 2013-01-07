package net.peachjean.overeasy;

import java.util.ServiceLoader;

import net.peachjean.overeasy.command.Command;
import net.peachjean.overeasy.inject.OverEasyModule;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

class OverEasyBaseModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		for(OverEasyModule module: ServiceLoader.load(OverEasyModule.class))
		{
			install(module);
		}
		MapBinder.newMapBinder(binder(), String.class, Command.class);
		bind(Environment.class);
	}
}
