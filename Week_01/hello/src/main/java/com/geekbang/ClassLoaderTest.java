package com.geekbang;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderTest extends ClassLoader {


    private String classPath;


    public ClassLoaderTest(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] classData = getClassByteArray();
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            for (int i = 0; i < classData.length; i++) {
                classData[i] = (byte) (255 - classData[i]);
            }
            return defineClass(className, classData, 0, classData.length);
        }

    }

    private byte[] getClassByteArray() {

        File file = new File(classPath);

        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[2048];
                int size;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }

                return out.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];

    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        String classPath = ClassLoaderTest.class.getResource("").getPath() + File.separator + "Hello.xlass";

        ClassLoaderTest classLoaderTest = new ClassLoaderTest(classPath);
        Class<?> aclass = classLoaderTest.findClass("Hello");

        Method method = aclass.getDeclaredMethod("hello");
        Object object = aclass.newInstance();
        method.invoke(object);

    }

}
