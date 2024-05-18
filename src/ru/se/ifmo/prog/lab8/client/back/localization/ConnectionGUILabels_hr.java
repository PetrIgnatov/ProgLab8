package ru.se.ifmo.prog.lab8.client.back.localization;

import java.util.ListResourceBundle;

public class ConnectionGUILabels_hr extends ListResourceBundle {
	private Object[][] contents = {{"IP"," IP: "}, {"PORT", " LUKA: "}, {"LOGIN", " PRIJAVA: "}, {"PASSWORD", " LOZINKA: "}, {"CONFIRM", "POTVRDITI"}, {"REGISTER", "REGISTAR"}, {"SIGN IN", "PRIJAVI SE"}, {"SISUCCESS", "USPJEŠNO STE PRIJAVLJENI!"}, {"SIERROR","POKUŠAJTE S DRUGOM PRIJAVOM ILI LOZINKOM"}, {"REGSUCCESS","USPJEŠNO STE SE REGISTRIRALI"}, {"REGERROR","POKUŠAJTE S DRUGOM PRIJAVOM ILI LOZINKOM"}, {"CONSUCCESS", "VEZA JE BILA USPJEŠNA!"}, {"CONERROR", "POVEZIVANJE NIJE USPJELO! ISPROBAJTE NEKI DRUGI IP ILI PORT!"}};
	@Override
	public Object[][] getContents() { return contents; }
}
