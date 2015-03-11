/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS pvData is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
package org.epics.pvdata.serialize;

import java.nio.ByteBuffer;

/**
 * Serialization helper.
 */
public final class SerializeHelper {

	public final static void writeSize(final int s, ByteBuffer buffer) {
		if (s == -1)					// null
			buffer.put((byte)-1);
		else if (s < 254)
			buffer.put((byte)s);
		else
			buffer.put((byte)-2).putInt(s);	// (byte)-2 + size
	}

	public final static int readSize(ByteBuffer buffer)
	{
		final byte b = buffer.get();
		if (b == -1)
			return -1;
		else if (b == -2) {
			final int s = buffer.getInt();
			if (s < 0)
				throw new RuntimeException("negative array size");
			return s;
		}
		else
			return (int)(b < 0 ? b + 256 : b);
	}

	public final static void serializeString(final String value, ByteBuffer buffer) {
		if (value == null)
			writeSize(-1, buffer);
		else {
			final int len = value.length(); 
			writeSize(len, buffer);
			buffer.put(value.getBytes());	// UTF-8
		}
	}

	public final static String deserializeString(ByteBuffer buffer) {
		int size = SerializeHelper.readSize(buffer);
		if (size >= 0) {
			byte[] bytes = new byte[size];
			buffer.get(bytes, 0, size);		// UTF-8
			return new String(bytes);
		}
		else
			return null;
	}
	
	/**
	 * Scalar types.
	 */
	
	public enum ScalarType {
	    pvBoolean,
	    pvByte,
	    pvShort,
	    pvInt,
	    pvLong,
	    pvUByte,
	    pvUShort,
	    pvUInt,
	    pvULong,
	    pvFloat,
	    pvDouble,
	    pvString;
	}
	
	/**
	 * Type code for each scalarType.
	 */
	public static final byte[] scalarTypeCodeLUT = {
		0x00, // pvBoolean
		0x20, // pvByte
		0x21, // pvShort
		0x22, // pvInt
		0x23, // pvLong
		0x24, // pvUByte
		0x25, // pvUShort
		0x26, // pvUInt
		0x27, // pvULong
		0x42, // pvFloat
		0x43, // pvDouble
		0x60  // pvString
	};
	
}
