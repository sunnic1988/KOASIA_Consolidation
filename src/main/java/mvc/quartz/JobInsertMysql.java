package mvc.quartz;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class JobInsertMysql extends QuartzJobBean {
	static String filesfolder;
	static String errorsfolder;
	static String successfolder;
	static List<File> filelist;
	static int depth = 1;

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("Task start : " + sf.format(date));

		DailyRunIRT a = new DailyRunIRT();
		try {
			a.run();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Task end : " + sf.format(date));
	}
}
