package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.shop.logic.csv.ObservateApplicationCsv;
import com.gourmetcaree.shop.logic.dto.ObservateApplicationRetDto;

/**
 * 店舗見学・質問メールのロジック
 * @author Yamane
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class ObservateApplicationLogic extends AbstractShopLogic {

	/** 店舗見学・質問メールサービス */
	@Resource
	protected ObservateApplicationService observateApplicationService;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * 1年以内の店舗見学・質問メールを取得する
	 * @param property ページプロパティ
	 * @return
	 * @throws WNoResultException
	 */
	public ObservateApplicationRetDto getObservateApplication(PagerProperty property) throws WNoResultException {

		ObservateApplicationRetDto dto = new ObservateApplicationRetDto();



		try {

			Where where = createWhere();
			long count = observateApplicationService.countRecords(where);

			PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
			pageNavi.sortKey = desc(toCamelCase(TApplication.APPLICATION_DATETIME)) + ", " + desc(toCamelCase(TApplication.ID));
			pageNavi.changeAllCount((int)count);
			pageNavi.setPage(property.targetPage);

			dto.pageNavi = pageNavi;

			dto.retList = observateApplicationService.findByCondition(where, pageNavi);

			return dto;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 未読メールのチェックを行う
	 * @param observateApplicationId 質問メールId
	 * @return true:存在する、false:存在しない
	 */
	public boolean isUnopenedApplicantMailExist(int observateApplicationId) {

		return mailService.isReceivedApplicationMailFromExist(observateApplicationId, getCustomerId(), MTypeConstants.MailKbn.OBSERVATE_APPLICATION, MTypeConstants.MailStatus.UNOPENED);

	}





	/**
	 * CSVをアウトプットします
	 * @param response
	 * @param fileName
	 * @throws WNoResultException, IOException
	 */
	public void outputCsv(HttpServletResponse response, String fileName) throws WNoResultException, IOException {
		Where where = createWhere();
		long count = observateApplicationService.countRecords(where);

		if (count == 0l) {
			throw new WNoResultException("CSVに出力する項目がありません。");
		}

		String csvFileName = String.format("%s_%s.csv", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), fileName);
		response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
		response.setHeader(GourmetCareeConstants.CSV_HEADER_PARAM1, GourmetCareeConstants.CSV_HEADER_FILENAME_PREFIX.concat(csvFileName));

		AutoSelect<TObservateApplication> select = jdbcManager.from(TObservateApplication.class)
															.where(where)
															.orderBy(desc(toCamelCase(TApplication.APPLICATION_DATETIME)) + ", " + desc(toCamelCase(TApplication.ID)));

		PrintWriter out = null;

		S2CSVWriteCtrl<ObservateApplicationCsv> csv_writer = null;

		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), GourmetCareeConstants.CSV_ENCODING));
			csv_writer = s2CSVCtrlFactory.getWriteController(ObservateApplicationCsv.class, out);

			writeCsv(select, csv_writer);
		} finally {
			if (csv_writer != null) {
				csv_writer.close();
				csv_writer = null;
			}
		}

	}

	/**
	 * CSVに書き込みます。
	 * @param select セレクト
	 * @param writer ライター
	 */
	private void writeCsv(AutoSelect<TObservateApplication> select, final S2CSVWriteCtrl<ObservateApplicationCsv> writer) {
		select.iterate(new IterationCallback<TObservateApplication, Void>() {

			@Override
			public Void iterate(TObservateApplication entity, IterationContext context) {
				if (entity == null) {
					return null;
				}

				ObservateApplicationCsv csv = Beans.createAndCopy(ObservateApplicationCsv.class, entity).execute();

				if (entity.sexKbn != null) {
					csv.sexKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, entity.sexKbn);
				}

				if (StringUtils.isNotBlank(entity.phoneNo1)
						&& StringUtils.isNotBlank(entity.phoneNo2)
						&& StringUtils.isNotBlank(entity.phoneNo3)) {
					csv.phoneNo = String.format("%s-%s-%s", entity.phoneNo1, entity.phoneNo2, entity.phoneNo3);
				}

				writer.write(csv);

				return null;
			}

		});
	}


	/**
	 * WHEREを作成します。
	 * @return
	 */
	private Where createWhere() {
		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		Where where = new SimpleWhere()
		.eq(toCamelCase(TObservateApplication.CUSTOMER_ID), getCustomerId())
		.eq(toCamelCase(TObservateApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL)	// いたずら応募以外
		.ge(toCamelCase(TObservateApplication.APPLICATION_DATETIME), cal.getTime());

		return where;
	}
}
