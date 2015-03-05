package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class DoubleArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		double[] data = (double[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		buffer.asDoubleBuffer().put(data);
		buffer.position(buffer.position() + data.length*Double.SIZE);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		double[] data = (double[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new double[len];
			reused = false;
		}
		buffer.asDoubleBuffer().get(data, 0, len);
		buffer.position(buffer.position() + len*Double.SIZE);
		if (!reused)
			reflectField.set(parentInstance, data);
	}
}