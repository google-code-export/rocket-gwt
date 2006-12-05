@echo compiling AutoCompleteTextBox demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.autocompletetextbox.AutoCompleteTextBox

@del www\rocket.widget.test.autocompletetextbox.AutoCompleteTextBox\tree*.gif
@del www\rocket.widget.test.autocompletetextbox.AutoCompleteTextBox\history.html