package game.engine.UI;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import game.engine.Engine;
import game.engine.Image;
import game.engine.Input;
import game.engine.Rect;

public class GUI 
{
	private static BufferedImage button;
	private static BufferedImage field;
	public static Graphics2D graphics;
	
	public static void setLayout(Color color, String button, String field)
	{
		if(Image.loadedImages.containsKey(button))
		{
			ImageIcon img = Image.loadedImages.get(button);
			GUI.button = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics g = GUI.button.createGraphics();
			img.paintIcon(null, g, img.getIconWidth(), img.getIconHeight());
			g.dispose();
		}
		
		else
		{
			try 
			{
				ImageIcon img = new ImageIcon(new Object().getClass().getResource(button));
				GUI.button = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
				Graphics g = GUI.button.createGraphics();
				img.paintIcon(null, g, 0, 0);
				g.dispose();
			} 
			
			catch (Exception e) 
			{
				System.out.println("Failed to load " + button + ": " + e.getMessage());
			}
		}

		if(Image.loadedImages.containsKey(field))
		{
			ImageIcon img = Image.loadedImages.get(field);
			GUI.field = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics g = GUI.field.createGraphics();
			img.paintIcon(null, g, 0, 0);
			g.dispose();
		}
		
		else
		{
			try 
			{
				ImageIcon img = new ImageIcon(new Object().getClass().getResource(field));
				GUI.field = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
				Graphics g = GUI.field.createGraphics();
				img.paintIcon(null, g, img.getIconWidth(), img.getIconHeight());
				g.dispose();
			} 
			
			catch (Exception e) 
			{
				System.out.println("Failed to load " + field + ": " + e.getMessage());
			}
		}
	}
	
	public static void setColor(Color color)
	{
		graphics.setColor(color);
	}
	
	public static void label(Rect rect, String content)
	{
		graphics.drawString(content, rect.x, rect.y);
	}
	
	public static void textArea(Rect rect, String content)
	{

		BufferedImage result = new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D img = (Graphics2D)result.getGraphics();
        img.drawImage(button, 0, 0, Engine.instance);
        img.setColor(Color.darkGray);
        img.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float)Color.darkGray.getAlpha() / 255f));
        img.fillRect(0, 0, button.getWidth(), button.getHeight());
		graphics.drawImage(result, rect.x, rect.y, rect.width, rect.height, Engine.instance);
		
		setColor(Color.white);
	    FontMetrics metrics = graphics.getFontMetrics();
	    int x = (rect.width - metrics.stringWidth(content)) / 2 + rect.x;
	    int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + rect.y;
		graphics.drawString(content, x, y);
	}
	
	public static boolean button(Rect rect, String content)
	{
		if(Engine.instance.getMousePosition() == null) return false;
		boolean hover = Math.abs(Engine.instance.getMousePosition().x) > Math.abs(rect.x) &&
				Math.abs(Engine.instance.getMousePosition().x) < Math.abs(rect.x + rect.width) &&
				Math.abs(Engine.instance.getMousePosition().y) > Math.abs(rect.y + rect.height / 2f) &&
				Math.abs(Engine.instance.getMousePosition().y) < Math.abs(rect.y + rect.height * 1.5f);
		Color color = hover ? new Color(150, 150, 150, 255) : new Color(200, 200, 200, 255);

		BufferedImage result = new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D img = (Graphics2D)result.getGraphics();
        img.drawImage(button, 0, 0, Engine.instance);
        img.setColor(color);
        img.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float)color.getAlpha() / 255f));
        img.fillRect(0, 0, button.getWidth(), button.getHeight());
		graphics.drawImage(result, rect.x, rect.y, rect.width, rect.height, Engine.instance);
		
		setColor(Color.black);
	    FontMetrics metrics = graphics.getFontMetrics();
	    int x = (rect.width - metrics.stringWidth(content)) / 2 + rect.x;
	    int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + rect.y;
		graphics.drawString(content, x, y);
		return hover && Input.getMouseButtonDown(0);
	}
}