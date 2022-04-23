package tv.quaint.streamlinebasevelo.self;

import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.configs.ConfigPage;
import tv.quaint.streamlinebasevelo.configs.SettingArgument;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

public class OwnMessages extends ConfigPage {
    public String prefix;
    public String nullString;
    public String needsMore;
    public String needsLess;
    public String noPlayer;

    public String playerRegularOnline;
    public String playerRegularOffline;
    public String playerDisplayOnline;
    public String playerDisplayOffline;

    public OwnMessages() {
        super(StreamlineBase.EXPANSION, "messages", true, new AppendableList<>());
    }

    @Override
    public void onReload(AppendableList<SettingArgument<?>> settingArguments) {
        prefix = config.getOrSetDefault("prefix", "&c&lStreamline &d&l>> &r");
        nullString = config.getOrSetDefault("null-string", "&c&lNULL&r");
        needsMore = config.getOrSetDefault("needs-more", "&cYou entered too few arguments for this command!");
        needsLess = config.getOrSetDefault("needs-more", "&cYou entered too many arguments for this command!");
        noPlayer = config.getOrSetDefault("no-player", "&cWe cannot find that player!");

        playerRegularOnline = config.getOrSetDefault("player.regular.online", "%player_absolute% &a•");
        playerRegularOffline = config.getOrSetDefault("player.regular.offline", "%player_absolute% &c•");
        playerDisplayOnline = config.getOrSetDefault("player.display.online", "%player_formatted% &a•");
        playerDisplayOffline = config.getOrSetDefault("player.display.offline", "%player_formatted% &c•");
    }
}
