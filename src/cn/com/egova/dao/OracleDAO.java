package cn.com.egova.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.com.egova.util.Constant;

public class OracleDAO implements DAO{
	String driverName="oracle.jdbc.driver.OracleDriver";  
	String url;
    String user;   
    String password;
    public OracleDAO(){
    	this.url="jdbc:oracle:thin:@"+Constant.DB_URL+":"+Constant.DB_PORT+":"+Constant.DB_NAME;
    	this.user=Constant.DB_USERNAME;
    	this.password=Constant.DB_PASSWORD;
    }
    
	public int[] getInitParams() throws  SQLException{
    	String sql1="select functionid from dlsys.tcmifunction where rownum=1 order by functionid desc";
    	String sql2="select paramid from dlsys.tcmifunctionparam where rownum=1 order by paramid desc";
    	String sql3="select funcmodelid from dlsys.tcmifuncmodel where rownum=1 order by funcmodelid desc";
    	String sql4="select procedureid from dlsys.tcmiprocedure where rownum=1 order by procedureid desc";
    	String sql5="select paramid from dlsys.tcmiprocedureparam where rownum=1 order by paramid desc";
    	String sql6="select modelid from dlsys.tcmimodel where rownum=1 order by modelid desc";
        return new int[]{getNowMaxID(sql1,"functionid")+1,getNowMaxID(sql2,"paramid")+1,getNowMaxID(sql3, "funcmodelid")+1,getNowMaxID(sql4, "procedureid")+1,getNowMaxID(sql5, "paramid")+1,getNowMaxID(sql6, "modelid")+1};
    }
    
   
    
    private int getNowMaxID(String sql, String columName) throws SQLException{
    	int maxID=0;
    	PreparedStatement pstmt = null;  
        ResultSet rs = null;  
          
        Connection conn = null;  
          
        try {  
            Class.forName(driverName);  
              
            conn = DriverManager.getConnection(url, user, password);  
              
            System.out.println(conn);  
             
            pstmt = conn.prepareStatement(sql); 
              
            rs = pstmt.executeQuery();  
            while (rs.next()){
            	maxID=Integer.valueOf(rs.getString(columName));
            }
                
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }finally{  
            try{  
                if(rs != null){  
                    rs.close();  
                }  
                if(pstmt != null){  
                    pstmt.close();  
                }  
                if(conn != null){  
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
		}finally{  
            try{  
                if(rs != null){  
                    rs.close();  
                }  
                if(pstmt != null){  
                    pstmt.close();  
                }  
                if(conn != null){  
                    conn.close();  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }     
        } 
		return true;
	}
	
	
}
