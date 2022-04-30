package tv.quaint.streamlinebase.utils;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.savables.users.SavableConsole;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.savables.users.SavableUser;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BasePlayerHandler {
    public static List<ProxiedPlayer> getOnlinePlayers() {
        return new ArrayList<>(StreamlineBase.SERVER.getPlayers());
    }

    public static List<String> getOnlinePlayerNames() {
        List<String> toReturn = new ArrayList<>();

        for (ProxiedPlayer player : getOnlinePlayers()) {
            toReturn.add(player.getName());
        }

        return toReturn;
    }

    public static ProxiedPlayer getPlayerByUUID(String uuid) {
        for (ProxiedPlayer player : getOnlinePlayers()) {
            if (player.getUniqueId().toString().equals(uuid)) return player;
        }

        return null;
    }

    public static String getSourceName(CommandSender source) {
        if (source.equals(StreamlineBase.SERVER.getConsole())) return "%";

        return ((ProxiedPlayer) source).getName();
    }

    public static String getOffOnDisplayBungee(SavableUser stat){
        if (stat == null) {
            return StreamlineBase.MESSAGES.nullString;
        }

        if (stat instanceof SavableConsole) {
            return StreamlineBase.SAVABLES.consoleNameRegular;
        }

        if (stat instanceof SavablePlayer) {
            if (stat.online) {
                return StreamlineBase.MESSAGES.playerDisplayOnline
                        .replace("%player_formatted%", stat.displayName)
                        .replace("%player_absolute%", stat.displayName)
                        ;
            } else {
                return StreamlineBase.MESSAGES.playerDisplayOffline
                        .replace("%player_formatted%", stat.displayName)
                        .replace("%player_absolute%", stat.displayName)
                        ;
            }
        }

        return StreamlineBase.MESSAGES.nullString;
    }

    public static void updateDisplayName(SavablePlayer player){
        if (! StreamlineBase.CONFIG.textUpdateDisplayName) return;
        if (! StreamlineBase.luckPermsHolder.enabled) return;

        String newDisplayName = getDisplayName(player);

//        if (ConfigUtils.debug()) MessagingUtils.logInfo("Updating " + player.latestName + "'s display name to '" + newDisplayName + "'");

        player.setDisplayName(newDisplayName);
    }

    public static String getDisplayName(SavablePlayer player) {
        return getDisplayName(player.latestName);
    }

    public static String getDisplayName(String username) {
        if (! StreamlineBase.luckPermsHolder.enabled) {
            BaseMessaging.logSevere("Could not get display name of player " + username + " because LuckPerms is disabled!");
            return username;
        }

        User user = StreamlineBase.luckPermsHolder.api.getUserManager().getUser(username);
        if (user == null) return username;

        Group group = StreamlineBase.luckPermsHolder.api.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return username;

        String prefix = "";
        String suffix = "";

        TreeMap<Integer, String> preWeight = new TreeMap<>();
        TreeMap<Integer, String> sufWeight = new TreeMap<>();

        for (PrefixNode node : group.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (PrefixNode node : user.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : group.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : user.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        prefix = preWeight.get(MathUtils.getCeilingInt(preWeight.keySet()));
        suffix = sufWeight.get(MathUtils.getCeilingInt(sufWeight.keySet()));

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return prefix + username + suffix;
    }

    public static String getOffOnRegBungee(SavableUser stat){
        if (stat == null) {
            return StreamlineBase.MESSAGES.nullString;
        }

        if (stat instanceof SavableConsole) {
            return StreamlineBase.SAVABLES.consoleNameRegular;
        }

        if (stat instanceof SavablePlayer) {
            if (stat.online) {
                return StreamlineBase.MESSAGES.playerRegularOnline
                        .replace("%player_formatted%", stat.displayName)
                        .replace("%player_absolute%", stat.displayName)
                        ;
            } else {
                return StreamlineBase.MESSAGES.playerRegularOffline
                        .replace("%player_formatted%", stat.displayName)
                        .replace("%player_absolute%", stat.displayName)
                        ;
            }
        }

        return StreamlineBase.MESSAGES.nullString;
    }

    public static String getJustDisplayBungee(SavableUser stat){
        if (stat == null) {
            return StreamlineBase.MESSAGES.nullString;
        }

        if (stat instanceof SavableConsole) {
            return StreamlineBase.SAVABLES.consoleNameDisplay;
        }

        if (stat instanceof SavablePlayer) {
            return stat.displayName;
        }

        return StreamlineBase.MESSAGES.nullString;
    }

    public static String getAbsoluteBungee(SavableUser stat){
        if (stat == null) {
            return StreamlineBase.MESSAGES.nullString;
        }

        if (stat instanceof SavableConsole) {
            return "%";
        }

        if (stat instanceof SavablePlayer) {
            return stat.latestName;
        }

        return StreamlineBase.MESSAGES.nullString;
    }

    public static String getLuckPermsPrefix(String username, boolean fromCache){
//        if (fromCache) return cachedPrefixes.get(getOrGetPlayerStat(username), (u) -> getLuckPermsPrefix(username, false));

        if (! StreamlineBase.luckPermsHolder.isPresent()) return "";

        User user = StreamlineBase.luckPermsHolder.api.getUserManager().getUser(username);
        if (user == null) {
//            MessagingUtils.logWarning("getLuckPermsPrefix -> user == null");
            return "";
        }

        String prefix = "";

        Group group = StreamlineBase.luckPermsHolder.api.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) {
//            MessagingUtils.logWarning("getLuckPermsPrefix -> group == null");
            TreeMap<Integer, String> preWeight = new TreeMap<>();

            for (PrefixNode node : user.getNodes(NodeType.PREFIX)) {
                preWeight.put(node.getPriority(), node.getMetaValue());
            }

            prefix = preWeight.get(MathUtils.getCeilingInt(preWeight.keySet()));

            if (prefix == null) {
//            MessagingUtils.logWarning("getLuckPermsPrefix -> prefix == null");
                prefix = "";
            }

//            MessagingUtils.logInfo("LP Pre : group == null | prefix = " + prefix);
            return prefix;
        }


        TreeMap<Integer, String> preWeight = new TreeMap<>();

        for (PrefixNode node : group.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
//            MessagingUtils.logInfo("getLuckPermsPrefix -> node added: " + node.getPriority() + " , " + node.getMetaValue());
        }

        for (PrefixNode node : user.getNodes(NodeType.PREFIX)) {
            preWeight.put(node.getPriority(), node.getMetaValue());
        }

        prefix = preWeight.get(MathUtils.getCeilingInt(preWeight.keySet()));

        if (prefix == null) {
//            MessagingUtils.logWarning("getLuckPermsPrefix -> prefix == null");
            prefix = "";
        }

//        cachedPrefixes.put(getOrGetPlayerStat(username), prefix);
        return prefix;
    }

    public static String getLuckPermsSuffix(String username, boolean fromCache){
//        if (fromCache) return cachedSuffixes.get(getOrGetPlayerStat(username), (u) -> getLuckPermsSuffix(username, false));

        if (! StreamlineBase.luckPermsHolder.isPresent()) return "";

        User user = StreamlineBase.luckPermsHolder.api.getUserManager().getUser(username);
        if (user == null) return "";

        String suffix = "";

        Group group = StreamlineBase.luckPermsHolder.api.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null){
            TreeMap<Integer, String> preWeight = new TreeMap<>();

            for (PrefixNode node : user.getNodes(NodeType.PREFIX)) {
                preWeight.put(node.getPriority(), node.getMetaValue());
            }

            suffix = preWeight.get(MathUtils.getCeilingInt(preWeight.keySet()));

            if (suffix == null) {
//            MessagingUtils.logWarning("getLuckPermsPrefix -> prefix == null");
                suffix = "";
            }

//            MessagingUtils.logInfo("LP Pre : group == null | prefix = " + prefix);
            return suffix;
        }


        TreeMap<Integer, String> sufWeight = new TreeMap<>();

        for (SuffixNode node : group.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        for (SuffixNode node : user.getNodes(NodeType.SUFFIX)) {
            sufWeight.put(node.getPriority(), node.getMetaValue());
        }

        suffix = sufWeight.get(MathUtils.getCeilingInt(sufWeight.keySet()));

        if (suffix == null) suffix = "";

//        cachedSuffixes.put(getOrGetPlayerStat(username), suffix);
        return suffix;
    }
}
