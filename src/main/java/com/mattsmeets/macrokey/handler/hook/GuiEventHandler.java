package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.config.ModState;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Add the switch layer button to the main menu.
 */
public class GuiEventHandler {
    private Button switchButton = null;
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
        final Screen gui = event.getGui();
        if (isNotMainMenu(event.getGui())) return;
        if (isSwitchButtonDisabled()) return;

        switchButton = new Button(
                gui.width / 2 + ModConfig.buttonLayerSwitchSetting1.get(),
                gui.height / 4 + ModConfig.buttonLayerSwitchSetting2.get(),
                ModConfig.buttonLayerSwitchSetting3.get(),
                ModConfig.buttonLayerSwitchSetting4.get(),
                getLayerButtonLabel(modState.getActiveLayer()),
                Button::onPress
        ) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.setMessage(getLayerButtonLabel(modState.nextLayer()));
            }
        };

        event.addWidget(switchButton);
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
                || !switchButton.isMouseOver(event.getMouseX(), event.getMouseY())) {
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
                || !switchButton.isHovered()) {
            return;
        }

        final MouseHandler mouseHelper = Minecraft.getInstance().mouseHandler;
        PoseStack posestack = new PoseStack();
        event.getGui().renderTooltip(
                posestack,
                new TranslatableComponent("text.layer.hover.right_click"),
                (int) (mouseHelper.xpos() / 2),
                (int) (mouseHelper.ypos() / 2));
    }

    //----------
    // Helpers
    //----------

    private static boolean isNotMainMenu(final Screen gui) {
        return !(gui instanceof PauseScreen);
    }

    private static boolean isSwitchButtonDisabled() {
        return ModConfig.buttonLayerSwitcherId.get() == -1;
    }

    private static TranslatableComponent getLayerButtonLabel(final LayerInterface layer) {
        return new TranslatableComponent("text.layer.display",
                layer == null ? I18n.get("text.layer.master") : layer.getDisplayName()
        );
    }
}
