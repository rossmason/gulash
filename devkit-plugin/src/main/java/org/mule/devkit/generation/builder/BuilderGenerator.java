package org.mule.devkit.generation.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.callback.HttpCallback;
import org.mule.api.callback.SourceCallback;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.dsl.Builder;
import org.mule.devkit.generation.api.Context;
import org.mule.devkit.generation.api.GenerationException;
import org.mule.devkit.generation.api.ModuleGenerator;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.generation.utils.NameUtils;
import org.mule.devkit.model.Method;
import org.mule.devkit.model.Parameter;
import org.mule.devkit.model.Type;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.GeneratedBlock;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedField;
import org.mule.devkit.model.code.GeneratedMethod;
import org.mule.devkit.model.code.GeneratedPackage;
import org.mule.devkit.model.code.GeneratedVariable;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.code.TypeReference;
import org.mule.devkit.model.code.builders.FieldBuilder;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.ModuleKind;
import org.mule.devkit.model.module.ProcessorMethod;
import org.mule.devkit.generation.NamingConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BuilderGenerator implements ModuleGenerator
{


    private final static List<Product> CONSUMES = Arrays.asList(Product.MESSAGE_PROCESSOR, Product.MESSAGE_SOURCE);
    private final static List<Product> PRODUCES = Collections.emptyList();
    private final static List<String> INTERNAL_PARAMETER = Arrays.asList(HttpCallback.class.getName(), SourceCallback.class.getName(), MuleMessage.class.getName(), MuleEvent.class.getName());

    private Context context;

    @Override
    public boolean shouldGenerate(Module module)
    {
        return (module.getKind() == ModuleKind.CONNECTOR || module.getKind() == ModuleKind.GENERIC);
    }

    @Override
    public void generate(Module module) throws GenerationException
    {
        final String className = NameUtils.camel(module.getModuleName());
        final GeneratedClass moduleFactoryClass = createClass("org.mule.module", className);

        generateBuilder(module, moduleFactoryClass);


        //generateGlobalConfig

    }

    void generateBuilder(Module module, GeneratedClass moduleFactoryClass)
    {
        final List<ProcessorMethod> processorMethods = module.getProcessorMethods();
        for (ProcessorMethod processorMethod : processorMethods)
        {
            org.mule.devkit.model.code.Type messageProcessorClass = ref(getMessageProcessorClassName(module, processorMethod));
            final GeneratedClass processorBuilderClass = createClass("org.mule.module.builder", NameUtils.friendlyNameFromCamelCase(processorMethod.getCapitalizedName()));
            processorBuilderClass._extends(ref(Builder.class).narrow(messageProcessorClass));
            createBuilder(moduleFactoryClass, processorMethod, messageProcessorClass, processorBuilderClass);
        }
    }

    private void createBuilder(GeneratedClass moduleFactoryClass, Method<Type> processorMethod, org.mule.devkit.model.code.Type messageProcessorClass, GeneratedClass processorBuilderClass)
    {
        final GeneratedMethod createMethod = processorBuilderClass.method(Modifier.PUBLIC, MessageProcessor.class, "create");
        createMethod.param(MuleContext.class, "context");
        final GeneratedBlock createMethodBlock = createMethod.body();
        final GeneratedVariable resultVariable = createMethodBlock.decl(messageProcessorClass, "result");
        createMethodBlock.assign(resultVariable, ExpressionFactory._new(messageProcessorClass).arg(processorMethod.getName()));

        for (Parameter<Method<Type>> variable : processorMethod.getParameters())
        {
            final String fieldName = variable.getName();

            if (!isInternalParameter(variable))
            {
                final String defaultValue = variable.getDefaultValue();
                new FieldBuilder(processorBuilderClass)
                        .name(fieldName)
                        .type(Object.class)
                        .privateVisibility()
                        .initialValue(defaultValue).build();
                //TODO Replace with two methods one for expression and one with the real type
                final GeneratedMethod fieldExpressionMethod = processorBuilderClass.method(Modifier.PUBLIC, processorBuilderClass.elementType(), fieldName);
                fieldExpressionMethod.param(Object.class, fieldName);
                fieldExpressionMethod.body().assign(ExpressionFactory.refthis(fieldName), ExpressionFactory.ref(fieldName));
                createMethodBlock.invoke(resultVariable, "set" + NameUtils.camel(fieldName)).arg(fieldName);

            }
        }

        createMethodBlock._return(resultVariable);
        String name = processorMethod.getName();
        GeneratedMethod processorFactoryMethod = moduleFactoryClass.method(Modifier.STATIC | Modifier.PUBLIC, processorBuilderClass.elementType(), name);
        processorFactoryMethod.body()._return(ExpressionFactory._new(processorBuilderClass.elementType()));
    }

    public String getMessageProcessorClassName(Module module, ProcessorMethod processorMethod)
    {

        return module.getPackageName() + NamingConstants.MESSAGE_PROCESSOR_NAMESPACE + "." + processorMethod.getCapitalizedName() + NamingConstants.MESSAGE_PROCESSOR_CLASS_NAME_SUFFIX;
    }

    public org.mule.devkit.model.code.Type ref(TypeMirror typeMirror)
    {
        return ctx().getCodeModel().ref(typeMirror);
    }

    public TypeReference ref(Class<?> clazz)
    {
        return ctx().getCodeModel().ref(clazz);
    }

    public org.mule.devkit.model.code.Type ref(String fullyQualifiedClassName)
    {
        return ctx().getCodeModel().ref(fullyQualifiedClassName);
    }

    protected GeneratedField generateLoggerField(GeneratedClass clazz)
    {
        return clazz.field(Modifier.PRIVATE | Modifier.STATIC, ref(Logger.class), "logger",
                           ref(LoggerFactory.class).staticInvoke("getLogger").arg(clazz.dotclass()));
    }


    private boolean isInternalParameter(Parameter variable)
    {
        if (variable.asType().isNestedProcessor() || (variable.asType().isArrayOrList() && variable.getTypeArguments().size() > 0 && variable.getTypeArguments().get(0).isNestedProcessor()))
        {
            return false;
        }
        String parameterType = variable.asTypeMirror().toString();
        for (String internalParameter : INTERNAL_PARAMETER)
        {
            if (parameterType.startsWith(internalParameter))
            {
                return true;
            }
        }
        return false;
    }

    private GeneratedClass createClass(String packageName, String className)
    {
        GeneratedPackage pkg = ctx().getCodeModel()._package(packageName);
        GeneratedClass clazz;
        clazz = pkg._class(className);
        return clazz;
    }

    @Override
    public List<Product> consumes()
    {
        return CONSUMES;
    }

    @Override
    public List<Product> produces()
    {
        return PRODUCES;
    }

    @Override
    public void setCtx(Context context)
    {

        this.context = context;
    }

    @Override
    public Context ctx()
    {
        return context;
    }
}
