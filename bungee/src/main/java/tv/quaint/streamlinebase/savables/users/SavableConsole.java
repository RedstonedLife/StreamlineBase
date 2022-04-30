package tv.quaint.streamlinebase.savables.users;

import net.md_5.bungee.api.ProxyServer;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.savables.SavableAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavableConsole extends SavableUser {
    public ProxyServer server;

    public List<String> savedKeys = new ArrayList<>();

    public SavableConsole() {
        super("%", SavableAdapter.getTypeByIdentifier("console"));

        this.uuid = this.identifier;

        if (this.uuid == null) return;
        if (this.uuid.equals("")) return;

        this.server = StreamlineBase.SERVER;
    }

    @Override
    public List<String> getTagsFromConfig(){
        return StreamlineBase.SAVABLES.consoleTagsDefault;
    }

    @Override
    public void populateMoreDefaults() {
        latestName = getOrSetDefault("profile.latest.name", StreamlineBase.SAVABLES.consoleNameRegular);
        displayName = getOrSetDefault("profile.display-name", StreamlineBase.SAVABLES.consoleNameDisplay);
        tagList = getOrSetDefault("profile.tags",StreamlineBase.SAVABLES.consoleTagsDefault);
    }

    @Override
    public void loadMoreValues() {

    }

    @Override
    public void saveMore() {

    }
}
