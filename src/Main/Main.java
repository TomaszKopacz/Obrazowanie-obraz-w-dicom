package Main;

import javax.swing.SwingUtilities;

import Controller.Controller;
import Model.DAO;
import View.StartPanel;
import View.AddingPatientFrame;
import View.Application;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				Application app = new Application();
				DAO dao = new DAO();
				Controller c = new Controller(app, dao);
			}
		 });
	}

}
