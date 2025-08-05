package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.admin.service.dto.ScoutMailLogDto;
import com.gourmetcaree.admin.service.dto.ScoutMailRemainRetDto;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.CustomerCsv;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.entity.TCustomerLoginHistory;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.enums.MTypeEnum.MailStatusEnum;
import com.gourmetcaree.db.common.enums.MTypeEnum.SenderKbnEnum;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerAccountService;
import com.gourmetcaree.db.common.service.CustomerLoginHistoryService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.PrefecturesService;


/**
 * 顧客のCSVロジック
 * @author Takehiro Nakamori
 *
 */
public class CustomerCsvLogic extends AbstractAdminLogic {

	/** ログ */
	private static final Logger log = Logger.getLogger(CustomerCsvLogic.class);

	/** 日付フォーマット */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
	/** 日付+時間のフォーマット */
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT);


	@Resource
	private CustomerService customerService;

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 名前変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	private MailService mailService;

	@Resource
	private CustomerLoginHistoryService customerLoginHistoryService;

	@Resource
	private PrefecturesService prefecturesService;

	@Resource
	private ScoutMailLogic scoutMailLogic;

	@Resource
	private CustomerLogic customerLogic;

	@Resource
	private CustomerAccountService customerAccountService;

	@Resource
	private CustomerSubMailService customerSubMailService;


	public void outputCsv(Map<String, String> map, String companyId, String sortKey) throws WNoResultException, IOException {
		if (customerService.countSearchResult(map) == 0) {
			throw new WNoResultException();
		}

		String fileName = createOutputFileName();


		PrintWriter out = null;
		S2CSVWriteCtrl<CustomerCsv> csv_writer = null;

		try {
			response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
			GourmetCareeUtil.setResponseCsvHeader(response, fileName);
			out = new PrintWriter(
					new OutputStreamWriter(
							response.getOutputStream(),
							GourmetCareeConstants.CSV_ENCODING));

			csv_writer =
					s2CSVCtrlFactory.getWriteController(CustomerCsv.class, out);

			outputCsv(map, companyId, sortKey, csv_writer);

			out.flush();



		} finally {
			if (csv_writer != null) {
				csv_writer.close();
			}

			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * CSV出力
	 * @param map
	 * @param companyId
	 * @param sortKey
	 * @param csv_writer
	 */
	private void outputCsv(Map<String, String> map, String companyId, String sortKey, final S2CSVWriteCtrl<CustomerCsv> csv_writer) {
		customerService.executeSearch(map, companyId, sortKey, new IterationCallback<MCustomer, Void>() {
			@Override
			public Void iterate(MCustomer entity, IterationContext context) {
				if (entity == null) {
					return null;
				}

				csv_writer.write(createCsvFromCustomer(entity));

				return null;
			}
		});
	}



	/**
	 * CSV出力
	 * @param customer
	 * @return
	 */
	private CustomerCsv createCsvFromCustomer(MCustomer customer) {
		CustomerCsv csv = Beans.createAndCopy(CustomerCsv.class, customer).execute();

		if (StringUtils.isNotBlank(customer.phoneNo1)
				&& StringUtils.isNotBlank(customer.phoneNo2)
				&& StringUtils.isNotBlank(customer.phoneNo3)) {
			csv.telNo = String.format("%s-%s-%s", customer.phoneNo1, customer.phoneNo2, customer.phoneNo3);
		}

		if (StringUtils.isNotBlank(customer.faxNo1)
				&& StringUtils.isNotBlank(customer.faxNo2)
				&& StringUtils.isNotBlank(customer.faxNo3)) {
			csv.faxNo = String.format("%s-%s-%s", customer.faxNo1, customer.faxNo2, customer.faxNo3);
		}

		if (customer.registrationDatetime != null) {
			csv.registrationDatetimeStr = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH).format(customer.registrationDatetime);
		}

		StringBuilder addressSb = new StringBuilder();
		if (customer.prefecturesCd != null) {
			addressSb.append(valueToNameConvertLogic.convertToPrefecturesName(customer.prefecturesCd));
			addressSb.append(" ");
		}
		addressSb.append(StringUtils.defaultString(customer.municipality));
		addressSb.append(" ");
		addressSb.append(StringUtils.defaultString(customer.address));
		csv.address = addressSb.toString();

		List<MCustomerSubMail> subMailList = customerSubMailService.findByCustomerId(customer.id);
		csv.subMail = CollectionUtils.isNotEmpty(subMailList) ? StringUtils.join(subMailList.stream().map(s -> s.subMail).collect(Collectors.toList()), ",") : "";

		try {
			csv.publicationEndDisplayFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PublicationEndDisplayFlg.TYPE_CD, customer.publicationEndDisplayFlg);
			csv.mailMagazineReceptionFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MailMagazineReceptionFlg.TYPE_CD, customer.mailMagazineReceptionFlg);
			csv.testFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.CustomerTestFlg.TYPE_CD, customer.testFlg);
			csv.loginFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.LoginFlg.TYPE_CD, customer.loginFlg);
			csv.publicationFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PublicationFlg.TYPE_CD, customer.publicationFlg);
			csv.scoutUseFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ScoutUseFlg.TYPE_CD, customer.scoutUseFlg);
		} catch (Exception e) {
			log.error(String.format("valueToNameConvertLogicの処理に失敗しました。顧客ID:[%d]", customer.id), e);
		}
		csv.unreadApplicationMailCount = mailService.countReceivedApplicationMail(customer.id, MailStatusEnum.UNOPENED, SenderKbnEnum.MEMBER, SenderKbnEnum.NO_MEMBER);
		csv.unReadScoutMailCount = mailService.countReceivedScoutMail(customer.id, MailStatusEnum.UNOPENED, SenderKbnEnum.MEMBER);


		CustomerProperty property;
		try {
			property = customerLogic.getDispData(customer.id, NumberUtils.toInt(getUserId()), getAuthLevel());
		} catch (WNoResultException e) {
			log.warn(String.format("顧客の情報(CustomerLogic#getDispData())が取得できませんでした。顧客ID:[%d]", customer.id), e);
			return csv;
		}

		if (CollectionUtils.isNotEmpty(property.mCustomerCompanyList)) {
			StringBuilder sb = new StringBuilder();
			for (MCustomerCompany customerCompany : property.mCustomerCompanyList) {
				sb.append(valueToNameConvertLogic.convertToCompanyName(new String[]{String.valueOf(customerCompany.companyId)}));
				sb.append("/");
				sb.append(valueToNameConvertLogic.convertToSalesName(new String[]{String.valueOf(customerCompany.salesId)}));
				sb.append(GourmetCareeConstants.CRLF);
			}
			csv.companySales = sb.toString();
		}



		createScoutRemain(csv, property);
		createScoutAddHistory(csv, property);

		MCustomerAccount account = customerAccountService.getMCustomerAccountByCustomerId(property.mCustomer.id);
		if (account != null) {
			csv.loginId = account.loginId;
		}



		try {
			TCustomerLoginHistory entity = customerLoginHistoryService.getEntityByCustomerId(customer.id);
			csv.lastLoginDatetimeStr = DATETIME_FORMAT.format(entity.lastLoginDatetime);
		} catch (WNoResultException e) {
			log.info(String.format("顧客の最終ログイン日時が取得できませんでした。顧客ID:[%d]", customer.id));
		}



		return csv;
	}


	/**
	 * スカウト残数
	 * @param csv
	 * @param property
	 */
	private void createScoutRemain(CustomerCsv csv, CustomerProperty property) {
		ScoutMailRemainRetDto remainDto = scoutMailLogic.createScoutMailRemain(property);
		StringBuilder sb = new StringBuilder();
		if (remainDto.freeScoutMailCount != null && remainDto.freeScoutMailCount.intValue() > 0) {
			sb.append("無料分 ")
				.append(remainDto.freeScoutMailCount)
				.append("通")
				.append(GourmetCareeConstants.CRLF);
		}

		if (CollectionUtils.isNotEmpty(remainDto.boughtScoutMailManageList)) {
			sb.append("購入分 ");
			boolean flg = false;
			for (VActiveScoutMail entity : remainDto.boughtScoutMailManageList) {
				if (flg) {
					sb.append(" / ");
				}
				sb.append(entity.scoutRemainCount)
					.append("通(")
					.append(DATE_FORMAT.format(entity.useEndDatetime))
					.append(")");

			}
			sb.append(GourmetCareeConstants.CRLF);
		}

		if (remainDto.unlimitedScoutMailCount != null && remainDto.unlimitedScoutMailCount.intValue() > 0) {
			sb.append("無期限分 ")
				.append(remainDto.unlimitedScoutMailCount)
				.append("通");
		}

		if (sb.length() == 0) {
			csv.scoutMailCountStr = "0";
		}
		csv.scoutMailCountStr = sb.toString();
	}







	/**
	 * スカウト追加履歴
	 * @param csv
	 * @param property
	 */
	private void createScoutAddHistory(CustomerCsv csv, CustomerProperty property) {
		List<ScoutMailLogDto> entityList;
		try {
			entityList = scoutMailLogic.getScoutMailAddLogList(property.mCustomer.id);
		} catch (WNoResultException e) {
			log.debug(String.format("スカウトメール追加履歴がありません。顧客ID:[%d]", property.mCustomer.id));
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (ScoutMailLogDto entity : entityList) {
			sb.append(DATE_FORMAT.format(entity.addDatetime))
				.append(" ")
				.append(entity.addScoutCount)
				.append("通 (");

			sb.append(valueToNameConvertLogic.convertToTypeName(MTypeConstants.ScoutMailKbn.TYPE_CD, entity.scoutMailKbn));
			if (GourmetCareeUtil.eqInt(MTypeConstants.ScoutMailLogKbn.ADD_MANUAL, entity.scoutMailLogKbn)) {
				sb.append("・手動");
			}
			sb.append(")");
			sb.append(GourmetCareeConstants.CRLF);
		}
		csv.scoutMailAddHistory = sb.toString();
	}

	/**
	 * 出力ファイル名作成
	 * @return
	 */
	private String createOutputFileName() {
		return String.format("customer_%s.csv", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}

}
