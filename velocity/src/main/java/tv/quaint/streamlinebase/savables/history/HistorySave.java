package tv.quaint.streamlinebase.savables.history;

import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.configs.ConfigPage;
import tv.quaint.streamlinebase.configs.SettingArgument;
import tv.quaint.streamlinebase.utils.FileUtils;
import tv.quaint.streamlinebase.utils.obj.AppendableList;

import java.time.Instant;
import java.util.TreeMap;
import java.util.TreeSet;

public class HistorySave extends ConfigPage {
    public String uuid;

    public HistorySave(String uuid) {
        super(StreamlineBase.EXPANSION, uuid, false, new AppendableList<>(), FileUtils.getHistorySave(uuid));
        this.uuid = uuid;
    }

    @Override
    public void onReload(AppendableList<SettingArgument<?>> settingArguments) {
        // Do nothing. Nothing to load.
    }

    public String addLine(String server, String message) {
        return addLine(server, Instant.now().toEpochMilli(), message);
    }

    public String addLine(String server, long milliDate, String message) {
        config.set(server + "." + milliDate, message);
        return message;
    }

    public TreeSet<String> getTimestamps(String server) {
        return new TreeSet<>(config.getSection(server).singleLayerKeySet());
    }

    public TreeSet<String> getTalkedInServers() {
        return new TreeSet<>(config.singleLayerKeySet());
    }

    public TreeMap<Long, String> getTimestampsWithMessageFrom(String timestampFrom, String server) {
        TreeMap<Long, String> map = new TreeMap<>();

        TreeSet<String> ts = getTimestamps(server);

        long timestampF;
        try {
            timestampF = Long.parseLong(timestampFrom);
        } catch (Exception e) {
            return map;
        }

        for (String t : ts) {
            try {
                long timestamp = Long.parseLong(t);
                if (timestamp >= timestampF) {
                    map.put(timestamp, config.getString(server + "." + t));
                }
            } catch (Exception e) {
                // continue
            }
        }

        return map;
    }
}
