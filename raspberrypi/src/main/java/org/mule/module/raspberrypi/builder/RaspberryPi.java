package org.mule.module.raspberrypi.builder;

/**
 * Created by machaval on 2/17/14.
 */
public class RaspberryPi
{

    public static CommandLineBuilder exec(String command)
    {
        return new CommandLineBuilder(command);
    }
}
