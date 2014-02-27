package org.mule.module.runtime.builder;

import org.mule.api.MuleContext;
import org.mule.module.core.builder.MessageProcessorBuilder;
import org.mule.module.runtime.processor.CommandLineMessageProcessor;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class CommandLineBuilder implements MessageProcessorBuilder<CommandLineMessageProcessor>
{

    private String command;
    private List<String> arguments;
    private String path;

    public CommandLineBuilder(String command)
    {
        this.command = command;
    }

    public CommandLineBuilder arguments(String... arguments)
    {
        this.arguments = Arrays.asList(arguments);
        return this;
    }

    public CommandLineBuilder path(String path)
    {
        this.path = path;
        return this;
    }

    @Override
    public CommandLineMessageProcessor create(MuleContext muleContext)
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
