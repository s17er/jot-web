package com.gourmetcaree.shop.pc.top.action.top;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AreaService;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService;
import com.gourmetcaree.shop.logic.dto.WebdataDto;
import com.gourmetcaree.shop.logic.logic.WebdataLogic;
import com.gourmetcaree.shop.logic.property.WebdataProperty;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.top.dto.top.MenuDto;
import com.gourmetcaree.shop.pc.top.form.top.MenuForm;

/**
 * メニューを表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class MenuAction extends PcShopAction {

	/** メニューフォーム */
	@ActionForm
	@Resource
	protected MenuForm menuForm;

	/** WEBデータロジック */
	@Resource
	protected WebdataLogic webdataLogic;

	/** 電話応募履歴サービス */
	@Resource
	private WebIpPhoneHistoryService webIpPhoneHistoryService;

	/** エリアサービス */
	@Resource
	private AreaService areaService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		menuForm.list = createDisplayValue(getData());
		getUnReadMailCount();
		return TransitionConstants.Menu.JSP_SPB01M01;
	}

	/**
	 * 一覧データの取得
	 */
	private List<VWebList> getData() {

		List<VWebList> entityList = new ArrayList<>();

		try {
			// データの取得
			entityList = webdataLogic.getPublishWebdataList();
		} catch (WNoResultException e) {
			//何もしない
		}

		return entityList;
	}

	/**
	 * 取得したデータを一覧表示用に調整
	 * @param entityList
	 * @return
	 */
	private List<MenuDto> createDisplayValue(List<VWebList> entityList) {

		// 掲載中の求人がない場合は空のリストを返す
		if(entityList.size() == 0) {
			return new ArrayList<>();
		}

		// 変換のため、プロパティにセット
		WebdataProperty property = new WebdataProperty();
		property.vWebListList = entityList;

		// 画面表示の共通項目を変換
		List<WebdataDto> webDatadtolist = webdataLogic.createDisplayList(property);

		// 画面Dtoを保持するリスト
		List<MenuDto> dtoList = new ArrayList<MenuDto>();

		// Dtoにコピー
		for (WebdataDto webdataDto : webDatadtolist) {

			MenuDto dto = new MenuDto();
			Beans.copy(webdataDto, dto).execute();

			String previewKey = DigestUtils.md5Hex(dto.id + GourmetCareeConstants.SYSTEM_HASH_SOLT);

			List<MArea> areaList;
			try {
				areaList = areaService.getMAreaList(dto.areaCd);
			} catch (WNoResultException e1) {
				continue;
			}

			StringBuilder previewUrl = new StringBuilder();
			previewUrl.append(getCommonProperty("gc.sslDomain"));
			previewUrl.append(String.format(getCommonProperty("gc.preview.url.webdata.list"), areaList.get(0).linkName));
			previewUrl.append(String.format("?ids=%s&key=%s", dto.id, previewKey));
			dto.previewUrl = previewUrl.toString();

			dto.ipPhoneHistoryCount = webIpPhoneHistoryService.getIpPhoneApplicationCount(Integer.parseInt(webdataDto.id));

			dtoList.add(dto);
		}

		// Dtoリストを返却
		return dtoList;
	}

	/**
	 * 未読のメールを取得する
	 */
	private void getUnReadMailCount() {
		menuForm.applicationMailCount = (int)applicationLogic.getUnReadApplicationMailCount();
		menuForm.preApplicationMailCount = (int)preApplicationLogic.getUnReadPreApplicationMailCount();
		menuForm.scoutMailCount = (int)scoutMailLogic.getUnReadScoutMailCount();
		menuForm.observateApplicationMailCount = (int)applicationLogic.getUnReadObservateApplicationMailCount();
		menuForm.arbeitMailCount = (int)applicationLogic.getUnReadArbeitMailCount();

		unReadApplicationMailFlg = menuForm.applicationMailCount > 0;
		unReadPreApplicationMailFlg =menuForm.preApplicationMailCount > 0;
		unReadScoutMailFlg = menuForm.scoutMailCount > 0;
		unReadObservateApplicationMailFlg = menuForm.observateApplicationMailCount > 0;
		unReadArbeitMailFlg = menuForm.arbeitMailCount > 0;
	}
}
