package com.example.custom.instrumentation.processor;

import io.opentelemetry.instrumentation.api.incubator.semconv.code.CodeAttributesGetter;

public class ProcessorCodeAttributesGetter implements CodeAttributesGetter<ClassMethodAndKind> {

    @Override
    public Class<?> getCodeClass(ClassMethodAndKind methodAndType) {
        return methodAndType.getClassAndMethod().declaringClass();
    }

    @Override
    public String getMethodName(ClassMethodAndKind methodAndType) {
        return methodAndType.getClassAndMethod().methodName();
    }
}
