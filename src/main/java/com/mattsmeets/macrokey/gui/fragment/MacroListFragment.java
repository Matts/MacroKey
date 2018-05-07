package com.mattsmeets.macrokey.gui.fragment;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.LayerChangedEvent;
import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import com.mattsmeets.macrokey.model.Layer;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.mattsmeets.macrokey.MacroKey.instance;

import java.io.IOException;
import java.util.Set;

public class MacroListFragment extends GuiListExtended {

    private final GuiMacroManagement guiMacroManagement;
    private final GuiListExtended.IGuiListEntry[] listEntries;

    private int[] listLabelLengths;

    public boolean isMaster;

    public LayerInterface currentLayer;

    public MacroListFragment(GuiMacroManagement guiMacroManagement, LayerInterface layer) throws IOException {
        super(guiMacroManagement.mc, guiMacroManagement.width + 45, guiMacroManagement.height, 63, guiMacroManagement.height - 32, 20);

        this.guiMacroManagement = guiMacroManagement;

        if (layer == null) {
            isMaster = true;
        } else {
            isMaster = false;
            this.currentLayer = layer;
        }

        Set<MacroInterface> keys = instance.bindingsRepository.findAllMacros(true);

        this.listEntries = new GuiListExtended.IGuiListEntry[keys.size()];
        this.listLabelLengths = new int[keys.size()];

        int i = 0;

        for (MacroInterface macro : keys) {
            this.listEntries[i] = new MacroListFragment.KeyEntry(macro, i);

            int j = this.mc.fontRenderer.getStringWidth(I18n.format(macro.getCommand()));

            if (j > this.listLabelLengths[i]) {
                this.listLabelLengths[i] = j;
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

        private final MacroInterface macro;

        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnRemoveKeyBinding;
        private final GuiButton btnEdit;

        private final GuiButton btnEnabledInLayer;
        private boolean enabledInLayer;

        private int index;

        private boolean deleted = false;

        public KeyEntry(MacroInterface macro, int i) {
            this.index = i;
            this.macro = macro;

            this.keyDesc = macro.getCommand();
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, macro.getCommand());
            this.btnRemoveKeyBinding = new GuiButton(1, 0, 0, 15, 20, "X");
            this.btnEdit = new GuiButton(2, 0, 0, 30, 20, I18n.format("gui.keybindings.edit"));
            this.btnEnabledInLayer = new GuiButton(3, 0, 0, 75, 20, "Disabled");

            if (currentLayer != null) {
                if (instance.bindingsRepository.isMacroInLayer(this.macro, currentLayer)) {
                    enabledInLayer = true;
                } else {
                    enabledInLayer = false;
                }
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float f) {
            if (!deleted) {
                boolean flag = this.macro.equals(MacroListFragment.this.guiMacroManagement.macroModify);

                MacroListFragment.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - MacroListFragment.this.listLabelLengths[index], y + slotHeight / 2 - MacroListFragment.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);

                if (isMaster) {
                    this.btnChangeKeyBinding.x = x + 95;
                    this.btnChangeKeyBinding.y = y;
                    this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.macro.getKeyCode());

                    this.btnEdit.x = x + 170;
                    this.btnEdit.y = y;
                    this.btnEdit.displayString = I18n.format("gui.keybindings.edit");
                    this.btnEdit.drawButton(MacroListFragment.this.mc, mouseX, mouseY, 0.0f);

                    this.btnRemoveKeyBinding.x = x + 200;
                    this.btnRemoveKeyBinding.y = y;
                    this.btnRemoveKeyBinding.enabled = true;
                    this.btnRemoveKeyBinding.drawButton(MacroListFragment.this.mc, mouseX, mouseY, 0.0f);
                } else {
                    this.btnEnabledInLayer.x = x + 95;
                    this.btnEnabledInLayer.y = y;

                    if (enabledInLayer) {
                        this.btnEnabledInLayer.displayString = "Enabled";
                    } else {
                        this.btnEnabledInLayer.displayString = "Disabled";
                    }

                    this.btnEnabledInLayer.drawButton(MacroListFragment.this.mc, mouseX, mouseY, 0.0f);
                }

                boolean flag1 = false;

                if (this.macro.getKeyCode() != 0) {
                    for (KeyBinding keybinding : MacroListFragment.this.mc.gameSettings.keyBindings) {
                        if (keybinding.getKeyCode() == this.macro.getKeyCode()) {
                            flag1 = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    this.btnChangeKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnChangeKeyBinding.displayString + TextFormatting.WHITE + " <";
                } else if (flag1) {
                    this.btnChangeKeyBinding.displayString = TextFormatting.GOLD + this.btnChangeKeyBinding.displayString;
                }

                this.btnChangeKeyBinding.drawButton(mc, mouseX, mouseY, 0.0f);
            }
        }


        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            /*if (this.btnEdit.mousePressed(mc, mouseX, mouseY)) {
                mc.displayGuiScreen(new GuiCreateKeybinding(GuiKeyBindingsListing.this.guiKeybindings, boundKey));
                return true;
            }*/
            if (this.btnChangeKeyBinding.mousePressed(mc, mouseX, mouseY)) {
                MacroListFragment.this.guiMacroManagement.macroModify = this.macro;
                return true;
            }
            /*if (this.btnRemoveKeyBinding.mousePressed(mc, mouseX, mouseY)) {
                boundKey.delete();
                Layer.removeKeybinding(boundKey);
                deleted = true;
                mc.displayGuiScreen(guiKeybindings);
                return true;
            }
            */
            if (this.btnEnabledInLayer.mousePressed(mc, mouseX, mouseY)) {
                enabledInLayer = !enabledInLayer;
                if (enabledInLayer) {
                    currentLayer.addMacro(this.macro);
                } else {
                    currentLayer.removeMacro(this.macro);
                }

                MinecraftForge.EVENT_BUS.post(new LayerChangedEvent(currentLayer));
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
