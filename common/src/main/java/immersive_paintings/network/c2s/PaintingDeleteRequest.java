package immersive_paintings.network.c2s;

import immersive_paintings.Main;
import immersive_paintings.cobalt.network.Message;
import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.network.s2c.PaintingListMessage;
import immersive_paintings.resources.ServerPaintingManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class PaintingDeleteRequest extends Message {
    public static final CustomPayload.Id<PaintingDeleteRequest> ID = new CustomPayload.Id<>(Main.locate("painting_delete_request"));
    public static final PacketCodec<PacketByteBuf, PaintingDeleteRequest> STREAM_CODEC = PacketCodec.of(PaintingDeleteRequest::encode, PaintingDeleteRequest::new);
    private final String identifier;

    public PaintingDeleteRequest(Identifier identifier) {
        this.identifier = identifier.toString();
    }

    public PaintingDeleteRequest(PacketByteBuf b) {
        this.identifier = b.readString();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeString(identifier);
    }

    @Override
    public void receive(PlayerEntity e) {
        Identifier identifier = Identifier.of(this.identifier);

        if (ServerPaintingManager.get().getCustomServerPaintings().get(identifier).author.equals(e.getGameProfile().getName()) || e.hasPermissionLevel(4)) {
            Main.LOGGER.info(String.format("Player %s deleted painting %s.", e, identifier));
        } else {
            Main.LOGGER.warn(String.format("Player %s tried to delete an image they do not own.", e));
            return;
        }

        ServerPaintingManager.deregisterPainting(identifier);

        //update clients
        for (ServerPlayerEntity player : Objects.requireNonNull(e.getServer()).getPlayerManager().getPlayerList()) {
            NetworkHandler.sendToPlayer(new PaintingListMessage(identifier, null), player);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
