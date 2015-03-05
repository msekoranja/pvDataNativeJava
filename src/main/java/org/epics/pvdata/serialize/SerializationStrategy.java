package org.epics.pvdata.serialize;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

// NOTE: avoid using "Object reflectField.get(parentInstance) since it does auto-boxing
public interface SerializationStrategy
{
	void serialize(ByteBuffer buffer, Field reflectField, Object parentInstance) throws IllegalAccessException;
	void deserialize(ByteBuffer buffer, Field reflectField, Object parentInstance) throws IllegalAccessException;
}