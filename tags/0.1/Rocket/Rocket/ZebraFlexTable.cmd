@setEnv.cmd
@java -cp "%~dp0\src;%~dp0\bin;%GWTLIBRARIES%" com.google.gwt.dev.GWTShell -out "%~dp0\www" %* rocket.test.widget.zebraflextable.ZebraFlexTable/ZebraFlexTable.html