package org.xijiu.share;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author likangning
 * @since 2021/2/19 下午3:40
 */
public class BusinessTest {

  @Test
  public void test() {
    String str = "";

    String[] split = str.split("\n");
    System.out.println(split.length);

    StringBuilder sb = new StringBuilder();
    for (String s : split) {
      sb.append(s).append(",");
    }
    System.out.println(sb.toString());
  }


  @Test
  public void test2() throws Exception {
    File file = new File("/Users/likangning/Downloads/超级巡量数据报告demo.csv");
    BufferedReader br = new BufferedReader(new FileReader(file));

    StringBuilder sql = new StringBuilder("insert into daily_campaign_auto_targeting_stat_test values ");

    Set<Integer> set = Sets.newHashSet();


    String line = null;
    while ((line = br.readLine()) != null) {
      if (line.startsWith("ds")) {
        continue;
      }
      sql.append("(");
      String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//      for (String field : fields) {
//        System.out.print(field + " | ");
//      }
//      System.out.println();
      sql.append(fields[1]).append(",");
      sql.append(fields[2]).append(",");
      sql.append(fields[3]).append(",");
      sql.append(fields[4]).append(",");
      sql.append(((Double) (Double.parseDouble(fields[5]) * 10000)).longValue()).append(",");
      sql.append(fields[6]).append(",");
      sql.append(removeUseless(fields[7])).append(",");
      sql.append(money(fields[8])).append(",");
      sql.append(removeUseless(fields[9])).append(",");
      sql.append(removeUseless(fields[10])).append(",");
      sql.append(removeUseless(fields[11])).append(",");
      sql.append(removeUseless(fields[12])).append(",");
      sql.append(money(fields[13])).append(",");
      sql.append(removeUseless(fields[14])).append(",");
      sql.append(removeUseless(fields[15])).append(",");
      sql.append(removeUseless(fields[16])).append(",");
      sql.append(removeUseless(fields[17])).append(",");
      sql.append(fields[0]);
      set.add(Integer.parseInt(fields[0]));
      sql.append("),").append("");
    }
    System.out.println(sql);
    System.out.println(set);
    br.close();
  }

  private String removeUseless(String field) {
    field = field.replaceAll("\"", "");
    field = field.replaceAll(",", "");
    return field;
  }

  private long money(String field) {
    field = removeUseless(field);
    BigDecimal money = new BigDecimal(field);
    money = money.multiply(new BigDecimal(100 * 1000));
    return money.longValue();
  }

  @Test
  public void test3() {
    Double a = 25.3333333D;
    System.out.println(a.longValue());
    System.out.println(removeUseless("\"27,390.78\""));
    System.out.println(money("\"27,390.78\""));
    System.out.println(Math.random() * 100);
  }

  private static AtomicInteger number = new AtomicInteger();


  private LoadingCache<Long, Collection<Integer>> cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(10, TimeUnit.MINUTES)
      .build(
          new CacheLoader<Long, Collection<Integer>>() {
            @Override
            public Collection<Integer> load(Long userId) {
              int index = number.incrementAndGet();
              if (index == 1) {
                if (userId.equals(1L)) {
                  return Lists.newArrayList(1);
                }
              } else {
                return Lists.newArrayList(2);
              }
              return null;
            }
          }
      );


  @Test
  public void test4() {
    System.out.println(Integer.MAX_VALUE);

  }



  public static List<Map<String, Object>> fillFirstListForDiff(List<Map<String, Object>> first,
                                                               List<Map<String, Object>> second, String dtField, String hrField,
                                                               int dayGap, Map<String, Object> viewKeyValues) {
    if (dtField == null || dtField.isEmpty()) {
      throw new IllegalArgumentException("dtField参数为空！");
    }
    if (viewKeyValues == null || viewKeyValues.isEmpty()) {
      throw new IllegalArgumentException("viewKeyValues参数为空！");
    }

    for (Map<String, Object> map : second) {
      String dt = String.valueOf(map.get(dtField));
      if (dt != null) {
        int dateValue = DateUDF.incDay(Integer.parseInt(dt), dayGap);
        Object hrValue = (hrField == null || "".equals(hrField)) ? null : map.get(hrField);
        if (firstListNotContains(first, dtField, dateValue, hrField, hrValue)) {
          Map<String, Object> newRecord = new HashMap<>(map);
          newRecord.putAll(viewKeyValues);
          newRecord.put(dtField, dateValue);
          first.add(newRecord);
        }
      }
    }
    return first;
  }

  private static boolean firstListNotContains(List<Map<String, Object>> first, String dtField, int dateValue,
                                              String hrField, Object hrValue) {
    for (Map<String, Object> map : first) {
      String dtStr = String.valueOf(map.get(dtField));
      if (String.valueOf(dateValue).equalsIgnoreCase(dtStr)) {
        if (hrField != null && !"".equals(hrField) && hrValue != null) {
          Object firstHr = map.get(hrField);
          if (hrValue == firstHr || hrValue.equals(firstHr)) {
            return false;
          }
        } else {
          return false;
        }
      }
    }
    return true;
  }


  public static void main(String[] args) {
    List<Map<String, Object>> first = new ArrayList<>();
    Map<String, Object> map2 = new HashMap<>();
    map2.put("user_id", 1061);
    map2.put("dt", 20210410);
    map2.put("hr", "04");
    map2.put("show", 1000);
    map2.put("click", 100);
    map2.put("consume", 23320);
    first.add(map2);

    Map<String, Object> map3 = new HashMap<>();
    map3.put("user_id", 1061);
    map3.put("dt", 20210410);
    map3.put("hr", "05");
    map3.put("show", 1000);
    map3.put("click", 100);
    map3.put("consume", 23320);
    first.add(map3);

    // ******************************************************************************

    List<Map<String, Object>> second = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    map.put("user_id", 1061);
    map.put("dt", 20210410);
    map.put("hr", "05");
    map.put("show", 100);
    map.put("click", 10);
    map.put("consume", 2332);
    second.add(map);

    Map<String, Object> map4 = new HashMap<>();
    map4.put("user_id", 1061);
    map4.put("dt", 20210411);
    map4.put("hr", "05");
    map4.put("show", 100);
    map4.put("click", 10);
    map4.put("consume", 2332);
    second.add(map4);

    Map<String, Object> viewKeyValues = new HashMap<>();
    viewKeyValues.put("show", 0);
    viewKeyValues.put("click", 0);
    viewKeyValues.put("consume", 0);
    first = fillFirstListForDiff(first, second, "dt", "hr",6, viewKeyValues);
    System.out.println(first);

  }
}
