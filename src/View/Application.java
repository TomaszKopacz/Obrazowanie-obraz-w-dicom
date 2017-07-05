package View;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import Controller.Controller;

/**
 * Application main window. 
 * There are provided advanced options like adding new test or edit data in menu bar .
 * 
 * @author Aleksandra Zajac
 *
 */
public class Application extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JMenuBar mMenuBar;
	
	private JMenu fileMenu;
	private JMenuItem addUserItem;
	private JMenuItem searchUserItem;
	private JMenuItem closeItem;
	private JMenu helpMenu;
	private JMenuItem helpItem;
	private JMenuItem sourcesItem;
	
	/**
	 * Constructor.
	 */
	public Application(){
		
		// default look
		this.setTitle("Obrazowanie badaÒ");
		this.setPreferredSize(new Dimension(600, 600));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(300, 100);
		
		// MENU
		mMenuBar = new JMenuBar();
		
		fileMenu = new JMenu("Plik");
		addUserItem = new JMenuItem("Dodaj pacjenta");
		searchUserItem = new JMenuItem("Wyszukaj pacjenta");
		closeItem = new JMenuItem("Zamknij");
		fileMenu.add(addUserItem);
		fileMenu.add(searchUserItem);
		fileMenu.add(closeItem);
		
		helpMenu = new JMenu("Pomoc");
		helpItem = new JMenuItem("Pomoc");
		sourcesItem = new JMenuItem("èrÛd≥a");
		helpMenu.add(helpItem);
		helpMenu.add(sourcesItem);
		
		this.setJMenuBar(mMenuBar);
		mMenuBar.add(fileMenu);
		mMenuBar.add(helpMenu);
	
		this.pack();
	}
	
	/**
	 * Controller for menu bar items and default action panel.
	 * 
	 * @param c
	 */
	public void setController(Controller c){
		addUserItem.addActionListener(c);
		searchUserItem.addActionListener(c);
		closeItem.addActionListener(c);
		helpItem.addActionListener(c);
		sourcesItem.addActionListener(c);
	}
	
	/**
	 * Sets JPanel panel to the action panel of the Window.
	 * Action panel can only hold one panel inside.
	 * 
	 * @param panel
	 */
	public void setActionPanel(JPanel panel){
		getContentPane().removeAll();
		getContentPane().add(panel);
		
		this.repaint();
		this.revalidate();
	}

	public Component getActionPanel() {
		if(this.getContentPane().getComponentCount() == 1)
			return this.getContentPane().getComponent(0);
		return null;
	}

	public JMenuItem getAddPatientItem() {
		return addUserItem;
	}

	public JMenuItem getSearchUserItem() {
		return searchUserItem;
	}

	public JMenuItem getCloseItem() {
		return closeItem;
	}

	public JMenuItem getHelpItem() {
		return helpItem;
	}

	public JMenuItem getSourcesItem() {
		return sourcesItem;
	}
}
