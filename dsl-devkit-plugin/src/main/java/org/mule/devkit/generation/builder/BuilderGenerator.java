package org.mule.devkit.generation.builder;

import org.mule.devkit.generation.api.Context;
import org.mule.devkit.generation.api.GenerationException;
import org.mule.devkit.generation.api.ModuleGenerator;
import org.mule.devkit.generation.api.Product;
import org.mule.devkit.generation.utils.NameUtils;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedPackage;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.ModuleKind;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BuilderGenerator implements ModuleGenerator
{


    private final static List<Product> CONSUMES = Arrays.asList(Product.CONNECTION_MANAGER, Product.OAUTH_MANAGER, Product.PROCESS_ADAPTER, Product.MESSAGE_PROCESSOR, Product.MESSAGE_SOURCE);
    private final static List<Product> PRODUCES = Collections.emptyList();

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
        //Config builder
        final ConfigBuilderGenerator configBuilderGenerator = new ConfigBuilderGenerator(moduleFactoryClass);
        configBuilderGenerator.setCtx(ctx());
        configBuilderGenerator.generate(module);

        final MessageProcessorBuilderGenerator messageProcessorBuilderGenerator = new MessageProcessorBuilderGenerator(moduleFactoryClass);
        messageProcessorBuilderGenerator.setCtx(ctx());
        messageProcessorBuilderGenerator.generate(module);

        final MessageSourceBuilderGenerator messageSourceBuilderGenerator = new MessageSourceBuilderGenerator(moduleFactoryClass);
        messageSourceBuilderGenerator.setCtx(ctx());
        messageSourceBuilderGenerator.generate(module);

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
