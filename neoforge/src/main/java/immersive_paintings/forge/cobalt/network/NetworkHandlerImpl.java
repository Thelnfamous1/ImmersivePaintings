package immersive_paintings.forge.cobalt.network;

import immersive_paintings.cobalt.network.Message;
import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.forge.CommonForge;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandlerImpl extends NetworkHandler.Impl {
    private static final String PROTOCOL_VERSION = "1";

    @Override
    public <T extends Message> void registerMessage(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> packetCodec) {
        CommonForge.getModBus().addListener((RegisterPayloadHandlersEvent event) -> {
            PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
            registrar.playBidirectional(id, packetCodec, (msg, context) -> {
                context.enqueueWork(() -> msg.receive(context.player()));
            });
        });
    }

    @Override
    public void sendToServer(Message m) {
        PacketDistributor.sendToServer(m);
    }

    @Override
    public void sendToPlayer(Message m, ServerPlayerEntity e) {
        PacketDistributor.sendToPlayer(e, m);
    }
}
