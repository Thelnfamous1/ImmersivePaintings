package immersive_paintings.network.s2c;

import immersive_paintings.Main;
import immersive_paintings.cobalt.network.Message;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class RegisterPaintingResponse extends Message {
    public static final CustomPayload.Id<RegisterPaintingResponse> ID = new CustomPayload.Id<>(Main.locate("register_painting_response"));
    public static final PacketCodec<PacketByteBuf, RegisterPaintingResponse> STREAM_CODEC = PacketCodec.of(RegisterPaintingResponse::encode, RegisterPaintingResponse::new);
    public final String error;
    public final String identifier;

    public RegisterPaintingResponse(String error, Identifier identifier) {
        this.error = error;
        this.identifier = identifier == null ? "" : identifier.toString();
    }

    public RegisterPaintingResponse(PacketByteBuf b) {
        this.error = b.readString();
        this.identifier = b.readString();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeString(error);
        b.writeString(identifier);
    }

    @Override
    public void receive(PlayerEntity e) {
        Main.networkManager.handleRegisterPaintingResponse(this);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
