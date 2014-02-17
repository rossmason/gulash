package org.mule.module.raspberrypi.processor;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.module.raspberrypi.rover.Direction;


public class RoverMotionMessageProcessor extends AbstractRoverMessageProcessor
{

    private Direction direction;


    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        direction.execute(rover);
        return event;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

}