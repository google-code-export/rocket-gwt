@setEnv.cmd
@java -cp "%~dp0\src;%~dp0\bin;%GWTLIBRARIES%" com.google.gwt.dev.GWTShell -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.zebraflextable.ZebraFlexTable/ZebraFlexTable.html