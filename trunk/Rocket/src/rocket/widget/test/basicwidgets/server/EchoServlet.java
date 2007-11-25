package rocket.widget.test.basicwidgets.server;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EchoServlet extends HttpServlet {
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		log("entering doPost");
		log("contentType: " + request.getContentType());
		log("contentLength: " + request.getContentLength());
		log("characterEncoding: " + request.getCharacterEncoding());

		final Reader reader = request.getReader();
		final char[] buffer = new char[4096];
		final StringWriter writer = new StringWriter();
		while (true) {
			final int readCount = reader.read(buffer);
			if (-1 == readCount) {
				break;
			}
			writer.write(buffer, 0, readCount);
		}

		int i = 0;
		final String string = writer.toString();
		final int length = string.length();
		while (i < length) {
			final int lineLength = Math.min(length - i, 80);
			String line = string.substring(i, i + lineLength);
			line = line.replace('\r', '?');
			line = line.replace('\n', '?');
			line = line.replace('\t', '?');
			log(" > " + line);

			i = i + 80;
		}

		response.getWriter().write(string);
		response.flushBuffer();
		log("echoing back \"" + string + "\".");
		log("leaving doPost");
	}
}
