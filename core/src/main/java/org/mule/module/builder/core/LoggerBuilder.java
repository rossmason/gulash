package org.mule.module.builder.core;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.processor.LoggerMessageProcessor;

import org.apache.commons.lang.StringUtils;


public class LoggerBuilder implements MessageProcessorBuilder<LoggerMessageProcessor>
{

    private String message;
    private String level;

    public LoggerBuilder(String message)
    {
        this.message = message;
    }

    public LoggerBuilder as(String level)
    {
        this.level = level;
        return this;
    }


    @Override
    public LoggerMessageProcessor build(MuleContext muleContext) throws NullPointerException, ConfigurationException, IllegalStateException
    {
        LoggerMessageProcessor logger = new LoggerMessageProcessor();
        if (!StringUtils.isEmpty(message))
        {
            logger.setMessage(message);
        }
        if (!StringUtils.isEmpty(level))
        {
            logger.setLevel(level);
        }
        return logger;
    }
}
