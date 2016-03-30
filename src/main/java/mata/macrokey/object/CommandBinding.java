package mata.macrokey.object;

import mata.macrokey.MacroKey;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

import java.util.Map;

/**
 * Created by Matt on 3/30/2016.
 */
public class CommandBinding {
    private String exec;
    private int key;
    private boolean isPressed=false;

    public CommandBinding(String exec, Integer key){
        this.exec = exec;
        this.key = key;
    }

    public void setPressed(boolean pressed){
        this.isPressed = pressed;
    }

    public Boolean isPressed(){
        return isPressed;
    }

    public int getKey(){
        return key;
    }

    public String getExec(){
        return exec;
    }

    public static void saveBinding(CommandBinding binding){
        MacroKey.instance.configuration.get("bindings", binding.getKey()+"", binding.getExec());
        MacroKey.instance.binding.add(binding);
        MacroKey.instance.configuration.save();
    }

    public static void loadKeybindings(){
        MacroKey.instance.binding.clear();
        MacroKey.instance.configuration.load();
        ConfigCategory category = MacroKey.instance.configuration.getCategory("bindings");
        for (Map.Entry<String, Property> entry : category.entrySet()) {
            MacroKey.instance.binding.add(new CommandBinding(entry.getValue().getString(), Integer.parseInt(entry.getKey())));
        }
    }
}
