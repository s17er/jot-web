package com.gourmetcaree.admin.service.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static org.seasar.framework.util.StringUtil.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.admin.service.connection.MailMagazineConnection;
import com.gourmetcaree.admin.service.property.MailMagazineProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.CsvUtil;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.DeliveryFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.DeliveryKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.ErrorFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailmagazineKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.TerminalKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.UserKbn;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.entity.TMailMagazineDelivery;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AreaService;
import com.gourmetcaree.db.common.service.MailMagazineDeliveryService;
import com.gourmetcaree.db.common.service.MailMagazineDetailService;
import com.gourmetcaree.db.common.service.MailMagazineService;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * メルマガに関するロジッククラスです。
 * @author Makoto Otani
 * @version 1.0
 *
 */
public class MailMagazineLogic extends AbstractAdminLogic {

	/** メルマガサービス */
	@Resource
	protected MailMagazineService mailMagazineService;

	/** メルマガ詳細サービス */
	@Resource
	protected MailMagazineDetailService mailMagazineDetailService;

	/** メルマガ配信先サービス */
	@Resource
	protected MailMagazineDeliveryService mailMagazineDeliveryService;

	/** 区分マスタサービス */
	@Resource
	protected TypeService typeService;

	/** エリアマスタサービス */
	@Resource
	protected AreaService areaService;

	/** 区分マスタの値を保持するMap（Map<"typeCd", Map<"typeValue", "typeName">） */
	private Map<String, Map<Integer, String>> mTypeMap = new HashMap<String, Map<Integer,String>>();

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(MailMagazineLogic.class);

	/**
	 * メルマガのデータを登録します。<br />
	 * プロパティのメルマガエンティティに、登録するメルマガ、メルマガ詳細、メルマガ配信先をセットして呼び出します。
	 * @param property メルマガプロパティ
	 * @return 登録したメルマガID
	 */
	public Integer insertMailMag(MailMagazineProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// メルマガの登録
		insertMailMagazine(property.mailMagazine);

		// メルマガ詳細の登録
		insertMailMagazineDetail(property.mailMagazine.id, property.mailMagazine.tMailMagazineDetailList);

		// メルマガ配信先の登録
		insertMailMagazineDelivery(property.mailMagazine.id, property.mailMagazine.tMailMagazineDeliveryList);

		// 登録したメルマガIDを返却
		return property.mailMagazine.id;
	}

	/**
	 * メルマガテーブルにデータを登録します。
	 * @param entity メルマガエンティティ
	 */
	private void insertMailMagazine(TMailMagazine entity) {

		// 登録日時
		entity.registrationDatetime = new Date();
		// 配信予定日時がnullの場合は、登録日時をセット
		if (entity.deliveryScheduleDatetime == null) {
			entity.deliveryScheduleDatetime = entity.registrationDatetime;
		}
		// 削除フラグ
		entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

		// データの登録
		mailMagazineService.insert(entity);
	}

	/**
	 * メルマガ詳細テーブルにデータを登録します。
	 * @param mailMagazineId メルマガID
	 * @param entityList メルマガ詳細エンティティリスト
	 */
	private void insertMailMagazineDetail(Integer mailMagazineId, List<TMailMagazineDetail> entityList) {

		// メルマガ詳細テーブルに登録するエンティティに値をセット
		for (TMailMagazineDetail entity : entityList) {
				entity.mailMagazineId = mailMagazineId;
				entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
				//entity.body = GourmetCareeUtil.makeUrlLink(entity.body);
		}
		// データの登録
		mailMagazineDetailService.insertBatch(entityList);
	}

	/**
	 * メルマガ配信先テーブルにデータを登録します。
	 * @param mailMagazineId メルマガID
	 * @param entityList メルマガ配信先エンティティリスト
	 */
	private void insertMailMagazineDelivery(Integer mailMagazineId, List<TMailMagazineDelivery> entityList) {

		// メルマガ配信先テーブルに登録するエンティティに値をセット
		for (TMailMagazineDelivery entity : entityList) {
			// メルマガID
			entity.mailMagazineId = mailMagazineId;
			// 配信フラグ(未配信)
			entity.deliveryFlg = MTypeConstants.DeliveryFlg.NON;
			// エラーフラグ(エラー無)
			entity.errorFlg = MTypeConstants.ErrorFlg.NON;
			// 削除フラグ
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		}

		// データの登録
		mailMagazineDeliveryService.insertBatch(entityList);
	}

