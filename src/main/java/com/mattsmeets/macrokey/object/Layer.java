package com.mattsmeets.macrokey.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.util.JsonConfig;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Generated("org.jsonschema2pojo")
public class Layer {

    private static Layer activeLayer;

    @SerializedName("layerID")
    @Expose
    private Integer layerID;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("boundKeyList")
    @Expose
    private List<UUID> boundKeyList;

    public Layer(){
        boundKeyList = new ArrayList<UUID>();
    }



    public Integer getLayerID() {
        return layerID;
    }

    public void setLayerID(Integer layerID) {
        this.layerID = layerID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void delete(){
        MacroKey.instance.layers.remove(this);
        MacroKey.instance.jsonConfig.saveLayers();
    }

    public static void addLayer(Layer layer){
        MacroKey.instance.layers.add(layer);
        JsonConfig.saveLayers();
    }

    public List<UUID> getBoundKeyList() {
        return boundKeyList;
    }

    public void setBoundKeyList(List<UUID> boundKeyList) {
        this.boundKeyList = boundKeyList;
        MacroKey.instance.jsonConfig.saveLayers();
    }

    public void removeBindingFromList(BoundKey bind){
        boundKeyList.remove(bind.getUuid());
        MacroKey.instance.jsonConfig.saveLayers();
    }

    public void addBindingToList(BoundKey bind){
        boundKeyList.add(bind.getUuid());
        MacroKey.instance.jsonConfig.saveLayers();
    }

    public boolean containsKey(BoundKey bind){
        return boundKeyList.contains(bind.getUuid());
    }

    public static void removeKeybinding(BoundKey key){
        for (Layer layer :
                MacroKey.instance.layers) {
            if (layer.containsKey(key)) {
                layer.removeBindingFromList(key);
            }
        }
    }

    public List<BoundKey> getKeys(){
        List<BoundKey> list = new ArrayList<BoundKey>();
        for (UUID key : boundKeyList) {
            list.add(BoundKey.getKeyfromUUID(key));
        }
        return list;
    }

    public static void setActiveLayer(Layer layer){
        activeLayer=layer;
    }

    public static Layer getActiveLayer(){
        return activeLayer;
    }

    public static List<BoundKey> getActiveKeys(){
        if(activeLayer==null){
            return MacroKey.instance.boundKeys;
        }
        return activeLayer.getKeys();
    }
}