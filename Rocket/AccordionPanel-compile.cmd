@echo compiling AccordionPanel demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.accordionpanel.AccordionPanel

@del www\rocket.widget.test.accordionpanel.AccordionPanel\tree*.gif

