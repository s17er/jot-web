package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebAccessCounter;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.entity.VMaterialNoData;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.InterestService;
import com.gourmetcaree.db.common.service.MaterialNoDataService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService;
import com.gourmetcaree.db.common.service.WebJobService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.dto.WebdataDto;
import com.gourmetcaree.shop.logic.property.WebAccessCounterProperty;
import com.gourmetcaree.shop.logic.property.WebdataProperty;

/**
 * WEBデータに関するロジッククラスです。
 * @author Makoto Otani
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class WebdataLogic extends AbstractShopLogic {

	/** 求人原稿の表示期間 */
	private static final int YEAR = -1;

	/** WEBリストビューサービス */
	@Resource
	protected WebListService webListService;

	/** 応募サービス */
	@Resource
	protected ApplicationService applicationService;

	/** データ本体をもたない素材Viewサービス */
	@Resource
	private MaterialNoDataService materialNoDataService;

	@Resource
	private WebService webService;

	@Resource
	private WebJobService webJobService;

	/** IP電話応募履歴サービス */
	@Resource
	private WebIpPhoneHistoryService webIpPhoneHistoryService;

	@Resource
	private InterestService interestService;

	@Resource
	private PreApplicationService preApplicationService;

	/** 原稿状態のパラメータ */
	private static final String STATUS_PARAM = "narr_mail";

	/** エリアのパラメータ */
	private static final String AREA_PARAM = "narr_area";

	/** 原稿サイズのパラメータ */
	private static final String SIZE_PARAM = "narr_size";

	/**
	 * 求人原稿一覧のデータを取得します。
	 * @return 求人原稿一覧リスト
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<VWebList> getWebdataList(PageNavigateHelper pageNavi, int targetPage, Map<String,String[]> parameterMap) throws WNoResultException {

		// 検索条件の設定
		Where where = createListWhere(parameterMap);

		// 件数を検索
		int count = (int) webListService.countRecords(where);

		//ページナビゲータのセット
		pageNavi.changeAllCount(count);

		// 検索結果が0件の場合エラー（ページに0をセットしてからエラー)
		if (count == 0) {
			throw new WNoResultException();
		}

		// 対象ページ
		pageNavi.setPage(targetPage);
		// 表示順
		pageNavi.setSortKey(createListSort());

//		表示件数セット
		pageNavi.minDisp = pageNavi.getMinDispNum();
		pageNavi.maxDisp = pageNavi.getMaxDispNum();

		// データの取得
		return webListService.findByCondition(where, pageNavi);

	}

	/**
	 * 掲載中の求人原稿一覧データを取得
	 * @return
	 * @throws WNoResultException
	 */
	public List<VWebList> getPublishWebdataList() throws WNoResultException {

		SimpleWhere where = new SimpleWhere();

		where.eq(camelize(VWebList.CUSTOMER_ID), getCustomerId());
		where.eq(camelize(VWebList.DISPLAY_STATUS), MStatusConstants.DisplayStatusCd.POST_DURING);
		where.ge(camelize(VWebList.POST_END_DATETIME), Calendar.getInstance());

		return webListService.findByCondition(where, camelize(VWebList.POST_START_DATETIME) + " desc");
	}

	/**
	 * 求人原稿一覧の検索条件を返します。
	 * @return 求人原稿一覧の検索条件
	 * @throws WNoResultException
	 */
	private Where createListWhere(Map<String,String[]> parameterMap) throws WNoResultException {

		//現在の日付から一年前の日を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, NumberUtils.toInt(getCommonProperty("gc.webdate.show.year"), YEAR));

		SimpleWhere where = new SimpleWhere();

		// 顧客ID = ログイン顧客
		where.eq(camelize(VWebList.CUSTOMER_ID), getCustomerId());
		// 表示ステータス in  掲載中と、掲載終了
		// 掲載終了日 < 指定日
		where.ge(camelize(VWebList.POST_END_DATETIME), cal);

		// 検索条件を追加
		if(!MapUtils.isEmpty(parameterMap)) {
			String[] status = checkIntParameter(parameterMap.get("where_webStatus"));
			String[] area = checkIntParameter(parameterMap.get("where_areaCd"));
			String[] size = checkIntParameter(parameterMap.get("where_sizeKbn"));
			if(area != null) {
				where.in(camelize(VWebList.AREA_CD), area);
			}
			if(size != null) {
				where.in(camelize(VWebList.SIZE_KBN), size);
			}
			if(status == null) {
				// 表示ステータス in  掲載中と、掲載終了
				where.in(camelize(VWebList.DISPLAY_STATUS), MStatusConstants.DisplayStatusCd.POST_DURING,
															MStatusConstants.DisplayStatusCd.POST_END);
			}else {
				where.in(camelize(VWebList.DISPLAY_STATUS), status);
			}
		}else {
			// 表示ステータス in  掲載中と、掲載終了
			where.in(camelize(VWebList.DISPLAY_STATUS), MStatusConstants.DisplayStatusCd.POST_DURING,
														MStatusConstants.DisplayStatusCd.POST_END);
		}

		return where;
	}

	/**
	 * 求人原稿一覧の表示順を返します。
	 * @return 求人原稿一覧の表示順
	 */
	private String createListSort() {

		String[] sortKey = new String[] {
				// ソート順を設定
				asc( camelize(VWebList.DISPLAY_STATUS)),		// 表示ステータス（昇順）
				desc(camelize(VWebList.POST_START_DATETIME)),	// 掲載開始日（降順）
				desc(camelize(VWebList.SIZE_KBN)),				// サイズ区分（降順）
				desc(camelize(VWebList.ID))						// WEBリストビュー.ID（降順）
			};

		// カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * 求人原稿一覧を表示するために、エンティティを画面表示Dtoに詰め替えます。<br />
	 * プロパティのWEBデータ一覧エンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 画面表示のDto
	 */
	public List<WebdataDto> createDisplayList(WebdataProperty property) {

		// 引数チェック
		checkEmptyProperty(property);
		if (property.vWebListList == null || property.vWebListList.isEmpty()) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		List<WebdataDto> dtoList = new ArrayList<>();

		// 表示Dtoにセット
		for (VWebList entity : property.vWebListList) {

			WebdataDto dto = new WebdataDto();
			// データをコピー
			Beans.copy(entity, dto).execute();

			// 応募の件数を取得してセットする
			dto.applicationCount = getAppCount(entity.id);

			// アクセスカウントをセットする
			StringBuilder sql = new StringBuilder("");
			List<Object> params = new ArrayList<>();
			WebAccessCounterProperty webAccessProperty = createWebAccessCounterProperty(entity.id);
			createAccessCountSql(sql, params, webAccessProperty);

			TWeb tWeb = webService.findById(entity.id);
			if (tWeb != null) {
				dto.renewalFlg = tWeb.renewalFlg == 1 ? true : false;
				// 登録日時を追加
				dto.insertDateime = tWeb.insertDatetime;
			} else {
				dto.renewalFlg = false;
			}

			dto.allAccessCount = getWebAccessCount(webAccessProperty, sql, params);
			dto.phoneApplicationCount = webIpPhoneHistoryService.getIpPhoneApplicationCount(entity.id);
			dto.preApplicationCount = preApplicationService.getPreApplicationCount(entity.id);


			// リストにセット
			dtoList.add(dto);
		}

		// 画面表示用のDtoリストを返却
		return dtoList;
	}

	/**
	 * 応募テーブルから件数を取得します。
	 * @param id WEBデータID
	 * @return 件数
	 */
	private int getAppCount(int id) {

		SimpleWhere where = new SimpleWhere();
		// WEBID
		where.eq(camelize(TApplication.WEB_ID), id);
		// いたずら応募以外
		where.eq(camelize(TApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL);
		// 削除フラグ
		where.eq(camelize(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		// 検索結果を返却
		return (int) applicationService.countRecords(where);

	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(WebdataProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * 画像のユニークキーを保持するMapを取得します。
	 * @param webId
	 * @return
	 */
	public Map<String, String> getImageUniqueKeyMap(int webId) {
		Map<String, String> imageCheckMap = new HashMap<String, String>();

		try {
			List<VMaterialNoData> retList = materialNoDataService.getMaterialList(webId);

			for (VMaterialNoData entity : retList) {
				imageCheckMap.put(Integer.toString(entity.materialKbn), GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
			}

			return imageCheckMap;
		} catch (WNoResultException e) {
			return  new HashMap<String, String>();
		}
	}

	public List<TWebJob> getRecruitedList() {
		try {
			List<VWebList> webList = webListService.getVWebListInThePastYearByCustomerIdStatuses(getCustomerId(),
					new ArrayList<Integer>(Arrays.asList(MStatusConstants.DisplayStatusCd.POST_DURING,
							MStatusConstants.DisplayStatusCd.POST_END))
					, "id");

			List<TWebJob> list = new ArrayList<>();

			for(VWebList w : webList) {
				list.addAll(webJobService.findByWebId(w.id));
			}

			return list;
		} catch (WNoResultException e) {
			return new ArrayList<>();
		}
	}

	private void createAccessCountSql(StringBuilder sb, List<Object> params, WebAccessCounterProperty property) {
		sb.append(" SELECT ");
		sb.append("    COUNTER.* ");
		sb.append(" FROM ");
		sb.append("    t_web_access_counter COUNTER ");
		sb.append("    INNER JOIN t_web WEB ON WEB.id = COUNTER.web_id AND WEB.delete_flg = ? ");
		sb.append("    INNER JOIN m_volume VOL ON VOL.id = WEB.volume_id AND VOL.delete_flg = ?");
		sb.append(" WHERE ");
		sb.append("      COUNTER.web_id = ?");
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(property.webId);

		if (property.terminalKbn != null && property.terminalKbn != 0) {
			sb.append(" COUNTER.terminal_kbn = ? ");
			params.add(property.terminalKbn);
		}
	}

	private int getWebAccessCount(WebAccessCounterProperty property, StringBuilder sql, List<Object> params) {
		int count = 0;
		try {
			List<TWebAccessCounter> entityList = jdbcManager.selectBySql(TWebAccessCounter.class, sql.toString(), params.toArray())
															.disallowNoResult()
															.getResultList();

			for (TWebAccessCounter entity : entityList) {
				count += entity.accessCount;
			}

			return count;
		} catch (SNoResultException e) {
			return 0;
		}
	}

	private WebAccessCounterProperty createWebAccessCounterProperty(Integer webId) {
		return createWebAccessCounterProperty(webId, 0);
	}

	private WebAccessCounterProperty createWebAccessCounterProperty(Integer webId, Integer terminalKbn) {
		WebAccessCounterProperty property = new WebAccessCounterProperty();
		property.webId =  webId;
		property.terminalKbn = terminalKbn;
		property.areaCd = getAreaCd();
		return property;
	}

	/**
	 * 渡されたパラメーターが数値がどうかチェックする　数値以外が入っていたらNullを返す
	 * @param param
	 * @return パラメーターかNull
	 * @throws WNoResultException
	 */
	private String[] checkIntParameter(String[] param) throws WNoResultException {
		if(param == null) {
			return param;
		}
		for(String value : param) {
//			数値以外が入っていれば例外をスロー
			if(!NumberUtils.isNumber(value)) {
				throw new WNoResultException();
			}
		}
		return param;

	}
}
