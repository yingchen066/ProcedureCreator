package cn.com.egova.swing;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import cn.com.egova.dao.OracleDAO;
import cn.com.egova.procedure.Parser;
import cn.com.egova.util.Constant;
import cn.com.egova.util.FileUtil;

public class ProcedureCreatorFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7302923768954513584L;
	private JLabel errorTips;
	private JTextArea textProcedure;
	private JTextArea textInsertSql;
	private JLabel tips;
	private JTextArea textDeleteSql;

	public ProcedureCreatorFrame() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		JButton btnCreateInsertSql = new JButton("\u751F\u6210sql\u8BED\u53E5");
		panel_1.add(btnCreateInsertSql);
		btnCreateInsertSql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tips.setText(" ");
				errorTips.setText("  ");
				tips.setText("正在生成sql语句");
				String procedureStr = textProcedure.getText();
				if (procedureStr == null || "".equals(procedureStr.trim())) {
					errorTips.setText("请先输入存储过程");
					tips.setText(" ");
					return;
				} else {
					try {
						boolean success = Parser.parseProcedure(textProcedure.getText(), false);
						if (success) {
							textInsertSql.setText(Parser.insertSql);
							textDeleteSql.setText(Parser.deleteSql);
							errorTips.setText(" ");
							tips.setText("生成sql语句成功");
							return;
						} else {
							errorTips.setText("请输入正确的存储过程");
							tips.setText(" ");
							return;
						}
					} catch (SQLException e1) {
						errorTips.setText("数据库连接错误；"+e1.getMessage());
						tips.setText(" ");
						return;
					}
				}
			}
		});

		JButton btnExcuteInDatabase = new JButton("\u63D2\u5165\u5230\u6570\u636E\u5E93");
		panel_1.add(btnExcuteInDatabase);
		btnExcuteInDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tips.setText(" ");
				errorTips.setText("  ");
				if (textInsertSql.getText() == null || textInsertSql.getText().trim().equals("")) {
					errorTips.setText("请先生成sql语句！");
					return;
				}
				OracleDAO dao = new OracleDAO();

				tips.setText("正在插入insert语句到数据库");

				String string = textInsertSql.getText();
				String[] sqls = string.split("\r\n");
				int count = 0;
				for (String sql : sqls) {

					if (sql != null && !"".equals(sql)) {
						sql = sql.substring(0, sql.indexOf(";"));
						if (dao.excuteSQL(sql)) {
							count++;
						}
					}
				}
				tips.setText("成功插入" + count + "条数据");
				if (count > 0) {
					FileUtil.save(string,
							Constant.DIR + "/updateSQLs_"+Parser.procedureName.substring(6)+"/dlsys/data/add"
									+ Parser.funcitionName.substring(0, 1).toUpperCase()
									+ Parser.funcitionName.substring(1) + ".sql",
							false);
					FileUtil.save(textDeleteSql.getText(),
							Constant.DIR + "/updateSQLs_"+Parser.procedureName.substring(6)+"/dlsys/data/delete"
									+ Parser.funcitionName.substring(0, 1).toUpperCase()
									+ Parser.funcitionName.substring(1) + ".sql",
							false);
				}
			}
		});

		JButton btnDeleteFromDatabase = new JButton("删除已插入数据");
		panel_1.add(btnDeleteFromDatabase);
		btnDeleteFromDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tips.setText(" ");
				errorTips.setText("  ");
				if (textDeleteSql.getText() == null || textDeleteSql.getText().trim().equals("")) {
					errorTips.setText("请先生成sql语句！");
					return;
				}
				OracleDAO dao = new OracleDAO();

				tips.setText("正在从数据库删除记录");

				String string = textDeleteSql.getText();
				String[] sqls = string.split("\r\n");
				int count = 0;
				for (String sql : sqls) {

					if (sql != null && !"".equals(sql)) {
						sql = sql.substring(0, sql.indexOf(";"));
						if (dao.excuteSQL(sql)) {
							count++;
						}
					}
				}
				tips.setText("删除成功");
				if (count > 0) {
					FileUtil.save(textInsertSql.getText(),
							Constant.DIR + "/updateSQLs" + Parser.createTime + "/dlsys/data/add"
									+ Parser.funcitionName.substring(0, 1).toUpperCase()
									+ Parser.funcitionName.substring(1) + ".sql",
							false);
					FileUtil.save(string,
							Constant.DIR + "/updateSQLs" + Parser.createTime + "/dlsys/data/delete"
									+ Parser.funcitionName.substring(0, 1).toUpperCase()
									+ Parser.funcitionName.substring(1) + ".sql",
							false);
				}

			}
		});

		JButton btnSetting = new JButton("\u8BBE\u7F6E");
		panel_1.add(btnSetting);
		btnSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tips.setText(" ");
				errorTips.setText("  ");
				new SettingFrame().setVisible(true);
			}
		});

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new GridLayout(0, 2));

		JLabel InputTip = new JLabel(
				"\u8BF7\u5728\u5DE6\u8FB9\u8F93\u5165\u6846\u8F93\u5165\u5B58\u50A8\u8FC7\u7A0B\uFF1A");
		panel_2.add(InputTip);

		errorTips = new JLabel("  ");
		errorTips.setHorizontalAlignment(SwingConstants.RIGHT);
		errorTips.setForeground(Color.RED);
		panel_2.add(errorTips);

		tips = new JLabel("   ");
		panel.add(tips, BorderLayout.CENTER);
		tips.setHorizontalAlignment(SwingConstants.CENTER);

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setToolTipText("");
		splitPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPane.setDividerLocation(0.4);
			}
		});
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setToolTipText("");
		splitPane.setLeftComponent(scrollPane);

		textProcedure = new JTextArea();
		textProcedure.setLineWrap(true);
		scrollPane.setViewportView(textProcedure);

		final JSplitPane splitPane_1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPane_1.setDividerLocation(0.6);
			}
		});

		JScrollPane scrollPane_2 = new JScrollPane();

		JScrollPane scrollPane_1 = new JScrollPane();

		textInsertSql = new JTextArea();
		textInsertSql.setLineWrap(true);
		scrollPane_1.setViewportView(textInsertSql);
		splitPane.setRightComponent(splitPane_1);
		splitPane_1.setTopComponent(scrollPane_1);
		splitPane_1.setBottomComponent(scrollPane_2);

		textDeleteSql = new JTextArea();
		scrollPane_2.setViewportView(textDeleteSql);

		setSize(800, 500);

		setExtendedState(Frame.MAXIMIZED_BOTH);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

}
