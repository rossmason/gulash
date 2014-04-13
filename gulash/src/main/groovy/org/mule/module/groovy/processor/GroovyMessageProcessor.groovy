package org.mule.module.groovy.processor

import org.mule.api.MuleEvent
import org.mule.api.MuleException
import org.mule.api.processor.MessageProcessor

public class GroovyMessageProcessor implements MessageProcessor
{

    private Closure closure;


    GroovyMessageProcessor(Closure closure)
    {
        this.closure = closure;
    }

    @Override
    MuleEvent process(MuleEvent event) throws MuleException
    {


        def call = closure.call(event.getMessage());
        if (call != null)
        {
            event.getMessage().payload = call
        }
        return event;
    }
}
