package org.xijiu.share.classloader.spi;

import org.junit.Test;
import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author likangning
 * @since 2020/7/22 下午5:21
 */
public class MysqlSpiTest {

	private static Connection connection;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wolong_extend?rewriteBatchedStatements=true&useServerPrepStmts=true","root","root");
			connection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void executeQuery() throws Exception {
		String sql = "select * from task limit 1";
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Object id = rs.getObject("id");
			System.out.println(id);
		}
		rs.close();
		ps.close();
		connection.close();
	}

	@Test
	public void checkConnectionImplClass() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wolong_extend?rewriteBatchedStatements=true&useServerPrepStmts=true","root","root");
		System.out.println(connection.getClass().getName());
	}

}
