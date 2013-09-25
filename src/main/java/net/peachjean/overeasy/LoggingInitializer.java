package net.peachjean.overeasy;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import org.slf4j.LoggerFactory;

class LoggingInitializer
{
	static void initLogging(File shellConfigDir)
	{
		File loggingConfig = new File(shellConfigDir, "logback-config.xml");
		if(!loggingConfig.exists())
		{
			final URL resource = Resources.getResource(LoggingInitializer.class, "logback-config-template.xml");
			if(resource == null)
			{
				throw new IllegalStateException("Could not locate logging config template.");
			}
			try
			{
				String template = Resources.toString(resource, Charsets.UTF_8);
				final String config = template.replaceAll("@@SHELL_DIR@@", shellConfigDir.getAbsolutePath());

				Files.write(config, loggingConfig, Charsets.UTF_8);
			}
			catch (IOException e)
			{
				throw new RuntimeException("Could not load config template from " + resource, e);
			}
		}

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			// Call context.reset() to clear any previous configuration, e.g. default
			// configuration. For multi-step configuration, omit calling context.reset().
			loggerContext.reset();
			configurator.doConfigure(loggingConfig);
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
	}
}
