package fr.maxlego08.essentials.api.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class DiscUtils {

	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	// -------------------------------------------- //
	// BYTE
	// -------------------------------------------- //

	public static byte[] readBytes(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}

	public static void writeBytes(File file, byte[] bytes) throws IOException {
		Files.write(file.toPath(), bytes);
	}

	// -------------------------------------------- //
	// STRING
	// -------------------------------------------- //

	public static void write(File file, String content) throws IOException {
		writeBytes(file, utf8(content));
	}

	public static String read(File file) throws IOException {
		return utf8(readBytes(file));
	}

	// -------------------------------------------- //
	// CATCH
	// -------------------------------------------- //

	public static boolean writeCatch(File file, String content) {
		try {
			write(file, content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String readCatch(File file) {
		try {
			return read(file);
		} catch (IOException e) {
			return null;
		}
	}

	// -------------------------------------------- //
	// DOWNLOAD
	// -------------------------------------------- //

	public static boolean downloadUrl(String urlstring, File file) {
		try {
			URLConnection connection = URI.create(urlstring).toURL().openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			try (InputStream in = connection.getInputStream(); FileOutputStream out = new FileOutputStream(file)) {
				in.transferTo(out);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean downloadUrl(String urlstring, String filename) {
		return downloadUrl(urlstring, new File(filename));
	}

	// -------------------------------------------- //
	// FILE DELETION
	// -------------------------------------------- //

	public static boolean deleteRecursive(File path) throws FileNotFoundException {
		if (!path.exists())
			throw new FileNotFoundException(path.getAbsolutePath());
		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	// -------------------------------------------- //
	// UTF8 ENCODE AND DECODE
	// -------------------------------------------- //

	public static byte[] utf8(String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

	public static String utf8(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

}
