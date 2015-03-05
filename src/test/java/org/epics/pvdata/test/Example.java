package org.epics.pvdata.test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.epics.pvdata.PVData;

public class Example {

	public static class TimeStamp
	{
		long secodnds;
		int nanos;
		int userTag;

		@Override
		public String toString() {
			return "TimeStamp [secodnds=" + secodnds + ", nanos=" + nanos
					+ ", userTag=" + userTag + "]";
		}
	}

	public static class MyData
	{
		double x, y;
		String name;
		int[] array;
		TimeStamp time;

		@Override
		public String toString() {
			return "MyData [x=" + x + ", y=" + y + ", name=" + name
					+ ", array=" + Arrays.toString(array) + ", time=" + time
					+ "]";
		}
	}
	
	public static void main(String[] args)
	{
		MyData data = new MyData();
		data.x = 12.3;
		data.y = 4.56;
		data.name = "jure";
		data.array = new int[] { 12, 8 };
		data.time = new TimeStamp();
		data.time.secodnds = 1234567890;
		data.time.nanos = 54321;
		data.time.userTag = 1208;
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		PVData.serialize(buffer, data);
		
		buffer.flip();
		
		MyData data2 = PVData.deserialize(buffer, MyData.class);
		System.out.println(data2);

		
	}

}
