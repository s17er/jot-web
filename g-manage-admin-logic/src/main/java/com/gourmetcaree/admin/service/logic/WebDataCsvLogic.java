package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import com.gourmetcaree.admin.service.property.WebListCsvProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.WebdataListCsv;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebJobService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebShopListService;
import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;

/**
 * WEBデータ用CSVロジックです。
 * @author Takehiro Nakamori
 *
 */
public class WebDataCsvLogic extends AbstractAdminLogic {

	/** WEBリストサービス */
	@Resource
	private WebListService webListService;

	/** WEB属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** CSVコントローラファクトリ */
	@Resource
	protected S2CSVCtrlFactory s2CSVCtrlFactory;

	/** WEB店舗一覧サービス */
	@Resource
	private WebShopListService webShopListService;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	/** 職種サービス */
	@Resource
	private WebJobService webJobService;


	/**
	 * CSVの出力
	 * @param vWebListDto
	 * @throws WNoResultException
	 */
	public void outputWebListCsv(WebListCsvProperty property) throws WNoResultException {
		List<WebdataListCsv> csvList = createCsvList(property.vWebListDto);
		outPutCsv(csvList, property);
	}

	/**
	 * CSVを出力
	 * @param csvList
	 * @param property
	 */
	private void outPutCsv(List<WebdataListCsv> csvList, WebListCsvProperty property) {
		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property.fileName));
		PrintWriter out = null;
		S2CSVWriteCtrl<WebdataListCsv> csvWriter = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));
			csvWriter =
					s2CSVCtrlFactory.getWriteController(WebdataListCsv.class, out);

			int count = 0;
			for (WebdataListCsv csv : csvList) {
				csvWriter.write(csv);
				count ++;
			}

			property.count = count;
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * CSVリストの作成
	 * @param webListDto
	 * @return
	 * @throws WNoResultException
	 */
	private List<WebdataListCsv> createCsvList(VWebListDto webListDto) throws WNoResultException {
		SqlSelect<VWebList> select = webListService.getWebdataListForCsvA(webListDto);

		List<WebdataListCsv> csvList =
		select.iterate(new IterationCallback<VWebList, List<WebdataListCsv>>() {
			List<WebdataListCsv> retList = new ArrayList<WebdataListCsv>();
			@Override
			public List<WebdataListCsv> iterate(VWebList entity, IterationContext context) {
				if (entity == null) {
					return retList;
				}
				retList.add(createWebDataCsv(entity));
				return retList;
			}
		});

		if (CollectionUtils.isEmpty(csvList)) {
			throw new WNoResultException();
		}

		return csvList;
	}



	/**
	 * WEBデータのCSVを作成します。
	 * @param entity
	 * @return
	 */
	private WebdataListCsv createWebDataCsv(VWebList entity) {
		WebdataListCsv csv = new WebdataListCsv();
		Beans.copy(entity, csv).execute();
		csv.postDate = createPostDate(entity.volumeId);
		csv.webId = entity.id;
		csv.sizeName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SizeKbn.TYPE_CD, entity.sizeKbn);
		csv.salesName = valueToNameConvertLogic.convertToSalesName(new String[]{String.valueOf(entity.salesId)});

		// 職種をセット
		List<TWebJob> webJobList = webJobService.findByWebId(entity.id);
		if (CollectionUtils.isNotEmpty(webJobList)) {
			List<String> employJob = new ArrayList<>();
			for (TWebJob webJob : webJobList) {
				String employName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, webJob.employPtnKbn);
				String jobName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, webJob.jobKbn);
				String lowerSalaryPrice = "";
				String upperSalaryPrice = "";
				if (null != webJob.lowerSalaryPrice) {
					lowerSalaryPrice = webJob.lowerSalaryPrice + "円";
				}
				if (null != webJob.upperSalaryPrice) {
					upperSalaryPrice = webJob.upperSalaryPrice  + "円";
				}
				if (StringUtils.isNotBlank(lowerSalaryPrice)) {
					lowerSalaryPrice += "~";
				} else if (StringUtils.isNotBlank(upperSalaryPrice)) {
					upperSalaryPrice = "~" + upperSalaryPrice;
				}

				employJob.add(employName + "/"  + jobName + "/" + lowerSalaryPrice + upperSalaryPrice);
			}

			csv.employJob = StringUtils.join(employJob, ", ");
		} else {
			csv.employJob = "";
		}


		// 店舗数をセット
		if (entity.customerId != null) {
			int webShopCount = webShopListService.countByWebId(entity.id);
			csv.selectShops = String.valueOf(webShopCount);
			int shopCount = shopListService.countByCustomerId(entity.customerId);
			csv.allShops = String.valueOf(shopCount);
		}else {
			csv.selectShops = "";
			csv.allShops = "";
		}

		return csv;
	}

	/**
	 * 掲載期間の作成
	 * @param volumeId
	 * @return
	 */
	private String createPostDate(Integer volumeId) {
		if (volumeId == null) {
			return "";
		}

		try {
			MVolume mVolume = volumeService.findById(volumeId);
			SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
			StringBuilder sb = new StringBuilder("");
			sb.append(sdf.format(mVolume.postStartDatetime));
			sb.append(" ～ ");
			sb.append(sdf.format(mVolume.postEndDatetime));
			return sb.toString();
		} catch (SNoResultException e ) {
			return "";
		}
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 * @throws IOException
	 * @throws IOException
	 */
	private static String createOutputFilePath(String fileName) {

		String dateStr = null;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(fileName)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}
}
