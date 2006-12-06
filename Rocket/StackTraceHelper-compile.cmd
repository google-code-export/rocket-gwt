@echo compiling StackTraceHelper demo

@rem compile in OBSFUCATED mode.
@call setEnv.cmd
java -cp %~dp0\bin\;"%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %* -style OBF rocket.util.test.stacktracehelper.StackTraceHelper

@del www\rocket.util.test.stacktracehelper.StackTraceHelper\tree*.gif
@del www\rocket.util.test.stacktracehelper.StackTraceHelper\history.html

@mkdir www\rocket.util.test.stacktracehelper.StackTraceHelper\obsfucated
@del /Q www\rocket.util.test.stacktracehelper.StackTraceHelper\obsfucated\*.*
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.xml www\rocket.util.test.stacktracehelper.StackTraceHelper\obsfucated
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.html www\rocket.util.test.stacktracehelper.StackTraceHelper\obsfucated
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.js www\rocket.util.test.stacktracehelper.StackTraceHelper\obsfucated

@rem -----------------------------------------------------------------------


@rem compile in PRETTY mode
@call setEnv.cmd
java -cp %~dp0\bin\;"%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style PRETTY rocket.util.test.stacktracehelper.StackTraceHelper

@del www\rocket.util.test.stacktracehelper.StackTraceHelper\tree*.gif
@del www\rocket.util.test.stacktracehelper.StackTraceHelper\history.html

@mkdir www\rocket.util.test.stacktracehelper.StackTraceHelper\pretty
@del /Q www\rocket.util.test.stacktracehelper.StackTraceHelper\pretty\*.*
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.xml www\rocket.util.test.stacktracehelper.StackTraceHelper\pretty
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.html www\rocket.util.test.stacktracehelper.StackTraceHelper\pretty
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.js www\rocket.util.test.stacktracehelper.StackTraceHelper\pretty

@rem -----------------------------------------------------------------------


@rem compile in DETAILED mode
@call setEnv.cmd
java -cp %~dp0\bin\;"%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %* -style DETAILED rocket.util.test.stacktracehelper.StackTraceHelper

@del www\rocket.util.test.stacktracehelper.StackTraceHelper\tree*.gif
@del www\rocket.util.test.stacktracehelper.StackTraceHelper\history.html

@mkdir www\rocket.util.test.stacktracehelper.StackTraceHelper\detailed
@del /Q www\rocket.util.test.stacktracehelper.StackTraceHelper\detailed\*.*
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.xml www\rocket.util.test.stacktracehelper.StackTraceHelper\detailed
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.html www\rocket.util.test.stacktracehelper.StackTraceHelper\detailed
@move /Y www\rocket.util.test.stacktracehelper.StackTraceHelper\*.js www\rocket.util.test.stacktracehelper.StackTraceHelper\detailed

