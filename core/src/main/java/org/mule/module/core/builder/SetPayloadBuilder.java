package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.transformer.simple.SetPayloadTransformer;

/**
 * Created by machaval on 2/26/14.
 */
public class SetPayloadBuilder implements MessageProcessorBuilder<SetPayloadTransformer>
{

    private String expression;

    public SetPayloadBuilder(String expression)
    {
        this.expression = expression;
    }


    @Override
    public SetPayloadTransformer create(MuleContext muleContext)
    {

        SetPayloadTransformer setPayloadTransformer = new SetPayloadTransformer();
        setPayloadTransformer.setValue(expression);
        return setPayloadTransformer;
    }
}
