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

	private static final String PORTAL_SERVER_URL = "https://proddev16:8443/swp.trader.core-web/rs/DocumentRetrievalService";
	private static final String AUTH_SERVER_URL = "https://proddev21.athens.intrasoft-intl.private:8443/auth/realms/swp/protocol/openid-connect/token";
	private JFrame frmProteusLpco;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField docIDField;
	private JTextPane txtpnDDD;
	private JComboBox lpcoTypeField;

	private OAuth2Client restClient = new OAuth2Client();
	private JTextField authServerUrlField;
	private JTextField portalServerUrlField;

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

		usernameField = new JTextField();
		usernameField.setBounds(122, 27, 110, 20);
		panel.add(usernameField);
		usernameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(36, 58, 77, 14);
		panel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(122, 55, 110, 20);
		panel.add(passwordField);

		JButton btnGetStatus = new JButton("Get Status");
		btnGetStatus.setBounds(25, 148, 100, 23);
		btnGetStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					txtpnDDD.setText("");
					setFormValues();

					Map<String, String> state = restClient.retrieveState();

					StringBuilder text = new StringBuilder();
					text.append("LPCO: " + state.get("LPCO") + "\n");
					text.append("ID: " + state.get("businessID") + "\n");
					text.append("Code: " + state.get("statusCode") + "\n");
					text.append("Description: " + state.get("statusDescription") + "\n");

					txtpnDDD.setText(text.toString());
				} catch (JsonParseException e1) {
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnGetStatus);

		JLabel lblDocumentId = new JLabel("LPCO");
		lblDocumentId.setBounds(36, 87, 77, 14);
		panel.add(lblDocumentId);

		JLabel lblDocumentType = new JLabel("Business ID");
		lblDocumentType.setBounds(36, 115, 77, 14);
		panel.add(lblDocumentType);

		JButton btnDocument = new JButton("Get Document");
		btnDocument.setBounds(132, 148, 130, 23);
		panel.add(btnDocument);

		lpcoTypeField = new JComboBox();
		lpcoTypeField.setModel(new DefaultComboBoxModel(
				new String[] { "Veterinary", "Phytosanitary", "Temporary Storage", "Simplified Procedure" }));
		lpcoTypeField.setBounds(122, 84, 110, 20);
		panel.add(lpcoTypeField);

		docIDField = new JTextField();
		docIDField.setColumns(10);
		docIDField.setBounds(122, 112, 220, 20);
		panel.add(docIDField);

		authServerUrlField = new JTextField();
		authServerUrlField.setText(AUTH_SERVER_URL);
		authServerUrlField.setColumns(10);
		authServerUrlField.setBounds(10, 207, 300, 20);
		authServerUrlField.setCaretPosition(0);
		panel.add(authServerUrlField);

		portalServerUrlField = new JTextField();
		portalServerUrlField.setText(PORTAL_SERVER_URL);
		portalServerUrlField.setColumns(10);
		portalServerUrlField.setBounds(10, 238, 300, 20);
		portalServerUrlField.setCaretPosition(0);
		panel.add(portalServerUrlField);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(369, 36, 305, 266);
		frmProteusLpco.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		txtpnDDD = new JTextPane();
		txtpnDDD.setBounds(0, 0, 305, 266);
		panel_1.add(txtpnDDD);
	}

	private void setFormValues() {
		restClient.setAuthResource(authServerUrlField.getText());
		restClient.setPortalResource(portalServerUrlField.getText());
		restClient.setUsername(usernameField.getText());
		restClient.setPassword(passwordField.getText());
		restClient.setBusinessID(docIDField.getText());
		restClient.setLpcoType(lpcoTypeField.getSelectedItem().toString());
	}
}
