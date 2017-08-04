package cn.com.egova.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.egova.util.Constant;

public class OracleDAO implements DAO {
	String driverName = "oracle.jdbc.driver.OracleDriver";
	String url;
	String user;
	String password;

	public OracleDAO() {
		this.user = Constant.DB_USERNAME;
		this.password = Constant.DB_PASSWORD;	
		if(Constant.JDBC_TYPE.equals(Constant.TYPE_SID)){
			this.url = "jdbc:oracle:thin:@" + Constant.DB_URL + ":" + Constant.DB_PORT + ":" + Constant.DB_NAME;
		}else{
			this.url = "jdbc:oracle:thin:@//" + Constant.DB_URL + ":" + Constant.DB_PORT + "/" + Constant.DB_NAME;
		}
	}

	public int[] getInitParams() throws SQLException {
		String sql1 = "select functionid from (select functionid from dlsys.tcmifunction order by functionid desc) where rownum=1";
		String sql2 = "select paramid from (select paramid from dlsys.tcmifunctionparam order by paramid desc) where rownum=1";
		String sql3 = "select funcmodelid from (select funcmodelid from dlsys.tcmifuncmodel order by funcmodelid desc) where rownum=1";
		String sql4 = "select procedureid from (select procedureid from dlsys.tcmiprocedure order by procedureid desc) where rownum=1";
		String sql5 = "select paramid from (select paramid from dlsys.tcmiprocedureparam order by paramid desc) where rownum=1";
		String sql6 = "select modelid from (select modelid from dlsys.tcmimodel order by modelid desc) where rownum=1";
		return new int[] { getNowMaxID(sql1, "functionid") + 1, getNowMaxID(sql2, "paramid") + 1,
				getNowMaxID(sql3, "funcmodelid") + 1, getNowMaxID(sql4, "procedureid") + 1,
				getNowMaxID(sql5, "paramid") + 1, getNowMaxID(sql6, "modelid") + 1 };
	}
	
	public boolean testConnection() throws SQLException{
		Connection conn = null;
		try {
			Class.forName(driverName);

			conn = DriverManager.getConnection(url, user, password);

			System.out.println(conn);

			return true;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private int getNowMaxID(String sql, String columName) throws SQLException {
		int maxID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Connection conn = null;

		try {
			Class.forName(driverName);

			conn = DriverManager.getConnection(url, user, password);

			System.out.println(conn);

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				maxID = Integer.valueOf(rs.getString(columName));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maxID;
	}

	@Override
	public boolean excuteSQL(String sql) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Connection conn = null;

		try {
			Class.forName(driverName);

			conn = DriverManager.getConnection(url, user, password);

			System.out.println(conn);

			pstmt = conn.prepareStatement(sql);
			
			pstmt.execute(sql);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
		
	}

}
