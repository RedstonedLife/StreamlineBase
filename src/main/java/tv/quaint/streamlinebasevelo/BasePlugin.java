package tv.quaint.streamlinebasevelo;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import tv.quaint.streamlinebasevelo.metrics.Metrics;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

@Plugin(
        id = "streamlinebase",
        name = "StreamlineBase",
        version = BuildConstants.VERSION
)
public abstract class BasePlugin {
    private static ProxyServer SERVER;
    private static Logger LOGGER;
    private static Metrics.Factory METRICS_FACTORY;
    private static Path DATA_FOLDER;

    public static BasePlugin INSTANCE;

    @Inject
    BasePlugin(ProxyServer serverThing, Logger loggerThing, @DataDirectory Path dataFolderThing, Metrics.Factory metricsFactoryThing) {
        SERVER = serverThing;
        LOGGER = loggerThing;
        DATA_FOLDER = dataFolderThing;
        INSTANCE = this;
        METRICS_FACTORY = metricsFactoryThing;
    }

    public static ProxyServer getProxy() {
        return SERVER;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static File getDataFolder() {
//		return FileSystems.getDefault().getPath("streamline").toFile();
        return DATA_FOLDER.toFile();
    }

    public InputStream getResourceAsStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    public static PluginDescription getDescription() {
        return getProxy().getPluginManager().getPlugin("streamlinebase").get().getDescription();
    }

    public static String getVersion() {
        return BuildConstants.VERSION;
    }
}
