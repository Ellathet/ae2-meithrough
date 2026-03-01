package dev.thet.ae2meithrough.common.handler;

import dev.thet.ae2meithrough.common.network.PacketOpenMEInterface;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
  public static final SimpleNetworkWrapper CHANNEL =
          NetworkRegistry.INSTANCE.newSimpleChannel("meithrough");

  public static void registerMessages() {
    CHANNEL.registerMessage(
            PacketOpenMEInterface.Handler.class,
            PacketOpenMEInterface.class,
            0,
            Side.SERVER
    );
  }
}
