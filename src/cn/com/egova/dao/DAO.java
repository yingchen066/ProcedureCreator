package cn.com.egova.dao;

import java.sql.SQLException;

public interface DAO {
	/** 获取初始参数  返回需要插入的functionid,paramid,funcmodelid，procedureid，paramid，modelid的起始值 
	 * @throws SQLException 
	 */
	int[] getInitParams() throws SQLException;
	/** 插入存储过程相关数据表 */
	boolean excuteSQL(String sql);
	/** 将存储过程插入数据库 */
//	boolean createProcedure(String sql);
}
