package com.mattsmeets.macrokey.gui.list;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.gui.GuiCreateKeybinding;
import com.mattsmeets.macrokey.gui.GuiManageKeybindings;
import com.mattsmeets.macrokey.object.BoundKey;
import com.mattsmeets.macrokey.object.Layer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiKeyBindingsListing extends GuiListExtended {

    private final Minecraft mc;
    private final GuiManageKeybindings guiKeybindings;
    private final GuiListExtended.IGuiListEntry[] listEntries;

    private int[] maxListLabelWidth;

    private List<BoundKey> boundKeyList;

    public boolean isMaster;

    public Layer currentLayer;

    public GuiKeyBindingsListing(GuiManageKeybindings controls, Minecraft mcIn, Layer layer) {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.mc = mcIn;
        this.guiKeybindings = controls;

        boundKeyList = MacroKey.instance.boundKeys;

        if(layer==null){
            isMaster=true;
        }else{
            isMaster=false;
            this.currentLayer=layer;
            /*boundKeyList = new ArrayList<BoundKey>();
            for (Layer layer1 : MacroKey.instance.layers) {
                if(layer1 == layer){
                    for (UUID uuid:layer.getBoundKeyList()) {
                        boundKeyList.add(BoundKey.getKeyfromUUID(uuid));
                    }
                }
            }*/


        }


        this.listEntries = new GuiListExtended.IGuiListEntry[boundKeyList.size()];
        this.maxListLabelWidth = new int[boundKeyList.size()];

        int i = 0;

        for(BoundKey bind : boundKeyList){
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

        private final GuiButton btnEnabledInLayer;
        private boolean enabledInLayer;

        private int index;

        private boolean deleted=false;

        public KeyEntry(BoundKey bind, int i) {
            this.index=i;
            this.boundKey = bind;

            this.keyDesc = bind.getCommand();
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, bind.getCommand());
            this.btnRemoveKeyBinding = new GuiButton(1, 0, 0, 15, 20, "X");
            this.btnEdit = new GuiButton(2, 0, 0, 30, 20, I18n.format("gui.keybindings.edit"));
            this.btnEnabledInLayer = new GuiButton(3,  0, 0, 75, 20, "Disabled");

            if(currentLayer!=null){
                if(currentLayer.containsKey(boundKey)){
                    enabledInLayer=true;
                }else{
                    enabledInLayer=false;
                }
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float f) {
            if(!deleted) {
                boolean flag = this.boundKey.equals(GuiKeyBindingsListing.this.guiKeybindings.boundKey);

                GuiKeyBindingsListing.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - GuiKeyBindingsListing.this.maxListLabelWidth[index], y + slotHeight / 2 - GuiKeyBindingsListing.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);

                if(isMaster) {
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
                }else{
                    this.btnEnabledInLayer.x = x + 95;
                    this.btnEnabledInLayer.y = y;

                    if(enabledInLayer){
                        this.btnEnabledInLayer.displayString = "Enabled";
                    }else {
                        this.btnEnabledInLayer.displayString = "Disabled";
                    }

                    this.btnEnabledInLayer.drawButton(GuiKeyBindingsListing.this.mc, mouseX, mouseY, 0.0f);
                }

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
                Layer.removeKeybinding(boundKey);
                deleted=true;
                mc.displayGuiScreen(guiKeybindings);
                return true;
            }
            if(this.btnEnabledInLayer.mousePressed(mc, mouseX, mouseY)){
                enabledInLayer=!enabledInLayer;
                if(enabledInLayer){
                    currentLayer.addBindingToList(boundKey);
                }else{
                    currentLayer.removeBindingFromList(boundKey);
                }
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



