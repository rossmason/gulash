package org.mule.module.raspberrypi.builder;

import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.module.builder.core.MessageProcessorBuilder;
import org.mule.module.raspberrypi.processor.RoverMotionMessageProcessor;
import org.mule.module.raspberrypi.rover.Direction;

/**
 * Created by machaval on 2/18/14.
 */
public class RoverMotionBuilder implements MessageProcessorBuilder<RoverMotionMessageProcessor>
{


    private Direction direction;

    RoverMotionBuilder()
    {
    }

    public RoverMotionBuilder turnLeft()
    {
        direction = Direction.LEFT;
        return this;
    }

    public RoverMotionBuilder turnRight()
    {
        direction = Direction.RIGHT;
        return this;
    }

    public RoverMotionBuilder forward()
    {
        direction = Direction.FORWARD;
        return this;
    }

    public RoverMotionBuilder backward()
    {
        direction = Direction.BACKWARDS;
        return this;
    }

    @Override
    public RoverMotionMessageProcessor build(MuleContext muleContext) throws ConfigurationException, IllegalStateException
    {
        return new RoverMotionMessageProcessor(direction);
    }
}
