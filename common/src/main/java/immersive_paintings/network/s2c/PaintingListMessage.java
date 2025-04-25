package immersive_paintings.network.s2c;

import immersive_paintings.Main;
import immersive_paintings.cobalt.network.Message;
import immersive_paintings.resources.Painting;
import immersive_paintings.resources.ServerPaintingManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class PaintingListMessage extends Message {
    public static final CustomPayload.Id<PaintingListMessage> ID = new CustomPayload.Id<>(Main.locate("painting_list_message"));
    public static final PacketCodec<PacketByteBuf, PaintingListMessage> STREAM_CODEC = PacketCodec.of(PaintingListMessage::encode, PaintingListMessage::new);
    private final Map<String, NbtCompound> paintings = new HashMap<>();
    private final boolean clear;

    public PaintingListMessage() {
        //datapack paintings
        for (Map.Entry<Identifier, Painting> entry : ServerPaintingManager.getDatapackPaintings().entrySet()) {
            this.paintings.put(entry.getKey().toString(), entry.getValue().toNbt());
        }

        //custom paintings
        for (Map.Entry<Identifier, Painting> entry : ServerPaintingManager.get().getCustomServerPaintings().entrySet()) {
            this.paintings.put(entry.getKey().toString(), entry.getValue().toNbt());
        }

        clear = true;
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeInt(paintings.size());
        for (Map.Entry<String, NbtCompound> entry : paintings.entrySet()) {
            b.writeString(entry.getKey());
            b.writeNbt(entry.getValue());
        }

        b.writeBoolean(clear);
    }

    public PaintingListMessage(PacketByteBuf b) {
        int size = b.readInt();
        for (int i = 0; i < size; i++) {
            String key = b.readString();
            NbtCompound value = b.readNbt();
            paintings.put(key, value);
        }

        clear = b.readBoolean();
    }

    public PaintingListMessage(Identifier identifier, Painting painting) {
        this.paintings.put(identifier.toString(), painting == null ? null : painting.toNbt());
        clear = false;
    }

    @Override
    public void receive(PlayerEntity e) {
        Main.networkManager.handlePaintingListResponse(this);
    }

    public Map<Identifier, Painting> getPaintings() {
        Map<Identifier, Painting> paintings = new HashMap<>();
        for (Map.Entry<String, NbtCompound> entry : this.paintings.entrySet()) {
            Identifier identifier = Identifier.of(entry.getKey());
            if (entry.getValue() == null) {
                paintings.put(identifier, null);
            } else {
                paintings.put(identifier, Painting.fromNbt(entry.getValue()));
            }
        }
        return paintings;
    }

    public boolean shouldClear() {
        return clear;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
