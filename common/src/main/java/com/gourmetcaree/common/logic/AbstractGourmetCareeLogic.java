package com.gourmetcaree.common.logic;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import com.gourmetcaree.db.common.service.dto.ServiceAccessUser;
import com.gourmetcaree.db.common.service.dto.ServiceAdminAccessUser;
import com.gourmetcaree.db.common.service.dto.ServiceMemberAccessUser;
import com.gourmetcaree.db.common.service.dto.ServiceShopAccessUser;

/**
 *
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public abstract class AbstractGourmetCareeLogic {

	/** JDBCマネージャ */
	@Resource
	protected JdbcManager jdbcManager;

	/** ログ */
	protected final Logger logicLog = Logger.getLogger(this.getClass());


	/**
	 * セッションからuserDtoを取得します。
	 * @return
	 */
	protected Object getServiceAccessUser() {
		Map<?, ?> sessionMap = (Map<?, ?>) SingletonS2ContainerFactory.getContainer().getExternalContext().getSessionMap();
		return sessionMap.get("userDto");
	}

	/**
	 * UserDtoからUserIdを取得します。
	 * @return userId
	 */
	protected String getUserId() {
		ServiceAccessUser serviceAccessUser = (ServiceAccessUser) getServiceAccessUser();
		return serviceAccessUser.getUserId();
	}

	/**
	 * UserDtoが運営側システムかどうか判別します。
	 * @return UserDtoが運営側システムの場合true、それ以外の場合false
	 */
	protected boolean isAdminDto() {

		// UserDtoが運営側システムの場合
		if (getServiceAccessUser() instanceof ServiceAdminAccessUser) {
			return true;
		}
		return false;
	}

	/**
	 * UserDtoが店舗側システムかどうか判別します。
	 * @return UserDtoが店舗側システムの場合true、それ以外の場合false
	 */
	protected boolean isShopDto() {

		// UserDtoが店舗側システムの場合
		if (getServiceAccessUser() instanceof ServiceShopAccessUser) {
			return true;
		}
		return false;
	}

	/**
	 * UserDtoが会員側システムかどうか判別します。
	 * @return UserDtoが会員側システムの場合true、それ以外の場合false
	 */
	protected boolean isMemberDto() {

		// UserDtoが会員側システムの場合
		if (getServiceAccessUser() instanceof ServiceMemberAccessUser) {
			return true;
		}
		return false;
	}

	/**
	 * 運営側システムのUserDtoを返します。<br />
	 * 運営システムから呼び出されていない場合はnullを返します。
	 * @return 運営側システムUserDto
	 */
	protected ServiceAdminAccessUser getAdminDto() {

		if (isAdminDto()) {
			return (ServiceAdminAccessUser) getServiceAccessUser();
		}
		return null;
	}

	/**
	 * 店舗側システムのUserDtoを返します。<br />
	 * 店舗システムから呼び出されていない場合はnullを返します。
	 * @return 店舗側システムUserDto
	 */
	protected ServiceShopAccessUser getShopDto() {

		if (isShopDto()) {
			return (ServiceShopAccessUser) getServiceAccessUser();
		}
		return null;
	}

	/**
	 * 会員システムのUserDtoを返します。<br />
	 * 会員システムから呼び出されていない場合はnullを返します。
	 * @return 会員システムUserDto
	 */
	protected ServiceMemberAccessUser getMemberDto() {

		if (isMemberDto()) {
			return (ServiceMemberAccessUser) getServiceAccessUser();
		}
		return null;
	}

	/**
	 * 共通プロパティファイルから指定のキーの値を取得します。
	 * @param key キー
	 * @return 値
	 */
	protected String getCommonProperty(String key) {
		ServletContext application = SingletonS2Container.getComponent(ServletContext.class);
		return (String) ((Map<?, ?>) application.getAttribute("common")).get(key);
	}
}
