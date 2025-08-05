package com.gourmetcaree.admin.pc.webdata.form.webdata;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * WEBデータ一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 439933628733011346L;



	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** エリアコード */
	@Required
	public String areaCd;

	/** 号数ID */
	public String volumeId;

	/** 掲載開始日 */
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String postFromDate;

	/** 掲載終了日 */
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String postToDate;

	/** ステータス */
	public List<String> displayStatusList;

	/** チェックステータス */
	public String checkedStatus;

	/** サイズ区分 */
	public String sizeKbn;

	/** 業種区分 */
	public String industryKbn;

	/** 所属（会社） */
	public String companyId;

	/** 営業担当 */
	public String salesId;

	/** 顧客名 */
	public String customerName;

	/** 応募フォームフラグ */
	public String applicationFormKbn;

	/** 原稿名 */
	public String manuscriptName;

	/** 誌面号数 */
	public String magazineVolume;

	/** WEBデータID */
	public String[] webId;

	/** 特集ID */
	public String specialId;

	/** 勤務地エリア(WEBエリアから名称変更)区分 */
	public String webAreaKbn;

	/** 海外エリア区分 */
	public String foreignAreaKbn;

	/** 顧客ID */
	@IntegerType
	public String customerId;

	/** WEBデータID(検索用) */
	@IntegerType
	public String whereWebId;

	/** 鉄道会社 */
	public String railroadId;

	/** 路線 */
	public String routeId;

	/** 駅 */
	public String stationId;

	/** 注目店舗フラグ */
	public String attentionShopFlg;

	/** 検索優先フラグ */
	public String searchPreferentialFlg;

	/** キーワード */
	public String keyword;

	/** IP電話番号 */
	public String ipPhone;

	/** 掲載 */
	public String serialPublication;

	/** 店舗一覧表示区分 */
	public String shopListDisplayKbn;

	/** 店舗区分リスト*/
	public List<String> shopsKbnList;

	/** 職種リスト */
	public List<String> jobKbnList;

	/** 雇用形態リスト */
	public List<String> employPtnKbnList;

	/** 詳細エリアリスト */
	public List<String> detailAreaList;

	/** 待遇リスト */
	public List<String> treatmentList;

	/** その他条件リスト */
	public List<String> otherConditionList;

	/** WEBデータタグID */
	public String webDataTagId;

	/** PM動画 */
	public String movieFlg;

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		// ページ遷移用に選択されたページ数
		pageNum = null;
		// 最大表示件数
		maxRow = null;
		// エリアコード
		areaCd = null;
		// 号数ID
		volumeId = null;
		// 掲載開始日
		postFromDate = null;
		// 掲載終了日
		postToDate = null;
		// ステータス
		displayStatusList = null;
		// チェックステータス
		checkedStatus = null;
		// サイズ区分
		sizeKbn = null;
		// 業種区分
		industryKbn = null;
		// 所属（会社）
		companyId = null;
		// 営業担当
		salesId = null;
		// 顧客名
		customerName = null;
		// 応募フォームフラグ
		applicationFormKbn = null;
		// 原稿名
		manuscriptName = null;
		// 誌面号数
		magazineVolume = null;
		// WEBデータID
		webId = null;
		// 特集ID
		specialId = null;
		// 勤務地エリア
		webAreaKbn = null;
		// 海外エリア
		foreignAreaKbn = null;
		// 顧客ID
		customerId = null;
		// WEBデータID
		whereWebId = null;
		// 鉄道会社
		railroadId = null;
		// 路線
		routeId = null;
		// 駅
		stationId = null;
		// 注目店舗フラグ
		attentionShopFlg = null;
		// 検索優先フラグ
		searchPreferentialFlg = null;
		// キーワード
		keyword = null;
		// 掲載区分
		serialPublication = null;
		// 店舗一覧表示区分
		shopListDisplayKbn = null;
		// IP電話番号
		ipPhone = null;

		shopsKbnList = null;
		jobKbnList = null;
		employPtnKbnList = null;
		detailAreaList = null;
		treatmentList = null;
		otherConditionList = null;

		webDataTagId = null;

		movieFlg = null;
	}

	/**
	 * 画面表示を非表示にする
	 */
	public void resetSearch() {

		setExistDataFlgNg();

		/** ステータス */
		this.displayStatusList = null;
		foreignAreaKbn = null;

		webId = null;

		shopsKbnList = null;
		jobKbnList = null;
		employPtnKbnList = null;
		detailAreaList = null;
		treatmentList = null;
		otherConditionList = null;
		movieFlg = null;
	}

	/**
	 * WEBデータIDをリセットする
	 */
	public void resetId() {
		webId = null;
	}

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 日付が入力されていればチェックする
		if (!StringUtil.isEmpty(this.postFromDate) && !StringUtil.isEmpty(this.postToDate)) {
			try {
				// 開始日より終了日が先の場合エラー（同じ日付もエラー）
				if (DateUtils.compareDateTime(this.postFromDate, this.postToDate) < 0) {
					// 「{掲載期間(開始日)}には{掲載期間(終了日)}より前の日時を入力してください。」
					errors.add("errors", new ActionMessage("errors.app.TermSet",
							MessageResourcesUtil.getMessage("labels.postFromDate"), MessageResourcesUtil.getMessage("labels.postToDate")));
				}
			} catch (ParseException e) {
				// 「日付を正しく入力してください。」
				errors.add("errors", new ActionMessage("errors.app.dateFailed"));
			}
		}

		return errors;
	}

	/**
	 *  独自チェックを行う(一括処理用)
	 * @return ActionMessages
	 */
	public ActionMessages lumpValidate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// WEBデータが選択されているかチェック
		if (webId == null || webId.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckWebData"));
		} else {
			for (String id : webId) {
				if (!StringUtils.isNumeric(id)) {
					errors.add("errors", new ActionMessage("errors.app.failGetWebId"));
					break;
				}
			}
		}

		return errors;
	}

	/**
	 * 勤務地エリア区分の変換
	 * 入力されたエリアが海外のものの場合、勤務地エリアを消して海外エリアに代入。
	 */
	public void convertWebAreaKbnToForeignAreaKbn() {
		if (StringUtils.isBlank(webAreaKbn)) {
			return;
		}

		if (MTypeConstants.ForeignAreaKbn.isForeignArea(NumberUtils.toInt(webAreaKbn), NumberUtils.toInt(areaCd))) {
			foreignAreaKbn = webAreaKbn;
			webAreaKbn = null;
		}
	}

	/**
	 * 勤務地エリア区分の変換
	 * 入力されたエリアが海外のものの場合、勤務地エリアを消して海外エリアに代入。
	 */
	public void convertForeignAreaKbnToWebAreaKbn() {
		if (StringUtils.isBlank(foreignAreaKbn)) {
			return;
		}
			webAreaKbn = foreignAreaKbn;
			foreignAreaKbn = null;
	}
}
