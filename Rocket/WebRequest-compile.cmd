@echo compiling WebRequest demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.remoting.test.webrequest.WebRequest

@del www\rocket.remoting.test.webrequest.WebRequest\tree*.gif