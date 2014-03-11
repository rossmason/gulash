package org.mule.devkit.generation.builder.processor;

import org.mule.api.MuleContext;
import org.mule.config.dsl.Builder;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.generation.builder.AbstractBuilderGenerator;
import org.mule.devkit.model.Method;
import org.mule.devkit.model.Parameter;
import org.mule.devkit.model.Type;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.GeneratedBlock;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedFieldReference;
import org.mule.devkit.model.code.GeneratedInvocation;
import org.mule.devkit.model.code.GeneratedMethod;
import org.mule.devkit.model.code.GeneratedVariable;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.code.builders.FieldBuilder;
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