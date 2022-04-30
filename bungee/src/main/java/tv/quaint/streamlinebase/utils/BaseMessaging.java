package tv.quaint.streamlinebase.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.rat.PlaceholderUtils;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.savables.users.SavableUser;

import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseMessaging {
    public static void logInfo(String message) {
        StreamlineBase.LOGGER.info(message);
    }

    public static void logWarning(String message) {
        StreamlineBase.LOGGER.warning(message);
    }

    public static void logSevere(String message) {
        StreamlineBase.LOGGER.severe(message);
    }

    public static void sendMessage(CommandSender source, String message) {
        if (source instanceof ProxiedPlayer) {
            SavablePlayer player = SavableHandler.getOrGetSavablePlayer((ProxiedPlayer) source);

            source.sendMessage(codedText(replaceAllPlayerBungee(replaceAllSenderBungee(message, source), source)
                    .replace("%version%", (player != null ? player.latestVersion : StreamlineBase.MESSAGES.nullString))
            ));
        } else {
            source.sendMessage(codedText(replaceAllPlayerBungee(replaceAllSenderBungee(message, source), source)));
        }
    }

    public static void sendMessage(CommandSender source, String message, SavableUser on) {
        if (source instanceof ProxiedPlayer) {
            SavablePlayer player = SavableHandler.getOrGetSavablePlayer((ProxiedPlayer) source);

            source.sendMessage(codedText(replaceAllPlayerBungee(replaceAllSenderBungee(message, source), on)
                    .replace("%version%", (player != null ? player.latestVersion : StreamlineBase.MESSAGES.nullString))
            ));
        } else {
            source.sendMessage(codedText(replaceAllPlayerBungee(replaceAllSenderBungee(message, source), on)));
        }
    }

    public static Title codedTitle(String main, String sub, int fadeIn, int stay, int fadeOut) {
        Title title = StreamlineBase.SERVER.createTitle();
        title.title(codedText(main));
        title.subTitle(codedText(sub));
        title.fadeIn(fadeIn);
        title.stay(stay);
        title.fadeOut(fadeOut);

        return title;
    }

    public static BaseComponent getCodedTextFromList(List<String> strings) {
        BaseComponent textComponent = codedText(strings.get(0));

        for (int i = 1; i < strings.size(); i ++) {
            textComponent.addExtra(codedText("\n" + strings.get(i)));
        }

        return textComponent;
    }

//    public static List<String> getCodedPlayerStringListBungee(List<String> strings, CommandSource player) {
//        List<String> toReturn = new ArrayList<>();
//
//        for (String string : strings) {
//            toReturn.add(TextUtils.replaceAllPlayerBungee(string, player));
//        }
//
//        return toReturn;
//    }

    public static TextComponent codedText(String text) {
        text = ChatColor.translateAlternateColorCodes('&', newLined(text));

        try {
            //String ntext = text.replace(ConfigUtils.linkPre(), "").replace(ConfigUtils.linkSuff, "");

            Pattern pattern = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(stripColor(text));
            String foundUrl = "";

            while (matcher.find()) {
                foundUrl = matcher.group(0);

                return makeLinked(text, foundUrl);
            }
        } catch (Exception e) {
            return hexedText(text);
        }
        return hexedText(text);
    }

    public static TextComponent clhText(String text, String hoverPrefix){
        text = ChatColor.translateAlternateColorCodes('&', newLined(text));

        try {
            //String ntext = text.replace(ConfigUtils.linkPre(), "").replace(ConfigUtils.linkSuff, "");

            Pattern pattern = Pattern.compile("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(stripColor(text));
            String foundUrl = "";

            while (matcher.find()) {
                foundUrl = matcher.group(0);

                TextComponent tc = makeLinked(text, foundUrl);
                return makeHoverable(tc, hoverPrefix + foundUrl);
            }
        } catch (Exception e) {
            return hexedText(text);
        }
        return hexedText(text);
    }

    public static String stripColor(String string){
        return ChatColor.stripColor(string)
                .replaceAll("([<][#][1-9a-f][1-9a-f][1-9a-f][1-9a-f][1-9a-f][1-9a-f][>])+", "")
                .replaceAll("[&][1-9a-f]", "");
    }

    public static String formatted(String string) {
        String[] strings = string.split(" ");

        for (int i = 0; i < strings.length; i ++) {
            if (strings[i].toLowerCase(Locale.ROOT).startsWith("<to_upper>")) {
                strings[i] = strings[i].toUpperCase(Locale.ROOT).replace("<TO_UPPER>", "");
            }
            if (strings[i].toLowerCase(Locale.ROOT).startsWith("<to_lower>")) {
                strings[i] = strings[i].toLowerCase(Locale.ROOT).replace("<to_lower>", "");
            }
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strings.length; i ++) {
            if (i == strings.length - 1) {
                builder.append(strings[i]);
            } else {
                builder.append(strings[i]).append(" ");
            }
        }

        return builder.toString();
    }

    public static TextComponent makeLinked(String text, String url){
        TextComponent tc = hexedText(text);
        ClickEvent ce = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        tc.setClickEvent(ce);
        return tc;
    }

    public static TextComponent makeHoverable(String text, String hoverText){
        TextComponent tc = new TextComponent(text);
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(codedString(hoverText)));
        tc.setHoverEvent(he);
        return tc;
    }

    public static TextComponent makeHoverable(TextComponent textComponent, String hoverText){
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(codedString(hoverText)));
        textComponent.setHoverEvent(he);
        return textComponent;
    }

    public static TextComponent hexedText(String text) {
        text = codedString(text);

        try {
            //String ntext = text.replace(ConfigUtils.linkPre(), "").replace(ConfigUtils.linkSuff, "");

            Pattern pattern = Pattern.compile("([<][#][1-9a-f][1-9a-f][1-9a-f][1-9a-f][1-9a-f][1-9a-f][>])+", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(stripColor(text));
            String found = "";

            String textLeft = text;

            TextComponent tc = new TextComponent();

            int i = 0;
            boolean find = false;
            TreeMap<Integer, String> founds = new TreeMap<>();

            while (matcher.find()) {
                find = true;
                found = matcher.group(0);

                founds.put(i, found);

                i ++;
            }
            if (! find) return new TextComponent(text);

            TreeMap<Integer, String> pieces = new TreeMap<>();
            int iter = 0;
            int from = 0;
            for (Integer key : founds.keySet()) {
                int at = text.indexOf(founds.get(key), from);
                pieces.put(iter, text.substring(at));
                from = at;
                iter ++;
            }

            tc = new TextComponent(pieces.get(0));

            for (Integer key : pieces.keySet()) {
                if (key == 0) continue;

                String p = pieces.get(key);
                String f = p.substring(0, "<#123456>".length());

                String colorHex = f.substring(1, f.indexOf('>'));
                String after = p.substring(f.length());

//                tc.addExtra(new LiteralText(after).styled(style -> style.withColor(Integer.decode(colorHex))));
                BaseComponent[] bc = new ComponentBuilder(after).color(ChatColor.of(Color.decode(colorHex))).create();

                for (BaseComponent b : bc) {
                    tc.addExtra(b);
                }
            }

            return tc;
        } catch (Exception e) {
            e.printStackTrace();
            return new TextComponent(text);
        }
    }

//    public static TreeMap<Integer, Player> getTaggedPlayersIndexed(String[] args, String serverName) {
//        TreeMap<Integer, Player> toIndex = new TreeMap<>();
//        List<Player> players = BasePlayerHandler.getServeredPPlayers(serverName);
//
//        for (Player player : players) {
//            for (int i = 0; i < args.length; i ++) {
//                if (player.getUsername().equals(args[i])) {
//                    toIndex.put(i, player);
//                }
//            }
//        }
//
//        return toIndex;
//    }

//    public static SingleSet<String, List<Player>> getMessageWithTags(Player sender, String message, String format) {
//        String[] args = message.split(" ");
//
//        String chatColor = isolateChatColor(format);
//
//        Optional<ServerConnection> serverConnection = sender.getCurrentServer();
//        if (! serverConnection.isPresent()) return new SingleSet<>("", new ArrayList<>());
//
//        TreeMap<Integer, Player> indexed = getTaggedPlayersIndexed(args, serverConnection.get().getServerInfo().getName());
//
//        for (Integer index : indexed.keySet()) {
//            args[index] = StreamLine.serverConfig.getTagsPrefix() + args[index];
//        }
//
//        for (int i = 0; i < args.length; i ++) {
//            args[i] = chatColor + args[i];
//        }
//
//        return new SingleSet<>(normalize(args), new ArrayList<>(indexed.values()));
//    }

    public static String isolateChatColor(String format) {
        String[] strings = format.split(" ");

        for (String string : strings) {
            if (string.contains("%message%")) {
                String[] gotten = string.split("%message%");
                return gotten[0];
            }
        }

        return "";
    }

    public static String newLined(String text) {
        File[] files = FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION).listFiles();
        if (files == null) return text.replace("%newline%", "\n");
        return text.replace("%newline%", "\n").replace("%uniques%", String.valueOf(files.length));
    }

    public static String removeExtraDot(String string){
        String s = string.replace("..", ".");

        if (s.endsWith(".")) {
            s = s.substring(0, s.lastIndexOf('.'));
        }

        return s;
    }

    public static String replaceArgs(String from, String... args) {
        String to = from;

        int i = 1;
        for (String arg : args) {
            to = to.replace("%arg" + i + "%", arg);
            i ++;
        }

        return to;
    }

    public static String resize(String text, int digits) {
        try {
            digits = getDigits(digits, text.length());
            return text.substring(0, digits);
        } catch (Exception e) {
            return text;
        }
    }

    public static String truncate(String text, int digits) {
        if (! text.contains(".")) return text;

        try {
            digits = getDigits(text.indexOf(".") + digits + 1, text.length());
            return text.substring(0, digits);
        } catch (Exception e) {
            return text;
        }
    }

    public static int getDigits(int start, int otherSize){
        if (start <= otherSize) {
            return start;
        } else {
            return otherSize;
        }
    }

    public static boolean isCommand(String msg){
        return msg.startsWith("/");
    }

    public static String normalize(String[] splitMsg, String spacer){
        int i = 0;
        StringBuilder text = new StringBuilder();

        for (String split : splitMsg){
            i++;
            if (split.equals("")) continue;

            if (i < splitMsg.length)
                text.append(split).append(spacer);
            else
                text.append(split);
        }

        return text.toString();
    }

    public static String normalize(TreeSet<String> splitMsg, String spacer) {
        int i = 0;
        StringBuilder text = new StringBuilder();

        for (String split : splitMsg){
            i++;
            if (split.equals("")) continue;

            if (i < splitMsg.size())
                text.append(split).append(" ");
            else
                text.append(split);
        }

        return text.toString();
    }

    public static String normalize(TreeMap<Integer, String> splitMsg, String spacer) {
        int i = 0;
        StringBuilder text = new StringBuilder();

        for (Integer split : splitMsg.keySet()){
            i++;
            if (splitMsg.get(split).equals("")) continue;

            if (i < splitMsg.size())
                text.append(splitMsg.get(split)).append(" ");
            else
                text.append(splitMsg.get(split));
        }

        return text.toString();
    }

