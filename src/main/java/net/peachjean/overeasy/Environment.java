// Copyright (c) 2012 P. Taylor Goetz (ptgoetz@gmail.com)

package net.peachjean.overeasy;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.peachjean.overeasy.command.Command;

// TODO: Remove this once Guice MapBinder supports javax.inject.Provider
import com.google.inject.Provider;

@Singleton
public class Environment {
    
    private Properties props = new Properties();
    private HashMap<String, Object> values = new HashMap<String, Object>();

	private final Map<String, Provider<Command>> commands;

	@Inject
	public Environment(Map<String, Provider<Command>> commands){
	    this.commands = commands;
    }

    public Command getCommand(String name){
	    final Provider<Command> commandProvider = this.commands.get(name);
	    return commandProvider == null ? null : commandProvider.get();
    }
    
    public Set<String> commandList(){
        return this.commands.keySet();
    }
    
    public void setProperty(String key, String value){
        if(value == null){
            this.props.remove(key);
        } else{
            this.props.setProperty(key, value);
        }
    }
    
    public String getProperty(String key){
        return this.props.getProperty(key);
    }
    
    public Properties getProperties(){
        return this.props;
    }
    
    public void setValue(String key, Object value){
        this.values.put(key, value);
    }
    
    public Object getValue(String key){
        return this.values.get(key);
    }
}
