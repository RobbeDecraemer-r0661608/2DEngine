package game.engine;

import java.lang.reflect.Array;
import java.util.*;

public class GameObject 
{
	public String name;
	public String tag;
	public Transform transform;
	private boolean enabled = false;
	@SuppressWarnings("rawtypes")
	private Map<Class, Component> components = new HashMap<Class, Component>();
	
	public GameObject()
	{
		transform = new Transform();
	}
	
	public GameObject(String objectName, String nameTag, Component[] startComponents)
	{
		this(objectName, startComponents);
		tag = nameTag;
	}
	
	public GameObject(String objectName, Component[] startComponents)
	{
		name = objectName;
		transform = new Transform();

		for(int i = 0; i < startComponents.length; i++)
		{
			Component c = startComponents[i];
			c.gameObject = this;
			c.transform = transform;
			c.enabled = true;
			components.put(c.getClass(), c);
		}
	}

	public void start()
	{
		enabled = true;
		Component[] list = getComponents();
		
		for(int i = 0; i < list.length; i++)
		{
			list[i].start();
		}
	}

	public void stop()
	{
		if (!enabled) return;
		Component[] list = getComponents();
		
		for(int i = 0; i < list.length; i++)
		{
			list[i].stop();
		}
	}

	public void update()
	{
		if (!enabled) return;
		Component[] list = getComponents();
		
		for(int i = 0; i < list.length; i++)
		{
			list[i].update();
		}
	}

	public void fixedUpdate()
	{
		if (!enabled) return;
		Component[] list = getComponents();
		
		for(int i = 0; i < list.length; i++)
		{
			list[i].fixedUpdate();
		}
	}

	public void onGUI()
	{
		if (!enabled) return;
		Component[] list = getComponents();
		
		for(int i = 0; i < list.length; i++)
		{
			list[i].onGUI();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public <T extends Component> T addComponent(T component)
	{
		if(components.containsKey(component.getClass()))
			components.remove(component.getClass());

		components.put(component.getClass(), component);
		component = (T)components.get(component.getClass());
		component.gameObject = this;
		component.transform = transform;
		component.enabled = true;
		component.start();
		return component;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Component> T getComponent(Class<T> type)
	{
		for(Class key : components.keySet())
		{
			if(type.isAssignableFrom(key) || key == type)
				return (T)components.get(key);
		}
		
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Component> T[] getComponents(Class<T> type)
	{
		List<T> result = new ArrayList<T>();
		
		for(Class key : components.keySet())
		{
			if(type.isAssignableFrom(key) || key == type)
				result.add((T)components.get(key));
		}
		
		T[] array = (T[])Array.newInstance(type, 0);
		result.toArray(array);
		return array;
	}
	
	public Component[] getComponents()
	{
		Component[] result = new Component[components.size()];
		components.values().toArray(result);
		return result;
	}
	
	public static void destroy(GameObject gameObject) 
	{
		
	}
	
	public static void create(GameObject gameObject, Vector3 position, float rotation) 
	{
		
	}
	
	public static void dontDestroyOnLoad(GameObject gameObject) 
	{
		
	}
}