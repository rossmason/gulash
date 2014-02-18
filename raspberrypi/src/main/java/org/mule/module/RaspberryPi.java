package org.mule.module;

import org.mule.module.raspberrypi.builder.CommandLineBuilder;
import org.mule.module.raspberrypi.builder.RoverMotionBuilder;

/**
 * Created by machaval on 2/17/14.
 */
public class RaspberryPi
{

    public static CommandLineBuilder exec(String command)
    {
        return new CommandLineBuilder(command);
    }


    public static RoverMotionBuilder rover()
    {
        return new RoverMotionBuilder();
    }

}
