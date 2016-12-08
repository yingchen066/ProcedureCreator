1.软件运行环境
安装jre1.7及以上的操作系统

2.打开方式
Windows：双击运行ProcedureCreator.bat，或在命令行下进入软件所在目录运行java -jar ProcedureCreator.jar
打开后请勿关闭命令行窗口
Mac/Linux:直接双击打开ProcedureCreator.jar即可，或在命令行下进入软件所在目录运行java -jar ProcedureCreator.jar

3.设置数据库连接
软件在第一次进入时需要设置数据库连接参数，之后可在主界面点设置按钮进行修改，数据库配置文件保存在本目录的dbConfig.txt下请勿删除
以192.168.101.9的数据库 配置参数如下：
数据库ip：192.168.101.9
端口号：1536
数据库名：stdcg
用户名：dlsys
密码：dlsys
配置好数据库连接参数之后即可进入主界面

4.主界面
①左侧框为存储过程的输入框，右侧框为insert语句的输出框
②将存储过程全部复制粘贴到左侧框中，然后点击“生成sql语句”按钮，即可在右侧框中看到生成的insert语句
③检查右侧框中的insert语句是否符合要求，可更具需要修改函数名，参数名以及描述文字
④点击插入到数据库按钮即可将修改后的数据插入到数据库中
⑤（可选）如有需要，点击“生成updateSQL文件”按钮即可在软件所在的目录看到生成的updateSQL文件夹用于上传svn或备份等；

PS:
1.本软件以dlmis的存储过程为基础开发，对其他命名空间的存储过程兼容性可能不太好，因此如果是其他命名空间的存储过程请仔细执行4-③操作
2.本软件同时支持function与procedure开头的存储过程
3.暂不支持需要添加model的存储过程的需求，如有需要请在4-③步骤中自行修改

项目开发地址：https://github.com/yingchen066/ProcedureCreator
   # ProcedureCreator
