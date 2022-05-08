package tv.quaint.streamlinebase.holders;

import net.luckperms.api.LuckPermsProvider;
import tv.quaint.streamlinebase.utils.BaseMessaging;

public class LuckPermsHolder extends BaseHolder<LuckPermsProvider> {

    public LuckPermsHolder(){
        super("LuckPerms");
        if(super.isPresent())
           try {super.setAPI(LuckPermsProvider.get().getClass());}
           catch(Exception e) {BaseMessaging.logSevere("LuckPerms not loaded Disabling LuckPerms support");}
    }
}
