package org.mule.module.runtime.processor;


import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.PropertyScope;
import org.mule.util.AttributeEvaluator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class CommandLineMessageProcessor implements MessageProcessor, MuleContextAware
{

    public static final String STATUS_PROPERTY_NAME = "STATUS";
    private String command;
    private List<String> arguments = new ArrayList<String>();
    private MuleContext context;
    private String path;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        String commandToRun;
        final AttributeEvaluator commandAttributeEvaluator = new AttributeEvaluator(command);
        commandAttributeEvaluator.initialize(context.getExpressionManager());
        if (commandAttributeEvaluator.isExpression())
        {
            commandToRun = String.valueOf(commandAttributeEvaluator.resolveValue(event.getMessage()));
        }
        else
        {
            commandToRun = commandAttributeEvaluator.getRawValue();
        }
        final List<String> argumentsToRun = new ArrayList<String>();
        for (String argument : arguments)
        {
            final AttributeEvaluator argumentAttributeEvaluator = new AttributeEvaluator(argument);
            argumentAttributeEvaluator.initialize(context.getExpressionManager());
            if (argumentAttributeEvaluator.isExpression())
            {
                argumentsToRun.add(String.valueOf(argumentAttributeEvaluator.resolveValue(event.getMessage())));
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
            if (!StringUtils.isEmpty(path))
            {
                AttributeEvaluator pathAttributeEvaluator = new AttributeEvaluator(path);
                pathAttributeEvaluator.initialize(context.getExpressionManager());
                if (pathAttributeEvaluator.isExpression())
                {
                    directory = new File(String.valueOf((pathAttributeEvaluator.resolveValue(event.getMessage()))));
                }
                else
                {
                    directory = new File(path);
                }
            }
            exec = Runtime.getRuntime().exec(commandToRun, argumentsToRun.toArray(new String[argumentsToRun.size()]), directory);
            final int status = exec.waitFor();
            final String consoleOutput = IOUtils.toString(exec.getInputStream());
            event.getMessage().setPayload(consoleOutput);
            event.getMessage().setProperty(STATUS_PROPERTY_NAME, status, PropertyScope.INVOCATION);
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
