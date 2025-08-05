package com.gourmetcaree.common.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * プロパティファイルをアプリケーションスコープに登録するクラスです。<br>
 * このサーブレットはコンテナ起動時に読み込まれるように設定して下さい。
 * @author Takahiro Ando
 * @version 1.0
 */
public class PorpertyRegisterServlet extends HttpServlet {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7010863473863186854L;

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		setup();
	}

	/**
	 * 初期設定を行います。
	 */
	private void setup() {
		Enumeration<?> paramNames = getInitParameterNames();

		while (paramNames.hasMoreElements()) {
			String propertyName = (String) paramNames.nextElement();
			String registName = getInitParameter(propertyName);
			registProperty(propertyName, registName);
		}
	}

	/**
	 * プロパティをアプリケーションスコープに登録します。
	 * @param propertyName プロパティファイル名
	 * @param registName 登録名
	 */
	private void registProperty(String propertyName, String registName) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle(propertyName);
		Enumeration<String> e = bundle.getKeys();

		Map<String, String> prop = new HashMap<String, String>();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			prop.put(key, bundle.getString(key));
		}

		getServletContext().setAttribute(registName, Collections.unmodifiableMap(prop));
	}
}
