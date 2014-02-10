package org.mule.module.raspberrypi.builder;


public class RaspberryPi
{

    public static CommandLineBuilder exec(String command)
    {
        return new CommandLineBuilder(command);
    }
}
