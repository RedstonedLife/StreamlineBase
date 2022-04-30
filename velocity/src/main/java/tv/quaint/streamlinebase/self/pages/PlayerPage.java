package tv.quaint.streamlinebase.self.pages;

import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.databases.DatabasePage;
import tv.quaint.streamlinebase.databases.DatabaseThing;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.utils.obj.SingleSet;

public class PlayerPage extends DatabasePage {
    public PlayerPage(SavablePlayer player, boolean getAfter) {
        super(StreamlineBase.CONFIG.databaseConfiguration, StreamlineBase.CONFIG.playersCollection, new SingleSet<>("uuid", new DatabaseThing<>(player.uuid)));

        addField(new SingleSet<>("profile.display-name", new DatabaseThing<>(player.displayName)));
        addField(new SingleSet<>("profile.latest.name", new DatabaseThing<>(player.latestName)));
        addField(new SingleSet<>("profile.latest.server", new DatabaseThing<>(player.latestServer)));
        addField(new SingleSet<>("profile.latest.version", new DatabaseThing<>(player.latestVersion)));
        addField(new SingleSet<>("profile.tags", new DatabaseThing<>(player.tagList)));
        addField(new SingleSet<>("profile.points", new DatabaseThing<>(player.points)));

        addField(new SingleSet<>("player.names", new DatabaseThing<>(player.nameList)));
        addField(new SingleSet<>("player.ips.list", new DatabaseThing<>(player.ipList)));
        addField(new SingleSet<>("player.ips.latest", new DatabaseThing<>(player.latestIP)));
        addField(new SingleSet<>("player.stats.playtime.seconds", new DatabaseThing<>(player.playSeconds)));
        addField(new SingleSet<>("player.stats.experience.total", new DatabaseThing<>(player.totalXP)));
        addField(new SingleSet<>("player.stats.experience.current", new DatabaseThing<>(player.currentXP)));
        addField(new SingleSet<>("player.stats.level", new DatabaseThing<>(player.level)));

        if (getAfter) get();
    }

    public void updateLatest(SavablePlayer player) {
        flushFields();

        addField(queryKey);

        addField(new SingleSet<>("profile.display-name", new DatabaseThing<>(player.displayName)));
        addField(new SingleSet<>("profile.latest.name", new DatabaseThing<>(player.latestName)));
        addField(new SingleSet<>("profile.latest.server", new DatabaseThing<>(player.latestServer)));
        addField(new SingleSet<>("profile.latest.version", new DatabaseThing<>(player.latestVersion)));
        addField(new SingleSet<>("profile.tags", new DatabaseThing<>(player.tagList)));
        addField(new SingleSet<>("profile.points", new DatabaseThing<>(player.points)));

        addField(new SingleSet<>("player.names", new DatabaseThing<>(player.nameList)));
        addField(new SingleSet<>("player.ips.list", new DatabaseThing<>(player.ipList)));
        addField(new SingleSet<>("player.ips.latest", new DatabaseThing<>(player.latestIP)));
        addField(new SingleSet<>("player.stats.playtime.seconds", new DatabaseThing<>(player.playSeconds)));
        addField(new SingleSet<>("player.stats.experience.total", new DatabaseThing<>(player.totalXP)));
        addField(new SingleSet<>("player.stats.experience.current", new DatabaseThing<>(player.currentXP)));
        addField(new SingleSet<>("player.stats.level", new DatabaseThing<>(player.level)));

        update();
    }
}