	/**
	 * メルマガ一覧のデータを取得します。<br />
	 * メルマガプロパティに、検索条件、ページナビをセットして呼び出します。
	 * @param property メルマガプロパティ
	 * @return メルマガ一覧
	 * @throws WNoResultException データが無い場合はエラー
	 * @throws ParseException 日付が不正な場合のエラー
	 */
	public List<TMailMagazine> getMailMagazineList(MailMagazineProperty property) throws WNoResultException, ParseException {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// 検索条件の設定
		StringBuilder whereStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		createListWhere(property, whereStr, params);

		// レコードの件数を取得
		int count = (int) mailMagazineService.countRecords(whereStr.toString(), params.toArray());

		// ページ件数のセット
		property.pageNavi.changeAllCount(count);

		// 件数がない場合は処理しない
		if (count < 1) {
			throw new WNoResultException("メルマガ一覧にデータがありません。");
		}

		// 現在のページをセット
		property.pageNavi.setPage(property.targetPage);
		// ソート順セット
		property.pageNavi.setSortKey(createSort());

		// メルマガリストの取得
		return mailMagazineService.findByCondition(whereStr.toString(), params.toArray(), property.pageNavi);
	}

	/**
	 * メルマガ一覧の条件を返します。
	 * @param property メルマガプロパティ
	 * @param whereStr 検索条件
	 * @param params 値
	 * @throws ParseException 日付が不正な場合はエラー
	 */
	private void createListWhere(MailMagazineProperty property, StringBuilder whereStr, List<Object> params) throws ParseException {

		// 削除フラグ
		whereStr.append(eq(TMailMagazine.DELETE_FLG));
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 配信開始日
		if (property.deliveryStartDate != null) {
			whereStr.append(andGe(TMailMagazine.DELIVERY_DATETIME));
			params.add(DateUtils.getStartDatetime(property.deliveryStartDate));
		}
		// 配信終了日
		if (property.deliveryEndDate != null) {
			whereStr.append(andLe(TMailMagazine.DELIVERY_DATETIME));
			params.add(DateUtils.getEndDatetime(property.deliveryEndDate));
		}
		// 配信先区分
		if (property.deliveryKbn != null) {
			whereStr.append(andEq(TMailMagazine.DELIVERY_KBN));
			params.add(property.deliveryKbn);
		}

		// メルマガ区分(固定)
		whereStr.append(andEq(TMailMagazine.MAILMAGAZINE_KBN));
		params.add(MailmagazineKbn.CUSTOMER_MEMEBER);

		// タイトル
		if (StringUtil.isNotBlank(property.mailMagazinetitle)) {

			whereStr.append("  AND EXISTS( ");
			whereStr.append("    SELECT");
			whereStr.append("      * ");
			whereStr.append("    FROM");
			whereStr.append("      t_mail_magazine_detail ");
			whereStr.append("    WHERE");
			whereStr.append("        mail_magazine_id = T1_.id ");
			whereStr.append("    AND mail_magazine_title like ?");
			whereStr.append("    AND delete_flg = ?");
			whereStr.append("  ) ");

			params.add(containPercent(property.mailMagazinetitle));
			params.add(DeleteFlgKbn.NOT_DELETED);
		}
	}

