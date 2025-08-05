package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.service.dto.AdvancedRegistrationMailMagDto;
import com.gourmetcaree.admin.service.dto.AdvancedRegistrationSearchResultDto;
import com.gourmetcaree.admin.service.property.AdvancedMemberCsvProperty;
import com.gourmetcaree.admin.service.property.MailMagazineProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.AdvancedMailMagReceptionFlg;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.entity.TMailMagazineDelivery;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MemberService;

/**
 * 事前登録ロジック
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationLogic extends AbstractGourmetCareeLogic {

	/** ログ */
	private static final Logger log = Logger.getLogger(AdvancedMemberCsvLogic.class);

	/** ORDER BY のカラム */
	private static final String ORDER_BY_COLUMN = "ENT.registration_datetime DESC";

	/** メルマガロジック */
	@Resource
	private MailMagazineLogic mailMagazineLogic;


	/** 事前登録会員CSVロジック */
	@Resource
	private AdvancedMemberCsvLogic advancedMemberCsvLogic;

	/** 会員サービス */
	@Resource
	private MemberService memberService;

	/**
	 * 検索を行います。
	 * @param property 検索プロパティ
	 * @param pageNavi ページナビ
	 * @throws WNoResultException 対象が見つからなかったらスロー
	 */
	public List<AdvancedRegistrationSearchResultDto> doSearch(SearchProperty property, PageNavigateHelper pageNavi) throws WNoResultException {

		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);
		createSearchSql(sql, params, property);


		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			throw new WNoResultException("検索結果が見つかりません。");
		}

		pageNavi.changeAllCount((int) count);
		pageNavi.setPage(property.targetPage);


		sql.append(" ORDER BY ")
			.append(ORDER_BY_COLUMN);

		return jdbcManager.selectBySql(AdvancedRegistrationSearchResultDto.class, sql.toString(), params.toArray())
				.limit(pageNavi.limit)
					.offset(pageNavi.offset)
				.iterate(new IterationCallback<AdvancedRegistrationSearchResultDto, List<AdvancedRegistrationSearchResultDto>>() {
					List<AdvancedRegistrationSearchResultDto> retList = new ArrayList<AdvancedRegistrationSearchResultDto>();
					@Override
					public List<AdvancedRegistrationSearchResultDto> iterate(
							AdvancedRegistrationSearchResultDto entity, IterationContext context) {
						if (entity == null) {
							return retList;
						}


						if (entity.advancedRegistrationUserId == null) {
							entity.memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
						} else {
							if (memberService.isDataExists(entity.advancedRegistrationUserId)) {
								entity.memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_NEW_MEMBER;
							} else {
								entity.memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
							}
						}

						retList.add(entity);
						return retList;
					}
				});

	}


	/**
	 * 印刷用の検索を行います。
	 * @param property 検索プロパティ
	 */
	public void doSearchPrint(SearchProperty property) {
		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);
		createSearchSql(sql, params, property);
		sql.append(" ORDER BY ")
			.append(ORDER_BY_COLUMN);

		property.select = jdbcManager.selectBySql(
				AdvancedRegistrationSearchResultDto.class,
				sql.toString(),
				params.toArray());
	}



	/**
	 * メールマガジンのインサートを行います。
	 * @param searchProperty 検索プロパティ
	 * @throws WNoResultException 送信先が見つからなければスロー
	 * @return メルマガID
	 */
	public Integer insertMailMagazine(SearchProperty searchProperty, MailMagazineContentDto contentDto) throws WNoResultException {


		MailMagazineProperty property = new MailMagazineProperty();

		// メルマガテーブルに登録する値をセット
		property.mailMagazine = new TMailMagazine();
		property.mailMagazine.deliveryKbn = MTypeConstants.DeliveryKbn.ADVANCED_REGISTRATION_DELIVERY_KBN;
		// 配信先(事前登録会員)
		property.deliveryKbn = MTypeConstants.DeliveryKbn.ADVANCED_REGISTRATION_DELIVERY_KBN;
		property.mailMagazine.mailmagazineKbn = MTypeConstants.MailmagazineKbn.CUSTOMER_MEMEBER;


		// メルマガ詳細テーブルに登録する値をセット
		property.mailMagazine.tMailMagazineDetailList = new ArrayList<TMailMagazineDetail>();
		TMailMagazineDetail mailMagazineDetail = new TMailMagazineDetail();
		mailMagazineDetail.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
		mailMagazineDetail.mailMagazineTitle = contentDto.pcMailMagazineTitle;
		mailMagazineDetail.body = contentDto.pcBody;
		property.mailMagazine.tMailMagazineDetailList.add(mailMagazineDetail);

		// メルマガ配信先に登録する値をセット
		property.mailMagazine.tMailMagazineDeliveryList = createMailMagazineDeliveryList(searchProperty, contentDto);

		// メルマガ関連データを登録
		Integer mailMagazineId = mailMagazineLogic.insertMailMag(property);
		log.info(String.format("メールマガジンを登録しました。ID:[%d]", mailMagazineId));

		property = null;

		return mailMagazineId;


	}

	/**
	 * メルマガ配信リストを作成します。
	 * @param searchProperty 検索プロパティ
	 * @param contentDto 内容DTO
	 * @return メルマガ配信リスト
	 * @throws WNoResultException 結果がない場合にスロー
	 */
	private List<TMailMagazineDelivery> createMailMagazineDeliveryList(SearchProperty searchProperty, MailMagazineContentDto contentDto) throws WNoResultException {

		final Logger logForSql = Logger.getLogger("advancedRegistrationEntryMailMagSelectLog");

		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);
		createMailMagazineSearchSql(sql, params, searchProperty);
		SqlSelect<AdvancedRegistrationMailMagDto> select =
				jdbcManager.selectBySql(AdvancedRegistrationMailMagDto.class, sql.toString(), params.toArray());


		logForSql.info(String.format("★★★★★★★★★検索条件★★★★★★★★★\r\n%s", ToStringBuilder.reflectionToString(searchProperty, ToStringStyle.MULTI_LINE_STYLE)));
		List<TMailMagazineDelivery> deliverlyList =
				select.iterate(new IterationCallback<AdvancedRegistrationMailMagDto, List<TMailMagazineDelivery>>() {
					List<TMailMagazineDelivery> list = new ArrayList<TMailMagazineDelivery>(0);
					@Override
					public List<TMailMagazineDelivery> iterate(AdvancedRegistrationMailMagDto dto, IterationContext context) {
						if (dto == null) {
							return list;
						}

						TMailMagazineDelivery defaultEntity = new TMailMagazineDelivery();
						defaultEntity.areaCd = dto.areaCd;
						defaultEntity.userKbn = MTypeConstants.UserKbn.ADVANCED_REGISTRATION;
						defaultEntity.deliveryId = dto.advancedRegistrationEntryId;
						defaultEntity.deliveryName = dto.memberName;

						logForSql.info(String.format("【メルマガ】事前登録エントリID[%d]が検索されました。", dto.advancedRegistrationEntryId));
						// リニューアルでログインIDのみになったためPC、携帯からログインIDに変更
						if (StringUtils.isNotBlank(dto.loginId)) {
							TMailMagazineDelivery entity = Beans.createAndCopy(TMailMagazineDelivery.class, defaultEntity)
																.execute();

							logForSql.info(String.format("【メルマガ】事前登録エントリID[%d]のログインメールを追加します。", dto.advancedRegistrationEntryId));
							entity.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
							entity.mail = dto.loginId;
							list.add(entity);
						}
						return list;
					}
				});

		SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();

		StringBuilder sb = new StringBuilder("★★★★★★★★★SQL★★★★★★★★★");
		sb.append(GourmetCareeConstants.CRLF)
			.append("完成形：[").append(sqlLog.getCompleteSql()).append("]")
			.append(GourmetCareeConstants.CRLF)
			.append("SQL単体：[").append(sqlLog.getRawSql()).append("]")
			.append(GourmetCareeConstants.CRLF);
		logForSql.info(sb.toString());



		if (CollectionUtils.isEmpty(deliverlyList)) {
			throw new WNoResultException("配信先事前登録会員が見つかりません。");
		}

		return deliverlyList;
	}







	/**
	 * CSVを出力します。
	 * @param property 検索プロパティ
	 * @throws WNoResultException 事前登録会員が見つからなかった場合にスロー
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public void outputCsv(SearchProperty property, AdvancedMemberCsvProperty csvProperty) throws WNoResultException, UnsupportedEncodingException, IOException {
		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);

		createSearchSql(sql, params, property);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());
		if (count == 0l) {
			throw new WNoResultException("事前登録会員が見つかりません。");
		}

		sql.append(" ORDER BY ")
			.append(ORDER_BY_COLUMN);


		csvProperty.select = jdbcManager.selectBySql(AdvancedRegistrationSearchResultDto.class, sql.toString(), params.toArray());

		advancedMemberCsvLogic.outputCsv(csvProperty);
	}








	/**
	 * メルマガ配信用の検索SQLを作成します。
	 * @param sql SQL
	 * @param params 条件
	 * @param property 検索条件
	 */
	private static void createMailMagazineSearchSql(StringBuffer sql, List<Object> params, SearchProperty property) {
		sql.append(" SELECT ");
		sql.append("         ENT.id AS advanced_registration_entry_id, ");
		sql.append("         ENT.member_name, ");
		sql.append("         ENT.login_id, ");
		sql.append("         ENT.pc_mail, ");
		sql.append("         ENT.pc_mail_stop_flg, ");
		sql.append("         ENT.mobile_mail, ");
		sql.append("         ENT.mobile_mail_stop_flg, ");
		sql.append("         ENT.area_cd");
		sql.append("     FROM ");
		sql.append("         t_advanced_registration_entry ENT ");
		sql.append(" WHERE ");
		sql.append("     ENT.delete_flg = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);

		addSearchCondition(sql, params, property);

		sql.append("     AND ENT.advanced_mail_magazine_reception_flg = ?");
		params.add(AdvancedMailMagReceptionFlg.RECEPTION);

	}







	/**
	 * 検索SQLを作成します。
	 * @param sql SQL
	 * @param params 条件
	 * @param property 検索条件
	 */
	private static void createSearchSql(StringBuffer sql, List<Object> params, SearchProperty property) {
		sql.append(" SELECT ");
		sql.append("         * ");
		sql.append("     FROM ");
		sql.append("         t_advanced_registration_entry ENT ");
		sql.append(" WHERE ");
		sql.append("     ENT.delete_flg = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);

			addSearchCondition(sql, params, property);


	}


	/**
	 * 検索条件を追加します。
	 * @param sql SQL
	 * @param params 検索条件
	 * @param property 検索プロパティ
	 */
	private static void addSearchCondition(StringBuffer sql, List<Object> params, SearchProperty property) {
		// 事前登録ユーザID

		if (property.id != null) {
			sql.append("     AND ENT.id = ? ");
			params.add(property.id);
		}

		// 名前
		if (StringUtils.isNotBlank(property.name)) {

			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.name,
					params,
					"     ENT.member_name LIKE ? "));
			sql.append(" ) ");
		}

		// ふりがな
		if (StringUtils.isNotBlank(property.furigana)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.furigana,
					params,
					"    ENT.member_name_kana LIKE ? "));
			sql.append(" ) ");
		}


		// 都道府県コード
		if (property.prefecturesCd != null) {
			sql.append("     AND ENT.prefectures_cd = ? ");
			params.add(property.prefecturesCd);
		}


		// 登録日時(開始)
		if (property.registrationStartTimestamp != null) {
			sql.append("     AND ENT.registration_datetime >= ? ");
			params.add(property.registrationStartTimestamp);
		}

		// 登録日時(終了)
		if (property.registrationEndTimestamp != null) {
			sql.append("     AND ENT.registration_datetime <= ? ");
			params.add(property.registrationEndTimestamp);
		}


		// 会員区分にチェックがある場合
		if (!ArrayUtils.isEmpty(property.statusKbnArray)) {

			sql.append(" AND (");

			boolean noMemberFlg = false;
			// 非会員がある場合 (会員IDがNULLになっているか、会員IDが存在するがDELETE_FLGがTRUEのもの)
			if (ArrayUtils.contains(property.statusKbnArray, String.valueOf(MTypeConstants.AdvancedRegistrationStatusKbn.NO_MEMBER))) {
				sql.append(" (ENT.advanced_registration_user_id IS NULL OR ");
				sql.append(" ( ENT.advanced_registration_user_id IS NOT NULL AND EXISTS ( SELECT * FROM m_member SUB_MEM WHERE SUB_MEM.id = ENT.advanced_registration_user_id ")
					.append("   AND SUB_MEM.delete_flg = ? ")
					.append("  ))) ");

				params.add(DeleteFlgKbn.DELETED);
				noMemberFlg = true;
			}

			if (ArrayUtils.contains(property.statusKbnArray, String.valueOf(MTypeConstants.AdvancedRegistrationStatusKbn.ADVANCED_MEMBER))) {
				if (noMemberFlg) {
					sql.append(" OR (");
				}

				sql.append(" ENT.advanced_registration_user_id IS NOT NULL ")
					.append(" AND EXISTS ( ")
					.append("        SELECT * FROM ")
					.append("        m_member MEM WHERE ")
					.append("        MEM.id = ENT.advanced_registration_user_id ")
					.append("        AND MEM.delete_flg = ? ")
					.append(" ) ");

				params.add(DeleteFlgKbn.NOT_DELETED);
				if (noMemberFlg) {
					sql.append(" ) ");
				}

			}



			sql.append(" ) ");
		}


		// 事前登録メルマガフラグ
		if (property.advancedMailMagazineReceptionFlg != null) {
			sql.append("     AND ENT.advanced_mail_magazine_reception_flg = ? ");
			params.add(property.advancedMailMagazineReceptionFlg);

		}


		// 性別区分
		if (property.sexKbn != null) {
			sql.append("     AND ENT.sex_kbn = ? ");
			params.add(property.sexKbn);
		}


		// 年齢(下限)
		if (property.minAge != null) {
			sql.append("     AND ENT.birthday <= ? ");
			params.add(property.getMinAgeTimestamp());
		}
		// 年齢(上限)
		if (property.maxAge != null) {
			sql.append("     AND ENT.birthday > ? ");
			params.add(property.getMaxAgeTimestamp());
		}

		// メールアドレス
		if (StringUtils.isNotBlank(property.mailAddress)) {
			sql.append("     AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.mailAddress,
					params,
					"     ENT.pc_mail LIKE ? ",
					"     OR ENT.mobile_mail LIKE ?  ",
					"     OR ENT.login_id LIKE ?  "
					));
			sql.append("     )");
		}

		// 端末区分
		if (property.terminalKbn != null) {
			sql.append("     AND ENT.terminal_kbn = ? ");
			params.add(property.terminalKbn);
		}

		// 事前登録ID
		if (CollectionUtils.isNotEmpty(property.advancedRegistrationIdList)) {
			sql.append(" AND ENT.advanced_registration_id IN (")
			.append(SqlUtils.getQMarks(property.advancedRegistrationIdList.size()))
			.append(" ) ");
			params.addAll(property.advancedRegistrationIdList);
		}

		if (property.attendedStatus != null) {
			sql.append("    AND ENT.attended_status = ? ");
			params.add(property.attendedStatus);
		}

		// エリアコード
		if (property.areaCd != null) {
			sql.append("     AND ENT.area_cd = ? ");
			params.add(property.areaCd);
		}

		/* 事前登録ユーザID */
		if (property.advancedRegistrationUserId != null) {
			sql.append("     AND ENT.advanced_registration_user_id = ? ");
			params.add(property.advancedRegistrationUserId);
		}
	}



	/**
	 * 検索プロパティを作成します
	 * @return 検索プロパティ
	 */
	public SearchProperty createSearchProperty() {
		return new SearchProperty();
	}

	/**
	 * メルマガ内容DTOを作成します。
	 * @return メルマガ内容DTO
	 */
	public MailMagazineContentDto createMailMagazineContentDto() {
		return new MailMagazineContentDto();
	}

	/**
	 * 検索条件プロパティ
	 * @author Takehiro Nakamori
	 *
	 */
	public class SearchProperty extends BaseProperty {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = -4269620682250641784L;

		/** 対象ページ */
		public int targetPage;

		/** セレクト */
		private SqlSelect<AdvancedRegistrationSearchResultDto> select;

		/** 事前登録ユーザID */
		public Integer id;

		/**
		 * 名前
		 */
		public String name;


		/**
		 * ふりがな
		 */
		public String furigana;

		/**
		 * エリアコード
		 */
		public Integer areaCd;

		/** 来場ステータス */
		public Integer attendedStatus;

		/**
		 * 都道府県コード
		 */
		public Integer prefecturesCd;

		/** ステータス区分配列 */
		public String[] statusKbnArray;

		/**
		 * 事前登録メルマガフラグ
		 */
		public Integer advancedMailMagazineReceptionFlg;

		/**
		 * 性別区分
		 */
		public Integer sexKbn;

		/**
		 * メールアドレス
		 */
		public String mailAddress;

		/**
		 * 端末区分
		 */
		public Integer terminalKbn;

		/** 事前登録ID配列 */
		public List<Integer> advancedRegistrationIdList;

		/**
		 * 年齢(下限)
		 */
		public Integer minAge;

		/**
		 * 年齢(上限)
		 */
		public Integer maxAge;

		/** 登録日時(開始) */
		public Timestamp registrationStartTimestamp;

		/** 登録日時(終了) */
		public Timestamp registrationEndTimestamp;

		/** ORDER BY フラグ */
		public boolean orderByFlg;

		/** 事前エントリ登録ユーザID */
		public Integer advancedRegistrationUserId;

		/**
		 * 年齢(下限)のタイムスタンプを取得します。
		 * @return 年齢(下限)のタイムスタンプ
		 */
		public Timestamp getMinAgeTimestamp() {
			return GourmetCareeUtil.convertToTimestampDivYear(minAge, 0);
		}

		/**
		 * 年齢(上限)のタイムスタンプを取得します。
		 * @return 年齢(上限)のタイムスタンプ
		 */
		public Timestamp getMaxAgeTimestamp() {
			return GourmetCareeUtil.convertToTimestampDivYear(maxAge, 1);
		}

		/**
		 * セレクトを取得します。
		 * @return セレクト
		 */
		public SqlSelect<AdvancedRegistrationSearchResultDto> getSelect() {
			return select;
		}
	}


	/**
	 * メルマガ内容DTO
	 * @author Takehiro Nakamori
	 *
	 */
	public class MailMagazineContentDto extends BaseDto {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = -3369276193355519928L;

		/** PCメールマガジンタイトル */
		public String pcMailMagazineTitle;

		/** PCメールマガジン本文 */
		public String pcBody;

		/** 携帯メールマガジンタイトル */
		public String mbMailMagazineTitle;

		/** 携帯メールマガジン本文 */
		public String mbBody;

	}
}

