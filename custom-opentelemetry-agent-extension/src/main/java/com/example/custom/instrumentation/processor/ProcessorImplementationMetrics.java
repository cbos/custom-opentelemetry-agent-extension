package com.example.custom.instrumentation.processor;

import com.google.auto.value.AutoValue;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;

import io.opentelemetry.instrumentation.api.instrumenter.OperationListener;
import io.opentelemetry.instrumentation.api.instrumenter.OperationMetrics;
import io.opentelemetry.instrumentation.api.internal.OperationMetricsUtil;

import java.util.concurrent.TimeUnit;

public final class ProcessorImplementationMetrics implements OperationListener {

    private static final double NANOS_PER_MILLIS = TimeUnit.MILLISECONDS.toNanos(1);

    private static final ContextKey<State> OPERATION_METRICS_STATE = ContextKey.named("custom-instrumentation-metrics-state");

    public static OperationMetrics get() {
        return OperationMetricsUtil.create("database client", ProcessorImplementationMetrics::new);
    }

    private final DoubleHistogram duration;
    private final LongUpDownCounter invocationCount;

    private ProcessorImplementationMetrics(Meter meter) {
        invocationCount = meter.upDownCounterBuilder("processor.invocation.active").build();
        duration = meter.histogramBuilder("processor.invocation.duration").setUnit("ms").build();
    }

    @Override
    public Context onStart(Context context, Attributes startAttributes, long startNanos) {

        Context newContext = context.with(OPERATION_METRICS_STATE, new AutoValue_ProcessorImplementationMetrics_State(startAttributes, startNanos));
        invocationCount.add(1, startAttributes, newContext);
        return newContext;
    }

    @Override
    public void onEnd(Context context, Attributes endAttributes, long endNanos) {
        State state = context.get(OPERATION_METRICS_STATE);
        if (state == null) {
            return;
        }
        invocationCount.add(-1, state.startAttributes(), context);

        Attributes attributes = state.startAttributes().toBuilder().putAll(endAttributes).build();

        duration.record((endNanos - state.startTimeNanos()) / NANOS_PER_MILLIS, attributes, context);
    }

    @AutoValue
    abstract static class State {

        abstract Attributes startAttributes();

        abstract long startTimeNanos();
    }
}
