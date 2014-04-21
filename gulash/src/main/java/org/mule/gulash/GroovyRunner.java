package org.mule.gulash;

import org.mule.module.core.Mule;
import org.mule.util.FileUtils;

import java.io.File;
import java.util.List;
import java.util.Set;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.tools.shell.IO;


public class GroovyRunner extends AbstractGroovyRunner
{

    public void run(File groovyFile, File muleHome) throws Exception
    {
        final Mule mule = new Mule(muleHome);
        final GroovyShell groovyShell = createGroovyShell(mule);

        final List<String> lines = FileUtils.readLines(groovyFile);
        StringBuilder script = new StringBuilder();
        StringBuilder require = new StringBuilder();
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);
            //needs to do this better
            if (StringUtils.trim(line).startsWith("require"))
            {
                require.append(line).append("\n");
            }
            else
            {
                script.append(line).append("\n");
            }
        }

        groovyShell.evaluate(require.toString(), "Dependencies");
        mule.build();
        final Set<String> installedModules = mule.getMuleClassLoader().getInstalledModules();
        for (String installedModule : installedModules)
        {
            script.insert(0, "import org.mule.module." + installedModule + ";\n");
        }
        groovyShell.evaluate(script.toString(), groovyFile.getName());
        mule.start();
    }

}
