package immersive_paintings.forge;

import immersive_paintings.*;
import immersive_paintings.forge.cobalt.network.NetworkHandlerImpl;
import immersive_paintings.forge.cobalt.registration.RegistrationImpl;
import net.minecraft.item.ItemGroup;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import static net.minecraft.registry.RegistryKeys.ITEM_GROUP;

@Mod(Main.MOD_ID)
@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class CommonForge {
    private static IEventBus modBus;
    public CommonForge(IEventBus modBus, ModContainer container) {
        RegistrationImpl.bootstrap();
        new NetworkHandlerImpl();
        DEF_REG.register(modBus);
        CommonForge.modBus = modBus;
    }

    public static IEventBus getModBus() {
        return modBus;
    }

    @SubscribeEvent
    public static void onRegistryEvent(RegisterEvent event) {
        Items.bootstrap();
        Entities.bootstrap();
        Messages.bootstrap();
    }

    public static final DeferredRegister<ItemGroup> DEF_REG = DeferredRegister.create(ITEM_GROUP, Main.MOD_ID);

    public static final DeferredHolder<ItemGroup, ItemGroup> TAB = DEF_REG.register(Main.MOD_ID, () -> ItemGroup.builder()
            .displayName(ItemGroups.getDisplayName())
            .icon(ItemGroups::getIcon)
            .entries((featureFlags, output) -> output.addAll(Items.getSortedItems()))
            .build()
    );
}
