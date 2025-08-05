package com.gourmetcaree.shop.pc.pattern.action.pattern;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.PatternService;
import com.gourmetcaree.shop.logic.logic.PatternLogic;
import com.gourmetcaree.shop.pc.pattern.dto.pattern.ListDto;
import com.gourmetcaree.shop.pc.pattern.form.pattern.ListForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

/**
 * 定型文一覧を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class ListAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 定型文一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 定型文ロジック */
	@Resource
	protected PatternLogic patternLogic;

	/** 定型文一覧サービス */
	@Resource
	protected PatternService patternService;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Pattern.JSP_SPF01L01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		listForm.list = createDisplayValue(getData());

		// 画面を表示する
		listForm.setExistDataFlgOk();
		checkUnReadMail();

		// 一覧画面へ遷移
		return TransitionConstants.Pattern.JSP_SPF01L01;
	}

	/**
	 * 定型文マスタからデータを取得する
	 */
	private List<MSentence> getData() {

		List<MSentence> entityList = null;

		try{
			// データの取得
			entityList = patternLogic.getSentenceList();

			log.debug("一覧データを取得しました。" + listForm);

			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("一覧データを取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.debug("一覧データを取得しました。営業ID：" + userDto.masqueradeUserId + " " + listForm);
			}

		// データが存在しない場合
		} catch (WNoResultException e) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「{定型文}は現在登録されていません。」
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.sentence"));
		}

		if (entityList == null || entityList.isEmpty()) {
			// リストが空の場合はエラーメッセージを返す「{定型文}は現在登録されていません。」
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.sentence"));
		}

		// 取得したリストを返却
		return entityList;

	}

	/**
	 * 検索結果を画面表示のDtoリストに設定する
	 * @param entityList MSentenceエンティティのリスト
	 * @return Dtoのリスト
	 */
	private List<ListDto> createDisplayValue(List<MSentence> entityList) {

		List<ListDto> retList = new ArrayList<ListDto>();

		//m_sentenceから情報を取得してdtoを作成
		for (MSentence entity : entityList) {

			// 取得した値をdtoにセット
			ListDto dto = new ListDto();
			BeanUtil.copyProperties(entity, dto);

			// 詳細画面のパスをセット
			dto.detailPath = GourmetCareeUtil.makePath(TransitionConstants.Pattern.ACTION_PATTERN_DETAIL_INDEX, String.valueOf(dto.id));

			retList.add(dto);
		}

		return retList;
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.patternInstance();
	}

	/**
	 * 定型文を一括削除する
	 * @return 定型文一覧再表示のパス
	 */
	@Execute(validator = false, validate = "validateForLump", reset = "resetMultiBox", input = TransitionConstants.Pattern.PATTERN_LIST_RE_SHOW_LIST_PATH)
	public String doLumpDelete() {
		checkArgsNull(NO_BLANK_FLG_NG, String.valueOf(userDto.getCustomerCd()));
		try {
			patternService.lumpDelete(userDto.getCustomerCd(), listForm.changeIdArray);
			listForm.resetMultiBox();
			return "/pattern/list/reShowList?redirect=true";
		} catch (NumberFormatException e) {
			listForm.resetMultiBox();
			throw new FraudulentProcessException();
		}
	}

	/**
	 * 定型文一覧を再表示する
	 * @return 一覧画面のパス
	 */
	@Execute(validator = false, input = TransitionConstants.Pattern.JSP_SPF01L01)
	public String reShowList() {
		return show();
	}

}
