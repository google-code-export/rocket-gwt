@echo compiling SplitterPanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.splitterpanel.SplitterPanel


@del www\rocket.widget.test.splitterpanel.SplitterPanel\tree*.gif