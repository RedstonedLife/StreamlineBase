package tv.quaint.streamlinebasevelo.savables;

import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.utils.FileUtils;

import java.io.File;
import java.util.List;

public class SavableAdapter {
    public enum DefaultType {
        USER("USER"),
        PLAYER("PLAYER"),
        CONSOLE("CONSOLE"),
        ;

        public String identifier;

        DefaultType(String identifier) {
            this.identifier = identifier;
        }
    }

    public static class SavableType {
        public String identifier;
        public File path;
        public String suffix;

        public SavableType(String identifier, File path, String suffix) {
            this.identifier = identifier.toUpperCase();
            this.path = path;
            this.suffix = suffix;
        }
    }

    public static List<SavableType> types = List.of(
            new SavableType(DefaultType.USER.identifier, FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
            new SavableType(DefaultType.PLAYER.identifier, FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml"),
            new SavableType(DefaultType.CONSOLE.identifier, FileUtils.getPlayersDirectory(StreamlineBase.EXPANSION), ".yml")
    );

    public static void insertNewType(SavableType info) {
        types.add(info);
    }

    public static SavableType getTypeByIdentifier(String identifier) {
        for (SavableType type : types) {
            if (type.identifier.equalsIgnoreCase(identifier)) return type;
        }

        return getNullableBasicType("USER");
    }

    public static SavableType getNullableBasicType(String identifier) {
        for (SavableType type : types) {
            if (type.identifier.equalsIgnoreCase(identifier)) return type;
        }

        return null;
    }
}
