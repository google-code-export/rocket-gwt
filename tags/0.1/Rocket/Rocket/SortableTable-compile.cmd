@echo compiling SortableTable demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %* rocket.test.widget.sortabletable.SortableTable

