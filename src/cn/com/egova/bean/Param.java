package cn.com.egova.bean;

public class Param {
	//参数名
	private String name;
	//参数类型
	private String type;
	//0表示输入，1表示输出
	private int direction;
	
	public final static String TYPE_CURSOR="sys_refcursor";
	public final static String TYPE_INTEGER="integer";
	public final static String TYPE_NUMBER="number";
	public final static String TYPE_VARCHAR2="varchar2";
	public final static String TYPE_VARCHAR="varchar";
	public final static String TYPE_FLOAT="float";
	
	
	public Param() {
		super();
	}


	public Param(String name, int direction,String type) {
		super();
		if(name.charAt(1)<='Z'&&name.charAt(1)>='A'){
			name=name.substring(1,2).toLowerCase()+name.substring(2);
		}
		if(type.equalsIgnoreCase("sys_refcursor")){
			name="dataList";
		}
		this.name = name;
		this.type = type;
		this.direction = direction;
	}
	
	public Param(String name, String direction,String type) {
		this(name,direction.equalsIgnoreCase("in")?0:1,type);		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}


	@Override
	public String toString() {
		return "Param [name=" + name + ", type=" + type + ", direction=" + direction + "]";
	}
	
	
}
