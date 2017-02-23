package com.mattsmeets.macrokey.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.util.JsonConfig;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.UUID;

@Generated("org.jsonschema2pojo")
public class BoundKey {

    @SerializedName("keyCode")
    @Expose
    private int keyCode;
    @SerializedName("command")
    @Expose
    private String command;
    @SerializedName("repeat")
    @Expose
    private boolean repeat;
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("uuid")
    @Expose
    private UUID uuid;

    private transient boolean isPressed;

    public BoundKey() {
        uuid = UUID.randomUUID();
    }

    public BoundKey(int keyCode, String command, boolean repeat, boolean active) {
        uuid = UUID.randomUUID();
        this.keyCode = keyCode;
        this.command = command;
        this.repeat = repeat;
        this.active=active;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        MacroKey.instance.jsonConfig.saveKeybinding();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
        MacroKey.instance.jsonConfig.saveKeybinding();
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
        MacroKey.instance.jsonConfig.saveKeybinding();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        MacroKey.instance.jsonConfig.saveKeybinding();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(keyCode).append(command).toHashCode();
    }

    public void delete(){
        MacroKey.instance.boundKeys.remove(this);
        Layer.removeKeybinding(this);
        MacroKey.instance.jsonConfig.saveKeybinding();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BoundKey) == false) {
            return false;
        }
        BoundKey rhs = ((BoundKey) other);
        return new EqualsBuilder().append(keyCode, rhs.keyCode).append(command, rhs.command).isEquals();
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public UUID getUuid(){
        return uuid;
    }

    public static BoundKey getKeyfromUUID(UUID uuid) {
        for (BoundKey key :
                MacroKey.instance.boundKeys) {
            if (key.uuid.equals(uuid)) {
                return key;
            }
        }
        return null;
    }

    public static void addKeybinding(BoundKey boundKey){
        MacroKey.instance.boundKeys.add(boundKey);
        JsonConfig.saveKeybinding();
    }
}