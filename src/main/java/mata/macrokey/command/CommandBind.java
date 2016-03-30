package mata.macrokey.command;

import mata.macrokey.MacroKey;
import mata.macrokey.object.CommandBinding;
import mata.macrokey.util.ConfigHelper;
import mata.macrokey.util.KeyHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 3/30/2016.
 */
public class CommandBind implements ICommand {
    private List aliases;

    public CommandBind()
    {
        this.aliases = new ArrayList();
        this.aliases.add("bind");
        this.aliases.add("b");
    }

    @Override
    public String getCommandName() {
        return "bind";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return localizeMessageWithTag("command.bind.usage");
    }

    @Override
    public List<String> getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("list")){
                sender.addChatMessage(new TextComponentString(TextFormatting.DARK_GREEN + localizeMessageWithTag("command.bind.list")));

                for (int i = 0; i < MacroKey.instance.binding.size(); i++) {
                    sender.addChatMessage(new TextComponentString(KeyHelper.getKeyFromInt(MacroKey.instance.binding.get(i).getKey()) + " | " + MacroKey.instance.binding.get(i).getExec()));
                }
            }
            if(args[0].equalsIgnoreCase("help")){
                sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
            }
            if(args[0].equalsIgnoreCase("add")){
                if (args.length >= 2) {
                    if(KeyHelper.getIntFromKey(args[1]) != null) {
                        int key = KeyHelper.getIntFromKey(args[1]);
                        if (!ConfigHelper.doesBindingExist(key)) {
                            StringBuilder buffer = new StringBuilder();

                            for (int i = 2; i < args.length; i++) {
                                buffer.append(args[i] + " ");
                            }

                            buffer.trimToSize();

                            CommandBinding bind = new CommandBinding(buffer.toString(), key);
                            CommandBinding.saveBinding(bind);



                            sender.addChatMessage(new TextComponentString(localizeMessageWithTag("command.add.bindsuccessfull").replace("%exec%", buffer.toString()).replace("%key%", args[1])));
                        } else {
                            sender.addChatMessage(new TextComponentString(localizeMessageWithTag("chat.keyalreadybound")));
                        }
                    }else {
                        sender.addChatMessage(new TextComponentString(localizeMessageWithTag("chat.keynotexist")));
                    }
                } else{
                    sender.addChatMessage(new TextComponentString(localizeMessageWithTag("command.add.usage")));
                }
            }
            if(args[0].equalsIgnoreCase("remove")){
                if (args.length >= 2) {
                    if(KeyHelper.getIntFromKey(args[1]) != null) {
                        if (ConfigHelper.doesBindingExist(KeyHelper.getIntFromKey(args[1]))) {
                            ConfigHelper.removeCommand(KeyHelper.getIntFromKey(args[1]));
                            sender.addChatMessage(new TextComponentString(localizeMessageWithTag("command.add.unbindsuccessfull").replace("%key%", args[1])));
                        } else {
                            sender.addChatMessage(new TextComponentString(localizeMessageWithTag("chat.keynotbound")));
                        }
                    }else {
                        sender.addChatMessage(new TextComponentString(localizeMessageWithTag("chat.keynotexist")));
                    }
                }else{
                    sender.addChatMessage(new TextComponentString(localizeMessageWithTag("command.remove.usage")));
                }
            }
        } else{
            sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
        }
    }

    public String localizeMessageWithTag(String unlocal){
        String message = I18n.translateToLocal(unlocal);

        message = message.replace("%tag%", I18n.translateToLocal("chat.tag"));
        return message;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}