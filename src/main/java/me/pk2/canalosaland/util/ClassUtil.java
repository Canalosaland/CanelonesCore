package me.pk2.canalosaland.util;

public class ClassUtil {
    public static boolean classExtends(Class<?> clazz, Class<?> extend) {
        if (clazz == null || extend == null || !extend.isAssignableFrom(clazz))
            return false;

        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null) {
            if (superClass.equals(extend))
                return true;
            superClass = superClass.getSuperclass();
        }

        return false;
    }
}