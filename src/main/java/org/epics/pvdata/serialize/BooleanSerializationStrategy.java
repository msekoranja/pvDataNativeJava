package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializeHelper.ScalarType;

class BooleanSerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean value = reflectField.getBoolean(parentInstance);
		buffer.put(value ? (byte)1 : (byte)0);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		boolean value = buffer.get() == 0 ? false : true;
		reflectField.setBoolean(parentInstance, value);
	}

	@Override
	public void serializeIF(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.put(SerializeHelper.scalarTypeCodeLUT[ScalarType.pvBoolean.ordinal()]);
	}
}