package com.mattsmeets.macrokey.object;

/**
 * Created by Matt on 5/20/2016.
 */
public class ToBeExecutedCommand {

    private int ticks;
    private String command;

    public ToBeExecutedCommand(int ticks, String command) {
        this.ticks = ticks;
        this.command = command;
    }

    public int getTicks() {
        return ticks;
    }

    public String getCommand() {
        return command;
    }
}
