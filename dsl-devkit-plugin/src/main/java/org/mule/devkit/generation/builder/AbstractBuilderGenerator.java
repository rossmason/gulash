package org.mule.devkit.generation.builder;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.callback.HttpCallback;
import org.mule.api.callback.SourceCallback;
import org.mule.devkit.generation.api.Context;
import org.mule.devkit.generation.api.ModuleGenerator;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.model.Method;
import org.mule.devkit.model.Parameter;
import org.mule.devkit.model.Type;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.GeneratedBlock;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedFieldReference;
import org.mule.devkit.model.code.GeneratedInvocation;
import org.mule.devkit.model.code.GeneratedMethod;
import org.mule.devkit.model.code.GeneratedPackage;
import org.mule.devkit.model.code.GeneratedVariable;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.code.TypeReference;
import org.mule.devkit.model.code.builders.FieldBuilder;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.ProcessorMethod;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.apache.commons.lang.StringUtils;

/**
 * Created by machaval on 2/25/14.
 */
public abstract class AbstractBuilderGenerator implements ModuleGenerator
{

    public static final String BUILDER_PACKAGE = "org.mule.module.builder";
    public static final String CONTEXT_ARG_NAME = "context";
    public static final String CREATE_METHOD_NAME = "create";
    public static final String RESULT_VARIABLE_NAME = "result";
    public static final String CONFIG_REF_VARIABLE_NAME = "configName";
    public static final String BUILDER_NAME = "Builder";

    private final static List<String> INTERNAL_PARAMETER = Arrays.asList(HttpCallback.class.getName(), SourceCallback.class.getName(), MuleMessage.class.getName(), MuleEvent.class.getName());

    protected final GeneratedClass moduleFactoryClass;
    private Context context;

    public AbstractBuilderGenerator(GeneratedClass moduleFactoryClass)
    {
        this.moduleFactoryClass = moduleFactoryClass;
    }

    @Override
    public boolean shouldGenerate(Module module)
    {
        return false;
    }

    public GeneratedClass getModuleFactoryClass()
    {
        return moduleFactoryClass;
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


    protected void createBuilder(GeneratedClass moduleFactoryClass, Method<Type> processorMethod, org.mule.devkit.model.code.Type messageProcessorClass, GeneratedClass processorBuilderClass)
    {
        final GeneratedMethod createMethod = processorBuilderClass.method(Modifier.PUBLIC, messageProcessorClass, CREATE_METHOD_NAME);
        createMethod.param(MuleContext.class, CONTEXT_ARG_NAME);
        final GeneratedBlock createMethodBlock = createMethod.body();
        final GeneratedVariable resultVariable = createMethodBlock.decl(messageProcessorClass, RESULT_VARIABLE_NAME);
        createMethodBlock.assign(resultVariable, ExpressionFactory._new(messageProcessorClass).arg(processorMethod.getName()));

        for (Parameter<Method<Type>> variable : processorMethod.getParameters())
        {
            final String fieldName = variable.getName();

            if (!isInternalParameter(variable))
            {
                final String defaultValue = variable.getDefaultValue();
                addField(fieldName, Object.class, processorBuilderClass, defaultValue);
                createMethodBlock.invoke(resultVariable, "set" + StringUtils.capitalize(fieldName)).arg(ExpressionFactory.ref(fieldName));

            }
        }


        new FieldBuilder(processorBuilderClass)
                .name(CONFIG_REF_VARIABLE_NAME)
                .type(String.class)
                .privateVisibility().build();

        final GeneratedMethod fieldExpressionMethod = processorBuilderClass.method(Modifier.PUBLIC, processorBuilderClass, "with");
        fieldExpressionMethod.param(String.class, CONFIG_REF_VARIABLE_NAME);
        GeneratedBlock fieldMethodBody = fieldExpressionMethod.body();
        fieldMethodBody.assign(ExpressionFactory.refthis(CONFIG_REF_VARIABLE_NAME), ExpressionFactory.ref(CONFIG_REF_VARIABLE_NAME));
        fieldMethodBody._return(ExpressionFactory._this());


        GeneratedFieldReference configRefVariableRef = ExpressionFactory.ref(CONFIG_REF_VARIABLE_NAME);
        //context.getRegistry().lookup(configRef);
        GeneratedInvocation lookupObject = ExpressionFactory.ref(CONTEXT_ARG_NAME).invoke("getRegistry").invoke("lookupObject").arg(configRefVariableRef);
        createMethodBlock.invoke(resultVariable, "setModuleObject").arg(lookupObject);

        createMethodBlock._return(resultVariable);
        String name = processorMethod.getName();
        GeneratedMethod processorFactoryMethod = moduleFactoryClass.method(Modifier.STATIC | Modifier.PUBLIC, processorBuilderClass, name);
        processorFactoryMethod.body()._return(ExpressionFactory._new(processorBuilderClass));
    }

    protected void addField(String fieldName, Class<?> type, GeneratedClass processorBuilderClass, String defaultValue)
    {
        new FieldBuilder(processorBuilderClass)
                .name(fieldName)
                .type(type)
                .privateVisibility()
                .initialValue(defaultValue).build();
        //TODO Replace with two methods one for expression and one with the real type
        final GeneratedMethod fieldExpressionMethod = processorBuilderClass.method(Modifier.PUBLIC, processorBuilderClass, fieldName);
        fieldExpressionMethod.param(type, fieldName);
        GeneratedBlock fieldMethodBody = fieldExpressionMethod.body();
        fieldMethodBody.assign(ExpressionFactory.refthis(fieldName), ExpressionFactory.ref(fieldName));
        fieldMethodBody._return(ExpressionFactory._this());
    }

    protected void addField(String fieldName, org.mule.devkit.model.code.Type type, GeneratedClass processorBuilderClass, String defaultValue)
    {
        new FieldBuilder(processorBuilderClass)
                .name(fieldName)
                .type(type)
                .privateVisibility()
                .initialValue(defaultValue).build();
        //TODO Replace with two methods one for expression and one with the real type
        final GeneratedMethod fieldExpressionMethod = processorBuilderClass.method(Modifier.PUBLIC, processorBuilderClass, fieldName);
        fieldExpressionMethod.param(type, fieldName);
        GeneratedBlock fieldMethodBody = fieldExpressionMethod.body();
        fieldMethodBody.assign(ExpressionFactory.refthis(fieldName), ExpressionFactory.ref(fieldName));
        fieldMethodBody._return(ExpressionFactory._this());
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

    protected GeneratedClass createClass(String packageName, String className)
    {
        GeneratedPackage pkg = ctx().getCodeModel()._package(packageName);
        GeneratedClass clazz;
        clazz = pkg._class(className);
        return clazz;
    }

    @Override
    public List<Product> consumes()
    {
        return null;
    }

    @Override
    public List<Product> produces()
    {
        return null;
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
