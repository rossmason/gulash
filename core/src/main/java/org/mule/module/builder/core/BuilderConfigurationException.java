package org.mule.module.builder.core;

/**
 * Created by machaval on 2/19/14.
 */
public class BuilderConfigurationException extends RuntimeException
{

    public BuilderConfigurationException(String message)
    {
        super(message);
    }

    public BuilderConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
