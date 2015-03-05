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
		buffer.position(buffer.position() + data.length*Integer.SIZE);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		int[] data = (int[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new int[len];
			reused = false;
		}
		buffer.asIntBuffer().get(data, 0, len);
		buffer.position(buffer.position() + len*Integer.SIZE);
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}