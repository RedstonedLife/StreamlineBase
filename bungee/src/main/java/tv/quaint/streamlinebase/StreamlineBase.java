package tv.quaint.streamlinebase;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.slf4j.Logger;
import tv.quaint.streamlinebase.expansions.BaseExpansion;
import tv.quaint.streamlinebase.holders.LuckPermsHolder;
import tv.quaint.streamlinebase.metrics.Metrics;
import tv.quaint.streamlinebase.rat.RATAPI;
import tv.quaint.streamlinebase.rat.addons.StreamlineExpansion;
import tv.quaint.streamlinebase.self.OwnConfig;
import tv.quaint.streamlinebase.self.OwnExpansion;
import tv.quaint.streamlinebase.self.OwnMessages;
import tv.quaint.streamlinebase.self.OwnSavables;
import tv.quaint.streamlinebase.self.commands.ParseCommand;
import tv.quaint.streamlinebase.self.commands.ReloadCommand;
import tv.quaint.streamlinebase.self.listeners.BaseListener;
import tv.quaint.streamlinebase.utils.BaseMessaging;
import tv.quaint.streamlinebase.utils.obj.BaseConstants;
import tv.quaint.streamlinebase.runnables.TickingTimer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StreamlineBase extends BasePlugin {
    public static String ID = BaseConstants.ID;
    public static OwnConfig CONFIG;
    public static OwnMessages MESSAGES;
    public static OwnSavables SAVABLES;
    public static RATAPI ratAPI;
    public static StreamlineExpansion ratExpansion;
    public static LuckPermsHolder luckPermsHolder;

    public List<ScheduledTask> tasks = new ArrayList<>();

    public void moreEnable() {
        BaseMessaging.logInfo("Initializing...");

        EXPANSION = new BaseExpansion(BaseConstants.ID);
        CONFIG = new OwnConfig();
        MESSAGES = new OwnMessages();
        SAVABLES = new OwnSavables();
        ratAPI = new RATAPI();
        ratExpansion = new StreamlineExpansion();
        luckPermsHolder = new LuckPermsHolder();

        loadCommands();

        loadListeners();

        loadTimers();

        BaseMessaging.logInfo("Initialized!");
    }

    public void moreDisable() {
        for (ScheduledTask task : new ArrayList<>(tasks)) {
            task.cancel();
        }
    }

    public void loadCommands() {
        new ParseCommand();
        new ReloadCommand();
    }

    public void loadListeners() {
        new BaseListener();
    }

    public void loadTimers() {
        tasks.add(SERVER.getScheduler().schedule(this, new TickingTimer(), 0, 50, TimeUnit.MILLISECONDS));
    }
}
