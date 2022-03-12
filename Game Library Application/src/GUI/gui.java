package GUI;

import javax.swing.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import Database.Database;
import User.Client;

import javax.swing.border.LineBorder;

public class gui {
	private String id = "admin";
	private String pw = "admin";
	private JTextField gameNameTxt;
	private JTextField descTxtField;
	private JTextField pubTxtField;
	private JTextField priceTxtField;
	private JTextField scoreTxtField;
	private JTextField idTxtField;
	private JLabel balanceLabel;
	private int uid = 0;
	public JFrame frame;
//	Connection conn = null;
//	PreparedStatement pst = null;
//	ResultSet rs = null;
	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel model2 = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Database.establishConnection();
					loginGui login = new loginGui();
					login.frame.setVisible(true);
//					gui2 window = new gui2();
//					window.frame.setVisible(false);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui() {

//		conn = JDBC.ConnectDB();
//		while (Database.whoLoggedIn() < 0) {
		uid = Database.whoLoggedIn();
//		}
		System.out.println(uid);
		if (uid > 0) {
			initialize();
			updateShopTable();
			updateLibraryTable();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public void updateShopTable() {

//		if (conn != null) {
//			String sql = "SELECT * FROM Games WHERE gid NOT in (SELECT gid FROM Ownership)";
//			
		try {
			model.setRowCount(0);
			ArrayList<Integer> gids = Database.listGamesOfNotOwnedByUser(uid);
			for (int i = 0; i < gids.size(); i++) {
				ArrayList<String> info = Database.listGameInfo(gids.get(i));
				Object[] columnData = new Object[7];
				columnData[0] = info.get(0);
				columnData[1] = info.get(1);
				columnData[2] = info.get(2);
				columnData[3] = info.get(3);
				columnData[4] = info.get(4);
				columnData[5] = info.get(5);
				columnData[6] = info.get(6);
				if (columnData[6].equals("true")) {
					columnData[6] = "Multiplayer";
				} else {
					columnData[6] = "Single Player";
				}

				model.addRow(columnData);
			}
//				pst = conn.prepareStatement(sql);
//				rs = pst.executeQuery();
//				
//
//				while (rs.next()) {
//					columnData[0] = rs.getString("gid");
//					columnData[1] = rs.getString("name");
//					columnData[2] = rs.getString("description");
//					columnData[3] = rs.getString("publisher");
//					columnData[4] = rs.getString("price");
//					columnData[5] = rs.getString("score");
//					columnData[6] = rs.getString("is_multi");
//
//					if (columnData[6].equals("1")) {
//						columnData[6] = "Multiplayer";
//					} else {
//						columnData[6] = "Single Player";
//					}

//
//				}
			model.fireTableDataChanged();
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

//		}
	}

	public void updateLibraryTable() {

//		if (conn != null) {
//			String sql = "SELECT * FROM Games WHERE gid in (SELECT gid FROM Ownership)";
		try {
			model2.setRowCount(0);
			ArrayList<Integer> gids = Database.listGamesOfUser(uid);
			for (int i = 0; i < gids.size(); i++) {
				ArrayList<String> info = Database.listGameInfo(gids.get(i));
				Object[] columnData = new Object[7];
				columnData[0] = info.get(0);
				columnData[1] = info.get(1);
				columnData[2] = info.get(2);
				columnData[3] = info.get(3);
				columnData[4] = info.get(4);
				columnData[5] = info.get(5);
				columnData[6] = info.get(6);
				if (columnData[6].equals("true")) {
					columnData[6] = "Multiplayer";
				} else {
					columnData[6] = "Single Player";
				}
				model2.addRow(columnData);
			}

			// pst = conn.prepareStatement(sql);
//				rs = pst.executeQuery();

//			while (rs.next()) {
//				columnData[0] = rs.getString("gid");
//				columnData[1] = rs.getString("name");
//				columnData[2] = rs.getString("description");
//				columnData[3] = rs.getString("publisher");
//				columnData[4] = rs.getString("price");
//				columnData[5] = rs.getString("score");
//				columnData[6] = rs.getString("is_multi");
//
//				if (columnData[6].equals("1")) {
//					columnData[6] = "Multiplayer";
//				} else {
//					columnData[6] = "Single Player";
//				}

//
//			}
			model2.fireTableDataChanged();
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

//		}
	}

	private void initialize() {
		frame = new JFrame("Game Library Application");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
//		frame.setResizable(false);
		JTable table = new JTable();
		Client c = new Client(1000);
//		try {
//			PreparedStatement info = conn
//					.prepareStatement("INSERT INTO info (username, password, balance) VALUES ('admin','admin',1000)");
//			info.executeUpdate();
//			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM info;");
//			ResultSet rs = stmt.executeQuery();
//			if (rs.next()) {
//				adminUsername = rs.getString("username");
//				adminPassword = rs.getString("password");
//				c.balance = rs.getFloat("balance");
//			}
//			rs.close();
//		}
//
//		catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e);
//		}
		c.setBalance(Database.getBalance(uid));
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
//////////////////////////////////////////ADMIN PANELI/////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		JFrame adminFrame = new JFrame("Admin Panel");
		adminFrame.setTitle("Admin Panel");
		adminFrame.setBounds(100, 100, 264, 458);
		adminFrame.setResizable(false);
		adminFrame.getContentPane().setLayout(null);

		JButton adminLoginBtn = new JButton("Admin Login");

		adminLoginBtn.setBounds(96, 81, 116, 25);

		JTextField idTxt = new JTextField();
		idTxt.setBounds(96, 13, 116, 22);

		idTxt.setColumns(10);

		JTextField pwTxt = new JTextField();
		pwTxt.setColumns(10);
		pwTxt.setBounds(96, 49, 116, 22);

		JLabel idLbl = new JLabel("ID: ");
		idLbl.setBounds(63, 16, 21, 16);

		JLabel pwLbl = new JLabel("Password:");
		pwLbl.setBounds(23, 52, 72, 16);

		JPanel adminLoginPanel2 = new JPanel();
		adminLoginPanel2.setBounds(0, 0, 258, 423);
		adminFrame.getContentPane().add(adminLoginPanel2);
		adminLoginPanel2.setLayout(null);
		adminFrame.getContentPane().add(adminLoginPanel2);

		JPanel adminLoginPanel = new JPanel();
		adminLoginPanel.setBounds(0, 0, 258, 423);
		adminFrame.getContentPane().add(adminLoginPanel);
		adminLoginPanel.setLayout(null);

		adminLoginPanel.setVisible(false);

		adminLoginPanel2.add(adminLoginBtn);
		adminLoginPanel2.add(idTxt);
		adminLoginPanel2.add(pwTxt);
		adminLoginPanel2.add(idLbl);
		adminLoginPanel2.add(pwLbl);

		JButton addGameButton = new JButton("Add Game");
		addGameButton.setBackground(Color.LIGHT_GRAY);
		addGameButton.setForeground(Color.BLACK);
		adminLoginPanel.add(addGameButton);

		addGameButton.setBounds(102, 224, 110, 25);

		gameNameTxt = new JTextField();
		gameNameTxt.setBounds(102, 54, 102, 22);
		gameNameTxt.setColumns(10);
		adminLoginPanel.add(gameNameTxt);

		JLabel addGameLbl = new JLabel("Game Name: ");
		addGameLbl.setBounds(22, 57, 79, 16);
		adminLoginPanel.add(addGameLbl);

		JLabel lblAddingGame = new JLabel("Adding Game: ");
		lblAddingGame.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblAddingGame.setBounds(15, 0, 250, 22);
		adminLoginPanel.add(lblAddingGame);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(35, 84, 68, 16);
		adminLoginPanel.add(lblDescription);

		descTxtField = new JTextField();
		descTxtField.setColumns(10);
		descTxtField.setBounds(105, 81, 102, 22);
		adminLoginPanel.add(descTxtField);

		JLabel lblPublisher = new JLabel("Publisher:");
		lblPublisher.setBounds(46, 113, 57, 16);
		adminLoginPanel.add(lblPublisher);

		pubTxtField = new JTextField();
		pubTxtField.setColumns(10);
		pubTxtField.setBounds(106, 108, 101, 22);
		adminLoginPanel.add(pubTxtField);

		priceTxtField = new JTextField();
		priceTxtField.setColumns(10);
		priceTxtField.setBounds(108, 137, 101, 22);
		adminLoginPanel.add(priceTxtField);

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(70, 140, 33, 16);
		adminLoginPanel.add(lblPrice);

		scoreTxtField = new JTextField();
		scoreTxtField.setColumns(10);
		scoreTxtField.setBounds(108, 162, 101, 22);
		adminLoginPanel.add(scoreTxtField);

		idTxtField = new JTextField();
		idTxtField.setColumns(10);
		idTxtField.setBounds(6, 330, 128, 16);
		adminLoginPanel.add(idTxtField);

		/////////////////////////////

		JLabel lblMetaScore = new JLabel("Meta Score:");
		lblMetaScore.setBounds(33, 170, 70, 16);
		adminLoginPanel.add(lblMetaScore);

		JLabel lblIsMultiplayer = new JLabel("Is Multiplayer? :");
		lblIsMultiplayer.setBounds(12, 192, 91, 16);
		adminLoginPanel.add(lblIsMultiplayer);

		JComboBox isMultiComboBox = new JComboBox();
		isMultiComboBox.setModel(new DefaultComboBoxModel(new String[] { "true", "false" }));
		isMultiComboBox.setBounds(106, 189, 106, 22);
		adminLoginPanel.add(isMultiComboBox);

		JLabel lblDeletingGame = new JLabel("Deleting Game:");
		lblDeletingGame.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDeletingGame.setBounds(11, 267, 250, 22);
		adminLoginPanel.add(lblDeletingGame);

		JLabel lblSelectGameFrom = new JLabel("Select game ID from list: ");
		lblSelectGameFrom.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSelectGameFrom.setBounds(6, 301, 250, 16);
		adminLoginPanel.add(lblSelectGameFrom);

		JButton btnDeleteGame = new JButton("Delete Game");
		btnDeleteGame.setForeground(Color.BLACK);
		btnDeleteGame.setBackground(Color.LIGHT_GRAY);
		btnDeleteGame.setBounds(6, 363, 200, 47);
		adminLoginPanel.add(btnDeleteGame);

		adminLoginBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (idTxt.getText().equals("admin") && pwTxt.getText().equals("admin")) {
					adminLoginPanel2.setVisible(false);
					adminLoginPanel.setVisible(true);
				}
			}
		});

		addGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {


				Database.addGame(gameNameTxt.getText(), descTxtField.getText(), pubTxtField.getText(),
						Float.parseFloat(priceTxtField.getText()), Float.parseFloat(scoreTxtField.getText()),
						Boolean.parseBoolean((String) isMultiComboBox.getSelectedItem()));
				updateShopTable();
				gameNameTxt.setText("");
				descTxtField.setText("");
				pubTxtField.setText("");
				priceTxtField.setText("");
				scoreTxtField.setText("");
			}
		});
		btnDeleteGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int gid = Integer.parseInt(idTxtField.getText());
				float b = Database.getPrice(gid);
				c.setBalance(c.getBalance() + b);
				Database.setBalance(uid, c.getBalance());
				Database.deleteGameFromUser(uid, gid);
				Database.deleteGame(gid);
				updateShopTable();
				updateLibraryTable();
				balanceLabel.setText(c.getBalance() + " TL");
				idTxtField.setText("");
			}
		});
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			


		Object[] columns = { "ID", "Game Name", "Description", "Publisher", "Price", "Meta Score", "Multiplayer" };

		model.setColumnIdentifiers(columns);


		table.setModel(model);


		table.setBackground(Color.LIGHT_GRAY);
		table.setForeground(Color.black);
		Font font = new Font("", 1, 22);
		table.setFont(font);
		table.setRowHeight(30);


		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(0, 84, 1423, 585);

		frame.getContentPane().setLayout(null);



		JPanel shopPanel = new JPanel();
		shopPanel.setBorder(new LineBorder(new Color(64, 64, 64), 3));
		shopPanel.setBackground(Color.LIGHT_GRAY);
		shopPanel.setBounds(0, 49, 1423, 669);
		frame.getContentPane().add(shopPanel);
		shopPanel.setLayout(null);
		shopPanel.add(pane);

		JButton buyBtn = new JButton("Buy Game");

		buyBtn.setForeground(Color.BLACK);
		buyBtn.setFont(new Font("Tahoma", Font.BOLD, 17));
		buyBtn.setBackground(Color.GRAY);
		buyBtn.setBounds(12, 13, 125, 58);
		shopPanel.add(buyBtn);

		JButton refundBtn = new JButton("Refund Game");

		refundBtn.setForeground(Color.BLACK);
		refundBtn.setFont(new Font("Tahoma", Font.BOLD, 17));
		refundBtn.setBackground(Color.GRAY);
		refundBtn.setBounds(12, 13, 160, 58);
		refundBtn.setVisible(false);

		JPanel topPanel = new JPanel();
		topPanel.setBorder(new LineBorder(new Color(64, 64, 64), 5));
		topPanel.setBackground(Color.GRAY);
		topPanel.setBounds(0, 0, 1423, 50);
		frame.getContentPane().add(topPanel);
		topPanel.setLayout(null);

		JLabel balanceTxtLabel = new JLabel("Balance:");
		balanceTxtLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		balanceTxtLabel.setBounds(930, 14, 96, 16);
		topPanel.add(balanceTxtLabel);

		balanceLabel = new JLabel("");
		balanceLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		balanceLabel.setBounds(1030, 14, 146, 16);
		topPanel.add(balanceLabel);

		JLabel userLabel = new JLabel("Logged in as " + Database.getUsername(uid));
		userLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		userLabel.setBounds(1150, 12, 300, 23);
		topPanel.add(userLabel);

		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getColumnModel().getColumn(2).setPreferredWidth(101);
		table.getColumnModel().getColumn(3).setPreferredWidth(51);
		table.getColumnModel().getColumn(4).setPreferredWidth(50);
		table.getColumnModel().getColumn(5).setPreferredWidth(1);
		table.getColumnModel().getColumn(6).setPreferredWidth(58);

		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
		balanceLabel.setText(c.getBalance() + " TL");

		JButton adminLoginBtn2 = new JButton("Admin Login");
		adminLoginBtn2.setFont(new Font("Tahoma", Font.BOLD, 17));
		adminLoginBtn2.setBackground(Color.LIGHT_GRAY);
		adminLoginBtn2.setForeground(Color.BLACK);
		adminLoginBtn2.setBounds(12, 7, 200, 35);
		topPanel.add(adminLoginBtn2);

		JButton libraryBtn = new JButton("Library");

		libraryBtn.setForeground(Color.BLACK);
		libraryBtn.setFont(new Font("Tahoma", Font.BOLD, 17));
		libraryBtn.setBackground(Color.LIGHT_GRAY);
		libraryBtn.setBounds(225, 7, 200, 35);
		topPanel.add(libraryBtn);

		JButton shopBtn = new JButton("Shop");
		shopBtn.setForeground(Color.BLACK);
		shopBtn.setFont(new Font("Tahoma", Font.BOLD, 17));
		shopBtn.setBackground(Color.LIGHT_GRAY);
		shopBtn.setBounds(225, 6, 200, 35);
		topPanel.add(shopBtn);

		JButton addBalanceBtn = new JButton("Add Balance");
		addBalanceBtn.setForeground(Color.BLACK);
		addBalanceBtn.setFont(new Font("Tahoma", Font.BOLD, 17));
		addBalanceBtn.setBackground(Color.LIGHT_GRAY);
		addBalanceBtn.setBounds(763, 7, 150, 35);
		topPanel.add(addBalanceBtn);

		JTextField addBalanceTxtField = new JTextField();
		addBalanceTxtField.setBounds(650, 7, 100, 35);
		topPanel.add(addBalanceTxtField);
		/////////////////////////

		JLabel topPanelLocationLabel = new JLabel("SHOP");
		topPanelLocationLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		topPanelLocationLabel.setBounds(500, 6, 157, 40);
		topPanel.add(topPanelLocationLabel);
		shopBtn.setVisible(false);

		JTable table2 = new JTable();
		Object[] columns2 = { "ID", "Game Name", "Description", "Publisher", "Price", "Meta Score", "Multiplayer" };
