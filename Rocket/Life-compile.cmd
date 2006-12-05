@echo compiling Life demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.life.Life

@del www\rocket.widget.test.life.Life\tree*.gif
@del www\rocket.widget.test.life.Life\history.html