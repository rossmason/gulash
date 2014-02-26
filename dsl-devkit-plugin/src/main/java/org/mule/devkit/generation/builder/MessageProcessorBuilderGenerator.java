package org.mule.devkit.generation.builder;

import org.mule.config.dsl.Builder;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.ProcessorMethod;

import java.util.List;


public class MessageProcessorBuilderGenerator extends AbstractBuilderGenerator
{

    public MessageProcessorBuilderGenerator(GeneratedClass moduleFactoryClass)
    {
        super(moduleFactoryClass);
    }

    @Override
    public void generate(Module module)
    {
        generateMessageProcessorsBuilders(module, getModuleFactoryClass());
    }

    void generateMessageProcessorsBuilders(Module module, GeneratedClass moduleFactoryClass)
    {
        final List<ProcessorMethod> processorMethods = module.getProcessorMethods();
        for (ProcessorMethod processorMethod : processorMethods)
        {
            org.mule.devkit.model.code.Type messageProcessorClass = ref(getMessageProcessorClassName(module, processorMethod));
            final GeneratedClass processorBuilderClass = createClass(BUILDER_PACKAGE, processorMethod.getCapitalizedName() + BUILDER_NAME);
            processorBuilderClass._implements(ref(Builder.class).narrow(messageProcessorClass));
            createBuilder(moduleFactoryClass, processorMethod, messageProcessorClass, processorBuilderClass);
        }
    }

    public String getMessageProcessorClassName(Module module, ProcessorMethod processorMethod)
    {

        return ((GeneratedClass) ctx().getProduct(Product.MESSAGE_PROCESSOR, module, processorMethod.getName())).fullName();
    }


}
