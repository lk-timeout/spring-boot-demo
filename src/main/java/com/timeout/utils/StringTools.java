package com.timeout.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author lk-timeout
 */
public final class StringTools {

	public final static String IPREGREX = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	public static final String _blank = "";
	public static final String s_blank_regrex = "\\s*";
	public static final String en_blank_regrex = " +";
	public static final String cn_blank_regrex = "　+";

	private StringTools() {
	}

	public final static String getString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public final static long getLong(Object str) {
		if (str == null) {
			return 0;
		}
		return getLong(str.toString());
	}

	public final static int getInt(Object str) {
		if (str == null) {
			return 0;
		}
		return getInt(str.toString());
	}

	public final static float getFloat(Object str) {
		if (str == null) {
			return 0;
		}
		return getFloat(str.toString());
	}

	public final static double getDouble(Object str) {
		if (str == null) {
			return 0;
		}
		return getDouble(str.toString());
	}

	/**
	 * @param str double的数值
	 * @return 保留两位小数值
	 */
	public final static double getDoubleDigit(Object str) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		if (str == null) {
			return 0;
		}
		return Double.parseDouble(decimalFormat.format(str));
	}

	public final static boolean getBoolean(Object str) {
		if (str == null) {
			return false;
		}
		return getBoolean(str.toString());
	}

	public final static short getShort(Object str) {
		return getShort(str.toString());
	}

	public final static long getLong(String str) {
		return (long) getDouble(str);
	}

	public final static int getInt(String str) {
		return (int) getDouble(str);
	}

	public final static float getFloat(String str) {
		return (float) getDouble(str);
	}

	public final static double getDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public final static boolean getBoolean(String str) {
		return Boolean.valueOf(str);
	}

	public final static short getShort(String str) {
		return Short.parseShort(str);
	}

	public final static boolean isEmpty(String str) {
		return (null == str) ? true : ((str.trim().length() == 0) ? true : false);
	}

	/**
	 * 判断两个字符串是否相等，兼容比较字符为null的情况。
	 *
	 * @author linsida
	 * @date 2016年8月4日 下午2:29:55
	 */
	public final static boolean equals(String str1, String str2) {
		return (str1 == null) ? (str2 == null) : ((str2 == null) ? false : str1.equals(str2));
	}

	/**
	 * 空白
	 *
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public final static String removeBlank(String expression) {
		if (null != expression) {
			expression = expression.replaceAll(en_blank_regrex, _blank);
			expression = expression.replaceAll(cn_blank_regrex, _blank);
			expression = expression.replaceAll(s_blank_regrex, _blank);
		}
		return expression;
	}

	/**
	 * 将数据保留两位小数
	 */
	public static double get2Decimal(double num) {
		DecimalFormat dFormat = new DecimalFormat("#.00");
		String yearString = dFormat.format(num);
		Double temp = Double.valueOf(yearString);
		return temp;
	}

	/**
	 * 如果只是用于程序中的格式化数值然后输出，那么这个方法还是挺方便的。
	 * 应该是这样使用：System.out.println(String.format("%.3f", d));
	 *
	 * @param d
	 * @return
	 */
	public static String formatDouble5(double d) {
		String str = String.format("%.20f", d);
		// double dscore = Double.parseDouble(str);
		return str;
	}

	/**
	 * 如果只是用于程序中的格式化数值然后输出，那么这个方法还是挺方便的。
	 * 应该是这样使用：System.out.println(String.format("%.3f", d));
	 *
	 * @param d
	 * @return
	 */
	public static String formatDouble6(double d) {
		String str = String.format("%.2f", d);
		// double dscore = Double.parseDouble(str);
		return str;
	}

	/**
	 * 移除小数点后多余的 0 和 .
	 *
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	public final static boolean isboolIp(String ipAddress) {
		if (isEmpty(ipAddress)) {
			return false;
		}
		Pattern pattern = Pattern.compile(IPREGREX);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	/**
	 * 判断是否为纯数字
	 */
	public final static boolean isDigit(String str) {
		if (!isEmpty(str)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher matcher = pattern.matcher(str);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}

	public final static String replaveAll(String content, String regex, Object replacement) {
		String replacementString = getString(replacement);
		StringBuilder builder = new StringBuilder();
		for (int index = 0, length = replacementString.length(); index < length; index++) {
			char replaceChar = replacementString.charAt(index);
			if (replaceChar == '$') {
				builder.append("\\");
			}
			builder.append(replaceChar);
		}
		try {
			return content.replaceAll(regex, builder.toString());
		} catch (Exception e) {
			return replacementString;
		}
	}
}
