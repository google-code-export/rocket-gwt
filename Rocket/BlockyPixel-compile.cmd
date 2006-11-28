@echo compiling BlockyPixel demo
@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.blockypixel.BlockyPixel


@del www\rocket.widget.test.blockypixel.BlockyPixel\tree*.gif