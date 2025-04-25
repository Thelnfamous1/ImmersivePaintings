package immersive_paintings.cobalt.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class NetworkHandler {
    private static Impl INSTANCE;

    public static <T extends Message> void registerMessage(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec) {
        INSTANCE.registerMessage(id, packetCodec);
    }

    public static void sendToServer(Message m) {
        INSTANCE.sendToServer(m);
    }

    public static void sendToPlayer(Message m, ServerPlayerEntity e) {
        INSTANCE.sendToPlayer(m, e);
    }

    public abstract static class Impl {

        protected Impl() {
            INSTANCE = this;
        }

        public abstract <T extends Message> void registerMessage(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec);

        public abstract void sendToServer(Message m);

        public abstract void sendToPlayer(Message m, ServerPlayerEntity e);
    }
}
