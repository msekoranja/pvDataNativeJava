package org.epics.pvdata.test;

import java.nio.ByteBuffer;

import org.epics.pvdata.PVData;

import junit.framework.TestCase;

public class PVDataTest extends TestCase {

	public static class AllPrimitives {
		boolean f1;
		byte f2;
		short f3;
		int f4;
		long f5;
		float f6;
		double f7;
		String f8;
		
		char f9;
		int check;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AllPrimitives other = (AllPrimitives) obj;
			if (check != other.check)
				return false;
			if (f1 != other.f1)
				return false;
			if (f2 != other.f2)
				return false;
			if (f3 != other.f3)
				return false;
			if (f4 != other.f4)
				return false;
			if (f5 != other.f5)
				return false;
			if (Float.floatToIntBits(f6) != Float.floatToIntBits(other.f6))
				return false;
			if (Double.doubleToLongBits(f7) != Double
					.doubleToLongBits(other.f7))
				return false;
			if (f8 == null) {
				if (other.f8 != null)
					return false;
			} else if (!f8.equals(other.f8))
				return false;
			if (f9 != other.f9)
				return false;
			return true;
		}
	}
	
	public void testPrimitives()
	{
		AllPrimitives data = new AllPrimitives();
		data.f1 = true;
		data.f2 = 10;
		data.f3 = 1000;
		data.f4 = 1000000000;
		data.f5 = 1000000000000000000L;
		data.f6 = 12.34f;
		data.f7 = 567.89;
		data.f8 = "hello";
		data.f9 = 'A';
		data.check = 0x12345678;
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		PVData.serialize(buffer, data);
		
		buffer.flip();
		
		AllPrimitives data2 = PVData.deserialize(buffer, AllPrimitives.class);
		
		assertEquals(data, data2);
	}
}
