package org.mule.me.launcher;


import java.io.File;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.ArrayUtils;

public class MuleLauncher
{

    public static final String CREATE_OPTION = "c";
    public static final String HELP_OPTION = "help";
    public static final String INTERACTIVE_OPTION = "i";
    public static final String REQUIRE_OPTION = "r";

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
                final ScaffoldGenerator scaffoldGenerator = new ScaffoldGenerator();
                scaffoldGenerator.generateScaffold(new File(line.getOptionValue(CREATE_OPTION)));
            }
            else if (line.hasOption(INTERACTIVE_OPTION))
            {
                final InteractiveGroovyRunner groovyRunner = new InteractiveGroovyRunner();
                groovyRunner.run(muleHome);
            }
            else if (line.hasOption(REQUIRE_OPTION))
            {
                //todo improve we should add more parameters to make it more configurable
                final ModuleResolver moduleResolver = new ModuleResolver();
                final String moduleName = line.getOptionValue(REQUIRE_OPTION);
                String groupId = "org.mule.modules";
                String artifactId = "mule-module-" + moduleName;
                String classifier = "plugin";
                String extension = "zip";
                String lastVersion = moduleResolver.getHighestVersion(groupId, artifactId, classifier, extension).toString();
                moduleResolver.installModule(groupId, artifactId, classifier, extension, lastVersion, new File(new File(muleHome, "lib"), moduleName));
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
                    printHelp(options);
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
        formatter.printHelp("mule <MuleFile>", options);
    }

    private static Options createOptions()
    {
        Option ramlFile = OptionBuilder.withArgName("RAML file")
                .hasArg()
                .withDescription("Create a script based on the specified RAML file.")
                .create(CREATE_OPTION);
        Option interactive = new Option(INTERACTIVE_OPTION, "Starts an interactive console for fast testing.");
        Option help = new Option(HELP_OPTION, "Print this message");

        Option require = OptionBuilder.withArgName("Module name.")
                .hasArg()
                .withDescription("Downloads the required dependency so is available.")
                .create("r");

        Options options = new Options();
        options.addOption(ramlFile);
        options.addOption(help);
        options.addOption(interactive);
        options.addOption(require);
        return options;
    }

}
