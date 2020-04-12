package com.huitong.monitortracker.executor;

public class ExecutorThreadLocal {
    private static ThreadLocal<Object> inputProcessorThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Object> businessProcessorThreadLocal = new ThreadLocal<>();

    public static void setInputData(Object obj) {
        inputProcessorThreadLocal.set(obj);
    }

    public static void setBusinessData(Object obj) {
        businessProcessorThreadLocal.set(obj);
    }

    public static Object getInputData() {
        return inputProcessorThreadLocal.get();
    }

    public static Object getBusinessData() {
        return businessProcessorThreadLocal.get();
    }
}
