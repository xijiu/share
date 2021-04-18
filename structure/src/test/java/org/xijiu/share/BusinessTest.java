package org.xijiu.share;

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
import java.util.Collection;
import java.util.Map;
import java.util.Set;
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

  }

}
