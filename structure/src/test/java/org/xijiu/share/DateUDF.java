package org.xijiu.share;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUDF {
  private static final String SIMPLE_DATE_PATTERN = "yyyyMMdd";
  private static final String STRING_DATE_PATTERN = "yyyy-MM-dd";
  private static final String HOUR_DATE_PATTERN = "yyyyMMddHH";
  private static final String MINUTE_DATE_PATTERN = "yyyyMMddHHmm";
  private static final String SECOND_DATE_PATTERN = "yyyyMMddHHmmss";


  private static ThreadLocal<DateFormat> simpleDateFormat =
      ThreadLocal.withInitial(() -> new SimpleDateFormat(SIMPLE_DATE_PATTERN));
  private static ThreadLocal<DateFormat> stringDateFormat =
      ThreadLocal.withInitial(() -> new SimpleDateFormat(STRING_DATE_PATTERN));
  private static ThreadLocal<DateFormat> hourDateFormat =
      ThreadLocal.withInitial(() -> new SimpleDateFormat(HOUR_DATE_PATTERN));
  private static ThreadLocal<DateFormat> minuteDateFormat =
      ThreadLocal.withInitial(() -> new SimpleDateFormat(MINUTE_DATE_PATTERN));
  private static ThreadLocal<DateFormat> secondDateFormat =
      ThreadLocal.withInitial(() -> new SimpleDateFormat(SECOND_DATE_PATTERN));

  private static Map<String, ThreadLocal<DateFormat>> dateFormatMap = new HashMap<>();
  static {
    dateFormatMap.put(SIMPLE_DATE_PATTERN, simpleDateFormat);
    dateFormatMap.put(STRING_DATE_PATTERN, stringDateFormat);
    dateFormatMap.put(HOUR_DATE_PATTERN, hourDateFormat);
    dateFormatMap.put(MINUTE_DATE_PATTERN, minuteDateFormat);
    dateFormatMap.put(SECOND_DATE_PATTERN, secondDateFormat);
  }

  private static final String SUMMARY = "summary";
  private static final String DAY = "day";
  private static final String MONTH = "month";
  private static final String WEEK = "week";
  private static final String HOUR = "hour";
  private static final String GAP = "~";





  public static int incDay(int intDate, int day) {
    Date date = intToDate(intDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, day);
    return dateToInt(calendar.getTime());
  }

  public static int decDay(int intDate, int day) {
    return incDay(intDate, -day);
  }





  private static String toStrDate(int dateint) {
    int year = dateint / 10000;
    dateint = dateint % 10000;
    int month = dateint / 100;
    int day = dateint % 100;
    return year + (month < 10 ? "-0" : "-") + month + (day < 10 ? "-0" : "-") + day;
  }

  private static String appendDateRange(Integer beginDate, Integer endDate, Integer totalBeginDate,
      Integer totalEndDate) {
    Integer start = beginDate < totalBeginDate ? totalBeginDate : beginDate;
    Integer end = endDate > totalEndDate ? totalEndDate : endDate;
    return appendDateRange(toStrDate(start), toStrDate(end));
  }

  private static String appendDateRange(String beginDate, String endDate) {
    return beginDate + GAP + endDate;
  }

  /**
   * 通过给定的时间日志，计算该日期当月的开始日期及结束日期
   *
   * @param intDate int类型的时间
   * @return {@link MonthRange}对象
   */
  private static MonthRange calcMonthRange(Integer intDate) {
    MonthRange monthRange = new MonthRange();
    Date date = intToDate(intDate);
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH, 1); // 设置为1号,当前日期既为本月第一天
    monthRange.firstDay = dateToInt(c.getTime());

    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    monthRange.lastDay = dateToInt(c.getTime());
    return monthRange;
  }


  /**
   * 通过给定的时间日志，计算该日期当月的开始日期及结束日期
   *
   * @param intDate int类型的时间
   * @return {@link MonthRange}对象
   */
  private static WeekRange calcWeekRange(Integer intDate) {
    WeekRange weekRange = new WeekRange();
    Date date = intToDate(intDate);
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int dayofweek = c.get(Calendar.DAY_OF_WEEK);
    if (dayofweek == 1) {
      dayofweek += 7;
    }
    c.add(Calendar.DATE, 2 - dayofweek);

    weekRange.firstDay = dateToInt(c.getTime());
    c.add(Calendar.DAY_OF_WEEK, 6);
    weekRange.lastDay = dateToInt(c.getTime());
    return weekRange;
  }


  /**
   * int转换日期
   */
  private static Date intToDate(Integer dateInt) {
    try {
      return simpleDateFormat.get().parse(String.valueOf(dateInt));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    throw new RuntimeException();
  }

  /**
   * 日期转换int
   */
  private static Integer dateToInt(Date date) {
    String format = simpleDateFormat.get().format(date);
    return Integer.parseInt(format);
  }

  /**
   * 定义月对象
   */
  private static class MonthRange {
    private Integer firstDay;
    private Integer lastDay;
  }

  /**
   * 定义月对象
   */
  private static class WeekRange {
    private Integer firstDay;
    private Integer lastDay;
  }
}
