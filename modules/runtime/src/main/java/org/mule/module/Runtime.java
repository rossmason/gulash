package org.mule.module;

import org.mule.module.runtime.builder.CommandLineBuilder;


public class Runtime
{

    public static CommandLineBuilder exec(String command)
    {
        return new CommandLineBuilder(command);
    }

}
