package tv.quaint.streamlinebasevelo;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import tv.quaint.streamlinebasevelo.metrics.Metrics;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;


public class StreamlineBase extends BasePlugin {

    @Inject
    StreamlineBase(ProxyServer serverThing, Logger loggerThing, Path dataFolderThing, Metrics.Factory metricsFactoryThing) {
        super(serverThing, loggerThing, dataFolderThing, metricsFactoryThing);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
