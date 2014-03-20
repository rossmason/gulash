package org.mule.devkit.generation.builder;

import org.mule.devkit.generation.api.AnnotationVerificationException;
import org.mule.devkit.generation.api.ModuleAnnotationVerifier;
import org.mule.devkit.generation.api.gatherer.NotificationGatherer;
import org.mule.devkit.model.module.Module;
import org.mule.devkit.model.module.ModuleKind;

/**
 * Created by machaval on 2/22/14.
 */
public class BuilderAnnotationVerifier implements ModuleAnnotationVerifier
{

    @Override
    public boolean shouldVerify(Module module)
    {
        return (module.getKind() == ModuleKind.CONNECTOR || module.getKind() == ModuleKind.GENERIC);
    }

    @Override
    public void verify(Module module, NotificationGatherer notificationGatherer) throws AnnotationVerificationException
    {

    }


}
