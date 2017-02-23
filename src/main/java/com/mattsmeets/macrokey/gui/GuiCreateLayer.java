package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.object.Layer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

/**
 * Created by Matt on 4/1/2016.
 */
public class GuiCreateLayer extends GuiScreen{
    private GuiScreen parentScreen;
    protected String screenTitle = "Create Layer";
    protected String editTitle = "Edit Layer";

    private GuiButton addButton,cancelButton;

    private GuiTextField textFieldName;

    private Layer result;

    private boolean editing;

    private int[] maxListLabelWidth;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, !editing ? this.screenTitle : this.editTitle, this.width / 2, 8, 16777215);
        addButton.drawButton(parentScreen.mc, mouseX, mouseY);
        cancelButton.drawButton(parentScreen.mc, mouseX, mouseY);

        this.textFieldName.drawTextBox();

    }

    public GuiCreateLayer(GuiScreen guiScreen, Layer layer){
        construct(guiScreen);
        editing =true;
        result = layer;
    }

    public GuiCreateLayer(GuiScreen guiScreen){
        construct(guiScreen);
        editing=false;
    }

    public void construct(GuiScreen guiScreen){
        this.parentScreen=guiScreen;
        this.result = new Layer();
    }

    public void updateScreen()
    {
        this.textFieldName.updateCursorCounter();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(addButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, "Save Layer"));
        this.buttonList.add(cancelButton = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.cancel", new Object[0])));

        this.textFieldName = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 50, 200, 20);
        this.textFieldName.setFocused(true);
        this.textFieldName.setMaxStringLength(20);

        if(editing){
            textFieldName.setText(result.getDisplayName());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            if(textFieldName.getText().length()>1){
                if(editing){
                    result.delete();
                }
                result.setDisplayName(textFieldName.getText());
                Layer.addLayer(result);
                this.mc.displayGuiScreen(parentScreen);
            }
        }
        if(button.id == 1){
            this.mc.displayGuiScreen(parentScreen);
        }


    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.textFieldName.isFocused())
        {
            this.textFieldName.textboxKeyTyped(typedChar, keyCode);
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.textFieldName.mouseClicked(mouseX, mouseY, mouseButton);
    }


}
