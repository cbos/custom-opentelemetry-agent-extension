package com.example.custom.instrumentation.processor;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.api.incubator.semconv.code.CodeSpanNameExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;

public class ProcessorSingleton {

    private static final String INSTRUMENTATION_NAME = "com.example.custom-1.0";

    private static final Instrumenter<ClassMethodAndKind, Void> INSTRUMENTER;

    static {
        ProcessorCodeAttributesGetter codeAttributesGetter = new ProcessorCodeAttributesGetter();

        INSTRUMENTER =
                Instrumenter.<ClassMethodAndKind, Void>builder(
                                GlobalOpenTelemetry.get(),
                                INSTRUMENTATION_NAME,
                                CodeSpanNameExtractor.create(codeAttributesGetter))
                        .addAttributesExtractor(ProcessorAttributeExtractor.create(codeAttributesGetter))
                        .addOperationMetrics(ProcessorImplementationMetrics.get())
                        .buildInstrumenter(ClassMethodAndKind::getSpanKind);
    }

    public static Instrumenter<ClassMethodAndKind, Void> instrumenter() {
        return INSTRUMENTER;
    }
}
