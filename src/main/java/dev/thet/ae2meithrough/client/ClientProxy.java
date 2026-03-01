package dev.thet.ae2meithrough.client;

import dev.thet.ae2meithrough.client.gui.GuiMolecularAssemblerHandler;
import dev.thet.ae2meithrough.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
  @Override
  public void init() {
    MinecraftForge.EVENT_BUS.register(new GuiMolecularAssemblerHandler());
  }
}