package View;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Tomasz Kopacz
 *
 */
public class SourcesFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JScrollPane mScrollPane;
	private JTextArea mTextArea;
	
	public SourcesFrame(){
		
		//default look
		this.setSize(new Dimension(500, 500));
		this.setTitle("èrÛd≥a");
		this.setLayout(new GridLayout(1, 1));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(350, 150);

		//content
		mTextArea = new JTextArea();
		mScrollPane = new JScrollPane(mTextArea);
		mScrollPane.setVisible(true);
		
		mTextArea.setEditable(false);
		getContentPane().add(mScrollPane);
	}
	
	/**
	 * Reads text from file given via path.
	 * 
	 * @param pathname
	 * @return JTextArea
	 */
	public void readText(String path){
          
		String str = "";
		try{
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			str = new String(data, "UTF-8");
		}catch (Exception e) {
		}
           
        mTextArea.setText(str);
	}
	
	public JTextArea getTextArea(){
		return this.mTextArea;
	}

}
