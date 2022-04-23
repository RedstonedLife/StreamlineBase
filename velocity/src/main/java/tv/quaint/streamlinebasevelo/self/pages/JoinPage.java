package tv.quaint.streamlinebasevelo.self.pages;

import com.velocitypowered.api.proxy.Player;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.databases.DatabaseField;
import tv.quaint.streamlinebasevelo.databases.DatabasePage;

import java.util.Date;

public class JoinPage extends DatabasePage {
    public JoinPage(Player player) {
        super(StreamlineBase.CONFIG.databaseConfiguration, "join-info", new DatabaseField<>("uuid", player.getUniqueId().toString()));

        addField(new DatabaseField<>("first", new Date()));
        get();
    }

    public JoinPage(String uuid) {
        super(StreamlineBase.CONFIG.databaseConfiguration, "join-info", new DatabaseField<>("uuid", uuid));

        addField(new DatabaseField<>("first", new Date()));
        get();
    }

    public void updateLatest() {
        getField("latest");
        addField(new DatabaseField<>("latest", new Date()));
        update();
    }
}
