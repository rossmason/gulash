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
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
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
        Option ramlFile = OptionBuilder.withArgName("raml file")
                .hasArg()
                .withDescription("Create an escafolder based on .")
                .create("c");
        Option help = new Option("help", "Print this message");

        Options options = new Options();
        options.addOption(ramlFile);
        options.addOption(help);
        return options;
    }

}
