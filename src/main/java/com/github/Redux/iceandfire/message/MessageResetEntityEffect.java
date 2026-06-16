package com.github.Redux.iceandfire.message;

import com.github.Redux.iceandfire.api.IEntityEffectCapability;
import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectCapability;
import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectProvider;
import com.github.Redux.iceandfire.capability.entityeffect.EntityEffectStorage;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** Paquete de red para Reset Entity Effect */


public class MessageResetEntityEffect extends AbstractMessage<MessageResetEntityEffect> {
    private int entityId;

    public MessageResetEntityEffect() { }

    public MessageResetEntityEffect(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageResetEntityEffect message, EntityPlayer player, MessageContext messageContext) {
        World world = Minecraft.getMinecraft().world;
        if (world == null) {
            return;
        }
        Entity entity = world.getEntityByID(message.getEntityId());
        if (entity != null) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                IEntityEffectCapability capability = entity.getCapability(EntityEffectProvider.ENTITY_EFFECT, null);
                if (capability != null) {
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setString(EntityEffectStorage.effectId, EntityEffectCapability.EntityEffectEnum.NONE.name());
                    compound.setInteger(EntityEffectStorage.effectTime, 0);
                    compound.setInteger(EntityEffectStorage.effectAdditionalData, 0);
                    EntityEffectProvider.readNBT(capability, null, compound);
                }
            });
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageResetEntityEffect message, EntityPlayer player, MessageContext messageContext) {}
}