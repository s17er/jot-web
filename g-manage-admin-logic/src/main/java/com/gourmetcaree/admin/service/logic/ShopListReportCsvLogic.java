package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ShopListReportCsv;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerCompanyService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.CustomerCompanyService.CompanySalesNameDto;
import com.gourmetcaree.functionalInterface.JdbcIterator;

/**
 * 店舗一覧レポート用CSVロジック
 * @author Takehiro Nakamori
 *
 */
public class ShopListReportCsvLogic extends AbstractAdminLogic {


	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	@Resource
	private CustomerService customerService;
	@Resource
	private CustomerCompanyService customerCompanyService;

	/** タイプサービス */
	@Resource
	private TypeService typeService;


	/**
	 * CSVのアウトプットを行います。
	 */
	public void outputReportCsv() throws WNoResultException, IOException {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		createSelectSql(sql, params);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0) {
			throw new WNoResultException("店舗一覧が存在しません。");
		}

		addOrderBy(sql);

		response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
		response.setHeader("Content-Disposition", String.format("attachment; filename=shopListReport%s.csv", new SimpleDateFormat("yyyyMMdd").format(new Date())));

		Writer writer = null;
		S2CSVWriteCtrl<ShopListReportCsv> csvWriter = null;

		try {
			writer = new OutputStreamWriter(response.getOutputStream(), GourmetCareeConstants.CSV_ENCODING);
			csvWriter = s2CSVCtrlFactory.getWriteController(ShopListReportCsv.class, writer);
			SqlSelect<ShopListReportSelectDto> select = jdbcManager.selectBySql(ShopListReportSelectDto.class, sql.toString(), params.toArray());
			writeCsv(csvWriter, select);

		}  finally {
			if (csvWriter != null) {
				csvWriter.close();
			}

			if (writer != null) {
				writer.close();
			}

		}
	}


	/**
	 * CSVを書き出します。
	 * @param csvWriter csvライター
	 * @param select
	 */
	private void writeCsv(final S2CSVWriteCtrl<ShopListReportCsv> csvWriter, SqlSelect<ShopListReportSelectDto> select) {

		select.iterate(new IterationCallback<ShopListReportCsvLogic.ShopListReportSelectDto, Void>() {
			@Override
			public Void iterate(ShopListReportSelectDto entity, IterationContext context) {
				if (entity == null) {
					return null;
				}

				ShopListReportCsv csv = Beans.createAndCopy(ShopListReportCsv.class, entity).execute();
				csv.address = String.format("%s%s", StringUtils.defaultString(entity.address1), StringUtils.defaultString(entity.address2));
				if (StringUtils.isNotBlank(entity.phoneNo1)
						&& StringUtils.isNotBlank(entity.phoneNo2)
						&& StringUtils.isNotBlank(entity.phoneNo3)) {
					csv.phoneNo = String.format("%s-%s-%s", entity.phoneNo1, entity.phoneNo2, entity.phoneNo3);
				}

				createAttributeName(entity, csv);
				createCustomerInfo(entity, csv);
				csvWriter.write(csv);
				return null;
			}
		});
	}


	private void createCustomerInfo(ShopListReportSelectDto dto, ShopListReportCsv csv) {
		final MCustomer customer;
		if (dto.customerId == null) {
			logicLog.warn(String.format("店舗一覧の顧客IDがNULLです。店舗ID:[%d]", dto.id));
		}
		try {
			customer = customerService.findById(dto.customerId);
		} catch (SNoResultException e) {
			logicLog.warn(String.format("顧客データの取得に失敗しました。顧客ID:[%d]", dto.customerId), e);
			return;
		}

		csv.customerName = customer.customerName;

		final StringBuilder salesName = new StringBuilder();
		final StringBuilder companyName = new StringBuilder();
		customerCompanyService.selectCustomerCompanyName(customer.id, new JdbcIterator<CustomerCompanyService.CompanySalesNameDto>() {

			int index = 0;
			@Override
			public void iterate(CompanySalesNameDto entity) {
				if (index++ > 0) {
					salesName.append(", ");
					companyName.append(", ");
				}

				salesName.append(entity.salesName);
				companyName.append(entity.companyName);
			}
		});

		csv.salesName = salesName.toString();
		csv.companyName = companyName.toString();

	}

	/**
	 * 属性名を作成
	 * @param csv
	 */
	private void createAttributeName(ShopListReportSelectDto entity, ShopListReportCsv csv) {
		csv.industryName = createAttributeName(entity.industryKbn1, MTypeConstants.IndustryKbn.TYPE_CD);
		csv.displayFlgName = createAttributeName(entity.displayFlg, MTypeConstants.ShopListDisplayFlg.TYPE_CD);
		csv.jobOfferFlgName = createAttributeName(entity.jobOfferFlg, MTypeConstants.JobOfferFlg.TYPE_CD);
	}

	/**
	 * 属性名を作成
	 * @param attributeValue 属性値
	 * @param attributeCd
	 * @return
	 */
	private String createAttributeName(Integer attributeValue, String attributeCd) {
		if (attributeValue == null
				|| StringUtils.isBlank(attributeCd)) {
			return "";
		}

		return typeService.getTypeName(attributeCd, attributeValue);
	}





	/**
	 * 検索SQLの作成
	 * @param sql
	 * @param params
	 */
	private void createSelectSql(StringBuffer sql, List<Object> params) {
		sql.append(" SELECT ");
		sql.append("   SHOP.* ");
		sql.append(" FROM ");
		sql.append("   t_shop_list SHOP  ");
		sql.append("   INNER JOIN m_customer CUS  ");
		sql.append("     ON SHOP.customer_id = CUS.id  ");
		sql.append(" WHERE ");
		sql.append("   SHOP.status = ?  ");
		sql.append("   AND SHOP.delete_flg = ? ");
		sql.append("   AND CUS.delete_flg = ? ");

		params.add(MTypeConstants.ShopListStatus.REGISTERED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

	}

	/**
	 * オーダーバイ句を追加
	 * @param sql
	 */
	private void addOrderBy(StringBuffer sql) {
		sql.append(" ORDER BY SHOP.")
			.append(TShopList.CUSTOMER_ID)
			.append(" ASC, SHOP.")
			.append(TShopList.DISP_ORDER);
	}


	/**
	 * レポート用セレクトDTO
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ShopListReportSelectDto extends TShopList {

		/**
		 *
		 */
		private static final long serialVersionUID = -14001004210671066L;

	}
}
