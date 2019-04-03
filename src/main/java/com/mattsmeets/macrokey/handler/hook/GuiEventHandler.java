package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.config.ModState;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Add the switch layer button to the main menu.
 */
public class GuiEventHandler {
    private GuiButton switchButton = null;
    private ModState modState;

    public GuiEventHandler(final ModState modState) {
        this.modState = modState;
    }

    //----------
    // Event handlers
    //----------

    /**
     * Create the switch layer button.
     *
     * @param event The init GUI event.
     */
    @SubscribeEvent
    public void init(final GuiScreenEvent.InitGuiEvent event) {
        final GuiScreen gui = event.getGui();

        if (isNotMainMenu(event.getGui())) return;
        if (isSwitchButtonDisabled()) return;

        switchButton = new GuiButton(
                ModConfig.buttonLayerSwitcherId,
                gui.width / 2 + ModConfig.buttonLayerSwitchSettings[0],
                gui.height / 4 + ModConfig.buttonLayerSwitchSettings[1],
                ModConfig.buttonLayerSwitchSettings[2],
                ModConfig.buttonLayerSwitchSettings[3],
                getLayerButtonLabel(modState.getActiveLayer())
        ) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.displayString = getLayerButtonLabel(modState.nextLayer());
            }
        };

        event.addButton(switchButton);
    }

    /**
     * Handle right click.
     * Open the GUI.
     *
     * @param event The mouse click event.
     */
    @SubscribeEvent(receiveCanceled = true)
    public void mouseClickedEvent(final GuiScreenEvent.MouseClickedEvent.Post event) {
        if (isNotMainMenu(event.getGui())
                || isSwitchButtonDisabled()
                || switchButton == null
                || !switchButton.isMouseOver()) {
            return;
        }

        MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(ExecuteOnTickInterface.openMacroKeyGUI));
    }

    /**
     * Render the tooltip.
     *
     * @param event The draw screen event.
     */
    @SubscribeEvent(receiveCanceled = true)
    public void render(final GuiScreenEvent.DrawScreenEvent.Post event) {
        if (isNotMainMenu(event.getGui())
                || isSwitchButtonDisabled()
                || switchButton == null
                || !switchButton.isMouseOver()) {
            return;
        }

        final MouseHelper mouseHelper = Minecraft.getInstance().mouseHelper;
        event.getGui().drawHoveringText(
                I18n.format("text.layer.hover.right_click"),
                (int) (mouseHelper.getMouseX() / 2),
                (int) (mouseHelper.getMouseY() / 2)
        );
    }

    //----------
    // Helpers
    //----------

    private static boolean isNotMainMenu(final GuiScreen gui) {
        return !(gui instanceof GuiIngameMenu);
    }

    private static boolean isSwitchButtonDisabled() {
        return ModConfig.buttonLayerSwitcherId == -1;
    }

    private static String getLayerButtonLabel(final LayerInterface layer) {
        return I18n.format("text.layer.display",
                layer == null ? I18n.format("text.layer.master") : layer.getDisplayName()
        );
    }
}
