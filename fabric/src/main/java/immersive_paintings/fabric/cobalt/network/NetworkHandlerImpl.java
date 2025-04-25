package immersive_paintings.fabric.cobalt.network;

import immersive_paintings.cobalt.network.Message;
import immersive_paintings.cobalt.network.NetworkHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public class NetworkHandlerImpl extends NetworkHandler.Impl {

    @Override
    public <T extends Message> void registerMessage(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec) {
        PayloadTypeRegistry.playS2C().register(id, packetCodec);
        PayloadTypeRegistry.playC2S().register(id, packetCodec);
        ServerPlayNetworking.registerGlobalReceiver(id, (msg, context) -> {
            context.server().execute(() -> msg.receive(context.player()));
        });

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientProxy.register(id, packetCodec);
        }
    }

    @Override
    public void sendToServer(Message msg) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        msg.encode(buf);
        ClientPlayNetworking.send(msg);
    }

    @Override
    public void sendToPlayer(Message msg, ServerPlayerEntity e) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        msg.encode(buf);
        ServerPlayNetworking.send(e, msg);
    }

    // Fabric's APIs are not side-agnostic.
    // We punt this to a separate class file to keep it from being eager-loaded on a server environment.
    private static final class ClientProxy {
        private ClientProxy() {
            throw new RuntimeException("new ClientProxy()");
        }

        public static <T extends Message> void register(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec) {
            ClientPlayNetworking.registerGlobalReceiver(id, (m, context) -> {
                context.client().execute(() -> m.receive(context.player()));
            });
        }
    }
}

