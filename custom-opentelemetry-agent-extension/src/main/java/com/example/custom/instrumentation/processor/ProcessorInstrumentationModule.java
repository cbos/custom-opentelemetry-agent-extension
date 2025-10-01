package com.example.custom.instrumentation.processor;


import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

import java.util.List;

import static java.util.Collections.singletonList;

@AutoService(InstrumentationModule.class)
public final class ProcessorInstrumentationModule extends InstrumentationModule {
    public ProcessorInstrumentationModule() {
        super("custom-instrumentation", "customer-instrumentation-processor");
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return singletonList(new ProcessorInstrumentation());
    }
}

