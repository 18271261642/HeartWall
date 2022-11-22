package com.jkcq.hrwtv.util;


public class StringUtil {
	/**
	 * 去除字符串中所有空格
	 * 
	 * @author haibo.wang
	 */
	public static String remove(String resource) {
		StringBuffer buffer = new StringBuffer();
		int position = 0;
		char currentChar;

		while (position < resource.length()) {
			currentChar = resource.charAt(position++);
			if (currentChar != ' ')
				buffer.append(currentChar);
		}
		return buffer.toString();
	}

	public static String deNull(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	public static String Object2String(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 功能描述：是否为空白,包括null和""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0 || "null".equals(str);
	}

	/**
	 * 
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return 返回是用户和密码为空
	 */
	public static boolean isnull(String name, String password) {
		return !(name.equals("") && password.equals(""));

	}


	public static String StringOrDefaultValue(String content, String defaultValue){
		return StringUtil.isBlank(content)?defaultValue:content;
	}

//	public static void setHD(final OnClickListener onclick, final String title,
//			final String name) {
//		MainActivity.getDataTitle(new MainActivity.IDecodeDataBack() {
//
//			@SuppressLint("NewApi")
//			@Override
//			public void dataDecodeCallback(TextView textview,
//					Button ivTitleBtnRight) {
//				// TODO Auto-generated method stub
//				textview.setText(title);
//				ivTitleBtnRight.setVisibility(View.VISIBLE);
//				ivTitleBtnRight.setText(name);
//				ivTitleBtnRight.setBackground(null);
//				ivTitleBtnRight.setOnClickListener(onclick);
//			}
//		});
//	}
//
//	public static void setHx(final String title) {
//		MainActivity.getDataTitle(new MainActivity.IDecodeDataBack() {
//
//			@Override
//			public void dataDecodeCallback(TextView textview,
//					Button ivTitleBtnRight) {
//				// TODO Auto-generated method stub
//				textview.setText(title);
//				ivTitleBtnRight.setVisibility(View.GONE);
//
//			}
//		});
//
//	}

}