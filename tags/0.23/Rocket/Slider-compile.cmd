@echo compiling Slider demo

@call setEnv.cmd
@java -cp "%~dp0\src;%~dp0\bin;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.slider.Slider

@del www\rocket.widget.test.slider.Slider\tree*.gif
@del www\rocket.widget.test.slider.Slider\history.html