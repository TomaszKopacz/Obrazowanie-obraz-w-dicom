package View;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import Controller.Controller;

/**
 * Panel to add new patient to database.
 * There are the following fields: name, surname, pesel, sex, insurance. 
 * 
 * @author Aleksandra Zajac
 *
 */
public class AddingPatientFrame extends JFrame{
	
private static final long serialVersionUID = 1L;
	
	private JLabel nameLabel;
	private JTextField nameTextField;
	
	private JLabel surnameLabel;
	private JTextField surnameTextField;
	
	private JLabel peselLabel;
	private JTextField peselTextField;
	
	private JLabel sexLabel;
	private JRadioButton maleBtn;
	private JRadioButton femaleBtn;
	
	private JLabel insuranceLabel;
	private JComboBox<String> insuranceBox;
	
	private JButton saveBtn;
	private JButton cancelBtn;

	/**
	 * Constructor
	 */
	public AddingPatientFrame(){
		
		// default look
		this.setSize(new Dimension(500, 250));
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		this.setTitle("Dodawanie pacjenta");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(350, 150);
		
		// components
		nameLabel = new JLabel("Imiê:");
		nameTextField = new JTextField();
		
		surnameLabel = new JLabel("Nazwisko:");
		surnameTextField = new JTextField();
		
		peselLabel = new JLabel("PESEL:");
		peselTextField = new JTextField();
		
		sexLabel = new JLabel("P³eæ:");
		femaleBtn = new JRadioButton("Kobieta");
		maleBtn = new JRadioButton("Mê¿czyzna");
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(femaleBtn);
		btnGroup.add(maleBtn);
		
		insuranceLabel = new JLabel("Ubezpieczenie");
		insuranceBox = new JComboBox<String>();
		insuranceBox.addItem("NFZ");
		insuranceBox.addItem("Prywatne");
		insuranceBox.addItem("Brak");
		
		saveBtn = new JButton("Zapisz");
		cancelBtn = new JButton("Anuluj");
		
		//margins
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		
		//CONFIGURE GROUPS
		//horizontal groups
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(nameLabel)
						.addComponent(surnameLabel)
						.addComponent(peselLabel)
						.addComponent(sexLabel)
						.addComponent(insuranceLabel)
						.addGroup(layout.createSequentialGroup()
								.addComponent(saveBtn)
								.addComponent(cancelBtn)))
												
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
						GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)

				.addGroup(layout.createParallelGroup( GroupLayout.Alignment.LEADING)
						.addComponent(nameTextField)
						.addComponent(surnameTextField)
						.addComponent(peselTextField)
						.addGroup(layout.createSequentialGroup()
								.addComponent(femaleBtn)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
										GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)		
								.addComponent(maleBtn))
								.addComponent(insuranceBox)));
																	
				
		//vertical groups
		layout.setVerticalGroup( layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(nameLabel)
						.addComponent(nameTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(surnameLabel)
						.addComponent(surnameTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(peselLabel)
						.addComponent(peselTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(sexLabel)
						.addComponent(femaleBtn)
						.addComponent(maleBtn))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(insuranceLabel)
						.addComponent(insuranceBox))
				.addGap(40)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(saveBtn)
						.addComponent(cancelBtn)));
		
	}
	
	public void setController(Controller c){
		saveBtn.addActionListener(c);
		cancelBtn.addActionListener(c);
	}

	public JButton getSaveBtn() {
		return saveBtn;
	}

	public JButton getCancelBtn() {
		return cancelBtn;
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}

	public JTextField getSurnameTextField() {
		return surnameTextField;
	}

	public JTextField getPeselTextField() {
		return peselTextField;
	}

	public JRadioButton getMaleBtn() {
		return maleBtn;
	}

	public JRadioButton getFemaleBtn() {
		return femaleBtn;
	}

	public JComboBox<String> getInsuranceBox() {
		return insuranceBox;
	}
}
