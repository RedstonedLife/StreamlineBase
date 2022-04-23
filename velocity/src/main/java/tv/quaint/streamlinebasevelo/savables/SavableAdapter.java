package tv.quaint.streamlinebasevelo.savables;

import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.utils.FileUtils;

import java.io.File;

public class SavableAdapter {
    public enum Type {
        USER(FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
        CONSOLE(FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
        PLAYER(FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
        GUILD(FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
        PARTY(FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
        ;

        public File path;
        public String suffix;

        Type(File path, String suffix) {
            this.path = path;
            this.suffix = suffix;
        }
    }
}