//      DefaultTableModel model2 = new DefaultTableModel();
		model2.setColumnIdentifiers(columns2);

		// set the model to the table
		table2.setModel(model2);

		// Change A JTable Background Color, Font Size, Font Color, Row Height
		table2.setBackground(Color.LIGHT_GRAY);
		table2.setForeground(Color.black);
		table2.setFont(font);
		table2.setRowHeight(30);

		// create JScrollPane
		JScrollPane pane2 = new JScrollPane(table2);
		pane2.setBounds(0, 84, 1423, 585);

		frame.getContentPane().setLayout(null);

//      frame.getContentPane().add(pane);

		JPanel libraryPanel = new JPanel();
		libraryPanel.setBorder(new LineBorder(new Color(64, 64, 64), 3));
		libraryPanel.setBackground(Color.LIGHT_GRAY);
		libraryPanel.setBounds(0, 49, 1423, 669);
		frame.getContentPane().add(libraryPanel);
		libraryPanel.setLayout(null);
		libraryPanel.add(pane2);

		libraryPanel.add(refundBtn);

		table2.getColumnModel().getColumn(0).setPreferredWidth(1);
		table2.getColumnModel().getColumn(1).setPreferredWidth(300);
		table2.getColumnModel().getColumn(2).setPreferredWidth(101);
		table2.getColumnModel().getColumn(3).setPreferredWidth(51);
		table2.getColumnModel().getColumn(4).setPreferredWidth(50);
		table2.getColumnModel().getColumn(5).setPreferredWidth(1);
		table2.getColumnModel().getColumn(6).setPreferredWidth(58);

		libraryBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				shopBtn.setVisible(true);
				libraryBtn.setVisible(false);
				shopPanel.setVisible(false);
				libraryPanel.setVisible(true);
				topPanelLocationLabel.setText("LIBRARY");
				refundBtn.setVisible(true);

			}
		});

		shopBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				shopBtn.setVisible(false);
				libraryBtn.setVisible(true);
				shopPanel.setVisible(true);
				libraryPanel.setVisible(false);
				topPanelLocationLabel.setText("SHOP");
				refundBtn.setVisible(false);
			}
		});

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	        
////////////////////////////////////SATIN ALMA TUÞU////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		buyBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int i = table.getSelectedRow();
					if (i >= 0) {


						String a = model.getValueAt(i, 4).toString();
						float b = Float.parseFloat(a);
						if (c.getBalance() >= b) {

							c.setBalance(c.getBalance() - b);

//

							int gid = Integer.parseInt((String) model.getValueAt(i, 0));


							Database.setBalance(uid, c.getBalance());
							Database.addGameToUser(uid, gid);
							updateShopTable();
							updateLibraryTable();
							balanceLabel.setText(c.getBalance() + " TL");
						} else {
							JOptionPane.showMessageDialog(null, "Not enough balance.");
						}
					}
				}

				catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	      
