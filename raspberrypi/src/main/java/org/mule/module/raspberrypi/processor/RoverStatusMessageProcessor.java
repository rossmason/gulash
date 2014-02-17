package org.mule.module.raspberrypi.processor;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;

public class RoverStatusMessageProcessor extends AbstractRoverMessageProcessor
{

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        event.getMessage().setPayload(String.valueOf(rover.getCurrentDirection()));
        return event;
    }
}
