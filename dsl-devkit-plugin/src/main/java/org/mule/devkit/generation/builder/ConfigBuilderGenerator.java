package org.mule.devkit.generation.builder;

import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.config.dsl.Builder;
import org.mule.devkit.generation.api.GenerationException;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.generation.utils.NameUtils;
import org.mule.devkit.model.Field;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.GeneratedBlock;
import org.mule.devkit.model.code.GeneratedCatchBlock;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedMethod;
import org.mule.devkit.model.code.GeneratedTry;
import org.mule.devkit.model.code.GeneratedVariable;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.code.TypeReference;
import org.mule.devkit.model.code.builders.FieldBuilder;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.connectivity.ManagedConnectionModule;
import org.mule.devkit.model.module.oauth.OAuthModule;

import java.util.List;

import org.apache.commons.lang.StringUtils;


public class ConfigBuilderGenerator extends AbstractBuilderGenerator
{


    public static final String CONFIG_POSTFIX = "Config";
    public static final String NAME_FIELD_NAME = "name";

    public ConfigBuilderGenerator(GeneratedClass moduleFactoryClass)
    {
        super(moduleFactoryClass);
    }

    @Override
    public void generate(Module module) throws GenerationException
    {
        generateConfig(module, getModuleFactoryClass());
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

        final GeneratedClass configBuilderClass = createClass(BUILDER_PACKAGE, NameUtils.camel(module.getModuleName()) + CONFIG_POSTFIX + BUILDER_NAME);
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

        new FieldBuilder(configBuilderClass)
                .name(NAME_FIELD_NAME)
                .type(String.class)
                .privateVisibility().build();

        final GeneratedMethod fieldExpressionMethod = configBuilderClass.method(Modifier.PUBLIC, configBuilderClass, "as");
        fieldExpressionMethod.param(String.class, NAME_FIELD_NAME);
        GeneratedBlock fieldMethodBody = fieldExpressionMethod.body();
        fieldMethodBody.assign(ExpressionFactory.refthis(NAME_FIELD_NAME), ExpressionFactory.ref(NAME_FIELD_NAME));
        fieldMethodBody._return(ExpressionFactory._this());


        //muleContext.getRegistry().registerObject(name, newInstance);
        GeneratedTry generatedTry = createMethodBlock._try();

        GeneratedVariable registry = generatedTry.body().decl(ref(MuleRegistry.class), "registry");
        generatedTry.body().assign(registry, ExpressionFactory.invoke(ExpressionFactory.ref(CONTEXT_ARG_NAME), "getRegistry"));
        generatedTry.body().invoke(registry, "registerObject").arg(ExpressionFactory.ref(NAME_FIELD_NAME)).arg(resultVariable);
        GeneratedCatchBlock generatedCatchBlock = generatedTry._catch(ref(Exception.class));
        generatedCatchBlock.param("e");
        createMethodBlock._return(resultVariable);

        //Static Factory method for builder
        GeneratedMethod processorFactoryMethod = moduleFactoryClass.method(Modifier.STATIC | Modifier.PUBLIC, configBuilderClass, StringUtils.uncapitalize(module.getModuleName()) + CONFIG_POSTFIX);
        processorFactoryMethod.body()._return(ExpressionFactory._new(configBuilderClass));

    }
}
