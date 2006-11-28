@echo compiling Pager demo...
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.pager.Pager


@del www\rocket.widget.test.pager.Pager\tree*.gif