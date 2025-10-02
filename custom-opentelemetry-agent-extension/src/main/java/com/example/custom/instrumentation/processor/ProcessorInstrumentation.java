package com.example.custom.instrumentation.processor;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.api.incubator.semconv.util.ClassAndMethod;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
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
        transformer.applyAdviceToMethod(
                named("transform")
                        .and(takesArguments(1))
                        .and(returns(named("java.lang.String")))
                        .and(isPublic()),
                ProcessorInstrumentation.class.getName() + "$TransformAdvice");
    }

    @SuppressWarnings("unused")
    public static class TransformAdvice {

        @Advice.OnMethodEnter()
        public static void onEnter(
                @Advice.Origin("#t") Class<?> declaringClass,
                @Advice.Origin("#m") String methodName,
                @Advice.Local("otelMethod") ClassMethodAndKind classAndMethod,
                @Advice.Local("otelContext") Context context,
                @Advice.Local("otelScope") Scope scope
        ) {
            Context parentContext = currentContext();
            classAndMethod =
                    ClassMethodAndKind.create(ClassAndMethod.create(declaringClass, methodName), SpanKind.SERVER);

            if (!instrumenter().shouldStart(parentContext, classAndMethod)) {
                return;
            }

            context = instrumenter().start(parentContext, classAndMethod);
            scope = context.makeCurrent();
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
        public static void onExit(
                @Advice.Local("otelMethod") ClassMethodAndKind classAndMethod,
                @Advice.Local("otelContext") Context context,
                @Advice.Local("otelScope") Scope scope,
                @Advice.Return String message,
                @Advice.Thrown Throwable throwable) {

            if (scope == null) {
                return;
            }
            scope.close();
            instrumenter().end(context, classAndMethod, message, throwable);
        }
    }
}
