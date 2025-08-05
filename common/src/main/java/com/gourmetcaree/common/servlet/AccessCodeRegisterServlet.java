package com.gourmetcaree.common.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.gourmetcaree.common.role.AccessCode;
import com.gourmetcaree.common.role.AuthLevel;
import com.gourmetcaree.common.role.Role;

/**
 * ロールとロールに属する権限をアプリケーションスコープに登録するクラスです。<br>
 * このサーブレットはコンテナ起動時に読み込まれるように設定して下さい。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AccessCodeRegisterServlet extends HttpServlet {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4103551140469343179L;

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
	private void setup() throws ServletException {
		try {
			setApplicationScope();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 *
	 * ロールと権限の付与情報をアプリケーションスコープに登録する。
	 */
	private void setApplicationScope() throws IOException, SAXException, ServletException {

		Role role = getRole();

		if (role == null) {
			throw new ServletException("マッチングオブジェクトの生成に失敗しました");
		}


		Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		List<AuthLevel> authLevelList = role.getAuthLevels();

		//RoleオブジェクトをSetに詰め替え
		if (authLevelList != null && !authLevelList.isEmpty()) {

			Iterator<AuthLevel> it = authLevelList.iterator();

			while (it.hasNext()) {

				Set<String> set = new HashSet<String>();

				AuthLevel authLevel = it.next();

				if (authLevel.getAccessCodes() == null) {
					continue;
				}

				Iterator<AccessCode> nextIt = authLevel.getAccessCodes().iterator();

				while (nextIt.hasNext()) {
					set.add(nextIt.next().getAccessCode());
				}

				map.put(authLevel.getAuthLevelCode(), set);
			}
		}

		getServletContext().setAttribute("authRole", Collections.unmodifiableMap(map));
	}

	/**
	 * XMLからロールオブジェクトを生成します。
	 * @return ロールオブジェクト
	 * @throws ServletException
	 * @throws IOException
	 * @throws SAXException
	 */
	private Role getRole()
	throws ServletException, IOException, SAXException {

		String path = getInitParameter("path");
		if (path == null) {
			throw new ServletException("パラメータpathを設定して下さい");
		}

		Digester digester = new Digester();

		// XMLからロール情報オブジェクトの生成
		digester.addObjectCreate("role", Role.class);
		digester.addBeanPropertySetter("role/version");
		digester.addBeanPropertySetter("role/description");
	    digester.addObjectCreate("role/authLevels", ArrayList.class);
	    digester.addSetNext("role/authLevels", "setAuthLevels");

	    digester.addObjectCreate("role/authLevels/authLevel", AuthLevel.class);
	    digester.addSetNext("role/authLevels/authLevel", "add");

		digester.addBeanPropertySetter("role/authLevels/authLevel/authLevelName");
		digester.addBeanPropertySetter("role/authLevels/authLevel/authLevelCode");
	    digester.addObjectCreate("role/authLevels/authLevel/accessCodes", ArrayList.class);
	    digester.addSetNext("role/authLevels/authLevel/accessCodes", "setAccessCodes");


	    digester.addObjectCreate("role/authLevels/authLevel/accessCodes/accessCode", AccessCode.class);
		digester.addBeanPropertySetter("role/authLevels/authLevel/accessCodes/accessCode");
		digester.addSetNext("role/authLevels/authLevel/accessCodes/accessCode", "add");


		String p = getServletContext().getRealPath(path);
		Role role = (Role) digester.parse(new File(p));
		return role;
	}
}