//////////////////////////////IADE TUÞU////////////////////////////////////////////////////////////////////////////////////////      
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	        
		refundBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int i = table2.getSelectedRow();
					if (i >= 0) {
//						Object[] refundingGame = new Object[7];
//						refundingGame[0] = model2.getValueAt(i, 0);
//						refundingGame[1] = model2.getValueAt(i, 1);
//						refundingGame[2] = model2.getValueAt(i, 2);
//						refundingGame[3] = model2.getValueAt(i, 3);
//						refundingGame[4] = model2.getValueAt(i, 4);
//						refundingGame[5] = model2.getValueAt(i, 5);
//						refundingGame[6] = model2.getValueAt(i, 6);
//						if (refundingGame[4].equals("0.0")) {
//							refundingGame[4] = "Free to Play";
//						}

						String a = model2.getValueAt(i, 4).toString();
						float b = Float.parseFloat(a);
						c.setBalance(c.getBalance() + b);
						Database.setBalance(uid, c.getBalance());
//						PreparedStatement stmt = conn.prepareStatement("UPDATE info SET balance=?;");
//						stmt.setFloat(1, c.balance);
//						stmt.executeUpdate();
////					model.addRow(refundingGame);
////					model2.removeRow(i);
//						PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Ownership WHERE gid=?;");
						int gid = Integer.parseInt((String) model2.getValueAt(i, 0));
//						stmt2.setInt(1, gid);
//						stmt2.executeUpdate();
						Database.deleteGameFromUser(uid, gid);
						JOptionPane.showMessageDialog(null, "Successfully refunded.");
						updateShopTable();
						updateLibraryTable();
						balanceLabel.setText(c.getBalance() + " TL");
					}
				}

				catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}

		});
		addBalanceBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String a = addBalanceTxtField.getText();
					float b = Float.parseFloat(a);
					c.setBalance(c.getBalance() + b);
					addBalanceTxtField.setText("");

					Database.setBalance(uid, c.getBalance());
					balanceLabel.setText(c.getBalance() + " TL");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e);
				}
			}

		});
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
/////////////////////////////ADMIN PANEL BUTONU////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		adminLoginBtn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				adminFrame.setVisible(true);
			}
		});

		table2.getTableHeader().setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));

//		JFrame adminFrame = new JFrame("Admin Panel");
//		adminFrame.setBounds(100, 100, 450, 300);
//		adminFrame.setVisible(false);
//		
//		adminLoginBtn.addMouseListener(new MouseAdapter() {
//        	@Override
//        	public void mouseClicked(MouseEvent e) {
//        		adminFrame.setVisible(true);
//        	}
//        });

		frame.setSize(1441, 764);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
