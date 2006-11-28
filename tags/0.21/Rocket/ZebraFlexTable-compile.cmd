@echo compiling ZebraFlexTable demo...
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.zebraflextable.ZebraFlexTable

@del www\rocket.widget.test.zebraflextable.ZebraFlexTable\tree*.gif