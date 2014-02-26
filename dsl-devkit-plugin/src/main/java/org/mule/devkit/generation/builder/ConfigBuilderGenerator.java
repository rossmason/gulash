package org.mule.devkit.generation.builder;

import org.mule.api.MuleContext;
import org.mule.config.dsl.Builder;
import org.mule.devkit.generation.api.GenerationException;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.generation.utils.NameUtils;
import org.mule.devkit.model.Field;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.GeneratedBlock;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedMethod;
import org.mule.devkit.model.code.GeneratedVariable;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.connectivity.ManagedConnectionModule;
import org.mule.devkit.model.module.oauth.OAuthModule;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * Created by machaval on 2/25/14.
 */
public class ConfigBuilderGenerator extends AbstractBuilderGenerator
{


    public static final String CONFIG_POSTFIX = "Config";

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
        createMethodBlock._return(resultVariable);


        GeneratedMethod processorFactoryMethod = moduleFactoryClass.method(Modifier.STATIC | Modifier.PUBLIC, configBuilderClass, StringUtils.uncapitalize(module.getModuleName()) + CONFIG_POSTFIX);
        processorFactoryMethod.body()._return(ExpressionFactory._new(configBuilderClass));

    }
}
