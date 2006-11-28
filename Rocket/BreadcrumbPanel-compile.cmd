@echo compiling BreadcrumbPanel demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.breadcrumbpanel.BreadcrumbPanel

@del www\rocket.widget.test.breadcrumbpanel.BreadcrumbPanel\tree*.gif