package Controller;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import Model.DAO;
import Model.Patient;
import Model.Series;
import Model.Test;
import MyUtils.MyUtils;
import View.AddingPatientFrame;
import View.Application;
import View.PatientInfoFrame;
import View.PatientViewPanel;
import View.SearchingPatientFrame;
import View.SourcesFrame;
import View.StartPanel;
import View.TreePopupMenu;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.plugin.DICOM;
import ij.plugin.JpegWriter;
import javafx.print.PrinterJob;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;
/**
 * Controller class.
 * 
 * @author Tomasz Kopacz
 *
 */
public class Controller implements ActionListener, TreeSelectionListener, MouseListener{

	//file storing data for creating tree
	private static String PATIENTS_DATA_FILE = "src\\resources\\patient.txt";
	
	//help text file and sources text file
	private static String HELP_PATH = "src\\resources\\help.txt";
	private static String SOURCES_PATH = "src\\resources\\sources.txt";
	
	private DAO dbmodel;
	private Application app;
	
	private StartPanel mStartPanel = null;
	private PatientViewPanel mPatientViewPanel = null;
	private AddingPatientFrame mAddingPatientFrame = null;
	private SearchingPatientFrame mSearchingPatientFrame = null;
	private PatientInfoFrame mPatientInfoFrame = null;
	
	private JTree tree = null;
	private TreePopupMenu mTreePopupMenu = null;
	
	private Patient currentPatient = null;
	private boolean isPatientEdition = false;
	
	/**
	 * Constructor.
	 * 
	 * @param window
	 */
	public Controller(Application app, DAO dbmodel) {
		
		this.app = app;
		this.dbmodel = dbmodel;
		
		app.setController(this);
		
		//set start look of controlled app
		mStartPanel = new StartPanel();
		app.setActionPanel(mStartPanel);
		mStartPanel.setController(this);
	}

	//ON CLICK LISTENING
	//================================================
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//MAIN APPLICATION FRAME
		//--------------------------------------------------
		
		//gets actual panel shown in App content pane
		Component actionPanel = app.getActionPanel();

		//if actual action panel is START PANEL
		if(actionPanel.getClass().equals(StartPanel.class)){
			
			//if add patient button clicked
			if(e.getSource() == mStartPanel.getAddUserBtn()){
				if (mAddingPatientFrame != null)
					mAddingPatientFrame.dispose();
				mAddingPatientFrame = new AddingPatientFrame();
				mAddingPatientFrame.setController(this);
				mAddingPatientFrame.setVisible(true);
			}
			
			//if search patient button clicked
			if(e.getSource() == mStartPanel.getSearchUserBtn()){
				
				if (mSearchingPatientFrame != null)
					mSearchingPatientFrame.dispose();
				mSearchingPatientFrame = new SearchingPatientFrame();
				mSearchingPatientFrame.setController(this);
				mSearchingPatientFrame.setVisible(true);
			}
		}
		
