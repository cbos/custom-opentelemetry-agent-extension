package com.example.custom.instrumentation.processor;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.api.incubator.semconv.util.ClassAndMethod;

public class ClassMethodAndKind
{
  private final ClassAndMethod classAndMethod;
  private final SpanKind spanKind;

  private ClassMethodAndKind(ClassAndMethod classAndMethod, SpanKind spanKind) {
    this.classAndMethod = classAndMethod;
    this.spanKind = spanKind;
  }

  public static ClassMethodAndKind create(ClassAndMethod classAndMethod, SpanKind spanKind) {
    return new ClassMethodAndKind(classAndMethod, spanKind);
  }

  public ClassAndMethod getClassAndMethod() {
    return classAndMethod;
  }

  public SpanKind getSpanKind() {
    return spanKind;
  }
}
