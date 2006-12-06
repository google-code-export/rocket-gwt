@echo compiling Grid demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.grid.Grid

@del www\rocket.widget.test.grid.Grid\tree*.gif
@del www\rocket.widget.test.grid.Grid\history.html