package org.mule.gulash.depedencies;
/*******************************************************************************
 * Copyright (c) 2010-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/


import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;

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
        final DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        final LocalRepository localRepo = newLocalRepository();
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        session.setTransferListener(new DefaultTransferListener());
        session.setRepositoryListener(new DefaultRepositoryListener());

        return session;
    }

    public static LocalRepository newLocalRepository()
    {
        final String homeDirectory = System.getProperty("user.home");
        return new LocalRepository(homeDirectory + "/.m2/repository");
    }

    public static RemoteRepository newCentralRepository()
    {
        return new RemoteRepository.Builder("central", "default", "http://repo1.maven.org/maven2/").build();
    }

    public static RemoteRepository newMuleReleasesRepository()
    {
        return new RemoteRepository.Builder("mule-releases", "default", "http://repository-master.mulesoft.org/releases/").build();
    }

    public static RemoteRepository newCustomRepository(String url)
    {
        return new RemoteRepository.Builder("custom", "default", url).build();
    }

}