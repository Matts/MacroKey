package com.mattsmeets.macrokey.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.mattsmeets.macrokey.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.mattsmeets.macrokey.service.JsonConfig;

@RunWith(MockitoJUnitRunner.class)
public class BindingsRepositoryTest {

    @Mock
    public JsonConfig jsonConfig;

    @InjectMocks
    public BindingsRepository bindingsRepository;

    @Test
    public void testFindLayerByUUIDWithSyncFalseGoodCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Layer layer = new Layer("test");

        Set<LayerInterface> input = new HashSet<>();
        input.add(layer);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getLayers()).thenReturn(input);

        LayerInterface result = this.bindingsRepository.findLayerByUUID(layer.getULID(), false);

        assertEquals(layer, result);

        verify(jsonConfig, times(0)).saveObjectToJson(file);
    }

    @Test
    public void testFindLayerByUUIDWithSyncFalseBadCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Layer layer = new Layer("test");
        Layer layer1 = new Layer("Hello World");

        Set<LayerInterface> input = new HashSet<>();
        input.add(layer);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getLayers()).thenReturn(input);

        LayerInterface result = this.bindingsRepository.findLayerByUUID(layer1.getULID(), false);

        assertEquals(null, result);

        verify(jsonConfig, times(0)).saveObjectToJson(file);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindLayerByUUIDWithSyncFalseMultiple() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Layer layer = new Layer("test");
        Layer layer1 = new Layer(layer.getULID(), "Hello World");

        Set<LayerInterface> input = new HashSet<>();
        input.add(layer);
        input.add(layer1);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getLayers()).thenReturn(input);

        LayerInterface result = this.bindingsRepository.findLayerByUUID(layer.getULID(), false);

        assertEquals(null, result);

        verify(jsonConfig, times(0)).saveObjectToJson(file);
    }

    @Test
    public void testFindAllLayersWithSyncFalseGoodCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Layer layer = mock(Layer.class);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer);

        Set<LayerInterface> input = new HashSet<>();
        input.add(layer);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getLayers()).thenReturn(input);

        Set<LayerInterface> result = this.bindingsRepository.findAllLayers(false);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindAllLayersWithSyncFalseBadCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Layer layer = mock(Layer.class);

        Set<LayerInterface> expectedResult = new HashSet<>();

        Set<LayerInterface> input = new HashSet<>();
        input.add(layer);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getLayers()).thenReturn(input);

        Set<LayerInterface> result = this.bindingsRepository.findAllLayers(false);

        assertNotEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testFindAllLayersWithSyncTrue() throws IOException {
        Layer layer = mock(Layer.class);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer);

        when(jsonConfig.getJSONObject()).thenThrow(new IOException());

        Set<LayerInterface> result = this.bindingsRepository.findAllLayers(true);

        // will never come here, if so it should go red
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddLayerWithSyncFalse() throws IOException {
        Macro macro = new Macro(10, "/gamemode 1", true);
        Layer layer = new Layer("Layer 1");
        layer.addMacro(macro);

        BindingsFile file = new BindingsFile(2, new HashSet<>());

        BindingsFile spyFile = spy(file);

        this.bindingsRepository.setBindingsFile(spyFile);

        doCallRealMethod().when(spyFile).addLayer(any());

        this.bindingsRepository.addLayer(layer, false);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer);

        assertEquals(expectedResult, file.getLayers());

        verify(spyFile).addLayer(layer);

        verify(jsonConfig, times(0)).saveObjectToJson(spyFile);

        verifyNoMoreInteractions(spyFile);
    }

    @Test
    public void testAddLayerWithSyncTrue() throws IOException {
        Macro macro = new Macro(10, "/gamemode 1", true);
        Layer layer = new Layer("Layer 1");
        layer.addMacro(macro);

        BindingsFile file = new BindingsFile(2, new HashSet<>());

        BindingsFile spyFile = spy(file);

        this.bindingsRepository.setBindingsFile(spyFile);

        doCallRealMethod().when(spyFile).addLayer(any());

        this.bindingsRepository.addLayer(layer, true);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer);

        assertEquals(expectedResult, file.getLayers());

        verify(spyFile).addLayer(layer);

        verify(jsonConfig).saveObjectToJson(spyFile);

        verifyNoMoreInteractions(spyFile);
    }

    @Test
    public void testUpdateLayerWithSyncFalse() throws IOException {
        BindingsFile file = new BindingsFile(2, new HashSet<>());
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        Layer layer = new Layer("Hello World");
        Layer layer1 = new Layer("Hello World1");
        Layer layer2 = new Layer(layer1.getULID(), "Hello World2");

        Layer layerSpy = spy(layer);
        Layer layer1Spy = spy(layer1);
        Layer layer2Spy = spy(layer2);

        assertEquals(0, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.addLayer(layerSpy, false);
        this.bindingsRepository.addLayer(layer1Spy, false);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.updateLayer(layer2Spy, false);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());
        assertEquals(layer2Spy, this.bindingsRepository.findLayerByUUID(layer1.getULID(), false));

        verify(fileSpy).setLayers(any());

        verify(layerSpy, times(2)).getULID();
        verify(layer1Spy).getULID();
        verify(layer2Spy, times(3)).getULID();

        verify(jsonConfig, times(0)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testUpdateLayerWithSyncTrue() throws IOException {
        BindingsFile file = new BindingsFile(2, new HashSet<>());
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        Layer layer = new Layer("Hello World");
        Layer layer1 = new Layer("Hello World1");
        Layer layer2 = new Layer(layer1.getULID(), "Hello World2");

        Layer layerSpy = spy(layer);
        Layer layer1Spy = spy(layer1);
        Layer layer2Spy = spy(layer2);

        assertEquals(0, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.addLayer(layerSpy, false);
        this.bindingsRepository.addLayer(layer1Spy, false);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.updateLayer(layer2Spy, true);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());

        assertEquals(layer2Spy, this.bindingsRepository.findLayerByUUID(layer1.getULID(), false));

        verify(fileSpy).setLayers(any());

        verify(layerSpy, times(2)).getULID();
        verify(layer1Spy).getULID();
        verify(layer2Spy, times(3)).getULID();

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteLayerUUIDWithSyncFalse() throws IOException {
        Layer layer1 = new Layer("Hello World");
        Layer layer2 = new Layer("Hello World1");
        Layer layer3 = new Layer("Hello World2");

        Layer layer1Spy = spy(layer1);
        Layer layer2Spy = spy(layer2);
        Layer layer3Spy = spy(layer3);

        Set<LayerInterface> layers = new HashSet<>();
        layers.add(layer1Spy);
        layers.add(layer2Spy);
        layers.add(layer3Spy);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer2Spy);
        expectedResult.add(layer3Spy);

        BindingsFile file = new BindingsFile(2, new HashSet<>(), layers);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.deleteLayer(layer1.getULID(), false);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllLayers(false));

        verify(layer1Spy).getULID();
        verify(layer2Spy).getULID();
        verify(layer3Spy).getULID();
        verify(fileSpy).setLayers(expectedResult);

        verify(jsonConfig, times(0)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteLayerUUIDWithSyncTrue() throws IOException {
        Layer layer1 = new Layer("Hello World");
        Layer layer2 = new Layer("Hello World1");
        Layer layer3 = new Layer("Hello World2");

        Layer layer1Spy = spy(layer1);
        Layer layer2Spy = spy(layer2);
        Layer layer3Spy = spy(layer3);

        Set<LayerInterface> layers = new HashSet<>();
        layers.add(layer1Spy);
        layers.add(layer2Spy);
        layers.add(layer3Spy);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer2Spy);
        expectedResult.add(layer3Spy);

        BindingsFile file = new BindingsFile(2, new HashSet<>(), layers);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.deleteLayer(layer1.getULID(), true);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllLayers(false));

        verify(layer1Spy).getULID();
        verify(layer2Spy).getULID();
        verify(layer3Spy).getULID();
        verify(fileSpy).setLayers(expectedResult);

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteLayerWithSyncFalse() throws IOException {
        Layer layer1 = new Layer("Hello World");
        Layer layer2 = new Layer("Hello World1");
        Layer layer3 = new Layer("Hello World2");

        Layer layer1Spy = spy(layer1);
        Layer layer2Spy = spy(layer2);
        Layer layer3Spy = spy(layer3);

        Set<LayerInterface> layers = new HashSet<>();
        layers.add(layer1Spy);
        layers.add(layer2Spy);
        layers.add(layer3Spy);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer2Spy);
        expectedResult.add(layer3Spy);

        BindingsFile file = new BindingsFile(2, new HashSet<>(), layers);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.deleteLayer(layer1, false);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllLayers(false));

        verify(layer1Spy).getULID();
        verify(layer2Spy).getULID();
        verify(layer3Spy).getULID();
        verify(fileSpy).setLayers(expectedResult);

        verify(jsonConfig, times(0)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteLayerWithSyncTrue() throws IOException {
        Layer layer1 = new Layer("Hello World");
        Layer layer2 = new Layer("Hello World1");
        Layer layer3 = new Layer("Hello World2");

        Layer layer1Spy = spy(layer1);
        Layer layer2Spy = spy(layer2);
        Layer layer3Spy = spy(layer3);

        Set<LayerInterface> layers = new HashSet<>();
        layers.add(layer1Spy);
        layers.add(layer2Spy);
        layers.add(layer3Spy);

        Set<LayerInterface> expectedResult = new HashSet<>();
        expectedResult.add(layer2Spy);
        expectedResult.add(layer3Spy);

        BindingsFile file = new BindingsFile(2, new HashSet<>(), layers);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllLayers(false).size());

        this.bindingsRepository.deleteLayer(layer1, true);

        assertEquals(2, this.bindingsRepository.findAllLayers(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllLayers(false));

        verify(layer1Spy).getULID();
        verify(layer2Spy).getULID();
        verify(layer3Spy).getULID();
        verify(fileSpy).setLayers(expectedResult);

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testFindAllMacrosWithSyncFalseGoodCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Macro macro = mock(Macro.class);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        Set<MacroInterface> input = new HashSet<>();
        input.add(macro);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<MacroInterface> result = this.bindingsRepository.findAllMacros(false);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindAllMacrosWithSyncFalseBadCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);
        Macro macro = mock(Macro.class);

        Set<MacroInterface> expectedResult = new HashSet<>();

        Set<MacroInterface> input = new HashSet<>();
        input.add(macro);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<MacroInterface> result = this.bindingsRepository.findAllMacros(false);

        assertNotEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testFindAllMacrosWithSyncTrue() throws IOException {
        Macro macro = mock(Macro.class);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        when(jsonConfig.getJSONObject()).thenThrow(new IOException());

        Set<MacroInterface> result = this.bindingsRepository.findAllMacros(true);

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

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro1);
        expectedResult.add(macro2);

        Set<MacroInterface> input = new HashSet<>();
        input.add(macro1);
        input.add(macro2);
        input.add(macro3);
        input.add(macro4);
        input.add(macro5);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<MacroInterface> result = this.bindingsRepository.findMacroByKeycode(1, null, false);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindMacroByUUIDWithSyncFalseGoodCase() throws IOException {
        BindingsFile file = mock(BindingsFile.class);

        BindingsFile spyFile = spy(file);

        Macro macro1 = new Macro(1, "testing", true);
        Macro macro2 = new Macro(1, "testing", true);
        Macro macro3 = new Macro(1, "testing", false);
        Macro macro4 = new Macro(2, "testing", true);
        Macro macro5 = new Macro(3, "testing", false);

        Set<MacroInterface> input = new HashSet<>();
        input.add(macro1);
        input.add(macro2);
        input.add(macro3);
        input.add(macro4);
        input.add(macro5);

        this.bindingsRepository.setBindingsFile(spyFile);

        when(spyFile.getMacros()).thenReturn(input);

        MacroInterface result = this.bindingsRepository.findMacroByUUID(macro1.getUMID(), false);

        assertEquals(macro1, result);
    }

    @Test
    public void testFindMacroByKeycodeWithSyncFalseAndLayerDependent() throws IOException {
        BindingsFile file = mock(BindingsFile.class);

        Layer layer = new Layer("Hello World!");

        Macro macro1 = new Macro(1, "testing", true);
        Macro macro2 = new Macro(1, "testing", true);
        Macro macro3 = new Macro(1, "testing", false);
        Macro macro4 = new Macro(1, "testing", true);
        Macro macro5 = new Macro(3, "testing", false);

        layer.addMacro(macro1);
        layer.addMacro(macro2);
        layer.addMacro(macro3);
        layer.addMacro(macro4);

        when(file.getLayers()).thenReturn(Collections.singleton(layer));

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro1);
        expectedResult.add(macro2);
        expectedResult.add(macro4);

        Set<MacroInterface> input = new HashSet<>();
        input.add(macro1);
        input.add(macro2);
        input.add(macro3);
        input.add(macro4);
        input.add(macro5);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<MacroInterface> result = this.bindingsRepository.findMacroByKeycode(1, layer, false);

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

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro1Spy);
        // expect to return macro2

        Set<MacroInterface> input = new HashSet<>();
        input.add(macro1Spy);
        input.add(macro2Spy);
        input.add(macro3Spy);
        input.add(macro4Spy);
        input.add(macro5Spy);

        this.bindingsRepository.setBindingsFile(file);

        when(file.getMacros()).thenReturn(input);

        Set<MacroInterface> result = this.bindingsRepository.findMacroByKeycode(1, null, false);

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

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro);

        when(jsonConfig.getJSONObject()).thenThrow(new IOException());

        Set<MacroInterface> result = this.bindingsRepository.findMacroByKeycode(1, null, true);

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

        Set<MacroInterface> expectedResult = new HashSet<>();
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

        Set<MacroInterface> expectedResult = new HashSet<>();
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

        Set<MacroInterface> newMacroSet = new HashSet<>();
        newMacroSet.add(newMacroSpy);

        assertEquals(newMacroSet, this.bindingsRepository.findMacroByKeycode(20, null, false));

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

        Set<MacroInterface> newMacroSet = new HashSet<>();
        newMacroSet.add(newMacroSpy);

        assertEquals(newMacroSet, this.bindingsRepository.findMacroByKeycode(20, null, false));

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

        Set<MacroInterface> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1.getUMID(), false, false);

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

        Set<MacroInterface> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1.getUMID(), true, false);

        assertEquals(2, this.bindingsRepository.findAllMacros(false).size());
        assertEquals(expectedResult, this.bindingsRepository.findAllMacros(false));

        verify(macro1Spy).getUMID();
        verify(macro2Spy).getUMID();
        verify(macro3Spy).getUMID();
        verify(fileSpy).setMacros(expectedResult);

        verify(jsonConfig, times(1)).saveObjectToJson(fileSpy);
    }

    @Test
    public void testDeleteMacroFromLayerWithSyncTrue() throws IOException {
        Macro macro1 = new Macro(10, "test", false);
        Macro macro2 = new Macro(10, "test", false);
        Macro macro3 = new Macro(10, "test", false);

        Layer layer1 = new Layer("testing layer");

        Macro macro1Spy = spy(macro1);
        Macro macro2Spy = spy(macro2);
        Macro macro3Spy = spy(macro3);

        Layer layer1Spy = spy(layer1);

        Set<MacroInterface> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        layer1Spy.addMacro(macro1Spy);
        layer1Spy.addMacro(macro3Spy);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro1Spy);
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        when(fileSpy.getLayers()).thenReturn((Set) new HashSet<Layer>() {{
            add(layer1Spy);
        }});

        this.bindingsRepository.setBindingsFile(fileSpy);

        when(layer1Spy.getMacros()).thenCallRealMethod();

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());
        assertEquals(true, this.bindingsRepository.isMacroInLayer(macro1Spy, layer1Spy));
        assertEquals(false, this.bindingsRepository.isMacroInLayer(macro2Spy, layer1Spy));
        assertEquals(true, this.bindingsRepository.isMacroInLayer(macro3Spy, layer1Spy));

        this.bindingsRepository.deleteMacroFromLayer(macro1Spy.getUMID(), true);

        assertEquals(false, this.bindingsRepository.isMacroInLayer(macro1Spy, layer1Spy));
        assertEquals(false, this.bindingsRepository.isMacroInLayer(macro2Spy, layer1Spy));
        assertEquals(true, this.bindingsRepository.isMacroInLayer(macro3Spy, layer1Spy));
        assertEquals(expectedResult, this.bindingsRepository.findAllMacros(false));

        verify(macro1Spy, times(4)).getUMID();
        verify(macro2Spy, times(2)).getUMID();
        verify(macro3Spy, times(3)).getUMID();

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

        Set<MacroInterface> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1, false, false);

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

        Set<MacroInterface> macros = new HashSet<>();
        macros.add(macro1Spy);
        macros.add(macro2Spy);
        macros.add(macro3Spy);

        Set<MacroInterface> expectedResult = new HashSet<>();
        expectedResult.add(macro2Spy);
        expectedResult.add(macro3Spy);

        BindingsFile file = new BindingsFile(2, macros);
        BindingsFile fileSpy = spy(file);

        this.bindingsRepository.setBindingsFile(fileSpy);

        assertEquals(3, this.bindingsRepository.findAllMacros(false).size());

        this.bindingsRepository.deleteMacro(macro1, true, false);

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

        Macro[] macros = new Macro[]{mock(Macro.class)};
        Layer[] layers = new Layer[]{mock(Layer.class)};

        when(jsonConfig.getJSONObject()).thenReturn(object);
        when(jsonConfig.bindJsonElementToObject(eq(Macro[].class), any())).thenReturn(macros);
        when(jsonConfig.bindJsonElementToObject(eq(Layer[].class), any())).thenReturn(layers);

        BindingsRepository bindingSpy = spy(bindingsRepository);

        bindingSpy.loadConfiguration();

        verify(bindingSpy).setBindingsFile(any());
    }

}
