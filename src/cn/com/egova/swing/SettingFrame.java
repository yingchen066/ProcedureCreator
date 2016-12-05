package cn.com.egova.swing;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import cn.com.egova.util.Constant;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Color;

public class SettingFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7666097760065310804L;
	private JTextField tfUrl;
	private JTextField tfPort;
	private JTextField tfDbname;
	private JTextField tfUsername;
	private JTextField tfPassword;
	private JButton btnOk;
	private JLabel lblNullHint;

	public SettingFrame() {

		FormLayout formLayout = new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, });
		Container contentPane = getContentPane();
		contentPane.setLayout(formLayout);
		JLabel lblurl = new JLabel("\u6570\u636E\u5E93IP");
		contentPane.add(lblurl, "6, 4, center, default");
		tfUrl = new JTextField();
		tfUrl.setText(Constant.DB_URL);
		contentPane.add(tfUrl, "8, 4, left, default");
		tfUrl.setColumns(12);

		JLabel lblNewLabel = new JLabel("\u7AEF\u53E3\u53F7");
		contentPane.add(lblNewLabel, "6, 6, center, default");

		tfPort = new JTextField();
		tfPort.setText(Constant.DB_PORT);
		contentPane.add(tfPort, "8, 6, left, default");
		tfPort.setColumns(6);

		JLabel label = new JLabel("\u6570\u636E\u5E93\u540D");
		contentPane.add(label, "6, 8, center, default");

		tfDbname = new JTextField();
		tfDbname.setText(Constant.DB_NAME);
		contentPane.add(tfDbname, "8, 8, left, default");
		tfDbname.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("\u7528\u6237\u540D");
		contentPane.add(lblNewLabel_1, "6, 10, center, default");

		tfUsername = new JTextField();
		tfUsername.setText(Constant.DB_USERNAME);
		contentPane.add(tfUsername, "8, 10, left, default");
		tfUsername.setColumns(10);

		JLabel label_1 = new JLabel("\u5BC6\u7801");
		contentPane.add(label_1, "6, 12, center, default");

		tfPassword = new JTextField();
		tfPassword.setText(Constant.DB_PASSWORD);
		contentPane.add(tfPassword, "8, 12, left, default");
		tfPassword.setColumns(10);

		lblNullHint = new JLabel("  ");
		lblNullHint.setForeground(Color.RED);
		getContentPane().add(lblNullHint, "8, 14, left, default");

		btnOk = new JButton("\u786E\u5B9A");
		contentPane.add(btnOk, "1, 16, 8, 1, center, default");
		btnOk.addActionListener(this);
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 300) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 250) / 2;
		setLocation(w, h);
		setSize(300, 250);
		setResizable(false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String url = tfUrl.getText().trim();
		String port = tfPort.getText().trim();
		String dbName = tfDbname.getText().trim();
		String username = tfUsername.getText().trim();
		String password = tfPassword.getText().trim();
		if (MainFrame.checkNull(url, port, dbName, username, password)) {
			Constant.DB_URL = url;
			Constant.DB_PORT = port;
			Constant.DB_NAME = dbName;
			Constant.DB_USERNAME = username;
			Constant.DB_PASSWORD = password;
			MainFrame.saveConfigsToFile();
			dispose();
			if (!MainFrame.isShoewMainFrame) {
				MainFrame.mainFrame.setVisible(true);
			}
		} else {
			lblNullHint.setText("请把信息填写完整");
		}
	}

}
