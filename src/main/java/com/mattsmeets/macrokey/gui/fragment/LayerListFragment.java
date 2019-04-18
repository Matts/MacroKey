package com.mattsmeets.macrokey.gui.fragment;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.gui.GuiLayerManagement;
import com.mattsmeets.macrokey.gui.GuiModifyLayer;
import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LayerListFragment extends GuiListExtended<LayerListFragment.LayerEntry> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final GuiLayerManagement guiLayerManagement;

    public LayerListFragment(final GuiLayerManagement guiLayerManagement) throws IOException {
        super(guiLayerManagement.mc, guiLayerManagement.width + 45, guiLayerManagement.height, 63, guiLayerManagement.height - 32, 20);

        this.guiLayerManagement = guiLayerManagement;

        for (LayerInterface layer : MacroKey.modState.getLayers(true)) {
            addEntry(new LayerEntry(layer));
        }
    }

    public class LayerEntry extends GuiListExtended.IGuiListEntry<LayerEntry> {
        private final LayerInterface layer;

        private final GuiButton btnRemove;
        private final GuiButton btnEdit;

        private LayerEntry(final LayerInterface layer) {
            this.layer = layer;

            this.btnEdit = new GuiButton(1, 0, 0, 60, 20, I18n.format("edit")) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    mc.displayGuiScreen(new GuiModifyLayer(guiLayerManagement, layer));
                }
            };

            this.btnRemove = new GuiButton(2, 0, 0, 15, 20, I18n.format("fragment.list.text.remove")) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    try {
                        if (layer.equals(MacroKey.modState.getActiveLayer())) {
                            MacroKey.modState.setActiveLayer(null);
                        }

                        MacroKey.bindingsRepository.deleteLayer(layer, true);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    } finally {
                        mc.displayGuiScreen(guiLayerManagement);
                    }
                }
            };
        }

        @Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            // Render layer name
            mc.fontRenderer.drawString(layer.getDisplayName(),
                    getX() + 90f - mc.fontRenderer.getStringWidth(layer.getDisplayName()),
                    getY() + slotHeight / 2f - mc.fontRenderer.FONT_HEIGHT / 2f,
                    0xFFFFFF);

            // Render buttons
            this.btnEdit.x = getX() + 140;
            this.btnEdit.y = getY();
            this.btnEdit.render(mouseX, mouseY, 0.0f);

            this.btnRemove.x = getX() + 200;
            this.btnRemove.y = getY();
            this.btnRemove.render(mouseX, mouseY, 0.0f);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            final boolean btnEditResult = this.btnEdit.mouseClicked(mouseX, mouseY, button);
            final boolean btnRemoveResult = this.btnRemove.mouseClicked(mouseX, mouseY, button);

            return btnEditResult || btnRemoveResult;
        }
    }
}
