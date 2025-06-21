package kasir_uas;

import kasir_uas.controller.*;
import kasir_uas.config.*;
import kasir_uas.model.*;
import kasir_uas.view.*;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	        LoginView lv = new LoginView();
	        LoginController lc = new LoginController(lv);
	        lv.setVisible(true);
	}
}
