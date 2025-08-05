package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.dto.TerminalDto;
import com.gourmetcaree.db.common.entity.MRRailroad;
import com.gourmetcaree.db.common.entity.MRRoute;
import com.gourmetcaree.db.common.entity.MRStation;
import com.gourmetcaree.db.common.entity.MTerminalStation;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * ターミナル駅マスタのサービスクラスです。
 * @version 1.0
 */
public class TerminalStationService extends AbstractGroumetCareeBasicService<MTerminalStation> {

	/** 鉄道会社サービス */
	@Resource
	protected RRailroadService rRailroadService;

	/** 路線サービス*/
	@Resource
	protected RRouteService rRouteService;

	/** 駅サービス*/
	@Resource
	protected RStationService rStationService;

	/**
	 * ターミナル駅リストを取得する
	 * @return
	 */
	public List<TerminalDto> getTerminalIdList(String terminalId, String prefecturesCd) {

		 return jdbcManager.from(MTerminalStation.class)
		 	.where(new SimpleWhere()
		 	.eq(WztStringUtil.toCamelCase(MTerminalStation.TERMINAL_ID), terminalId)
		 	.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
		 	.iterate(new IterationCallback<MTerminalStation, List<TerminalDto>>() {
		 		List<TerminalDto> list = new ArrayList<>();
		 		@Override
		 		public List<TerminalDto> iterate(MTerminalStation entity, IterationContext context) {
		 			if (entity != null) {

		 				MRStation station = rStationService.findByStationCd(entity.stationCd);
		 				MRRoute mRoute = rRouteService.findByLineCd(station.lineCd);
		 				MRRailroad mRailroad = rRailroadService.findByCompanyCd(mRoute.companyCd);

		 				TerminalDto dto = new TerminalDto();
		 				dto.prefecturesCd = prefecturesCd;
		 				dto.companyCd = String.valueOf(mRailroad.companyCd);
		 				dto.lineCd = String.valueOf(mRoute.lineCd);
		 				dto.stationCd = String.valueOf(entity.stationCd);
		 				list.add(dto);
		 			}
		 			return list;
		 		}
			});
	}

	/**
	 * 駅から運営会社までを取得する
	 * @param prefecturesCd
	 * @param stationCd
	 * @return
	 */
	public TerminalDto getTerminalInfo(String prefecturesCd, String stationCd) {

		TerminalDto dto = new TerminalDto();

		MRStation station = rStationService.findByStationCd(Integer.parseInt(stationCd));
		MRRoute mRoute = rRouteService.findByLineCd(station.lineCd);
		MRRailroad mRailroad = rRailroadService.findByCompanyCd(mRoute.companyCd);

		dto.prefecturesCd = prefecturesCd;
		dto.companyCd = String.valueOf(mRailroad.companyCd);
		dto.lineCd = String.valueOf(mRoute.lineCd);
		dto.stationCd = String.valueOf(stationCd);

		return dto;
	}

	/**
	 * ターミナルIDに紐づくターミナル駅IDを取得する
	 * @param terminalId
	 * @return
	 */
	public List<String> getTerminalStationId(String terminalId) {

		 return jdbcManager.from(MTerminalStation.class)
				 	.where(new SimpleWhere()
				 	.eq(WztStringUtil.toCamelCase(MTerminalStation.TERMINAL_ID), terminalId)
				 	.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				 	.iterate(new IterationCallback<MTerminalStation, List<String>>() {
				 		List<String> list = new ArrayList<>();
				 		@Override
				 		public List<String> iterate(MTerminalStation entity, IterationContext context) {
				 			if (entity != null) {
				 				list.add(String.valueOf(entity.id));
				 			}
				 			return list;
				 		}
					});
	}
}
