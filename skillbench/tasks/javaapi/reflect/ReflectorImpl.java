package ru.skillbench.tasks.javaapi.reflect;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReflectorImpl implements Reflector {
    private Class<?> clazz;

    @Override
    public void setClass(Class<?> clazz) {
        try{
            if (clazz == null)
                throw new NullPointerException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.clazz = clazz;
    }

    @Override
    public Stream<String> getMethodNames(Class<?>... paramTypes) {
        return Arrays.stream(clazz.getMethods()).filter(method -> Arrays.equals(method.getParameterTypes(), paramTypes)).map(Method::getName);
    }

    @Override
    public Stream<Field> getAllDeclaredFields() {
        ArrayList<Class> allClasses = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null){
            allClasses.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return allClasses.stream().map(Class::getDeclaredFields).flatMap(Arrays::stream).filter(((Predicate<Field>)field -> Modifier.isStatic(field.getModifiers())).negate());
    }

    @Override
    public Object getFieldValue(Object target, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = null;
        if(clazz!=null){
            field = clazz.getDeclaredField(fieldName);
        }else target.getClass().getDeclaredField(fieldName);
        assert field != null;
        try {
            field.setAccessible(true);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return field.get(target);
    }

    @Override
    public Object getMethodResult(Object constructorParam, String methodName, Object... methodParams) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?>[] arrayParams = Arrays.stream(methodParams).map(Object::getClass).toArray(Class[]::new);
        Object instance;
        try{
        if (constructorParam != null)
            instance = clazz.getConstructor(constructorParam.getClass()).newInstance(constructorParam);
        else
            instance = clazz.getConstructor().newInstance();
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, arrayParams);
        } catch (NoSuchMethodException e) {
            method = clazz.getMethod(methodName, arrayParams);
        }
        method.setAccessible(true);
        return method.invoke(instance, methodParams);
        }catch (InvocationTargetException e) {
            if(e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw e;
            }
        }
    }
}
