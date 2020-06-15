package com.amdocs.oss.armadilo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class TestDataSource {

	public static void main(String[] args) throws MyException {
		Hashtable<String,String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, "t3://127.0.0.1:8081");
		env.put(Context.SECURITY_PRINCIPAL, "username");
		env.put(Context.SECURITY_CREDENTIALS, "password");
		Context ctx = null;
		Connection conn = null;
		DataSource ds = null;
		try {

			ctx = new InitialContext(env);
			ds = (DataSource) ctx.lookup("dataSource-OracleDS_native");
			System.out.println("DBUtils: Fetching datasource ->");
			if (ds != null) {
				conn = ds.getConnection();

				System.out.println("DBUtils: Got the DB connection ->");
			} else {
				System.out.println("DBUtils: Error getting DB connection ->");
			}
			String value = getValue(conn);
			System.out.println("value = " + value);
			// use the EmployeeBean
		}catch(SQLException se) {
			System.out.println("Inside Exception");
			if(se.getMessage() != null && se.getMessage().contains("ORA-06502: PL/SQL: numeric or value error: character string buffer too small")) {
				String value = getValue(conn);
				System.out.println("value = " + value);
			}
		} catch (NamingException | MyException  e) {
			// a failure occurred
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (ctx != null) {
					ctx.close();
				}
			} catch (Exception e) {
				// a failure occurred
			}
		}
	}

	private static String getValue(Connection conn) throws PldtArmException {
		CommonUtils util = new CommonUtils(conn);
		String value = util.getTextValueDP(1700000004L);
		return value;
	}
}
