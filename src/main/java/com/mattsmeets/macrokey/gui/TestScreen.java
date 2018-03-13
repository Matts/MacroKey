package com.mattsmeets.macrokey.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

import java.io.IOException;
import java.util.Comparator;

public class TestScreen extends GuiScreen implements GuiYesNoCallback {
    private GuiScreen parentScreen;
    private GameSettings options;

    protected String screenTitle = I18n.format("gui.keybindings.screenTitle", new Object[0]);

    public int currentSelectedLayer;

    private static boolean updateList = false;

    public TestScreen(GuiScreen screen, GameSettings settings) {
        this.parentScreen = screen;
        this.options = settings;
        currentSelectedLayer = -1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawRect(width/2-100, 0, width/2+100, height, -6250336);

        for (GuiButton button :
                this.buttonList) {
            button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);
        }

        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(new CustomButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new CustomButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.keybindings.addkeybinding", new Object[0])));

        this.buttonList.add(new CustomButton(2, this.width / 2 - 155 + 160, 40, 150, 20, "Layer Editor"));
        this.buttonList.add(new CustomButton(3, this.width / 2 - 155, 40, 150, 20, "Switch Layer"));
    }

    private GuiButton buttonDragging = null;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        prevMouseX = -1;
        prevMouseY = -1;

        for (GuiButton button :
                this.buttonList) {
            if (button.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY))
                buttonDragging = button;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        prevMouseX = -1;
        prevMouseY = -1;

        if(buttonDragging != null) {
            buttonDragging.x = width / 2 - 75;
        }

        StringBuilder str = new StringBuilder();

        this.buttonList.sort(Comparator.comparing(o -> o.y));

        for (GuiButton button :
                this.buttonList) {
            str.append(button.displayString).append(", ");
        }

        System.out.println(str);

        buttonDragging = null;
    }

    private int prevMouseX = -1, prevMouseY = -1;

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (buttonDragging != null) {
            if (prevMouseX != -1 && prevMouseY != -1) {
                buttonDragging.x += mouseX - prevMouseX;
                buttonDragging.y += mouseY - prevMouseY;
            }
        }

        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
