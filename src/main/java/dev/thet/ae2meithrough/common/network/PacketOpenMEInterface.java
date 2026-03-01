package dev.thet.ae2meithrough.common.network;

import appeng.api.parts.IPartHost;
import appeng.parts.misc.PartInterface;
import appeng.tile.crafting.TileMolecularAssembler;
import appeng.tile.misc.TileInterface;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenMEInterface implements IMessage {
  private BlockPos assemblerPos;

  // Just because forge requires it
  public PacketOpenMEInterface() {}

  public PacketOpenMEInterface(BlockPos pos) {
    this.assemblerPos = pos;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    assemblerPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(assemblerPos.getX());
    buf.writeInt(assemblerPos.getY());
    buf.writeInt(assemblerPos.getZ());
  }

  public static class Handler implements IMessageHandler<PacketOpenMEInterface, IMessage> {

    @Override
    public IMessage onMessage(PacketOpenMEInterface message, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().player;
      World world = player.world;

      // This is from Cleanroom
      MinecraftServer server = world.getMinecraftServer();
      if (server == null) return null;

      server.addScheduledTask(() -> {
        TileEntity te = world.getTileEntity(message.assemblerPos);
        if (!(te instanceof TileMolecularAssembler)) return;

        // Scan all faces of molecular
        for (EnumFacing face : EnumFacing.VALUES) {
          BlockPos neighbourPos = message.assemblerPos.offset(face);
          TileEntity neighbour  = world.getTileEntity(neighbourPos);

          // Block-form ME Interface
          if (neighbour instanceof TileInterface) {
            world.getBlockState(neighbourPos).getBlock().onBlockActivated(
              world,
              neighbourPos,
              world.getBlockState(neighbourPos),
              player,
              EnumHand.MAIN_HAND,
              EnumFacing.UP,
              0.5f, 0.5f, 0.5f
            );
            return;
          }

          if (neighbour instanceof IPartHost) {
            IPartHost host = (IPartHost) neighbour;
            appeng.api.parts.IPart part = host.getPart(face.getOpposite());
            if (part instanceof PartInterface) {
              ((PartInterface) part).onActivate(player, EnumHand.MAIN_HAND, new Vec3d(0.5, 0.5, 0.5));
              return;
            }
          }
        }
      });

      return null;
    }
  }
}
