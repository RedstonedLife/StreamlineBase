package tv.quaint.streamlinebase.holders;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.utils.BaseMessaging;

public class LuckPermsHolder {
    public LuckPerms api;
    public boolean enabled;

    public LuckPermsHolder(){
        enabled = isPresent();
    }

    public boolean isPresent(){
        if (StreamlineBase.SERVER.getPluginManager().getPlugin("luckperms").isEmpty()) {
            return false;
        }

        try {
            api = LuckPermsProvider.get();
            return true;
        } catch (Exception e) {
            BaseMessaging.logSevere("LuckPerms not loaded... Disabling LuckPerms support...");
        }
        return false;
    }
}
