package org.epics.pvdata.serialize;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SerializationStrategies
{
	public static SerializationStrategy BOOLEAN = new BooleanSerializationStrategy();
	public static SerializationStrategy BYTE = new ByteSerializationStrategy();
	public static SerializationStrategy SHORT = new ShortSerializationStrategy();
	public static SerializationStrategy INT = new IntSerializationStrategy();
	public static SerializationStrategy LONG = new LongSerializationStrategy();
	public static SerializationStrategy FLOAT = new FloatSerializationStrategy();
	public static SerializationStrategy DOUBLE = new DoubleSerializationStrategy();
	public static SerializationStrategy STRING = new StringSerializationStrategy();

	public static SerializationStrategy BOOLEAN_ARRAY = new BooleanArraySerializationStrategy();
	public static SerializationStrategy BYTE_ARRAY = new ByteArraySerializationStrategy();
	public static SerializationStrategy SHORT_ARRAY = new ShortArraySerializationStrategy();
	public static SerializationStrategy INT_ARRAY = new IntArraySerializationStrategy();
	public static SerializationStrategy LONG_ARRAY = new LongArraySerializationStrategy();
	public static SerializationStrategy FLOAT_ARRAY = new FloatArraySerializationStrategy();
	public static SerializationStrategy DOUBLE_ARRAY = new DoubleArraySerializationStrategy();
	public static SerializationStrategy STRING_ARRAY = new StringArraySerializationStrategy();

	public static SerializationStrategy CHAR = new CharSerializationStrategy();
	public static SerializationStrategy CHAR_ARRAY = new CharArraySerializationStrategy();

	public static SerializationStrategy BOOLEAN_BOXED = new BooleanBoxedSerializationStrategy();
	public static SerializationStrategy BYTE_BOXED = new ByteBoxedSerializationStrategy();
	public static SerializationStrategy SHORT_BOXED = new ShortBoxedSerializationStrategy();
	public static SerializationStrategy INT_BOXED = new IntBoxedSerializationStrategy();
	public static SerializationStrategy LONG_BOXED = new LongBoxedSerializationStrategy();
	public static SerializationStrategy FLOAT_BOXED = new FloatBoxedSerializationStrategy();
	public static SerializationStrategy DOUBLE_BOXED = new DoubleBoxedSerializationStrategy();

	public static SerializationStrategy CHAR_BOXED = new CharBoxedSerializationStrategy();

	public static final Map<Class<?>, SerializationStrategy> strategiesMap;
	
	static {
		Map<Class<?>, SerializationStrategy> map = new HashMap<Class<?>, SerializationStrategy>();
		map.put(boolean.class, BOOLEAN);
		map.put(byte.class, BYTE);
		map.put(short.class, SHORT);
		map.put(int.class, INT);
		map.put(long.class, LONG);
		map.put(float.class, FLOAT);
		map.put(double.class, DOUBLE);
		map.put(String.class, STRING);

		map.put(boolean[].class, BOOLEAN_ARRAY);
		map.put(byte[].class, BYTE_ARRAY);
		map.put(short[].class, SHORT_ARRAY);
		map.put(int[].class, INT_ARRAY);
		map.put(long[].class, LONG_ARRAY);
		map.put(float[].class, FLOAT_ARRAY);
		map.put(double[].class, DOUBLE_ARRAY);
		map.put(String[].class, STRING_ARRAY);

		map.put(char.class, CHAR);
		map.put(char[].class, CHAR_ARRAY);
	
		// boxed
		map.put(Boolean.class, BOOLEAN_BOXED);
		map.put(Byte.class, BYTE_BOXED);
		map.put(Short.class, SHORT_BOXED);
		map.put(Integer.class, INT_BOXED);
		map.put(Long.class, LONG_BOXED);
		map.put(Float.class, FLOAT_BOXED);
		map.put(Double.class, DOUBLE_BOXED);

		map.put(Character.class, CHAR_BOXED);
		
		strategiesMap = Collections.unmodifiableMap(map);
	}
	
}