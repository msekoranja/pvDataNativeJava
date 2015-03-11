package org.epics.pvdata;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.epics.pvdata.serialize.SerializationStrategies;
import org.epics.pvdata.serialize.SerializationStrategy;
import org.epics.pvdata.serialize.SerializeHelper;

public class PVData {
	/*
	public static void serializeIF(ByteBuffer buffer, Object data, Class<?> clazz) 
	{
		// TODO cache introspection interfaces 
		
		for (Field field : getFields(clazz))
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
					serializeFieldIF(buffer, data, field, type);
				} catch (Throwable e) {
					throw new RuntimeException("failed to serializeIF: " + field, e);
				}
			}
		}
	}

	private static void serializeFieldIF(ByteBuffer buffer, Object data,
			Field field, Class<?> fieldClass) throws IllegalAccessException
	{
		SerializationStrategy ss = SerializationStrategies.strategiesMap.get(fieldClass);
		if (ss != null)
		{
			ss.serializeIF(buffer, field, data);
		}
		else
		{
			if (fieldClass.isPrimitive())
				throw new RuntimeException("unsupported primitive type: " + fieldClass);
			else if (fieldClass.isArray())
			{
				Object array = field.get(data);
				if (array == null)
					throw new NullPointerException(field.toString());
				
				Class<?> elementType = fieldClass.getComponentType();
				
				// primitive arrays should be serialized via predefined strategy
				if (elementType.isPrimitive())
					throw new RuntimeException("unsupported primitive array type: " + fieldClass);
				// nd-arrays not supported by pvData
				else if (elementType.isArray())
					throw new RuntimeException("nd-arrays not supported: " + fieldClass);
					
				//ss = SerializationStrategies.strategiesMap.get(elementType);
					
				// TODO fixed, bounded arrays
				
				// TODO serialize complex arrays
			}
			else if (fieldClass.isEnum())
			{
				// TODO extract int value()
				throw new RuntimeException("enums not supported:" + field);
			}
			else
			{
				Object fieldData = field.get(data);
				Class<?> fieldDataClass = fieldData.getClass();
				
				// tricky way to detect type erasure, i.e. generics
				if (fieldClass == Object.class && fieldDataClass != Object.class)
					serializeFieldIF(buffer, data, field, fieldDataClass);
				else
				{
					// sub-structure
	
					buffer.put((byte)0x80);

					//private static final String EMPTY_ID = "";
					//final String idToSerialize = (id == DEFAULT_ID) ? EMPTY_ID : id;
					SerializeHelper.serializeString(fieldClass.getName(), buffer);
					
					SerializeHelper.writeSize(fields.length, buffer);
					for (int i = 0; i < fields.length; i++)
					{
						SerializeHelper.serializeString(fieldNames[i], buffer);
						control.cachedSerialize(fields[i], buffer);
					}
					
					serializeIF(buffer, fieldData, fieldClass);
					
					
				}
			}
		}
	}
	*/
	
	public static void serialize(ByteBuffer buffer, Object data) 
	{
		serialize(buffer, data, data.getClass());
	}
	
	private static Map<Class<?>, Field[]> cachedFields = new HashMap<Class<?>, Field[]>();
	
	private static Field[] getFields(Class<?> clazz)
	{
		// TODO sync?
		// cached access to fields
		Field[] fields = cachedFields.get(clazz);
		if (fields == null)
		{
			// TODO only declared, what about inherited ?!!!
			fields = clazz.getDeclaredFields();
			cachedFields.put(clazz, fields);
		}
		return fields;
	}
	
	public static void serialize(ByteBuffer buffer, Object data, Class<?> clazz) 
	{
		for (Field field : getFields(clazz))
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
					serializeField(buffer, data, field, type);
				} catch (Throwable e) {
					throw new RuntimeException("failed to serialize: " + field, e);
				}
			}
		}
	}

	private static void serializeField(ByteBuffer buffer, Object data,
			Field field, Class<?> fieldClass) throws IllegalAccessException
	{
		SerializationStrategy ss = SerializationStrategies.strategiesMap.get(fieldClass);
		if (ss != null)
		{
			ss.serialize(buffer, field, data);
		}
		else
		{
			if (fieldClass.isPrimitive())
				throw new RuntimeException("unsupported primitive type: " + fieldClass);
			else if (fieldClass.isArray())
			{
				Object array = field.get(data);
				if (array == null)
					throw new NullPointerException(field.toString());
				
				int len = Array.getLength(array);
				Class<?> elementType = fieldClass.getComponentType();
				
				// primitive arrays should be serialized via predefined strategy
				if (elementType.isPrimitive())
					throw new RuntimeException("unsupported primitive array type: " + fieldClass);
				// nd-arrays not supported by pvData
				else if (elementType.isArray())
					throw new RuntimeException("nd-arrays not supported: " + fieldClass);
					
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
			else if (fieldClass.isEnum())
			{
				// TODO extract int value()
				throw new RuntimeException("enums not supported:" + field);
			}
			else
			{
				Object fieldData = field.get(data);
				Class<?> fieldDataClass = fieldData.getClass();
				
				// tricky way to detect type erasure, i.e. generics
				if (fieldClass == Object.class && fieldDataClass != Object.class)
					serializeField(buffer, data, field, fieldDataClass);
				else
					serialize(buffer, fieldData, fieldClass);
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
		for (Field field : getFields(clazz))
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
							Class<?> elementType = type.getComponentType();
							
							// primitive arrays should be serialized via predefined strategy
							if (elementType.isPrimitive())
								throw new RuntimeException("unsupported primitive array type: " + type);
							// nd-arrays not supported by pvData
							else if (elementType.isArray())
								throw new RuntimeException("nd-arrays not supported: " + type);
								
							//ss = SerializationStrategies.strategiesMap.get(elementType);
								
							int len = SerializeHelper.readSize(buffer);

							Object array = field.get(data);
							if (array == null)
							{
								array = Array.newInstance(elementType, len);
								field.set(data, array);
							}
							
							for (int i = 0; i < len; i++)
							{
								boolean isNull = (buffer.get() == 0);
								if (isNull)
								{
									Array.set(array, i, null);
								}
								else
								{
									Object elementData = Array.get(array, i);
									if (elementData == null)
									{
										elementData = elementType.newInstance();
										Array.set(array, i, elementData);
									}

									//if (ss != null)
									//	ss.deserialize(buffer, elementData);
									deserialize(buffer, elementData, elementType);
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
							// tricky way to detect type erasure, i.e. generics
							else if (type == Object.class && fieldData.getClass() != Object.class)
							{
								type = fieldData.getClass();
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
