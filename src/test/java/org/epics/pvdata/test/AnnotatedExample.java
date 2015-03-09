package org.epics.pvdata.test;

import java.nio.ByteBuffer;

/*
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
*/
import org.epics.pvdata.PVData;
import org.epics.pvdata.annotation.Union;

public class AnnotatedExample {

	public static class MyData
	{
		Object unrestrictedUnion;

		@Union(restrictedTypes = { int.class, double.class, String.class })
		Object restricted;

		@Override
		public String toString() {
			return "MyData [unrestrictedUnion=" + unrestrictedUnion
					+ ", restricted=" + restricted + "]";
		}
		
		
	}

	static class MCL extends ClassLoader
	{
		public Class<?> define(byte[] code) {
			return defineClass(null, code, 0, code.length);
		}
	}
	/*
	public static class pojoDump implements Opcodes {

		public static byte[] dump() throws Exception {

			ClassWriter cw = new ClassWriter(0);
			FieldVisitor fv;
			MethodVisitor mv;
			//AnnotationVisitor av0;

			cw.visit(52, ACC_PUBLIC + ACC_SUPER, "pojo", null,
					"java/lang/Object", null);

			{
				fv = cw.visitField(0, "f1", "I", null, null);
				fv.visitEnd();
			}
			{
				fv = cw.visitField(0, "f2", "D", null, null);
				fv.visitEnd();
			}
			{
				fv = cw.visitField(0, "f3", "Ljava/lang/String;", null, null);
				fv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
						"()V", false);
				mv.visitInsn(RETURN);
				mv.visitMaxs(1, 1);
				mv.visitEnd();
			}
			cw.visitEnd();

			return cw.toByteArray();
		}
		
		public static synchronized Class<?> loadClass(byte[] bytecode)
				throws Exception {
			return (new MCL().define(bytecode));
		}
		
		public static Class<?> load() throws Exception
		{
			return loadClass(dump());
		}
	
	}
*/
	
	
	public static void main(String[] args) throws Throwable
	{
		MyData data = new MyData();
		data.unrestrictedUnion = new Double(12.3);
		data.restricted = "hello";
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		PVData.serialize(buffer, data);
		
		buffer.flip();
		
		MyData data2 = PVData.deserialize(buffer, MyData.class);
		
		System.out.println(data);
		System.out.println(data2);
	}

}
