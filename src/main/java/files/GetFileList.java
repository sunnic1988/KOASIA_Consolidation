package files;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class GetFileList {
	private static int depth = 1;

	public static ArrayList<String> find(String pathName, int depth) throws IOException {
		ArrayList<String> strArray = new ArrayList();
		int filecount = 0;

		File dirFile = new File(pathName);
		if (!dirFile.exists()) {
			System.out.println("do not exit");
			return strArray;
		}
		if (!dirFile.isDirectory()) {
			if (dirFile.isFile()) {
				System.out.println(dirFile.getCanonicalFile());
			}
			return strArray;
		}
		for (int j = 0; j < depth; j++) {
			System.out.print("  ");
		}
		System.out.print("|--");
		System.out.println(dirFile.getName());

		String[] fileList = dirFile.list();
		int currentDepth = depth + 1;
		for (int i = 0; i < fileList.length; i++) {
			String string = fileList[i];

			File file = new File(dirFile.getPath(), string);

			String name = file.getAbsolutePath();
			if (file.isDirectory()) {
				find(file.getCanonicalPath(), currentDepth);
			} else {
				for (int j = 0; j < currentDepth; j++) {
					System.out.print("   ");
				}
				System.out.print("|--");
				System.out.println(name);

				strArray.add(name);
			}
		}
		return strArray;
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
   
