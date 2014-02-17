package org.mule.module.raspberrypi.rover;

import static org.mule.dsl.builder.apikit.Apikit.api;
import static org.mule.dsl.builder.apikit.Apikit.request;
import static org.mule.dsl.builder.core.Core.custom;
import static org.mule.dsl.builder.core.Core.logger;
import static org.mule.dsl.builder.core.Properties.properties;

import org.mule.dsl.builder.core.FlowBuilder;
import org.mule.dsl.builder.core.Mule;
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
                        .when(requestFlow(Direction.FORWARD))
                        .when(requestFlow(Direction.BACKWARDS))
                        .when(requestFlow(Direction.LEFT))
                        .when(requestFlow(Direction.RIGHT))
                        .when(requestFlow(Direction.STOP))
                        .when(request("/motion/status", ActionType.PUT)
                                      .chain(logger().withMessage("#[payload]"))
                                      .chain(custom(RoverStatusMessageProcessor.class).using(properties("rover", rover))))
        ).start();
    }

    private FlowBuilder requestFlow(Direction direction)
    {
        return request("/motion/" + direction.name().toLowerCase(), ActionType.PUT)
                .chain(logger().withMessage("#[payload]"))
                .chain(custom(RoverMotionMessageProcessor.class).using(properties("rover", rover, "direction", direction)));
    }
}
