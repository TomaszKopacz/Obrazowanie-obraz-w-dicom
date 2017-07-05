package View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sun.glass.events.KeyEvent;

import Controller.Controller;

/**
 * Welcoming panel, containing two default buttons: add patient and search patient.
 * 
 * @author Aleksandra Zajac
 *
 */
public class StartPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static String ADD_USER_ICON = "src\\resources\\ic\\add_user.png";
	private static String SEARCH_USER_ICON = "src\\resources\\ic\\find_user.png";
	
	private JButton addUserBtn;
	private JButton searchUserBtn;
	
	/**
	 * Constructor.
	 */
	public StartPanel(){
		
		// default look
		this.setPreferredSize(new Dimension(600, 440));
		this.setLayout(new GridBagLayout());
		
		// buttons
		addUserBtn = new JButton();
		searchUserBtn = new JButton();
		
		BufferedImage addUserIcon = null;
		BufferedImage searchUserIcon = null;

		try{
			addUserIcon = ImageIO.read(new File(ADD_USER_ICON));
			searchUserIcon = ImageIO.read(new File(SEARCH_USER_ICON));
			
			//customize addUser button
			addUserBtn.setIcon(new ImageIcon(addUserIcon));
			addUserBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			addUserBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			addUserBtn.setText("Dodaj pacjenta");
			addUserBtn.setBackground(java.awt.Color.WHITE);
			
			//customize searchUser button
			searchUserBtn.setIcon(new ImageIcon(searchUserIcon));
			searchUserBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			searchUserBtn.setHorizontalTextPosition(SwingConstants.CENTER);
			searchUserBtn.setText("Wyszukaj pacjenta");
			searchUserBtn.setBackground(java.awt.Color.WHITE);
			
		}catch (Exception e) {

		}
		
		this.add(addUserBtn);
		this.add(Box.createRigidArea(new Dimension(30,30)));
		this.add(searchUserBtn);
		
	}

	/**
	 * Sets Controller for this panel.
	 * 
	 * @param c
	 */
	public void setController(Controller c){
		addUserBtn.addActionListener(c);
		searchUserBtn.addActionListener(c);
	}
	
	public JButton getAddUserBtn() {
		return addUserBtn;
	}

	public JButton getSearchUserBtn() {
		return searchUserBtn;
	}

}
