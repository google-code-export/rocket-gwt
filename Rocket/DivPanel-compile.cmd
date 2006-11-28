@echo compiling DivPanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.divpanel.DivPanel

@del www\rocket.widget.test.divpanel.DivPanel\tree*.gif