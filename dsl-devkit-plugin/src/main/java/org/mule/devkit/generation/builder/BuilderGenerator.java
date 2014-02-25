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
import org.mule.devkit.model.Field;
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
import org.mule.devkit.model.module.ModuleKind;
import org.mule.devkit.model.module.ProcessorMethod;
import org.mule.devkit.model.module.SourceMethod;
import org.mule.devkit.model.module.connectivity.ManagedConnectionModule;
import org.mule.devkit.model.module.oauth.OAuthModule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.apache.commons.lang.StringUtils;


public class BuilderGenerator implements ModuleGenerator
{


    private final static List<Product> CONSUMES = Arrays.asList(Product.CONNECTION_MANAGER, Product.OAUTH_MANAGER, Product.PROCESS_ADAPTER, Product.MESSAGE_PROCESSOR, Product.MESSAGE_SOURCE);
    private final static List<Product> PRODUCES = Collections.emptyList();
    private final static List<String> INTERNAL_PARAMETER = Arrays.asList(HttpCallback.class.getName(), SourceCallback.class.getName(), MuleMessage.class.getName(), MuleEvent.class.getName());
    public static final String CONFIG_REF_VARIABLE_NAME = "configRef";
    public static final String BUILDER_PACKAGE = "org.mule.module.builder";
    public static final String BUILDER_NAME = "Builder";
    public static final String CONTEXT_ARG_NAME = "context";
    public static final String CREATE_METHOD_NAME = "create";
    public static final String RESULT_VARIABLE_NAME = "result";
    public static final String CONFIG_POSTFIX = "Config";

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
        //Separate this 3 methods into 3 classes to make it more clear
        generateConfig(module, moduleFactoryClass);
        generateMessageProcessorsBuilders(module, moduleFactoryClass);
        generateMessageSourcesBuilders(module, moduleFactoryClass);


    }

    private void generateConfig(Module module, GeneratedClass moduleFactoryClass)
    {
        GeneratedClass configClass;
        if (module instanceof ManagedConnectionModule)
        {
            // Config
            configClass = ctx().getProduct(Product.CONNECTION_MANAGER, module);
        }
        else if (module instanceof OAuthModule)
        {
            configClass = ctx().getProduct(Product.OAUTH_MANAGER, module);
        }
        else
        {
            configClass = ctx().getProduct(Product.PROCESS_ADAPTER, module);
        }

        final GeneratedClass configBuilderClass = createClass(BUILDER_PACKAGE, NameUtils.camel(module.getName()) + CONFIG_POSTFIX + BUILDER_NAME);
        configBuilderClass._implements(ref(Builder.class).narrow(configClass));
        final GeneratedMethod createMethod = configBuilderClass.method(Modifier.PUBLIC, configClass, CREATE_METHOD_NAME);
        createMethod.param(MuleContext.class, CONTEXT_ARG_NAME);
        final GeneratedBlock createMethodBlock = createMethod.body();
        final GeneratedVariable resultVariable = createMethodBlock.decl(configClass, RESULT_VARIABLE_NAME);
        createMethodBlock.assign(resultVariable, ExpressionFactory._new(configClass));

        final List<Field> configurableFields = module.getConfigurableFields();
        for (Field configurableField : configurableFields)
        {
            String fieldName = configurableField.getName();
            addField(fieldName, ref(configurableField.asTypeMirror()), configBuilderClass, configurableField.getDefaultValue());
            createMethodBlock.invoke(resultVariable, configurableField.getSetter().getName()).arg(ExpressionFactory.ref(fieldName));

        }
        createMethodBlock._return(resultVariable);

        GeneratedMethod processorFactoryMethod = moduleFactoryClass.method(Modifier.STATIC | Modifier.PUBLIC, configBuilderClass, module.getName() + CONFIG_POSTFIX);
        processorFactoryMethod.body()._return(ExpressionFactory._new(configBuilderClass));

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

    private void createBuilder(GeneratedClass moduleFactoryClass, Method<Type> processorMethod, org.mule.devkit.model.code.Type messageProcessorClass, GeneratedClass processorBuilderClass)
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

        addField(CONFIG_REF_VARIABLE_NAME, String.class, processorBuilderClass, null);

        GeneratedFieldReference configRefVariableRef = ExpressionFactory.ref(CONFIG_REF_VARIABLE_NAME);
        //context.getRegistry().lookup(configRef);
        GeneratedInvocation lookupObject = ExpressionFactory.ref(CONTEXT_ARG_NAME).invoke("getRegistry").invoke("lookupObject").arg(configRefVariableRef);
        createMethodBlock.invoke(resultVariable, "setModuleObject").arg(lookupObject);

        createMethodBlock._return(resultVariable);
        String name = processorMethod.getName();
        GeneratedMethod processorFactoryMethod = moduleFactoryClass.method(Modifier.STATIC | Modifier.PUBLIC, processorBuilderClass, name);
        processorFactoryMethod.body()._return(ExpressionFactory._new(processorBuilderClass));
    }

    private void addField(String fieldName, Class<?> type, GeneratedClass processorBuilderClass, String defaultValue)
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

    private void addField(String fieldName, org.mule.devkit.model.code.Type type, GeneratedClass processorBuilderClass, String defaultValue)
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

    public String getMessageProcessorClassName(Module module, ProcessorMethod processorMethod)
    {

        return ((GeneratedClass) ctx().getProduct(Product.MESSAGE_PROCESSOR, module, processorMethod.getName())).fullName();
    }

    public String getMessageSourceClassName(Module module, SourceMethod sourceMethod)
    {

        return ((GeneratedClass) ctx().getProduct(Product.MESSAGE_SOURCE, module, sourceMethod.getName())).fullName();
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
