package immersive_paintings.network.s2c;

import immersive_paintings.Main;
import immersive_paintings.cobalt.network.Message;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class OpenGuiRequest extends Message {
    public static final CustomPayload.Id<OpenGuiRequest> ID = new CustomPayload.Id<>(Main.locate("open_gui_request"));
    public static final PacketCodec<PacketByteBuf, OpenGuiRequest> STREAM_CODEC = PacketCodec.of(OpenGuiRequest::encode, OpenGuiRequest::new);
    public final Type gui;

    public final int entity;

    public final int minResolution;
    public final int maxResolution;
    public final boolean showOtherPlayersPaintings;
    public final int uploadPermissionLevel;

    public OpenGuiRequest(Type gui, int entity, int minResolution, int maxResolution, boolean showOtherPlayersPaintings, int uploadPermissionLevel) {
        this.gui = gui;
        this.entity = entity;
        this.minResolution = minResolution;
        this.maxResolution = maxResolution;
        this.showOtherPlayersPaintings = showOtherPlayersPaintings;
        this.uploadPermissionLevel = uploadPermissionLevel;
    }

    public OpenGuiRequest(PacketByteBuf b) {
        this.gui = b.readEnumConstant(OpenGuiRequest.Type.class);
        this.entity = b.readInt();
        this.minResolution = b.readInt();
        this.maxResolution = b.readInt();
        this.showOtherPlayersPaintings = b.readBoolean();
        this.uploadPermissionLevel = b.readInt();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeEnumConstant(gui);
        b.writeInt(entity);
        b.writeInt(minResolution);
        b.writeInt(maxResolution);
        b.writeBoolean(showOtherPlayersPaintings);
        b.writeInt(uploadPermissionLevel);
    }

    @Override
    public void receive(PlayerEntity e) {
        Main.networkManager.handleOpenGuiRequest(this);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public enum Type {
        EDITOR,
    }
}
