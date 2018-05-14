package com.mattsmeets.macrokey.gui.fragment;

import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import com.mattsmeets.macrokey.gui.GuiModifyMacro;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class MacroListFragment extends GuiListExtended {

    private final GuiMacroManagement guiMacroManagement;
    private final GuiListExtended.IGuiListEntry[] listEntries;

    private final LayerInterface currentLayer;

    public MacroListFragment(GuiMacroManagement guiMacroManagement, LayerInterface layer) throws IOException {
        super(guiMacroManagement.mc, guiMacroManagement.width + 45, guiMacroManagement.height, 63, guiMacroManagement.height - 32, 20);

        this.guiMacroManagement = guiMacroManagement;
        this.currentLayer = layer;

        List<MacroInterface> macros = instance.bindingsRepository.findAllMacros(true)
                .stream()
                .sorted(Comparator.comparing(MacroInterface::getUMID))
                .collect(Collectors.toList());

        this.listEntries = new GuiListExtended.IGuiListEntry[macros.size()];

        for (int i = 0; i < macros.size(); i++) {
            MacroInterface macro = macros.get(i);

            this.listEntries[i] = new MacroListFragment.KeyEntry(macro);
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

        private final MacroInterface macro;

        private final GuiButton
                btnChangeKeyBinding,
                btnRemoveKeyBinding,
                btnEdit,
                btnEnabledInLayer;

        private boolean enabledInLayer;

        private boolean deleted = false;

        private final String
                removeMacroText = I18n.format("fragment.list.text.remove"),
                editMacroText = I18n.format("edit");

        private final String
                enabledText = I18n.format("enabled"),
                disabledText = I18n.format("disabled");

        private KeyEntry(MacroInterface macro) {
            this.macro = macro;

            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, macro.getCommand());
            this.btnRemoveKeyBinding = new GuiButton(1, 0, 0, 15, 20, this.removeMacroText);
            this.btnEdit = new GuiButton(2, 0, 0, 30, 20, this.editMacroText);
            this.btnEnabledInLayer = new GuiButton(3, 0, 0, 75, 20, this.disabledText);

            if (currentLayer != null) {
                enabledInLayer = instance.bindingsRepository.isMacroInLayer(this.macro, currentLayer);
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float f) {
            if (this.deleted) {
                return;
            }

            boolean macroKeyCodeModifyFlag = this.macro.equals(guiMacroManagement.macroModify);

            mc.fontRenderer.drawString(this.macro.getCommand(), x + 90 - mc.fontRenderer.getStringWidth(macro.getCommand()), y + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215);

            if (currentLayer == null) {
                this.btnChangeKeyBinding.x = x + 95;
                this.btnChangeKeyBinding.y = y;
                this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.macro.getKeyCode());

                this.btnEdit.x = x + 170;
                this.btnEdit.y = y;
                this.btnEdit.drawButton(mc, mouseX, mouseY, 0.0f);

                this.btnRemoveKeyBinding.x = x + 200;
                this.btnRemoveKeyBinding.y = y;
                this.btnRemoveKeyBinding.enabled = true;
                this.btnRemoveKeyBinding.drawButton(mc, mouseX, mouseY, 0.0f);
            } else {
                this.btnEnabledInLayer.x = x + 95;
                this.btnEnabledInLayer.y = y;

                if (enabledInLayer) {
                    this.btnEnabledInLayer.displayString = this.enabledText;
                } else {
                    this.btnEnabledInLayer.displayString = this.disabledText;
                }

                this.btnEnabledInLayer.drawButton(mc, mouseX, mouseY, 0.0f);
            }

            boolean currentKeyAlreadyUsedFlag = false;

            if (this.macro.getKeyCode() != 0) {
                for (KeyBinding keybinding : mc.gameSettings.keyBindings) {
                    if (keybinding.getKeyCode() == this.macro.getKeyCode()) {
                        currentKeyAlreadyUsedFlag = true;
                        break;
                    }
                }
            }

            if (macroKeyCodeModifyFlag) {
                this.btnChangeKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnChangeKeyBinding.displayString + TextFormatting.WHITE + " <";
            } else if (currentKeyAlreadyUsedFlag) {
                this.btnChangeKeyBinding.displayString = TextFormatting.GOLD + this.btnChangeKeyBinding.displayString;
            }

            this.btnChangeKeyBinding.drawButton(mc, mouseX, mouseY, 0.0f);
        }


        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (this.btnEdit.mousePressed(mc, mouseX, mouseY)) {
                mc.displayGuiScreen(new GuiModifyMacro(guiMacroManagement, macro));

                return true;
            }
            if (this.btnChangeKeyBinding.mousePressed(mc, mouseX, mouseY)) {
                guiMacroManagement.macroModify = this.macro;

                return true;
            }
            if (this.btnRemoveKeyBinding.mousePressed(mc, mouseX, mouseY)) {
                try {
                    instance.bindingsRepository.deleteMacro(this.macro, true, true);

                    this.deleted = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mc.displayGuiScreen(guiMacroManagement);
                }

                return true;
            }
            if (this.btnEnabledInLayer.mousePressed(mc, mouseX, mouseY)) {
                enabledInLayer = !enabledInLayer;
                if (enabledInLayer) {
                    currentLayer.addMacro(this.macro);
                } else {
                    currentLayer.removeMacro(this.macro);
                }

                MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerChangedEvent(currentLayer));

                return true;
            }

            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnEdit.mouseReleased(x, y);
        }

        @Override
        public void updatePosition(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }

    }
}
