package org.epics.pvdata.test;

import java.nio.ByteBuffer;

import org.epics.pvdata.PVData;

public class Example {

	public static class TimeStamp
	{
		long seconds;
		int nanos;
	}

	public static class MyData
	{
		double x, y;
		String name;
		int[] numbers;
		TimeStamp time;
	}
	
	public static void main(String[] args)
	{
		MyData data = new MyData();
		data.x = 12.3;
		data.y = 4.56;
		data.name = "Matej";
		data.numbers = new int[] { 1, 2, 8 };
		data.time = new TimeStamp();
		data.time.seconds = 1234567890;
		data.time.nanos = 54321;
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		PVData.serialize(buffer, data);
		
		buffer.flip();
		
		@SuppressWarnings("unused")
		MyData data2 = PVData.deserialize(buffer, MyData.class);
	}

}
