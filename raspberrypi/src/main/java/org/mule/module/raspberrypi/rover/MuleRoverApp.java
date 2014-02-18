package org.mule.module.raspberrypi.rover;

import static org.mule.dsl.builder.apikit.Apikit.api;
import static org.mule.dsl.builder.apikit.Apikit.request;
import static org.mule.dsl.builder.core.Core.invoke;
import static org.mule.dsl.builder.core.Core.log;
import static org.mule.dsl.builder.core.Properties.properties;

import org.mule.dsl.builder.core.Mule;
import org.mule.dsl.builder.core.PrivateFlowBuilder;
import org.mule.module.raspberrypi.processor.RoverMotionMessageProcessor;
import org.mule.module.raspberrypi.processor.RoverStatusMessageProcessor;

import org.raml.model.ActionType;

public class MuleRoverApp
{

    private Rover rover;

    private MuleRoverApp(Rover rover)
    {
        this.rover = rover;
    }

    public static void main(String[] args) throws Exception
    {
        new MuleRoverApp(new MockRover()).start();
    }

    private void start() throws Exception
    {
        Mule mule = new Mule();
        mule.declare(
                api("rover.raml")
                        .using(properties("consolePath", "/console", "name", "roverConfig"))
                        .on(requestFlow(Direction.FORWARD))
                        .on(requestFlow(Direction.BACKWARDS))
                        .on(requestFlow(Direction.LEFT))
                        .on(requestFlow(Direction.RIGHT))
                        .on(requestFlow(Direction.STOP))
                        .on(request("/motion/status", ActionType.PUT)
                                    .then(log("#[payload]"))
                                    .then(invoke(RoverStatusMessageProcessor.class).using(properties("rover", rover))))
        ).start();
    }

    private PrivateFlowBuilder requestFlow(Direction direction)
    {
        return request("/motion/" + direction.name().toLowerCase(), ActionType.PUT)
                .then(log("#[payload]") ,
                      invoke(RoverMotionMessageProcessor.class).using(properties("rover", rover, "direction", direction)));
    }
}
