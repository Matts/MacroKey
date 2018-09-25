package com.mattsmeets.macrokey.gui.fragment;

import com.mattsmeets.macrokey.gui.GuiLayerManagement;
import com.mattsmeets.macrokey.gui.GuiModifyLayer;
import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class LayerListFragment extends GuiListExtended {

    private final GuiLayerManagement guiLayerManagement;

    private final GuiListExtended.IGuiListEntry[] listEntries;

    public LayerListFragment(GuiLayerManagement guiLayerManagement) throws IOException {
        super(guiLayerManagement.mc, guiLayerManagement.width + 45, guiLayerManagement.height, 63, guiLayerManagement.height - 32, 20);

        this.guiLayerManagement = guiLayerManagement;

        List<LayerInterface> layers = instance.modState.getLayers(true);

        this.listEntries = new GuiListExtended.IGuiListEntry[layers.size()];

        for (int i = 0; i < layers.size(); i++) {
            LayerInterface layer = layers.get(i);

            this.listEntries[i] = new LayerListFragment.KeyEntry(layer);
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
        private final LayerInterface layer;

        private final String keyDesc;

        private final GuiButton
                btnRemove,
                btnEdit;

        private final String
                removeLayerText = I18n.format("fragment.list.text.remove"),
                editLayerText = I18n.format("edit");

        private boolean deleted = false;

        private KeyEntry(LayerInterface layer) {
            this.layer = layer;
            this.keyDesc = layer.getDisplayName();

            this.btnRemove = new GuiButton(1, 0, 0, 15, 20, this.removeLayerText);
            this.btnEdit = new GuiButton(2, 0, 0, 60, 20, this.editLayerText);
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float f) {
            if (deleted) {
                return;
            }

            mc.fontRenderer.drawString(this.keyDesc, x + 90 - mc.fontRenderer.getStringWidth(layer.getDisplayName()), y + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215);

            this.btnEdit.x = x + 140;
            this.btnEdit.y = y;
            this.btnEdit.displayString = this.editLayerText;

            this.btnEdit.drawButton(mc, mouseX, mouseY, 0.0f);

            this.btnRemove.x = x + 200;
            this.btnRemove.y = y;
            this.btnRemove.enabled = true;
            this.btnRemove.drawButton(mc, mouseX, mouseY, 0.0f);
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (this.btnEdit.mousePressed(mc, mouseX, mouseY)) {
                mc.displayGuiScreen(new GuiModifyLayer(guiLayerManagement, layer));

                return true;
            }

            if (this.btnRemove.mousePressed(mc, mouseX, mouseY)) {
                try {
                    if (instance.modState.getActiveLayer().equals(this.layer))
                        instance.modState.setActiveLayer(null);

                    instance.bindingsRepository.deleteLayer(this.layer, true);

                    this.deleted = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mc.displayGuiScreen(guiLayerManagement);
                }

                return true;
            }

            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnEdit.mouseReleased(x, y);
        }

        @Override
        public void updatePosition(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }
    }

}
