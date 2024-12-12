package cl.dlab.abm.core.simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyValueObject
{

	private PropertyValue<?>[] properties;

	@SuppressWarnings("rawtypes")
	public PropertyValueObject(ArrayList<PropertyValue<?>> properties)
	{
		ArrayList<PropertyValue> list = new ArrayList<PropertyValue>();
		for (PropertyValue propertyValue : properties)
		{
			if (propertyValue != null && propertyValue.getValue() != null)
			{
				list.add(propertyValue);
			}
		}
		this.properties = list.toArray(new PropertyValue<?>[list.size()]);
	}

	public PropertyValue<?>[] getProperties()
	{
		return properties;
	}

	public ArrayList<HashMap<String, Object>> getAllValues()
	{
		ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
		if (properties.length > 0)
		{
			Object[] values = properties[0].getValue();
			String key = properties[0].getProperty();
			for (Object value : values)
			{
				HashMap<String, Object> hs = new HashMap<String, Object>();
				hs.put(key, value);
				results.add(hs);
			}
			for (int i = 1; i < properties.length; i++)
			{
				PropertyValue<?> prop = properties[i];
				key = prop.getProperty();
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(results);
				results = new ArrayList<HashMap<String, Object>>();
				for (HashMap<String, Object> hs : list)
				{
					for (Object value : prop.getValue())
					{
						HashMap<String, Object> _hs = new HashMap<String, Object>(hs);
						_hs.put(key, value);
						results.add(_hs);
					}
				}
	
			}
		}
		return results;
	}

}
