// Copyright (c) 2012 Health Market Science, Inc.

package net.peachjean.overeasy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import jline.console.history.FileHistory;
import jline.console.history.History;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.peachjean.overeasy.command.Command;
import net.peachjean.overeasy.commands.DefaultCommandsModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public abstract class AbstractShell {
	private static final String HELP_HEADER = "If a command is given, "
	                                          + "the command will be run and the shell will exit. Otherwise, "
	                                          + "an interactive shell will be entered.";
	private static final String HELP_USAGE = "[OPTIONS] [command [command options]]";

	private static final CommandLineParser parser = new PosixParser();
	private static final LineTokenizer lineTokenizer = new LineTokenizer();
	private static final Logger logger = LoggerFactory.getLogger(AbstractShell.class);

	private static final ShellHelpFormatter shellHelpFormatter = new ShellHelpFormatter();

    public final void run(final String[] arguments) throws Exception {
	    final CommandLine shellCommandLine = parseShellCommandLine(arguments);

	    final Injector injector = createInjector(shellCommandLine);
        final Environment env = injector.getInstance(Environment.class);

	    if (shellCommandLine.hasOption("h"))
	    {
		    final HelpFormatter helpFormatter = new HelpFormatter();
		    final PrintWriter pw = new PrintWriter(System.err);
		    helpFormatter.printHelp(pw, helpFormatter.getWidth(), HELP_USAGE, HELP_HEADER,
		                            getShellOptions(), helpFormatter.getLeftPadding(),
		                            helpFormatter.getDescPadding(), null, false);
		    pw.println();
		    this.shellHelpFormatter.printGlobalHelp(env, pw);
		    pw.flush();
		    return;
	    }
	    // create reader and add completers
	    final ConsoleReader reader = new ConsoleReader(getName(), System.in, System.out, null);
        reader.setHandleUserInterrupt(true);

	    final boolean suppressSplashScreen = isSplashScreenSuppressed(shellCommandLine);
	    if(!suppressSplashScreen)
	    {
		    final SplashScreen ss = injector.getInstance(SplashScreen.class);
		    if(ss != null)
		    {
			    ss.render(reader);
		    }
	    }

	    final Prompt prompt = injector.getInstance(Prompt.class);

        reader.addCompleter(initCompleters(env));
        // add history support
        reader.setHistory(initHistory());

        AnsiConsole.systemInstall();

	    acceptCommands(reader, env, prompt);

    }

	private boolean isSplashScreenSuppressed(final CommandLine shellCommandLine)
	{
		return shellCommandLine.hasOption("sss");
	}

	private CommandLine parseShellCommandLine(final String[] arguments) throws ParseException
	{
		final Options options = getShellOptions();
		return parser.parse(options, arguments);
	}

	private Options getShellOptions()
	{
		final Options options = new Options();
		options.addOption("sss", "suppressSplashScreen", false, "When given, the splash screen is suppressed.");
		options.addOption("h", "help", false, "When given, shell-level help is displayed, rather than the shell.");
		provideOptions(options);
		return options;
	}

	private Injector createInjector(final CommandLine commandLine) throws Exception
	{
		final Module initialModule = this.initialize(commandLine);
		final Module defaultCommands = this.useDefaultCommands() ? new DefaultCommandsModule() : Modules.EMPTY_MODULE;
		return Guice.createInjector(Modules.override(new OverEasyBaseModule(), defaultCommands).with(initialModule));
	}

	protected boolean useDefaultCommands()
	{
		return true;
	}

	private void acceptCommands(final ConsoleReader reader, final Environment env, final Prompt prompt) throws IOException {
        final Iterator<String> lineIterator = new ConsoleLineIterator(reader, prompt);
        while (lineIterator.hasNext()) {
            final String line = lineIterator.next();
            final String[] argv = lineTokenizer.splitLine(line);
	        if(argv.length == 0) {
		        continue;
	        }
            final String cmdName = argv[0];

            final Command command = env.getCommand(cmdName);
            if (command != null) {
//                System.out.println("Running: " + command.getName() + " ("
//                                + command.getClass().getName() + ")");
                final String[] cmdArgs = Arrays.copyOfRange(argv, 1, argv.length);
                final CommandLine cl = parse(command, cmdArgs);
                if (cl != null) {
                    try {
                        command.execute(env, cl, reader);
                    }
                    catch (Throwable e) {
                        System.out.println("Command failed with error: "
                                        + e.getMessage());
	                    logger.error("Command << " + line + " >> failed.", e);
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
        reader.println();
    }

    private static CommandLine parse(final Command cmd, final String[] args) {
        final Options opts = cmd.getOptions();
	    final Options customOptions = new Options();
	    for(final Option option: (Iterable<Option>) opts.getOptions()) {
		    customOptions.addOption(option);
	    }
	    customOptions.addOption(new Option("v", "verbose", false, "verbose mode"));
        CommandLine retval = null;
        try {
            retval = parser.parse(opts, args);
        }
        catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        return retval;
    }

	private Completer initCompleters(final Environment env){
        // create completers
        final ArrayList<Completer> completers = new ArrayList<Completer>();
        for (final String cmdName : env.commandList()) {
            // command name
            final StringsCompleter sc = new StringsCompleter(cmdName);

            final ArrayList<Completer> cmdCompleters = new ArrayList<Completer>();
            // add a completer for the command name
            cmdCompleters.add(sc);
            // add the completer for the command
            cmdCompleters.add(env.getCommand(cmdName).getCompleter());
            // add a terminator for the command
            // cmdCompleters.add(new NullCompleter());

            final ArgumentCompleter ac = new ArgumentCompleter(cmdCompleters);
            completers.add(ac);
        }

        final AggregateCompleter aggComp = new AggregateCompleter(completers);

		return aggComp;
    }

    private History initHistory() throws IOException {
        final File dir = new File(System.getProperty("user.home"), "."
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
        final File histFile = new File(dir, "history");
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

    public abstract Module initialize(CommandLine commandLine) throws Exception;

    public abstract String getName();

	public abstract void provideOptions(Options options);
}
