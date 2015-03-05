package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class ShortArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		short[] data = (short[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		buffer.asShortBuffer().put(data);
		buffer.position(buffer.position() + data.length*Short.SIZE);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		short[] data = (short[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new short[len];
			reused = false;
		}
		buffer.asShortBuffer().get(data, 0, len);
		buffer.position(buffer.position() + len*Short.SIZE);
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}