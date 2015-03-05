package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

class StringSerializationStrategy implements SerializationStrategy
{
	@Override
	public void serialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		String string = (String)reflectField.get(parentInstance);
		SerializeHelper.serializeString(string, buffer);
	}

	@Override
	public void deserialize(ByteBuffer buffer, Field reflectField,
			Object parentInstance) throws IllegalAccessException {
		String string = SerializeHelper.deserializeString(buffer);
		reflectField.set(parentInstance, string);
	}
}