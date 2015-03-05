# pvDataNativeJava

This project implements pvData serialization using POJOs, instead of pvData PV<type> container classes.
pvData introspection interface classes and mechanism where replaced by using Java Reflection API.

Usage:

- first define a simple Java POJO class:
```java
public static class MyData
{
	double x, y;
	String name;
	int[] numbers;
}
```

- create a new instance and initialze data
```java
	MyData data = new MyData();
	data.x = 12.3;
	data.y = 4.56;
	data.name = "Matej";
	data.numbers = new int[] { 1, 2, 8 };
```

- serialize into a buffer
```java
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	PVData.serialize(buffer, data);
```

- deserialize (from the same buffer in this example)
```java
	buffer.flip();
	MyData data2 = PVData.deserialize(buffer, MyData.class);
```

See full example [code](../master/src/test/java/org/epics/pvdata/test/Example.java).
