package fr.maxlego08.essentials;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

/** Supplies the JDBC driver formerly declared in Bukkit's plugin.yml libraries section. */
public final class ZEssentialsPluginLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder("central", "default", centralMirror()).build());
        resolver.addDependency(new Dependency(new DefaultArtifact("org.mariadb.jdbc:mariadb-java-client:3.5.6"), null));
        classpathBuilder.addLibrary(resolver);
    }

    // Same precedence and default as Paper's MAVEN_CENTRAL_DEFAULT_MIRROR. Resolve it here because
    // the minimum supported compile API (1.21.4) predates that field. No reflection is needed.
    static String centralMirror() {
        String mirror = System.getenv("PAPER_DEFAULT_CENTRAL_REPOSITORY");
        if (mirror == null) mirror = System.getProperty("org.bukkit.plugin.java.LibraryLoader.centralURL");
        return mirror == null ? "https://maven-central.storage-download.googleapis.com/maven2" : mirror;
    }
}
