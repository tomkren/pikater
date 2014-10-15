package org.pikater.shared.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;

import org.pikater.shared.logging.database.PikaterDBLogger;

/**
 * Utility class handling some common tasks related to
 * files and paths.
 * 
 * @author SkyCrawl
 */
public class IOUtils {
	//----------------------------------------------------------------------------------------------------------------
	// PATHS RELATED STUFF

	private static String baseAbsAppPath = null;

	/**
	 * This method must be called prior to using any other
	 * path-related methods from this class.
	 * 
	 * @param baseAbsAppPath absolute path to this application (the "src"
	 * folder's parent folder)
	 */
	public static void setAbsoluteBaseAppPath(String baseAbsAppPath) {
		IOUtils.baseAbsAppPath = baseAbsAppPath;
	}

	/**
	 * Gets the path set with {@link #setAbsoluteBaseAppPath(String)}. If null,
	 * throws an exception.
	 */
	public static String getAbsoluteBaseAppPath() {
		if (baseAbsAppPath == null) {
			throw new IllegalStateException("The base application path has not been set.");
		}
		return baseAbsAppPath;
	}

	/**
	 * {@link #setAbsoluteBaseAppPath(String)} needs to be called before using this
	 * method.
	 */
	public static String getAbsoluteSRCPath() {
		return joinPathComponents(getAbsoluteBaseAppPath(), "src");
	}

	/**
	 * {@link #setAbsoluteBaseAppPath(String)} needs to be called before using this
	 * method.
	 */
	public static String getAbsoluteCorePath() {
		return joinPathComponents(getAbsoluteBaseAppPath(), "core");
	}

	/**
	 * {@link #setAbsoluteBaseAppPath(String)} needs to be called before using this
	 * method.
	 */
	public static String getAbsoluteWEBINFPath() {
		return joinPathComponents(getAbsoluteBaseAppPath(), "WEB-INF");
	}

	/**
	 * {@link #setAbsoluteBaseAppPath(String)} needs to be called before using this
	 * method.
	 */
	public static String getAbsoluteWEBINFCLASSESPath() {
		return joinPathComponents(getAbsoluteWEBINFPath(), "classes");
	}

	/**
	 * {@link #setAbsoluteBaseAppPath(String)} needs to be called before using this
	 * method.
	 */
	public static String getAbsoluteWEBINFCONFPath() {
		return joinPathComponents(getAbsoluteWEBINFPath(), "conf");
	}

	/**
	 * Abstract method to join string-defined paths. Automatically handles
	 * directory separation.
	 * 
	 * @param suffixPath may denote a directory or file but must NOT be an absolute path
	 */
	public static String joinPathComponents(String prefixPath, String suffixPath) {
		StringBuilder result = new StringBuilder();
		result.append(prefixPath);
		if (!prefixPath.endsWith(System.getProperty("file.separator"))) {
			result.append(System.getProperty("file.separator"));
		}
		if (suffixPath.startsWith(System.getProperty("file.separator"))) {
			throw new IllegalArgumentException("The suffix path must not be absolute.");
		}
		result.append(suffixPath);
		return result.toString();
	}

	/**
	 * Gets an absolute path to the given class - consists of 2 parts:
	 * <ol>
	 * <li> Absolute path to the application. See {@link #setAbsoluteBaseAppPath(String)},
	 * which must be called prior to this method.
	 * <li> Relative source path to the given class.
	 * </ol>
	 * 
	 */
	public static String getAbsolutePath(Class<?> clazz) {
		return joinPathComponents(getAbsoluteSRCPath(), clazz.getPackage().getName().replace(".", System.getProperty("file.separator"))) + System.getProperty("file.separator");
	}

	//----------------------------------------------------------------------------------------------------------------
	// FILE RELATED STUFF

	/**
	 * Reads the content of the given file to a string.
	 * 
	 */
	public static String readTextFile(String filePath) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			PikaterDBLogger.logThrowable(String.format("Could not read the '%s' file because of the below IO error:", filePath), e);
			return null;
		}
	}

	/**
	 * Writes the given content to the given using the given charset.
	 * 
	 */
	public static void writeToFile(String filePath, String content, Charset charset) {
		try {
			Files.write(Paths.get(filePath), content.getBytes(charset), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			PikaterDBLogger.logThrowable(String.format("Could not write given content to file '%s' because of the below IO error:", filePath), e);
		}
	}

	/**
	 * Creates a temporary file in the system defined temp directory, with no extension.
	 * 
	 */
	public static File createTemporaryFile(String prefix) {
		return createTemporaryFile(prefix, null);
	}

	/**
	 * Creates a temporary file in the system defined temp directory, with
	 * the given extension.
	 * 
	 */
	public static File createTemporaryFile(String prefix, String extension) {
		File file;
		try {
			file = File.createTempFile(prefix + "_tmpfile_" + System.currentTimeMillis(), extension);
			file.deleteOnExit();
			return file;
		} catch (IOException e) {
			throw new IllegalStateException("Could not create a temporary file.", e);
		}
	}

	/**
	 * Creates a temporary file in the given directory, with given extension.
	 * 
	 * @param extension must include a dot, e.g. ".jpg"
	 */
	public static File createTemporaryFile(String directory, String prefix, String extension) {
		File file;
		try {
			file = new File(directory + System.getProperty("file.separator") + prefix + "_tmpfile_" + System.currentTimeMillis() + extension);
			if (file.mkdirs() && file.createNewFile()) {
				file.deleteOnExit();
				return file;
			} else {
				throw new IllegalStateException("Temporary file could not be created.");
			}
		} catch (IOException e) {
			throw new IllegalStateException("Could not create a temporary file.", e);
		}
	}

	/**
	 * Converts the given size in bytes into the appropriate string
	 * notation, e.g. "10 KiB".
	 * 
	 * @see <a href="http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc">StackOverFlow</a>
	 */
	public static String formatFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KiB", "MiB", "GiB", "TiB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
