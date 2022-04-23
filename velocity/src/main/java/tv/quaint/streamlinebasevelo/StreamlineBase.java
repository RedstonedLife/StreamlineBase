package tv.quaint.streamlinebasevelo;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import tv.quaint.streamlinebasevelo.expansions.BaseExpansion;
import tv.quaint.streamlinebasevelo.holders.LuckPermsHolder;
import tv.quaint.streamlinebasevelo.metrics.Metrics;
import tv.quaint.streamlinebasevelo.rat.RATAPI;
import tv.quaint.streamlinebasevelo.rat.addons.StreamlineExpansion;
import tv.quaint.streamlinebasevelo.self.OwnConfig;
import tv.quaint.streamlinebasevelo.self.OwnExpansion;
import tv.quaint.streamlinebasevelo.self.OwnMessages;
import tv.quaint.streamlinebasevelo.self.OwnSavables;
import tv.quaint.streamlinebasevelo.self.commands.ParseCommand;
import tv.quaint.streamlinebasevelo.self.commands.ReloadCommand;
import tv.quaint.streamlinebasevelo.utils.obj.BaseConstants;

import java.nio.file.Path;

@Plugin(
        id = BaseConstants.ID,
        name = "StreamlineBase",
        version = BaseConstants.VERSION,
        dependencies = {
                @Dependency(id = "luckperms")
        }
)
public class StreamlineBase extends BasePlugin {
    public static String ID = BaseConstants.ID;
    public static OwnConfig CONFIG;
    public static OwnMessages MESSAGES;
    public static OwnSavables SAVABLES;
    public static RATAPI ratAPI;
    public static StreamlineExpansion ratExpansion;
    public static LuckPermsHolder luckPermsHolder;

    @Inject
    public StreamlineBase(ProxyServer serverThing, Logger loggerThing, @DataDirectory Path dataFolderThing, Metrics.Factory metricsFactoryThing) {
        super(serverThing, loggerThing, dataFolderThing, metricsFactoryThing, new OwnExpansion());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        EXPANSION = new BaseExpansion(BaseConstants.ID);
        CONFIG = new OwnConfig();
        MESSAGES = new OwnMessages();
        SAVABLES = new OwnSavables();
        ratAPI = new RATAPI();
        ratExpansion = new StreamlineExpansion();
        luckPermsHolder = new LuckPermsHolder();

        loadCommands();
    }

    public void loadCommands() {
        new ParseCommand();
        new ReloadCommand();
    }
}
