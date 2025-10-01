package com.example.custom.instrumentation.processor;

import io.opentelemetry.instrumentation.api.internal.Timer;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

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
        public static Timer onEnter() {
            System.out.println("Entering instrumentation");
            return Timer.start();
        }


        @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
        public static void onExit(
                @Advice.Enter Timer timer,
                @Advice.Return String message,
                @Advice.Thrown Throwable throwable) {

            System.out.println("Leaving instrumentation");
        }
    }
}
