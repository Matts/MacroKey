package com.mattsmeets.macrokey.api;

import java.util.HashMap;
import java.util.Map;

public class StateAPI extends AbstractAPI {

    public Map<String, Object> State = new HashMap<>();

    public boolean Has(String key) {
        return this.State.containsKey(key);
    }

    public Object Load(String key) {
        return this.State.get(key);
    }

    public Object Save(String key, Object value) {
        return this.State.put(key, value);
    }

    public void Clear() {
        this.State.clear();
    }

}
