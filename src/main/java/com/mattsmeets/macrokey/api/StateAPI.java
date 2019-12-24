package com.mattsmeets.macrokey.api;

import org.graalvm.polyglot.HostAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * StateAPI provides a runtime state for commands, this is lost when the application exits. Using the StateAPI you can
 * keep specific settings to be called on a later date, this also breaks the barrier between individual command runs.
 *
 * Your command can, for example, implement a counter. Or calculate distance travelled using the PlayerAPI
 *
 * @see PlayerAPI
 * @since 2.1
 */
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
