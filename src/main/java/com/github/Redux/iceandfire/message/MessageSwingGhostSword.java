package com.github.Redux.iceandfire.message;

import com.github.Redux.iceandfire.event.EventLiving;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/** Paquete de red para Swing Ghost Sword */


public class MessageSwingGhostSword extends AbstractMessage<MessageSwingGhostSword> {

    public MessageSwingGhostSword() {

    }

    @Override
    public void onClientReceived(Minecraft minecraft, MessageSwingGhostSword messageSwingGhostSword, EntityPlayer entityPlayer, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, MessageSwingGhostSword messageSwingGhostSword, EntityPlayer entityPlayer, MessageContext messageContext) {
        EventLiving.onSwingGhostSword(entityPlayer, entityPlayer.getHeldItem(EnumHand.MAIN_HAND));
    }


    public void fromBytes(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }

}