package mata.macrokey.gui;


import mata.macrokey.MacroKey;
import mata.macrokey.object.BoundKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiKeyBindingsListing extends GuiListExtended {

    private final Minecraft mc;
    private final GuiKeybindings guiKeybindings;
    private final GuiListExtended.IGuiListEntry[] listEntries;

    private int maxListLabelWidth = 0;

    public GuiKeyBindingsListing(GuiKeybindings controls, Minecraft mcIn) {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.mc = mcIn;
        this.guiKeybindings = controls;
        this.listEntries = new GuiListExtended.IGuiListEntry[MacroKey.instance.boundKeys.size()];

        int i = 0;

        for(BoundKey bind : MacroKey.instance.boundKeys){
                this.listEntries[i++] = new GuiKeyBindingsListing.KeyEntry(bind);

                int j = mcIn.fontRendererObj.getStringWidth(I18n.format(bind.getCommand(), new Object[0]));

                if (j > this.maxListLabelWidth)
                {
                    this.maxListLabelWidth = j;
                }
            }

    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return this.listEntries[index];
    }

    @Override
    protected int getSize() {
        return this.listEntries.length;
    }


    @SideOnly(Side.CLIENT)
    public class KeyEntry implements GuiListExtended.IGuiListEntry {
        private final BoundKey boundKey;

        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnRemoveKeyBinding;

        private boolean deleted=false;

        public KeyEntry(BoundKey bind) {
            this.boundKey = bind;
            this.keyDesc = bind.getCommand();
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, bind.getCommand());
            this.btnRemoveKeyBinding = new GuiButton(0, 0, 0, 20, 20, "X");
        }

        @Override
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {

        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            if(!deleted) {
                boolean flag = this.boundKey.equals(GuiKeyBindingsListing.this.guiKeybindings.boundKey);

                GuiKeyBindingsListing.this.mc.fontRendererObj.drawString(this.keyDesc, x + 90 - GuiKeyBindingsListing.this.maxListLabelWidth, y + slotHeight / 2 - GuiKeyBindingsListing.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
                this.btnChangeKeyBinding.xPosition = x + 105;
                this.btnChangeKeyBinding.yPosition = y;
                this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.boundKey.getKeyCode());

                this.btnRemoveKeyBinding.xPosition = x + 180;
                this.btnRemoveKeyBinding.yPosition = y;
                this.btnRemoveKeyBinding.enabled = true;
                this.btnRemoveKeyBinding.drawButton(GuiKeyBindingsListing.this.mc, mouseX, mouseY);

                boolean flag1=false;

                if (this.boundKey.getKeyCode() != 0)
                {
                    for (KeyBinding keybinding : GuiKeyBindingsListing.this.mc.gameSettings.keyBindings)
                    {
                        if (keybinding.getKeyCode() == this.boundKey.getKeyCode())
                        {
                            flag1 = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    this.btnChangeKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnChangeKeyBinding.displayString + TextFormatting.WHITE + " <";
                }else if (flag1)
                {
                    this.btnChangeKeyBinding.displayString = TextFormatting.GOLD + this.btnChangeKeyBinding.displayString;
                }

                this.btnChangeKeyBinding.drawButton(GuiKeyBindingsListing.this.mc, mouseX, mouseY);
            }
        }


        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {

            if(this.btnChangeKeyBinding.mousePressed(mc, mouseX, mouseY)){
                GuiKeyBindingsListing.this.guiKeybindings.boundKey = this.boundKey;
                return true;
            }else if(this.btnRemoveKeyBinding.mousePressed(mc, mouseX, mouseY)){
                boundKey.delete();
                deleted=true;
            }
            return false;

        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
        }
    }
}



