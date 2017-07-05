package View;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import Model.Patient;

/**
 * Frame showing Patient data.
 * 
 * @author Tomasz Kopacz
 *
 */
public class PatientInfoFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JLabel nameLabel;
	private JLabel name;
	
	private JLabel surnameLabel;
	private JLabel surname;
	
	private JLabel peselLabel;
	private JLabel pesel;
	
	private JLabel sexLabel;
	private JLabel sex;
	
	private JLabel insuranceLabel;
	private JLabel insurance;
	
	public PatientInfoFrame(){
		
		//default look
		this.setSize(new Dimension(250, 250));
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(350, 150);
		
		//components
		nameLabel = new JLabel("Imiê:");
		surnameLabel = new JLabel("Nazwisko:");
		peselLabel = new JLabel("PESEL:");
		sexLabel = new JLabel("P³eæ:");
		insuranceLabel = new JLabel("Ubezpieczenie:");
		
		name = new JLabel("");
		surname = new JLabel("");
		pesel = new JLabel("");
		sex = new JLabel("");
		insurance = new JLabel("");
		
		//margins
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		//configure groups
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(nameLabel)
						.addComponent(surnameLabel)
						.addComponent(peselLabel)
						.addComponent(sexLabel)
						.addComponent(insuranceLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(name)
						.addComponent(surname)
						.addComponent(pesel)
						.addComponent(sex)
						.addComponent(insurance)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(nameLabel)
						.addComponent(name))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(surnameLabel)
						.addComponent(surname))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(peselLabel)
						.addComponent(pesel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(sexLabel)
						.addComponent(sex))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(insuranceLabel)
						.addComponent(insurance)));
	}
	
	/**
	 * Gets Patients attributes and show them as labels.
	 * 
	 * @param p - Patient
	 */
	public void setPatientsData(Patient p){
		this.setTitle(p.getName() + " " + p.getSurname());
		
		this.name.setText(p.getName());
		this.surname.setText(p.getSurname());
		this.pesel.setText(p.getPesel());
		this.sex.setText(p.getSex());
		this.insurance.setText(p.getInsurance());
		
		this.repaint();
		this.revalidate();
	}

}
