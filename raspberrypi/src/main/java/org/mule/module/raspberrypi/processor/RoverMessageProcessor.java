package org.mule.module.raspberrypi.processor;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.raspberrypi.rover.Rover;
import org.mule.module.raspberrypi.rover.State;


public class RoverMessageProcessor implements MessageProcessor, Startable, Stoppable
{

    private Rover rover;
    private State state;


    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        state.execute(rover);
        return event;
    }

    public Rover getRover()
    {
        return rover;
    }

    public void setState(State state)
    {
        this.state = state;
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