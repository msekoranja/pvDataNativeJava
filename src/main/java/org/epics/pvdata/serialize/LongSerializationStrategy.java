package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializeHelper.ScalarType;

class LongSerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.putLong(reflectField.getLong(parentInstance));
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		reflectField.setLong(parentInstance, buffer.getLong());
	}

	@Override
	public void serializeIF(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.put(SerializeHelper.scalarTypeCodeLUT[ScalarType.pvLong.ordinal()]);
	}
}