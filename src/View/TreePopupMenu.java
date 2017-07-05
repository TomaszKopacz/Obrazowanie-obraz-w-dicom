package View;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import Controller.Controller;

/**
 * @author Tomasz Kopacz
 *
 */
public class TreePopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = 1L;
	
	public static String PATIENT_NODE = "patient_node";
	public static String SERIES_NODE = "series_node";
	public static String TEST_NODE = "test_node";
	
	private JMenuItem showPatientDataItem = new JMenuItem("Poka¿ dane pacjenta");
	private JMenuItem editPatientDataItem = new JMenuItem("Edytuj dane pacjenta");
	private JMenuItem removePatientItem = new JMenuItem("Usuñ pacjenta z bazy");
	
	private JMenuItem renameSeriesItem = new JMenuItem("Zmieñ nazwê");
	private JMenuItem removeSeriesItem = new JMenuItem("Usuñ seriê z bazy");
	
	private JMenuItem renameTestItem = new JMenuItem("Zmieñ nazwê");
	private JMenuItem removeTestItem = new JMenuItem("Usuñ obraz z bazy");

	
	public TreePopupMenu(String node){
		
		if(node.equals(PATIENT_NODE)){
			add(showPatientDataItem);
			add(editPatientDataItem);
			add(removePatientItem);
		}
		
		else if(node.equals(SERIES_NODE)){
			add(renameSeriesItem);
			add(removeSeriesItem);
		}
		
		else if (node.equals(TEST_NODE)) {
			add(renameTestItem);
			add(removeTestItem);
		}
	}
	
	public void setController(Controller c){
		showPatientDataItem.addActionListener(c);
		editPatientDataItem.addActionListener(c);
		removePatientItem.addActionListener(c);
		
		renameSeriesItem.addActionListener(c);
		removeSeriesItem.addActionListener(c);
		
		renameTestItem.addActionListener(c);
		removeTestItem.addActionListener(c);
	}


	public JMenuItem getShowPatientDataItem() {
		return showPatientDataItem;
	}


	public JMenuItem getEditPatientDataItem() {
		return editPatientDataItem;
	}


	public JMenuItem getRemovePatientItem() {
		return removePatientItem;
	}

	public JMenuItem getRenameSeriesItem() {
		return renameSeriesItem;
	}


	public JMenuItem getRemoveSeriesItem() {
		return removeSeriesItem;
	}


	public JMenuItem getRenameTestItem() {
		return renameTestItem;
	}

	public JMenuItem getRemoveTestItem() {
		return removeTestItem;
	}
}
