package com.mattsmeets.macrokey.gui.list;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.gui.GuiCreateLayer;
import com.mattsmeets.macrokey.gui.GuiManageLayers;
import com.mattsmeets.macrokey.object.Layer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiLayerList extends GuiListExtended {

    private final Minecraft mc;
    private final GuiManageLayers parent;
    private final GuiListExtended.IGuiListEntry[] listEntries;

    private int[] maxListLabelWidth;

    public GuiLayerList(GuiManageLayers controls, Minecraft mcIn) {
        super(mcIn, controls.width + 45, controls.height, 63, controls.height - 32, 20);
        this.mc = mcIn;
        this.parent = controls;
        this.listEntries = new GuiListExtended.IGuiListEntry[MacroKey.instance.layers.size()];
        this.maxListLabelWidth = new int[MacroKey.instance.layers.size()];

        int i = 0;

        for(Layer layer : MacroKey.instance.layers){
            this.listEntries[i] = new GuiLayerList.KeyEntry(layer, i);

            int j = mcIn.fontRendererObj.getStringWidth(I18n.format(layer.getDisplayName(), new Object[0]));

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
        private final Layer layer;

        private final String keyDesc;

        private final GuiButton btnRemove;
        private final GuiButton btnEdit;

        private int index;

        private boolean deleted=false;

        public KeyEntry(Layer layer, int i) {
            this.index=i;
            this.layer = layer;
            this.keyDesc = layer.getDisplayName();

            this.btnRemove = new GuiButton(1, 0, 0, 15, 20, "X");
            this.btnEdit = new GuiButton(2, 0, 0, 60, 20, I18n.format("gui.keybindings.edit"));
        }

        @Override
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {

        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            if(!deleted) {
                GuiLayerList.this.mc.fontRendererObj.drawString(this.keyDesc, x + 90 - GuiLayerList.this.maxListLabelWidth[index], y + slotHeight / 2 - GuiLayerList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);

                this.btnEdit.xPosition = x + 140;
                this.btnEdit.yPosition = y;
                this.btnEdit.displayString = I18n.format("gui.keybindings.edit");

                this.btnEdit.drawButton(GuiLayerList.this.mc, mouseX, mouseY);

                this.btnRemove.xPosition = x + 200;
                this.btnRemove.yPosition = y;
                this.btnRemove.enabled = true;
                this.btnRemove.drawButton(GuiLayerList.this.mc, mouseX, mouseY);
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if(this.btnEdit.mousePressed(mc, mouseX, mouseY)){
                mc.displayGuiScreen(new GuiCreateLayer(GuiLayerList.this.parent, layer));
                return true;
            }
            if(this.btnRemove.mousePressed(mc, mouseX, mouseY)){
                layer.delete();
                deleted=true;
                mc.displayGuiScreen(parent);
            }
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

        }

    }
}



