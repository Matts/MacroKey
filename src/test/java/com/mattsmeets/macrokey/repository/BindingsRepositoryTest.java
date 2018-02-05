package com.mattsmeets.macrokey.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.mattsmeets.macrokey.model.BindingsFile;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.service.JsonConfig;

@RunWith(MockitoJUnitRunner.class)
public class BindingsRepositoryTest {

    @Mock
    public JsonConfig jsonConfig;

    @InjectMocks
    public BindingsRepository bindingsRepository;

    @Test
    public void testFindAllWithSyncFalseGoodCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Macro macro = mock(Macro.class);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        Set<Macro> input = new HashSet<>();
        input.add(macro);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<Macro> result = this.bindingsRepository.findAllMacros(false);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindAllWithSyncFalseBadCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Macro macro = mock(Macro.class);

        Set<Macro> expectedResult = new HashSet<>();

        Set<Macro> input = new HashSet<>();
        input.add(macro);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<Macro> result = this.bindingsRepository.findAllMacros(false);

        assertNotEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testFindAllWithSyncTrue() throws IOException {
        Macro macro = mock(Macro.class);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        when(jsonConfig.getJSONObject()).thenThrow(new IOException());

        Set<Macro> result = this.bindingsRepository.findAllMacros(true);

        // will never come here, if so it should go red
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindMacroByKeycodeWithSyncFalseGoodCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);

