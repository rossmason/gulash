package org.mule.module.builder.core;

import groovy.lang.Closure;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;

import java.util.List;

class GroovyMessageProcessor implements MessageProcessor {

    private Closure closure;
    private List params;

    public GroovyMessageProcessor(groovy.lang.Closure closure, java.util.List params){
        this.closure = closure;
        this.params = params;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {

        if (closure.getParameterTypes().length >= 1
                && closure.getParameterTypes()[0].isAssignableFrom(MuleEvent.class)){
            Object[] eventParam = {event};
            closure = closure.curry(eventParam);
        }

        //here should be evaluate and transform code for params
        if (params.size() > 0) {
            closure = closure.curry(params.toArray());
            event.getMessage().setPayload(closure.call());
        } else {
            event.getMessage().setPayload(closure.call());
        }


        return event;
    }
}
