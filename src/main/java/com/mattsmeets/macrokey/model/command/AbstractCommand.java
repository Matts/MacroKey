package com.mattsmeets.macrokey.model.command;

public abstract class AbstractCommand implements CommandInterface {

    private final String type;

    AbstractCommand(final String type) {
        this.type = type;
    }

    @Override
    public String getCommandType() {
        return this.type;
    }
}
