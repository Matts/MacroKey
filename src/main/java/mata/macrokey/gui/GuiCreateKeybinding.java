package mata.macrokey.gui;

import mata.macrokey.MacroKey;
import mata.macrokey.object.BoundKey;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

/**
 * Created by Matt on 4/1/2016.
 */
public class GuiCreateKeybinding extends GuiScreen{
    private GuiScreen parentScreen;
    protected String screenTitle = I18n.format("gui.createkeybindings.screenTitle", new Object[0]);

    private GuiButton addButton,cancelButton;
    private GuiButton btnKeyBinding;
    private GuiTextField worldNameField;

    private boolean changingKey=false;
    private BoundKey result;

    private int maxListLabelWidth = 0;


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 8, 16777215);
        addButton.drawButton(parentScreen.mc, mouseX, mouseY);
        cancelButton.drawButton(parentScreen.mc, mouseX, mouseY);
        this.worldNameField.drawTextBox();
        this.drawString(this.fontRendererObj, I18n.format("gui.createkeybindings.commandExecute", new Object[0]), this.width / 2 - 60, 47, -6250336);
        this.drawString(this.fontRendererObj, I18n.format("gui.createkeybindings.toBind", new Object[0]), this.width / 2 + 67 - maxListLabelWidth, 112, -6250336);

        this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(this.result.getKeyCode());

        boolean flag1=false;

        if (this.result.getKeyCode() != 0)
        {
            for (KeyBinding keybinding : mc.gameSettings.keyBindings)
            {
                if (keybinding.getKeyCode() == this.result.getKeyCode())
                {
                    flag1 = true;
                    break;
                }
            }
        }

        if (changingKey) {
            this.btnKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnKeyBinding.displayString + TextFormatting.WHITE + " <";
        }else if (flag1)
        {
            this.btnKeyBinding.displayString = TextFormatting.GOLD + this.btnKeyBinding.displayString;
        }
        this.btnKeyBinding.drawButton(mc, mouseX, mouseY);

    }

    public GuiCreateKeybinding(GuiScreen guiScreen){
        this.parentScreen=guiScreen;
        this.result = new BoundKey();

        int j = guiScreen.mc.fontRendererObj.getStringWidth(I18n.format("gui.createkeybindings.toBind", new Object[0]));

        if (j > this.maxListLabelWidth)
        {
            this.maxListLabelWidth = j;
        }
    }

    public void updateScreen()
    {
        this.worldNameField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(addButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.createkeybindings.saveBind", new Object[0])));
        this.buttonList.add(cancelButton = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.cancel", new Object[0])));

        this.buttonList.add(this.btnKeyBinding = new GuiButton(3, this.width / 2 - 75, 122, 150, 20, GameSettings.getKeyDisplayString(0)));

        this.worldNameField = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.worldNameField.setFocused(true);
        this.worldNameField.setMaxStringLength(100);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            if(worldNameField.getText().length()>1){
                result.setCommand(worldNameField.getText());
                MacroKey.instance.jsonConfig.addKeybinding(result);
                this.mc.displayGuiScreen(parentScreen);
            }
        }
        if(button.id == 1){
            this.mc.displayGuiScreen(parentScreen);
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (changingKey)
        {
            if (keyCode == 1)
            {
                this.result.setKeyCode(0);
            }
            else if (keyCode != 0)
            {
                this.result.setKeyCode(keyCode);
            }
            else if (typedChar > 0)
            {
                this.result.setKeyCode(typedChar + 256);
            }

            this.changingKey = false;
        }else if (this.worldNameField.isFocused())
        {
            this.worldNameField.textboxKeyTyped(typedChar, keyCode);
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.worldNameField.mouseClicked(mouseX, mouseY, mouseButton);

       if(this.btnKeyBinding.mousePressed(mc, mouseX, mouseY)){
           changingKey=true;
       }
    }


}
