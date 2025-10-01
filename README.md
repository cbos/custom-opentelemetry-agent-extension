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