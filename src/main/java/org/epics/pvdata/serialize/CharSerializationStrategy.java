package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import org.epics.pvdata.serialize.SerializeHelper.ScalarType;

class CharSerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		char c = reflectField.getChar(parentInstance);
		buffer.putShort((short)c);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		char c = (char)buffer.getShort();
		reflectField.setChar(parentInstance, c);
	}

	@Override
	public void serializeIF(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		buffer.put(SerializeHelper.scalarTypeCodeLUT[ScalarType.pvShort.ordinal()]);
	}
}