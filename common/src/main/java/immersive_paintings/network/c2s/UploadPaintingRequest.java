package immersive_paintings.network.c2s;

import immersive_paintings.Main;
import immersive_paintings.network.SegmentedPaintingMessage;
import immersive_paintings.resources.ByteImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.HashMap;

public class UploadPaintingRequest extends SegmentedPaintingMessage {
    public static final CustomPayload.Id<UploadPaintingRequest> ID = new CustomPayload.Id<>(Main.locate("upload_painting_request"));
    public static final PacketCodec<PacketByteBuf, UploadPaintingRequest> STREAM_CODEC = PacketCodec.of(UploadPaintingRequest::encode, UploadPaintingRequest::new);
    public static final HashMap<String, ByteImage> uploadedImages = new HashMap<>();

    public UploadPaintingRequest(byte[] data, int segment, int totalSegments) {
        super(data, segment, totalSegments);
    }

    public UploadPaintingRequest(PacketByteBuf b) {
        super(b);
    }

    @Override
    protected String getIdentifier(PlayerEntity e) {
        return e.getUuidAsString();
    }

    @Override
    protected void process(PlayerEntity e, ByteImage image) {
        uploadedImages.put(getIdentifier(e), image);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
