package com.mattsmeets.macrokey.gui.list;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import com.mattsmeets.macrokey.gui.GuiModifyMacro;
import com.mattsmeets.macrokey.gui.button.KeyBindingButton;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

public class MacroListFragment extends ExtendedList<MacroListFragment.Entry> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final GuiMacroManagement guiMacroManagement;

    private final LayerInterface currentLayer;

    public MacroListFragment(GuiMacroManagement guiMacroManagement, LayerInterface layer) {
        super(guiMacroManagement.getMinecraft(), guiMacroManagement.width + 45, guiMacroManagement.height, 63, guiMacroManagement.height - 32, 20);

        this.guiMacroManagement = guiMacroManagement;
        this.currentLayer = layer;

        final List<MacroInterface> macros = MacroKey.bindingsRepository.findAllMacros(true)
                .stream()
                .sorted(Comparator.comparing(MacroInterface::getUMID))
                .collect(Collectors.toList());

        for (final MacroInterface macro : macros) {
            addEntry(new MacroEntry(macro));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends ExtendedList.AbstractListEntry<Entry> {
    }

    public class MacroEntry extends Entry {
        private final MacroInterface macro;

        private final KeyBindingButton btnChangeKeyBinding;
        private final Button btnRemoveKeyBinding;
        private final Button btnEdit;
        private final Button btnEnabledInLayer;

        private final String enabledText = I18n.get("enabled");
        private final String disabledText = I18n.get("disabled");

        private MacroEntry(MacroInterface macro) {
            this.macro = macro;

            this.btnChangeKeyBinding = new KeyBindingButton( 0, 0, 75, 20, new TranslationTextComponent(macro.getCommand().toString()), Button::onPress) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    guiMacroManagement.macroModify = macro;
                }
            };

            this.btnRemoveKeyBinding = new Button(0, 0, 15, 20, new TranslationTextComponent("fragment.list.text.remove"), Button::onPress) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    try {
                        MacroKey.bindingsRepository.deleteMacro(macro, true, true);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    } finally {
                        minecraft.setScreen(guiMacroManagement);
                    }
                }
            };

            this.btnEdit = new Button(0, 0, 30, 20, new TranslationTextComponent("edit"), Button::onPress) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    minecraft.setScreen(new GuiModifyMacro(guiMacroManagement, macro));
                }
            };

            this.btnEnabledInLayer = new Button( 0, 0, 75, 20, new TranslationTextComponent(MacroKey.bindingsRepository.isMacroInLayer(macro, currentLayer) ? this.enabledText : this.disabledText), Button::onPress) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    if (MacroKey.bindingsRepository.isMacroInLayer(macro, currentLayer)) {
                        currentLayer.removeMacro(macro);
                        this.setMessage(new StringTextComponent(disabledText));
                    } else {
                        currentLayer.addMacro(macro);
                        this.setMessage(new StringTextComponent(enabledText));
                    }

                    MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerChangedEvent(currentLayer));
                }
            };
        }

        @Override
        public void render(MatrixStack ps, int entryWidth, int entryHeight, int mouseX, int mouseY, int c, int b, int a, boolean isSelected, float partialTicks) {
            boolean macroKeyCodeModifyFlag = this.macro.equals(guiMacroManagement.macroModify);

            // Render macro command
            minecraft.font.draw(ps, this.macro.getCommand().toString(),
                    mouseX + 90f - minecraft.font.width(macro.getCommand().toString()),
                    (float)(entryHeight + c / 2 - 9 / 2),
                    0xFFFFFF);
            if (currentLayer == null) {
                this.btnEdit.x = mouseX + 170;
                this.btnEdit.y = entryHeight;
                this.btnEdit.render(ps, b, a, partialTicks);

                this.btnRemoveKeyBinding.x = mouseX + 200;
                this.btnRemoveKeyBinding.y = entryHeight;
                this.btnRemoveKeyBinding.render(ps, b, a, partialTicks);

                this.btnChangeKeyBinding.x = mouseX + 95;
                this.btnChangeKeyBinding.y = entryHeight;
                this.btnChangeKeyBinding.updateDisplayString(macro, macroKeyCodeModifyFlag);
                this.btnChangeKeyBinding.render(ps, b, a, partialTicks);
            } else {
                this.btnEnabledInLayer.x = mouseX + 95;
                this.btnEnabledInLayer.y = entryHeight;
                this.btnEnabledInLayer.render(ps, b, a, partialTicks);
            }
        }

        public List<? extends IGuiEventListener> children() {
            return ImmutableList.of(this.btnEdit, this.btnChangeKeyBinding, this.btnRemoveKeyBinding, this.btnEnabledInLayer);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean result = this.btnEdit.mouseClicked(mouseX, mouseY, button);
            result = result || this.btnChangeKeyBinding.mouseClicked(mouseX, mouseY, button);
            result = result || this.btnRemoveKeyBinding.mouseClicked(mouseX, mouseY, button);
            result = result || this.btnEnabledInLayer.mouseClicked(mouseX, mouseY, button);

            return result;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            boolean result = this.btnChangeKeyBinding.mouseReleased(mouseX, mouseY, button);
            result = result || this.btnEdit.mouseReleased(mouseX, mouseY, button);

            return result;
        }
    }
}
