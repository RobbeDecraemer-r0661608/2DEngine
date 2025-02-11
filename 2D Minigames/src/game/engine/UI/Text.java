package game.engine.UI;

import java.awt.*;
import game.engine.*;

public class Text extends UI
{
	public String content;
	public Rect rect;
	
	public Text(Rect rect, String content)
	{
		this.rect = rect;
		this.content = content;
	}
	
	public void draw(Graphics g, Vector2 offset)
	{
		g.setColor(Color.black);
	    FontMetrics metrics = g.getFontMetrics();
	    int x = (rect.width - metrics.stringWidth(content)) / 2 + rect.x;
	    int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + rect.y;
		g.drawString(content, x, y);
	}
}