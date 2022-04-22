package tv.quaint.streamlinebasevelo.utils;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.expansions.BaseExpansion;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
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

    public static File getPagesDirectory() {
        File dir = new File(StreamlineBase.getDataFolder(), "addons" + File.separator);
        try {
            return Files.createDirectory(dir.toPath()).toFile();
        } catch (FileAlreadyExistsException e) {
            // do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }

    public static File getExpansionDirectory(BaseExpansion expansion) {
        File dir = new File(getPagesDirectory(), expansion.identifier + File.separator);
        try {
            return Files.createDirectory(dir.toPath()).toFile();
        } catch (FileAlreadyExistsException e) {
            // do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }

    public static File getExpansionPage(BaseExpansion expansion, String fileName) {
        return new File(getExpansionDirectory(expansion), fileName + ".yml");
    }
}
