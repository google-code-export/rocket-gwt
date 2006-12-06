@echo compiling UnorderedListPanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.unorderedlistpanel.UnorderedListPanel

@del www\rocket.widget.test.unorderedlistpanel.UnorderedListPanel\tree*.gif
@del www\rocket.widget.test.unorderedlistpanel.UnorderedListPanel\history.html