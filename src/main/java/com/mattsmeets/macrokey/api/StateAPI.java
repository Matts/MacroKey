package com.mattsmeets.macrokey.api;

import org.graalvm.polyglot.HostAccess;

import java.util.HashMap;
import java.util.Map;

public class StateAPI extends AbstractAPI {

    public Map<String, Object> State = new HashMap<>();

    @HostAccess.Export
    public boolean Has(String key) {
        return this.State.containsKey(key);
    }

    @HostAccess.Export
    public Object Load(String key) {
        return this.State.get(key);
    }

    @HostAccess.Export
    public Object Save(String key, Object value) {
        return this.State.put(key, value);
    }

    @HostAccess.Export
    public void Clear() {
        this.State.clear();
    }

}
