package com.gourmetcaree.admin.pc.ipPhone.action.ipPhoneHistory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.ipPhone.form.ipPhoneHistory.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.IpPhoneHistoryCsvLogic;
import com.gourmetcaree.admin.service.property.IpPhoneHistoryCsvProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.csv.IpPhoneHistoryCsv;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWebIpPhoneHistory;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.ResultCSVDto;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.ResultDto;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.SearchProperty;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * IP電話履歴一覧アクション
 * @author nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction {


	@ActionForm
	@Resource
	private ListForm listForm;

	/** IP電話応募履歴サービス */
	@Resource
	private WebIpPhoneHistoryService webIpPhoneHistoryService;

	/** CSV出力ロジック */
	@Resource
	private IpPhoneHistoryCsvLogic ipPhoneHistoryCsvLogic;

	/** 検索結果 */
	public List<ResultDto> dtoList;

	/** ページナビ */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.IpPhone.JSP_APR01L01)
	public String index() {
		return show();
	}


	/**
	 * 初期表示用
	 * @return JSP
	 */
	private String show() {
		// 自身の会社IDをセット
		if(StringUtil.isEmpty(listForm.where_companyId)) {
			listForm.where_companyId = userDto.companyId;
		}
		// 自身の営業担当者IDをセット
		if(StringUtil.isEmpty(listForm.where_salesId)) {
			listForm.where_salesId = userDto.userId;
		}
		return TransitionConstants.IpPhone.JSP_APR01L01;
	}

	/**
	 * リセットして検索
	 */
	@Execute(validator = true, reset = "resetForm", validate = "validate", input = TransitionConstants.IpPhone.JSP_APR01L01)
	public String clearSearch() {
		return search();
	}

	/**
	 *
	 * 検索を行います。
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.IpPhone.JSP_APR01L01)
	public String search() {
		listForm.pageNum = null;
		doSearch();
		return TransitionConstants.IpPhone.JSP_APR01L01;
	}

	/**
	 * ページの切り替え
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.IpPhone.JSP_APR01L01)
	public String changePage() {
		doSearch();
		return TransitionConstants.IpPhone.JSP_APR01L01;
	}

	/**
	 * 表示件数の切り替え
	 */
	@Execute(validator = false, reset = "resetPageNum", input = TransitionConstants.IpPhone.JSP_APR01L01)
	public String changeMaxRow() {
		doSearch();
		return TransitionConstants.IpPhone.JSP_APR01L01;
	}

	/**
	 * CSV出力
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.IpPhone.JSP_APR01L01)
	public String output() {
		outputCsv();
		return null;
	}

	/**
	 * 検索を行います。
	 */
	private void doSearch() {
		SearchProperty property = listForm.createSearchProperty();
		final List<String> ignoreList = readIgnoreIpPhoneList();
		try {
			dtoList = new ArrayList<WebIpPhoneHistoryService.ResultDto>();
			webIpPhoneHistoryService.select(property,
					new IterationCallback<WebIpPhoneHistoryService.ResultDto, Void>() {

						@Override
						public Void iterate(ResultDto entity,
								IterationContext context) {

							if (entity == null) {
								return null;
							}

							entity.telTime = GourmetCareeUtil.convertSecondsToMinuteSecondStr(entity.memberTelSecond);

							// 電話番号変換
							entity.memberTel = convertIgnoreMembertel(entity.memberTel, ignoreList);

							// 通話ステータスがエラーの場合は表示を変更する
							entity.telStatusName = convertTelStatusName(entity.telStatusName);

							dtoList.add(entity);
							return null;
						}

					});
			pageNavi = property.pageNavi;
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * CSV出力を行います。
	 */
	private void outputCsv() {
		SearchProperty property = listForm.createSearchProperty();
		final List<String> ignoreList = readIgnoreIpPhoneList();
		final List<IpPhoneHistoryCsv> csvList = new ArrayList<IpPhoneHistoryCsv>();
		try {
			webIpPhoneHistoryService.selectCsv(property,
				new IterationCallback<ResultCSVDto, Void>() {
					@Override
					public Void iterate(ResultCSVDto entity,
							IterationContext context) {
						if (entity == null) {return null;}

						IpPhoneHistoryCsv csv = Beans.createAndCopy(IpPhoneHistoryCsv.class, entity)
								.dateConverter("yyyy/MM/dd HH:mm",
										WztStringUtil.toCamelCase(TWebIpPhoneHistory.CALL_NOTE_CAUGHT_MEMBER_TEL_DATETIME))
								.dateConverter("yyyy/MM/dd HH:mm",
										WztStringUtil.toCamelCase(MVolume.POST_START_DATETIME))
								.dateConverter("yyyy/MM/dd HH:mm",
										WztStringUtil.toCamelCase(MVolume.POST_END_DATETIME))
								.execute();
						// 通話時間
						csv.telTime = GourmetCareeUtil.convertSecondsToMinuteSecondStr(entity.memberTelSecond);
						// 電話番号変換
						csv.memberTel = convertIgnoreMembertel(entity.memberTel, ignoreList);
						// 通話ステータスがエラーの場合は表示を変更する
						csv.telStatusName = convertTelStatusName(entity.telStatusName);
						csvList.add(csv);
						return null;
					}
				});

			IpPhoneHistoryCsvProperty csvProperty = new IpPhoneHistoryCsvProperty();
			csvProperty.pass = getCommonProperty("gc.csv.filepass");
			csvProperty.encode = getCommonProperty("gc.csv.encoding");
			csvProperty.fileName = getCommonProperty("gc.ipPhoneHistory.csv.filename");
			ipPhoneHistoryCsvLogic.outPutCsv(csvList, csvProperty);

		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * 電話番号が無効な場合、ジェイオフィス東京に変換する
	 * @param memberTel 変換対象
	 * @param ignoreList 無効番号のリスト
	 * @return 無効番号の場合はジェイオフィス東京、そうでない場合はそのまま返す
	 */
	private String convertIgnoreMembertel(String memberTel, List<String> ignoreList) {
		return StringUtils.isNotEmpty(memberTel) && ignoreList.contains(memberTel) ? "ジェイオフィス東京" : memberTel;
	}

	/**
	 * 通話ステータスが"エラー"の場合は"不通状態"に変換する
	 * @param telStatusName 変換対象
	 * @return エラーの場合は、不通状態、そうでない場合はそのまま返す
	 */
	private String convertTelStatusName(String telStatusName) {
		// 通話ステータスがエラーの場合は表示を変更する
		return "エラー".equals(telStatusName) ? "不通状態" : telStatusName;
	}
}
