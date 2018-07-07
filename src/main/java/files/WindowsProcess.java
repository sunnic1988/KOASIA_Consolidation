package files;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.lang.StringUtils;

public class WindowsProcess {
	public static void killTask(String taskPatch) throws IOException {
		String command = "taskkill /f /im " + taskPatch;
		Runtime.getRuntime().exec(command);
	}

	public static void showTaskList(String task) {
		try {
			Process process = Runtime.getRuntime().exec("taskList");
			Scanner in = new Scanner(process.getInputStream());
			int count = 0;
			while (in.hasNextLine()) {
				count++;
				System.out.println(count + ":" + in.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startTask(String task) {
		try {
			Runtime.getRuntime().exec(task);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkTaskList(String task) {
		ArrayList<String> arrayTaskList = new ArrayList();
		try {
			Process process = Runtime.getRuntime().exec("taskList");
			Scanner in = new Scanner(process.getInputStream());
			while (in.hasNextLine()) {
				String str = in.nextLine();
				if (str.indexOf("EXE") > 0) {
					str = str.substring(0, str.indexOf("EXE") + 3);
					arrayTaskList.add(str);
				}
				if (str.indexOf("exe") > 0) {
					str = str.substring(0, str.indexOf("exe") + 3);
					arrayTaskList.add(str);
				}
			}
			return arrayTaskList.contains(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void startProgram(String programPath) throws IOException {
		if (StringUtils.isNotBlank(programPath)) {
			try {
				Desktop.getDesktop().open(new File(programPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
