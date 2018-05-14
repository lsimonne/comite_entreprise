package salaries.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FrenchDate
{
	private static DateTimeFormatter formatNumerique = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private FrenchDate()
	{
		
	}
	/**
	 * Permet d'obtenir une instance de LocalDate à partir d'une date exprimée sous
	 * forme d'une chaine de type jj/mm/aaaa
	 * 
	 * @param date
	 *            : date sous la forme jj/mm/aaa
	 * @return une instance de la classe LocalDate
	 */
	public static LocalDate stringToDate(String date)
	{
		try {
			return LocalDate.parse(date, formatNumerique);
		}
		catch (Exception e) {
			return null;
		}
	}


	public static String dateHeureToString(LocalDate uneDate)
	{
		return formatNumerique.format(uneDate);
	}

}
