package mata.macrokey.object;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import mata.macrokey.MacroKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class BoundKey {

    @SerializedName("keyCode")
    @Expose
    private int keyCode;
    @SerializedName("command")
    @Expose
    private String command;


    private transient boolean isPressed;

    public BoundKey() {
    }

    public BoundKey(int keyCode, String command) {
        this.keyCode = keyCode;
        this.command = command;
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
}