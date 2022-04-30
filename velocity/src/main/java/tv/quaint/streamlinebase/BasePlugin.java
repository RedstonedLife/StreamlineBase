package tv.quaint.streamlinebase;

import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import tv.quaint.streamlinebase.expansions.BaseExpansion;
import tv.quaint.streamlinebase.metrics.Metrics;
import tv.quaint.streamlinebase.utils.obj.BaseConstants;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class BasePlugin {
    private static Path DATA_FOLDER;
    public static ProxyServer SERVER;
    public static Logger LOGGER;
    public static Metrics.Factory METRICS_FACTORY;

    public static BasePlugin INSTANCE;
    public static BaseExpansion EXPANSION;

    public BasePlugin(ProxyServer serverThing, Logger loggerThing, Path dataFolderThing, Metrics.Factory metricsFactoryThing, BaseExpansion expansion) {
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
        return BaseConstants.VERSION;
    }
}
