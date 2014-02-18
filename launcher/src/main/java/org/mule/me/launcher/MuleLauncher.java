package org.mule.me.launcher;


import java.io.File;

public class MuleLauncher
{

    public static void main(String[] args) throws Exception
    {
        File muleHome = new File(".");
        new GroovyRunner().run(new File(args[0]), muleHome);

    }

}
