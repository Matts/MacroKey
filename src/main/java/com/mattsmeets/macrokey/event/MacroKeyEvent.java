package com.mattsmeets.macrokey.event;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mattsmeets.macrokey.model.Macro;

@SideOnly(Side.CLIENT)
public class MacroKeyEvent extends Event {

    private Set<Macro> macros;
    private EntityPlayerSP currentPlayer;
    private boolean pressed;

    public MacroKeyEvent(Set<Macro> macros, boolean pressed) {
        this.macros = macros;
        this.pressed = pressed;

        this.currentPlayer = Minecraft.getMinecraft().player;
    }

    public Set<Macro> getMacros() {
        return this.macros;
    }

    public EntityPlayerSP getCurrentPlayer() {
        return currentPlayer;
    }

    public static class MacroKeyPressEvent extends MacroKeyEvent {
        public MacroKeyPressEvent(Set<Macro> macros) {
            super(macros, true);
        }
    }

    public static class MacroKeyReleaseEvent extends MacroKeyEvent {
        public MacroKeyReleaseEvent(Set<Macro> macros) {
            super(macros, false);
        }
    }

    public boolean isPressed() {
        return pressed;
    }
}
