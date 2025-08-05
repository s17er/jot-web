package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMailMagazineOption;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * メルマガオプションのサービスクラスです。
 * @author TakehiroNakamori
 *
 */
public class MailMagazineOptionService extends AbstractGroumetCareeBasicService<TMailMagazineOption> {

	/** デフォルトの号数 */
	private static final int DEFAULT_DISPLAY_NUM = 20;

	/**
	 * すでに登録されているかどうかを判断します。
	 * @param deliveryScheduleDatetime 配信予定日時
	 * @param areaCd エリアコード
	 * @param mailMagazineKbn メルマガ区分
	 * @param mailmagazineOptionKbn メルマガオプション区分
	 * @return 登録されている場合に trueを返す。
	 */
	public boolean isAlreadyResisteredDate(Date deliveryScheduleDatetime, List<String> areaCd,
			int mailMagazineKbn, int mailmagazineOptionKbn) {

		try {
			Date startdaDate = DateUtils.truncate(deliveryScheduleDatetime, Calendar.DATE);
			List<Integer> areaCds = new ArrayList<>();
			for(String s : areaCd) {
				areaCds.add(Integer.parseInt(s));
			}
			TMailMagazineOption entity = jdbcManager.from(TMailMagazineOption.class)
											.where(new SimpleWhere()
													.ge(WztStringUtil.toCamelCase(TMailMagazineOption.DELIVERY_SCHEDULE_DATETIME),
															startdaDate)
													.lt(WztStringUtil.toCamelCase(TMailMagazineOption.DELIVERY_SCHEDULE_DATETIME),
															DateUtils.addDays(startdaDate, 1))
													.in(WztStringUtil.toCamelCase(TMailMagazineOption.AREA_CD),
															areaCds)
													.eq(WztStringUtil.toCamelCase(TMailMagazineOption.MAIL_MAGAZINE_KBN),
															mailMagazineKbn)
													.eq(WztStringUtil.toCamelCase(TMailMagazineOption.MAIL_MAGAZINE_OPTION_KBN),
															mailmagazineOptionKbn)
													.eq(WztStringUtil.toCamelCase(TMailMagazineOption.DELETE_FLG),
															DeleteFlgKbn.NOT_DELETED))
													.disallowNoResult()
													.getSingleResult();

			if (entity == null) {
				return false;
			}

			return true;
		} catch (SNoResultException e) {
			return false;
		}
	}

	/**
	 * すでに登録されているかどうかを判断します。
	 * @param deliveryScheduleDatetime 配信予定日時
	 * @param areaCd エリアコード
	 * @param mailMagazineKbn メルマガ区分
	 * @param mailmagazineOptionKbn メルマガオプション区分
	 * @return 登録されている場合に trueを返す。
	 */
	public boolean isAlreadyResisteredDate(String id, Date deliveryScheduleDatetime, List<String> areaCd,
			int mailMagazineKbn, int mailmagazineOptionKbn) {

		try {
			List<Integer> areaCds = new ArrayList<>();
			for(String s : areaCd) {
				areaCds.add(Integer.parseInt(s));
			}
			Date startdaDate = DateUtils.truncate(deliveryScheduleDatetime, Calendar.DATE);
			TMailMagazineOption entity = jdbcManager.from(TMailMagazineOption.class)
											.where(new SimpleWhere()
													.ge(WztStringUtil.toCamelCase(TMailMagazineOption.DELIVERY_SCHEDULE_DATETIME),
															startdaDate)
													.lt(WztStringUtil.toCamelCase(TMailMagazineOption.DELIVERY_SCHEDULE_DATETIME),
															DateUtils.addDays(startdaDate, 1))
													.in(WztStringUtil.toCamelCase(TMailMagazineOption.AREA_CD),
															areaCds)
													.eq(WztStringUtil.toCamelCase(TMailMagazineOption.MAIL_MAGAZINE_KBN),
															mailMagazineKbn)
													.eq(WztStringUtil.toCamelCase(TMailMagazineOption.MAIL_MAGAZINE_OPTION_KBN),
															mailmagazineOptionKbn)
													.eq(WztStringUtil.toCamelCase(TMailMagazineOption.DELETE_FLG),
															DeleteFlgKbn.NOT_DELETED)
													.ne(WztStringUtil.toCamelCase(TMailMagazineOption.ID), Integer.parseInt(id)))

													.disallowNoResult()
													.getSingleResult();

			if (entity == null) {
				return false;
			}

			return true;
		} catch (SNoResultException e) {
			return false;
		}
	}

	/**
	 * 編集・削除できるかのチェック
	 * @throws WNoResultException
	 */
	public boolean canEditable(int id) throws WNoResultException {
		try {
			return canEditable(findById(id, DeleteFlgKbn.NOT_DELETED));
		} catch (SNoResultException e) {
			throw new WNoResultException("該当データがありません。");
		}

	}


	/**
	 * 編集・削除できるかのチェック
	 */
	public boolean canEditable(TMailMagazineOption entity) {
		return entity.mailMagazineId == null ? true : false;
	}

	/**
	 * ヘッダメッセージリストの取得
	 * @param maxRowValue
	 * @param areaCd
	 * @return
	 * @throws WNoResultException
	 */
	public List<TMailMagazineOption> getHeaderMessageList(String maxRowValue, List<String> areaCd) throws WNoResultException {

		StringBuilder sqlSb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();

		sqlSb.append(" SELECT ");
		sqlSb.append("    * ");
		sqlSb.append(" FROM ");
		sqlSb.append("    t_mail_magazine_option ");
		sqlSb.append(" WHERE ");
		sqlSb.append("    delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
		sqlSb.append("    AND mailmagazine_kbn = ? ");
		params.add(MTypeConstants.MailmagazineKbn.NEW_INFORMATION);
		sqlSb.append("    AND mailmagazine_option_kbn = ? ");
		params.add(MTypeConstants.MailmagazineOptionKbn.HEADER_MESSAGE);
		// エリアでの検索を行わないなら不要
		if(CollectionUtils.isNotEmpty(areaCd)) {
			sqlSb.append(SqlUtils.andInNoCamelize(TMailMagazineOption.AREA_CD, areaCd.size()));
			params.addAll(areaCd);
		}
		sqlSb.append(" ORDER BY delivery_schedule_datetime DESC ");

		if (StringUtils.isNotBlank(maxRowValue)) {
			sqlSb.append(" LIMIT ? ");
			params.add(NumberUtils.toInt(maxRowValue, DEFAULT_DISPLAY_NUM));
		}


		try {
			List<TMailMagazineOption> entityList = jdbcManager.selectBySql(TMailMagazineOption.class, sqlSb.toString(), params.toArray())
															.disallowNoResult()
															.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException("ヘッダメッセージの件数が0件です。");
		}
	}

}
