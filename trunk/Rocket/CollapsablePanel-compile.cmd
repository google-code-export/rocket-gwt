@echo compiling CollapsablePanel demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.collapsablepanel.CollapsablePanel

@del www\rocket.widget.test.collapsablepanel.CollapsablePanel\tree*.gif
@del www\rocket.widget.test.collapsablepanel.CollapsablePanel\history.html