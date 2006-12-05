@echo compiling SortableTable demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.sortabletable.SortableTable

@del www\rocket.widget.test.sortabletable.SortableTable\tree*.gif
@del www\rocket.widget.test.sortabletable.SortableTable\history.html