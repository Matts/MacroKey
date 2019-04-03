package com.mattsmeets.macrokey.gui.fragment;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import com.mattsmeets.macrokey.gui.GuiModifyMacro;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MacroListFragment extends GuiListExtended<MacroListFragment.MacroEntry> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final GuiMacroManagement guiMacroManagement;

    private final LayerInterface currentLayer;

    public MacroListFragment(GuiMacroManagement guiMacroManagement, LayerInterface layer) {
        super(guiMacroManagement.mc, guiMacroManagement.width + 45, guiMacroManagement.height, 63, guiMacroManagement.height - 32, 20);

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

    public class MacroEntry extends GuiListExtended.IGuiListEntry<MacroEntry> {
        private final MacroInterface macro;

        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnRemoveKeyBinding;
        private final GuiButton btnEdit;
        private final GuiButton btnEnabledInLayer;

        private final String enabledText = I18n.format("enabled");
        private final String disabledText = I18n.format("disabled");

        private MacroEntry(MacroInterface macro) {
            this.macro = macro;

            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, macro.getCommand().toString()) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    guiMacroManagement.macroModify = macro;
                }
            };

            this.btnRemoveKeyBinding = new GuiButton(1, 0, 0, 15, 20, I18n.format("fragment.list.text.remove")) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    try {
                        MacroKey.bindingsRepository.deleteMacro(macro, true, true);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    } finally {
                        mc.displayGuiScreen(guiMacroManagement);
                    }
                }
            };

            this.btnEdit = new GuiButton(2, 0, 0, 30, 20, I18n.format("edit")) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    mc.displayGuiScreen(new GuiModifyMacro(guiMacroManagement, macro));
                }
            };

            this.btnEnabledInLayer = new GuiButton(3, 0, 0, 75, 20, MacroKey.bindingsRepository.isMacroInLayer(macro, currentLayer) ? this.disabledText : this.enabledText) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    if (MacroKey.bindingsRepository.isMacroInLayer(macro, currentLayer)) {
                        currentLayer.removeMacro(macro);

                        this.displayString = enabledText;
                    } else {
                        currentLayer.addMacro(macro);

                        this.displayString = disabledText;
                    }

                    MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerChangedEvent(currentLayer));
                }
            };
        }

        @Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            boolean macroKeyCodeModifyFlag = this.macro.equals(guiMacroManagement.macroModify);

            // Render macro command
            mc.fontRenderer.drawString(this.macro.getCommand().toString(),
                    getX() + 90f - mc.fontRenderer.getStringWidth(macro.getCommand().toString()),
                    getY() + slotHeight / 2f - mc.fontRenderer.FONT_HEIGHT / 2f,
                    0xFFFFFF);

            if (currentLayer == null) {
                this.btnEdit.x = getX() + 170;
                this.btnEdit.y = getY();
                this.btnEdit.render(mouseX, mouseY, 0.0f);

                this.btnRemoveKeyBinding.x = getX() + 200;
                this.btnRemoveKeyBinding.y = getY();
                this.btnRemoveKeyBinding.render(mouseX, mouseY, 0.0f);

                this.btnChangeKeyBinding.x = getX() + 95;
                this.btnChangeKeyBinding.y = getY();
                this.btnChangeKeyBinding.displayString = getChangeKeyBindingDisplayString(macro, macroKeyCodeModifyFlag);
                this.btnChangeKeyBinding.render(mouseX, mouseY, 0.0f);
            } else {
                this.btnEnabledInLayer.x = getX() + 95;
                this.btnEnabledInLayer.y = getY();
                this.btnEnabledInLayer.render(mouseX, mouseY, 0.0f);
            }
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

        private String getChangeKeyBindingDisplayString(final MacroInterface macro, final boolean macroKeyCodeModifyFlag) {
            final String displayString = GLFW.glfwGetKeyName(macro.getKeyCode(), 0);
            if (macroKeyCodeModifyFlag) {
                return TextFormatting.WHITE + "> " + TextFormatting.YELLOW + displayString + TextFormatting.WHITE + " <";
            } else if (isCurrentKeyAlreadyUsed(macro)) {
                return TextFormatting.GOLD + displayString;
            } else {
                return displayString;
            }
        }

        private boolean isCurrentKeyAlreadyUsed(final MacroInterface macro) {
            if (macro.getKeyCode() != 0) {
                for (KeyBinding keybinding : Minecraft.getInstance().gameSettings.keyBindings) {
                    if (keybinding.getKey().getKeyCode() == macro.getKeyCode()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
