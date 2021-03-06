package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializeHelper.ScalarType;

class LongArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		long[] data = (long[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		buffer.asLongBuffer().put(data);
		buffer.position(buffer.position() + data.length*Long.SIZE);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		long[] data = (long[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new long[len];
			reused = false;
		}
		buffer.asLongBuffer().get(data, 0, len);
		buffer.position(buffer.position() + len*Long.SIZE);
		if (!reused)
			reflectField.set(parentInstance, data);
	}

	@Override
	public void serializeIF(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.put((byte)(0x08 | SerializeHelper.scalarTypeCodeLUT[ScalarType.pvLong.ordinal()]));
	}
}