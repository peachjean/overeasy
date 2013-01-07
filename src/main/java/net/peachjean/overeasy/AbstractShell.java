// Copyright (c) 2012 Health Market Science, Inc.

package net.peachjean.overeasy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import jline.console.history.FileHistory;
import jline.console.history.History;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.fusesource.jansi.AnsiConsole;

import net.peachjean.overeasy.command.Command;
import net.peachjean.overeasy.commands.DefaultCommandsModule;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public abstract class AbstractShell {
    private static CommandLineParser parser = new PosixParser();

    public final void run(String[] arguments) throws Exception {
	    Injector injector = createInjector(arguments);
        Environment env = injector.getInstance(Environment.class);

        // create reader and add completers
        ConsoleReader reader = new ConsoleReader();

	    SplashScreen ss = injector.getInstance(SplashScreen.class);
	    if(ss != null)
	    {
		    ss.render(reader);
	    }

	    Prompt prompt = injector.getInstance(Prompt.class);

        reader.addCompleter(initCompleters(env));
        // add history support
        reader.setHistory(initHistory());

        AnsiConsole.systemInstall();
        
        acceptCommands(reader, env, prompt);

    }

	private Injector createInjector(final String[] arguments) throws Exception
	{
		final Module initialModule = this.initialize(Iterators.peekingIterator(Arrays.asList(arguments).iterator()));
		final Module defaultCommands = this.useDefaultCommands() ? new DefaultCommandsModule() : Modules.EMPTY_MODULE;
		return Guice.createInjector(Modules.override(new OverEasyBaseModule()).with(initialModule), defaultCommands);
	}

	private boolean useDefaultCommands()
	{
		return true;
	}

	private void acceptCommands(ConsoleReader reader, final Environment env, final Prompt prompt) throws IOException {
        String line;
        while ((line = reader.readLine(prompt.getPrompt())) != null) {
            String[] argv = line.split("\\s");
            String cmdName = argv[0];

            Command command = env.getCommand(cmdName);
            if (command != null) {
//                System.out.println("Running: " + command.getName() + " ("
//                                + command.getClass().getName() + ")");
                String[] cmdArgs = Arrays.copyOfRange(argv, 1, argv.length);
                CommandLine cl = parse(command, cmdArgs);
                if (cl != null) {
                    try {
                        command.execute(env, cl, reader);
                    }
                    catch (Throwable e) {
                        System.out.println("Command failed with error: "
                                        + e.getMessage());
                        if (cl.hasOption("v")) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            else {
                if (cmdName != null && cmdName.length() > 0) {
                    System.out.println(cmdName + ": command not found");
                }
            }
        }
    }

    private static CommandLine parse(Command cmd, String[] args) {
        Options opts = cmd.getOptions();
        CommandLine retval = null;
        try {
            retval = parser.parse(opts, args);
        }
        catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        return retval;
    }
    
    private Completer initCompleters(Environment env){
        // create completers
        ArrayList<Completer> completers = new ArrayList<Completer>();
        for (String cmdName : env.commandList()) {
            // command name
            StringsCompleter sc = new StringsCompleter(cmdName);

            ArrayList<Completer> cmdCompleters = new ArrayList<Completer>();
            // add a completer for the command name
            cmdCompleters.add(sc);
            // add the completer for the command
            cmdCompleters.add(env.getCommand(cmdName).getCompleter());
            // add a terminator for the command
            // cmdCompleters.add(new NullCompleter());

            ArgumentCompleter ac = new ArgumentCompleter(cmdCompleters);
            completers.add(ac);
        }

        AggregateCompleter aggComp = new AggregateCompleter(completers);
        
        return aggComp;
    }

    private History initHistory() throws IOException {
        File dir = new File(System.getProperty("user.home"), "."
                        + this.getName());
        if (dir.exists() && dir.isFile()) {
            throw new IllegalStateException(
                            "Default configuration file exists and is not a directory: "
                                            + dir.getAbsolutePath());
        }
        else if (!dir.exists()) {
            dir.mkdir();
        }
        // directory created, touch history file
        File histFile = new File(dir, "history");
        if (!histFile.exists()) {
            if (!histFile.createNewFile()) {
                throw new IllegalStateException(
                                "Unable to create history file: "
                                                + histFile.getAbsolutePath());
            }
        }

        final FileHistory hist = new FileHistory(histFile);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    hist.flush();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });

        return hist;

    }

    public abstract Module initialize(final PeekingIterator<String> iterator) throws Exception;

    public abstract String getName();

}