	/**
	 * メルマガ一覧のソート順を返します。
	 * @return メルマガのソート順
	 */
	private String createSort(){

		String[] sortKey = new String[] {
			// ソート順を設定
			desc(camelize(TMailMagazine.ID))	// メルマガ.ID

		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * メルマガ詳細のデータを取得すます。
	 * @param property メルマガプロパティ
	 * @return メルマガ詳細用のメルマガエンティティ
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public TMailMagazine getMailMagazineDetail(MailMagazineProperty property, boolean headerFlg) throws WNoResultException {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// メール詳細テーブルの削除フラグ
		SimpleWhere where = new SimpleWhere();
		where.eq(dot(camelize(TMailMagazine.T_MAIL_MAGAZINE_DETAIL_LIST), camelize(TMailMagazineDetail.DELETE_FLG)), DeleteFlgKbn.NOT_DELETED);
		if(headerFlg) {
			where.eq(camelize(TMailMagazine.MAILMAGAZINE_KBN), MailmagazineKbn.NEW_INFORMATION);
		} else {
			where.eq(camelize(TMailMagazine.MAILMAGAZINE_KBN), MailmagazineKbn.CUSTOMER_MEMEBER);
		}


		// メルマガ詳細データを結合してデータを取得
		return mailMagazineService.findByIdInnerJoin(camelize(TMailMagazine.T_MAIL_MAGAZINE_DETAIL_LIST), property.mailMagazineId, where, createCsvSort());
	}

	/**
	 * メルマガ詳細のソート順を返します。
	 * @return メルマガ詳細のソート順
	 */
	private String createCsvSort(){

		String[] sortKey = new String[] {
			// ソート順を設定
			desc(camelize(TMailMagazine.ID)),	// メルマガ.ID
			asc(dot(camelize(TMailMagazine.T_MAIL_MAGAZINE_DETAIL_LIST), camelize(TMailMagazineDetail.TERMINAL_KBN)))	// メルマガ詳細.端末区分
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * メルマガCSVを出力します。
	 * @param property メルマガプロパティ
	 * @return 出力行
	 * @throws WNoResultException
	 * @throws IOException
	 */
	public int outPutCsv(MailMagazineProperty property) throws WNoResultException, IOException {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

	    // 詳細データの取得
		TMailMagazine tMailMagazine = getMailMagazineDetail(property, false);
		// 配信先データの取得
		List<TMailMagazineDelivery> tMailMagazineDeliveryList;
		try {
			tMailMagazineDeliveryList = mailMagazineDeliveryService.findByMailMagazineIdDelivery(property.mailMagazineId);
		// 配信先が取得できない場合は、リストを空にしておく
		} catch (WNoResultException e) {
			tMailMagazineDeliveryList = new ArrayList<TMailMagazineDelivery>(0);
		}

		// エリア情報を保持するMap
		Map<Integer, String> areaMap = areaService.getAreaNameMap();

		// ファイルをオープンする
		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createCsvFileName());
	    PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), getCsvEncode()));

	    try {
			int count = 0;

			// メルマガヘッダーを出力
			out.println(getCsvHeader());

			// メルマガの値をセット
			List<String> mailMagazineValueList = new ArrayList<String>(5);
			// ID
			mailMagazineValueList.add(tMailMagazine.id == null ? "" : String.valueOf(tMailMagazine.id));
			// 登録日時
			mailMagazineValueList.add(tMailMagazine.registrationDatetime == null ? "" : DateUtils.getDateStr(tMailMagazine.registrationDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
			// 配信日時
			mailMagazineValueList.add(tMailMagazine.deliveryDatetime == null ? "" : DateUtils.getDateStr(tMailMagazine.deliveryDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
			// 配信先区分
			mailMagazineValueList.add(tMailMagazine.deliveryKbn == null ? "" : getTypeName(DeliveryKbn.TYPE_CD, tMailMagazine.deliveryKbn));
			// 配信先フラグ
			mailMagazineValueList.add(tMailMagazine.deliveryFlg == null ? "" : getTypeName(DeliveryFlg.TYPE_CD, tMailMagazine.deliveryFlg));

			//カンマ区切りにしてセットする
			out.println(CsvUtil.createDelimiterStr(mailMagazineValueList));
			count++;

			// 改行
			out.println();
			count++;

			// メルマガ詳細ヘッダーを出力
			out.println(getCsvDetailHeader());

			// メルマガ詳細の値をセット
			for (TMailMagazineDetail tMailMagazineDetail : tMailMagazine.tMailMagazineDetailList) {

				List<String> detailValueList = new ArrayList<String>(3);

				// 端末区分
				detailValueList.add(tMailMagazineDetail.terminalKbn == null ? "" : getTypeName(TerminalKbn.TYPE_CD, tMailMagazineDetail.terminalKbn));
				// メルマガタイトル
				detailValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDetail.mailMagazineTitle, ""));
				// 本文
				detailValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDetail.body, ""));

				//カンマ区切りにしてセットする
				out.println(CsvUtil.createDelimiterStr(detailValueList));
				count++;
			}

			// 改行
			out.println();

			// メルマガ配信先ヘッダーを出力
			out.println(getCsvDeliveryHeader());

			// メルマガ配信先の値をセット
			for (TMailMagazineDelivery tMailMagazineDelivery : tMailMagazineDeliveryList) {

				List<String> DeliveryValueList = new ArrayList<String>(10);
				// エリアコード
				DeliveryValueList.add(tMailMagazineDelivery.areaCd == null ? "" : areaMap.get(tMailMagazineDelivery.areaCd));
				// ユーザー区分
				DeliveryValueList.add(tMailMagazineDelivery.userKbn == null ? "" : getTypeName(UserKbn.TYPE_CD, tMailMagazineDelivery.userKbn));
				// 配信先
				DeliveryValueList.add(tMailMagazineDelivery.deliveryId == null ? "" : String.valueOf(tMailMagazineDelivery.deliveryId));
				// 配信先名
				DeliveryValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDelivery.deliveryName, ""));
				// 端末区分
				DeliveryValueList.add(tMailMagazineDelivery.terminalKbn == null ? "" : getTypeName(TerminalKbn.TYPE_CD, tMailMagazineDelivery.terminalKbn));
				// メールアドレス
				DeliveryValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDelivery.mail, ""));
				// 配信フラグ
				DeliveryValueList.add(tMailMagazineDelivery.deliveryFlg == null ? "" : getTypeName(DeliveryFlg.TYPE_CD, tMailMagazineDelivery.deliveryFlg));
				// 配信日時
				DeliveryValueList.add(tMailMagazineDelivery.deliveryDatetime == null ? "" : DateUtils.getDateStr(tMailMagazineDelivery.deliveryDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
				// エラーフラグ
				DeliveryValueList.add(tMailMagazineDelivery.errorFlg == null ? "" : getTypeName(ErrorFlg.TYPE_CD, tMailMagazineDelivery.errorFlg));
				// エラー受信日時
				DeliveryValueList.add(tMailMagazineDelivery.errorDatetime == null ? "" : DateUtils.getDateStr(tMailMagazineDelivery.errorDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
				// 備考
				DeliveryValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDelivery.note, ""));

				//カンマ区切りにしてセットする
				out.println(CsvUtil.createDelimiterStr(DeliveryValueList));
				count++;
			}

			return count;

	    } finally {
			out.close();
		}
	}

	/**
	 * メルマガCSVを出力します。
	 * @param property メルマガプロパティ
	 * @return 出力行
	 * @throws WNoResultException
	 * @throws IOException
	 */
	public int outPutHeaderCsv(MailMagazineProperty property) throws WNoResultException, IOException {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

	    // 詳細データの取得
		TMailMagazine tMailMagazine = getMailMagazineDetail(property, true);
		// 配信先データの取得
		List<TMailMagazineDelivery> tMailMagazineDeliveryList;
		try {
			tMailMagazineDeliveryList = mailMagazineDeliveryService.findByMailMagazineIdHeaderDelivery(property.mailMagazineId, property.areaCd);
		// 配信先が取得できない場合は、リストを空にしておく
		} catch (WNoResultException e) {
			tMailMagazineDeliveryList = new ArrayList<TMailMagazineDelivery>(0);
		}

		// エリア情報を保持するMap
		Map<Integer, String> areaMap = areaService.getAreaNameMap();

		// ファイルをオープンする
		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createCsvFileName());
	    PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), getCsvEncode()));

	    try {
			int count = 0;

			// メルマガヘッダーを出力
			out.println(getCsvHeader());

			// メルマガの値をセット
			List<String> mailMagazineValueList = new ArrayList<String>(5);
			// ID
			mailMagazineValueList.add(tMailMagazine.id == null ? "" : String.valueOf(tMailMagazine.id));
			// 登録日時
			mailMagazineValueList.add(tMailMagazine.registrationDatetime == null ? "" : DateUtils.getDateStr(tMailMagazine.registrationDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
			// 配信日時
			mailMagazineValueList.add(tMailMagazine.deliveryDatetime == null ? "" : DateUtils.getDateStr(tMailMagazine.deliveryDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
			// 配信先区分
			mailMagazineValueList.add(tMailMagazine.deliveryKbn == null ? "" : getTypeName(DeliveryKbn.TYPE_CD, tMailMagazine.deliveryKbn));
			// 配信先フラグ
			mailMagazineValueList.add(tMailMagazine.deliveryFlg == null ? "" : getTypeName(DeliveryFlg.TYPE_CD, tMailMagazine.deliveryFlg));

			//カンマ区切りにしてセットする
			out.println(CsvUtil.createDelimiterStr(mailMagazineValueList));
			count++;

			// 改行
			out.println();
			count++;

			// 改行
			out.println();

			// メルマガ配信先ヘッダーを出力
			out.println(getCsvDeliveryHeader());

			// メルマガ配信先の値をセット
			for (TMailMagazineDelivery tMailMagazineDelivery : tMailMagazineDeliveryList) {

				List<String> DeliveryValueList = new ArrayList<String>(10);
				// エリアコード
				DeliveryValueList.add(tMailMagazineDelivery.areaCd == null ? "" : areaMap.get(tMailMagazineDelivery.areaCd));
				// ユーザー区分
				DeliveryValueList.add(tMailMagazineDelivery.userKbn == null ? "" : getTypeName(UserKbn.TYPE_CD, tMailMagazineDelivery.userKbn));
				// 配信先
				DeliveryValueList.add(tMailMagazineDelivery.deliveryId == null ? "" : String.valueOf(tMailMagazineDelivery.deliveryId));
				// 配信先名
				DeliveryValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDelivery.deliveryName, ""));
				// 端末区分
				DeliveryValueList.add(tMailMagazineDelivery.terminalKbn == null ? "" : getTypeName(TerminalKbn.TYPE_CD, tMailMagazineDelivery.terminalKbn));
				// メールアドレス
				DeliveryValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDelivery.mail, ""));
				// 配信フラグ
				DeliveryValueList.add(tMailMagazineDelivery.deliveryFlg == null ? "" : getTypeName(DeliveryFlg.TYPE_CD, tMailMagazineDelivery.deliveryFlg));
				// 配信日時
				DeliveryValueList.add(tMailMagazineDelivery.deliveryDatetime == null ? "" : DateUtils.getDateStr(tMailMagazineDelivery.deliveryDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
				// エラーフラグ
				DeliveryValueList.add(tMailMagazineDelivery.errorFlg == null ? "" : getTypeName(ErrorFlg.TYPE_CD, tMailMagazineDelivery.errorFlg));
				// エラー受信日時
				DeliveryValueList.add(tMailMagazineDelivery.errorDatetime == null ? "" : DateUtils.getDateStr(tMailMagazineDelivery.errorDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT));
				// 備考
				DeliveryValueList.add(StringUtils.defaultIfEmpty(tMailMagazineDelivery.note, ""));

				//カンマ区切りにしてセットする
				out.println(CsvUtil.createDelimiterStr(DeliveryValueList));
				count++;
			}

			return count;

	    } finally {
			out.close();
		}
	}

	/**
	 * 区分マスタから区分名を取得して返します。<br />
	 * 取得できない場合はブランクを返します。
	 * @param typeCd 区分コード
	 * @param typeValue 区分値
	 * @return 区分名
	 */
	private String getTypeName(String typeCd, Integer typeValue) {

		// 区分マスタ保持Mapに値が保持出来ていない場合
		if (mTypeMap.get(typeCd) == null) {

			// 区分マスタに値をセット
			Map<Integer, String> valueMap = new HashMap<Integer, String>();

			try {
				// 区分マスタからデータを取得
				valueMap = typeService.getMTypeValueMap(typeCd);
				// 区分マスタ保持Mapにセット
				mTypeMap.put(typeCd, valueMap);
				// 区分名を返す
				return valueMap.get(typeValue) == null ? "" : valueMap.get(typeValue);

			// データが取得できない場合
			} catch (WNoResultException e) {
				// 区分保持Mapに空のMapをセット
				mTypeMap.put(typeCd, valueMap);
				// ブランクを返す
				return "";
			}

		// 区分マスタから取得できなかった場合
		} else if (mTypeMap.get(typeCd).isEmpty()) {
			return "";

		// 区分を保持している場合
		} else {
			// 区分名を返す
			return mTypeMap.get(typeCd).get(typeValue) == null ? "" : mTypeMap.get(typeCd).get(typeValue);
		}
	}

	/**
	 * CSV出力用のプロパティファイルを取得します。
	 * @return
	 */
	private Properties getCsvProperties() {
		return ResourceUtil.getProperties("csv.properties");
	}

	/**
	 * CSVファイル名を取得します。
	 * @return
	 */
	private String getCsvFileName() {
		return getCsvProperties().getProperty("gc.mailMag.csv.filename");
	}

	/**
	 * CSV出力文字コードを取得します。
	 * @return
	 */
	private String getCsvEncode() {
		return getCsvProperties().getProperty("gc.csv.encoding");
	}

	/**
	 * メルマガのCSVヘッダーを取得します。
	 * @return メルマガのCSVヘッダー
	 */
	private String getCsvHeader() {
		return getCsvProperties().getProperty("gc.mailMag.csv.header");
	}

	/**
	 * メルマガ詳細のCSVヘッダーを取得します。
	 * @return ルマガ詳細のCSVヘッダー
	 */
	private String getCsvDetailHeader() {
		return getCsvProperties().getProperty("gc.mailMag.csv.detailHeader");
	}

	/**
	 * メルマガ配信先のCSVヘッダーを取得します。
	 * @return  メルマガ配信先のCSVヘッダー
	 */
	private String getCsvDeliveryHeader() {
		return getCsvProperties().getProperty("gc.mailMag.csv.deliveryHeader");
	}

	/**
	 * CSVのファイル名を生成する
	 * @return ファイル名
	 */
	private String createCsvFileName() {

		// ファイル名の取得
		String filename = getCsvFileName();
		// 日時の取得(yyyyMMddHHmmss)
		String dateStr = DateUtils.getTodayDateStr(GourmetCareeConstants.DATETIME_FORMAT_NONSLASH);

		String outputFileName = new StringBuilder()
		.append(filename)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}


	/**
	 * 手動メールマガジンの配信処理
	 * @param mailMagazineId
	 */
	public void sendMailMagazineBackground(int mailMagazineId) {
		new Thread(() -> sendMailMagazine(mailMagazineId))
		.start();
	}

	/**
	 * メルマガ配信のPHPアクセス処理
	 * @param mailMagazineId
	 */
	private void sendMailMagazine(int mailMagazineId) {

		MailMagazineConnection connection = new MailMagazineConnection(GourmetCareeConstants.MAILMAGAZINE_ACCESS_TOKEN, mailMagazineId);
		InputStream is = null;
		ByteArrayOutputStream baos = null;

		boolean isFailure = true;
		try {
			 is = connection.execute();
			 isFailure = false;
		} catch (IOException e1) {
			log.error("メルマガ送信処理に失敗しました", e1);
		} finally {
			Map<String, List<String>> headers = new HashMap<>();
			final StringBuilder header = new StringBuilder();

			int responseStatus = 0;
			String responseBody = null;

			if (connection != null) {
				headers = connection.getResponseHeaders();
				responseStatus = connection.getHttpStatus();
				headers.forEach((k, list) -> header.append(String.format("k:[%s] v:[%s]\n", k, StringUtils.join(list, ","))));
			}
			if (is != null) {
				try {
					responseBody = IOUtils.toString(is);
				} catch (IOException e) {
					log.error("ボディの取得に失敗しました", e);
				}
			}

			if (isFailure) {
				log.error(String.format("メール送信APIでエラーが発生しました。メルマガID:%s　responseCode:%s　responseBody:%s　responseHeader:%s", mailMagazineId, responseStatus, responseBody, header.toString()));
			} else {
				log.info(String.format("メール送信APIが終了しました。メルマガID:%s　responseCode:%s　responseBody:%s　responseHeader:%s", mailMagazineId, responseStatus, responseBody, header.toString()));
			}

			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(baos);
		}
	}

}
