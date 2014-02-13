package org.mule.me.launcher;


import java.io.File;

public class MuleLauncher
{

    public static void main(String[] args) throws Exception
    {
        new GroovyRunner().run(new File(args[0]));
    }

}
