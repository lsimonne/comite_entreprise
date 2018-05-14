package salaries.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FichierTexte {

	private File file;

	public FichierTexte(File file) {

		this.file = file;
	}

	public List<String> lire() {

		List<String> lignes = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(
				new FileReader(file))) {
			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				lignes.add(ligne);
			}
		} catch (IOException e) {
			
		}
		return lignes;
	}

	public void ecrireDansFichierTexte(List<String> lignes) {

		try {
			PrintWriter printwriter = new PrintWriter(
					new BufferedWriter(new FileWriter(file)));
			for (String ligne : lignes) {
				printwriter.println(ligne);
			}
			printwriter.close();
		} catch (IOException exception) {
			// Nothing to do
		}
	}
}
