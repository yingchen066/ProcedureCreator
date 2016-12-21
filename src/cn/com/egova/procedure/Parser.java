package cn.com.egova.procedure;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.com.egova.bean.Param;
import cn.com.egova.dao.OracleDAO;
import cn.com.egova.util.Constant;
import cn.com.egova.util.FileUtil;

public class Parser {
	public static String procedureName;
	public static String funcitionName;
	public static String modelname;
	public static boolean isFunction = false;
	public static String deleteSql;
	public static String insertSql;
	public static String createTime;

	public static boolean parseProcedure(String sql, boolean isAddNewModel) throws SQLException {
		createTime="_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		deleteSql="";
		insertSql="";
		
		try {
			String p = "(?<!:)--.*|\\/\\*(\\s|.)*?\\*\\/";
			sql = sql.replaceAll(p, "");
			sql = sql.replace("\t", "");
			String procedureHeader = sql.substring(0, sql.indexOf("("));
			String[] headerArr = procedureHeader.trim().split("\\s+");
			procedureName = headerArr[4];
			if (!procedureName.contains(".")) {
				procedureName = "dlmis." + procedureName;
			}
			
			funcitionName = procedureName.substring(10, 11).toLowerCase() + procedureName.substring(11);
			String paramStr = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
			String params[] = paramStr.split(",");
			ArrayList<Param> list = new ArrayList<Param>();
			if (headerArr[3].equalsIgnoreCase("function")) {
				list.add(new Param("funcResult", 1, Param.TYPE_INTEGER));
				isFunction = true;
			} else {
				isFunction = false;
			}
			FileUtil.save(sql, Constant.DIR+"/updateSQLs"+createTime+"/dlmis/procedure/"+Parser.procedureName.substring(6)+(Parser.isFunction?".fnc":".prc"),false);
			for (String str : params) {
				Param param = initParam(str);
				if (param != null) {
					list.add(param);
				}
			}

			return createInsertOrDeleteSql(list);
		} catch (StringIndexOutOfBoundsException e) {
			return false;
		}
	}

	private static Param initParam(String string) {
		String[] strs = string.split("\n");
		for (String str : strs) {
			str = removeBlankOfStrStart(str);
			// if (!str.startsWith("--")) {
			String[] split = str.split("\\s+");
			if (split.length >= 3) {
				return new Param(split[0], split[1], split[2]);
			}
			// }
		}
		return null;
	}

	private static String removeBlankOfStrStart(String str) {
		if (str.startsWith(" ") || str.startsWith("\r") || str.startsWith("\n")) {
			str = str.substring(1);
			return removeBlankOfStrStart(str);
		}
		return str;
	}

