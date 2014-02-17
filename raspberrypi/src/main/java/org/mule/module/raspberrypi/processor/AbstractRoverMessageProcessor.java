package org.mule.module.raspberrypi.processor;

import org.mule.api.MuleException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.raspberrypi.rover.Rover;

public abstract class AbstractRoverMessageProcessor implements MessageProcessor, Startable, Stoppable
{

    protected Rover rover;

    public Rover getRover()
    {
        return rover;
    }

    public void setRover(Rover rover)
    {
        this.rover = rover;
    }

    @Override
    public void start() throws MuleException
    {
        getRover().startEngine();
    }

    @Override
    public void stop() throws MuleException
    {
        getRover().stopEngine();
    }
}
