package com.mattsmeets.macrokey.model;

public abstract class AbstractCommand implements CommandInterface {

    private final String type;

    AbstractCommand(String type) {
        this.type = type;
    }

    @Override
    public String getCommandType() {
        return this.type;
    }
}
