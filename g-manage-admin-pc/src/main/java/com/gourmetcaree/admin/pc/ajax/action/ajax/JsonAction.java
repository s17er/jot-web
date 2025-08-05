package com.gourmetcaree.admin.pc.ajax.action.ajax;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.RequestUtil;

import com.gourmetcaree.admin.service.logic.ShopListLogic;
import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.logic.LabelValueListLogic;
import com.gourmetcaree.common.logic.StationLogic;
import com.gourmetcaree.common.logic.TypeLogic;
import com.gourmetcaree.common.util.ResponseUtils;
import com.gourmetcaree.db.common.entity.MCity;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CityService;

/**
 * AjaxのレスポンスをJSONへ返却するアクション
 * {@link IndexAction} はViewを返しているため汎用性に欠ける。
 */
public class JsonAction {

    @Resource
    private LabelValueListLogic labelValueListLogic;

    @Resource
    private TypeLogic typeLogic;

    @Resource
    private ShopListLogic shopListLogic;

    @Resource
    private HttpServletRequest request;

    @Resource
    private StationLogic stationLogic;

    @Resource
    private CityService cityService;


    @Execute(validator = false)
    public String salesList() {
        final String companyId = RequestUtil.getRequest().getParameter("companyId");
        final String blankLineLabel = RequestUtil.getRequest().getParameter("blankLineLabel");
        List<LabelValueDto> salesList = labelValueListLogic.getAssignedSalesList(blankLineLabel,
                "",
                Collections.<Integer>emptyList(),
                companyId);

        ResponseUtils.writeJson(salesList);
        return null;
    }


    @Execute(validator = false)
    public String detailAreaList() {
        List<LabelValueDto> list = labelValueListLogic.eachAreaDetailAreaList(NumberUtils.toInt(request.getParameter("areaCd")),
                true,
                "｜");

        ResponseUtils.writeJson(list);
        return null;
    }

    /**
     * 店舗一覧を取得する
     * @return
     */
    @Execute(validator = false)
    public String shopList() {
        final String customerId = RequestUtil.getRequest().getParameter("customerId");
    	try {
			ResponseUtils.writeJson(shopListLogic.getWebShopListByCustomerId(Integer.parseInt(customerId)));
    	} catch (NumberFormatException e) {
			ResponseUtils.writeJson(null);
		}
    	return null;
    }

	/**
	 * 鉄道会社JSONを取得する
	 * @return
	 */
    @Execute(validator = false)
    public String railroad() {
		final String cd = RequestUtil.getRequest().getParameter("prefecturesCd");
		if (StringUtils.isEmpty(cd)) {
			ResponseUtils.writeJson(null);
		}
		try {
			ResponseUtils.writeJson(stationLogic.getRailroadCdName(Integer.parseInt(cd)));
    	} catch (NumberFormatException e) {
			ResponseUtils.writeJson(null);
		}
		return null;
    }

	/**
	 * 路線JSONを取得する
	 * @return
	 */
    @Execute(validator = false)
    public String route() {
    	final String prefecturesCd = RequestUtil.getRequest().getParameter("prefecturesCd");
		final String cd = RequestUtil.getRequest().getParameter("companyCd");
		if (StringUtils.isEmpty(cd)) {
			ResponseUtils.writeJson(null);
		}
		try {
			if (StringUtils.isNotEmpty(prefecturesCd)) {
				ResponseUtils.writeJson(stationLogic.getRouteCdName(Integer.parseInt(prefecturesCd), Integer.parseInt(cd)));
			}
			ResponseUtils.writeJson(stationLogic.getRouteCdName(Integer.parseInt(cd)));
    	} catch (NumberFormatException e) {
			ResponseUtils.writeJson(null);
		}
		return null;
    }

	/**
	 * 駅JSONを取得する
	 * @return
	 */
    @Execute(validator = false)
    public String station() {
		final String cd = RequestUtil.getRequest().getParameter("lineCd");
		if (StringUtils.isEmpty(cd)) {
			ResponseUtils.writeJson(null);
		}
		try {
			ResponseUtils.writeJson(stationLogic.getStationCdName(Integer.parseInt(cd)));
    	} catch (NumberFormatException e) {
			ResponseUtils.writeJson(null);
		}
		return null;
    }

    /**
     * ターミナルの駅グループJSONを取得する
     * @return
     */
    @Execute(validator = false)
    public String terminalStation() {
		final String terminalId = RequestUtil.getRequest().getParameter("terminalId");
		if (StringUtils.isEmpty(terminalId)) {
			ResponseUtils.writeJson(null);
		}
		try {
			ResponseUtils.writeJson(stationLogic.getTerminalStationList(Integer.parseInt(terminalId)));
    	} catch (NumberFormatException e) {
			ResponseUtils.writeJson(null);
		}
		return null;
    }

	/**
	 * 市区町村JSONを取得する
	 * @return
	 */
    @Execute(validator = false)
    public String city() {
		final String prefecturesCd = RequestUtil.getRequest().getParameter("prefecturesCd");
		if (StringUtils.isEmpty(prefecturesCd)) {
			ResponseUtils.writeJson(null);
		}
		try {
			List<MCity> cityList = cityService.findByPrefecturesCd(Integer.parseInt(prefecturesCd));
			ResponseUtils.writeJson(cityList.stream().map(s -> new CityDto(s.cityCd, s.cityName, s.cityNameKana)).collect(Collectors.toList()));
    	} catch (NumberFormatException | WNoResultException e) {
			ResponseUtils.writeJson(null);
		}
		return null;
    }

    /**
     * 市区町村のDTO
     */
    public class CityDto {
    	public CityDto(String ctiyCd, String cityName, String cityNameKana) {
    		this.cd = ctiyCd;
    		this.name = cityName;
    		this.kana = cityNameKana;
		}
    	public String cd;
    	public String name;
    	public String kana;
    }

}
