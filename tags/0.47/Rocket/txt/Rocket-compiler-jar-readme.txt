This archive contains several classes that modify the gwt compiler adding some extras to help reduce code size as well as another to spit warnings
for all long type references within a program.

To enable warnings to be emitted set the following system property "rocket.compiler.LongNotifier.enabled=true" and set logLevel to WARN or ERROR.
The LongNotifier will then spit out in a tree like heirarchy all types, methods, fields, parameters, local variables and literals that are actually longs.
This can be particularly useful when one wishes to identify all potential spots within a program that might be susceptible to precision problems for
overly large long values due to the lack of a 64 bit long type in javascript.