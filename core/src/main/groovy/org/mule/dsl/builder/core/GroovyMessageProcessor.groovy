package org.mule.dsl.builder.core

import org.mule.api.MuleEvent
import org.mule.api.MuleException
import org.mule.api.processor.MessageProcessor

/**
 * Created with IntelliJ IDEA.
 * User: ivan
 * Date: 2/13/14
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
class GroovyMessageProcessor implements MessageProcessor {

    def Closure closure
    def List params

    public GroovyMessageProcessor(Closure closure,List params){
        this.closure = closure;
        this.params = params;
    }

    @Override
    MuleEvent process(MuleEvent event) throws MuleException {

        if (closure.getParameterTypes().size() >= 1
                && closure.getParameterTypes()[0].isAssignableFrom(MuleEvent.class)){
            closure = closure.curry(event)
        }

        //here should be evaluate and transform code for params
        if (params.size() > 0) {
            for (param in params) {
                closure = closure.curry(param)
            }
            event.getMessage().setPayload(closure.call());
        } else {
            event.getMessage().setPayload(closure.call());
        }


        return event;
    }
}
