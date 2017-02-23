package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.object.BoundKey;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/1/2016.
 */
public class GuiCreateKeybinding extends GuiScreen{
    private GuiScreen parentScreen;
    protected String screenTitle = I18n.format("gui.createkeybindings.screenTitle", new Object[0]);
    protected String editTitle = I18n.format("gui.createkeybindings.editKeybinding", new Object[0]);

    private GuiTextField command;

    private GuiButton btnKeyBinding;

    private GuiButton repeatCommand;
    private GuiButton commandActive;


    private GuiButton addButton,cancelButton;

    private boolean changingKey=false;
    private BoundKey result;

    private int toBindOffset = 0,repeatOffset = 0,toExecuteOffset = 0;

    private boolean repeatEnabled=false;
    private boolean isCommandActive=true;

    private boolean editing;

    private int[] maxListLabelWidth;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, !editing ? this.screenTitle : this.editTitle, this.width / 2, 8, 16777215);
        addButton.drawButton(parentScreen.mc, mouseX, mouseY);
        cancelButton.drawButton(parentScreen.mc, mouseX, mouseY);
        this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(this.result.getKeyCode());

        this.repeatCommand.displayString = repeatEnabled ? I18n.format("enabled", new Object[0]) : I18n.format("disabled", new Object[0]);
        this.commandActive.displayString = isCommandActive ? I18n.format("enabled", new Object[0]) : I18n.format("disabled", new Object[0]);

        repeatCommand.drawButton(parentScreen.mc, mouseX, mouseY);
        commandActive.drawButton(parentScreen.mc, mouseX, mouseY);


        this.command.drawTextBox();
        this.drawString(this.fontRendererObj, I18n.format("gui.createkeybindings.commandExecute", new Object[0]), this.width / 2+ 67 - toExecuteOffset, 37, -6250336);
        this.drawString(this.fontRendererObj, I18n.format("gui.createkeybindings.toBind", new Object[0]), this.width / 2 + 67 - toBindOffset, 90, -6250336);

        List<String> list = new ArrayList<String>();
        list.add("Hold to repeat");
        list.add("Enable command");
        int k = 0;
        this.maxListLabelWidth = new int[list.size()];
        for(String string : list) {
            int j = mc.fontRendererObj.getStringWidth(string);

            if (j > this.maxListLabelWidth[k]) {
                this.maxListLabelWidth[k] = j;
            }
            k++;
        }

        this.drawString(this.fontRendererObj, list.get(0), this.width / 2 + 50 - maxListLabelWidth[0] - toBindOffset, 145, -6250336);
        this.drawString(this.fontRendererObj, list.get(1), this.width / 2 + 50 - maxListLabelWidth[1] - toBindOffset, 168, -6250336);


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

    public GuiCreateKeybinding(GuiScreen guiScreen, BoundKey key){
        construct(guiScreen);
        editing =true;
        result = key;
        repeatEnabled = key.isRepeat();
        isCommandActive = key.isActive();

    }

    public GuiCreateKeybinding(GuiScreen guiScreen){
        construct(guiScreen);
        editing=false;
    }

    public void construct(GuiScreen guiScreen){
        this.parentScreen=guiScreen;
        this.result = new BoundKey();

        int toBind = guiScreen.mc.fontRendererObj.getStringWidth(I18n.format("gui.createkeybindings.toBind", new Object[0]));

        if (toBind > this.toBindOffset)
        {
            this.toBindOffset = toBind;
        }

        int execute = guiScreen.mc.fontRendererObj.getStringWidth(I18n.format("gui.createkeybindings.commandExecute", new Object[0]));

        if (execute > this.toExecuteOffset)
        {
            this.toExecuteOffset = execute;
        }

        int repeat = guiScreen.mc.fontRendererObj.getStringWidth(I18n.format("gui.createkeybindings.repeat", new Object[0]));

        if (repeat > this.repeatOffset)
        {
            this.repeatOffset = repeat;
        }
    }

    public void updateScreen()
    {
        this.command.updateCursorCounter();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(addButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.createkeybindings.saveBind", new Object[0])));
        this.buttonList.add(cancelButton = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.cancel", new Object[0])));

        this.buttonList.add(this.btnKeyBinding = new GuiButton(3, this.width / 2 - 75, 100, 150, 20, GameSettings.getKeyDisplayString(0)));
        this.buttonList.add(this.repeatCommand = new GuiButton(4, this.width / 2 - 75, 140, 75, 20, I18n.format("disabled", new Object[0])));
        this.buttonList.add(this.commandActive = new GuiButton(5, this.width / 2 - 75, 163, 75, 20, I18n.format("disabled", new Object[0])));


        this.command = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 50, 200, 20);
        this.command.setFocused(true);
        this.command.setMaxStringLength(Integer.MAX_VALUE);

        if(editing){
            command.setText(result.getCommand());
            this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(result.getKeyCode());
            this.repeatCommand.displayString = repeatEnabled?I18n.format("enabled", new Object[0]):I18n.format("disabled", new Object[0]);
            this.commandActive.displayString = isCommandActive ? I18n.format("enabled", new Object[0]) : I18n.format("disabled", new Object[0]);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            if(command.getText().length()>1){
                if(editing){
                    result.delete();
                }
                result.setCommand(command.getText());
                BoundKey.addKeybinding(result);
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
        }else if (this.command.isFocused())
        {
            this.command.textboxKeyTyped(typedChar, keyCode);
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.command.mouseClicked(mouseX, mouseY, mouseButton);

        if(this.btnKeyBinding.mousePressed(mc, mouseX, mouseY)){
            changingKey=true;
        }
        if(this.repeatCommand.mousePressed(mc, mouseX, mouseY)){
            repeatEnabled = !repeatEnabled;
            result.setRepeat(repeatEnabled);
        }
        if(this.commandActive.mousePressed(mc, mouseX, mouseY)){
            isCommandActive = !isCommandActive;
            result.setActive(isCommandActive);
        }
    }


}
