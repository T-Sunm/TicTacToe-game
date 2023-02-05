package main;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class main {
	public static void main(String[] args) {
		try {
		    //set look and feel
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		    new view();
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
	}
}
