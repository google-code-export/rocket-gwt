@echo compiling InteractivePanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.interactivepanel.InteractivePanel

@del www\rocket.widget.test.interactivepanel.InteractivePanel\tree*.gif
@del www\rocket.widget.test.interactivepanel.InteractivePanel\history.html