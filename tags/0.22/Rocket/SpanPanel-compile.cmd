@echo compiling SpanPanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.spanpanel.SpanPanel

@del www\rocket.widget.test.spanpanel.SpanPanel\tree*.gif
@del www\rocket.widget.test.spanpanel.SpanPanel\history.html