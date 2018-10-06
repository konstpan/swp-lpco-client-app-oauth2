package com.konstpan.lpcoreg;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ClientApp {

	private JFrame frmProteusLpco;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField textField_1;
	private JTextPane txtpnDDD;

	private OAuth2Client restClient = new OAuth2Client();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApp window = new ClientApp();
					window.frmProteusLpco.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProteusLpco = new JFrame();
		frmProteusLpco.setTitle("PROTEUS - Document Validation");
		frmProteusLpco.setBounds(100, 100, 700, 500);
		frmProteusLpco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProteusLpco.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 36, 349, 266);
		frmProteusLpco.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblClientId = new JLabel("Username");
		lblClientId.setBounds(36, 30, 77, 14);
		panel.add(lblClientId);

		textField = new JTextField();
		textField.setBounds(122, 27, 110, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(36, 58, 77, 14);
		panel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(122, 55, 110, 20);
		panel.add(passwordField);

		JButton btnGetStatus = new JButton("Status");
		btnGetStatus.setBounds(36, 148, 89, 23);
		btnGetStatus.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					Map<String, String> state = restClient.retrieveState(textField.getText(), passwordField.getText());

					StringBuilder text = new StringBuilder();
					text.append("Code: " + state.get("status") + "\n");
					text.append("Description: " + state.get("statusDescr") + "\n");

					txtpnDDD.setText(text.toString());
				} catch (JsonParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnGetStatus);

		JLabel lblDocumentId = new JLabel("LPCO");
		lblDocumentId.setBounds(36, 87, 77, 14);
		panel.add(lblDocumentId);

		JLabel lblDocumentType = new JLabel("Document ID");
		lblDocumentType.setBounds(36, 115, 77, 14);
		panel.add(lblDocumentType);

		JButton btnDocument = new JButton("Document");
		btnDocument.setBounds(132, 148, 100, 23);
		panel.add(btnDocument);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(
				new String[] { "Veterinary", "Phytosanitary", "Temporary Storage", "Simplified Procedure" }));
		comboBox.setBounds(122, 84, 110, 20);
		panel.add(comboBox);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(122, 112, 220, 20);
		panel.add(textField_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(369, 36, 305, 266);
		frmProteusLpco.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		txtpnDDD = new JTextPane();
		txtpnDDD.setBounds(0, 0, 305, 266);
		panel_1.add(txtpnDDD);
	}
}
