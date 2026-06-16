package com.github.Redux.iceandfire.integration.baubles;

import com.github.Redux.iceandfire.integration.CompatLoadUtil;
import com.github.Redux.iceandfire.integration.baubles.client.model.layer.LayerHeadBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;

import java.util.Map;
/** BaublesCompatBridge — Baubles Compat Bridge */


public class BaublesCompatBridge {

    public static void loadBaublesClientModels() {
        if(CompatLoadUtil.isBaublesLoaded()) {
            addRenderLayers();
        }
    }

    public static void addRenderLayers() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();

        addLayersToSkin(skinMap.get("default"));
        addLayersToSkin(skinMap.get("slim"));
    }

    private static void addLayersToSkin(RenderPlayer renderPlayer) {
        renderPlayer.addLayer(new LayerHeadBauble(renderPlayer));
    }
}
