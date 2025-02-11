package game.engine;

import java.lang.reflect.Constructor;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Component 
{
	public GameObject gameObject;
	public Transform transform;
	public boolean enabled;
	public void start() { }
	public void stop() { }
	public void update() { }
	public void fixedUpdate() { }
	public void onGUI() { }
	public void collide() { }
	
	@SuppressWarnings("unchecked")
	public static Component fromXML(Node component)
	{
		Object obj = null;
		
		try
		{
			NamedNodeMap attributes = component.getAttributes();
			Constructor<Node> c = (Constructor<Node>) Class.forName(attributes.getNamedItem("Type").getNodeValue()).getConstructor(Node.class);
			obj = c.newInstance(component.getFirstChild());
		}
		
		catch(Exception e)
		{
			
		}
		
		return (Component)obj;
	}
}