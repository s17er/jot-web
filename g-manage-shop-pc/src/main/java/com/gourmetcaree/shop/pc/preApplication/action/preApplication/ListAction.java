package com.gourmetcaree.shop.pc.preApplication.action.preApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MemberAttributeService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;
import com.gourmetcaree.shop.logic.dto.PreApplicationListRetDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.ApplicationSearchProperty;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.pc.application.form.application.ListForm;
import com.gourmetcaree.shop.pc.application.form.application.ListForm.ListDisplayKbn;
import com.gourmetcaree.shop.pc.application.form.applicationMail.InputForm;
import com.gourmetcaree.shop.pc.preApplication.dto.preApplication.PreApplicationListDto;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;
import net.arnx.jsonic.JSON;


/**
 * プレ応募者一覧を表示するアクションクラスです。
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	/** 応募者一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 応募サービス */
	@Resource
	protected PreApplicationService preApplicationService;

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** 会員属性サービス */
	@Resource
	protected MemberAttributeService memberAttributeService;

	/** 応募を保持するリスト */
	public List<PreApplicationListDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	@Resource
	public InputForm inputForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPP01L01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		listForm.listDisplayKbn = ListDisplayKbn.ALL;

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPP01L01;
	}

	/**
	 * WEBデータで絞った状態で一覧を表示します
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "focusWebdata/{webId}", input = TransitionConstants.Application.JSP_SPP01L01)
	public String focusWebdata() {

		listForm.listDisplayKbn = ListDisplayKbn.WEB_DATA;
		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPP01L01;
	}

	/**
	 * 応募者の検索を行う。
	 * 一覧から通常のサブミットでアクセスされます。
	 * 処理後はリダイレクトで再表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPP01L01)
	public String doSearch() {

		createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.Application.JSP_SPP01L01;
	}

	/**
	 * 別画面から再び一覧に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 */
	@Execute(validator=false, input =TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String searchAgain() {
		// 表示リストを取得
		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPP01L01;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP01L01)
	public String showList() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		createList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPP01L01;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();


		// ページの設定
		listForm.pageNum = String.valueOf(targetPage);

		List<PreApplicationListDto> tmpList = new ArrayList<PreApplicationListDto>();

		try {
			PreApplicationListRetDto retDto;

			ApplicationSearchProperty searchProperty = new ApplicationSearchProperty();

			if (listForm.listDisplayKbn == ListDisplayKbn.WEB_DATA) {
				searchProperty.webId = NumberUtils.toInt(listForm.webId);
			} else {
				searchProperty.keyword = listForm.search;
			}

			retDto = preApplicationLogic.getPreApplicationListData(property, searchProperty);

			addPreApplicationListDto(retDto, tmpList);

			dataList = tmpList;
			pageNavi = retDto.pageNavi;

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			dataList = tmpList;
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * プレ応募者一覧のDTO
	 * @param retDto
	 * @param list
	 */
	private void addPreApplicationListDto(PreApplicationListRetDto retDto, List<PreApplicationListDto> list) {

		List<String> lumpSendIdList;
		if (StringUtils.isBlank(listForm.lumpSendIds)) {
			lumpSendIdList = new ArrayList<String>(0);
		} else {
			lumpSendIdList = Arrays.asList(listForm.lumpSendIds.split(GourmetCareeConstants.KANMA_STR));
		}
		//不正な値を防ぐため値をチェック。
		Map<Integer, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP;
		for (TPreApplication entity : retDto.retList) {

			PreApplicationListDto dto = new PreApplicationListDto();
			Beans.copy(entity, dto).execute();

			if(entity.selectionFlg == null) {
				dto.selectionFlg = STATUS_DEFAULT_VALUE;
			}

			//応募にたいして未読メールが存在するかどうかを取得。
			dto.unopenedMailFlg = preApplicationLogic.isUnopenedApplicantMailExist(entity.id);

			if (StringUtils.isNotBlank(selectionMap.get(dto.selectionFlg))) {
				dto.selectionFlgColor = selectionMap.get(dto.selectionFlg);
			} else {
				dto.selectionFlg = STATUS_DEFAULT_VALUE;
				dto.selectionFlgColor = MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
			}
			dto.containsCheckedFlg = lumpSendIdList.contains(String.valueOf(entity.id));

			dto.industryKbnList = memberAttributeService.getMemberAttributeValueStringList(entity.memberId, MTypeConstants.IndustryKbn.TYPE_CD);
			dto.jobKbnList = memberAttributeService.getMemberAttributeValueStringList(entity.memberId, MTypeConstants.JobKbn.TYPE_CD);
			dto.employPtnKbnList = memberAttributeService.getMemberAttributeValueStringList(entity.memberId, MTypeConstants.EmployPtnKbn.TYPE_CD);

			try {
				 dto.memberStatusDto = memberService.getMemberStatusDto(entity.memberId, entity.customerId);
			} catch (WNoResultException e) {
				dto.memberStatusDto = new MemberStatusDto();
			}

			list.add(dto);
		}

	}

	/**
	 * ページ遷移
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPP01L01)
	public String changePage() {

		createList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPP01L01;
	}

	/**
	 * 応募者のメモを編集する。
	 * 一覧から通常のサブミットでアクセスされます。
	 * 処理後はリダイレクトで再表示
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP01L01)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TPreApplication entity = new TPreApplication();
		Beans.copy(listForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		preApplicationService.update(entity);

		return String.format("/preApplication/list/changePage/%s%s", StringUtils.defaultIfEmpty(listForm.pageNum, "1"), GourmetCareeConstants.REDIRECT_STR );
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPP01L01)
	public String ajaxSelectionFlg() {

		TPreApplication entity = new TPreApplication();
		Beans.copy(listForm, entity).execute();

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;

		if (StringUtils.isNotBlank(selectionMap.get(listForm.selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(listForm.selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}

		preApplicationService.updateIncludesVersion(entity);


		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.application.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.PRE_APPLICATION_MAIL;
	}

}
