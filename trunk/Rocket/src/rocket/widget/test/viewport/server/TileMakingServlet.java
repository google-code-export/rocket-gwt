/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.widget.test.viewport.server;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.server.IoHelper;
import rocket.util.server.UncheckedIOException;
import rocket.widget.test.viewport.client.ViewportTestConstants;

/**
 * A simple servlet that returns a tile or rectangular area from a larger image
 * and writes out the tile.
 * 
 * The tile's location and dimensions are controlled by url parameters.
 * 
 * @author Miroslav Pokorny
 */
public class TileMakingServlet extends HttpServlet {

	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		this.log(request, "Incoming request.");

		final long started = System.currentTimeMillis();

		OutputStream output = null;
		try {
			int x = this.getIntegerParameter(request, ViewportTestConstants.X);
			int y = this.getIntegerParameter(request, ViewportTestConstants.Y);
			final int width = this.getIntegerParameter(request, ViewportTestConstants.WIDTH);
			final int height = this.getIntegerParameter(request, ViewportTestConstants.HEIGHT);
			final int zoom = this.getIntegerParameter(request, ViewportTestConstants.ZOOM);

			// create a new image and write a scaled sub image into it.
			final BufferedImage tile = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final int sourceWidth = 100 * width / zoom;
			final int sourceHeight = 100 * height / zoom;

			final Image image = this.getImage();

			final int imageWidth = image.getWidth(null);
			x = (x + imageWidth) % imageWidth;

			final int imageHeight = image.getHeight(null);
			y = (y + imageHeight) % imageHeight;
			System.out.println("SERVER " + x + " imageWidth: " + imageWidth + " y: " + y + " imageheight: " + imageHeight);
			tile.getGraphics().drawImage(image,
			/* destination x, y, x + width, y + height */
			0, 0, width, height,
			/* source x, y, x + width, y + height */
			x, y, x + sourceWidth, y + sourceHeight,
			/* background colour, observer */
			null, null);// );

			response.setContentType("image/jpg");
			output = response.getOutputStream();

			final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ImageIO.write(tile, "jpg", bytes);
			bytes.flush();
			bytes.close();

			output.write(bytes.toByteArray());
			output.flush();

			final long finished = System.currentTimeMillis();
			this.log(request, "Finishing writing " + bytes.size() + " bytes, time taken: " + (finished - started) + " milli(s).");

		} catch (final RuntimeException caught) {
			this.log(request, "failed to write tile, " + caught.getMessage());
			throw caught;
		} catch (final IOException caught) {
			this.log(request, "Failed to write tile, " + caught.getMessage());
			throw caught;
		} catch (final ServletException caught) {
			this.log(request, "Failed to write tile, " + caught.getMessage());
			throw caught;
		} finally {
			IoHelper.closeIfNecessary(output);
		}
	}

	protected int getIntegerParameter(final HttpServletRequest request, final String name) throws ServletException {
		final String string = request.getParameter(name);
		if (StringHelper.isNullOrEmpty(string)) {
			throw new ServletException("Unable to find parameter \"" + name + "\".");
		}
		try {
			return Integer.parseInt(string);
		} catch (final NumberFormatException number) {
			throw new ServletException("The parameter \"" + name + "\" does not contain a number.");
		}
	}

	/**
	 * A cached copy of the Image which will be the source of all tiles.
	 */
	private BufferedImage image;

	protected BufferedImage getImage() {
		if (false == this.hasImage()) {
			this.setImage(this.loadImage());
		}
		ObjectHelper.checkNotNull("field:image", image);
		return image;
	}

	protected boolean hasImage() {
		return null != this.image;
	}

	protected void setImage(final BufferedImage image) {
		ObjectHelper.checkNotNull("parameter:image", image);
		this.image = image;
	}

	protected BufferedImage loadImage() {
		final String filename = this.getFilename();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new RuntimeException("Unable to locate \"" + filename + "\".");
		}
		BufferedImage image = null;
		try {
			image = ImageIO.read(inputStream);

			log("Loaded image " + image);
		} catch (final IOException caught) {
			throw new UncheckedIOException(caught);
		}
		return image;
	}

	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	protected String getFilename() {
		return "wikipedia-worldmap.jpg";
	}

	protected void log(final HttpServletRequest request, final String message) {
		System.out.println("SERVER " + Thread.currentThread().getName() + '\t' + request.getQueryString() + '\t' + message);
	}
}
