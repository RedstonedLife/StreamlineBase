package tv.quaint.streamlinebasevelo.savables.users;

import com.velocitypowered.api.proxy.ProxyServer;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.savables.SavableAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavableConsole extends SavableUser {
    public ProxyServer server;

    public List<String> savedKeys = new ArrayList<>();

    public SavableConsole() {
        super("%", SavableAdapter.Type.CONSOLE);

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

    @Override
    public void sync() {

    }

    @Override
    public void onMoreReload() {

    }
}
