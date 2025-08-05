package com.gourmetcaree.common.logic;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MRRailroad;
import com.gourmetcaree.db.common.entity.MRRoute;
import com.gourmetcaree.db.common.entity.MRStation;
import com.gourmetcaree.db.common.entity.MTerminalStation;
import com.gourmetcaree.db.common.entity.VStationGroup;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.RRailroadService;
import com.gourmetcaree.db.common.service.RRouteService;
import com.gourmetcaree.db.common.service.RStationService;
import com.gourmetcaree.db.common.service.SummaryRailRoadPrefService;
import com.gourmetcaree.db.common.service.SummaryRoutePrefService;
import com.gourmetcaree.db.common.service.TerminalStationService;

/**
 * 駅関連のロジック
 * @author whizz
 *
 */
public class StationLogic extends AbstractGourmetCareeLogic {

	@Resource
	private SummaryRailRoadPrefService summaryRailRoadPrefService;

	@Resource
	private SummaryRoutePrefService summaryRoutePrefService;

	@Resource
	private RRailroadService rRailroadService;

	@Resource
	private RRouteService rRouteService;

	@Resource
	private RStationService rStationService;

	@Resource
	private TerminalStationService terminalStationService;


	/**
	 * 鉄道会社DTO一覧を取得
	 * @param prefCd 都道府県コード
	 * @return 鉄道会社DTOリスト。取得できない場合は空のリスト
	 */
	public List<RailroadDto> getRailroad(int prefCd) {
		List<Integer> companyCdList = summaryRailRoadPrefService.getCompanyCdList(prefCd);
		try {
			List<MRRailroad> list = rRailroadService.findByCompanyCdList(companyCdList);
			return list.stream().map(s -> Beans.createAndCopy(RailroadDto.class, s).execute()).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 鉄道事業者コードと事業者名のDTOリストを取得
	 * @param prefCd
	 * @return 鉄道会社のコードと名前の入ったDTOリスト
	 */
	public List<CdNameDto> getRailroadCdName(int prefCd) {
		List<Integer> companyCdList = summaryRailRoadPrefService.getCompanyCdList(prefCd);
		try {
			List<MRRailroad> list = rRailroadService.findByCompanyCdList(companyCdList);
			return list.stream().map(s ->  new CdNameDto(s.companyCd, s.companyName)).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 路線DTO一覧を取得
	 * @param companyCd 鉄道事業者コード
	 * @return 路線DTOリスト。取得できない場合は空のリスト
	 */
	public List<RouteDto> getRoute(int companyCd) {
		try {
			List<MRRoute> list = rRouteService.findByCompanyCd(companyCd);
			return list.stream().map(s -> Beans.createAndCopy(RouteDto.class, s).execute()).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 路線DTO一覧を取得
	 * @param prefCd 都道府県コード
	 * @param companyCd 鉄道事業者コード
	 * @return 路線DTOリスト。取得できない場合は空のリスト
	 */
	public List<RouteDto> getRoute(int prefCd, int companyCd) {
		List<Integer> lineCdList = summaryRoutePrefService.getLineCdList(prefCd);
		try {
			List<MRRoute> list = rRouteService.findByLineCdList(companyCd, lineCdList);
			return list.stream().map(s -> Beans.createAndCopy(RouteDto.class, s).execute()).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 路線コードと路線名のDTOリストを取得
	 * @param companyCd 鉄道事業者コード
	 * @return 路線のコードと名前の入ったDTOリスト
	 */
	public List<CdNameDto> getRouteCdName(int companyCd) {
		try {
			List<MRRoute> list = rRouteService.findByCompanyCd(companyCd);
			return list.stream().map(s -> new CdNameDto(s.lineCd, s.lineName)).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 路線コードと路線名のDTOリストを取得
	 * @param int prefCd 都道府県コード
	 * @param int companyCd 鉄道事業者コード
	 * @return 路線のコードと名前の入ったDTOリスト
	 */
	public List<CdNameDto> getRouteCdName(int prefCd, int companyCd) {
		List<Integer> lineCdList = summaryRoutePrefService.getLineCdList(prefCd);
		try {
			List<MRRoute> list = rRouteService.findByLineCdList(companyCd, lineCdList);
			return list.stream().map(s -> new CdNameDto(s.lineCd, s.lineName)).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 駅DTO一覧を取得
	 * @param lineCd 路線コード
	 * @return 駅DTOリスト。取得できない場合は空のリスト
	 */
	public List<StationDto> getStation(int lineCd) {
		try {
			List<MRStation> list = rStationService.findByLineCd(lineCd);
			return list.stream().map(s -> Beans.createAndCopy(StationDto.class, s).execute()).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 駅コードと駅名のDTOリストを取得
	 * @param int lineCd 路線コード
	 * @return 駅のコードと名前の入ったDTOリスト
	 */
	public List<CdNameDto> getStationCdName(int lineCd) {
		try {
			List<MRStation> list = rStationService.findByLineCd(lineCd);
			return list.stream().map(s -> new CdNameDto(s.stationCd, s.stationName)).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * ターミナル駅に設定されている駅グループのリスト取得
	 * @param prefCd 都道府県コード
	 * @param tarminalId ターミナルID
	 * @return 駅グループのリスト。取得できない場合は空のリスト
	 */
	public List<VStationGroup> getTerminalStationList(Integer prefCd, int tarminalId) {
		try {
			List<MTerminalStation> list = terminalStationService.findByConditionInnerJoin(
					toCamelCase(VStationGroup.TABLE_NAME),
					new SimpleWhere()
						.eq(toCamelCase(MTerminalStation.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						.eq(toCamelCase(MTerminalStation.TERMINAL_ID), tarminalId)
						.eq(SqlUtils.dot(toCamelCase(VStationGroup.TABLE_NAME), toCamelCase(VStationGroup.PREFECTURES_CD)), prefCd),
						SqlUtils.createCommaStr(
								new String[]{
									toCamelCase(MTerminalStation.DISPLAY_ORDER),
									toCamelCase(MTerminalStation.ID)
								}));
			return list.stream().map(s -> s.vStationGroup).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * ターミナル駅に設定されている駅グループのリスト取得
	 * @param tarminalId ターミナルID
	 * @return 駅グループのリスト。取得できない場合は空のリスト
	 */
	public List<VStationGroup> getTerminalStationList(int tarminalId) {
		return getTerminalStationList(null, tarminalId);
	}

	/**
	 * コードと名前のDTO
	 */
	 public static class CdNameDto {
		public CdNameDto(Integer cd, String name) {
			this.cd = cd;
			this.name = name;
		}
		public Integer cd;
		public String name;
	}

	/**
	 * 鉄道会社DTO
	 */
	 public static class RailroadDto implements Serializable {

		private static final long serialVersionUID = -9089244556124300891L;

		/** 事業者コード */
		public Integer companyCd;

		/** 事業者名(一般) */
		public String companyName;

		/** 事業者名(一般・カナ) */
		public String companyNameK;

		/** 事業者名(正式名称) */
		public String companyNameH;

		/** 事業者名(略称) */
		public String companyNameR;
	}

	/**
	 * 路線DTO
	 */
	 public static class RouteDto implements Serializable {

		private static final long serialVersionUID = 7995283314521442631L;

		/** 路線コード */
		public Integer lineCd;

		/** 事業者コード */
		public Integer companyCd;

		/** 路線名称(一般) */
		public String lineName;

		/** 路線名称(一般・カナ) */
		public String lineNameK;

		/** 路線名称(正式名称) */
		public String lineNameH;

		/** 路線区分 */
		public Integer lineType;

		/** 中央経度 */
		public Double lon;

		/** 中央緯度 */
		public Double lat;
	}

	/**
	 * 駅DTO
	 */
	public static class StationDto implements Serializable {

		private static final long serialVersionUID = 616721229748612619L;

		/** 駅コード */
		public Integer stationCd;

		/** 駅グループコード */
		public Integer stationGCd;

		/** 駅名称 */
		public String stationName;

		/** 駅名称(カナ) */
		public String stationNameK;

		/** 駅名称(ローマ字) */
		public String stationNameR;

		/** 路線コード */
		public Integer lineCd;

		/** 都道府県コード */
		public Integer prefCd;

		/** 駅郵便番号 */
		public String post;

		/** 住所 */
		public String add;

		/** 経度 */
		public Double lon;

		/** 緯度 */
		public Double lat;
	}

}
