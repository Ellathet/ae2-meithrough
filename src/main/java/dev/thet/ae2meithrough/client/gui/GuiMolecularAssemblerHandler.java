package dev.thet.ae2meithrough.client.gui;

import appeng.api.parts.IPartHost;
import appeng.api.parts.IPart;
import appeng.parts.misc.PartInterface;
import appeng.tile.crafting.TileMolecularAssembler;
import appeng.tile.misc.TileInterface;
import dev.thet.ae2meithrough.common.handler.PacketHandler;
import dev.thet.ae2meithrough.common.network.PacketOpenMEInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiMolecularAssemblerHandler {
  private ButtonMEInterface activeButton = null;

  private static final int    BUTTON_ID    = 9800;

  private static final int BTN_X_OFFSET = 145;
  private static final int BTN_Y_OFFSET = 16;

  // The ref for GuiMAC
  private static final String GUI_CLASS_NAME = "appeng.client.gui.implementations.GuiMAC";

  private boolean isMolecularAssemblerGui(GuiScreen gui) {
    return gui.getClass().getName().equals(GUI_CLASS_NAME);
  }

  // Inject the btn
  @SubscribeEvent
  public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
    if (!isMolecularAssemblerGui(event.getGui())) return;

    activeButton = null; // reset

    GuiContainer gui = (GuiContainer) event.getGui();
    BlockPos pos = getAssemblerPos(gui);
    if (pos == null || !hasAdjacentInterface(pos)) return;

    int guiLeft = getGuiLeft(gui);
    int guiTop  = getGuiTop(gui);

    activeButton = new ButtonMEInterface(BUTTON_ID, guiLeft + BTN_X_OFFSET, guiTop + BTN_Y_OFFSET);
    event.getButtonList().add(activeButton);
  }

  @SubscribeEvent
  public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
    if (!isMolecularAssemblerGui(event.getGui())) return;
    if (activeButton == null || !activeButton.isMouseOver()) return;

    FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(
      Collections.singletonList("Open ME Interface"),
      event.getMouseX(),
      event.getMouseY(),
      event.getGui().width,
      event.getGui().height,
      -1,
      fr
    );
  }

  @SubscribeEvent
  public void onButtonClick(GuiScreenEvent.ActionPerformedEvent.Pre event) {
    if (!isMolecularAssemblerGui(event.getGui())) return;
    if (event.getButton().id != BUTTON_ID) return;

    GuiContainer gui = (GuiContainer) event.getGui();
    BlockPos pos = getAssemblerPos(gui);
    if (pos == null) return;

    PacketHandler.CHANNEL.sendToServer(new PacketOpenMEInterface(pos));
  }

  private BlockPos getAssemblerPos(GuiContainer gui) {
    try {
      for (Field f : gui.inventorySlots.getClass().getDeclaredFields()) {
        f.setAccessible(true);
        Object val = f.get(gui.inventorySlots);
        if (val instanceof TileMolecularAssembler) {
          return ((TileMolecularAssembler) val).getPos();
        }
      }
      for (Field f : gui.inventorySlots.getClass().getSuperclass().getDeclaredFields()) {
        f.setAccessible(true);
        Object val = f.get(gui.inventorySlots);
        if (val instanceof TileMolecularAssembler) {
          return ((TileMolecularAssembler) val).getPos();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  private boolean hasAdjacentInterface(BlockPos pos) {
    World world = Minecraft.getMinecraft().world;
    if (world == null) return false;

    for (EnumFacing face : EnumFacing.VALUES) {
      TileEntity te = world.getTileEntity(pos.offset(face));
      if (te instanceof TileInterface) return true;
      if (te instanceof IPartHost) {
        IPart part = ((IPartHost) te).getPart(face.getOpposite());
        if (part instanceof PartInterface) return true;
      }
    }
    return false;
  }

  private int getGuiLeft(GuiContainer gui) {
    try {
      Field f = GuiContainer.class.getDeclaredField("guiLeft");
      f.setAccessible(true);
      return f.getInt(gui);
    } catch (Exception e) {
      return (gui.width - 176) / 2;
    }
  }

  private int getGuiTop(GuiContainer gui) {
    try {
      Field f = GuiContainer.class.getDeclaredField("guiTop");
      f.setAccessible(true);
      return f.getInt(gui);
    } catch (Exception e) {
      return (gui.height - 166) / 2;
    }
  }
}