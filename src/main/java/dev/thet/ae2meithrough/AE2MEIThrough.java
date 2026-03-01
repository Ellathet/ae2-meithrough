package dev.thet.ae2meithrough;

import dev.thet.ae2meithrough.common.CommonProxy;
import dev.thet.ae2meithrough.common.handler.PacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:appliedenergistics2"
)
public class AE2MEIThrough {


    @SidedProxy(
            clientSide = "dev.thet.ae2meithrough.client.ClientProxy",
            serverSide = "dev.thet.ae2meithrough.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.Instance
    public static AE2MEIThrough INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.registerMessages();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

}
