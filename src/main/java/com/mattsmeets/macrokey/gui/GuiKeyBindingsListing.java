package com.mattsmeets.macrokey.gui;


import com.mattsmeets.macrokey.MacroKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.mattsmeets.macrokey.object.*;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiKeyBindingsListing extends GuiListExtended {

    private final Minecraft mc;
    private final GuiKeybindings guiKeybindings;
    private final GuiListExtended.IGuiListEntry[] listEntries;

    private int[] maxListLabelWidth;

    public GuiKeyBindingsListing(GuiKeybindings controls, Minecraft mcIn) {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.mc = mcIn;
        guiKeybindings = controls;
        this.listEntries = new GuiListExtended.IGuiListEntry[MacroKey.instance.boundKeys.size()];
        this.maxListLabelWidth = new int[MacroKey.instance.boundKeys.size()];

        int i = 0;

        for(BoundKey bind : MacroKey.instance.boundKeys){
                this.listEntries[i] = new GuiKeyBindingsListing.KeyEntry(bind, i);

                int j = mcIn.fontRenderer.getStringWidth(I18n.format(bind.getCommand(), new Object[0]));

                if (j > this.maxListLabelWidth[i])
                {
                    this.maxListLabelWidth[i] = j;
                }
            i++;
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
        private final GuiButton btnEdit;

        private int index;

        private boolean deleted=false;

        public KeyEntry(BoundKey bind, int i) {
            this.index=i;
            this.boundKey = bind;
            this.keyDesc = bind.getCommand();
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, bind.getCommand());
            this.btnRemoveKeyBinding = new GuiButton(1, 0, 0, 15, 20, "X");
            this.btnEdit = new GuiButton(2, 0, 0, 30, 20, I18n.format("gui.keybindings.edit"));
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float f) {
            if(!deleted) {
                boolean flag = this.boundKey.equals(GuiKeyBindingsListing.this.guiKeybindings.boundKey);

                GuiKeyBindingsListing.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - GuiKeyBindingsListing.this.maxListLabelWidth[index], y + slotHeight / 2 - GuiKeyBindingsListing.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
                this.btnChangeKeyBinding.x = x + 95;
                this.btnChangeKeyBinding.y = y;
                this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.boundKey.getKeyCode());

                this.btnEdit.x = x + 170;
                this.btnEdit.y = y;
                this.btnEdit.displayString = I18n.format("gui.keybindings.edit");

                this.btnEdit.drawButton(GuiKeyBindingsListing.this.mc, mouseX, mouseY, 0.0f);

                this.btnRemoveKeyBinding.x = x + 200;
                this.btnRemoveKeyBinding.y = y;
                this.btnRemoveKeyBinding.enabled = true;
                this.btnRemoveKeyBinding.drawButton(GuiKeyBindingsListing.this.mc, mouseX, mouseY, 0.0f);

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

                this.btnChangeKeyBinding.drawButton(GuiKeyBindingsListing.this.mc, mouseX, mouseY, 0.0f);
            }
        }


        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if(this.btnEdit.mousePressed(mc, mouseX, mouseY)){
                mc.displayGuiScreen(new GuiCreateKeybinding(GuiKeyBindingsListing.this.guiKeybindings, boundKey));
                return true;
            }
            if(this.btnChangeKeyBinding.mousePressed(mc, mouseX, mouseY)){
                GuiKeyBindingsListing.this.guiKeybindings.boundKey = this.boundKey;
                return true;
            }
            if(this.btnRemoveKeyBinding.mousePressed(mc, mouseX, mouseY)){
                boundKey.delete();
                deleted=true;
                mc.displayGuiScreen(guiKeybindings);
                return true;
            }

            return false;

        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnEdit.mouseReleased(x,y);
        }

        @Override
        public void updatePosition(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }

    }
}



