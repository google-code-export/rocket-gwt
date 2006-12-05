@echo compiling InteractiveList demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.interactivelist.InteractiveList

@del www\rocket.widget.test.interactivelist.InteractiveList\tree*.gif
@del www\rocket.widget.test.interactivelist.InteractiveList\history.html