		//if actual action panel is PATIENT VIEW PANEL
		if(actionPanel.getClass().equals(PatientViewPanel.class)){
			mPatientViewPanel = (PatientViewPanel)actionPanel;
			
			//add new series button clicked
			if(e.getSource() == mPatientViewPanel.getNewSeriesBtn()){
				String input = JOptionPane.showInputDialog(app, "Wprowadz nazwê serii:");
				if(input != null && input != ""){
					
					//check if series name is not occupied
					List<Series> seriesList = new ArrayList<>();
					seriesList = dbmodel.readSeries(currentPatient.getPesel(), "");
					for (Series s : seriesList){
						if(s.getName().equals(input)){
							JOptionPane.showMessageDialog(app, "Nazwa serii jest juz zajeta dla tego pacjenta.");
							return;
						}
					}
					
					//if name is not occupied, create new Series
					Series newSeries = new Series();
					newSeries.setName(input);
					newSeries.setPatientPesel(currentPatient.getPesel());
					
					//add series to database
					dbmodel.createSeries(newSeries);
					
					//actualize data file and tree
					createDataTxtFile(currentPatient);
					mPatientViewPanel.setTreePanel(createTree(currentPatient));
					app.repaint();
					app.revalidate();
				}
			}
			
			//add new test button clicked
			if(e.getSource() == mPatientViewPanel.getNewTestBtn()){
				
				//check if any series is selected to insert new picture into it
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(node != null && node.getLevel() == 1){
			
					//get series id 
					String seriesName = node.toString();
					int seriesID = dbmodel.readSeries(currentPatient.getPesel(), seriesName).get(0).getId();
	
					//choose file to upload
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.showOpenDialog(app);
					String filePath  = fileChooser.getSelectedFile().getAbsolutePath();
						
					//get name of test and add to db
					String input = JOptionPane.showInputDialog(app, "Wprowadz nazwê obrazu:");
					if(input != null && input != ""){
						
						//check if name of test is not occupied
						List<Test> testsList = new ArrayList<>();
						testsList = dbmodel.readTests(seriesID, "");
						for (Test test : testsList){
							if (test.getName().equals(input)){
								JOptionPane.showMessageDialog(app, "Nazwa obrazu jest juz zajeta w tej serii.");
								return;
							}
						}
						
						Test newTest = new Test();
						newTest.setName(input);
						Series series = new Series();
						series.setID(seriesID);
						newTest.setSeries(series);
						newTest.setImage(filePath);
						
						dbmodel.createTest(newTest);
					}
							
					//actualize data file and tree
					createDataTxtFile(currentPatient);
					mPatientViewPanel.setTreePanel(createTree(currentPatient));
					
					//refresh
					app.repaint();
					app.revalidate();
					
				}else{
					JOptionPane.showMessageDialog(app, "Nie wybrano serii.");
				}
			}
			
			//save image as jpg button clicked
			if(e.getSource() == mPatientViewPanel.getToJpgBtn()){
				
				//get selected node
				DefaultMutableTreeNode node = 
						(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				
				//if node is test
				if (node.getLevel() == 2){
					
					//read test from database and put image of test to file
					String testName = node.toString();
					String series = node.getParent().toString();
					int seriesID = dbmodel.readSeries(currentPatient.getPesel(), series).get(0).getId();
					
					//get test, but with no image
					Test testWithNoImage = dbmodel.readTests(seriesID, testName).get(0);
					
					//get test with image
					Test testWithImage = dbmodel.getTestByID(testWithNoImage.getId());
					
					String imagePath = testWithImage.getImagePath();
					File image = new File(imagePath);
					
					//choose directory to save image
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = fileChooser.showSaveDialog(app);
					
					if(result == JFileChooser.APPROVE_OPTION){
						File file  = fileChooser.getSelectedFile();
						String path = file.getAbsolutePath() + "\\" + testName + ".jpg";
						
						try {
							//create DICOM
							InputStream is = new FileInputStream(image);
							DICOM dicom = new DICOM(is);
							dicom.open(testWithImage.getImagePath());
							ImagePlus ip = (ImagePlus)dicom;
							
							//convert to jpg and save image to chosen directory
							JpegWriter.save(ip, path, 100);
							
						} catch (Exception e1) {
						}
						
					}	
				}
			}
			
			//save as dicom button clicked
			if(e.getSource() == mPatientViewPanel.getToDicomBtn()){
				
				//get selected node
				DefaultMutableTreeNode node = 
						(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				
				//if node is test
				if (node.getLevel() == 2){
					
					//read test from database and put image to file
					String testName = node.toString();
					String series = node.getParent().toString();
					int seriesID = dbmodel.readSeries(currentPatient.getPesel(), series).get(0).getId();
					
					//get test, but with no image
					Test testWithNoImage = dbmodel.readTests(seriesID, testName).get(0);
					
					//get test with image
					Test testWithImage = dbmodel.getTestByID(testWithNoImage.getId());
					
					String imagePath = testWithImage.getImagePath();
					File image = new File(imagePath);
					
					//get directory to save
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = fileChooser.showSaveDialog(app);
					
					if(result == JFileChooser.APPROVE_OPTION){
						File directory  = fileChooser.getSelectedFile();
						String path = directory.getAbsolutePath() + "\\" + testName + ".dcm";
						
						//copy from test file (got from db) to chosen file
						InputStream is;
						OutputStream os;
						try {
							is = new FileInputStream(image);
							os = new FileOutputStream(new File(path));
							byte[] buf = new byte[1024];
							int len;
							while ((len = is.read(buf)) > 0){
							    os.write(buf, 0, len);
							}
							is.close();
							os.close();
							
						} catch (Exception e1) {
						}
					}
				}
			}
			
			//dicom info button clicked
			if(e.getSource() == mPatientViewPanel.getTestInfoBtn()){

				//get selected node
				DefaultMutableTreeNode node = 
						((DefaultMutableTreeNode)tree.getLastSelectedPathComponent());
				
				if(node != null && node.getLevel() == 2){
				
					//get test from database
					String testName = node.toString();
					int seriesID = dbmodel
							.readSeries(currentPatient.getPesel(), node.getParent().toString())
							.get(0)
							.getId();
					
					//get test, but with no image
					Test testWithNoImage = dbmodel.readTests(seriesID, testName).get(0);
					
					//get test with image
					Test testWithImage = dbmodel.getTestByID(testWithNoImage.getId());
					
					//get dicom file
					File file = new File(testWithImage.getImagePath());
					InputStream is;
					try {
						is = new FileInputStream(file);
						DICOM dicom = new DICOM(is);
						
						//get image info and put into new SourcesFrame
						String data = dicom.getInfo(testWithImage.getImagePath());
						SourcesFrame sourcesFrame = new SourcesFrame();
						sourcesFrame.getTextArea().setText(data);
						sourcesFrame.setVisible(true);
						
					} catch (FileNotFoundException e1) {
					}
				}
			}
		}
		
		//ADDING PATIENT FRAME
		//---------------------------------------------------------
			
		if(mAddingPatientFrame != null){
			
			//IF SAVE BUTTON CLICKED
			
			//create new patient, add to database and open PatientViewPanel
			if(e.getSource() == mAddingPatientFrame.getSaveBtn()){
				
				//get data from fields
				String pesel = mAddingPatientFrame.getPeselTextField().getText();
				String name = mAddingPatientFrame.getNameTextField().getText();
				String surname = mAddingPatientFrame.getSurnameTextField().getText();
				String sex = "";
				if(mAddingPatientFrame.getMaleBtn().isSelected())
					sex = "Mê¿czyzna";
				else if(mAddingPatientFrame.getFemaleBtn().isSelected())
					sex = "Kobieta";
				String insurance 
					= mAddingPatientFrame.getInsuranceBox().getSelectedItem().toString();
				
				Patient patient = new Patient();
				
				//validate data
				
				//are fields empty
				if(	name.isEmpty()
					|| surname.isEmpty()
					|| pesel.isEmpty()
					|| sex.isEmpty()){
						JOptionPane.showMessageDialog(app, "Wype³nij wszystkie pola!");
						return ;
				}
				
				//is input incorrect
				else if(MyUtils.areNumbers(name)
					|| MyUtils.areNumbers(surname)
					|| MyUtils.areLetters(pesel)){
						JOptionPane.showMessageDialog(app, "Nieprawid³owe dane.");
						return;
				}
				
				//has pesel correct length
				else if((pesel.length()) != 11){
					JOptionPane.showMessageDialog(app, "Sprawdz d³ugoœæ peselu.");
					return;
				}
				
				//create Patient object
				else{
					patient.setPesel(pesel);
					patient.setName(name);
					patient.setSurname(surname);
					patient.setSex(sex);
					patient.setInsurance(insurance);
					
					//is patient data edited - if true, involve update method in db
					if(isPatientEdition){
						
						dbmodel.updatePatient(currentPatient.getPesel(), patient);
						isPatientEdition = false;
					}
					
					//if new patient is added, check if typed pesel is occupied
					//if pesel is not occupied, involve create patient method in db
					else{
						if(!dbmodel.readPatients(pesel, "", "").isEmpty()){
							JOptionPane.showMessageDialog(app, "Pacjent o podanym peselu ju¿ istnieje!");
							return;
						}
						
						else{
							dbmodel.createPatient(patient);
						}	
					}
					
					//actualize current patient
					currentPatient = new Patient();
					currentPatient = patient;
					
					//close AddingPatientFrame, open PatientViewPanel
					mAddingPatientFrame.dispose();
					mAddingPatientFrame = null;
					
					//actualize file
					createDataTxtFile(patient);
					
					//create tree
					JScrollPane treePane 
					= createTree(patient);
					
					//if PatientViewPanel has been opened, simply actualize the tree
					if(actionPanel.getClass().equals(PatientViewPanel.class)){
						mPatientViewPanel.setTreePanel(treePane);
						mPatientViewPanel.getImagePanel().removeAll();
					}
					
					//or create new PatientPanelView if it hasn't been existing
					else{
						mPatientViewPanel = new PatientViewPanel();
						mPatientViewPanel.setController(this);
						mPatientViewPanel.setTreePanel(treePane);
						app.setActionPanel(mPatientViewPanel);
						mPatientViewPanel.setController(this);
					}
					
					//refresh
					app.repaint();
					app.revalidate();
				}
			}
			
			//IF CANCEL BUTTON CLICKED
			else if(e.getSource() == mAddingPatientFrame.getCancelBtn()){
				mAddingPatientFrame.dispose();
				mAddingPatientFrame = null;
			}
		}
		
		//SEARCHING PATIENT FRAME
		//---------------------------------------------------------
		
		if(mSearchingPatientFrame != null){
		
			//IF SEARCH BUTTON CLICKED
			if(e.getSource() == mSearchingPatientFrame.getSearchBtn()){
				
				//get input from text fields
				String pesel = mSearchingPatientFrame.getPeselTextField().getText();
				String name = mSearchingPatientFrame.getNameTextField().getText();
				String surname = mSearchingPatientFrame.getSurnameTextField().getText();
				
				//search in database
				List<Patient> patients = dbmodel.readPatients(pesel, name, surname);
				
				DefaultTableModel model 
					= (DefaultTableModel)mSearchingPatientFrame.getTableModel();
				
				//clear table
				int rowsNum = model.getRowCount();
				for(int i = rowsNum - 1; i >= 0; i--){
					model.removeRow(i);
				}
				
				//show found patients in table
				for(Patient p : patients ){
					String[] row = new String[]{p.getName(), p.getSurname(), p.getPesel()};
					model.addRow(row);
				}
			}
			
			//IF CANCEL BUTTON CLICKED
			else if(e.getSource() == mSearchingPatientFrame.getCancelBtn()){
				mSearchingPatientFrame.dispose();
				mSearchingPatientFrame = null;
			}
			
			//IF CHOOSE BUTTON CLICKED
			else if(e.getSource() == mSearchingPatientFrame.getChooseBtn()){
				
				//get selected patient from db
				int selectedRowIndex = mSearchingPatientFrame.getTable().getSelectedRow();
				if(selectedRowIndex != -1){
					String pesel = mSearchingPatientFrame.getTable()
							.getValueAt(selectedRowIndex, 2)
							.toString();
					Patient p = dbmodel.readPatients(pesel, "", "").get(0);
					
					//close SearchingPanelFrame
					mSearchingPatientFrame.dispose();
					mSearchingPatientFrame = null;
					
					//update data text file
					createDataTxtFile(p);
					
					//create tree
					JScrollPane treePane = createTree(p);
					
					//if PatientViewPanel has been opened simply actualize it with new tree
					if(actionPanel.getClass().equals(PatientViewPanel.class)){
						mPatientViewPanel.setTreePanel(treePane);
						mPatientViewPanel.getImagePanel().removeAll();
					}
					
					//or create new PatientPanelView if it hasn't been existing
					else{
						mPatientViewPanel = new PatientViewPanel();
						mPatientViewPanel.setController(this);
						mPatientViewPanel.setTreePanel(treePane);
						app.setActionPanel(mPatientViewPanel);
					}
					
					//update currentPatient
					currentPatient = new Patient();
					currentPatient = p;
					
					//refresh
					app.repaint();
					app.revalidate();
				}
			}
			
			//IF REMOVE BUTTON CLICKED
			else if(e.getSource() == mSearchingPatientFrame.getRemoveBtn()){
				
				//get selected patient from db
				int selectedRowIndex = mSearchingPatientFrame.getTable().getSelectedRow();
				if(selectedRowIndex != -1){
					String pesel = mSearchingPatientFrame.getTable()
							.getValueAt(selectedRowIndex, 2)
							.toString();
					
					//remove from db and from table
					dbmodel.deletePatient(pesel);
					mSearchingPatientFrame.getTableModel().removeRow(selectedRowIndex);
				}
			}
		}
		
		//TREE POPUP MENU ITEMS
		//----------------------------------------------------------------
		
		if(mTreePopupMenu != null){
			
			//SHOW PATIENTS DATA
			if(e.getSource() == mTreePopupMenu.getShowPatientDataItem()){
				
				//find patient in database
				Patient p = dbmodel.readPatientByID(currentPatient.getPesel());
				
				//create frame with info
				mPatientInfoFrame = new PatientInfoFrame();
				mPatientInfoFrame.setPatientsData(p);
				mPatientInfoFrame.setVisible(true);
			}
			
			//EDIT PATIENTS DATA
			if(e.getSource() == mTreePopupMenu.getEditPatientDataItem()){
				isPatientEdition = true;
				
				//for editing patient data we use the same frame as to add patient
				if(mAddingPatientFrame != null)
					mAddingPatientFrame.dispose();
				
				mAddingPatientFrame = new AddingPatientFrame();
				mAddingPatientFrame.getNameTextField().setText(currentPatient.getName());
				mAddingPatientFrame.getSurnameTextField().setText(currentPatient.getSurname());
				mAddingPatientFrame.getPeselTextField().setText(currentPatient.getPesel());
				mAddingPatientFrame.getPeselTextField().setEnabled(false);
				if(currentPatient.getSex().equals("Kobieta"))
					mAddingPatientFrame.getFemaleBtn().setSelected(true);
				else
					mAddingPatientFrame.getMaleBtn().setSelected(true);
				mAddingPatientFrame.getInsuranceBox().setSelectedItem(currentPatient.getInsurance());
				
				mAddingPatientFrame.setVisible(true);
				mAddingPatientFrame.setController(this);
			}
			
			//REMOVE PATIENT
			if(e.getSource() == mTreePopupMenu.getRemovePatientItem()){
				
				//delete patient
				dbmodel.deletePatient(currentPatient.getPesel());
				currentPatient = null;
				mPatientViewPanel = null;
				
				//set Start Panel to app main panel
				mStartPanel = new StartPanel();
				mStartPanel.setController(this);
				app.setActionPanel(mStartPanel);
			}
			
			//EDIT SERIES NAME
			if(e.getSource() == mTreePopupMenu.getRenameSeriesItem()){
				
				//open dialog to get new name
				String input = JOptionPane.showInputDialog(app, "Wprowadz nazwê serii:");
				if(input != null && input != ""){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					int seriesId = dbmodel.readSeries(currentPatient.getPesel(), node.toString()).get(0).getId();
					
					Series newSeries = new Series();
					newSeries.setName(input);
					newSeries.setPatientPesel(currentPatient.getPesel());
					
					dbmodel.updateSeries(seriesId, newSeries);
					
					//actualize file
					createDataTxtFile(currentPatient);
					
					//create tree
					JScrollPane treePane 
					= createTree(currentPatient);
					mPatientViewPanel.setTreePanel(treePane);
					
					//refresh
					app.repaint();
					app.revalidate();
				}
			}
			
			//REMOVE SERIES
			if(e.getSource() == mTreePopupMenu.getRemoveSeriesItem()){
				String seriesName = 
						((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).toString();
				int seriesID = dbmodel.readSeries(currentPatient.getPesel(), seriesName).get(0).getId();
				
				//delete series
				dbmodel.deleteSeries(seriesID);
				
				//actualize data file and tree
				createDataTxtFile(currentPatient);
				mPatientViewPanel.setTreePanel(createTree(currentPatient));
				
				//refresh
				app.repaint();
				app.revalidate();
			}
			
			//EDIT TEST NAME
			if (e.getSource() == mTreePopupMenu.getRenameTestItem()){
				
				//open dialog to get new name
				String input = JOptionPane.showInputDialog(app, "Wprowadz nazwê obrazu:");
				if(input != null && input != ""){
					DefaultMutableTreeNode testNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					DefaultMutableTreeNode seriesNode = (DefaultMutableTreeNode) testNode.getParent();
					int seriesId = dbmodel.readSeries(currentPatient.getPesel(), seriesNode.toString()).get(0).getId();
					String actualTestName = testNode.toString();
					
					int testId = dbmodel.readTests(seriesId, actualTestName).get(0).getId();
					
					dbmodel.updateTest(testId, input);
					
					//actualize file
					createDataTxtFile(currentPatient);
					
					//create tree
					JScrollPane treePane 
					= createTree(currentPatient);
					mPatientViewPanel.setTreePanel(treePane);
					
					//refresh
					app.repaint();
					app.revalidate();
				}
			}
			
			//REMOVE TEST
			if(e.getSource() == mTreePopupMenu.getRemoveTestItem()){
				
				//get selected node
				DefaultMutableTreeNode testNode = 
						((DefaultMutableTreeNode)tree.getLastSelectedPathComponent());
				
				//get series id and test name
				String seriesName = testNode.getParent().toString();
				int seriesID = dbmodel.readSeries(currentPatient.getPesel(), seriesName).get(0).getId();
				String testName = testNode.toString();
				
				//remove test
				Test testToRemove = dbmodel.readTests(seriesID, testName).get(0);
				dbmodel.deleteTest(testToRemove.getId());
				
				//actualize data file and tree
				createDataTxtFile(currentPatient);
				mPatientViewPanel.setTreePanel(createTree(currentPatient));
				
				//remove image from PatientViewPanel
				mPatientViewPanel.getImagePanel().removeAll();
				
				//refresh
				app.repaint();
				app.revalidate();
			}
		}
		
		
		//MENU BAR ITEMS
		//--------------------------------------------------------------
		
		//item add patient clicked
		if(e.getSource() == app.getAddPatientItem()){
			if (mAddingPatientFrame != null)
				mAddingPatientFrame.dispose();
			mAddingPatientFrame = new AddingPatientFrame();
			mAddingPatientFrame.setController(this);
			mAddingPatientFrame.setVisible(true);
		}
		
		//item search patient clicked
		if(e.getSource() == app.getSearchUserItem()){
			if (mSearchingPatientFrame != null)
				mSearchingPatientFrame.dispose();
			mSearchingPatientFrame = new SearchingPatientFrame();
			mSearchingPatientFrame.setController(this);
			mSearchingPatientFrame.setVisible(true);
		}
		
		//item close clicked
		if(e.getSource() == app.getCloseItem()){
			app.dispose();
		}
		
		//item help clicked
		if(e.getSource() == app.getHelpItem()){
			SourcesFrame sourcesFrame = new SourcesFrame();
			sourcesFrame.readText(HELP_PATH);
			sourcesFrame.setVisible(true);
		}
		
		//item sources clicked
		if(e.getSource() == app.getSourcesItem()){
			SourcesFrame sourcesFrame = new SourcesFrame();
			sourcesFrame.readText(SOURCES_PATH);
			sourcesFrame.setVisible(true);
		}
	}
	
	
	// MOUSE ACTION LISTENING
	//===============================================
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//right click on patient tree
		if(SwingUtilities.isRightMouseButton(e)){
			if(e.getSource() == tree){
				
				//select closest node
				int row = tree.getClosestRowForLocation(e.getX(), e.getY());
				tree.setSelectionRow(row);
			
				//show popup menu 
				mTreePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		//show tool tip text 
		if(e.getSource() == mPatientViewPanel.getNewSeriesBtn()){
			mPatientViewPanel.getNewSeriesBtn().setToolTipText("Utwórz now¹ seriê badañ");
		}
		
		if(e.getSource() == mPatientViewPanel.getNewTestBtn()){
			mPatientViewPanel.getNewTestBtn().setToolTipText("Dodaj nowy obraz badania");
		}
		
		if(e.getSource() == mPatientViewPanel.getToJpgBtn()){
			mPatientViewPanel.getToJpgBtn().setToolTipText("Zapisz obraz w formacie jpg");
		}
		
		if(e.getSource() == mPatientViewPanel.getToDicomBtn()){
			mPatientViewPanel.getToDicomBtn().setToolTipText("Zapisz obraz w formacie dicom");
		}
		
		if(e.getSource() == mPatientViewPanel.getTestInfoBtn()){
			mPatientViewPanel.getTestInfoBtn().setToolTipText("Poka¿ szczegó³owe dane obrazu");
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	
	//TREE SELECTION LISTENING
	//=============================================
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		//get selected node
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		
		if(node.getLevel() == 0){
			
			//create pupup menu (but do not open)
			mTreePopupMenu = new TreePopupMenu(TreePopupMenu.PATIENT_NODE);
			mTreePopupMenu.setController(this);
		}
		
		else if(node.getLevel() == 1){
			
			//create popup menu (but do not open)
			mTreePopupMenu = new TreePopupMenu(TreePopupMenu.SERIES_NODE);
			mTreePopupMenu.setController(this);
		}

		else if(node.getLevel() == 2){
			
			//create pupup menu (but do not open)
			mTreePopupMenu = new TreePopupMenu(TreePopupMenu.TEST_NODE);
			mTreePopupMenu.setController(this);
			
			//get name and series of selected test node, then find that test in database
			//if test is found, new dicom image file is created
			String testName = node.toString();
			int seriesID = dbmodel.readSeries(currentPatient.getPesel(), node.getParent().toString()).get(0).getId();
			
			//get test, but with no image
			Test testWithNoImage = dbmodel.readTests(seriesID, testName).get(0);
			
			//get test with image
			Test testWithImage = dbmodel.getTestByID(testWithNoImage.getId());
			
			//get dicom file
			File file = new File(testWithImage.getImagePath());
			InputStream is;
			try {
				//get dicom and open in new PatientViewPanel
				is = new FileInputStream(file);
				DICOM dicom = new DICOM(is);
				dicom.open(testWithImage.getImagePath());
				ImagePlus ip = (ImagePlus)dicom;
				ImageCanvas ic = new ImageCanvas(ip);
				JPanel panel = new JPanel();
				panel.add(ic);
				mPatientViewPanel.setImagePanel(panel);
				
			} catch (FileNotFoundException e1) {
			}
			
			//refresh
			app.repaint();
			app.revalidate();
		}
	}	
	
	// OTHER METHODS
	//===========================================
	
	/**
	 * Reads patient series and tests from database
	 * and saves result in file.
	 * 
	 * @param p
	 */
	private void createDataTxtFile(Patient p){
		//read all user series from db
		List<Series> seriesList = dbmodel.readSeries(p.getPesel(), "");
		List<Test> testsList = new  ArrayList<Test>();
				
		//write file
		PrintWriter pw = null;
		try{
			//create new file writer
			File file = new File(PATIENTS_DATA_FILE);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
					
			//for each series make sequence "S nameofseries"
			for(Series s : seriesList){
				pw.println("S " + s.getName());
				
				//for each test make sequence "T nameoftest"
				testsList = dbmodel.readTests(s.getId(), "");
				for(Test t : testsList)
					pw.println("T " + t.getName());
			}	
			
		}catch (Exception e) {
		}finally{
			if(pw != null)
			pw.close();
		}
	}
	
	/**
	 * Creates tree due to the information in file.
	 * 
	 * @param p - Patient
	 * @return JScrollPane
	 */
	private JScrollPane createTree(Patient p){

		//tree nodes
		DefaultMutableTreeNode patient = 
				new DefaultMutableTreeNode(p.getName() + " " + p.getSurname());
		DefaultMutableTreeNode series = null;
		DefaultMutableTreeNode tests = null;
		
		//read data from file
		BufferedReader br = null;
		
		try{
			//make new reader
			FileReader fr = new FileReader(PATIENTS_DATA_FILE);
			br = new BufferedReader(fr);
			
			//read line, save as nodes
			String line  = br.readLine();
			while(line != null){
				if(!line.equals("")){
					
					//get first letter of line
					char type = line.charAt(0);
		           
					switch(type){
		           	
		           		//S means Series - make series node
		           		case 'S':
		           			series = new DefaultMutableTreeNode(line.substring(2));
		           			patient.add(series);
		           			break;
		           			
		           		//T means Test - make test node
		           		case 'T':
		           			if(series != null){
		           				tests = new DefaultMutableTreeNode(line.substring(2));
		           				series.add(tests);
		           			}
		           			break;
		           		default:
		           			break;	
		           	}
        	   }
        	   
			   line = br.readLine();
        	   }
			br.close();
			
		}catch (Exception e) {
		}
		
		//create tree
		tree = new JTree(patient){
			public Insets getInsets(){
				return new Insets(5,5,5,5);
			}
		};
		
		//events listening
		tree.getSelectionModel().addTreeSelectionListener(this);
		tree.addMouseListener(this);
		
		return new JScrollPane(tree);
	}
}
