package salaries.utils;

/**
 * Classe regroupant des m�thodes "d'intendance"
 * 
 * @author Fran�ois
 * 
 */
public class OutilsChaine
{
	private OutilsChaine()
	{

	}

	/**
	 * Convertit une cha�ne de caract�res en Nom Propre
	 * 
	 * @param chaine
	 *            cha�ne � convertir
	 * @return la cha�ne convertie en Nom Propre
	 */
	public static String toNomPropre(String chaine)
	{
		if (chaine.length() == 0)
			return chaine;
		StringBuilder resultat = new StringBuilder();
		resultat.append(chaine.substring(0, 1).toUpperCase());
		for (int i = 1; i < chaine.length(); i++)
		{
			if (chaine.substring(i - 1, i).equals(" ") || chaine.substring(i - 1, i).equals("-"))
			{
				resultat.append(chaine.substring(i, i + 1).toUpperCase());
			}
			else
			{
				resultat.append(chaine.substring(i, i + 1).toLowerCase());
			}
		}
		return resultat.toString();
	}
}
