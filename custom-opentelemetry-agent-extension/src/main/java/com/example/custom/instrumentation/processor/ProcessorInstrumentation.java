package com.example.custom.instrumentation.processor;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.api.annotation.support.async.AsyncOperationEndSupport;
import io.opentelemetry.instrumentation.api.incubator.semconv.util.ClassAndMethod;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.matcher.ElementMatcher;

import static com.example.custom.instrumentation.processor.ProcessorSingleton.instrumenter;
import static io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge.currentContext;
import static io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers.implementsInterface;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class ProcessorInstrumentation implements TypeInstrumentation {

    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return implementsInterface(named("com.example.custom.api.CustomProcessor"));
    }

    @Override
    public void transform(TypeTransformer transformer) {
        SpanKind spanKind = SpanKind.SERVER;
        transformer.applyAdviceToMethod(
                named("transform"),
                mapping ->
                        mapping
                                .bind(
                                        MethodReturnType.class,
                                        (instrumentedType, instrumentedMethod, assigner, argumentHandler, sort) ->
                                                Advice.OffsetMapping.Target.ForStackManipulation.of(
                                                        instrumentedMethod.getReturnType().asErasure()))
                                .bind(
                                        MethodSpanKind.class,
                                        new EnumerationDescription.ForLoadedEnumeration(spanKind)),
                ProcessorInstrumentation.class.getName() + "$TransformAdvice");
    }

    // custom annotation that represents the return type of the method
    @interface MethodReturnType {}

    // custom annotation that represents the SpanKind of the method
    @interface MethodSpanKind {}

    @SuppressWarnings("unused")
    public static class TransformAdvice {

        @Advice.OnMethodEnter()
        public static void onEnter(
                @MethodSpanKind SpanKind spanKind,
                @Advice.Origin("#t") Class<?> declaringClass,
                @Advice.This Object thisObject,
                @Advice.Origin("#m") String methodName,
                @Advice.Local("otelMethod") ClassMethodAndKind classAndMethod,
                @Advice.Local("otelContext") Context context,
                @Advice.Local("otelScope") Scope scope
        ) {
            Context parentContext = currentContext();
            classAndMethod =
                    ClassMethodAndKind.create(ClassAndMethod.create(thisObject.getClass(), methodName), spanKind);

            if (!instrumenter().shouldStart(parentContext, classAndMethod)) {
                return;
            }

            context = instrumenter().start(parentContext, classAndMethod);
            scope = context.makeCurrent();
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
        public static void onExit(
                @MethodReturnType Class<?> methodReturnType,
                @Advice.Local("otelMethod") ClassMethodAndKind classAndMethod,
                @Advice.Local("otelContext") Context context,
                @Advice.Local("otelScope") Scope scope,
                @Advice.Return(typing = Assigner.Typing.DYNAMIC, readOnly = false) Object returnValue,
                @Advice.Thrown Throwable throwable) {

            if (scope == null) {
                return;
            }
            scope.close();

            returnValue =
                    AsyncOperationEndSupport.create(instrumenter(), Void.class, methodReturnType)
                            .asyncEnd(context, classAndMethod, returnValue, throwable);
        }
    }
}
