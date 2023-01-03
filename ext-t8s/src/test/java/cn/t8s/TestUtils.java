package cn.t8s;

import org.junit.rules.TemporaryFolder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class TestUtils {

	/**
	 * Returns the format of an image which is read through the {@link InputStream}.
	 * 
	 * @param is			The {@link InputStream} to an image.
	 * @return				File format of the image.
	 * @throws IOException if a cache file is needed but cannot be created.
	 */
	public static String getFormatName(InputStream is) throws IOException {
		return ImageIO.getImageReaders(
				ImageIO.createImageInputStream(is)
		).next().getFormatName();
	}

	public static InputStream getResourceStream(String resourceName) throws IOException {
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
		if (is == null) {
			throw new IOException("Resource not found: " + resourceName);
		}
		return is;
	}

	public static File copyResourceToFile(String resourceName, File destination) throws IOException {
		InputStream is = getResourceStream(resourceName);
		FileOutputStream fos = new FileOutputStream(destination);

		byte[] buffer;
		int bytesAvailable;
		while ((bytesAvailable = is.available()) != 0) {
			buffer = new byte[bytesAvailable];
			int bytesRead = is.read(buffer, 0, buffer.length);
			fos.write(buffer, 0, bytesRead);
		}
		is.close();
		fos.close();

		return destination;
	}

	public static File copyResourceToTemporaryFile(String resourceName, TemporaryFolder folder) throws IOException {
		String name;
		if (resourceName.contains("/")) {
			name = resourceName.substring(resourceName.lastIndexOf("/") + 1);
		} else {
			name = resourceName;
		}
		File destination = folder.newFile(name);

		return copyResourceToFile(resourceName, destination);
	}

	public static File copyResourceToTemporaryFile(String resourceName, String namedAs, TemporaryFolder folder) throws IOException {
		return copyResourceToFile(resourceName, folder.newFile(namedAs));
	}

	public static BufferedImage getImageFromResource(String resourceName) throws IOException {
		try (InputStream is = getResourceStream(resourceName)) {
			return ImageIO.read(is);
		}
	}
}
