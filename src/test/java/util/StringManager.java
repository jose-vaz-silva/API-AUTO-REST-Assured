package util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManager {
	public static String substringByRegex(String string, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		String matcherStr = new String();
		if (matcher.find()) {
			matcherStr = matcher.group();
		}
		return matcherStr;
	}

	public static List<String> getListMatcherByRegex(String string, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		List<String> matcherList = new LinkedList<String>();
		while (matcher.find()) {
			matcherList.add(matcher.group());
		}
		return matcherList;
	}
}
