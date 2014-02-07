package org.mule.module.raspberrypi.processor;


import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.processor.MessageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CommandLineMessageProcessor implements MessageProcessor, MuleContextAware
{

    private String command;
    private List<String> arguments;
    private MuleContext context;
    private String path;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        String commandToRun = command;

        if (command.startsWith("#["))
        {
            commandToRun = String.valueOf(context.getExpressionManager().evaluate(command, event));
        }
        List<String> argumentsToRun = new ArrayList<String>();
        for (String argument : arguments)
        {
            if (argument.startsWith("#["))
            {
                argumentsToRun.add(String.valueOf(context.getExpressionManager().evaluate(argument, event)));
            }
            else
            {
                argumentsToRun.add(argument);
            }
        }

        Process exec;
        try
        {
            File directory = null;
            if (StringUtils.isEmpty(path))
            {
                directory = new File(path);
            }
            exec = Runtime.getRuntime().exec(commandToRun, argumentsToRun.toArray(new String[argumentsToRun.size()]), directory);
            int status = exec.waitFor();
            event.getMessage().setPayload(status);
            return event;
        }
        catch (IOException e)
        {
            throw new DefaultMuleException(e);
        }
        catch (InterruptedException e)
        {
            throw new DefaultMuleException(e);
        }

    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setArguments(List<String> arguments)
    {
        this.arguments = arguments;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    @Override
    public void setMuleContext(MuleContext context)
    {

        this.context = context;
    }
}
