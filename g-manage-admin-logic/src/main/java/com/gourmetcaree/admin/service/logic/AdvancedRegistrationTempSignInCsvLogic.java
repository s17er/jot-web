package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.AdvancedRegistrationTempSignInCsv;
import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TTemporaryRegistration;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * 事前登録に仮登録をした結果CSV用ロジック
 * @author nakamori
 *
 */
public class AdvancedRegistrationTempSignInCsvLogic extends AbstractGourmetCareeLogic {

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	@Resource
	private TypeService typeService;



	/**
	 * CSVの出力を行います。
	 * @param advancedRegistrationId
	 * @throws WNoResultException
	 * @throws IOException
	 * @throws
	 */
	public void output(Integer advancedRegistrationId) throws WNoResultException, IOException {

		List<TTemporaryRegistration> tempList = selectTempAdvancedRegistration(advancedRegistrationId);
		if (tempList == null) {
			throw new WNoResultException(String.format("事前登録の仮登録データが見つかりませんでした。 仮登録ID:[%d]", advancedRegistrationId));
		}

		HttpServletResponse response = ResponseUtil.getResponse();
		response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
		response.setHeader(GourmetCareeConstants.CSV_HEADER_PARAM1, createOutputFileName());

		PrintWriter out = null;
		S2CSVWriteCtrl<AdvancedRegistrationTempSignInCsv> csvWriter = null;

		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), GourmetCareeConstants.CSV_ENCODING));
			csvWriter = s2CSVCtrlFactory.getWriteController(AdvancedRegistrationTempSignInCsv.class, out);

			Map<Integer, String> terminalMap = typeService.getMTypeValueMap(MTypeConstants.TerminalKbn.TYPE_CD);
			Map<Integer, String> accessMap = createAccessFlgMap();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			for (TTemporaryRegistration entity : tempList) {
				AdvancedRegistrationTempSignInCsv csv = new AdvancedRegistrationTempSignInCsv();
				csv.mail = entity.mail;
				if (entity.terminalKbn != null) {
					csv.terminall = StringUtils.defaultString(terminalMap.get(entity.terminalKbn));
				} else {
					csv.terminall = "";
				}


				if (entity.accessFlg != null) {
					csv.registeredFlg = accessMap.get(entity.accessFlg);
				} else {
					csv.registeredFlg = "未登録";
				}

				if (entity.accessDatetime != null) {
					csv.registeredDate = sdf.format(entity.accessDatetime);
				}

				csvWriter.write(csv);
			}
		} finally {
			IOUtils.closeQuietly(out);
			csvWriter.close();
		}

	}


	/**
	 * 仮登録されている事前登録をセレクト
	 * @param advancedRegistrationId
	 * @return
	 */
	private List<TTemporaryRegistration> selectTempAdvancedRegistration(Integer advancedRegistrationId) {
		if (advancedRegistrationId == null) {
			return new ArrayList<TTemporaryRegistration>();
		}

		// メール一覧取得
		List<String> mailAddressList = selectTempSigninMailAddress(advancedRegistrationId);

		if (CollectionUtils.isEmpty(mailAddressList)) {
			return new ArrayList<TTemporaryRegistration>(0);
		}

		List<Integer> tempIdList = new ArrayList<Integer>();
		// メールごとに、データを取得する。
		for (String mail : mailAddressList) {
			TTemporaryRegistration entity = selectTempporaryRegistration(advancedRegistrationId, mail);
			if (entity == null) {
				logicLog.error(String.format("事前登録の仮登録者CSV生成時、TTemporaryRegistrationのデータが見つかりませんでした。 メールアドレス：[%s]", mail));
				continue;
			}
			tempIdList.add(entity.id);
		}


		// 各データが取得できたら、登録済/未登録・登録時間順にオーダーバイ
		String orderBy = String.format("%s DESC, %s DESC, %s DESC", TTemporaryRegistration.ACCESS_FLG, TTemporaryRegistration.ACCESS_DATETIME, TTemporaryRegistration.INSERT_DATETIME);
		List<TTemporaryRegistration> list =
				jdbcManager.from(TTemporaryRegistration.class)
						.where(new SimpleWhere()
								.in(WztStringUtil.toCamelCase(TTemporaryRegistration.ID), tempIdList)
								.eq(WztStringUtil.toCamelCase(TTemporaryRegistration.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
								.orderBy(orderBy)
								.getResultList();

		return list;
	}


	/**
	 * メールと仮登録IDから、仮登録データを取得。
	 *
	 * 同メールが複数登録されていた場合にまとめるため、メールごとの取得を行っている。
	 * 登録済み と 未登録両方が存在すれば登録済みを採用。
	 * @param advancedRegistrationId
	 * @param mail
	 * @return
	 */
	private TTemporaryRegistration selectTempporaryRegistration(int advancedRegistrationId, String mail) {
		StringBuilder sql = new StringBuilder("SELECT * FROM t_temporary_registration TEMP ");
		List<Object> params = new ArrayList<Object>();

		createWhereSql(sql, params, advancedRegistrationId, mail);

		sql.append(" ORDER BY access_flg DESC NULLS LAST, ")
			.append(" update_datetime DESC NULLS LAST  ");

		TTemporaryRegistration entity = jdbcManager.selectBySql(TTemporaryRegistration.class, sql.toString(), params.toArray())
				.limit(1)
				.getSingleResult();

		if (logicLog.isDebugEnabled()) {
			logicLog.debug(String.format("事前登録ID:[%d] メールアドレス:[%s] 一時保存エンティティ：[%s]", advancedRegistrationId, mail, entity));
		}

		return entity;
	}

	/**
	 * 仮登録しているメールの一覧を取得
	 * @param advancedRegistrationId
	 * @return
	 */
	private List<String> selectTempSigninMailAddress(int advancedRegistrationId) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT mail FROM t_temporary_registration TEMP ");
		List<Object> params = new ArrayList<Object>();

		createWhereSql(sql, params, advancedRegistrationId, null);

		return jdbcManager.selectBySql(String.class, sql.toString(), params.toArray())
							.getResultList();
	}


	private void createWhereSql(StringBuilder sql, List<Object> params,  int advancedRegistrationId, String mail) {
		sql.append(" WHERE TEMP.temporary_registration_kbn = ? ");
		params.add(MTypeConstants.TemporaryRegistrationKbn.ADVANCED_REGISTRATION);

		sql.append(" AND TEMP.advanced_registration_id = ? ");
		params.add(advancedRegistrationId);

		if (StringUtils.isNotBlank(mail)) {
			sql.append(" AND TEMP.mail = ? ");
			params.add(mail);
		}

		sql.append(" AND TEMP.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
	}


	private static String createOutputFileName() {
		String dateStr = null;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		return String.format("attachment; filename=advancedTempSignIn_%s.csv", dateStr);


	}

	private Map<Integer, String> createAccessFlgMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(MTypeConstants.AccessFlg.ALREADY, "登録済み");
		map.put(MTypeConstants.AccessFlg.NEVER, "未登録");
		return map;
	}
}
