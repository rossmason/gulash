package org.mule.me.goulash;

import org.mule.me.goulash.mvn.MvnFactory;
import org.mule.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;


public class ModuleResolver
{

    public void installModule(String groupId, String artifactId, String classifier, String extension, String version, File moduleDirectory) throws ArtifactResolutionException
    {

        final RepositorySystem system = MvnFactory.newRepositorySystem();
        final RepositorySystemSession session = MvnFactory.newRepositorySystemSession(system);
        final Artifact artifact = new DefaultArtifact(groupId, artifactId, classifier, extension, version);
        final ArtifactRequest artifactRequest = createArtifactRequest(artifact);
        final ArtifactResult rangeResult = system.resolveArtifact(session, artifactRequest);
        File file = rangeResult.getArtifact().getFile();
        try
        {
            FileUtils.unzip(file, moduleDirectory);
            System.out.println("Module " + groupId + ":" + artifactId + ":" + version + " was successfully installed at " + moduleDirectory.getPath());
        }
        catch (IOException e)
        {
        }
    }

    public Version getHighestVersion(String groupId, String artifactId, String classifier, String extension) throws VersionRangeResolutionException
    {

        final RepositorySystem system = MvnFactory.newRepositorySystem();
        final RepositorySystemSession session = MvnFactory.newRepositorySystemSession(system);
        final Artifact artifact = new DefaultArtifact(groupId, artifactId, classifier, extension, "[0,)");
        final VersionRangeRequest rangeRequest = createVersionRangeRequest(artifact);
        final VersionRangeResult rangeResult = system.resolveVersionRange(session, rangeRequest);
        return rangeResult.getHighestVersion();
    }

    public List<Version> listVersions(String groupId, String artifactId, String classifier, String extension) throws VersionRangeResolutionException
    {
        final RepositorySystem system = MvnFactory.newRepositorySystem();
        final RepositorySystemSession session = MvnFactory.newRepositorySystemSession(system);
        final Artifact artifact = new DefaultArtifact(groupId, artifactId, classifier, extension, "[0,)");
        final VersionRangeRequest rangeRequest = createVersionRangeRequest(artifact);
        final VersionRangeResult rangeResult = system.resolveVersionRange(session, rangeRequest);
        return rangeResult.getVersions();
    }

    private VersionRangeRequest createVersionRangeRequest(Artifact artifact)
    {
        final VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.addRepository(MvnFactory.newMuleReleasesRepository());
        rangeRequest.addRepository(MvnFactory.newCentralRepository());
        rangeRequest.setArtifact(artifact);
        return rangeRequest;
    }

    private ArtifactRequest createArtifactRequest(Artifact artifact)
    {
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.addRepository(MvnFactory.newMuleReleasesRepository());
        artifactRequest.addRepository(MvnFactory.newCentralRepository());
        artifactRequest.setArtifact(artifact);
        return artifactRequest;
    }


}
