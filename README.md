# custom-opentelemetry-agent-extension
Playground for a custom OpenTelemetry Java agent extension

This project has 3 modules:
- custom-api
  This contains just the API with some interfaces
- custom-impl
  This contains the implementation of the API
- custom-opentelemetry-agent-extension
  This contains the custom OpenTelemetry Java agent extension which uses only the API

Custom API and implementation are just plain java modules without OpenTelemetry.

# Local development

## Prepare - download OpenTelemetry Java agent

```shell
mkdir -p .otel
OTEL_VERSION=2.20.1
echo "Download opentelemetry java instrumentation jar version $OTEL_VERSION"
curl -sL -o .otel/opentelemetry-javaagent.jar "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v$OTEL_VERSION/opentelemetry-javaagent.jar"
```

## Build and run

```shell

mvn clean package


java -javaagent:.otel/opentelemetry-javaagent.jar \
     -Dotel.javaagent.extensions=custom-opentelemetry-agent-extension/target/custom-opentelemetry-agent-extension-1.0-SNAPSHOT.jar \
     -jar custom-implementation/target/custom-implementation.jar
      
```