@echo compiling OrderedListPanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.orderedlistpanel.OrderedListPanel

@del www\rocket.widget.test.orderedlistpanel.OrderedListPanel\tree*.gif
@del www\rocket.widget.test.orderedlistpanel.OrderedListPanel\history.html