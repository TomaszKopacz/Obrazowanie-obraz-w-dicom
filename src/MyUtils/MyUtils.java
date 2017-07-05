package MyUtils;

/**
 * Abstract class containing simple methods to validate input format.
 * @author Tomasz Kopacz
 */
public abstract class MyUtils {

	/**
	 * Checks if String str contains any digits.
	 * @param str - the String to check
	 * @return boolean
	 */
	public static boolean areNumbers(String str){
		for(char c : str.toCharArray()){
			if(Character.isDigit(c))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if String str contains only digits.
	 * @param str - the String to check
	 * @return boolean
	 */
	public static boolean areLetters(String str){
		for(char c : str.toCharArray()){
			if(c < 48 || c > 57 )
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if given String str is convertible to Float.
	 * @param str - the String to check
	 * @return boolean
	 */
	public static boolean isFloat(String str){
		try{
			Float.parseFloat(str);
		}catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if String str is convertible to Integer
	 * @param str - the String to check
	 * @return <code>true</code>or <code>false</code>
	 */
	public static boolean isInteger(String str){
		try{
			Integer.parseInt(str);
		}catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}