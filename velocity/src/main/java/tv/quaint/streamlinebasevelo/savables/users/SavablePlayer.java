package tv.quaint.streamlinebasevelo.savables.users;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.proxy.player.SkinParts;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.savables.SavableAdapter;
import tv.quaint.streamlinebasevelo.self.pages.PlayerPage;
import tv.quaint.streamlinebasevelo.utils.BaseMessaging;
import tv.quaint.streamlinebasevelo.utils.BasePlayerHandler;
import tv.quaint.streamlinebasevelo.utils.MathUtils;
import tv.quaint.streamlinebasevelo.utils.SavableHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

public class SavablePlayer extends SavableUser {
    public int totalXP;
    public int currentXP;
    public int level;
    public int playSeconds;
    public String latestIP;
    public List<String> ipList;
    public List<String> nameList;
    public Player player;

    public int defaultLevel = StreamlineBase.SAVABLES.playerLevelingLevelStart;

    public String getLatestIP() {
        if (this.player == null) return StreamlineBase.MESSAGES.nullString;

        String ipSt = player.getRemoteAddress().toString().replace("/", "");
        String[] ipSplit = ipSt.split(":");
        ipSt = ipSplit[0];

        return ipSt;
    }

    public SavablePlayer(Player player) {
        this(player.getUniqueId());
    }

    public SavablePlayer(String uuid){
        super(uuid, SavableAdapter.getTypeByIdentifier("player"));
        this.player = BasePlayerHandler.getPlayerByUUID(uuid);
    }

    public SavablePlayer(UUID uuid) {
        this(uuid.toString());
    }

    @Override
    public List<String> getTagsFromConfig() {
        return StreamlineBase.SAVABLES.playerTagsDefault;
    }

    @Override
    public void populateMoreDefaults() {
        // Ips.
        latestIP = getOrSetDefault("player.ips.latest", getLatestIP());
        ipList = getOrSetDefault("player.ips.list", new ArrayList<>());
        // Names.
        nameList = getOrSetDefault("player.names", new ArrayList<>());
        // Stats.
        level = getOrSetDefault("player.stats.level", defaultLevel);
        totalXP = getOrSetDefault("player.stats.experience.total", StreamlineBase.SAVABLES.playerLevelingXPStart);
        currentXP = getOrSetDefault("player.stats.experience.current", StreamlineBase.SAVABLES.playerLevelingXPStart);
    }

    @Override
    public void loadMoreValues() {
        // Ips.
        latestIP = getOrSetDefault("player.ips.latest", latestIP);
        ipList = getOrSetDefault("player.ips.list", ipList);
        // Names.
        nameList = getOrSetDefault("player.names", nameList);
        // Stats.
        level = getOrSetDefault("player.stats.level", level);
        totalXP = getOrSetDefault("player.stats.experience.total", totalXP);
        currentXP = getOrSetDefault("player.stats.experience.current", currentXP);
        playSeconds = getOrSetDefault("player.stats.playtime.seconds", playSeconds);
    }

    @Override
    public void saveMore() {
        // Ips.
        set("player.ips.latest", latestIP);
        set("player.ips.list", ipList);
        // Names.
        set("player.names", nameList);
        // Stats.
        set("player.stats.level", level);
        set("player.stats.experience.total", totalXP);
        set("player.stats.experience.current", currentXP);
        set("player.stats.playtime.seconds", playSeconds);

        PlayerPage playerPage = new PlayerPage(this, false);
        playerPage.updateLatest(this);
    }

    public void addName(String name){
        if (nameList.contains(name)) return;

        nameList.add(name);
        saveAll();
    }

    public void removeName(String name){
        //        loadValues();
        if (! nameList.contains(name)) return;

        nameList.remove(name);
        saveAll();
    }

    public void setLatestIP(String ip) {
        if (player == null) return;
        this.latestIP = ip;
        addIP(SavableHandler.parsePlayerIP(player));
        saveAll();
    }

    public void setLatestIP(Player player) {
        setLatestIP(SavableHandler.parsePlayerIP(player));
    }

    public void addIP(String ip){
        if (ipList.contains(ip)) return;

        ipList.add(ip);
        saveAll();
    }

    public void addIP(Player player){
        addIP(SavableHandler.parsePlayerIP(player));
    }

    public void removeIP(String ip){
        //        loadValues();
        if (! ipList.contains(ip)) return;

        ipList.remove(ip);
        saveAll();
    }

    public void removeIP(Player player){
        removeIP(SavableHandler.parsePlayerIP(player));
    }

    public void addPlaySecond(int amount){
//        MessagingUtils.logInfo("Added play second!");
        //        loadValues();
        setPlaySeconds(playSeconds + amount);
    }

    public void setPlaySeconds(int amount){
        playSeconds = amount;
//        MessagingUtils.logInfo("Set play seconds to " + playSeconds + "!");
        saveAll();
    }

