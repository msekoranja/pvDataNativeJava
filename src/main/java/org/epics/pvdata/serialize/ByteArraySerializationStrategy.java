package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class ByteArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		byte[] data = (byte[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		buffer.put(data);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		byte[] data = (byte[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new byte[len];
			reused = false;
		}
		buffer.get(data, 0, len);
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}