package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Controller.Controller;

/**
 * Panel to search patients in database.
 * There are following filter fields: pesel, name, surname.
 * Results are shown in list.
 * 
 * @author Uzytkownik
 *
 */
public class SearchingPatientFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JLabel peselLabel;
	private JTextField peselTextField;
	
	private JLabel nameLabel;
	private JTextField nameTextField;
	
	private JLabel surnameLabel;
	private JTextField surnameTextField;

	private JTable table;
	private DefaultTableModel tableModel;
	private String[] columnNames;
	private JScrollPane listScrollPanel;
	
	private JButton searchBtn;
	private JButton cancelBtn;
	private JButton chooseBtn;
	private JButton removeBtn;
	
	public SearchingPatientFrame(){
		
		// default look
		this.setSize(new Dimension(500, 250));
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		this.setTitle("Wyszukiwanie pacjenta");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(350, 150);
		
		//components
		peselLabel = new JLabel("PESEL:");
		peselTextField = new JTextField(20);
		
		nameLabel = new JLabel("Imiê:");
		nameTextField = new JTextField(20);
		
		surnameLabel = new JLabel("Nazwisko:");
		surnameTextField = new JTextField(20);
		
		searchBtn = new JButton("Szukaj");
		cancelBtn = new JButton("Anuluj");
		chooseBtn = new JButton("Wybierz");
		removeBtn = new JButton("Usuñ");
		removeBtn.setForeground(Color.RED);
		
		
		//table definition
		columnNames = new String[]{"Imiê", "Nazwisko", "PESEL"};
		table = new JTable();
		table.setShowGrid(true);
		listScrollPanel = new JScrollPane(table);
		listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		tableModel = new DefaultTableModel(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		tableModel.setColumnIdentifiers(columnNames);
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(tableModel);
		
		//CONFIGURE GROUPS
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//horizontal groups
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(peselLabel)
						.addComponent(nameLabel)
						.addComponent(surnameLabel)
						.addComponent(searchBtn))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(peselTextField)
						.addComponent(nameTextField)
						.addComponent(surnameTextField)
						.addComponent(cancelBtn))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(listScrollPanel)
						.addGroup(layout.createSequentialGroup()
								.addComponent(chooseBtn)
								.addComponent(removeBtn))));
		
		//vertical groups
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(peselLabel)
										.addComponent(peselTextField))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(nameLabel)
										.addComponent(nameTextField))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(surnameLabel)
										.addComponent(surnameTextField)))
						.addComponent(listScrollPanel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(searchBtn)
						.addComponent(cancelBtn)
						.addComponent(chooseBtn)
						.addComponent(removeBtn)));
		
	}
	
	public void setController(Controller c){
		searchBtn.addActionListener(c);
		cancelBtn.addActionListener(c);
		chooseBtn.addActionListener(c);
		removeBtn.addActionListener(c);
	}

	public JButton getSearchBtn() {
		return searchBtn;
	}

	public JButton getCancelBtn() {
		return cancelBtn;
	}

	public JButton getChooseBtn() {
		return chooseBtn;
	}

	public JButton getRemoveBtn() {
		return removeBtn;
	}
	
	public JTextField getPeselTextField() {
		return peselTextField;
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}

	public JTextField getSurnameTextField() {
		return surnameTextField;
	}

	public JTable getTable() {
		return table;
	}
	
	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}
