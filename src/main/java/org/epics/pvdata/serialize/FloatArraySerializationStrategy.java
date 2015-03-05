package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class FloatArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		float[] data = (float[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		buffer.asFloatBuffer().put(data);
		buffer.position(buffer.position() + data.length*Float.SIZE);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		float[] data = (float[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new float[len];
			reused = false;
		}
		buffer.asFloatBuffer().get(data, 0, len);
		buffer.position(buffer.position() + len*Float.SIZE);
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}