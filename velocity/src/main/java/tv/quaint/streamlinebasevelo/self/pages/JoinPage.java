package tv.quaint.streamlinebasevelo.self.pages;

import com.velocitypowered.api.proxy.Player;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.databases.DatabasePage;
import tv.quaint.streamlinebasevelo.databases.DatabaseThing;
import tv.quaint.streamlinebasevelo.utils.obj.SingleSet;

import java.util.Date;

public class JoinPage extends DatabasePage {
    public JoinPage(Player player, boolean getAfter) {
        this(player.getUniqueId().toString(), getAfter);
    }

    public JoinPage(String uuid, boolean getAfter) {
        super(StreamlineBase.CONFIG.databaseConfiguration, StreamlineBase.CONFIG.joinCollection, new SingleSet<>("uuid", new DatabaseThing<>(uuid)));

        addField(new SingleSet<>("latest", new DatabaseThing<>(new Date())));
        addField(new SingleSet<>("first", new DatabaseThing<>(new Date())));

        if (getAfter) get();
    }

    public void updateLatest() {
        flushFields();
        addField(queryKey);
        addField(new SingleSet<>("latest", new DatabaseThing<>(new Date())));
        update();
    }
}
