package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import Controller.Controller;

/** 
 * Panel holding list of all series and tests of patient in the list
 * and displays selected image. 
 * 
 * @author Uzytkownik
 *
 */
public class PatientViewPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static String NEW_SERIES_BTN_ICON_PATH = "src\\resources\\ic\\series.png";
	private static String NEW_TEST_BTN_ICON_PATH = "src\\resources\\ic\\test.png";
	private static String TO_JPG_BTN_ICON_PATH = "src\\resources\\ic\\jpg.png";
	private static String TO_DICOM_BTN_ICON_PATH = "src\\resources\\ic\\dcm.png";
	private static String TEST_INFO_BTN_ICON_PATH = "src\\resources\\ic\\info.png";
	
	private JToolBar mToolBar;
	private JButton newSeriesBtn;
	private JButton newTestBtn;
	private JButton toJpgBtn;
	private JButton toDicomBtn;
	private JButton testInfoBtn;
	
	private JPanel treePanel;
	private JPanel imagePanel;
	
	/**
	 * Constructor.
	 */
	public PatientViewPanel(){
		
		//default look
		this.setPreferredSize(new Dimension(300, 300));
		this.setLayout(new BorderLayout());
		
		//TOOLBAR
		mToolBar = new JToolBar("Dodawanie badañ");
		newSeriesBtn = new JButton();
		newTestBtn = new JButton();
		toJpgBtn = new JButton();
		toDicomBtn = new JButton();
		testInfoBtn = new JButton();
		
		BufferedImage newSeriesIcon = null;
		BufferedImage newTestIcon = null;
		BufferedImage toJpgIcon = null;
		BufferedImage toDicomIcon = null;
		BufferedImage testInfoIcon = null;
		
		try{
			newSeriesIcon = ImageIO.read(new File(NEW_SERIES_BTN_ICON_PATH));
			newTestIcon = ImageIO.read(new File(NEW_TEST_BTN_ICON_PATH));
			toJpgIcon = ImageIO.read(new File(TO_JPG_BTN_ICON_PATH));
			toDicomIcon = ImageIO.read(new File(TO_DICOM_BTN_ICON_PATH));
			testInfoIcon = ImageIO.read(new File(TEST_INFO_BTN_ICON_PATH));
			
			newSeriesBtn.setIcon(new ImageIcon(newSeriesIcon));
			newTestBtn.setIcon(new ImageIcon(newTestIcon));
			toJpgBtn.setIcon(new ImageIcon(toJpgIcon));
			toDicomBtn.setIcon(new ImageIcon(toDicomIcon));
			testInfoBtn.setIcon(new ImageIcon(testInfoIcon));
			
		}catch (Exception e) {
		}
		
		mToolBar.add(newSeriesBtn);
		mToolBar.addSeparator();
		mToolBar.add(newTestBtn);
		mToolBar.addSeparator();
		mToolBar.addSeparator();
		mToolBar.add(toJpgBtn);
		mToolBar.addSeparator();
		mToolBar.add(toDicomBtn);
		mToolBar.addSeparator();
		mToolBar.add(testInfoBtn);
		this.add(mToolBar, BorderLayout.PAGE_START);
		
		//PANELS
		treePanel = new JPanel();
		imagePanel = new JPanel();
		imagePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		imagePanel.setLayout(new GridBagLayout());

		this.add(treePanel, BorderLayout.WEST);
		this.add(imagePanel, BorderLayout.CENTER);
	}
	
	/**
	 * Controller.
	 * 
	 * @param c
	 */
	public void setController(Controller c){
		newSeriesBtn.addActionListener(c);
		newTestBtn.addActionListener(c);
		toJpgBtn.addActionListener(c);
		toDicomBtn.addActionListener(c);
		testInfoBtn.addActionListener(c);
		
		newSeriesBtn.addMouseListener(c);
		newTestBtn.addMouseListener(c);
		toJpgBtn.addMouseListener(c);
		toDicomBtn.addMouseListener(c);
		testInfoBtn.addMouseListener(c);
	}
	
	public void setTreePanel(JScrollPane sp){
		treePanel.removeAll();
		treePanel.add(sp);
	}
	
	public void setImagePanel(JComponent component){
		imagePanel.removeAll();
		imagePanel.add(component);
	}

	public JPanel getTreePanel() {
		return treePanel;
	}

	public JPanel getImagePanel() {
		return imagePanel;
	}

	public JButton getNewSeriesBtn() {
		return newSeriesBtn;
	}

	public JButton getNewTestBtn() {
		return newTestBtn;
	}
	
	public JButton getToJpgBtn() {
		return toJpgBtn;
	}

	public JButton getToDicomBtn() {
		return toDicomBtn;
	}

	public JButton getTestInfoBtn() {
		return testInfoBtn;
	}
}
