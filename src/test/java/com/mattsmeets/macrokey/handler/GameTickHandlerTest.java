package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.event.InGameTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.command.CommandInterface;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import net.minecraft.client.entity.EntityPlayerSP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameTickHandlerTest {

    @Test
    public void testOnKeyEventAddsMacroWhenKeyDown() {
        HashSet set = new HashSet();
        HashSet spySet = spy(set);

        MacroActivationEvent event = mock(MacroActivationEvent.class);

        Macro macro = mock(Macro.class);

        when(event.getMacroState()).thenReturn(MacroActivationEvent.MacroState.KEY_DOWN);
        Set<MacroInterface> macros = Collections.singleton(macro);
        when(event.getMacros()).thenReturn(macros);

        GameTickHandler handler = new GameTickHandler(spySet, null);

        handler.onKeyEvent(event);

        verify(event).getMacros();
        verify(event).getMacroState();
        verify(spySet).addAll(macros);
    }

    @Test
    public void testOnKeyEventAddsMacroWhenKeyUp() {
        HashSet set = new HashSet();
        HashSet spySet = spy(set);

        MacroActivationEvent event = mock(MacroActivationEvent.class);

        Macro macro = mock(Macro.class);

        when(event.getMacroState()).thenReturn(MacroActivationEvent.MacroState.KEY_UP);

        GameTickHandler handler = new GameTickHandler(spySet, null);

        handler.onKeyEvent(event);

        verify(event).getMacroState();
        verify(spySet).removeIf(any());
    }

    @Test
    public void testOnExecutorEventWillAddExecutor() {
        HashSet set = new HashSet();
        HashSet spySet = spy(set);

        ExecuteOnTickEvent event = mock(ExecuteOnTickEvent.class);

        Macro macro = mock(Macro.class);

        GameTickHandler handler = new GameTickHandler(null, spySet);

        ExecuteOnTickInterface executor = (boolean delay) -> {
        };

        when(event.getExecutor()).thenReturn(executor);

        handler.onExecutorEvent(event);

        verify(event).getExecutor();
        verify(spySet).add(executor);
    }

    @Test
    public void testOnTickWillClearExecutors() {
        HashSet macroSet = new HashSet();
        macroSet = spy(macroSet);

        HashSet executorSet = new HashSet();
        executorSet = spy(executorSet);

        InGameTickEvent event = mock(InGameTickEvent.class);
        Macro macro = mock(Macro.class);
        ExecuteOnTickInterface executor = mock(ExecuteOnTickInterface.class);

        GameTickHandler handler = new GameTickHandler(macroSet, executorSet);

        handler.onTick(event);

        verify(executorSet).clear();
    }

    @Test
    public void testOnTickWillRunAndClearExecutors() {
        HashSet macroSet = new HashSet();
        macroSet = spy(macroSet);

        HashSet executorSet = new HashSet();
        executorSet = spy(executorSet);

        InGameTickEvent event = mock(InGameTickEvent.class);
        Macro macro = mock(Macro.class);
        ExecuteOnTickInterface executor = mock(ExecuteOnTickInterface.class);

        GameTickHandler handler = new GameTickHandler(macroSet, executorSet);

        handler.onExecutorEvent(new ExecuteOnTickEvent(executor));

        handler.onTick(event);

        verify(executor).execute(false);
        verify(executorSet).add(executor);
        verify(executorSet).forEach(any());
        verify(executorSet).clear();
    }

    @Test
    public void testOnTickWillRunAndClearExecutorsLimitedTick() {
        HashSet macroSet = new HashSet();
        macroSet = spy(macroSet);

        HashSet executorSet = new HashSet();
        executorSet = spy(executorSet);

        InGameTickEvent event = mock(InGameTickEvent.class);
        Macro macro = mock(Macro.class);
        ExecuteOnTickInterface executor = mock(ExecuteOnTickInterface.class);

        GameTickHandler handler = new GameTickHandler(macroSet, executorSet);

        when(event.isLimitedTick()).thenReturn(true);

        handler.onExecutorEvent(new ExecuteOnTickEvent(executor));

        handler.onTick(event);

        verify(executor).execute(true);
        verify(executorSet).add(executor);
        verify(executorSet).forEach(any());
        verify(executorSet).clear();
    }

    @Test
    public void testOnTickWillRunNotRepeatingMacros() {
        HashSet macroSet = new HashSet();
        macroSet = spy(macroSet);

        EntityPlayerSP player = mock(EntityPlayerSP.class);

        InGameTickEvent event = mock(InGameTickEvent.class);
        Macro macro = mock(Macro.class);
        Macro macro1 = mock(Macro.class);
        Macro macro2 = mock(Macro.class);

        when(macro.willRepeat()).thenReturn(false);
        when(macro1.willRepeat()).thenReturn(false);
        when(macro2.willRepeat()).thenReturn(true);

        CommandInterface command = mock(CommandInterface.class);

        when(macro.getCommand()).thenReturn(command);
        when(macro1.getCommand()).thenReturn(command);

        HashSet inputMacros = new HashSet();
        inputMacros.add(macro);
        inputMacros.add(macro1);
        inputMacros.add(macro2);

        HashSet expectedMacros = new HashSet();
        inputMacros.add(macro);
        inputMacros.add(macro1);

        MacroActivationEvent macroActivationEvent = mock(MacroActivationEvent.class);
        GameTickHandler handler = new GameTickHandler(macroSet, null);

        when(macroActivationEvent.getMacroState()).thenReturn(MacroActivationEvent.MacroState.KEY_DOWN);
        when(macroActivationEvent.getMacros()).thenReturn(inputMacros);
        when(event.getCurrentPlayer()).thenReturn(player);
        when(event.isLimitedTick()).thenReturn(false);

        handler.onKeyEvent(macroActivationEvent);

        handler.onTick(event);

        verify(command, times(2)).execute(player);
        verify(macroSet).addAll(inputMacros);
        verify(macroSet).removeIf(any());
    }

    @Test
    public void testOnTickWillRunRepeatingMacrosOnLimitedTick() {
        HashSet macroSet = new HashSet();
        macroSet = spy(macroSet);

        EntityPlayerSP player = mock(EntityPlayerSP.class);

        InGameTickEvent event = mock(InGameTickEvent.class);
        Macro macro = mock(Macro.class);
        Macro macro1 = mock(Macro.class);
        Macro macro2 = mock(Macro.class);

        when(macro.willRepeat()).thenReturn(false);
        when(macro1.willRepeat()).thenReturn(false);
        when(macro2.willRepeat()).thenReturn(true);

        CommandInterface command = mock(CommandInterface.class);

        when(macro.getCommand()).thenReturn(command);
        when(macro1.getCommand()).thenReturn(command);
        when(macro2.getCommand()).thenReturn(command);

        HashSet inputMacros = new HashSet();
        inputMacros.add(macro);
        inputMacros.add(macro1);
        inputMacros.add(macro2);

        HashSet expectedMacros = new HashSet();
        inputMacros.add(macro);
        inputMacros.add(macro1);

        MacroActivationEvent macroActivationEvent = mock(MacroActivationEvent.class);
        GameTickHandler handler = new GameTickHandler(macroSet, null);

        when(macroActivationEvent.getMacroState()).thenReturn(MacroActivationEvent.MacroState.KEY_DOWN);
        when(macroActivationEvent.getMacros()).thenReturn(inputMacros);
        when(event.getCurrentPlayer()).thenReturn(player);
        when(event.isLimitedTick()).thenReturn(true);

        handler.onKeyEvent(macroActivationEvent);

        handler.onTick(event);

        verify(command, times(3)).execute(player);
        verify(macroSet).addAll(inputMacros);
        verify(macroSet).removeIf(any());
    }

}
