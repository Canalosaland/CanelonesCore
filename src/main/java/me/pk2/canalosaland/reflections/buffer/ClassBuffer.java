package me.pk2.canalosaland.reflections.buffer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ClassBuffer {
    private HashMap<String, Method> methodBuffer;
    private HashMap<String, Method> dmethodBuffer;
    private HashMap<String, Field> fieldBuffer;
    private HashMap<String, Field> dfieldBuffer;
    private HashMap<String, Constructor<?>> constructorBuffer;
    private HashMap<String, Constructor<?>> dconstructorBuffer;
    private Class<?> clazz;
    private Object instance;

    public ClassBuffer(Class<?> clazz) {
        methodBuffer = new HashMap<>();
        dmethodBuffer = new HashMap<>();
        fieldBuffer = new HashMap<>();
        dfieldBuffer = new HashMap<>();
        constructorBuffer = new HashMap<>();
        dconstructorBuffer = new HashMap<>();

        this.clazz = clazz;
    }
    public ClassBuffer(String className) {
        methodBuffer = new HashMap<>();
        dmethodBuffer = new HashMap<>();
        fieldBuffer = new HashMap<>();
        dfieldBuffer = new HashMap<>();
        constructorBuffer = new HashMap<>();
        dconstructorBuffer = new HashMap<>();

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            clazz = null;
            ex.printStackTrace();
        }
    }

    public Class<?> getClazz() { return clazz; }

    public void setInstance(Object instance) { this.instance = instance; }
    public Object getInstance() { return instance; }

    public Method getMethod(String method, boolean isStatic, Class<?>... args) {
        StringBuilder builder = new StringBuilder();
        builder.append(method);
        builder.append(isStatic);
        for(Class<?> arg : args)
            builder.append(arg.getName());
        String key = builder.toString();

        if(methodBuffer.containsKey(key))
            return methodBuffer.get(key);

        try {
            Method m = clazz.getMethod(method, args);
            methodBuffer.put(key, m);

            return m;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public Method getDeclaredMethod(String method, boolean isStatic, Class<?>... args) {
        StringBuilder builder = new StringBuilder();
        builder.append(method);
        builder.append(isStatic);
        for(Class<?> arg : args)
            builder.append(arg.getName());
        String key = builder.toString();

        if(dmethodBuffer.containsKey(key))
            return dmethodBuffer.get(key);

        try {
            Method m = clazz.getDeclaredMethod(method, args);
            dmethodBuffer.put(key, m);

            return m;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Field getField(String field, boolean isStatic) {
        String key = field + isStatic;
        if(fieldBuffer.containsKey(key))
            return fieldBuffer.get(key);

        try {
            Field f = clazz.getField(field);
            fieldBuffer.put(key, f);

            return f;
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public Field getDeclaredField(String field, boolean isStatic) {
        String key = field + isStatic;
        if(dfieldBuffer.containsKey(key))
            return dfieldBuffer.get(key);

        try {
            Field f = clazz.getDeclaredField(field);
            dfieldBuffer.put(key, f);

            return f;
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Constructor<?> getConstructor(Class<?>... args) {
        StringBuffer buffer = new StringBuffer();
        for(Class<?> arg : args)
            buffer.append(arg.getName());
        String key = buffer.toString();

        if(constructorBuffer.containsKey(key))
            return constructorBuffer.get(key);

        try {
            Constructor<?> c = clazz.getConstructor(args);
            constructorBuffer.put(key, c);

            return c;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public Constructor<?> getDeclaredConstructor(Class<?>... args) {
        StringBuffer buffer = new StringBuffer();
        for(Class<?> arg : args)
            buffer.append(arg.getName());
        String key = buffer.toString();

        if(dconstructorBuffer.containsKey(key))
            return dconstructorBuffer.get(key);

        try {
            Constructor<?> c = clazz.getDeclaredConstructor(args);
            dconstructorBuffer.put(key, c);

            return c;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}