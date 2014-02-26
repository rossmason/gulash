package org.mule.devkit.generation.builder;

import org.mule.config.dsl.Builder;
import org.mule.devkit.generation.api.GenerationException;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.SourceMethod;

import java.util.List;

/**
 * Created by machaval on 2/25/14.
 */
public class MessageSourceBuilderGenerator extends AbstractBuilderGenerator
{

    public MessageSourceBuilderGenerator(GeneratedClass moduleFactoryClass)
    {
        super(moduleFactoryClass);
    }

    @Override
    public void generate(Module module) throws GenerationException
    {
        generateMessageSourcesBuilders(module, getModuleFactoryClass());
    }

    void generateMessageSourcesBuilders(Module module, GeneratedClass moduleFactoryClass)
    {
        final List<SourceMethod> sourceMethods = module.getSourceMethods();
        for (SourceMethod sourceMethod : sourceMethods)
        {
            org.mule.devkit.model.code.Type messageProcessorClass = ref(getMessageSourceClassName(module, sourceMethod));
            final GeneratedClass processorBuilderClass = createClass(BUILDER_PACKAGE, sourceMethod.getCapitalizedName() + BUILDER_NAME);
            processorBuilderClass._implements(ref(Builder.class).narrow(messageProcessorClass));
            createBuilder(moduleFactoryClass, sourceMethod, messageProcessorClass, processorBuilderClass);
        }
    }

    public String getMessageSourceClassName(Module module, SourceMethod sourceMethod)
    {

        return ((GeneratedClass) ctx().getProduct(Product.MESSAGE_SOURCE, module, sourceMethod.getName())).fullName();
    }
}
