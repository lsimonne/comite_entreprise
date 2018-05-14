package salaries.utils;


public final class Regex {

	private Regex() {}
	
	public static final String REGEX_NAME = "^[a-zA-Z[- ]]+$";
	public static final String REGEX_NUMBER = "^[0-9]+$";
	public static final String REGEX_PHONE_NUMBER = "^\\+?[0-9 ]+$";
	public static final String REGEX_ZIP_CODE = "^[0-9]{5}$";
	public static final String REGEX_STREET_NAME = "^[a-zA-z[0-9][- ]]+$";
}
