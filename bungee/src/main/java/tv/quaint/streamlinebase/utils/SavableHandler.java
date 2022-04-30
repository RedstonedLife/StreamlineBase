package tv.quaint.streamlinebase.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.savables.users.SavableConsole;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.savables.users.SavableUser;

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

    public static SavablePlayer addUser(ProxiedPlayer player) {
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

    public static SavableUser getOrGetSavableUser(CommandSender source) {
        if (source.equals(StreamlineBase.SERVER.getConsole())) return getOrGetSavableUser("%");
        else return getOrGetSavableUser(((ProxiedPlayer) source).getUniqueId().toString());
    }

    public static SavablePlayer getOrGetSavablePlayer(ProxiedPlayer player) {
        return (SavablePlayer) getOrGetSavableUser(player);
    }

    public static String parsePlayerIP(ProxiedPlayer player) {
        String ipSt = player.getSocketAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        return ipSt;
    }
}
