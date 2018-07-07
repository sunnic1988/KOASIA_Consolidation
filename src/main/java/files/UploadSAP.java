package files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import koasiaco.DBUtil;
import koasiaco.main;

public class UploadSAP {
	static String filesfolder;
	static String errorsfolder;
	static String successfolder;
	static List<File> filelist;
	static int depth = 1;

	public void SAPUploadtoMysql() {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sa = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println("Task Start : " + sf.format(date));

		ArrayList<String> strArray = new ArrayList();

		ClassLoader classLoader = DBUtil.class.getClassLoader();

		InputStream is = classLoader.getResourceAsStream("config/props/db.properties");
		Properties props = new Properties();
		try {
			props.load(is);
			filesfolder = props.getProperty("autouploadfolderfiles");
			errorsfolder = props.getProperty("autouploadfoldererrors");
			successfolder = props.getProperty("autouploadfoldersuccess");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			strArray = GetFileList.find(filesfolder, depth);
			for (int i = 0; i < strArray.size(); i++) {
				main.excelUpload((String) strArray.get(i), "");

				File afile = new File((String) strArray.get(i));

				String newName = afile.getName().substring(0, afile.getName().lastIndexOf(".")) + " " + sa.format(date)
						+ afile.getName().substring(afile.getName().lastIndexOf("."));

				System.out.println("new file name is" + successfolder + newName);

				File target = new File(successfolder + newName);
				if (afile.renameTo(target)) {
					System.out.println("File is moved successful!");
				} else {
					System.out.println("File is failed to move!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Task End : " + sf.format(date));
	}
}
