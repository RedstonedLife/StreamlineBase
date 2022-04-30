package tv.quaint.streamlinebase.utils;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.expansions.BaseExpansion;
import tv.quaint.streamlinebase.savables.SavableAdapter;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class FileUtils {
    public static Config loadConfigFromSelf(File file, String fileString) {
        if (! file.exists()) {
            try {
                StreamlineBase.getDataFolder().mkdirs();
                try (InputStream in = StreamlineBase.INSTANCE.getResourceAsStream(fileString)) {
                    assert in != null;
                    Files.copy(in, file.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return LightningBuilder.fromFile(file).createConfig();
    }

    public static Config loadConfigNoDefault(File file) {
        if (! file.exists()) {
            try {
                StreamlineBase.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return LightningBuilder.fromFile(file).createConfig();
    }

    public static File getDirectoryOf(File parent, String directoryName) {
        if (! parent.isDirectory()) return null;

        parent.mkdirs();

        File dir = new File(parent, directoryName + File.separator);
        dir.mkdirs();
        return dir;
    }

    public static void check(File file) {
        file.mkdirs();
    }

    public static File getPagesDirectory() {
        check(StreamlineBase.getDataFolder());
        return new File(StreamlineBase.getDataFolder(), "addons" + File.separator);
    }

    public static File getExpansionDirectory(BaseExpansion expansion) {
        check(getPagesDirectory());
        return new File(getPagesDirectory(), expansion.identifier + File.separator);
    }

    public static File getExpansionPage(BaseExpansion expansion, String fileName) {
        return new File(getExpansionDirectory(expansion), fileName + ".yml");
    }

    public static File getHistoryDirectory(BaseExpansion expansion) {
        return getDirectoryOf(getExpansionDirectory(expansion), "history");
    }

    public static File getHistorySave(String uuid) {
        return new File(getHistoryDirectory(StreamlineBase.EXPANSION), uuid + ".yml");
    }

    public static File getCorrectSavableTypeFile(String identifier, SavableAdapter.SavableType type) {
        return new File(type.path, identifier + type.suffix);
    }

    public static File getPlayersDirectory(BaseExpansion expansion) {
        return getDirectoryOf(getExpansionDirectory(expansion), "players");
    }

    public static File getPlayerFile(BaseExpansion expansion, String identifier) {
        return new File(getPlayersDirectory(expansion), identifier + ".yml");
    }

    public static File getCommandsDirectory(BaseExpansion expansion) {
        return getDirectoryOf(getExpansionDirectory(expansion), "commands");
    }

    public static File getCommandFile(BaseExpansion expansion, String identifier) {
        return new File(getCommandsDirectory(expansion), identifier + ".yml");
    }
}
