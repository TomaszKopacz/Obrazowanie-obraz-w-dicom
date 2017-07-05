package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksandra Zajac
 * 
 * Data Access class, making CRUD queries.
 *
 */
public class DAO {
	
	private static String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String DATABASE = "jdbc:derby:PatientsDB";
	
	private Connection connection = null;
	
	/**
	 * Constructor.
	 */
	public DAO(){
		

		try {
			Class.forName(DERBY_EMBEDDED_DRIVER);
		} catch (ClassNotFoundException e) {
		}

	}
	
	
	// PATIENTS TABLE
	//==========================================================
	
	/**
	 * Inserts new Patient into database.
	 * @param patient
	 */
	public void createPatient(Patient patient){
		
		String pesel = patient.getPesel();
		String name = patient.getName();
		String surname = patient.getSurname();
		String sex = patient.getSex();
		String insurance = patient.getInsurance();
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){ 
				
				String sql = "INSERT INTO App.Patients "
						+ "VALUES(?, ?, ?, ?, ?)";
				
				PreparedStatement statement;
				statement = connection.prepareStatement(sql);
				statement.setString(1, pesel);
				statement.setString(2, name);
				statement.setString(3, surname);
				statement.setString(4, sex);
				statement.setString(5, insurance);
					
				statement.executeUpdate();	
		}
		} catch (SQLException e1) {
		}
	}
	
	/**
	 * Updates data of patient. Pesel cannot be changed.
	 * 
	 * @param id
	 * @param p
	 */
	public void updatePatient(String id, Patient p){
		String name = p.getName();
		String surname = p.getSurname();
		String sex = p.getSex();
		String insurance = p.getInsurance();
		
		try{
		connection = DriverManager.getConnection(DATABASE);
		
			if(connection != null){
				
				String sql = "UPDATE App.Patients "
						+ "SET name = ?, "
						+ "surname = ?, "
						+ "sex = ?, "
						+ "insurance = ? "
						+ "WHERE pesel LIKE ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, name);
				statement.setString(2, surname);
				statement.setString(3, sex);
				statement.setString(4, insurance);
				statement.setString(5, id);
				
				statement.executeUpdate();
			}
		
		}catch (Exception e) {
		}
	}
	
	/**
	 * Searches for patients in database. 
	 * Method returns each Patient that meets the requirements 
	 * of containing substrings given with the parameters (pesel, name and surname).
	 * 
	 * @param subpesel
	 * @param subname
	 * @param subsurname
	 * @return List<Patient>
	 */
	public List<Patient> readPatients(
			String subpesel, 
			String subname, 
			String subsurname){
		
		List<Patient> patients = new ArrayList<>();
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){
			
				String sql = "SELECT * FROM APP.PATIENTS "
						+ "WHERE pesel LIKE ? AND "
						+ "name LIKE ? AND "
						+ "surname LIKE ?";
					
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, "%"+subpesel+"%");
				statement.setString(2, "%"+subname+"%");
				statement.setString(3, "%"+subsurname+"%");
					
				ResultSet set = statement.executeQuery();
					
				while(set.next()){
					String pesel = set.getString("pesel");
					String name = set.getString("name");
					String surname = set.getString("surname");
					String sex = set.getString("sex");
					String insurance = set.getString("insurance");
						
					Patient p = new Patient();
		
					p.setPesel(pesel);
					p.setName(name);
					p.setSurname(surname);
					p.setSex(sex);
					p.setInsurance(insurance);
						
					patients.add(p);
				}
			}
		} catch (SQLException e1) {
		}

		
		return patients;
	}
	
	/**
	 * Get one patient with a given id.
	 * 
	 * @param pesel
	 * @return
	 */
	public Patient readPatientByID(String pesel){
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){
				
				String sql = "SELECT * FROM App.Patients "
						+ "WHERE pesel LIKE ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, pesel);
				
				ResultSet set = statement.executeQuery();
				if(set.next()){
					String name = set.getString("name");
					String surname = set.getString("surname");
					String sex = set.getString("sex");
					String insurance = set.getString("insurance");
					
					Patient p = new Patient();
	
					p.setName(name);
					p.setSurname(surname);
					p.setPesel(pesel);
					p.setSex(sex);
					p.setInsurance(insurance);
					
					return p;
				}
			}
		} catch (SQLException e1) {
		}

		return null;
	}
	
	/**
	 * Removes patient with a given String pesel from database.
	 * 
	 * @param pesel
	 */
	public void deletePatient(String pesel){
		if(connection != null){
			
			List<Series> seriesList = readSeries(pesel, "");
			
			//delete all patients tests
			for (Series series : seriesList){
				deleteTestOfSeries(series.getId());
			}
			
			//delete all patients series
			deleteSeriesOfPatient(pesel);
			
			//delete patient
			try{
				String sql = "DELETE FROM App.Patients "
						+ "WHERE pesel = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, pesel);
				
				statement.executeUpdate();
				
			}catch (Exception e) {
			}
		}
	}
	
	
	// SERIES TABLE
	//=============================================================================
	
	/**
	 * Adds new series row to table, where auto increment id is Primary Key
	 * and pesel is foreign key to patients table.
	 * 
	 * @param name
	 * @param pesel
	 */
	public void createSeries(Series series){
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){
				String sql = "INSERT INTO App.Series VALUES (DEFAULT, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, series.getPatientPesel());
				statement.setString(2, series.getName());
				statement.executeUpdate();
			}
		} catch (SQLException e1) {
		}
	}
	
	/**
	 * Updates series row with a given id primary key.
	 * 
	 * @param id
	 * @param s
	 */
	public void updateSeries(int id, Series s){
		String name = s.getName();
		String patientPesel = s.getPatientPesel();

		try{
		connection = DriverManager.getConnection(DATABASE);
		
			if(connection != null){
				
				String sql = "UPDATE App.Series "
						+ "SET seriesname = ?, "
						+ "pesel = ? "
						+ "WHERE id = ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, name);
				statement.setString(2, patientPesel);
				statement.setInt(3, id);
				
				statement.executeUpdate();
			}
		
		}catch (Exception e) {
		}
	}
	
	/**
	 * Return all series that matches given pesel and name.
	 * 
	 * @param pesel
	 * @param name
	 * @return List<Series>
	 */
	public List<Series> readSeries(String pesel, String name){
		List<Series> seriesList = new ArrayList<>();
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){

				String sql = "SELECT * FROM App.Series "
						+ "WHERE pesel LIKE ? AND "
						+ "seriesname LIKE ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, "%" + pesel + "%");
				statement.setString(2, "%" + name + "%");
				
				ResultSet set = statement.executeQuery();
				while(set.next()){
					Series s = new Series();
	
					s.setID(set.getInt("id"));
					s.setPatientPesel(set.getString("pesel"));
					s.setName(set.getString("seriesname"));
					seriesList.add(s);
				}
			}
		} catch (SQLException e1) {
		}
		
		return seriesList;
	}
	
	/**
	 * Returns Series with a given id.
	 * 
	 * @param id
	 * @return Series
	 */
	public Series getSeriesByID(int id){
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){
				
				String sql = "SELECT * FROM App.Series "
						+ "WHERE id LIKE ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, id);
				
				ResultSet set = statement.executeQuery();
				if(set.next()){
					Series s = new Series();
					
					s.setID(set.getInt("id"));
					s.setPatientPesel(set.getString("pesel"));
					s.setName(set.getString("seriesname"));
					
					return s;
				}
			}
		} catch (SQLException e1) {
		}
		
		return null;
		
	}
	
	/**
	 * Deletes Series with a given id.
	 * 
	 * @param seriesid
	 */
	public void deleteSeries(int seriesid){
		
		//delete all tests
		deleteTestOfSeries(seriesid);
		
		try{
			String sql = "DELETE FROM App.Series "
					+ "WHERE id = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, seriesid);
			
			statement.executeUpdate();
			
		}catch (Exception e) {
		}
	}

	
	/**
	 * Deletes all series owned by Patient with a given pesel.
	 * 
	 * @param pesel
	 */
	public void deleteSeriesOfPatient(String pesel){
		
		try {
			connection = DriverManager.getConnection(DATABASE);
			
			if(connection != null){

				String sql = "DELETE FROM App.Series "
						+ "WHERE pesel LIKE ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, pesel);
				
				statement.executeUpdate();
			}
		} catch (SQLException e1) {
		}
	}
	
	
	// TESTS TABLE
	//=================================================================
	
	/**
	 * Inserts new Test file into table.
	 * 
	 * @param fis - Input stream storing file to upload.
	 * @param name - name of test
	 * @param seriesID - id of files series
	 */
	public void createTest(Test t){
		
		String name = t.getName();
		int seriesID = t.getSeries().getId();
		String path = t.getImagePath();
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(path));
			
			connection =  DriverManager.getConnection(DATABASE);
			
			if(connection != null){
				String sql = "INSERT INTO App.Tests "
						+ "VALUES (DEFAULT, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, seriesID);
				statement.setBlob(2, fis);
				statement.setString(3, name);
				statement.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates test row.
	 * 
	 * @param id
	 * @param name
	 */
	public void updateTest(int id, String name){

		try{
		connection = DriverManager.getConnection(DATABASE);
		
			if(connection != null){
				
				String sql = "UPDATE App.Tests "
						+ "SET name = ? "
						+ "WHERE id = ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, name);
				statement.setInt(2, id);
				
				statement.executeUpdate();
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads all tests with given series id.
	 * 
	 * @param seriesID
	 * @return
	 */
	public List<Test> readTests(int seriesID, String name){
		List<Test> testsList = new ArrayList<Test>();
		
		if(connection != null){
			try{
				String sql = "SELECT * FROM App.Tests "
						+ "WHERE series = ? AND "
						+ "name LIKE ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, seriesID);
				statement.setString(2, "%"+name+"%");
				
				ResultSet set = statement.executeQuery();
				
				while(set.next()){
					Test test = new Test();
					
					test.setName(set.getString("name"));
					
					Series series = new Series();
					series.setID(set.getInt("series"));
					test.setSeries(series);
					
					test.setId(set.getInt("id"));
					
					testsList.add(test);
				}
				
				return testsList;
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * Returns test with a given name and series id from database.
	 * Test imagePath attribute is a path of new created file named "patient.dcm", 
	 * where dicom is currently stored.
	 * 
	 * @param seriesId
	 * @param name
	 * @return
	 */
	public Test getTestByID(int id){
		Test t = null;

		if (connection != null){
		    
			try{
				String sql = "SELECT * FROM App.Tests "
						+ "WHERE id = ? ";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, id);
				
				ResultSet set = statement.executeQuery();
				
				while(set.next()){
					t = new Test();
					
					Blob blob = set.getBlob("image");
					byte[] array = blob.getBytes(1, (int)blob.length());
					File file = new File("patinet.dcm");
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(array);
					fos.close();
					
					t.setId(set.getInt("id"));
					t.setName(set.getString("name"));
					
					Series series = new Series();
					series.setID(set.getInt("series"));
					t.setSeries(series);
					
					t.setImage(file.getAbsolutePath());
				}
				
				return t;
				
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Removes all test of one series. 
	 * Parameter series is primary key of Series which tests are to remove.
	 * 
	 * @param series
	 */
	public void deleteTestOfSeries(int series){
		if(connection != null){
			try{
				String sql = "DELETE FROM App.Tests "
						+ "WHERE series = ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, series);
				
				statement.executeUpdate();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Removes test with a given id.
	 * 
	 * @param id
	 */
	public void deleteTest(int id){
		if(connection != null){
			try{
				String sql = "DELETE FROM App.Tests "
						+ "WHERE id = ?";
				
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, id);
				
				statement.executeUpdate();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
