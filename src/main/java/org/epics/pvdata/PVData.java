package org.epics.pvdata;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializationStrategies;
import org.epics.pvdata.serialize.SerializationStrategy;
import org.epics.pvdata.serialize.SerializeHelper;

public class PVData {

	public static void serialize(ByteBuffer buffer, Object data) 
	{
		serialize(buffer, data, data.getClass());
	}
	
	public static void serialize(ByteBuffer buffer, Object data, Class<?> clazz) 
	{
		for (Field field : clazz.getDeclaredFields())
		{
			// make field accessible even if it's private, protected
			if (!field.isAccessible())
				field.setAccessible(true);
		
			int modifiers = field.getModifiers();
			// note: we do no ignore static
			// we ignore final for non-static classes (they carry this$0 field)
			boolean ignore = Modifier.isFinal(modifiers) || Modifier.isTransient(modifiers);
			if (!ignore)
			{
				try
				{
					Class<?> type = field.getType();
					SerializationStrategy ss = SerializationStrategies.strategiesMap.get(type);
					if (ss != null)
					{
						ss.serialize(buffer, field, data);
					}
					else
					{
						if (type.isPrimitive())
							throw new RuntimeException("unsupported primitive type: " + type);
						else if (type.isArray())
						{
							Object array = field.get(data);
							if (array == null)
								throw new NullPointerException(field.toString());
							
							int len = Array.getLength(array);
							Class<?> elementType = type.getComponentType();
							
							// primitive arrays should be serialized via predefined strategy
							if (elementType.isPrimitive())
								throw new RuntimeException("unsupported primitive array type: " + type);
							// nd-arrays not supported by pvData
							else if (elementType.isArray())
								throw new RuntimeException("nd-arrays not supported: " + type);
								
							//ss = SerializationStrategies.strategiesMap.get(elementType);
								
							SerializeHelper.writeSize(len, buffer);
							for (int i = 0; i < len; i++)
							{
								Object elementData = Array.get(array, i);
								boolean notNull = (elementData != null);
								buffer.put(notNull ? (byte)1 : (byte)0);
								if (notNull)
								{
									//if (ss != null)
									//	ss.serialize(buffer, elementData);
									serialize(buffer, elementData, elementType);
								}
							}
						}
						else if (type.isEnum())
						{
							// TODO extract int value()
							throw new RuntimeException("enums not supported:" + field);
						}
						else
						{
							Object fieldData = field.get(data);
							serialize(buffer, fieldData, type);
						}
					}
				} catch (Throwable e) {
					throw new RuntimeException("failed to serialize: " + field, e);
				}
			}
		}
	}

	
	/*
	public static <T> T deserialize(ByteBuffer buffer, Object data) 
	{
		return (T)deserialize(buffer, data, data.getClass()));
	}
	*/

	public static <T> T deserialize(ByteBuffer buffer, Class<T> clazz) 
	{
		T data;
		try {
			data = clazz.newInstance();
		} catch (Throwable th) {
			throw new RuntimeException("failed to create a new instance of " + clazz, th);
		}
		deserialize(buffer, data, clazz);
		return data;
	}
	
	public static <T> T deserialize(ByteBuffer buffer, T data) 
	{
		return deserialize(buffer, data, data.getClass());
	}
	
	public static <T> T deserialize(ByteBuffer buffer, T data, Class<?> clazz) 
	{
		for (Field field : clazz.getDeclaredFields())
		{
			// make field accessible even if it's private, protected
			if (!field.isAccessible())
				field.setAccessible(true);
		
			int modifiers = field.getModifiers();
			// note: we do no ignore static
			// we ignore final for non-static classes (they carry this$0 field)
			boolean ignore = Modifier.isFinal(modifiers) || Modifier.isTransient(modifiers);
			if (!ignore)
			{
				try
				{
					Class<?> type = field.getType();
					SerializationStrategy ss = SerializationStrategies.strategiesMap.get(type);
					if (ss != null)
					{
						ss.deserialize(buffer, field, data);
					}
					else
					{
						if (type.isPrimitive())
							throw new RuntimeException("unsupported primitive type: " + type);
						else if (type.isArray())
						{
							Object array = field.get(data);
							if (array == null)
								throw new NullPointerException(field.toString());
							
							int len = Array.getLength(array);
							Class<?> elementType = type.getComponentType();
							
							// primitive arrays should be serialized via predefined strategy
							if (elementType.isPrimitive())
								throw new RuntimeException("unsupported primitive array type: " + type);
							// nd-arrays not supported by pvData
							else if (elementType.isArray())
								throw new RuntimeException("nd-arrays not supported: " + type);
								
							//ss = SerializationStrategies.strategiesMap.get(elementType);
								
							SerializeHelper.writeSize(len, buffer);
							for (int i = 0; i < len; i++)
							{
								Object elementData = Array.get(array, i);
								boolean notNull = (elementData != null);
								buffer.put(notNull ? (byte)1 : (byte)0);
								if (notNull)
								{
									//if (ss != null)
									//	ss.serialize(buffer, elementData);
									serialize(buffer, elementData, elementType);
								}
							}
						}
						else if (type.isEnum())
						{
							// TODO read int value() and set appropriate const type.getEnumConstants()
							throw new RuntimeException("enums not supported:" + field);
						}
						else
						{
							Object fieldData = field.get(data);
							if (fieldData == null)
							{
								fieldData = type.newInstance();
								field.set(data, fieldData);
							}
							deserialize(buffer, fieldData, type);
						}
					}
				} catch (Throwable e) {
					throw new RuntimeException("failed to deserialize: " + field, e);
				}
			}
		}
		
		return data;
	}
	
}
