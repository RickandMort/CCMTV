package com.linlic.ccmtv.yx.utils;

import com.linlic.ccmtv.yx.activity.entity.Date_Weekdays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {

	/**
	 * 
	 * 格式化日期
	 * 
	 * 
	 * 
	 * @param sdate
	 * 
	 *            日期字符串
	 * 
	 * @param format
	 * 
	 *            要格式化的日期格式
	 * 
	 * @return 格式化后的日期字符串
	 */

	public static String format(String sdate, String format) {

		Date date = new Date();

		SimpleDateFormat df = new SimpleDateFormat(format);

		try {

			date = df.parse(sdate);

		} catch (ParseException ex) {

			ex.printStackTrace();

		}

		return df.format(date);

	}

	/**
	 * 
	 * 一个日期是否是指定的日期格式
	 * 
	 * 
	 * 
	 * @param dateStr
	 * 
	 *            日期字符串
	 * 
	 * @param pattern
	 * 
	 *            验证的日期格式
	 * 
	 * @return 是否是指定的日期格式
	 */

	public static boolean isValidDate(String dateStr, String pattern) {

		SimpleDateFormat df = new SimpleDateFormat(pattern);

		try {

			df.setLenient(false);

			df.parse(dateStr);

			return true;

		} catch (ParseException e) {

			return false;

		}

	}

	/**
	 * 
	 * 将字符串按指定的格式转换为日期类型
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 *            日期字符串
	 * 
	 * @param format
	 * 
	 *            指定格式
	 * 
	 * @return 格式化后的日期对象
	 */

	public static Date strToDate(String str, String format) {

		SimpleDateFormat dtFormat = null;

		try {

			dtFormat = new SimpleDateFormat(format);

			return dtFormat.parse(str);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * 对一个日期进行偏移
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 *            日期
	 * 
	 * @param offset
	 * 
	 *            偏移两
	 * 
	 * @return 偏移后的日期
	 */

	public static Date addDayByDate(Date date, int offset) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		int day = cal.get(Calendar.DAY_OF_YEAR);

		cal.set(Calendar.DAY_OF_YEAR, day + offset);

		return cal.getTime();

	}

	/**
	 * 
	 * 将日期格式化为<字符串类型>
	 * 
	 * 
	 * 
	 * @param date 要格式化的日期
	 * 
	 * @param dateFormat
	 * 
	 *            日期格式
	 * 
	 * @return 当前日期<字符串类型>
	 */

	public static String dataToStr(Date date, String dateFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		return sdf.format(date);

	}

	/**
	 * 
	 * 得到当前日期<字符串类型>
	 * 
	 * 
	 * 
	 * @param dateFormat
	 * 
	 *            日期格式
	 * 
	 * @return 当前日期<字符串类型>
	 */

	public static String getCurrDate(String dateFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		return sdf.format(new Date());

	}

	/**
	 * 
	 * 得到当前日期<java.util.Date类型>
	 * 
	 * 
	 * 
	 * @param dateFormat
	 * 
	 *            日期格式
	 * 
	 * @return 当前日期<java.util.Date类型>
	 */

	public static Date getCurrentDate(String dateFormat) {

		return strToDate(getCurrDate(dateFormat), dateFormat);

	}

	/**
	 * 
	 * 将一个日期转换为指定格式的日期类型
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 *            要转换的日期
	 * 
	 * @param dateFormat
	 * 
	 *            日期格式
	 * 
	 * @return 转换后的日期对象
	 */

	public static Date formatDate(Date date, String dateFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		return strToDate(sdf.format(date), dateFormat);

	}

	/**
	 * 
	 * 对格式为20080101类型的字符串进行日期格式化
	 * 
	 * 
	 * 
	 * @param dateStr
	 * 
	 *            要格式化的字符串
	 * 
	 * @param formatChar
	 * 
	 *            连接字符
	 * 
	 * @param dateFormat
	 * 
	 *            日期格式
	 * 
	 * @return 格式后的日期字符串
	 */

	public static String format(String dateStr, String formatChar,

	String dateFormat) {

		try {

			dateStr = dateStr.substring(0, 4) + formatChar

			+ dateStr.substring(4, 6) + formatChar

			+ dateStr.substring(6, 8);

			return format(dateStr, dateFormat);

		} catch (Exception e) {

			return null;

		}

	}

	/**
	 * 
	 * 对格式为20080101类型的字符串进行日期格式化
	 * 
	 * 
	 * 
	 * @param dateStr
	 * 
	 *            要格式化的字符串
	 * 
	 * @param formatChar
	 * 
	 *            连接字符
	 * 
	 * @param dateFormat
	 * 
	 *            日期格式
	 * 
	 * @return 格式后的日期对象
	 */

	public static Date formatDate(String dateStr, String formatChar,

	String dateFormat) {

		try {

			dateStr = dateStr.substring(0, 4) + formatChar

			+ dateStr.substring(4, 6) + formatChar

			+ dateStr.substring(6, 8);

			return strToDate(dateStr, dateFormat);

		} catch (Exception e) {

			return null;

		}

	}
	/**
	 *
	 * 对格式为20080101类型的字符串进行日期格式化
	 *
	 *
	 *
	 * @param dateStr
	 *
	 *
	 *            连接字符
	 *
	 * @param dateFormat
	 *
	 *            日期格式
	 *
	 * @return 格式后的日期对象
	 */

	public static Date formatDate2(String dateStr,	String dateFormat) {

		try {

			return strToDate(dateStr, dateFormat);

		} catch (Exception e) {

			return null;

		}

	}

	/**
	 * 
	 * 获得某一个月份的第一天
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */

	@SuppressWarnings("static-access")
	public static Date getFirstDayByMonth(Date date) {

		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();

		gc.setTime(date);

		gc.set(Calendar.DAY_OF_MONTH, 1);

		return formatDate(gc.getTime(), "yyyy-MM-dd");

	}

	/**
	 * 
	 * 获得某一个月份的最后一天
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */

	@SuppressWarnings("static-access")
	public static Date getLastDayByMonth(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.set(Calendar.DATE, 1);// 设为当前月的1号

		cal.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号

		cal.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		return formatDate(cal.getTime(), "yyyy-MM-dd");

	}

	/**
	 * 
	 * 获得指定日期的年份
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static int getYearByDate(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		return cal.get(Calendar.YEAR);

	}

	/**
	 * 
	 * 获得指定日期的月份
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static int getMonthByDate(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		return cal.get(Calendar.MONTH);

	}

	/**
	 * 
	 * 获得指定日期的所在月份当前的天数
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static int getDayInMonthByDate(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		return cal.get(Calendar.DAY_OF_MONTH);

	}

	/**
	 * 
	 * 获得当前传入日期的上一个月份的当前日期
	 * 
	 * 
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static Date getPreviousDate(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.add(Calendar.MONTH, -1);

		return DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");

	}
	
	/**
	 * 
	 * name:日期比较
	 * author:Tom
	 * 2016-4-7下午3:42:36
	 * @param fDate 
	 * @param sDate
	 * @return
	 */
	public static Boolean compareDateDayValue(Date fDate, Date sDate) {
		long l1 = fDate.getTime();
		long l2 = sDate.getTime();
		long diff = l1  - l2 ;
	    long days = diff / (1000 * 60 * 60 * 24);
	    if(days>=0)
	    	if(diff>=0)
	    		return true;
	    	else
	    		return false;
	    else
	    	return false;
	
	}

	public static final int FIRST_DAY = Calendar.MONDAY;

	public static List<Date_Weekdays> printWeekdays() {
		List<Date_Weekdays> dates = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		setToFirstDay(calendar);
		for (int i = 0; i < 7; i++) {
			dates.add(printDay(calendar));
			calendar.add(Calendar.DATE, 1);
		}

		return dates;
	}

	public static void setToFirstDay(Calendar calendar) {
		while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
			calendar.add(Calendar.DATE, -1);
		}
	}

	public static Date_Weekdays printDay(Calendar calendar) {
		Date_Weekdays date_weekdays = new Date_Weekdays();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EE");
		String[] date = dateFormat.format(calendar.getTime()).split(" ");
		date_weekdays.setName(date[1]);
		date_weekdays.setDate(date[0]);
		date_weekdays.setIs(getJudgetoDay(date[0])?1:0);
		System.out.println(dateFormat.format(calendar.getTime()));
		return date_weekdays;
	}

	public static boolean getJudgetoDay(String str) {
		try {
			boolean flag = IsToday(str);
			if (flag == true) {//是今天
				//TODO
				return true;
			} else {//不是今天
				//TODO
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断是否为今天(效率比较高)
	 *
	 * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
	 * @return true今天 false不是
	 * @throws ParseException
	 */
	public static boolean IsToday(String day) throws ParseException {

		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = getDateFormat().parse(day);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 0) {
				return true;
			}
		}
		return false;
	}


	public static SimpleDateFormat getDateFormat() {
		if (null == DateLocal.get()) {
			DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
		}
		return DateLocal.get();
	}
	private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

	/**
	 * 
	 * 获得当前传入日期的下一个月份的当前日期
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static Date getLastMonthDate(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.add(Calendar.MONTH, 1);

		return DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");

	}

	/**
	 * 
	 * 计算两个日期相差的月数（具体细分到天数的差别）
	 * 
	 * @param date1
	 * 
	 * @param date2
	 * 
	 * @return
	 */

	public static int getDiffMonths(Date date1, Date date2) {

		int iMonth = 0;

		int flag = 0;

		try {

			Calendar objCalendarDate1 = Calendar.getInstance();

			objCalendarDate1.setTime(date1);

			Calendar objCalendarDate2 = Calendar.getInstance();

			objCalendarDate2.setTime(date2);

			if (objCalendarDate2.equals(objCalendarDate1))

				return 0;

			if (objCalendarDate1.after(objCalendarDate2)) {

				Calendar temp = objCalendarDate1;

				objCalendarDate1 = objCalendarDate2;

				objCalendarDate2 = temp;

			}

			if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1

			.get(Calendar.DAY_OF_MONTH))

				flag = 1;

			if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1

			.get(Calendar.YEAR))

				iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1

				.get(Calendar.YEAR))

						* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)

						- objCalendarDate1.get(Calendar.MONTH);

			else

				iMonth = objCalendarDate2.get(Calendar.MONTH)

				- objCalendarDate1.get(Calendar.MONTH) - flag;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return iMonth;

	}

	/**
	 * 
	 * 计算两个日期月数的差别，不计算详细到天数的差别
	 * 
	 * 日期大的为参数一，结果为正数，反之为负数
	 * 
	 * @return
	 */

	public static int getDiffMonth(Date date1, Date date2) {

		Calendar calendar1 = Calendar.getInstance();

		calendar1.setTime(date1);

		Calendar calendar2 = Calendar.getInstance();

		calendar2.setTime(date2);

		int diffyaer = calendar1.get(Calendar.YEAR)
				- calendar2.get(Calendar.YEAR);

		int diffmonth = calendar1.get(Calendar.MONTH)
				- calendar2.get(Calendar.MONTH);

		return diffyaer * 12 + diffmonth;

	}

	public static void main(String[] args) {

		System.out.println(DateUtil.compareDateDayValue(
				DateUtil.strToDate("2010-02-29 15:25:42", "yyyy-MM-dd hh:mm:ss"),
				DateUtil.strToDate("2010-02-29 15:25:32", "yyyy-MM-dd hh:mm:ss")));

	}



	public static long getDateDeff(Date startDate, Date endDate) {

		long intValue = 0;

		try {

			String df = new String("yyyy-MM-dd");

			startDate = DateUtil.strToDate(DateUtil.dataToStr(startDate, df),

					df);

			endDate = DateUtil.strToDate(DateUtil.dataToStr(endDate, df), df);

			intValue = (startDate.getTime() - endDate.getTime()) / 86400000;

		} catch (Exception e) {

			System.err.println("getDateDeff error");

		}

		return intValue;

	}

	public static String gethhssmmmssSSS(Date date) {

		String result = DateUtil.dataToStr(date, "yyyy-MM-dd:HH:mm:ss:SSS")
				.replace(":", "");

		return result.substring(10, result.length());

	}
	/**
	 *
	 * 计算两个日期相差多少秒
	 *
	 *
	 * @return
	 */
	public static  long subtract_seconds(Date start,Date end){
		try {



			long interval = (start.getTime() - end.getTime())/1000;
			LogUtil.e("时间差","两个时间相差"+interval+"秒");

			return interval;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 样你就可以对当前时间进行年份的加减，比如求一年后i=1，取1年前i=-1
		如果是月份加减cal.add(2, i);
		如果是星期加减cal.add(3, i);
		如果是每日加减cal.add(5, i);
		如果是小时加减cal.add(10, i);
		如果是分钟加减cal.add(12, i);
		如果是秒的加减cal.add(13, i);
	 * @param ti
	 * @param i
	 * @return
	 * @throws Exception
	 *//*
	public static  String addOrMinusYear(String ti, int i) throws Exception {  
	       Date rtn = null;
		   int mm = 12;
	       GregorianCalendar cal = new GregorianCalendar();  
	       Date date = strToDate(ti,"yyyy-MM-dd HH:mm:ss");
	       cal.setTime(date);  
	       cal.add(mm, i);
	       rtn = cal.getTime();  
	       return dataToStr(rtn, "yyyy-MM-dd HH:mm:ss") ;  
	   }
*/

}