package mata.macrokey.gui;

import mata.macrokey.object.BoundKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

import java.io.IOException;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiKeybindings extends GuiScreen implements GuiYesNoCallback {

    private GuiKeyBindingsListing keyBindingList;

    private GuiScreen parentScreen;
    private GameSettings options;

    public BoundKey boundKey;

    protected String screenTitle = I18n.format("gui.keybindings.screenTitle", new Object[0]);

    public GuiKeybindings(GuiScreen screen, GameSettings settings){
        this.parentScreen = screen;
        this.options = settings;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        guiButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        buttonAdd.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 8, 16777215);



    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if(button.id == 1){
            this.mc.displayGuiScreen(new GuiCreateKeybinding(this));
        }
    }

    private GuiButton guiButton;
    private GuiButton buttonAdd;

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(guiButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.keybindings.addkeybinding", new Object[0])));
        this.keyBindingList = new GuiKeyBindingsListing(this, this.mc);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.boundKey != null)
        {
            if (keyCode == 1)
            {
                this.boundKey.setKeyCode(0);
            }
            else if (keyCode != 0)
            {
                this.boundKey.setKeyCode(keyCode);
            }
            else if (typedChar > 0)
            {
                this.boundKey.setKeyCode(typedChar + 256);
            }

            this.boundKey = null;
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.boundKey != null)
        {
            this.boundKey = null;
        }
        else
        if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton))
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if(guiButton.mousePressed(mc, mouseX, mouseY)){
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.keyBindingList.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
