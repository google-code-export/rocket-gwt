@echo compiling Style demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -style DETAILED -out "%~dp0\www" %* rocket.test.style.Style

