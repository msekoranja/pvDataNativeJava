package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializeHelper.ScalarType;

class BooleanArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		final boolean[] data = (boolean[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		for (int i = 0; i < data.length; i++)
			buffer.put(data[i] ? (byte)1 : (byte)0);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		boolean[] data = (boolean[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new boolean[len];
			reused = false;
		}
		for (int i = 0; i < len; i++)
			data[i] = (buffer.get() == 0) ? false : true;
		if (!reused)
			reflectField.set(parentInstance, data);
	}

	@Override
	public void serializeIF(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.put((byte)(0x08 | SerializeHelper.scalarTypeCodeLUT[ScalarType.pvBoolean.ordinal()]));
	}
}