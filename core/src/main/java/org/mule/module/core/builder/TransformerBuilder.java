package org.mule.module.core.builder;

import org.mule.api.MuleContext;
import org.mule.api.transformer.Transformer;
import org.mule.transformer.AbstractTransformer;

public class TransformerBuilder implements MessageProcessorBuilder<Transformer>
{
    private TransformerBuilderImpl transformerBuilderImpl;
    private Class<? extends Transformer> transformerClass;

    public TransformerBuilder(Class<? extends AbstractTransformer> transformerClass)
    {
        this.transformerClass = transformerClass;
    }

    public TransformerBuilder(TransformerBuilderImpl transformerBuilderImpl)
    {
        this.transformerBuilderImpl = transformerBuilderImpl;
    }

    @Override
    public Transformer create(MuleContext muleContext)
    {
        if (transformerBuilderImpl != null) {
            return transformerBuilderImpl.create();
        }

        try
        {
            return transformerClass.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while instantiating the transformer", e);
        }
    }
}
