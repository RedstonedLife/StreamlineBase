package tv.quaint.streamlinebase;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import tv.quaint.streamlinebase.expansions.BaseExpansion;
import tv.quaint.streamlinebase.metrics.Metrics;
import tv.quaint.streamlinebase.utils.obj.BaseConstants;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.logging.Logger;

public abstract class BasePlugin extends Plugin {
    public static ProxyServer SERVER;
    public static Logger LOGGER;
    public static BasePlugin INSTANCE;
    public static BaseExpansion EXPANSION;

    @Override
    public void onEnable() {
        SERVER = getProxy();
        LOGGER = getLogger();

        moreEnable();
    }

    @Override
    public void onDisable() {
        moreDisable();
    }

    abstract public void moreEnable();

    abstract public void moreDisable();

    public static String getVersion() {
        return BaseConstants.VERSION;
    }
}
