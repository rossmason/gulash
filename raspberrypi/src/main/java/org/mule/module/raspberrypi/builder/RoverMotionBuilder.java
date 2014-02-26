package org.mule.module.raspberrypi.builder;

import org.mule.api.MuleContext;
import org.mule.module.core.builder.MessageProcessorBuilder;
import org.mule.module.raspberrypi.processor.RoverMotionMessageProcessor;
import org.mule.module.raspberrypi.rover.Direction;

/**
 * Created by machaval on 2/18/14.
 */
public class RoverMotionBuilder implements MessageProcessorBuilder<RoverMotionMessageProcessor>
{


    private Direction direction;

    public RoverMotionBuilder()
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
    public RoverMotionMessageProcessor create(MuleContext muleContext)
    {
        return new RoverMotionMessageProcessor(direction);
    }
}
