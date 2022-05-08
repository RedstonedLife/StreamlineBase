package tv.quaint.streamlinebase.holders;

import tv.quaint.streamlinebase.StreamlineBase;

public abstract class BaseHolder<T> {
    String pluginName;
    Class<?> api;

    BaseHolder(String pluginName) {
            this.pluginName = pluginName;
    }

    public boolean isPresent() { return (StreamlineBase.SERVER.getPluginManager().getPlugin(this.plugin) != null); }
    public void setAPI(Class<?> api) { this.api = api; }
    public Class<?> getAPI() { return this.api; }
}
