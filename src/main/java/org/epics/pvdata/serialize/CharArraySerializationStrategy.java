package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class CharArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		final char[] data = (char[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		for (int i = 0; i < data.length; i++)
			buffer.putShort((short)data[i]);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		char[] data = (char[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new char[len];
			reused = false;
		}
		for (int i = 0; i < len; i++)
			data[i] = (char)buffer.getShort();
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}