package com.yue.messagecounter;

import com.yue.messagecounter.annotaion.Initialization;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Launcher {
    protected Launcher() {
        runAllInitialization();

        Thread thread = new Thread(this::launch);
        thread.setName("Launcher");
        thread.start();
    }

    protected void runAllInitialization() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forJavaClassPath()).setScanners(
                    new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Initialization.class);

        Map<Method, Integer> map = new HashMap<>();

        for (Method method : methods) {
            Initialization initialization = method.getAnnotation(Initialization.class);
            map.put(method, initialization.priority());
        }

        Set<Map.Entry<Method, Integer>> set = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toCollection(LinkedHashSet::new));

        set.forEach((entry) -> {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            Method method = entry.getKey();
            method.setAccessible(true);
            try {
                method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

    }

    protected abstract void launch();
}
