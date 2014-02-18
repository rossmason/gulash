package org.mule.module.raspberrypi.builder;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.dsl.builder.core.MessageProcessorBuilder;
import org.mule.module.raspberrypi.processor.CommandLineMessageProcessor;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Created by machaval on 2/9/14.
 */
public class CommandLineBuilder implements MessageProcessorBuilder<CommandLineMessageProcessor>
{

    private String command;
    private List<String> arguments;
    private String path;

    CommandLineBuilder(String command)
    {
        this.command = command;
    }

    public CommandLineBuilder usingArguments(String... arguments)
    {
        this.arguments = Arrays.asList(arguments);
        return this;
    }

    public CommandLineBuilder usingPath(String path)
    {
        this.path = path;
        return this;
    }

    @Override
    public CommandLineMessageProcessor build(MuleContext muleContext) throws ConfigurationException, IllegalStateException
    {
        CommandLineMessageProcessor commandLineMessageProcessor = new CommandLineMessageProcessor();
        if (!StringUtils.isEmpty(command))
        {
            commandLineMessageProcessor.setCommand(command);
        }
        else
        {
            throw new IllegalStateException("Command must be declared");
        }
        if (arguments != null && !arguments.isEmpty())
        {
            commandLineMessageProcessor.setArguments(arguments);
        }

        if (!StringUtils.isEmpty(path))
        {
            commandLineMessageProcessor.setPath(path);
        }
        return commandLineMessageProcessor;
    }
}
