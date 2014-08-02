package com.insane.simpleachievements.client.gui;

import static com.insane.simpleachievements.SimpleAchievements.bookWidth;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.insane.simpleachievements.SimpleAchievements;
import com.insane.simpleachievements.data.DataManager;
import com.insane.simpleachievements.data.Element;

public class ButtonElement extends GuiButton
{
	private static final ResourceLocation texture = new ResourceLocation(SimpleAchievements.MODID.toLowerCase(), "textures/gui/checkboxes.png");

	private final Element element;

	public ButtonElement(int id, int x, int y, int width, Element ele)
	{
		super(id, x, y, width, ele.height, ele.getText());

		this.element = ele;

		this.height += (Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(ele.getText(), width).size() - 1) * 8;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY)
	{
		par1Minecraft.renderEngine.bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int offsetX = 0, offsetY = 0;

		// get offset for state and hover
		if (element.getState())
		{
			offsetY = 20;
		}
		if (mouseX >= xPosition && mouseX <= xPosition + width && mouseY >= yPosition && mouseY <= yPosition + height)
		{
			offsetX = 20;
		}

		Offset offset = getOffsetForPlayer(Minecraft.getMinecraft().thePlayer);
		offsetX += offset.x * 40;
		offsetY += offset.y * 40;

		// don't render icon if not achievement
		if (element.isAchievement)
		{
			drawTexturedModalRect(xPosition, yPosition + (height / 2) - 10, offsetX, offsetY, 20, 20);
		}

		FontRenderer fnt = Minecraft.getMinecraft().fontRenderer;
		int lineNum = getExpectedLines(this.element, width);

		// render the text according to alignment
		switch (element.align)
		{
		case CENTER:
			List<String> lines = fnt.listFormattedStringToWidth(element.getText(), this.width);
			for (int i = 0; i < lines.size(); i++)
			{
				String s = lines.get(i);
				fnt.drawString(s, xPosition + getIconOffset() + (bookWidth / 4) - 20 - (fnt.getStringWidth(s) / 2), (int) (yPosition + (height / 2) - lineNum * 4) + i * 8,
						element.getColorBasedOnState());
			}
			break;
		case LEFT:
			fnt.drawSplitString(element.getText(), xPosition + getIconOffset(), (int) (yPosition + (height / 2) - lineNum * 4), this.width, element.getColorBasedOnState());
			break;
		case RIGHT:
			lines = fnt.listFormattedStringToWidth(element.getText(), this.width);
			for (int i = 0; i < lines.size(); i++)
			{
				String s = lines.get(i);
				fnt.drawSplitString(s, xPosition + 25 + this.width - fnt.getStringWidth(s), (int) (yPosition + (height / 2) - lineNum * 4) + i * 8, this.width, element.getColorBasedOnState());
			}
			break;
		}
	}

	private int getIconOffset()
	{
		return element.isAchievement ? 25 : 0;
	}

	public int getHeight()
	{
		return this.height;
	}
	
	// don't allow the element to be clickable if not an achievement
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		if (element.isAchievement)
		{
			return super.mousePressed(par1Minecraft, par2, par3);
		}
		return false;
	}

	public static int getExpectedLines(Element ele, int width)
	{
		return Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(ele.getText(), width).size();
	}

	private static Offset getOffsetForPlayer(EntityPlayer player)
	{
		return DataManager.instance().getOffsetFor(player.username);
	}
}
