@echo compiling StackTraceHelper demo

@call setEnv.cmd
java -cp %~dp0\bin\;"%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.util.test.stacktracehelper.StackTraceHelper

@del www\rocket.util.test.stacktracehelper.StackTraceHelper\tree*.gif
@del www\rocket.util.test.stacktracehelper.StackTraceHelper\history.html
