package org.epics.pvdata.test;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
	
	
	public static class AllPrimitiveArrays {
		boolean[] f1;
		byte[] f2;
		short[] f3;
		int[] f4;
		long[] f5;
		float[] f6;
		double[] f7;
		String[] f8;
		
		char[] f9;
		int check;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AllPrimitiveArrays other = (AllPrimitiveArrays) obj;
			if (check != other.check)
				return false;
			if (!Arrays.equals(f1, other.f1))
				return false;
			if (!Arrays.equals(f2, other.f2))
				return false;
			if (!Arrays.equals(f3, other.f3))
				return false;
			if (!Arrays.equals(f4, other.f4))
				return false;
			if (!Arrays.equals(f5, other.f5))
				return false;
			if (!Arrays.equals(f6, other.f6))
				return false;
			if (!Arrays.equals(f7, other.f7))
				return false;
			if (!Arrays.equals(f8, other.f8))
				return false;
			if (!Arrays.equals(f9, other.f9))
				return false;
			return true;
		}

	}
	
	public void testPrimitiveArrays()
	{
		AllPrimitiveArrays data = new AllPrimitiveArrays();
		data.f1 = new boolean[] { true, false, true };
		data.f2 = new byte[] { 10, 0, -10 };
		data.f3 = new short[] { 1000, 0, -1000 };
		data.f4 = new int[] { 1000000000, 0, -1000000000 };
		data.f5 = new long[] { 1000000000000000000L, 0, -1000000000000000000L};
		data.f6 = new float[] { 12.34f, 0f, -12.34f };
		data.f7 = new double[] { 567.89, 0, -567.89 };
		data.f8 = new String[] { "hello", "world", "!" };
		data.f9 = new char[] { 'A', 'b', 'C' };
		data.check = 0x12345678;
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		PVData.serialize(buffer, data);
		
		buffer.flip();
		
		AllPrimitiveArrays data2 = PVData.deserialize(buffer, AllPrimitiveArrays.class);
		
		assertEquals(data, data2);
	}
	
	
	public static class Nested3 {
		String f1;
		int check;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Nested3 other = (Nested3) obj;
			if (check != other.check)
				return false;
			if (f1 == null) {
				if (other.f1 != null)
					return false;
			} else if (!f1.equals(other.f1))
				return false;
			return true;
		}

	}

	public static class Nested2 {
		double f1;
		Nested3 f2; 
		int check;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Nested2 other = (Nested2) obj;
			if (check != other.check)
				return false;
			if (Double.doubleToLongBits(f1) != Double
					.doubleToLongBits(other.f1))
				return false;
			if (f2 == null) {
				if (other.f2 != null)
					return false;
			} else if (!f2.equals(other.f2))
				return false;
			return true;
		}
		
	}

	public static class Nested1 {
		int f1;
		Nested2 f2; 
		int check;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Nested1 other = (Nested1) obj;
			if (check != other.check)
				return false;
			if (f1 != other.f1)
				return false;
			if (f2 == null) {
				if (other.f2 != null)
					return false;
			} else if (!f2.equals(other.f2))
				return false;
			return true;
		}
	}

	public void testNested()
	{
		Nested3 n3 = new Nested3();
		n3.f1 = "nested3";
		n3.check = 0x12345678;
		
		Nested2 n2 = new Nested2();
		n2.f1 = 12.8;
		n2.f2 = n3;
		n2.check = 0x87654321;
		
		Nested1 n1 = new Nested1();
		n1.f1 = 0x11223344;
		n1.f2 = n2;
		n1.check = 0xFFFFFFFF;
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		PVData.serialize(buffer, n1);
		
		buffer.flip();
		
		Nested1 data = PVData.deserialize(buffer, Nested1.class);
		
		assertEquals(n1, data);
		
	}
}
