package org.mule.devkit.generation.builder;

import org.mule.devkit.generation.api.Generator;
import org.mule.devkit.generation.api.ModuleAnnotationVerifier;
import org.mule.devkit.generation.api.Plugin;
import org.mule.devkit.generation.api.PostProcessor;
import org.mule.devkit.generation.api.annotations.Dependency;
import org.mule.devkit.generation.api.annotations.RequiresMavenDependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresMavenDependencies({
        @Dependency(groupId = "org.mule.transports", artifactId = "mule-transport-http", version = "[3.0.0,4.0.0)")
})
public class BuilderPlugin implements Plugin {
    private List<ModuleAnnotationVerifier> annotationVerifiers;
    private List<Generator> moduleGenerators;

    public BuilderPlugin() {
        moduleGenerators = new ArrayList<Generator>();

        annotationVerifiers = new ArrayList<ModuleAnnotationVerifier>();
        annotationVerifiers.add(new BuilderAnnotationVerifier());

        moduleGenerators = new ArrayList<Generator>();
        moduleGenerators.add(new BuilderGenerator());
    }

    @Override
    public String getOptionName() {
        return null;
    }

    @Override
    public List<ModuleAnnotationVerifier> getVerifiers() {
        return annotationVerifiers;
    }

    @Override
    public List<Generator> getGenerators() {
        return moduleGenerators;
    }

    @Override
    public List<PostProcessor> getPostProcessors() {
        return Collections.emptyList();
    }
}