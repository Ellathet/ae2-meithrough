package dev.thet.ae2meithrough.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonMEInterface extends GuiButton {

  private static final ResourceLocation ME_INTERFACE_TEXTURE = new ResourceLocation(
          "appliedenergistics2", "textures/blocks/interface.png"
  );

  public ButtonMEInterface(int id, int x, int y) {
    super(id, x, y, 16, 16, ""); // 16x16 icon button, no text
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    if (!this.visible) return;

    this.hovered = mouseX >= this.x && mouseY >= this.y
            && mouseX < this.x + this.width
            && mouseY < this.y + this.height;

    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.enableBlend();

    mc.getTextureManager().bindTexture(ME_INTERFACE_TEXTURE);
    drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 0, 16, 16, 16, 16);

    if (this.hovered) {
      // Purple
      GlStateManager.color(0.8f, 0.4f, 1.0f, 0.5f);
      drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 0, 16, 16, 16, 16);
    }

    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.disableBlend();
  }
}