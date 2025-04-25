package immersive_paintings.cobalt.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;

public abstract class Message implements CustomPayload {
    protected Message() {

    }

    public abstract void encode(PacketByteBuf b);

    public abstract void receive(PlayerEntity e);
}
