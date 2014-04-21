package org.mule.gulash;


import org.mule.dependency.DependencyManager;
import org.mule.dependency.MavenDependencyManager;
import org.mule.dependency.ModuleBuilder;
import org.mule.module.core.Mule;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.ArrayUtils;

public class GulashMain
{

    public static final String CREATE_OPTION = "c";
    public static final String HELP_OPTION = "help";

    public static final String REQUIRE_OPTION = "get";
    public static final String VERSION_OPTION = "version";
    public static final String LIST_OPTION = "list";

    public static void main(String[] args) throws Exception
    {

        final File muleHome = new File(".");

        final Options options = createOptions();

        // create the parser
        final CommandLineParser parser = new BasicParser();
        try
        {
            // parse the command line arguments
            final CommandLine line = parser.parse(options, args);
            if (line.hasOption(HELP_OPTION))
            {
                printHelp(options);
            }
            else if (line.hasOption(CREATE_OPTION))
            {
                final RamlGenerator ramlGenerator = new RamlGenerator();
                ramlGenerator.generateScaffold(new File(line.getOptionValue(CREATE_OPTION)));
            }

            else if (line.hasOption(REQUIRE_OPTION))
            {
                final DependencyManager mavenDependencyManager = new MavenDependencyManager();
                final String moduleName = line.getOptionValue(REQUIRE_OPTION);
                final ModuleBuilder moduleBuilder = new ModuleBuilder(moduleName);
                if (line.hasOption(VERSION_OPTION))
                {
                    moduleBuilder.version(line.getOptionValue(VERSION_OPTION));
                }
                mavenDependencyManager.installModule(moduleBuilder.create(), new Mule(muleHome));
            }
            else if (line.hasOption(LIST_OPTION))
            {

                final DependencyManager mavenDependencyManager = new MavenDependencyManager();
                final String moduleName = line.getOptionValue(LIST_OPTION);


                final List<String> versions = mavenDependencyManager.listVersions(moduleName);
                if (versions.isEmpty())
                {
                    System.out.println("NO VERSION of " + moduleName + " was found");
                }
                else
                {
                    System.out.println("Available versions of " + moduleName);
                    for (String version : versions)
                    {
                        System.out.println("\t- " + version.toString());
                    }
                }
            }
            else
            {
                String[] argsArray = line.getArgs();
                if (!ArrayUtils.isEmpty(argsArray))
                {
                    final GroovyRunner groovyRunner = new GroovyRunner();
                    groovyRunner.run(new File(argsArray[argsArray.length - 1]), muleHome);
                }
                else
                {
                    final InteractiveGroovyRunner groovyRunner = new InteractiveGroovyRunner();
                    groovyRunner.run(muleHome);
                }
            }
        }
        catch (ParseException exp)
        {
            System.err.println("Error: " + exp.getMessage());
            printHelp(options);

        }

    }

    private static void printHelp(Options options)
    {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("gulash.sh <File>", options);
    }

    private static Options createOptions()
    {
        Option ramlFile = OptionBuilder.withArgName("RAML file")
                .hasArg()
                .withDescription("Create a script based on the specified RAML file.")
                .create(CREATE_OPTION);

        Option help = new Option(HELP_OPTION, "Print this message");

        Option require = OptionBuilder.withArgName("Dependency Module Name.")
                .hasArg()
                .withDescription("Downloads the required dependency so it is available at runtime.")
                .create(REQUIRE_OPTION);

        Option version = OptionBuilder.withArgName("Dependency Module  Version.")
                .hasArg()
                .withDescription("The version of the dependency. If not specified use the latest one.")
                .create(VERSION_OPTION);

        Option listVersion = OptionBuilder.withArgName("Dependency Module Version.")
                .hasArg()
                .withDescription("The version of the dependency. If not specified use the latest one.")
                .create(LIST_OPTION);

        Options options = new Options();
        options.addOption(ramlFile);
        options.addOption(help);
        options.addOption(require);
        options.addOption(version);
        options.addOption(listVersion);
        return options;
    }

}
