package com.gourmetcaree.admin.pc.sys.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.IllegalTargetException;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.struts.util.S2ActionMappingUtil;

import com.gourmetcaree.admin.pc.sys.annotation.WebdataAccessUser;
import com.gourmetcaree.admin.pc.sys.annotation.WebdataAccessUser.AccessType;
import com.gourmetcaree.admin.pc.sys.dto.UserDto;
import com.gourmetcaree.admin.pc.sys.dto.WebDataEditorDto;
import com.gourmetcaree.admin.pc.sys.dto.WebDataEditorDto.WebDataAccessDto;
import com.gourmetcaree.admin.pc.webdata.action.webdata.EditAction;
import com.gourmetcaree.admin.pc.webdata.form.webdata.EditForm;

/**
 * WEBデータへのアクセスインターセプタ
 * @author nakamori
 *
 */
public class WebDataAccessInterceptor extends AbstractInterceptor {

	/**
	 *
	 */
	private static final long serialVersionUID = -5381824366069210400L;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept
	 * .MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Class<?> clazz = getTargetClass(invocation);

		// webデータのエディットアクションのみ適応
		if (!clazz.isAssignableFrom(EditAction.class)) {
			return invocation.proceed();
		}

		Method method = invocation.getMethod();
		// WebdataAccessUserのアノテーションがないクラスは対象外
		if (!method.isAnnotationPresent(WebdataAccessUser.class)) {
			return invocation.proceed();
		}

		// アノテーションからアクセスタイプを取得
		AccessType accessType = method.getAnnotation(WebdataAccessUser.class).accessType();


		S2Container container = SingletonS2ContainerFactory.getContainer();
		WebDataEditorDto editorDto;
		int webId;
		UserDto userDto;

		// UserDto設定
		Object userObj = container.getComponent(UserDto.class);
		if (userObj == null || !(userObj instanceof UserDto)) {
			return invocation.proceed();
		}
		userDto = (UserDto) userObj;

		// webId設定
		try {
			webId = getWebId();
		} catch (NumberFormatException e) {
			return invocation.proceed();
		} catch (IllegalTargetException e) {
			return invocation.proceed();
		}

		// アクセスユーザDTO設定
		Object dtoObj = container.getComponent(WebDataEditorDto.class);
		if (dtoObj == null || !(dtoObj instanceof WebDataEditorDto)) {
			return invocation.proceed();
		}
		editorDto = (WebDataEditorDto) dtoObj;


		switch (accessType) {
			case CHECK_ACCESS:
				operateInputPage(container, editorDto, userDto, webId);
				break;
			case UPDATE_TIME:
				operateUpdate(editorDto, userDto, webId);
				break;
			case REMOVE:
				if (editorDto.containsWebId(webId) && isOwnAccess(editorDto.getWebDataAccessDto(webId), NumberUtils.toInt(userDto.getUserId()))) {
					editorDto.remove(webId);
				}
				break;
			default:
				break;
		}

		return invocation.proceed();
	}

	/**
	 * 入力ページ表示時の処理を行います。
	 * @param container コンテナ
	 * @param editorDto WEBデータアクセスユーザDTO
	 * @param userDto ユーザDTO
	 * @param webId webId
	 *
	 * @author nakamori
	 */
	private void operateInputPage(S2Container container,
			WebDataEditorDto editorDto, UserDto userDto, int webId) {
		int salesId = NumberUtils.toInt(userDto.getUserId());

		// webDataのアクセスマップ処理は同期させる。
		synchronized (this) {

			EditAction action = (EditAction) S2ActionMappingUtil
					.getActionMapping().getAction();
			action.setAlertOtherAccessFlg(false);




			// webIdが登録されていないか、セッション時間が切れている場合は、自分の情報で更新する。
			if (!editorDto.containsWebId(webId)) {
				editorDto.addWebDataAccessDto(webId, salesId, userDto.name);
				return;
			}

			WebDataAccessDto accessDto = editorDto.getWebDataAccessDto(webId);

			// セッション時間が切れている場合は、入力し直す。
			if (isOverSessionTime(accessDto, container)) {
				editorDto.addWebDataAccessDto(webId, salesId, userDto.name);
				return;
			}
			// 自分のアクセスの場合は、情報を入れなおす。
			if (isOwnAccess(accessDto, salesId)) {
				editorDto.addWebDataAccessDto(webId, salesId, userDto.name);
				return;
			}

			action.setAlertOtherAccessFlg(true);

		}
	}


	/**
	 * 更新処理を行います。
	  * @param editorDto WEBデータアクセスユーザDTO
	 * @param userDto ユーザDTO
	 * @param webId webId
	 *
	 * @author nakamori
	 */
	private void operateUpdate(WebDataEditorDto editorDto, UserDto userDto, int webId) {
		// アクセスDTOが存在しない場合は処理をしない。
		if (!editorDto.containsWebId(webId)) {
			return;
		}
		WebDataAccessDto dto = editorDto.getWebDataAccessDto(webId);
		if (isOwnAccess(dto, NumberUtils.toInt(userDto.getUserId()))) {
			dto.updateLastAccessTimestamp();
		}
	}

	/**
	 * 自分のアクセスかどうか
	 *
	 * @param dto  webデータアクセスDTO
	 * @param salesId 営業ID
	 * @return 自分のアクセスの場合にtrue
	 *
	 * @author nakamori
	 */
	private boolean isOwnAccess(WebDataAccessDto dto, int salesId) {
		return dto.getSalesId() == salesId;
	}

	/**
	 * セッション時間が切れているか
	 * @param dto  webデータアクセスDTO
	 * @return セッション時間が切れていればtrue
	 * @author nakamori
	 */
	private boolean isOverSessionTime(WebDataAccessDto dto,
			S2Container container) {
		if (dto == null) {
			return false;
		}
		HttpSession session = (HttpSession) container.getComponent("session");
		long sessionTime = session.getMaxInactiveInterval() * 1000l;
		long diff = System.currentTimeMillis() - dto.getLastAccessTimestamp();
		return diff > sessionTime;
	}

	/**
	 * webIdを取得します。
	 * @return webId
	 * @author nakamori
	 */
	private int getWebId() {
		Object formObj = S2ActionMappingUtil.getActionMapping().getActionForm();
		if (formObj == null || !(formObj instanceof EditForm)) {
			throw new IllegalTargetException("対象フォームがEditFormではありません。");
		}

		EditForm form = (EditForm) formObj;

		return Integer.parseInt(form.id);

	}

}
