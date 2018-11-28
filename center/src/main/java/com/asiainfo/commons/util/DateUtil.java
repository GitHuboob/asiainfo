package com.asiainfo.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtil {

	/**
	 * 获取当前系统时间 yyyy-MM-dd kk:mm:ss
	 * @return
	 */
	public static String getSystemTime() {
		return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date(System.currentTimeMillis()));
	}

	public static String getSystemTime(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date(System.currentTimeMillis()));
	}

	public static String getSystemTime(String pattern, int number) {
		return new SimpleDateFormat(pattern).format(new Date(System.currentTimeMillis() + 24*60*60*1000*number));
	}

	/**
	 * date格式-->string格式
	 * @param dateTime
	 * @return
	 */
	public static String parseStringTime(Date dateTime){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(dateTime);
		return date;
	}
	/**
	 * string格式-->date格式
	 * @param dateTime
	 * @return
	 */
	public static Date parseDateTime(String dateTime){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String modifyDay(String date, String type){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        } catch (ParseException e) {
            System.out.println("获取NEXT_STARTTIME失败, 报错信息如下：");
            e.printStackTrace();
        }

		if ("M".equals(type)) {
			calendar.add(Calendar.MONTH, -1);
			return sdf.format(calendar.getTime());
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			return sdf.format(calendar.getTime());
		}
	}
}
