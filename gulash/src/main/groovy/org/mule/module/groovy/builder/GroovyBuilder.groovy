package org.mule.module.groovy.builder

import org.mule.api.MuleContext
import org.mule.config.dsl.Builder
import org.mule.module.groovy.processor.GroovyMessageProcessor

class GroovyBuilder implements Builder<GroovyMessageProcessor>
{


    private Closure closure;

    GroovyBuilder(Closure closure)
    {
        this.closure = closure;
    }

    @Override
    GroovyMessageProcessor create(MuleContext muleContext)
    {
        return new GroovyMessageProcessor(this.closure);
    }


}