//    public static String getMessageWithEmotes(Player player, String input) {
//        for (String emote : StreamLine.serverConfig.getEmotes()) {
//            if (! player.hasPermission(StreamLine.serverConfig.getEmotePermission(emote))) continue;
//            input = input.replace(emote, StreamLine.serverConfig.getEmote(emote));
//        }
//
//        return input;
//    }

    public static boolean equalsAll(Object object, Object... toEqual){
        for (Object equal : toEqual) {
            if (! object.equals(equal)) return false;
        }

        return true;
    }

    public static boolean equalsAll(Object object, Collection<Object> toEqual){
        for (Object equal : toEqual) {
            if (! object.equals(equal)) return false;
        }

        return true;
    }

    public static boolean equalsAny(Object object, Collection<?> toEqual){
        for (Object equal : toEqual) {
            if (object.equals(equal)) return true;
        }

        return false;
    }

    public static String replaceBasicPlaceholders(String of, SavableUser user) {
        return PlaceholderUtils.parsePlaceholder(StreamlineBase.ratExpansion, user, of);
    }

    public static String replaceAllAnyIdentifierBungee(String of, SavableUser user, String identifier) {
        if (user == null) {
            return of;
        }

        of = of
                .replace("%" + identifier + "_uuid%", user.uuid)
                .replace("%" + identifier + "_server%", user.findServer())

                .replace("%" + identifier + "_absolute%", BasePlayerHandler.getAbsoluteBungee(user))
                .replace("%" + identifier + "_normal%", BasePlayerHandler.getOffOnRegBungee(user))
                .replace("%" + identifier + "_display%", BasePlayerHandler.getOffOnDisplayBungee(user))
                .replace("%" + identifier + "_formatted%", BasePlayerHandler.getJustDisplayBungee(user))

                .replace("%" + identifier + "_points%", String.valueOf(user.points))

                .replace("%" + identifier + "_prefix%", BasePlayerHandler.getLuckPermsPrefix(user.latestName, true))
                .replace("%" + identifier + "_suffix%", BasePlayerHandler.getLuckPermsSuffix(user.latestName, true))

//                .replace("%" + identifier + "_guild_name%", BasePlayerHandler.getPlayerGuildName(user))
//                .replace("%" + identifier + "_guild_members%", BasePlayerHandler.getPlayerGuildMembers(user))
//                .replace("%" + identifier + "_guild_leader_uuid%", BasePlayerHandler.getPlayerGuildLeaderUUID(user))
//                .replace("%" + identifier + "_guild_leader_absolute%", BasePlayerHandler.getPlayerGuildLeaderAbsoluteBungee(user))
//                .replace("%" + identifier + "_guild_leader_formatted%", BasePlayerHandler.getPlayerGuildLeaderJustDisplayBungee(user))
//                .replace("%" + identifier + "_guild_leader_normal%", BasePlayerHandler.getPlayerGuildLeaderOffOnRegBungee(user))
//                .replace("%" + identifier + "_guild_leader_display%", BasePlayerHandler.getPlayerGuildLeaderOffOnDisplayBungee(user))

                .replace("%" + identifier + "_level%", (user instanceof SavablePlayer ? String.valueOf(((SavablePlayer) user).level) : ""))
                .replace("%" + identifier + "_xp_current%", (user instanceof SavablePlayer ? String.valueOf(((SavablePlayer) user).currentXP) : ""))
                .replace("%" + identifier + "_xp_total%", (user instanceof SavablePlayer ? String.valueOf(((SavablePlayer) user).totalXP) : ""))
                .replace("%" + identifier + "_play_seconds%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlaySecondsAsString() : ""))
                .replace("%" + identifier + "_play_minutes%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlayMinutesAsString() : ""))
                .replace("%" + identifier + "_play_hours%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlayHoursAsString() : ""))
                .replace("%" + identifier + "_play_days%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlayDaysAsString() : ""))
//                .replace("%" + identifier + "_votes%", (user instanceof SavablePlayer && ConfigUtils.moduleBRanksEnabled() ? String.valueOf(BasePlayerHandler.getVotesForPlayer((SavablePlayer) user)) : ""))
        ;

//        if (ConfigUtils.mysqlbridgerEnabled()) {
//            of  = StreamLine.msbConfig.parsePlaceholder(of, user);
//        }

        return of;
    }

//    public static String replaceAllAnyIdentifierDiscord(String of, SavableUser user, String identifier) {
//        if (user == null) return of;
//
//        of = of
//                .replace("%" + identifier + "_uuid%", user.uuid)
//                .replace("%" + identifier + "_server%", user.findServer())
//
//                .replace("%" + identifier + "_absolute%", BasePlayerHandler.getAbsoluteDiscord(user))
//                .replace("%" + identifier + "_normal%", BasePlayerHandler.getOffOnRegDiscord(user))
//                .replace("%" + identifier + "_display%", BasePlayerHandler.getOffOnDisplayDiscord(user))
//                .replace("%" + identifier + "_formatted%", BasePlayerHandler.getJustDisplayDiscord(user))
//
//                .replace("%" + identifier + "_points%", String.valueOf(user.points))
//
//                .replace("%" + identifier + "_prefix%", BasePlayerHandler.getLuckPermsPrefix(user.latestName, true))
//                .replace("%" + identifier + "_suffix%", BasePlayerHandler.getLuckPermsSuffix(user.latestName, true))
//
////                .replace("%" + identifier + "_guild_name%", BasePlayerHandler.getPlayerGuildNameDiscord(user))
////                .replace("%" + identifier + "_guild_members%", BasePlayerHandler.getPlayerGuildMembers(user))
////                .replace("%" + identifier + "_guild_leader_uuid%", BasePlayerHandler.getPlayerGuildLeaderUUID(user))
////                .replace("%" + identifier + "_guild_leader_absolute%", BasePlayerHandler.getPlayerGuildLeaderAbsoluteDiscord(user))
////                .replace("%" + identifier + "_guild_leader_formatted%", BasePlayerHandler.getPlayerGuildLeaderJustDisplayDiscord(user))
////                .replace("%" + identifier + "_guild_leader_normal%", BasePlayerHandler.getPlayerGuildLeaderOffOnRegDiscord(user))
////                .replace("%" + identifier + "_guild_leader_display%", BasePlayerHandler.getPlayerGuildLeaderOffOnDisplayDiscord(user))
//
//                .replace("%" + identifier + "_level%", (user instanceof SavablePlayer ? String.valueOf(((SavablePlayer) user).level) : ""))
//                .replace("%" + identifier + "_xp_current%", (user instanceof SavablePlayer ? String.valueOf(((SavablePlayer) user).currentXP) : ""))
//                .replace("%" + identifier + "_xp_total%", (user instanceof SavablePlayer ? String.valueOf(((SavablePlayer) user).totalXP) : ""))
//                .replace("%" + identifier + "_play_seconds%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlaySecondsAsString() : ""))
//                .replace("%" + identifier + "_play_minutes%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlayMinutesAsString() : ""))
//                .replace("%" + identifier + "_play_hours%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlayHoursAsString() : ""))
//                .replace("%" + identifier + "_play_days%", (user instanceof SavablePlayer ? ((SavablePlayer) user).getPlayDaysAsString() : ""))
////                .replace("%" + identifier + "_votes%", (user instanceof SavablePlayer && ConfigUtils.moduleBRanksEnabled() ? String.valueOf(BasePlayerHandler.getVotesForPlayer((SavablePlayer) user)) : ""))
//        ;
//
////        if (ConfigUtils.mysqlbridgerEnabled()) {
////            of  = StreamLine.msbConfig.parsePlaceholder(of, user);
////        }
//
//        return of;
//    }

    public static String replaceAllPlayerBungee(String of, SavableUser user) {
        of = replaceBasicPlaceholders(of, user);
        return replaceAllAnyIdentifierBungee(of, user, "player");
    }

//    public static String replaceAllPlayerBungee(String of, String uuid) {
//        if (! uuid.contains("-") && ! uuid.equals("%")) return replaceAllPlayerBungeeFromDiscord(of, uuid);
//
//        return replaceAllPlayerBungee(of, SavableHandler.getOrGetSavableUser(uuid));
//    }

    public static String replaceAllPlayerBungee(String of, CommandSender sender) {
        return replaceAllPlayerBungee(of, SavableHandler.getOrGetSavableUser(sender));
    }

//    public static String replaceAllPlayerBungeeFromDiscord(String of, String discordID) {
//        long dID = 0L;
//
//        try {
//            dID = Long.parseLong(discordID);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return of;
//        }
//
//        if (StreamLine.discordData.isVerified(dID)) {
//            SavableUser user = SavableHandler.getOrGetSavableUser(StreamLine.discordData.getUUIDOfVerified(dID));
//            of = replaceBasicPlaceholders(of, user);
//            return replaceAllAnyIdentifierBungee(of, user, "player");
//        } else {
//            User user = StreamLine.getJda().getUserById(dID);
//
//            if (user == null) {
//                if (ConfigUtils.debug()) MessagingUtils.logInfo("Discord User of ID " + dID + " returned null...");
//                return of;
//            }
//
//            return of
//                    .replace("%player_absolute%", user.getName())
//                    .replace("%player_normal%", user.getName())
//                    .replace("%player_display%", user.getName())
//                    .replace("%player_formatted%", user.getName())
//                    ;
//        }
//    }

    public static String replaceAllUserBungee(String of, SavableUser user) {
        of = replaceBasicPlaceholders(of, user);
        return replaceAllAnyIdentifierBungee(of, user, "user");
    }

//    public static String replaceAllUserBungee(String of, String uuid) {
//        if (! uuid.contains("-") && ! uuid.equals("%")) return replaceAllUserBungeeFromDiscord(of, uuid);
//
//        return replaceAllUserBungee(of, SavableHandler.getOrGetSavableUser(uuid));
//    }

    public static String replaceAllUserBungee(String of, CommandSender sender) {
        return replaceAllUserBungee(of, SavableHandler.getOrGetSavableUser(sender));
    }

//    public static String replaceAllUserBungeeFromDiscord(String of, String discordID) {
//        long dID = 0L;
//
//        try {
//            dID = Long.parseLong(discordID);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return of;
//        }
//
//        if (StreamLine.discordData.isVerified(dID)) {
//            SavableUser user = SavableHandler.getOrGetSavableUser(StreamLine.discordData.getUUIDOfVerified(dID));
//
//            of = replaceBasicPlaceholders(of, user);
//            return replaceAllAnyIdentifierBungee(of, user, "user");
//        } else {
//            User user = StreamLine.getJda().getUserById(dID);
//
//            if (user == null) {
//                if (ConfigUtils.debug()) MessagingUtils.logInfo("Discord User of ID " + dID + " returned null...");
//                return of;
//            }
//
//            return of
//                    .replace("%user_absolute%", user.getName())
//                    .replace("%user_normal%", user.getName())
//                    .replace("%user_display%", user.getName())
//                    .replace("%user_formatted%", user.getName())
//                    ;
//        }
//    }

    public static String replaceAllSenderBungee(String of, SavableUser user) {
        of = replaceBasicPlaceholders(of, user);
        return replaceAllAnyIdentifierBungee(of, user, "sender");
    }

//    public static String replaceAllSenderBungee(String of, String uuid) {
//        if (! uuid.contains("-") && ! uuid.equals("%")) return replaceAllSenderBungeeFromDiscord(of, uuid);
//
//        return replaceAllSenderBungee(of, SavableHandler.getOrGetSavableUser(uuid));
//    }

    public static String replaceAllSenderBungee(String of, CommandSender sender) {
        return replaceAllSenderBungee(of, SavableHandler.getOrGetSavableUser(sender));
    }

//    public static String replaceAllSenderBungeeFromDiscord(String of, String discordID) {
//        long dID = 0L;
//
//        try {
//            dID = Long.parseLong(discordID);
//        } catch (Exception e) {
////            e.printStackTrace();
//            return of;
//        }
//
//        User u = StreamLine.getJda().getUserById(dID);
//        if (u == null) return of;
//
//        if (StreamLine.discordData.isVerified(dID)) {
//            SavableUser user = SavableHandler.getOrGetSavableUser(StreamLine.discordData.getUUIDOfVerified(dID));
//
//            of = replaceBasicPlaceholders(of, user);
//            return replaceAllAnyIdentifierBungee(of, user, "sender");
//        } else {
//            User user = StreamLine.getJda().getUserById(dID);
//
//            if (user == null) {
//                if (ConfigUtils.debug()) MessagingUtils.logInfo("Discord User of ID " + dID + " returned null...");
//                return of;
//            }
//
//            return of
//                    .replace("%sender_absolute%", user.getName())
//                    .replace("%sender_normal%", user.getName())
//                    .replace("%sender_display%", user.getName())
//                    .replace("%sender_formatted%", user.getName())
//                    ;
//        }
//    }

//    public static String replaceAllPlayerDiscord(String of, SavableUser user) {
//        of = replaceBasicPlaceholders(of, user);
//        return replaceAllAnyIdentifierDiscord(of, user, "player");
//    }
//
//    public static String replaceAllPlayerDiscord(String of, String uuid) {
//        return replaceAllPlayerDiscord(of, SavableHandler.getOrGetSavableUser(uuid));
//    }
//
//    public static String replaceAllPlayerDiscord(String of, CommandSource sender) {
//        return replaceAllPlayerDiscord(of, SavableHandler.getOrGetSavableUser(sender));
//    }
//
//    public static String replaceAllUserDiscord(String of, SavableUser user) {
//
//        of = replaceBasicPlaceholders(of, user);
//        return replaceAllAnyIdentifierDiscord(of, user, "user");
//    }

//    public static String replaceAllUserDiscord(String of, String uuid) {
//        return replaceAllUserDiscord(of, SavableHandler.getOrGetSavableUser(uuid));
//    }
//
//    public static String replaceAllUserDiscord(String of, CommandSource sender) {
//        return replaceAllUserDiscord(of, SavableHandler.getOrGetSavableUser(sender));
//    }
//
//    public static String replaceAllSenderDiscord(String of, SavableUser user) {
//        of = replaceBasicPlaceholders(of, user);
//        return replaceAllAnyIdentifierDiscord(of, user, "sender");
//    }

//    public static String replaceAllSenderDiscord(String of, String uuid) {
//        return replaceAllSenderDiscord(of, SavableHandler.getOrGetSavableUser(uuid));
//    }
//
//    public static String replaceAllSenderDiscord(String of, CommandSource sender) {
//        return replaceAllSenderDiscord(of, SavableHandler.getOrGetSavableUser(sender));
//    }

    public static Collection<ServerInfo> getServers() {
        return StreamlineBase.SERVER.getServers().values();
    }

//    public static int replaceAllPlayerRanks(SavablePlayer player) {
//        String of = ConfigUtils.moduleBRanksUses()
//                .replace("%player_level%", String.valueOf(player.level))
//                .replace("%player_xp_current%", String.valueOf(player.currentXP))
//                .replace("%player_xp_total%", String.valueOf(player.totalXP))
//                .replace("%player_play_seconds%", String.valueOf(player.playSeconds))
//                .replace("%player_play_minutes%", String.valueOf(player.getPlayMinutes()))
//                .replace("%player_play_hours%", String.valueOf(player.getPlayHours()))
//                .replace("%player_play_days%", String.valueOf(player.getPlayDays()))
//                .replace("%player_votes%", String.valueOf(BasePlayerHandler.getVotesForPlayer(player)))
//                ;
//
//        int toReturn = 0;
//
//        try {
//            toReturn = Integer.parseInt(of);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return toReturn;
//    }

    public static boolean equalsAnyServer(String servername) {
        for (ServerInfo server : getServers()) {
            if (server.getName().equals(servername)) return true;
        }

        return false;
    }

    public static List<String> getStringListFromString(String string) {
        String[] strings = string.split(",");

        return List.of(strings);
    }
    public static String getStringFromStringList(List<String> list) {
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (String string : list) {
            if (i != list.size()) {
                builder.append(string).append(" ");
            } else {
                builder.append(string);
            }
        }

        return builder.toString();
    }


    public static boolean isNullOrLessThanEqualTo(Object[] thingArray, int lessThanOrEqualTo) {
        if (thingArray == null) return true;
        return thingArray.length <= lessThanOrEqualTo;
    }

    public static String codedString(String replace) {
        return replace.replace("&", "§");
    }

    public static String[] argsMinus(String[] args, int... toRemove) {
        TreeMap<Integer, String> argsSet = new TreeMap<>();

        for (int i = 0; i < args.length; i++) {
            argsSet.put(i, args[i]);
        }

        for (int remove : toRemove) {
            argsSet.remove(remove);
        }

        return argsSet.values().toArray(new String[0]);
    }

    public static String argsToStringMinus(String[] args, int... toRemove){
        return normalize(argsMinus(args, toRemove), " ");
    }

    public static String argsToStringMinus(String[] args, String spacer, int... toRemove){
        return normalize(argsMinus(args, toRemove), spacer);
    }

    public static String argsToString(String[] args){
        TreeMap<Integer, String> argsSet = new TreeMap<>();

        for (int i = 0; i < args.length; i++) {
            argsSet.put(i, args[i]);
        }

        return normalize(argsSet, " ");
    }
}
