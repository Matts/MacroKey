package com.mattsmeets.macrokey.model;

public abstract class AbstractCommand implements CommandInterface {

    private String type;

    AbstractCommand(String type) {
        this.type = type;
    }

    @Override
    public String getCommandType() {
        return this.type;
    }

    @Override
    public void setup() {}

    public void setCommandType(String type) {
        this.type = type;
    }
}
