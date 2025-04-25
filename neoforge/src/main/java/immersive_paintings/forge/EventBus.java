package immersive_paintings.forge;

import immersive_paintings.ClientMain;
import immersive_paintings.Main;
import immersive_paintings.ServerDataManager;
import immersive_paintings.network.LazyNetworkManager;
import immersive_paintings.resources.PaintingsLoader;
import immersive_paintings.resources.ServerPaintingManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EventBus {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        LazyNetworkManager.tickServer();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        LazyNetworkManager.tickClient();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().getWorld().isClient) {
            ServerDataManager.playerLoggedOff((ServerPlayerEntity)event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ServerPaintingManager.server = event.getServer();
    }

    public static boolean firstLoad = true;

    @SubscribeEvent
    public static void onClientStart(ClientTickEvent.Pre event) {
        //forge decided to be funny and won't trigger the client load event
        if (firstLoad) {
            ClientMain.postLoad();
            firstLoad = false;
        }
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new PaintingsLoader());
    }
}