    public double getPlayMinutes(){
        //        loadValues();
        return playSeconds / (60.0d);
    }

    public double getPlayHours(){
        //        loadValues();
        return playSeconds / (60.0d * 60.0d);
    }

    public double getPlayDays(){
        //        loadValues();
        if (playSeconds < 300) return 0;
        return playSeconds / (60.0d * 60.0d * 24.0d);
    }

    public String getPlaySecondsAsString(){
        //        loadValues();
        return BaseMessaging.truncate(String.valueOf(this.playSeconds), 2);
    }

    public String getPlayMinutesAsString(){
        //        loadValues();
        return BaseMessaging.truncate(String.valueOf(getPlayMinutes()), 2);
    }

    public String getPlayHoursAsString(){
        //        loadValues();
        return BaseMessaging.truncate(String.valueOf(getPlayHours()), 2);
    }

    public String getPlayDaysAsString(){
        //        loadValues();
        return BaseMessaging.truncate(String.valueOf(getPlayDays()), 2);
    }

    /*
   Experience required =
   2 × current_level + 7 (for levels 0–15)
   5 × current_level – 38 (for levels 16–30)
   9 × current_level – 158 (for levels 31+)
    */

    public int getNeededXp(){
        int needed = 0;

        String function = BaseMessaging.replaceAllSenderBungee(BaseMessaging.replaceAllPlayerBungee(StreamlineBase.SAVABLES.playerLevelingLevelEquation, this), this)
                        .replace("%default_level%", String.valueOf(defaultLevel));

        needed = (int) Math.round(MathUtils.eval(function));

        return needed;
    }

    public int xpUntilNextLevel(){
        //        loadValues();
        return getNeededXp() - this.totalXP;
    }

    public void addTotalXP(int amount){
        //        loadValues();
        setTotalXP(amount + this.totalXP);
    }

    public void setTotalXP(int amount){
        //        loadValues();
        this.totalXP = amount;

        while (xpUntilNextLevel() <= 0) {
            int setLevel = this.level + 1;
            this.level = setLevel;
        }

        currentXP = getCurrentXP();
        saveAll();
    }

    public int getCurrentLevelXP(){
        //        loadValues();
        int xpTill = 0;
        for (int i = 0; i <= this.level; i++) {
            xpTill += getNeededXp();
        }

        return xpTill;
    }

    public int getCurrentXP(){
        //        loadValues();
        return this.totalXP - getCurrentLevelXP();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void connect(ServerInfo target) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).createConnectionRequest(StreamlineBase.SERVER.getServer(target.getName()).get()).connect();
        }
    }

    public void connect(RegisteredServer target) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).createConnectionRequest(target).connect();
        }
    }

    
    public ServerConnection getServer() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getCurrentServer().get();
        }
        return null;
    }


    public long getPing() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getPing();
        }
        return -1;
    }
    
    public void chat(String message) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).spoofChatInput(message);
        }
    }
    
    public String getUUID() {
        return uuid;
    }

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    
    public Locale getLocale() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getEffectiveLocale();
        }
        return null;
    }

    
    public byte getViewDistance() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getPlayerSettings().getViewDistance();
        }
        return -1;
    }

    
    public PlayerSettings.ChatMode getChatMode() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getPlayerSettings().getChatMode();
        }
        return null;
    }

    
    public boolean hasChatColors() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getPlayerSettings().hasChatColors();
        }
        return false;
    }

    
    public SkinParts getSkinParts() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getPlayerSettings().getSkinParts();
        }
        return null;
    }

    
    public PlayerSettings.MainHand getMainHand() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getPlayerSettings().getMainHand();
        }
        return null;
    }

    
    public void setTabHeader(TextComponent header, TextComponent footer) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getTabList().setHeaderAndFooter(header, footer);
        }
    }

    public void resetTabHeader() {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getTabList().clearHeaderAndFooter();
        }
    }

    public void sendTitle(Title title) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).showTitle(title);
        }
    }
    
    public String getName() {
        return latestName;
    }

    public void sendMessage(Component message) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).sendMessage(message);
        }
    }
    public void sendMessage(String message) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).sendMessage(BaseMessaging.codedText(message));
        }
    }
    
    public boolean hasPermission(String permission) {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).hasPermission(permission);
        }
        return false;
    }

    @Deprecated
    public InetSocketAddress getAddress() {
        if (online) {
            return Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getRemoteAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    
    public SocketAddress getSocketAddress() {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).getRemoteAddress();
        }
        return InetSocketAddress.createUnresolved(latestIP, new Random().nextInt(26666));
    }

    @Deprecated
    public void disconnect(String reason) {
        if (online) {
            Objects.requireNonNull(BasePlayerHandler.getPlayerByUUID(uuid)).disconnect(BaseMessaging.codedText(reason));
        }
    }
    
    public boolean isConnected() {
        return online;
    }
}
