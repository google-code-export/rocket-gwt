@echo compiling Style demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -style DETAILED -out "%~dp0\www" %* rocket.style.test.Style

@del www\rocket.style.test.Style\tree*.gif
@del www\rocket.style.test.Style\history.html