@echo compiling HyperlinkPanel demo

@call setEnv.cmd
@java -cp "%~dp0\src;%GWTLIBRARIES%" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %*  -style DETAILED rocket.widget.test.hyperlinkpanel.HyperlinkPanel

@del www\rocket.widget.test.hyperlinkpanel.HyperlinkPanel\tree*.gif
@del www\rocket.widget.test.hyperlinkpanel.HyperlinkPanel\history.html