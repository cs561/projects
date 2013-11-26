package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileManager {

	private FileManager() {
	}

	public static long getFileSize(final String filePath) {
		File f = new File(filePath);
		return f.length();
	}

	public static boolean directoryOrFileExist(final String dirPath) {
		File directory = new File(dirPath);
		return directory.exists();
	}

	public static boolean isFile(final String path) {
		File directory = new File(path);
		return directory.isFile();
	}

	public static void createDir(final String dirPath) throws IOException {
		File directory = new File(dirPath);
		if (!directory.mkdir()) {
			throw new IOException("Cannot create directory " + dirPath);
		}
	}

	public static void deleteFile(final String dirPath) throws IOException {
		File f = new File(dirPath);
		FileUtils.forceDelete(f);
	}

	public static void createFile(final String filePath) throws IOException {
		File f = new File(filePath);
		f.createNewFile();
	}

	public static boolean isDirectoryEmpty(final String dirPath) {
		File directory = new File(dirPath);
		if (directory.list() != null && directory.list().length > 0) {
			return false;
		}
		return true;
	}

	public static FileReader readFile(final String path) {
		try {
			return (new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String fileToString(final FileReader fr) {
		BufferedReader br = new BufferedReader(fr);
		String str = "";
		String a;
		try {
			while ((a = br.readLine()) != null) {
				str += a;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	public static void copy(final String srcPath, final String destPath)
			throws IOException {
		File src = new File(srcPath);
		File dest = new File(destPath);

		if (!src.isDirectory()) {
			FileUtils.copyFileToDirectory(src, dest);
		} else {
			FileUtils.copyDirectory(src, dest);
		}
	}

	public static void move(final String srcPath, final String destPath)
			throws IOException {
		File src = new File(srcPath);
		File dest = new File(destPath);

		if (!src.isDirectory()) {
			FileUtils.moveFileToDirectory(src, dest, true);
		} else {
			FileUtils.moveDirectory(src, dest);
		}
	}

	public static void writeFile(final String path, final String data)
			throws IOException {
		FileWriter file = null;
		try {
			file = new FileWriter(path);
			file.write(data);
			file.flush();
		} finally {
			IOUtils.closeQuietly(file);
		}
	}

	public static void writeFile(final File file, final String data)
			throws IOException {
		writeFile(file.getAbsoluteFile().getAbsolutePath(), data);
	}

	public static void appendToFile(final String path, final String data)
			throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path, true));
			bw.write(data);
			bw.flush();
		} finally {
			IOUtils.closeQuietly(bw);
		}
	}

	public static String getDocumentFolderPath() {
		JFileChooser fr = new JFileChooser();
		FileSystemView fw = fr.getFileSystemView();
		return fw.getDefaultDirectory().getAbsolutePath();
	}
}