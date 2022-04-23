package tv.quaint.streamlinebasevelo.utils;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.savables.users.SavableConsole;
import tv.quaint.streamlinebasevelo.savables.users.SavablePlayer;
import tv.quaint.streamlinebasevelo.savables.users.SavableUser;

import java.util.ArrayList;
import java.util.List;

public class SavableHandler {
    public static List<SavableUser> loadedUsers = new ArrayList<>();

    public static SavableUser getUser(String uuid) {
        for (SavableUser user : loadedUsers) {
            if (user.uuid.equals(uuid)) return user;
        }

        return null;
    }

    public static SavablePlayer addUser(Player player) {
        if (getUser(player.getUniqueId().toString()) != null) {
            return (SavablePlayer) getUser(player.getUniqueId().toString());
        }

        SavablePlayer savablePlayer = new SavablePlayer(player.getUniqueId().toString());

        loadedUsers.add(savablePlayer);

        return savablePlayer;
    }

    public static void removeUser(SavableUser user) {
        user.saveAll();
        loadedUsers.remove(user);
    }

    public static SavableUser getOrGetSavableUser(String uuid) {
        if (getUser(uuid) != null) return getUser(uuid);

        if (uuid.equals("%")) {
            return new SavableConsole();
        } else {
            return new SavablePlayer(uuid);
        }
    }

    public static SavableUser getOrGetSavableUser(CommandSource source) {
        if (source.equals(StreamlineBase.SERVER.getConsoleCommandSource())) return getOrGetSavableUser("%");
        else return getOrGetSavableUser(((Player) source).getUniqueId().toString());
    }

    public static SavablePlayer getOrGetSavablePlayer(Player player) {
        return (SavablePlayer) getOrGetSavableUser(player);
    }

    public static String parsePlayerIP(Player player) {
        String ipSt = player.getRemoteAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        return ipSt;
    }
}