	private static boolean createInsertOrDeleteSql(ArrayList<Param> list) throws SQLException {
		int[] params = new OracleDAO().getInitParams();

		StringBuffer functionInsertSql = new StringBuffer();
		StringBuffer functionDeleteSql = new StringBuffer();
		functionInsertSql
				.append("insert into dlsys.tcmifunction (FUNCTIONID, FUNCTIONNAME, FUNCTIONDESC, PRODUCTID, PROCEDUREID, ENABLEDFLAG, REMARK) values ("
						+ params[0] + ", '" + funcitionName + "', '', 0, " + params[3] + ", 1, '客户端请求');\r\n");
		functionDeleteSql.append("delete  from dlsys.tcmifunction where FUNCTIONID = " + params[0] + ";\r\n");

		StringBuffer funcmodelInsertSql = new StringBuffer();
		StringBuffer funcmodelDeleteSql = new StringBuffer();
		funcmodelInsertSql
				.append("insert into dlsys.tcmifuncmodel (FUNCMODELID, FUNCTIONID, MODELID, EXECUTEORDER, REMARK, PRODUCTID) values ("
						+ params[2] + ", " + params[0] + ", 44, 1, null, null);\r\n");
		funcmodelDeleteSql.append("delete  from dlsys.tcmifuncmodel where FUNCMODELID = " + params[2] + ";\r\n");

		StringBuffer procedureInsertSql = new StringBuffer();
		StringBuffer procedureDeleteSql = new StringBuffer();
		procedureInsertSql
				.append("insert into dlsys.tcmiprocedure (PROCEDUREID, PROCEDURENAME, PROCEDUREDESC, REMARK, PRODUCTID) values ("
						+ params[3] + ", '" + procedureName + "', '', '客户端请求', null);\r\n");

		procedureDeleteSql.append("delete  from dlsys.tcmiprocedure where PROCEDUREID = " + params[3] + ";\r\n");

		StringBuffer functionparamInsertSql = new StringBuffer();
		StringBuffer functionparamDeleteSql = new StringBuffer();

		StringBuffer procedureparamInsertSql = new StringBuffer();
		StringBuffer procedureparamDeleteSql = new StringBuffer();
		int functionParamCount = 0;
		for (int i = 0; i < list.size(); i++) {
			String functionparamType = "";
			int functionparamTypeID = 0;
			String procedureparamType = "";
			int procedureparamTypeID = 0;
			Param param = list.get(i);
			String type = param.getType().toLowerCase();
			if (type.equals(Param.TYPE_VARCHAR) || type.equals(Param.TYPE_VARCHAR2)) {
				functionparamType = "String";
				functionparamTypeID = 1;
				procedureparamType = "Varchar";
				procedureparamTypeID = 12;
			} else if (type.equals(Param.TYPE_INTEGER)) {

				functionparamType = "Int";
				functionparamTypeID = 0;
				procedureparamType = "Integer";
				procedureparamTypeID = 4;
			} else if (type.equals(Param.TYPE_CURSOR)) {

				procedureparamType = "Cursor";
				procedureparamTypeID = -10;
			} else if (type.equals(Param.TYPE_NUMBER)) {

				functionparamType = "Double";
				functionparamTypeID = 4;
				procedureparamType = "Number";
				procedureparamTypeID = 2;
			} else if (type.equals(Param.TYPE_FLOAT)) {
				functionparamType = "Float";
				functionparamTypeID = 2;
				procedureparamType = "Float";
				procedureparamTypeID = 6;
			}
			procedureparamInsertSql
					.append("insert into dlsys.TCMIPROCEDUREPARAM (PARAMID, PROCEDUREID, PARAMNAME, ORDERINPRO, DIRECTION, SQLTYPE, REMARK, PRODUCTID) values ("
							+ (params[4] + i) + ", " + params[3] + ", '" + param.getName() + "', " + (i + 1) + ", "
							+ param.getDirection() + ", " + procedureparamTypeID + ", '" + procedureparamType
							+ "', null);\r\n");
			procedureparamDeleteSql
					.append("delete  from dlsys.TCMIPROCEDUREPARAM where PARAMID = " + (params[4] + i) + ";\r\n");
			if (param.getDirection() == 0) {
				functionparamInsertSql
						.append("insert into dlsys.TCMIFUNCTIONPARAM (PARAMID, FUNCTIONID, PARAMNAME, VALUETYPE, REMARK, PRODUCTID) values ("
								+ (params[1] + functionParamCount) + ", " + params[0] + ", '" + param.getName() + "', "
								+ functionparamTypeID + ", '" + functionparamType + "', null);\r\n");
				functionparamDeleteSql.append("delete  from dlsys.TCMIFUNCTIONPARAM where PARAMID = "
						+ (params[1] + functionParamCount) + ";\r\n");
				functionParamCount++;
			}
		}
		
		insertSql = functionInsertSql.append("\r\n").append(funcmodelInsertSql).append("\r\n").append(procedureInsertSql)
				.append("\r\n").append(functionparamInsertSql).append("\r\n").append(procedureparamInsertSql)
				.append("\r\n").toString();

		deleteSql = functionDeleteSql.append("\r\n").append(funcmodelDeleteSql).append("\r\n")
				.append(procedureDeleteSql).append("\r\n").append(functionparamDeleteSql).append("\r\n")
				.append(procedureparamDeleteSql).append("\r\n").toString();
		FileUtil.save(insertSql, Constant.DIR + "/updateSQLs"+createTime+"/dlsys/data/add"
				+ Parser.funcitionName.substring(0, 1).toUpperCase() + Parser.funcitionName.substring(1) + ".sql",
				false);
		FileUtil.save(deleteSql, Constant.DIR + "/updateSQLs"+createTime+"/dlsys/data/delete"
				+ Parser.funcitionName.substring(0, 1).toUpperCase() + Parser.funcitionName.substring(1) + ".sql",
				false);
		return true;

	}

}
