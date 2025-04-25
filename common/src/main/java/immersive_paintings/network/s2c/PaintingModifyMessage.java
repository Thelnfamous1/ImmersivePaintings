package immersive_paintings.network.s2c;

import immersive_paintings.Main;
import immersive_paintings.entity.ImmersivePaintingEntity;
import immersive_paintings.network.PaintingDataMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class PaintingModifyMessage extends PaintingDataMessage {
    public static final CustomPayload.Id<PaintingModifyMessage> ID = new CustomPayload.Id<>(Main.locate("painting_modify_message"));
    public static final PacketCodec<PacketByteBuf, PaintingModifyMessage> STREAM_CODEC = PacketCodec.of(PaintingModifyMessage::encode, PaintingModifyMessage::new);
    public PaintingModifyMessage(ImmersivePaintingEntity painting) {
        super(painting);
    }

    public PaintingModifyMessage(PacketByteBuf b) {
        super(b);
    }

    @Override
    public void receive(PlayerEntity e) {
        Main.networkManager.handlePaintingModifyMessage(this);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
