package immersive_paintings;


import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.network.c2s.*;
import immersive_paintings.network.s2c.*;

public class Messages {
    public static void bootstrap() {

    }

    static {
        NetworkHandler.registerMessage(ImageRequest.ID, ImageRequest.STREAM_CODEC);
        NetworkHandler.registerMessage(PaintingListMessage.ID, PaintingListMessage.STREAM_CODEC);
        NetworkHandler.registerMessage(ImageResponse.ID, ImageResponse.STREAM_CODEC);
        NetworkHandler.registerMessage(PaintingModifyRequest.ID, PaintingModifyRequest.STREAM_CODEC);
        NetworkHandler.registerMessage(PaintingModifyMessage.ID, PaintingModifyMessage.STREAM_CODEC);
        NetworkHandler.registerMessage(RegisterPaintingRequest.ID, RegisterPaintingRequest.STREAM_CODEC);
        NetworkHandler.registerMessage(PaintingDeleteRequest.ID, PaintingDeleteRequest.STREAM_CODEC);
        NetworkHandler.registerMessage(UploadPaintingRequest.ID, UploadPaintingRequest.STREAM_CODEC);
        NetworkHandler.registerMessage(RegisterPaintingResponse.ID, RegisterPaintingResponse.STREAM_CODEC);
        NetworkHandler.registerMessage(OpenGuiRequest.ID, OpenGuiRequest.STREAM_CODEC);
    }
}
