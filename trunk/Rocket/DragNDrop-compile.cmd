@echo compiling DragNDrop demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.dragndrop.test.DragNDrop

@del www\rocket.dragndrop.test.DragNDrop\tree*.gif
@del www\rocket.dragndrop.test.DragNDrop\history.html