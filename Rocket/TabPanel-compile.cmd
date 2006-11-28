@echo compiling TabPanel demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.tabpanel.TabPanel


@del www\rocket.widget.test.tabpanel.TabPanel\tree*.gif