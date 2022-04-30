package tv.quaint.streamlinebase.configs;

import java.io.File;

public class ConfigFileInput {
    public String fileName;
    public boolean hasDefault;
    public File file;

    public ConfigFileInput(String fileName, boolean hasDefault, File file) {
        this.fileName = fileName;
        this.hasDefault = hasDefault;
        this.file = file;
    }
}
