package org.mule.module.raspberrypi.processor;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.raspberrypi.rover.Rover;

public abstract class AbstractRoverMessageProcessor implements MessageProcessor, Startable, Stoppable, MuleContextAware
{

    protected Rover rover;
    private MuleContext context;

    @Override
    public void setMuleContext(MuleContext context)
    {

        this.context = context;
    }

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
        if (getRover() == null) //if not set look for one in the context
        {
            setRover(context.getRegistry().lookupObject(Rover.class));
        }
        getRover().startEngine();
    }

    @Override
    public void stop() throws MuleException
    {
        getRover().stopEngine();
    }
}
