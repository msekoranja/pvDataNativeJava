package org.epics.pvdata.test;

import java.nio.ByteBuffer;

import org.epics.pvdata.PVData;

public class Example {

	static class TimeStamp
	{
		long secodnds;
		int nanos;
		int userTag;
	}

	static class MyData
	{
		double x, y;
		String name;
		int[] array;
		TimeStamp time;
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
		
	}

}
