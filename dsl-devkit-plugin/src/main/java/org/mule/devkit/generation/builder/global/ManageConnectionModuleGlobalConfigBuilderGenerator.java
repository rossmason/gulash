package org.mule.devkit.generation.builder.global;

import org.mule.devkit.generation.api.Product;
import org.mule.devkit.model.Method;
import org.mule.devkit.model.Parameter;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.GeneratedBlock;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.code.GeneratedField;
import org.mule.devkit.model.code.GeneratedVariable;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.connectivity.ConnectMethod;
import org.mule.devkit.model.module.connectivity.ManagedConnectionModule;

import java.util.List;


public class ManageConnectionModuleGlobalConfigBuilderGenerator extends GlobalConfigBuilderGenerator
{

    public ManageConnectionModuleGlobalConfigBuilderGenerator(GeneratedClass moduleFactoryClass)
    {
        super(moduleFactoryClass);
    }

    @Override
    public boolean shouldGenerate(Module module)
    {
        return module instanceof ManagedConnectionModule;
    }


    @Override
    protected List<GeneratedField> declareConfigBuilderFields(Module module, GeneratedClass configBuilderClass, GeneratedBlock createMethodBlock, GeneratedVariable resultVariable)
    {
        List<GeneratedField> requiredFields = super.declareConfigBuilderFields(module, configBuilderClass, createMethodBlock, resultVariable);
        ManagedConnectionModule managedConnectionModule = (ManagedConnectionModule) module;
        ConnectMethod connectMethod = managedConnectionModule.getConnectMethod();
        List<Parameter<Method<ManagedConnectionModule>>> parameters = connectMethod.getParameters();
        for (Parameter<Method<ManagedConnectionModule>> parameter : parameters)
        {
            String fieldName = parameter.getName();
            declareField(fieldName, ref(parameter.asTypeMirror()), configBuilderClass, parameter.getDefaultValue());
            createMethodBlock.invoke(resultVariable, getSetterMethod(fieldName)).arg(ExpressionFactory.ref(fieldName));
        }
        return requiredFields;
    }

    @Override
    protected GeneratedClass getConfigClass(Module module)
    {
        // Config
        return ctx().getProduct(Product.CONNECTION_MANAGER, module);
    }
}
