package com.mattsmeets.macrokey.event;

import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import net.minecraftforge.eventbus.api.Event;

public class ExecuteOnTickEvent extends Event {

    private ExecuteOnTickInterface executor;

    public ExecuteOnTickEvent(ExecuteOnTickInterface executor) {
        this.executor = executor;
    }

    public ExecuteOnTickInterface getExecutor() {
        return executor;
    }
}
