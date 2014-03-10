package org.mule.devkit.generation.builder.global;

import org.mule.devkit.generation.api.Product;
import org.mule.devkit.model.code.GeneratedClass;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.oauth.OAuthModule;

/**
 * TODO need to check with someone what are the other parameters that we need for OAuth
 */
public class OAuthModuleGlobalConfigBuilderGenerator extends GlobalConfigBuilderGenerator
{

    public OAuthModuleGlobalConfigBuilderGenerator(GeneratedClass moduleFactoryClass)
    {
        super(moduleFactoryClass);
    }

    @Override
    public boolean shouldGenerate(Module module)
    {
        return module instanceof OAuthModule;
    }

    @Override
    protected GeneratedClass getConfigClass(Module module)
    {
        return ctx().getProduct(Product.OAUTH_MANAGER, module);
    }
}
