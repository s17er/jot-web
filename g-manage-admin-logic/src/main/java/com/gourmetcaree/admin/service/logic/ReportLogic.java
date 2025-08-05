package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;

import com.gourmetcaree.admin.service.constants.AdminServiceConstants.SqlFileName;
import com.gourmetcaree.admin.service.dto.ReportListDetailDto;
import com.gourmetcaree.admin.service.dto.ReportListDetailSqlDto;
import com.gourmetcaree.admin.service.dto.ReportListDto;
import com.gourmetcaree.admin.service.dto.ReportListSqlDto;
import com.gourmetcaree.admin.service.property.ReportListDetailSqlProperty;
import com.gourmetcaree.admin.service.property.ReportListSqlProperty;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * レポート機能のロジッククラス
 * @author Takahiro Ando
 * @version 1.0
 *
 */
public class ReportLogic extends AbstractAdminLogic {

	/**
	 * レポートの一覧を取得します。。<br>
	 * VIEWから号数とステータス別に分かれたレコードを取得し、
	 * 号数毎のオブジェクトにステータス別の情報をセットします。
	 * セット後はそのオブジェクトを返却用のリストにセットして返します。
	 * データが存在しない場合はWNoResultExceptionをスローします。
	 * @param property
	 * @return 返却用のリスト
	 * @throws WNoResultException
	 */
	public List<ReportListDto> getReportList(ReportListSqlProperty property) throws WNoResultException {

		List<ReportListDto> retList = new ArrayList<ReportListDto>();

		try {
			List<ReportListSqlDto> selectResult = jdbcManager.selectBySqlFile(
																ReportListSqlDto.class, SqlFileName.REPORT_VOLUME_LIST, property)
																.disallowNoResult()
																.getResultList();

			Map<Integer, ReportListDto> map = new HashMap<Integer, ReportListDto>();
			Set<Integer> volumeSet = new LinkedHashSet<Integer>();

			for (ReportListSqlDto entity : selectResult) {

				//重複のないvolumeをソート順どおりに保持
				volumeSet.add(entity.volume);

				//volumeで一意のDTOを作成するため既にループを回っていればmapから取得する
				ReportListDto dto;
				if (map.containsKey(entity.volume)) {
					dto = map.get(entity.volume);
				} else {
					dto = new ReportListDto();
					dto.volumeId = entity.id;
					dto.volume = entity.volume;
					dto.postStartDatetime = entity.postStartDatetime;
					dto.postEndDatetime = entity.postEndDatetime;
				}

				//ループで回っているentityがもつステータスに一致するフィールドに値をセット
				if (entity.displayStatus == MStatusConstants.DisplayStatusCd.DRAFT) {
					dto.draftCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.REQ_APPROVAL) {
					dto.reqApprovalCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_WAIT) {
					dto.postWaitCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_DURING) {
					dto.postDuringCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_END) {
					dto.postEndCount = entity.statusCount;
				}

				//ループで初回登場時のみmapに保持
				if (!map.containsKey(entity.volume)) {
					map.put(entity.volume, dto);
				}
			}

			//ソート順通りに戻り値用リストにセット
			for (int key : volumeSet) {
				retList.add(map.get(key));
			}

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return retList;
	}

	/**
	 * 特定の号数で絞ったWebデータをレポート管理の詳細画面用に加工して取得します。
	 * データが存在しない場合はWNoResultExceptionをスローします。
	 * @param property
	 * @return
	 * @throws WNoResultException
	 */
	public List<ReportListDetailDto> getReportDetailList(ReportListDetailSqlProperty property) throws WNoResultException {

		List<ReportListDetailDto> retListNoSales = createReportDetailNoSalesList(property);
		List<ReportListDetailDto> retList = createReportDetailList(property);

		if (CollectionUtils.isEmpty(retListNoSales) && CollectionUtils.isEmpty(retList)) {
			throw new WNoResultException("レポート詳細を表示時に営業担当者ありも、営業担当者なしのデータも存在しませんでした。volumeId" + property.volumeId);
		}

		List<ReportListDetailDto> ret = new ArrayList<ReportListDetailDto>(0);

		//営業担当者がNullのリストが存在すれば戻り値用リストの先頭に移し替える
		if (!CollectionUtils.isEmpty(retListNoSales) ) {
			for (ReportListDetailDto dto : retListNoSales) {
				ret.add(dto);
			}
		}

		//通常のリストの中身を移し替える
		if (!CollectionUtils.isEmpty(retList) ) {
			for (ReportListDetailDto dto : retList) {
				ret.add(dto);
			}
		}

		return ret;
	}



	/**
	 * 特定の号数で絞ったレポートの一覧を取得する。<br>
	 * 号数を条件にVIEWから営業担当者とステータス別に分かれたレコードを取得し、
	 * 営業担当者毎のオブジェクトにステータス別の情報をセットします。
	 * セット後はそのオブジェクトを返却用のリストにセットして返します。
	 * @param property
	 * @return
	 */
	private List<ReportListDetailDto> createReportDetailList(ReportListDetailSqlProperty property) {

		try {
			List<ReportListDetailSqlDto> selectResult = jdbcManager
															.selectBySqlFile(ReportListDetailSqlDto.class,
																	SqlFileName.REPORT_DETAIL_LIST, property)
															.disallowNoResult()
															.getResultList();

			Map<Integer, ReportListDetailDto> map = new HashMap<Integer, ReportListDetailDto>();
			Set<Integer> salesSet = new LinkedHashSet<Integer>();

			for (ReportListDetailSqlDto entity : selectResult) {

				//重複のないsalesIdをソート順どおりに保持
				salesSet.add(entity.salesId);

				//salesIdで一意のDTOを作成するため既にループを回っていればmapから取得する
				ReportListDetailDto dto;
				if (map.containsKey(entity.salesId)) {
					dto = map.get(entity.salesId);
				} else {
					dto = new ReportListDetailDto();
					dto.salesId = entity.salesId;
					dto.companyId = entity.companyId;
				}

				//ループで回っているentityがもつステータスに一致するフィールドに値をセット
				if (entity.displayStatus == MStatusConstants.DisplayStatusCd.DRAFT) {
					dto.draftCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.REQ_APPROVAL) {
					dto.reqApprovalCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_WAIT) {
					dto.postWaitCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_DURING) {
					dto.postDuringCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_END) {
					dto.postEndCount = entity.statusCount;
				}

				//ループで初回登場時のみmapに保持
				if (!map.containsKey(entity.salesId)) {
					map.put(entity.salesId, dto);
				}
			}

			List<ReportListDetailDto> retList = new ArrayList<ReportListDetailDto>(salesSet.size());

			//ソート順通りに戻り値用リストにセット
			for (int key : salesSet) {
				retList.add(map.get(key));
			}

			return retList;

		} catch (SNoResultException e) {
			return new ArrayList<ReportListDetailDto>(0);
		}
	}

	/**
	 * 特定の号数で絞ったレポートの一覧を取得する。<br>
	 * 号数を条件にVIEWから営業担当者がNULLのものをステータス別に分かれたレコードとして取得し、
	 * ステータス別の情報をセットします。
	 * セット後はそのオブジェクトを返却用のリストにセットして返します。
	 * @param property
	 * @return
	 */
	private List<ReportListDetailDto> createReportDetailNoSalesList(ReportListDetailSqlProperty property) {

		try {
			List<ReportListDetailSqlDto> selectResult = jdbcManager
															.selectBySqlFile(ReportListDetailSqlDto.class,
																	SqlFileName.REPORT_DETAIL_NOSALES_LIST, property)
															.disallowNoResult()
															.getResultList();

			Map<Integer, ReportListDetailDto> map = new HashMap<Integer, ReportListDetailDto>();
			Set<Integer> companySet = new LinkedHashSet<Integer>();

			for (ReportListDetailSqlDto entity : selectResult) {

				//重複のないcompanyIdをソート順どおりに保持
				companySet.add(entity.companyId);

				//companyIdで一意のDTOを作成するため既にループを回っていればmapから取得する
				ReportListDetailDto dto;
				if (map.containsKey(entity.companyId)) {
					dto = map.get(entity.companyId);
				} else {
					dto = new ReportListDetailDto();
					dto.salesId = entity.salesId;
					dto.companyId = entity.companyId;
				}

				//ループで回っているentityがもつステータスに一致するフィールドに値をセット
				if (entity.displayStatus == MStatusConstants.DisplayStatusCd.DRAFT) {
					dto.draftCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.REQ_APPROVAL) {
					dto.reqApprovalCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_WAIT) {
					dto.postWaitCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_DURING) {
					dto.postDuringCount = entity.statusCount;
				} else if (entity.displayStatus == MStatusConstants.DisplayStatusCd.POST_END) {
					dto.postEndCount = entity.statusCount;
				}

				//ループで初回登場時のみmapに保持
				if (!map.containsKey(entity.companyId)) {
					map.put(entity.companyId, dto);
				}
			}

			List<ReportListDetailDto> retList = new ArrayList<ReportListDetailDto>(companySet.size());

			//ソート順通りに戻り値用リストにセット
			for (int key : companySet) {
				retList.add(map.get(key));
			}

			return retList;

		} catch (SNoResultException e) {
			return new ArrayList<ReportListDetailDto>(0);
		}
	}

}
