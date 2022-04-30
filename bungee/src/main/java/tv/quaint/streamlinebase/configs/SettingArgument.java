package tv.quaint.streamlinebase.configs;

public class SettingArgument<T> {
    public T type;

    public SettingArgument(T type) {
        this.type = type;
    }

    public boolean isOfType(Object object) {
        return type.getClass().isInstance(object);
    }
}
