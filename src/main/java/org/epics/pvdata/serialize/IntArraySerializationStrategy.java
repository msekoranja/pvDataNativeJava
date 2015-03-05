package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class IntArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		int[] data = (int[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		buffer.asIntBuffer().put(data);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		int[] data = (int[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (len != data.length) {
			data = new int[len];
			reused = false;
		}
		buffer.asIntBuffer().get(data, 0, len);
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}