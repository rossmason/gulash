package org.mule.module.core.builder;

import groovy.lang.Closure;

import org.mule.api.MuleContext;
import org.mule.module.core.processor.GroovyMessageProcessor;

import java.util.ArrayList;
import java.util.List;


public class GroovyBuilder implements MessageProcessorBuilder<GroovyMessageProcessor>
{


    private Closure closure;
    private List params;

    public GroovyBuilder(Closure closure)
    {
        this.closure = closure;
        params = new ArrayList();
    }

    @Override
    public GroovyMessageProcessor create(MuleContext muleContext)
    {
        return new GroovyMessageProcessor(this.closure, this.params);
    }

    public GroovyBuilder argument(Object param)
    {
        params.add(param);
        return this;
    }
}
