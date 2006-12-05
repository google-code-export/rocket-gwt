@echo compiling Comet demo

@call setEnv.cmd
java -cp %~dp0\bin\;"%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.remoting.test.comet.Comet

@del www\rocket.remoting.test.comet.Comet\tree*.gif
@del www\rocket.remoting.test.comet.Comet\history.html