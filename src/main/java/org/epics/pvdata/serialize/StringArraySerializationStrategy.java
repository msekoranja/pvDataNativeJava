package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializeHelper.ScalarType;

class StringArraySerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		final String[] data = (String[])reflectField.get(parentInstance);
		SerializeHelper.writeSize(data.length, buffer);
		for (int i = 0; i < data.length; i++)
			SerializeHelper.serializeString(data[i], buffer);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean reused = true;
		String[] data = (String[])reflectField.get(parentInstance);
		int len = SerializeHelper.readSize(buffer);
		if (data == null || len != data.length) {
			data = new String[len];
			reused = false;
		}
		for (int i = 0; i < len; i++)
			data[i] = SerializeHelper.deserializeString(buffer);
		if (!reused)
			reflectField.set(parentInstance, data);
	}

	@Override
	public void serializeIF(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.put((byte)(0x08 | SerializeHelper.scalarTypeCodeLUT[ScalarType.pvString.ordinal()]));
	}
}