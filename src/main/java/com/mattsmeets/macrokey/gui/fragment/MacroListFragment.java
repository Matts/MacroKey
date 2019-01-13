package com.mattsmeets.macrokey.gui.fragment;

import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import com.mattsmeets.macrokey.gui.GuiModifyMacro;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
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
        private final String
                removeMacroText = I18n.format("fragment.list.text.remove"),
                editMacroText = I18n.format("edit");
        private final String
                enabledText = I18n.format("enabled"),
                disabledText = I18n.format("disabled");
        private boolean enabledInLayer;
        private boolean deleted = false;

        private KeyEntry(MacroInterface macro) {
            this.macro = macro;

            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, macro.getCommand().toString());
            this.btnRemoveKeyBinding = new GuiButton(1, 0, 0, 15, 20, this.removeMacroText);
            this.btnEdit = new GuiButton(2, 0, 0, 30, 20, this.editMacroText);
            this.btnEnabledInLayer = new GuiButton(3, 0, 0, 75, 20, this.disabledText);

            if (currentLayer != null) {
                enabledInLayer = instance.bindingsRepository.isMacroInLayer(this.macro, currentLayer);
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            if (this.deleted) {
                return;
            }

            boolean macroKeyCodeModifyFlag = this.macro.equals(guiMacroManagement.macroModify);

            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

            fontRenderer.drawString(this.macro.getCommand().toString(), x + 90 - fontRenderer.getStringWidth(macro.getCommand().toString()), y + slotHeight / 2 - fontRenderer.FONT_HEIGHT / 2, 16777215);

            if (currentLayer == null) {
                this.btnChangeKeyBinding.xPosition = x + 95;
                this.btnChangeKeyBinding.yPosition = y;
                this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.macro.getKeyCode());

                this.btnEdit.xPosition = x + 170;
                this.btnEdit.yPosition = y;
                this.btnEdit.drawButton(mc, mouseX, mouseY);

                this.btnRemoveKeyBinding.xPosition = x + 200;
                this.btnRemoveKeyBinding.yPosition = y;
                this.btnRemoveKeyBinding.enabled = true;
                this.btnRemoveKeyBinding.drawButton(mc, mouseX, mouseY);
            } else {
                this.btnEnabledInLayer.xPosition = x + 95;
                this.btnEnabledInLayer.yPosition = y;

                if (enabledInLayer) {
                    this.btnEnabledInLayer.displayString = this.enabledText;
                } else {
                    this.btnEnabledInLayer.displayString = this.disabledText;
                }

                this.btnEnabledInLayer.drawButton(mc, mouseX, mouseY);
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
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
            } else if (currentKeyAlreadyUsedFlag) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.GOLD + this.btnChangeKeyBinding.displayString;
            }

            this.btnChangeKeyBinding.drawButton(mc, mouseX, mouseY);
        }


        @Override
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
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
    }
}