        Macro macro1 = new Macro(1, "testing", true);
        Macro macro2 = new Macro(1, "testing", true);
        Macro macro3 = new Macro(1, "testing", false);
        Macro macro4 = new Macro(2, "testing", true);
        Macro macro5 = new Macro(3, "testing", false);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro1);
        expectedResult.add(macro2);

        Set<Macro> input = new HashSet<>();
        input.add(macro1);
        input.add(macro2);
        input.add(macro3);
        input.add(macro4);
        input.add(macro5);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<Macro> result = this.bindingsRepository.findMacroByKeycode(1, false);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindMacroByKeycodeWithSyncFalseBadCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);

        Macro macro1 = new Macro(1, "testing", true);
        Macro macro2 = new Macro(1, "testing", true);
        Macro macro3 = new Macro(1, "testing", false);
        Macro macro4 = new Macro(2, "testing", true);
        Macro macro5 = new Macro(3, "testing", false);

        Macro macro1Spy = spy(macro1);
        Macro macro2Spy = spy(macro2);
        Macro macro3Spy = spy(macro3);
        Macro macro4Spy = spy(macro4);
        Macro macro5Spy = spy(macro5);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro1Spy);
        // expect to return macro2

        Set<Macro> input = new HashSet<>();
        input.add(macro1Spy);
        input.add(macro2Spy);
        input.add(macro3Spy);
        input.add(macro4Spy);
        input.add(macro5Spy);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<Macro> result = this.bindingsRepository.findMacroByKeycode(1, false);

        verify(macro1Spy, times(1)).getKeyCode();
        verify(macro1Spy, times(1)).isActive();

        verify(macro2Spy, times(1)).getKeyCode();
        verify(macro2Spy, times(1)).isActive();

        verify(macro3Spy, times(1)).getKeyCode();
        verify(macro3Spy, times(1)).isActive();

        verify(macro4Spy, times(1)).getKeyCode();
        verify(macro4Spy, times(0)).isActive();

        verify(macro5Spy, times(1)).getKeyCode();
        verify(macro5Spy, times(0)).isActive();

        verifyNoMoreInteractions(macro1Spy, macro2Spy, macro3Spy, macro4Spy, macro5Spy);

        assertNotEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testFindMacroByKeycodeWithSyncTrue() throws IOException {
        Macro macro = mock(Macro.class);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        when(jsonConfig.getJSONObject()).thenThrow(new IOException());

        Set<Macro> result = this.bindingsRepository.findMacroByKeycode(1, true);

        // will never come here, if so it should go red
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddMacroWithSyncFalse() throws IOException {
        Macro macro = new Macro(10, "/gamemode 1", true);
        BindingsFile file = new BindingsFile(2, new HashSet<>());

        BindingsFile spyFile = spy(file);

        this.bindingsRepository.setBindingsFile(spyFile);

        doCallRealMethod().when(spyFile).addMacro(any());

        this.bindingsRepository.addMacro(macro, false);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        assertEquals(expectedResult, file.getMacros());

        verify(spyFile).addMacro(macro);

        verify(jsonConfig, times(0)).saveObjectToJson(spyFile);

        verifyNoMoreInteractions(spyFile);
    }

    @Test
    public void testAddMacroWithSyncTrue() throws IOException {
        Macro macro = new Macro(10, "/gamemode 1", true);
        BindingsFile file = new BindingsFile(2, new HashSet<>());

        BindingsFile spyFile = spy(file);

        this.bindingsRepository.setBindingsFile(spyFile);

        doCallRealMethod().when(spyFile).addMacro(any());

        this.bindingsRepository.addMacro(macro, true);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        assertEquals(expectedResult, file.getMacros());

        verify(spyFile).addMacro(macro);

        verify(jsonConfig).saveObjectToJson(spyFile);

        verifyNoMoreInteractions(spyFile);
    }

    @Test
    public void testFindVersion() {
        int version = 1000;

        BindingsFile bindingsFile = new BindingsFile(version);
        BindingsFile fileSpy = spy(bindingsFile);

        doCallRealMethod().when(fileSpy).getVersion();

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(version, this.bindingsRepository.findFileVersion());

        verify(fileSpy).getVersion();
    }

    @Test
    public void testSaveConfiguration() throws IOException {
        BindingsFile bindingsFile = new BindingsFile(2, new HashSet<>());

        this.bindingsRepository.setBindingsFile(bindingsFile);

        this.bindingsRepository.saveConfiguration();

        verify(this.jsonConfig).saveObjectToJson(bindingsFile);
    }

    @Test
    public void testUpdateMacroWithSyncFalse() throws IOException {
        BindingsFile file = new BindingsFile(2, new HashSet<>());
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        Macro macro = new Macro(10, "test", false);
        Macro macro1 = new Macro(50, "testing", true);
        Macro newMacro = new Macro(macro.getUMID(), 20, "test2", true, false);

        Macro macroSpy = spy(macro);
        Macro macro1Spy = spy(macro1);
        Macro newMacroSpy = spy(newMacro);

        assertEquals(0, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.addMacro(macroSpy, false);
        this.bindingsRepository.addMacro(macro1Spy, false);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.updateMacro(newMacroSpy, false);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());

        Set<Macro> newMacroSet = new HashSet<>();
        newMacroSet.add(newMacroSpy);

        assertEquals(newMacroSet, this.bindingsRepository.findMacroByKeycode(20, false));

        verify(fileSpy).setMacros(any());

        verify(macroSpy).getUMID();
        verify(macro1Spy).getUMID();
        verify(newMacroSpy, times(2)).getUMID();

        verify(jsonConfig, times(0)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testUpdateMacroWithSyncTrue() throws IOException {
        BindingsFile file = new BindingsFile(2, new HashSet<>());
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        Macro macro = new Macro(10, "test", false);
        Macro macro1 = new Macro(50, "testing", true);
        Macro newMacro = new Macro(macro.getUMID(), 20, "test2", true, false);

        Macro macroSpy = spy(macro);
        Macro macro1Spy = spy(macro1);
        Macro newMacroSpy = spy(newMacro);

        assertEquals(0, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.addMacro(macroSpy, false);
        this.bindingsRepository.addMacro(macro1Spy, false);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.updateMacro(newMacroSpy, true);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());

        Set<Macro> newMacroSet = new HashSet<>();
        newMacroSet.add(newMacroSpy);

        assertEquals(newMacroSet, this.bindingsRepository.findMacroByKeycode(20, false));

        verify(fileSpy).setMacros(any());

        verify(macroSpy).getUMID();
        verify(macro1Spy).getUMID();
        verify(newMacroSpy, times(2)).getUMID();

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteMacroUUIDWithSyncFalse() throws IOException {
        Macro macro1 = new Macro(10, "test", false);
        Macro macro2 = new Macro(10, "test", false);
        Macro macro3 = new Macro(10, "test", false);

        Macro macro1Spy = spy(macro1);
        Macro macro2Spy = spy(macro2);
        Macro macro3Spy = spy(macro3);

        Set<Macro> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1.getUMID(), false);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllMacros(false));

        verify(macro1Spy).getUMID();
        verify(macro2Spy).getUMID();
        verify(macro3Spy).getUMID();
        verify(fileSpy).setMacros(expectedResult);

        verify(jsonConfig, times(0)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteMacroUUIDWithSyncTrue() throws IOException {
        Macro macro1 = new Macro(10, "test", false);
        Macro macro2 = new Macro(10, "test", false);
        Macro macro3 = new Macro(10, "test", false);

        Macro macro1Spy = spy(macro1);
        Macro macro2Spy = spy(macro2);
        Macro macro3Spy = spy(macro3);

        Set<Macro> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1.getUMID(), true);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllMacros(false));

        verify(macro1Spy).getUMID();
        verify(macro2Spy).getUMID();
        verify(macro3Spy).getUMID();
        verify(fileSpy).setMacros(expectedResult);

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteMacroWithSyncFalse() throws IOException {
        Macro macro1 = new Macro(10, "test", false);
        Macro macro2 = new Macro(10, "test", false);
        Macro macro3 = new Macro(10, "test", false);

        Macro macro1Spy = spy(macro1);
        Macro macro2Spy = spy(macro2);
        Macro macro3Spy = spy(macro3);

        Set<Macro> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1, false);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllMacros(false));

        verify(macro1Spy).getUMID();
        verify(macro2Spy).getUMID();
        verify(macro3Spy).getUMID();
        verify(fileSpy).setMacros(expectedResult);

        verify(jsonConfig, times(0)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteMacroWithSyncTrue() throws IOException {
        Macro macro1 = new Macro(10, "test", false);
        Macro macro2 = new Macro(10, "test", false);
        Macro macro3 = new Macro(10, "test", false);

        Macro macro1Spy = spy(macro1);
        Macro macro2Spy = spy(macro2);
        Macro macro3Spy = spy(macro3);

        Set<Macro> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<Macro> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1, true);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllMacros(false));

        verify(macro1Spy).getUMID();
        verify(macro2Spy).getUMID();
        verify(macro3Spy).getUMID();
        verify(fileSpy).setMacros(expectedResult);

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testLoadConfigurationWithNullJsonObject() throws IOException {
        when(jsonConfig.getJSONObject()).thenReturn(null);

        BindingsRepository bindingSpy = spy(bindingsRepository);

        bindingSpy.loadConfiguration();

        verify(bindingSpy).setBindingsFile(any());
        verify(bindingSpy).saveConfiguration();
    }

    @Test
    public void testLoadConfigurationWithJsonObjectButBindingNull() throws IOException {
        this.bindingsRepository.setBindingsFile(null);

        JsonObject object = new JsonObject();
        object.addProperty("version", 20);

        assertEquals(20, object.get("version").getAsInt());

        Macro[] macros = new Macro[] { mock(Macro.class) };

        when(jsonConfig.getJSONObject()).thenReturn(object);
        when(jsonConfig.bindJsonElementToObject(any(), any())).thenReturn(macros);

        BindingsRepository bindingSpy = spy(bindingsRepository);

        bindingSpy.loadConfiguration();

        verify(bindingSpy).setBindingsFile(any());
    }

}
