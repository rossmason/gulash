package org.mule.gulash;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.Raml;
import org.raml.model.Resource;
import org.raml.parser.visitor.RamlDocumentBuilder;

public class RamlGenerator
{

    public void generateScaffold(File ramlFile) throws FileNotFoundException
    {
        final Raml raml = new RamlDocumentBuilder().build(new FileInputStream(ramlFile), ramlFile.getParentFile().getAbsolutePath());
        final Scaffolding scaffolding = new Scaffolding();
        scaffolding.init();
        scaffolding.api(ramlFile.getName());
        addResourceMap(scaffolding, raml.getResources());
        scaffolding.end();
        scaffolding.generate(new File(ramlFile.getParent(), FilenameUtils.getBaseName(ramlFile.getName()) + ".groovy"));

    }

    private void addResourceMap(Scaffolding scaffolding, Map<String, Resource> resources)
    {
        for (Map.Entry<String, Resource> resourceEntry : resources.entrySet())
        {
            Resource resource = resourceEntry.getValue();
            addResource(scaffolding, resource);
            Map<String, Resource> childrenResource = resource.getResources();
            if (childrenResource != null)
            {
                addResourceMap(scaffolding, childrenResource);
            }
        }
    }

    private void addResource(Scaffolding scaffolding, Resource resource)
    {
        Map<ActionType, Action> actions = resource.getActions();
        for (Map.Entry<ActionType, Action> actionTypeActionEntry : actions.entrySet())
        {
            final String description = actionTypeActionEntry.getValue().getDescription();
            final String resourceName = resource.getUri();
            final ActionType actionType = actionTypeActionEntry.getKey();
            scaffolding.declareResourceAction(description, resourceName, actionType);
        }
    }

}
