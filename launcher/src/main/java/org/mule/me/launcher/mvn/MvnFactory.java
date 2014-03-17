package org.mule.me.launcher.mvn;
/*******************************************************************************
 * Copyright (c) 2010-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;


/**
 * A helper to boot the repository system and a repository system session.
 */
public class MvnFactory
{

    public static RepositorySystem newRepositorySystem()
    {
        return ManualRepositorySystemFactory.newRepositorySystem();
    }

    public static RepositorySystemSession newRepositorySystemSession(RepositorySystem system)
    {
        MavenRepositorySystemSession session = new MavenRepositorySystemSession();

        LocalRepository localRepo = newLocalRepository();
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo));

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public static LocalRepository newLocalRepository()
    {
        String homeDirectory = System.getProperty("user.home");
        return new LocalRepository(homeDirectory + "/.m2/repository");
    }

    public static RemoteRepository newCentralRepository()
    {
        return new RemoteRepository("central", "default", "http://repo1.maven.org/maven2/");
    }

    public static RemoteRepository newMuleReleasesRepository()
    {
        RemoteRepository remoteRepository = new RemoteRepository("mule-releases", "default", "http://repository-master.mulesoft.org/releases/");
        return remoteRepository;
    }

}