package com.mattsmeets.macrokey.gui.list;

import com.google.common.collect.ImmutableList;
import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.gui.GuiLayerManagement;
import com.mattsmeets.macrokey.gui.GuiModifyLayer;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class LayerListFragment extends ContainerObjectSelectionList<LayerListFragment.Entry> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final GuiLayerManagement guiLayerManagement;

    public LayerListFragment(final GuiLayerManagement guiLayerManagement) throws IOException {
        super(guiLayerManagement.getMinecraft(), guiLayerManagement.width + 45, guiLayerManagement.height, 63, guiLayerManagement.height - 32, 20);

        this.guiLayerManagement = guiLayerManagement;

        for (LayerInterface layer : MacroKey.modState.getLayers(true)) {
            addEntry(new LayerEntry(layer));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<LayerListFragment.Entry> {
    }

    public class LayerEntry extends Entry {
        private final LayerInterface layer;

        private final Button btnRemove;
        private final Button btnEdit;

        private LayerEntry(final LayerInterface layer) {
            this.layer = layer;

            this.btnEdit = new Button(0, 0, 60, 20, new TextComponent("edit"), Button::onPress) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    minecraft.setScreen(new GuiModifyLayer(guiLayerManagement, layer));
                }
            };

            this.btnRemove = new Button( 0, 0, 15, 20, new TranslatableComponent("fragment.list.text.remove"), Button::onPress) {
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
                        minecraft.setScreen(guiLayerManagement);
                    }
                }
            };
        }

        @Override
        public void render(PoseStack ps, int entryWidth, int entryHeight, int mouseX, int mouseY, int c, int b, int a, boolean isSelected, float partialTicks) {
            // Render layer name
            minecraft.font.draw(ps, this.layer.getDisplayName(),
                    mouseX + 90f - minecraft.font.width(this.layer.getDisplayName()),
                    (float)(entryHeight + c / 2 - 9 / 2),
                    0xFFFFFF);
            // Render buttons
            this.btnEdit.x = mouseX + 140;
            this.btnEdit.y = entryHeight;
            this.btnEdit.render(ps, b, a, 0.0f);

            this.btnRemove.x = mouseX + 200;
            this.btnRemove.y = entryHeight;
            this.btnRemove.render(ps, b, a, 0.0f);
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            final boolean btnEditResult = this.btnEdit.mouseClicked(mouseX, mouseY, button);
            final boolean btnRemoveResult = this.btnRemove.mouseClicked(mouseX, mouseY, button);

            return btnEditResult || btnRemoveResult;
        }
    }
}
