package com.gourmetcaree.admin.service.logic;

import static com.gourmetcaree.common.enums.ManageAuthLevel.*;
import static com.gourmetcaree.common.util.SqlUtils.*;
import static com.gourmetcaree.db.common.constants.MStatusConstants.DisplayStatusCd.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.UUID;

import com.google.common.collect.Lists;
import com.gourmetcaree.admin.service.dto.IPPhoneDataCopyDto;
import com.gourmetcaree.admin.service.dto.WebdataControlDto;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.logic.TagListLogic;
import com.gourmetcaree.common.logic.TypeMappingLogic;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MStatusConstants.DBStatusCd;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.AttentionShopFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.entity.MWebTagMapping;
import com.gourmetcaree.db.common.entity.TMaterial;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.entity.TWebJobAttribute;
import com.gourmetcaree.db.common.entity.TWebJobShopList;
import com.gourmetcaree.db.common.entity.TWebRoute;
import com.gourmetcaree.db.common.entity.TWebShopList;
import com.gourmetcaree.db.common.entity.TWebSpecial;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.NoExistCompanyDataException;
import com.gourmetcaree.db.common.exception.NoExistSalesDataException;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationTestService;
import com.gourmetcaree.db.common.service.CompanyAreaService;
import com.gourmetcaree.db.common.service.CompanyService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.DetailAreaGroupMappingService;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.SalesService;
import com.gourmetcaree.db.common.service.TypeMappingService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebJobAttributeService;
import com.gourmetcaree.db.common.service.WebJobService;
import com.gourmetcaree.db.common.service.WebJobShopListService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebRouteService;
import com.gourmetcaree.db.common.service.WebSearchService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.common.service.WebShopListService;
import com.gourmetcaree.db.common.service.WebSpecialService;
import com.gourmetcaree.db.common.service.WebTagMappingService;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;
import com.gourmetcaree.db.webdata.dto.webdata.WebSearchDto;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * WEBデータロジッククラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
public class WebdataLogic extends AbstractAdminLogic {

	/** WEBデータのサービス */
	@Resource
	protected WebService webService;

	/** WEBデータ属性のサービス */
	@Resource
	protected WebAttributeService webAttributeService;

	/** WEBデータ路線図のサービス */
	@Resource
	protected WebRouteService webRouteService;

	/** WEBデータ特集のサービス */
	@Resource
	protected WebSpecialService webSpecialService;

	/** 素材のサービス */
	@Resource
	protected MaterialService materialService;

	/** 顧客のロジック */
	@Resource
	protected CustomerLogic customerLogic;

	/** 顧客のサービス */
	@Resource
	protected CustomerService customerService;

	/** 会社マスタのサービス */
	@Resource
	protected CompanyService companyService;

	/** 会社エリアマスタのサービス */
	@Resource
	protected CompanyAreaService companyAreaService;

	/** 号数マスタのサービス */
	@Resource
	protected VolumeService volumeService;

	/** 営業担当者マスタのサービス */
	@Resource
	protected SalesService salesService;

	/** 応募テストのサービス */
	@Resource
	protected ApplicationTestService applicationTestService;

	/** スカウトメールロジック */
	@Resource
	private ScoutMailLogic scoutMailLogic;

	@Resource
	private IPPhoneLogic iPPhoneLogic;

	@Resource
	private WebListService webListService;

	@Resource
    private TypeMappingLogic typeMappingLogic;

	@Resource
	private TypeMappingService typeMappingService;

	@Resource
    private TypeService typeService;

	@Resource
	DetailAreaGroupMappingService detailAreaGroupMappingService;

	@Resource
	private WebJobService webJobService;

	@Resource
	private WebJobAttributeService webJobAttributeService;

	@Resource
	private WebJobShopListService webJobShopListService;

	@Resource
	private WebShopListService webShopListService;

	@Resource
	private ShopListLogic shopListLogic;

	@Resource
	private TagListLogic tagListLogic;

	@Resource
	private WebTagMappingService webTagMappingService;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	private WebSearchService webSearchService;

	/**
	 * WEBデータのエリアと、ログインしている会社に登録されているエリアが同じかどうかチェックを行う。<br />
	 * プロパティのWEBデータエンティティにエリアコードをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return チェックするエリアと、会社に登録されたエリアが同じの場合はtrue、違う場合はfalse
	 * @throws WNoResultException データが存在しない場合はエラー
	 */
	public boolean isCompanyArea(WebdataProperty property) throws WNoResultException {

		// プロパティの中身チェック
		checkEmptyProperty(property);

		// 会社エリアマスタからエリアコードを取得
		List<Integer> areaCdList = companyAreaService.getCompanyAreaCd(getCompanyId());

		// 会社のエリアとWEBデータが同じかチェック
		for (int areaCd : areaCdList) {
			if (areaCd == property.areaCd) {
				// エリアが同じ場合はtrueを返す
				return true;
			}
		}

		// エリアが同じでない場合はfalseを返す
		return false;
	}

	/**
	 * WEBデータのエリアと、登録する顧客に登録されているエリアが同じかどうかチェックを行う。<br />
	 * プロパティのWEBデータエンティティにエリア、顧客エンティティにIDをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return チェックするエリアと、顧客に登録されたエリアが同じの場合はtrue、違う場合はfalse
	 * @throws WNoResultException 顧客データが存在しない場合はエラー
	 */
	public boolean isCustomerArea(WebdataProperty property) throws WNoResultException {

		// プロパティの中身チェック
		checkEmptyProperty(property);
		try {
			// 顧客データを取得
			MCustomer mCustomer = customerService.findById(property.customerId);

			// 選択した顧客と同じエリアかどうかをチェック
			return mCustomer.areaCd.equals(property.areaCd);

		// データが取得できない場合は、エラーを返却
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

	}

	/**
	 * WEBデータの会社と、登録する顧客に登録されている会社が同じかどうかチェックを行う。<br />
	 * プロパティに会社ID、顧客IDをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return チェックする会社と、顧客に登録された会社が同じの場合はtrue、違う場合はfalse
	 * @throws WNoResultException 顧客データが存在しない場合はエラー
	 */
	public boolean isCustomerCompany(WebdataProperty property) throws WNoResultException {

		// プロパティの中身チェック
		checkEmptyProperty(property);

		CustomerProperty customerProperty = new CustomerProperty();

		customerProperty.mCustomer = new MCustomer();
		customerProperty.mCustomer.id = property.customerId;

		// データ検索
		customerLogic.getListRowData(customerProperty);

		// 選択した会社と同じ担当会社かどうかをチェック
		boolean chekCompany = false;
		for (MCustomerCompany mCustomerCompany : customerProperty.mCustomer.mCustomerCompanyList) {
			if(mCustomerCompany.companyId.equals(property.companyId)) {
				chekCompany = true;
				break;
			}
		}

		// チェックした結果を返す
		return chekCompany;

	}

	/**
	 * 権限によって選択可能な会社かどうかをチェックする。<br />
	 * プロパティに会社IDをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 選択可能な会社の場合はtrue、選択不可能な場合はfalseを返す
	 * @throws WNoResultException 会社データが存在しない場合はエラー
	 */
	public boolean canSelectCompany(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyProperty(property);

		// 自社スタッフの場合、代理店以外を選択しているかチェック
		if (STAFF.value().equals(getAuthLevel()) || SALES.value().equals(getAuthLevel())) {

			try {
				// 会社マスタからデータを取得
				MCompany mCompany = companyService.findById(property.companyId);

				// 代理店以外ならtrueを返す
				return MTypeConstants.AgencyFlg.NOT_AGENCY == mCompany.agencyFlg;

			// データが取得できないときはエラー
			} catch (SNoResultException e) {
				throw new WNoResultException();
			}

		// 代理店の場合、会社が存在するか、また代理店か、ログインユーザの会社かチェック
		} else if (AGENCY.value().equals(getAuthLevel())) {

			try {
				// 会社マスタからデータを取得
				MCompany mCompany = companyService.findById(property.companyId);
				// 代理店以外ならfalseを返す
				if (MTypeConstants.AgencyFlg.NOT_AGENCY == mCompany.agencyFlg) {
					return false;
				}

				// 自身の会社でなければfalse
				return String.valueOf(property.companyId).equals(getCompanyId());

				// データが取得できないときはエラー
			} catch (SNoResultException e) {
				throw new WNoResultException();
			}
		}

		// 管理者ならチェックしない
		return true;
	}

	/**
	 * 会社マスタからデータを取得して、プロパティにセットする。<br />
	 * プロパティのWEBデータエンティティに会社IDをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @exception WNoResultException データが無い場合はエラー
	 */
	public void setCompanyEntity(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		try {
			// 会社マスタからデータを取得
			property.mCompany = companyService.findById(property.tWeb.companyId);
		// 取得できない場合はエラー
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * ログイン会社を会社マスタからデータを取得して、プロパティにセットする。<br />
	 * @param property WEBデータプロパティ
	 * @exception WNoResultException データが無い場合はエラー
	 */
	public void setLoginCompanyEntity(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		try {
			// 会社マスタからデータを取得
			property.loginCompany = companyService.findById(Integer.parseInt(getCompanyId()));
		// 取得できない場合はエラー
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 営業担当者マスタからデータを取得して、プロパティにセットする。<br />
	 * プロパティのWEBデータエンティティに営業担当者IDをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @exception WNoResultException データが無い場合はエラー
	 */
	public void setSalesEntity(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		try {
			// 営業担当者マスタからデータを取得
			property.mSales = salesService.findById(property.tWeb.salesId);
			// 取得できない場合はエラー
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 指定した会社が代理店かどうかを判別する。<br />
	 * プロパティのWEBデータエンティティに会社IDをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 代理店の場合true、代理店以外の場合false
	 * @exception WNoResultException データが無い場合はエラー
	 */
	public boolean isAgencyCompany(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyProperty(property);

		// 会社マスタがなければ取得
		if (property.mCompany == null) {
			setCompanyEntity(property);
		}
		// 代理店フラグが代理店以外(0)の場合false
		return MTypeConstants.AgencyFlg.NOT_AGENCY == property.mCompany.agencyFlg ? false : true;
	}

	public boolean setWorkContent(String workContent) {
		return true;
	}

	/**
	 * ログイン会社が存在するかどうかを判別する。<br />
	 * 取得したログイン会社をセットするために、WEBデータプロパティを引数に指定。
	 * @param property WEBデータプロパティ
	 * @return ログイン会社が存在する場合true、存在しない場合false
	 */
	private boolean isLoginCompanyExsists(WebdataProperty property) {

		// プロパティの中身をチェック
		checkEmptyProperty(property);

		// ログイン会社マスタがなければ取得
		if (property.loginCompany == null) {
			try {
				setLoginCompanyEntity(property);
			// データが存在しない場合false
			} catch (WNoResultException e) {
				return false;
			}
		}
		//会社マスタにデータが存在すればtrue
		return true;
	}

	/**
	 * WEBデータの登録を行う。<br />
	 * プロパティのWEBデータエンティティ、WEBデータ属性リスト、<br />
	 * WEBデータ路線図リスト、WEBデータ特集リスト、素材リストに値をセットして呼び出し。
	 * @param property 登録するプロパティ
	 */
	public void insertWebData(WebdataProperty property) {

		// プロパティの中身チェック
		checkEmptyPropertyWeb(property);

		// WEBデータ登録のために値をセット
		setTWeb(property);
		// WEBの登録
		webService.insert(property.tWeb);

		// 求人識別番号が指定されていない場合、求人IDを求人識別番号として登録
		if (property.tWeb.webNo == null) {
			webService.updateWebNoById(property.tWeb);
		}

		// WEBデータ属性の登録
		if (property.tWebAttributeList != null && !property.tWebAttributeList.isEmpty()) {
			addTWebAttributeValue(property);
			webAttributeService.insertBatch(property.tWebAttributeList);
		}

		// WEBデータ特集の登録
		if (property.tWebSpecialList != null && !property.tWebSpecialList.isEmpty()) {
			addTWebSpecialValue(property);
			webSpecialService.insertBatch(property.tWebSpecialList);
		}

		// 素材の登録
		if (property.tMaterialList != null && !property.tMaterialList.isEmpty()) {
			addTMaterialValue(property);
		}

		// WEBデータ系列店舗の登録
		if (CollectionUtils.isNotEmpty(property.tWebShopListList)) {
			webShopListService.deleteInsert(property.tWeb.id, property.tWebShopListList);
		}

		// WEBデータ職種の登録
		if (CollectionUtils.isNotEmpty(property.tWebJobList)) {
			addWebJobValue(property);
		}

		// キーワードの更新
		updateKeywork(property.tWeb.id);

		// 検索テーブルの更新
		updateWebSearch(property);

		property = null;
	}

	/**
	 * WEBデータのステータス更新を行い、制御Dtoを返却する。<br />
	 * プロパティのWEBデータエンティティに更新項目をセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @param changeStatusKbn 変更ステータス区分
	 * @return WEBデータ処理可否の制御設定
	 * @throws WNoResultException データが存在しない場合はエラー
	 * @throws NoExistCompanyDataException 会社が見つからない場合エラー
	 * @throws NoExistSalesDataException 営業担当者が見つからない場合エラー
	 */
	public WebdataControlDto updateStatus(WebdataProperty property, String changeStatusKbn)
									throws WNoResultException, NoExistCompanyDataException, NoExistSalesDataException {

		// 引数のチェック
		checkEmptyPropertyWeb(property);

		// UPDATE対象のみ保持
		TWeb entity = property.tWeb;

		// データの取得
		getWebdataDetailExcludesMatelial(property);

		// 掲載確定の場合のみ、会社マスタ、営業担当者マスタが削除されていればエラーとする
		if (String.valueOf(MTypeConstants.ChangeStatusKbn.FIXED_VALUE).equals(changeStatusKbn)) {

			try {
				// 会社マスタを取得
				setCompanyEntity(property);

			// 会社が取得できない場合、エラー
			} catch (WNoResultException e) {
					property = null;
					throw new NoExistCompanyDataException("会社データが取得できません。" + property);
			}

			try {
				// 営業担当者IDが取得できなければエラー
				if (property.tWeb.salesId == null) {
					property = null;
					throw new NoExistSalesDataException("営業担当者が取得できません。" + property);
				}

				// 営業担当者マスタを取得
				setSalesEntity(property);

			// 営業担当者が取得できない場合、エラー
			} catch (WNoResultException e) {
				property = null;
				throw new NoExistSalesDataException("営業担当者が取得できません。" + property);
			}
		}

		// 画面制御のため制御Dtoをセット
		WebdataControlDto controlDto = setControlFlg(property);

		// 掲載確定で、処理可能な場合
		if (String.valueOf(MTypeConstants.ChangeStatusKbn.FIXED_VALUE).equals(changeStatusKbn)
				&& controlDto.fixedFlg) {
				// ステータスを掲載確定に変更
				entity.status = MStatusConstants.DBStatusCd.POST_FIXED;
				// 掲載確定担当者をセット
				entity.fixedUserId = Integer.parseInt(getUserId());
				// 掲載確定日時をセット
				entity.fixedDatetime = new Date();

		// 確定取消で、処理可能な場合
		} else if (String.valueOf(MTypeConstants.ChangeStatusKbn.CANCEL_VALUE).equals(changeStatusKbn)
				&& controlDto.cancelFlg) {
			// ステータスを下書きに変更
			entity.status = MStatusConstants.DBStatusCd.DRAFT;
			// 確定取り消しの場合はチェックステータスを未チェックにする。
			entity.checkedStatus = MTypeConstants.WebdataCheckedStatus.UNCHECKED;

		// 掲載依頼で、処理可能な場合
		} else if (String.valueOf(MTypeConstants.ChangeStatusKbn.POST_REQUEST_VALUE).equals(changeStatusKbn)
				&& controlDto.postRequestFlg) {
			// ステータスを承認中に変更
			entity.status = MStatusConstants.DBStatusCd.REQ_APPROVAL;
			// 掲載依頼担当者をセット
			entity.requestUserId = Integer.parseInt(getUserId());
			// 掲載依頼日時をセット
			entity.requestDatetime = DateUtils.getJustDateTime();

		// 掲載終了で、処理可能な場合
		} else if (String.valueOf(MTypeConstants.ChangeStatusKbn.POST_END_VALUE).equals(changeStatusKbn)
				&& controlDto.postEndFlg) {
			// ステータスを掲載終了に変更
			entity.status = MStatusConstants.DBStatusCd.POST_END;

		// 上記以外の場合はエラー
		} else {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// データ更新
		webService.update(entity);

		entity = null;
		property = null;

		// メール送信処理用に返却
		return controlDto;
	}

	/**
	 * WEBデータのWEB非表示フラグの更新を行い、制御Dtoを返却する。<br />
	 * プロパティのWEBデータエンティティに更新項目をセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @param publicationEndDisplayFlg WEB表示判定フラグ
	 * @return WEBデータ処理可否の制御設定
	 * @throws WNoResultException データが存在しない場合はエラー
	 * @throws NoExistCompanyDataException 会社が見つからない場合エラー
	 * @throws NoExistSalesDataException 営業担当者が見つからない場合エラー
	 */
	public WebdataControlDto updatePublicationEndDisplayFlg(WebdataProperty property, Integer publicationEndDisplayFlg)
			throws WNoResultException, NoExistCompanyDataException, NoExistSalesDataException {

		// 引数のチェック
		checkEmptyPropertyWeb(property);

		// UPDATE対象のみ保持
		TWeb entity = property.tWeb;

		// データの取得
		getWebdataDetailExcludesMatelial(property);

		// 画面制御のため制御Dtoをセット
		WebdataControlDto controlDto = setControlFlg(property);

		// WEB表示フラグを非表示に変更
		entity.publicationEndDisplayFlg = MTypeConstants.PublicationEndDisplayFlg.DISPLAY_NG;

		// データ更新
		webService.update(entity);

		entity = null;
		property = null;

		// メール送信処理用に返却
		return controlDto;
	}

	/**
	 * WEBデータの更新を行う。<br />
	 * プロパティのWEBデータエンティティ、WEBデータ属性リスト、<br />
	 * WEBデータ路線図リスト、WEBデータ特集リスト、素材リストに更新する値をセットして呼び出し。
	 *
	 * 更新をする場合、ユーザ権限に関わらずチェック状態を未チェックにする
	 *
	 * @param property WEBデータプロパティ
	 */
	public void updateWebData(WebdataProperty property) {

		// プロパティの中身チェック
		checkEmptyPropertyWeb(property);

		TWeb oldWeb = webService.findById(property.tWeb.id);
		// 更新対象の原稿がないため、不正エラー
		if (oldWeb == null) {
			throw new FraudulentProcessException();
		}

		// 更新をする場合、ユーザ権限に関わらずチェック状態を未チェックにする
		property.tWeb.checkedStatus = MTypeConstants.WebdataCheckedStatus.UNCHECKED;

		changeScoutMail(property.tWeb, oldWeb, property.addScoutMailCount);

		// 系列店舗の表示フラグを設定
		property.tWeb.shopListDisplayKbn =
			CollectionUtils.isNotEmpty(property.tWebShopListList)
				? MTypeConstants.ShopListDisplayKbn.ARI
				: MTypeConstants.ShopListDisplayKbn.NASHI;

		// WEBの登録
		webService.updateWithNull(property.tWeb);

		/* 関連データのDelete/Insert */
		// WEBデータ属性の削除
		webAttributeService.deleteTWebAttributeByWebId(property.tWeb.id);
		// WEBデータ属性の登録
		if (property.tWebAttributeList != null && !property.tWebAttributeList.isEmpty()) {
			addTWebAttributeValue(property);
			webAttributeService.insertBatch(property.tWebAttributeList);
		}

		// WEBデータ特集を削除
		webSpecialService.deleteTWebSpecialByWebId(property.tWeb.id);
		// WEBデータ特集の登録
		if (property.tWebSpecialList != null && !property.tWebSpecialList.isEmpty()) {
			addTWebSpecialValue(property);
			webSpecialService.insertBatch(property.tWebSpecialList);
		}

		// 素材を削除
		materialService.deleteTMaterialByWebId(property.tWeb.id);
		// 素材の登録
		if (property.tMaterialList != null && !property.tMaterialList.isEmpty()) {
			addTMaterialValue(property);
		}

		// 系列店舗の登録
		webShopListService.deleteInsert(property.tWeb.id, property.tWebShopListList);

		// 職種の削除登録
		deleteInsertWebJob(property);

		// キーワードの更新
		updateKeywork(property.tWeb.id);

		// 検索テーブルの更新
		updateWebSearch(property);

		property = null;
	}

	/**
	 * スカウトメールの変更
	 * @param newEntity
	 * @param oldEntity
	 * @param addScoutCount
	 */
	private void changeScoutMail(TWeb newEntity, TWeb oldEntity, int addScoutCount) {
		if (newEntity.customerId == null
				|| newEntity.volumeId == null) {
			// 削除して終了
			scoutMailLogic.deleteFreeScoutMail(oldEntity);

			return;
		}

		if (!GourmetCareeUtil.eqInt(newEntity.customerId, oldEntity.customerId)
				|| !GourmetCareeUtil.eqInt(newEntity.volumeId, oldEntity.volumeId)) {
			//  スカウト削除
			scoutMailLogic.deleteFreeScoutMail(oldEntity);


			// 掲載確定状態のまま変更されたら、追加し直す
			if (GourmetCareeUtil.eqInt(MStatusConstants.DBStatusCd.POST_FIXED,
					oldEntity.status)) {
				scoutMailLogic.addScoutMailByFixWeb(newEntity, addScoutCount);
			}
		}
	}

	/**
	 * WEBデータ登録のため、エンティティに値をセットする。
	 * @param entity WEBデータエンティティ
	 */
	private void setTWeb(WebdataProperty property) {

		// ステータス(下書き)
		property.tWeb.status = DBStatusCd.DRAFT;
		// アクセスコード
		property.tWeb.accessCd = UUID.create();
		// 対象外フラグを対象に設定
		property.tWeb.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
		// 削除フラグ
		property.tWeb.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		// リニューアルフラグをONにする
		property.tWeb.renewalFlg = MTypeConstants.RenewalFlg.TARGET;

		// 系列店舗の表示フラグを設定
		property.tWeb.shopListDisplayKbn =
			CollectionUtils.isNotEmpty(property.tWebShopListList)
				? MTypeConstants.ShopListDisplayKbn.ARI
				: MTypeConstants.ShopListDisplayKbn.NASHI;

//		// キーワードの追加
//		createKeywordSearchColumnValue(property.tWeb);
	}

	/**
	 * WEBデータ属性に登録する値を追加する。
	 * @param property WEBデータプロパティ
	 */
	private void addTWebAttributeValue(WebdataProperty property) {

		// 区分の値をプロパティにセット
		for (TWebAttribute entity : property.tWebAttributeList) {
			// WEBID
			entity.webId = property.tWeb.id;
			// 対象外フラグに対象をセット
			entity.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
			// 削除フラグ
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		}
	}

	/**
	 * WEBデータ路線図に登録する値を追加する。
	 * @param property WEBデータプロパティ
	 */
	private void addTWebRouteValue(WebdataProperty property) {

		List<TWebRoute> duplicateEntityList = new ArrayList<TWebRoute>();

		for (Iterator<TWebRoute> it = property.tWebRouteList.iterator(); it.hasNext();) {
			TWebRoute entity = it.next();
			if (isDuplicatedWebRoute(duplicateEntityList, entity)) {
				it.remove();
				continue;
			}
			// WEBID
			entity.webId = property.tWeb.id;
			// 対象外フラグに対象をセット
			entity.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
			// 削除フラグ
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		}
	}

	/**
	 * {@link TWebRoute}が被っているかどうか
	 * @param duplicateEntityList
	 * @param entity
	 * @return
	 */
	private boolean isDuplicatedWebRoute(List<TWebRoute> duplicateEntityList, TWebRoute entity) {
		for (TWebRoute subEntity : duplicateEntityList) {
			if (GourmetCareeUtil.eqInt(subEntity.railroadId, entity.railroadId)
					&& GourmetCareeUtil.eqInt(subEntity.stationId, entity.stationId)
					&& GourmetCareeUtil.eqInt(subEntity.routeId, entity.routeId)) {
				return true;
			}
		}

		duplicateEntityList.add(entity);
		return false;
	}

	/**
	 * {@link TWebRoute}が被っているかどうか
	 * @param duplicateEntityList
	 * @param entity
	 * @return
	 */
	private boolean isDuplicatedWebRouteWithWebId(List<TWebRoute> duplicateEntityList, TWebRoute entity) {
		for (TWebRoute subEntity : duplicateEntityList) {
			if (GourmetCareeUtil.eqInt(subEntity.railroadId, entity.railroadId)
					&& GourmetCareeUtil.eqInt(subEntity.stationId, entity.stationId)
					&& GourmetCareeUtil.eqInt(subEntity.routeId, entity.routeId)
					&& GourmetCareeUtil.eqInt(subEntity.webId, entity.webId)) {
				return true;
			}
		}

		duplicateEntityList.add(entity);
		return false;
	}

	/**
	 * WEBデータ特集に登録する値を追加する。
	 * @param property WEBデータプロパティ
	 */
	private void addTWebSpecialValue(WebdataProperty property) {

		// 特集の値をリストセット
		for (TWebSpecial tWebSpecial : property.tWebSpecialList) {
			// WEBID
			tWebSpecial.webId = property.tWeb.id;
			// 対象外フラグに対象をセット
			tWebSpecial.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
			// 削除フラグ
			tWebSpecial.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		}
	}

	/**
	 * 素材データに登録する値を追加する。
	 * @param property WEBデータプロパティ
	 */
	private void addTMaterialValue(WebdataProperty property) {

		// 素材の値をリストセット
		for (TMaterial tMaterial : property.tMaterialList) {

			// WEBID
			tMaterial.webId = property.tWeb.id;
			// 対象外フラグに対象をセット
			tMaterial.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
			// 削除フラグ
			tMaterial.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			if (!MaterialKbn.MOVIE_WM.equals(Integer.toString(tMaterial.materialKbn))
				&& !MaterialKbn.MOVIE_QT.equals(Integer.toString(tMaterial.materialKbn))) {

				try {
					MaterialDto dto = WebdataFileUtils.getWebdataImageFile(
															property.webdataSessionImgdirPath,
															property.idForDir,
															Integer.toString(tMaterial.materialKbn));

					tMaterial.materialData = Base64Util.encode(dto.materialData);

				} catch (ImageWriteErrorException e) {
					throw new SOptimisticLockException("ファイルシステムから取り出した画像の登録時に画像、またはフォルダが存在しなかった可能性があります。" + e);
				}
			}

			materialService.insert(tMaterial);
		}
	}

	/**
	 * WEBデータ職種を登録
	 * @param property
	 */
	private void addWebJobValue(WebdataProperty property) {

		for (TWebJob tWebJob : property.tWebJobList) {
			tWebJob.webId = property.tWeb.id;
			tWebJob.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			webJobService.insert(tWebJob);

			if (CollectionUtils.isNotEmpty(tWebJob.tWebJobAttributeList)) {
				addWebJobAttribute(tWebJob.id, tWebJob.tWebJobAttributeList);
			}
			if (CollectionUtils.isNotEmpty(tWebJob.tWebJobShopList)) {
				addWebJobShopList(tWebJob.id, tWebJob.tWebJobShopList);
			}
		}
	}

	/**
	 * 職種を削除登録する
	 * @param property
	 */
	private void deleteInsertWebJob(WebdataProperty property) {

		// 削除対象の職種を取得
		List<TWebJob> deletelist = webJobService.findByWebId(property.tWeb.id);
		// 属性と職種に紐づく店舗を削除
		for (TWebJob deleteEntity : deletelist) {
			webJobAttributeService.deleteByWebJobId(deleteEntity.id);
			webJobShopListService.deleteByWebJobId(deleteEntity.id);
		}
		// WEBIDで削除
		webJobService.deleteByWebId(property.tWeb.id);
		// 登録
		addWebJobValue(property);
	}

	/**
	 * WEB職種を登録する
	 * @param webJobId
	 * @param list TWebJobAttributeのリスト
	 */
	private void addWebJobAttribute(int webJobId, List<TWebJobAttribute> list) {
		for (TWebJobAttribute attr : list) {
			attr.webJobId = webJobId;
			attr.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			webJobAttributeService.insert(attr);
		}
	}

	/**
	 * WEB職種店舗を登録する
	 * @param webJobId
	 * @param list TWebJobShopListのリスト
	 */
	private void addWebJobShopList(int webJobId, List<TWebJobShopList> list) {
		for (TWebJobShopList attr : list) {
			attr.webJobId = webJobId;
			attr.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			webJobShopListService.insert(attr);
		}
	}

	/**
	 * WEB詳細を表示する条件を返す。
	 * @return 検索条件
	 */
	private Where createDetailWhere() {

		SimpleWhere where = new SimpleWhere();

		// 代理店の場合
		if (AGENCY.value().equals(getAuthLevel())) {
			// WEBデータ.会社ID = ログイン会社ID
			where.eq(camelize(TWeb.COMPANY_ID), getCompanyId());
		}
		// WEBデータ.削除フラグ = 0
		where.eq(camelize(TWeb.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return where;
	}

	/**
	 * 画像を除いた詳細のソート順を返す。
	 * @return WEB詳細のソート順
	 */
	private String createDetailSortExcludesMaterial(){

		String[] sortKey = new String[] {
				// ソート順を設定
				asc(dot(camelize(TWeb.T_WEB_ATTRIBUTE_LIST), camelize(TWebAttribute.ATTRIBUTE_CD))),// WEBデータ属性.区分コード
				asc(dot(camelize(TWeb.T_WEB_ATTRIBUTE_LIST), camelize(TWebAttribute.ID))),			// WEBデータ属性.ID
				asc(dot(camelize(TWeb.T_WEB_ROUTE_LIST),     camelize(TWebRoute.ID))),				// WEBデータ路線図.ID
				asc(dot(camelize(TWeb.T_WEB_SPECIAL_LIST),   camelize(TWebRoute.ID)))				// WEBデータ特集.ID
		};

		// カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * 詳細のソート順を返す。
	 * @return WEB詳細のソート順
	 */
	private String createDetailSort(){

		String[] sortKey = new String[] {
				// ソート順を設定
				asc(dot(camelize(TWeb.T_WEB_ATTRIBUTE_LIST), camelize(TWebAttribute.ATTRIBUTE_CD))),// WEBデータ属性.区分コード
				asc(dot(camelize(TWeb.T_WEB_ATTRIBUTE_LIST), camelize(TWebAttribute.ID))),			// WEBデータ属性.ID
				asc(dot(camelize(TWeb.T_WEB_ROUTE_LIST),     camelize(TWebRoute.ID))),				// WEBデータ路線図.ID
				asc(dot(camelize(TWeb.T_WEB_SPECIAL_LIST),   camelize(TWebRoute.ID))),				// WEBデータ特集.ID
				asc(dot(camelize(TWeb.T_MATERIAL_LIST),      camelize(TMaterial.ID)))				// 素材.ID
		};

		// カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);

	}

	/**
	 * WEB詳細を表示するためのデータを画像を除いて取得する。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEB詳細の検索条件プロパティ
	 * @throws WNoResultException 検索結果が無い場合はエラー
	 */
	public void getWebdataDetailExcludesMatelial(WebdataProperty property) throws WNoResultException {

		// プロパティチェック
		checkEmptyProperty(property);

		try {
			// WEBデータと、関連するテーブルを結合して詳細データを取得する。
			property.tWeb = findByIdWebdataExcludesMaterial(property.tWeb.id, createDetailWhere(), createDetailSortExcludesMaterial());

			// 顧客が登録されていればデータを取得。データがなければエラー
			if (property.tWeb.customerId != null) {

				// 顧客ロジック用にプロパティを用意
				CustomerProperty customerProperty = new CustomerProperty();

				// 顧客IDをセットして、プロパティに格納
				MCustomer mCustomer = new MCustomer();
				mCustomer.id = property.tWeb.customerId;
				customerProperty.mCustomer = mCustomer;

				// 顧客検索の実施
				customerLogic.getListRowData(customerProperty);

				// WEBデータプロパティにセット
				property.mCustomer = customerProperty.mCustomer;

				// 系列店舗の取得
				property.tWebShopListList = webShopListService.findByWebId(property.tWeb.id);
			}

			// 号数が登録されていればデータを取得。データがなければエラー
			if (property.tWeb.volumeId != null) {
				property.mVolume = volumeService.findById(property.tWeb.volumeId);
			}

			// 職種を取得
			property.tWebJobList = webJobService.findByWebIdWithAttribute(property.tWeb.id);
//			職種に紐づく店舗を取得
			if (CollectionUtils.isNotEmpty(property.tWebJobList)) {
				for (TWebJob tWebJob : property.tWebJobList) {
					tWebJob.tWebJobShopList = webJobShopListService.findByWebJobId(tWebJob.id);
				}
			}

		// データが無い場合は、エラーを返す
		}catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * WEB詳細を表示するためのデータを取得する。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEB詳細の検索条件プロパティ
	 * @throws WNoResultException 検索結果が無い場合はエラー
	 */
	public void getWebdataDetail(WebdataProperty property) throws WNoResultException {

		// プロパティチェック
		checkEmptyProperty(property);

		try {

			// WEBデータと、関連するテーブルを結合して詳細データを取得する。
			property.tWeb = findByIdWebdata(property.tWeb.id, createDetailWhere(), createDetailSort());

			// 顧客が登録されていればデータを取得。データがなければエラー
			if (property.tWeb.customerId != null) {

				// 顧客ロジック用にプロパティを用意
				CustomerProperty customerProperty = new CustomerProperty();

				// 顧客IDをセットして、プロパティに格納
				MCustomer mCustomer = new MCustomer();
				mCustomer.id = property.tWeb.customerId;
				customerProperty.mCustomer = mCustomer;

				// 顧客検索の実施
				customerLogic.getListRowData(customerProperty);

				// WEBデータプロパティにセット
				property.mCustomer = customerProperty.mCustomer;
			}

			// 号数が登録されていればデータを取得。データがなければエラー
			if (property.tWeb.volumeId != null) {
				property.mVolume = volumeService.findById(property.tWeb.volumeId);
			}

		// データが無い場合は、エラーを返す
		}catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * WEBデータの情報を取得する。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param id データのID
	 * @param sortKey ソート順
	 * @return 検索結果の
	 * @throws WNoResultException
	 */
	private TWeb findByIdWebdataExcludesMaterial(Integer id, Where where, String sortKey) throws WNoResultException {

		try {
			return jdbcManager
					.from(TWeb.class)
					.leftOuterJoin(camelize(TWeb.T_WEB_ATTRIBUTE_LIST))				// WEBデータ属性
					.leftOuterJoin(camelize(TWeb.T_WEB_ROUTE_LIST))					// WEBデータ路線図
					.leftOuterJoin(camelize(TWeb.T_WEB_SPECIAL_LIST))				// WEBデータ特集
					.id(id)															// WEBデータ.ID = id
					.where(where)
					.orderBy(sortKey)
					.disallowNoResult()
					.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * WEBデータの情報を取得する。<br />
	 * データが取得できなければ、エラーを返す。
	 * @param id データのID
	 * @param sortKey ソート順
	 * @return 検索結果の
	 * @throws WNoResultException
	 */
	private TWeb findByIdWebdata(Integer id, Where where, String sortKey) throws WNoResultException {

		try {
			return jdbcManager
					.from(TWeb.class)
					.leftOuterJoin(camelize(TWeb.T_WEB_ATTRIBUTE_LIST))				// WEBデータ属性
					.leftOuterJoin(camelize(TWeb.T_WEB_ROUTE_LIST))					// WEBデータ路線図
					.leftOuterJoin(camelize(TWeb.T_WEB_SPECIAL_LIST))				// WEBデータ特集
					.leftOuterJoin(camelize(TWeb.T_MATERIAL_LIST))					// 素材
					.id(id)															// WEBデータ.ID = id
					.where(where)
					.orderBy(sortKey)
					.disallowNoResult()
					.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * WEBデータの操作可否ををセットしたDtoを作成して返す。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return WEBデータ操作Dto
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public WebdataControlDto setControlFlg(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		WebdataControlDto dto = new WebdataControlDto();

		// 画面表示ステータスを取得
		int displayStatus = getDisplayStatus(property);

		// 画面表示ステータスをセット
		dto.displayStatus = displayStatus;

		// コピー可否の制御をセット
		dto.copyFlg = canCopy(property);

		// 応募テスト可否の制御をセット
		dto.appTestFlg = canAppTest(property);

		// 編集可否の制御をセット
		dto.editFlg = canEdit(property, displayStatus);

		// 削除可否の制御をセット
		dto.deleteFlg = canDelete(property, displayStatus);

		// 掲載確定可否の制御をセット
		dto.fixedFlg = canFixed(property, displayStatus);

		// 確定取消可否の制御をセット
		dto.cancelFlg = canCancel(property, displayStatus);

		// 掲載依頼可否の制御をセット
		dto.postRequestFlg = canPostRequest(property, displayStatus);

		// 掲載終了可否の制御をセット
		dto.postEndFlg = canPostEnd(property, displayStatus);

		// WEB非表示可否の制御をセット
		dto.hiddenFlg = canHidden(property, displayStatus);

		// セットしたDtoを返却
		return dto;

	}

	/**
	 * 編集可能なデータがどうかを判別する。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 編集可能な場合はtrue、不可能な場合はfalse
	 * @throws WNoResultException 関連データが見つからない場合のエラー
	 */
	public boolean checkEdit(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		// 画面表示ステータスを取得
		int displayStatus = getDisplayStatus(property);

		// 編集可否を返却
		return canEdit(property, displayStatus);
	}

	/**
	 * 削除可能なデータがどうかを判別する。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 削除可能な場合はtrue、不可能な場合はfalse
	 * @throws WNoResultException 関連データが見つからない場合のエラー
	 */
	public boolean checkDelete(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		// 画面表示ステータスを取得
		int displayStatus = getDisplayStatus(property);

		// 削除可否を返却
		return canDelete(property, displayStatus);
	}

	/**
	 * 応募テスト可能なデータがどうかを判別する。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 応募テスト可能な場合はtrue、不可能な場合はfalse
	 * @throws WNoResultException 関連データが見つからない場合のエラー
	 */
	public boolean checkAppTest(WebdataProperty property) {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		// 編集可否を返却
		return canAppTest(property);
	}

	/**
	 * 画面表示用のステータスを返却する。<br />
	 * プロパティのWEBデータの号数が登録されていなければ<br />
	 * WEBデータに登録されたステータスを返す。<br />
	 * プロパティに号数マスタが登録されていなければ取得してセットする。<br />
	 * プロパティのWEBデータエンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 * @return 画面表示ステータス
	 * @throws WNoResultException 号数が取得できない場合エラー
	 */
	public int getDisplayStatus(WebdataProperty property) throws WNoResultException {

		// プロパティの中身をチェック
		checkEmptyPropertyWeb(property);

		// 画面表示ステータス
		int displayStatus = property.tWeb.status;

		// 号数が登録されていなければ処理しない
		if (property.tWeb.volumeId == null) {
			return displayStatus;
		}

		// 号数マスタがnullの場合は取得する
		if (property.tWeb.volumeId != null && property.mVolume == null) {
			try {
				property.mVolume = volumeService.findById(property.tWeb.volumeId);

			// 号数が取得できない場合はエラー
			} catch (SNoResultException e) {
				throw new WNoResultException();
			}
		}

		// ステータスが掲載確定の場合、画面表示のステータスを設定
		if (DBStatusCd.POST_FIXED.equals(property.tWeb.status)) {

			Date today = new Date();

			// 掲載開始日時より前は掲載待ち
			if (today.before(property.mVolume.postStartDatetime)) {
				displayStatus = POST_WAIT;

			// 掲載期間は掲載中
			} else if (today.after(property.mVolume.postStartDatetime) && today.before(property.mVolume.postEndDatetime)) {
				displayStatus = POST_DURING;

			// 掲載終了日時より後は掲載終了
			} else if (today.after(property.mVolume.postEndDatetime)) {
				displayStatus = POST_END;
			}
		}

		// 画面ステータスを返却
		return displayStatus;
	}

	/**
	 * WEBデータのコピーが可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return コピー可能の場合true、不可の場合false
	 */
	private boolean canCopy(WebdataProperty property) {

		// 権限
		String authLevel = getAuthLevel();

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {

			// コピー可能
			return true;
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel) || SALES.value().equals(authLevel)) {

			try {
				// 代理店データの場合、不可
				if (isAgencyCompany(property)) {
					return false;
				}
			// 会社が取得できない場合、不可
			} catch (WNoResultException e) {
					return false;
			}
			return true;
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// ログイン会社が削除されていれば、不可
			if (!isLoginCompanyExsists(property)) {
				return false;
			}

			// WEBデータの会社がログインユーザの会社で無い場合、不可
			if (!String.valueOf(property.tWeb.companyId).equals(getCompanyId())) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * WEBデータの応募テストが可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 応募テスト可能の場合true、不可の場合false
	 */
	private boolean canAppTest(WebdataProperty property) {

		// 顧客が未登録の場合、不可
		if (property.tWeb.customerId == null) {
			return false;
		}

		// 応募フォーム無の場合、不可
		if (MTypeConstants.ApplicationFormKbn.NON == property.tWeb.applicationFormKbn) {
			return false;
		}

		// 権限
		String authLevel = getAuthLevel();

		// 管理者の場合、可
		if (ADMIN.value().equals(authLevel)) {
			return true;
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel) || SALES.value().equals(authLevel)) {
			try {
				// 代理店データの場合、不可
				if (isAgencyCompany(property)) {
					return false;
				}
			// 会社が取得できない場合、不可
			} catch (WNoResultException e) {
					return false;
			}

			return true;
		}

		// 代理店の場合
		if (AGENCY.value().equals(getAuthLevel())) {

			// ログイン会社が削除されていれば、不可
			if (!isLoginCompanyExsists(property)) {
				return false;
			}

			// WEBデータの会社がログインユーザの会社で無い場合、不可
			if (!String.valueOf(property.tWeb.companyId).equals(getCompanyId())) {
				return false;
			}
			return true;
		}

		return false;
	}

	/**
	 * 過去に掲載確定がされたかどうか
	 * @param property
	 * @return
	 */
	private boolean didNotFixButtonPressed(WebdataProperty property) {
		return (property.tWeb.fixedDatetime == null && property.tWeb.fixedUserId == null);
	}

	/**
	 * WEBデータの編集が可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 編集可能の場合true、不可の場合false
	 */
	private boolean canEdit(WebdataProperty property, int displayStatus) {

		// 権限
		String authLevel = getAuthLevel();

		// 現在の日時
		Date today = new Date();

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {

			// 掲載終了の場合のみ、不可
			if (POST_END.equals(displayStatus)) {
				return false;
			}
			return true;
		}

		// 営業の場合
		if(SALES.value().equals(authLevel)) {
			if(getUserId().equals(String.valueOf(property.tWeb.salesId)) && !POST_END.equals(displayStatus)) {
				return true;
			} else {
				return false;
			}
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel)) {

			try {
				// 代理店データの場合、不可
				if (isAgencyCompany(property)) {
					return false;
				}
			// 会社が取得できない場合、不可
			} catch (WNoResultException e) {
					return false;
			}

			// 下書きの場合、可
			if (DRAFT.equals(displayStatus)) {
				return true;

			// 承認中の場合、不可
			} else if (REQ_APPROVAL.equals(displayStatus)) {
				return false;

			// 掲載待ちの場合
			} else if (POST_WAIT.equals(displayStatus)) {

				// 掲載確定日時以前の場合、可
				if (today.before(property.mVolume.fixedDeadlineDatetime)) {
					return true;

				// 掲載確定日時以降の場合、不可
				} else {
					return false;
				}

			// 掲載中、掲載終了の場合、不可
			} else {
				return false;
			}
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// ログイン会社が削除されていれば、不可
			if (!isLoginCompanyExsists(property)) {
				return false;
			}

			// WEBデータの会社がログインユーザの会社で無い場合、不可
			if (!String.valueOf(property.tWeb.companyId).equals(getCompanyId())) {
				return false;
			}

			// 下書きの場合のみ、可
			if (DRAFT.equals(displayStatus)) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * WEBデータの削除が可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 削除可能の場合true、不可の場合false
	 */
	private boolean canDelete(WebdataProperty property, int displayStatus) {

		// 権限
		String authLevel = getAuthLevel();

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {

			// いつでも削除可
			return didNotFixButtonPressed(property);
		}

		if (SALES.value().equals(authLevel)) {
			if(DRAFT.equals(displayStatus) && getUserId().equals(String.valueOf(property.tWeb.salesId))) {
				return didNotFixButtonPressed(property);
			}
			return false;
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel) || SALES.value().equals(authLevel)) {

			try {
				// 代理店データの場合、不可
				if (isAgencyCompany(property)) {
					return false;
				}
			// 会社が取得できない場合、不可
			} catch (WNoResultException e) {
					return false;
			}

			// 下書きの場合のみ、可
			if (DRAFT.equals(displayStatus)) {
				return didNotFixButtonPressed(property);
			}
			return false;
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// ログイン会社が削除されていれば、不可
			if (!isLoginCompanyExsists(property)) {
				return false;
			}

			// WEBデータの会社がログインユーザの会社で無い場合、不可
			if (!String.valueOf(property.tWeb.companyId).equals(getCompanyId())) {
				return false;
			}

			// 下書きの場合のみ、可
			if (DRAFT.equals(displayStatus)) {
				return didNotFixButtonPressed(property);
			}
			return false;
		}
		return false;
	}

	/**
	 * WEBデータの掲載確定が可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 掲載確定可能の場合true、不可の場合false
	 */
	private boolean canFixed(WebdataProperty property, int displayStatus) {

		// 権限
		String authLevel = getAuthLevel();

		// 現在の日時
		Date today = new Date();

		// 会社マスタを取得
		try {
			if (property.mCompany == null) {
				setCompanyEntity(property);
			}
		// 会社が取得できない場合、不可
		} catch (WNoResultException e) {
			return false;
		}

		try {
			// 営業担当者が登録されていなければ、不可
			if (property.mSales == null && property.tWeb.salesId == null) {
				return false;
			}
			// 営業担当者マスタの取得
			if (property.mSales == null) {
				setSalesEntity(property);
			}
		// 営業担当者が取得できない場合、不可
		} catch (WNoResultException e) {
			return false;
		}

		// 号数が登録されていなければ不可
		if (property.tWeb.volumeId == null) {
			return false;
		}

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {

			// 下書きの場合、可
			if (DRAFT.equals(displayStatus)) {
				return true;
			}

			// 承認中場合、可
			if (REQ_APPROVAL.equals(displayStatus)) {
				return true;
			}

			return false;
		}

		if(SALES.value().equals(authLevel) && getUserId().equals(String.valueOf(property.tWeb.salesId))) {
			// 下書きの場合、可
			if (DRAFT.equals(displayStatus)) {
				return true;
			}

			// 承認中場合、可
			if (REQ_APPROVAL.equals(displayStatus)) {
				return true;
			}

			return false;
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel)) {

			try {
				// 代理店データの場合、不可
				if (isAgencyCompany(property)) {
					return false;
				}
			// 会社が取得できない場合、不可
			} catch (WNoResultException e) {
					return false;
			}

			// 下書きの場合
			if (DRAFT.equals(displayStatus)) {

				// 掲載確定日時以前の場合、可
				if (today.before(property.mVolume.fixedDeadlineDatetime)) {
					return true;
				}
				// 掲載確定日時以降の場合、不可
				return false;
			}
			return false;
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// 代理店の掲載確定は不可
			return false;
		}
		return false;
	}

	/**
	 * WEBデータの確定取消が可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 確定取消可能の場合true、不可の場合false
	 */
	private boolean canCancel(WebdataProperty property, int displayStatus) {

		// 権限
		String authLevel = getAuthLevel();

		// 現在の日時
		Date today = new Date();

		// 号数が登録されていなければ不可
		if (property.tWeb.volumeId == null) {
			return false;
		}

		// 下書きの場合、不可
		if (DRAFT.equals(displayStatus)) {
			return false;
		}

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {

			// 承認中場合、可
			if (REQ_APPROVAL.equals(displayStatus)) {
				return true;
			}

			// 掲載待ち場合、可
			if (POST_WAIT.equals(displayStatus)) {
				return true;
			}

			// 掲載中の場合、可
			if (POST_DURING.equals(displayStatus)) {
				return true;
			}
			// 掲載終了の場合は、不可
			return false;
		}

		if(SALES.value().equals(authLevel)) {
			if(getUserId().equals(String.valueOf(property.tWeb.salesId)) && POST_WAIT.equals(displayStatus)) {
				// 承認中場合、可
				if (REQ_APPROVAL.equals(displayStatus)) {
					return true;
				}

				// 掲載待ち場合、可
				if (POST_WAIT.equals(displayStatus)) {
					return true;
				}

				// 掲載中の場合、可
				if (POST_DURING.equals(displayStatus)) {
					return true;
				}
			} else {
				return false;
			}
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel)) {

			try {
				// 代理店データの場合、不可
				if (isAgencyCompany(property)) {
					return false;
				}
			// 会社が取得できない場合、不可
			} catch (WNoResultException e) {
					return false;
			}

			// 承認中場合、不可
			if (REQ_APPROVAL.equals(displayStatus)) {
				return false;
			}

			// 掲載待ち場合
			if (POST_WAIT.equals(displayStatus)) {

				// 掲載確定日時以前の場合、可
				if (today.before(property.mVolume.fixedDeadlineDatetime)) {
					return true;

				// 掲載確定日時以降の場合、不可
				} else {
					return false;
				}
			}
			return false;
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// 確定取消は不可
			return false;
		}
		return false;
	}

	/**
	 * WEBデータのが掲載依頼可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 掲載依頼可能の場合true、不可の場合false
	 */
	private boolean canPostRequest(WebdataProperty property, int displayStatus) {

		// 権限
		String authLevel = getAuthLevel();

		// 現在の日時
		Date today = new Date();

		// 号数が登録されていなければ不可
		if (property.tWeb.volumeId == null) {
			return false;
		}

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {
			// 掲載依頼は不可
			return false;
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel) || SALES.value().equals(authLevel)) {

			// 掲載依頼は不可
			return false;
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// ログイン会社が削除されていれば、不可
			if (!isLoginCompanyExsists(property)) {
				return false;
			}

			// 下書きの場合
			if (DRAFT.equals(displayStatus)) {

				// 締切日時以前の場合、可
				if (today.before(property.mVolume.deadlineDatetime)) {
					return true;

				// 締切日時以降の場合、不可
				} else {
					return false;
				}
			}
			// 下書き以外は不可
			return false;
		}
		return false;
	}

	/**
	 * WEBデータの掲載終了が可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return 掲載終了可能の場合true、不可の場合false
	 */
	private boolean canPostEnd(WebdataProperty property, int displayStatus) {

		// 権限
		String authLevel = getAuthLevel();

		// 号数が登録されていなければ、不可
		if (property.tWeb.volumeId == null) {
			return false;
		}

		// 下書きの場合、不可
		if (DRAFT.equals(displayStatus)) {
			return false;
		}

		// 承認中の場合、不可
		if (REQ_APPROVAL.equals(displayStatus)) {
			return false;
		}

		// 管理者の場合
		if (ADMIN.value().equals(authLevel)) {

			// 掲載中のみ、可
			if (POST_DURING.equals(displayStatus)) {
				return true;
			}
			return false;
		}

		// 営業の場合
		if(SALES.value().equals(authLevel)) {
			if(getUserId().equals(String.valueOf(property.tWeb.salesId)) && POST_DURING.equals(displayStatus)) {
				return true;
			} else {
				return false;
			}
		}

		// 自社スタッフ、他社スタッフの場合
		if (STAFF.value().equals(authLevel) || OTHER.value().equals(authLevel)) {

			// 掲載終了は不可
			return false;
		}

		// 代理店の場合
		if (AGENCY.value().equals(authLevel)) {

			// 掲載終了は不可
			return false;
		}
		return false;
	}

	/**
	 * WEBデータの非表示が可能か判定する。
	 * @param property WEBデータプロパティ
	 * @param displayStatus 画面表示ステータス
	 * @return WEB非表示可能の場合true、不可の場合false
	 */
	private boolean canHidden(WebdataProperty property, int displayStatus) {

		// 現在の日時
		Date today = new Date();

		// WEB表示状態の判定
		if (property.tWeb.publicationEndDisplayFlg == MTypeConstants.PublicationEndDisplayFlg.DISPLAY_OK) {
			// 掲載終了状態は可
			if (displayStatus == MStatusConstants.DBStatusCd.POST_END) {
				return true;

			// 掲載中かつ、現在日時が掲載終了日時を過ぎている場合は可
			} else if (displayStatus == MStatusConstants.DBStatusCd.POST_FIXED && today.after(property.mVolume.postEndDatetime)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * WEBデータの一括コピーが可能かどうかチェック
	 * @param property
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	public boolean isEnableLumpCopy(WebdataProperty property) throws NumberFormatException {

		List<TWeb> entityList;
		try {
			entityList = webService.getWebDataByIdArray(property.webId);

		} catch (WNoResultException e) {
			property.deleteWebId = Arrays.asList(property.webId);
			return false;
		}

		if (!isDeleteData(entityList, property)) {
			return false;
		}

		if (STAFF.value().equals(getAuthLevel()) || SALES.value().equals(getAuthLevel())) {
			return isEnableLumpCopyStaff(entityList, property);
		} else if (AGENCY.value().equals(getAuthLevel())) {
			return isEnableLumpCopyAgency(entityList, property);
		} else {
			return true;
		}

	}

	/**
	 * 選択されたWEBデータが削除されていないかチェック
	 * @param property
	 * @return 削除データが含まれている場合、false
	 */
	private boolean isDeleteData(List<TWeb> entityList, WebdataProperty property) {

		List<String> deleteWebId = new ArrayList<String>();

		for (String id : property.webId) {
			boolean deleteFlg = true;
			for (TWeb entity : entityList) {
				if (String.valueOf(entity.id).equals(id)) {
					deleteFlg = false;
					break;
				}
			}
			if (deleteFlg) {
				deleteWebId.add(String.valueOf(id));
			}
		}
		property.deleteWebId = deleteWebId;

		if (deleteWebId.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * スタッフが一括コピーできるデータかどうかチェック
	 * @return すべてのデータがコピー可の場合、trueを返す
	 */
	private boolean isEnableLumpCopyStaff(List<TWeb> entityList, WebdataProperty property) {

		List<String> idList = new ArrayList<String>();

		for (TWeb entity : entityList) {
			if (entity.mCompany != null && MCompany.AgencyFlgValue.AGENCY == entity.mCompany.agencyFlg) {
				idList.add(String.valueOf(entity.id));
			}
		}

		property.failWebId = idList;

		if (idList.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 代理店が一括コピーできるデータかどうかチェック
	 * @param entityList WEBデータリスト
	 * @param property WEBプロパティ
	 * @return すべてのデータがコピー可の場合、trueを返す
	 */
	private boolean isEnableLumpCopyAgency(List<TWeb> entityList, WebdataProperty property) {

		List<String> idList = new ArrayList<String>();

		for (TWeb entity : entityList) {
			if (!getCompanyId().equals(String.valueOf(entity.companyId))) {
				idList.add(String.valueOf(entity.id));
			}
		}

		property.failWebId = idList;

		if (idList.size() > 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 一括コピー処理
	 * @param property WEBプロパティ
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	public void doLumpCopy(WebdataProperty property) throws NumberFormatException, WNoResultException {

		// WEBデータ一括コピー
		convertLumpCopyWebData(property);
	}

	/**
	 * 一括コピー用WEBデータを生成
	 * @param property WEBデータプロパティ
	 * @return WEBデータエンティティリスト
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void convertLumpCopyWebData(WebdataProperty property) throws NumberFormatException, WNoResultException {

		Map<Integer, TWeb> oldEntityMap = property.getBeforeEntityMap();
		int count = 0;

		for (String webIdStr : property.webId) {
			int oldId = Integer.parseInt(webIdStr);
			int newWebId = 0;

			if (!oldEntityMap.containsKey(oldId)) {
				continue;
			}

			try {
				newWebId = copyWeb(oldEntityMap.get(oldId));
				count++;
			} catch (WNoResultException e) {
				//Webデータ自体が存在しない場合は次のWebデータへ
				continue;
			}

			//関連テーブルをコピー
			copyWebAttr(oldId, newWebId);
			copyWebRoute(oldId, newWebId);
			copyMaterial(oldId, newWebId);
			copyWebSpecial(oldId, newWebId);
			copyWebShopList(oldId, newWebId);
			copyWebJob(oldId, newWebId);
			copyWebSearch(newWebId);

			// IP電話テーブルのコピー
			IPPhoneDataCopyDto dto = new IPPhoneDataCopyDto();
			dto.webId = newWebId;
			dto.sourceWebId = oldId;

			TWeb entity = oldEntityMap.get(oldId);
			if (entity == null) {
				continue;
			}
			dto.customerId = entity.customerId;

			iPPhoneLogic.copyIpPhoneNumber(dto);

			//  Webデータ用タグのコピー
			copyWebTag(oldId, newWebId);

		}

		oldEntityMap.clear();

		//結果がゼロ件であれば例外をスロー
		if (count == 0) {
			throw new WNoResultException("一括コピー対象のWebデータの取得に失敗しました。");
		}
	}

	/**
	 * Webデータのエンティティ間の必要情報をコピーして、INSERTします。
	 * @param beforeEntity
	 * @param newEntity
	 * @return シーケンスに新しく振られたID
	 * @throws WNoResultException
	 */
	private int copyWeb(TWeb beforeEntity) throws WNoResultException {

		try {
			TWeb newEntity = webService.findById(beforeEntity.id);

			newEntity.sizeKbn = beforeEntity.sizeKbn;						// サイズ区分
			newEntity.volumeId = beforeEntity.volumeId;						// 号数
			newEntity.sourceWebId = beforeEntity.id;						// 親IDのセット
			newEntity.serialPublication = beforeEntity.serialPublication;   // 連載
//			newEntity.magazineManuscriptNo = null;							// 誌面原稿番号
//			newEntity.magazineVolume = null;								// 誌面号数
//			newEntity.memo = null;											// メモ
//			newEntity.magazineFlg = MagazineFlg.NOT_TARGET;					// 誌面データフラグを否対象
			newEntity.attentionShopFlg = AttentionShopFlg.NOT_TARGET;		// 注目店舗フラグを否対象
			newEntity.updateUserId = null;									// 更新ユーザ
			newEntity.updateDatetime = null;								// 更新日時
			newEntity.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;	// 対象外フラグを対象
			newEntity.status = DBStatusCd.DRAFT;							// ステータスを下書き
			newEntity.accessCd = UUID.create();								// アクセスコードの生成
			newEntity.fixedDatetime = null;									// 掲載確定日時
			newEntity.fixedUserId = null;									// 掲載確定担当者ID
			newEntity.requestDatetime = null;								// 掲載依頼日時
			newEntity.requestUserId = null;									// 掲載依頼担当者ID
			newEntity.applicationTestFlg = MTypeConstants.ApplicationTestFlg.NON; // 応募テストフラグを未送信
			newEntity.checkedStatus = MTypeConstants.WebdataCheckedStatus.UNCHECKED; // チェックステータスを未チェックにする。

			webService.insert(newEntity);

			//シーケンスが割り振ったIDを取得
			int newId = newEntity.id;
			newEntity = null;
			beforeEntity = null;

			return newId;
		} catch (SNoResultException e) {
			throw new WNoResultException("コピー時に元のWebデータIDの取得失敗");
		} finally {
			beforeEntity = null;
		}
	}

	/**
	 * WebIdをもとにWeb属性テーブルを新しいWebIdと紐付けてコピーします。
	 * ほとんどの情報は変更なしのため、直接元のエンティティを加工してINSERT
	 * @param oldId
	 * @param newWebId
	 */
	private void copyWebAttr(int oldId, int newWebId) {
		List<IdSelectDto> webAttributeIdList = webAttributeService.getIdList(oldId);

		for (IdSelectDto idSelectDto : webAttributeIdList) {
			try {
				TWebAttribute oldWebAttrEntity = webAttributeService.findById(idSelectDto.id);

				/*
				 * <依頼メモ>　※通常コピーも対応
				 * １．原稿入稿画面の2連載、3連載のステイタスについて
				 * 2連載、もしくは3連載目を忘れず入稿するためのメモ的な機能ですが、
				 * これを、初入稿時、2連載と選択して入稿し、
				 * 2週間後、2連載入稿する為に、一括コピーすると、ステイタスが自動で2連載OKになっている、
				 * ようになるととても助かります。
				 * 3連載入稿した場合には、2週間後に3連載中の2回目を入稿する場合、
				 * 自動的にステイタスのプルダウンメニューが「2連載」を選択してあるのが望ましいです。
				 */
				if (MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD.equals(oldWebAttrEntity.attributeCd)) {

					Integer serialPublicationKbn = oldWebAttrEntity.attributeValue;

					// 2連載の場合は２連載OK
					if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE, serialPublicationKbn)) {
						oldWebAttrEntity.attributeValue = MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE_OK;

					// 3連載の場合は２連載
					} else if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.TRIPLE, serialPublicationKbn)) {
						oldWebAttrEntity.attributeValue = MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE;

					// 定額制の場合は定額制
					}else if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.FLAT_RATE_SYSTEM, serialPublicationKbn)) {
							oldWebAttrEntity.attributeValue = MTypeConstants.SERIAL_PUBLICATION_KBN.FLAT_RATE_SYSTEM;

					// それ以外は処理しない
					} else {
						continue;
					}
				}

				oldWebAttrEntity.updateDatetime = null;
				oldWebAttrEntity.updateUserId = null;
				oldWebAttrEntity.webId = newWebId;
				webAttributeService.insert(oldWebAttrEntity);

				oldWebAttrEntity = null;
			} catch (SNoResultException e) {
				// 主キーのループ中のため0件は想定されない。未処理とする。
			}
		}

		webAttributeIdList.clear();
	}

	/**
	 * WebIdをもとにWeb路線テーブルを新しいWebIdと紐付けてコピーします。
	 * ほとんどの情報は変更なしのため、直接元のエンティティを加工してINSERT
	 * @param oldId
	 * @param newWebId
	 */
	private void copyWebRoute(int oldId, int newWebId) {
		List<IdSelectDto> webRouteIdList = webRouteService.getIdList(oldId);
		List<TWebRoute> duplicatedEntityList = new ArrayList<TWebRoute>();

		for (IdSelectDto idSelectDto : webRouteIdList) {
			try {
				TWebRoute oldWebRouteEntity = webRouteService.findById(idSelectDto.id);
				oldWebRouteEntity.updateDatetime = null;
				oldWebRouteEntity.updateUserId = null;
				oldWebRouteEntity.webId = newWebId;

				if (isDuplicatedWebRouteWithWebId(duplicatedEntityList, oldWebRouteEntity)) {
					continue;
				}
				webRouteService.insert(oldWebRouteEntity);

				oldWebRouteEntity = null;
			} catch (SNoResultException e) {
				// 主キーのループ中のため0件は想定されない。未処理とする。
			}
		}

		duplicatedEntityList.clear();
		webRouteIdList.clear();
	}

	/**
	 * WebIdをもとに素材テーブルを新しいWebIdと紐付けてコピーします。
	 * ほとんどの情報は変更なしのため、直接元のエンティティを加工してINSERT
	 * @param oldId
	 * @param newWebId
	 */
	private void copyMaterial(int oldId, int newWebId) {
		List<IdSelectDto> materialList = materialService.getIdList(oldId);

		for (IdSelectDto idSelectDto : materialList) {
			try {
				TMaterial oldMaterialEntity = materialService.findById(idSelectDto.id);

				oldMaterialEntity.updateDatetime = null;
				oldMaterialEntity.updateUserId = null;
				oldMaterialEntity.webId = newWebId;
				materialService.insert(oldMaterialEntity);


				oldMaterialEntity = null;
			} catch (SNoResultException e) {
				// 主キーのループ中のため0件は想定されない。未処理とする。
			}
		}

		materialList.clear();
	}


	/**
	 * WebIdをもとにWeb特集テーブルを新しいWebIdと紐付けてコピーします。
	 * ほとんどの情報は変更なしのため、直接元のエンティティを加工してINSERT
	 * @param oldId
	 * @param newWebId
	 */
	private void copyWebSpecial(int oldId, int newWebId) {
		List<IdSelectDto> webSpecialList = webSpecialService.getIdList(oldId);

		for (IdSelectDto idSelectDto : webSpecialList) {
			try {
				TWebSpecial oldWebSpecialEntity = webSpecialService.findById(idSelectDto.id);
				oldWebSpecialEntity.updateDatetime = null;
				oldWebSpecialEntity.updateUserId = null;
				oldWebSpecialEntity.webId = newWebId;
				webSpecialService.insert(oldWebSpecialEntity);

				oldWebSpecialEntity = null;
			} catch (SNoResultException e) {
				// 主キーのループ中のため0件は想定されない。未処理とする。
			}
		}

		webSpecialList.clear();
	}

	/**
	 * WebIdをもとにWeb店舗テーブルにコピー
	 * @param oldId
	 * @param newWebId
	 */
	private void copyWebShopList(int oldId, int newWebId) {
		List<TWebShopList> oldList = webShopListService.findByWebId(oldId);

		for (TWebShopList entity : oldList) {
			entity.updateDatetime = null;
			entity.updateUserId = null;
			entity.webId = newWebId;
			webShopListService.insert(entity);
		}
	}

	/**
	 * WebIdをもとに職種をコピー登録
	 * @param oldId
	 * @param newWebId
	 */
	private void copyWebJob(int oldId, int newWebId) {

		List<TWebJob> oldList = webJobService.findByWebIdWithAttributeAndShopList(oldId);

		for (TWebJob entity : oldList) {
			TWebJob newEntity = Beans.createAndCopy(TWebJob.class, entity)
					.excludes(
							WztStringUtil.toCamelCase(TWebJob.ID),
							WztStringUtil.toCamelCase(TWebJob.WEB_ID),
							WztStringUtil.toCamelCase(TWebJob.UPDATE_DATETIME),
							WztStringUtil.toCamelCase(TWebJob.UPDATE_USER_ID)
					)
					.execute();
			newEntity.webId = newWebId;
			webJobService.insert(newEntity);

			// 属性を登録
			copyWebJobAttribute(entity.tWebJobAttributeList, newEntity.id);

//			職種店舗を登録
			copyWebJobShopList(entity.tWebJobShopList, newEntity.id);

		}

	}

	/**
	 * Web職種をコピー
	 * @param oldAttrList
	 * @param newWebJobId
	 */
	private void copyWebJobAttribute(List<TWebJobAttribute> oldAttrList, int newWebJobId) {

		for (TWebJobAttribute entity : oldAttrList) {
			entity.updateDatetime = null;
			entity.updateUserId = null;
			entity.webJobId = newWebJobId;
			webJobAttributeService.insert(entity);
		}
	}

	/**
	 * Web職種店舗をコピー
	 * @param oldShopList
	 * @param newWebJobId
	 */
	private void copyWebJobShopList(List<TWebJobShopList> oldShopList, int newWebJobId) {

		for (TWebJobShopList entity : oldShopList) {
			entity.updateDatetime = null;
			entity.updateUserId = null;
			entity.webJobId = newWebJobId;
			webJobShopListService.insert(entity);
		}
	}

	/**
	 * WEBデータの一括確定が可能かどうかチェック
	 * @param property
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 * @throws NoExistCompanyDataException
	 */
	public void checkEnableLumpDecide(WebdataProperty property) throws NumberFormatException {

		List<TWeb> entityList;
		try {
			entityList = webService.getWebDataListByIdArray(property.webId);

			// データが削除されていないかチェック
			checkDeleteData(entityList, property);

			// 会社・営業担当者・顧客・号数が存在しているかチェック
			checkExistData(entityList, property);

		} catch (WNoResultException e) {
			property.deleteWebId = Arrays.asList(property.webId);
		}
	}

	/**
	 * WEBデータの一括削除が可能かどうかチェック
	 * @param property WEBプロパティ
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 * @throws NoExistCompanyDataException
	 */
	public void checkEnableLumpDelete(WebdataProperty property) throws NumberFormatException {

		List<TWeb> entityList;
		try {
			entityList = webService.getWebDataListByIdArray(property.webId);

			// データが削除されていないかチェック
			checkDeleteData(entityList, property);

		} catch (WNoResultException e) {
			property.deleteWebId = Arrays.asList(property.webId);
		}
	}

	/**
	 * 選択されたWEBデータが削除されていないかチェック
	 * @param property
	 * @return 削除データが含まれている場合、false
	 */
	private void checkDeleteData(List<TWeb> entityList, WebdataProperty property) {

		List<String> deleteWebId = new ArrayList<String>();

		for (String id : property.webId) {
			boolean deleteFlg = true;
			for (TWeb entity : entityList) {
				if (String.valueOf(entity.id).equals(id)) {
					deleteFlg = false;
					break;
				}
			}
			if (deleteFlg) {
				deleteWebId.add(String.valueOf(id));
			}
		}
		property.deleteWebId = deleteWebId;
	}

	/**
	 * WEBデータに登録されている会社・営業担当者・顧客・号数が存在しているかチェック
	 * @param entityList WEBデータエンティティリスト
	 * @param property WEBデータプロパティ
	 * @return すべての会社が存在していれば、trueを返す
	 */
	private void checkExistData(List<TWeb> entityList, WebdataProperty property) {

		Date nowDateTime = new Date();
		List<String> noExistCompanyWebIdList = new ArrayList<String>();
		List<String> noExistSalesWebIdList = new ArrayList<String>();
		List<String> noExistCustomerWebIdList = new ArrayList<String>();
		List<String> noRegistVolumeWebIdList = new ArrayList<String>();
		List<String> noExistVolumeWebIdList = new ArrayList<String>();
		List<String> failStatusWebIdList = new ArrayList<String>();
		List<String> failFixedDatetimeWebIdList = new ArrayList<String>();
		List<String> failWebId = new ArrayList<String>();

		for (TWeb entity : entityList) {

			// 会社が存在しているかチェック
			if (entity.mCompany == null) {
				noExistCompanyWebIdList.add(String.valueOf(entity.id));
			}

			// 営業担当者が存在しているかチェック
			if (entity.mSales == null) {
				noExistSalesWebIdList.add(String.valueOf(entity.id));
			}

			// 顧客が存在しているかチェック
			if (entity.customerId != null && entity.mCustomer == null) {
				noExistCustomerWebIdList.add(String.valueOf(entity.id));
			}

			// 号数が登録されているかチェック
			if (entity.volumeId == null) {
				noRegistVolumeWebIdList.add(String.valueOf(entity.id));
				// 号数が存在しているかチェック
			} else if (entity.mVolume == null) {
				noExistVolumeWebIdList.add(String.valueOf(entity.id));
			}

			// 管理者の場合
			if (ADMIN.value().equals(getAuthLevel())) {
				// 下書き・承認中かチェック
				if (!(DRAFT.equals(entity.status) || REQ_APPROVAL.equals(entity.status))) {
					failStatusWebIdList.add(String.valueOf(entity.id));
				}
				// 自社スタッフの場合
			} else if (STAFF.value().equals(getAuthLevel()) || SALES.value().equals(getAuthLevel())) {
				// 代理店データでないかチェック
				if (entity.mCompany != null && MTypeConstants.AgencyFlg.AGENCY == entity.mCompany.agencyFlg) {
					failWebId.add(String.valueOf(entity.id));
				} else {
					// 下書きかチェック
					if (!DRAFT.equals(entity.status)) {
						failStatusWebIdList.add(String.valueOf(entity.id));
					} else {
						// 掲載確定締切前かチェック
						if (entity.mVolume != null && !nowDateTime.before(entity.mVolume.fixedDeadlineDatetime)) {
							failFixedDatetimeWebIdList.add(String.valueOf(entity.id));
						}
					}
				}
			}
		}

		property.noExistCompanyWebIdList = noExistCompanyWebIdList;
		property.noExistSalesWebIdList = noExistSalesWebIdList;
		property.noExistCustomerWebIdList = noExistCustomerWebIdList;
		property.noRegistVolumeWebIdList = noRegistVolumeWebIdList;
		property.noExistVolumeWebIdList = noExistVolumeWebIdList;
		property.failStatusWebIdList = failStatusWebIdList;
		property.failFixedDatetimeWebIdList = failFixedDatetimeWebIdList;
		property.failWebId = failWebId;
	}

	/**
	 * WEBデータ一括確定処理
	 * @param property WEBデータプロパティ
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	public void doLumpDecide(WebdataProperty property) throws NumberFormatException {

		List<TWeb> beforeEntityList = new ArrayList<TWeb>();
		try {
			// 一括確定対象承認依頼WEBデータを取得
			beforeEntityList = webService.getAgencyWebData(property.webId);

		} catch (WNoResultException e1) {
			// 承認依頼WEBデータがない場合、何も処理しない
		}

		// 一括確定
		List<String> idList = new ArrayList<String>();
		for (TWeb updateEntity : property.entityList) {
			try {
				webService.update(updateEntity);
				scoutMailLogic.addScoutMailByFixWeb(updateEntity, property.addScoutMailCount);
			} catch (OptimisticLockException e) {
				idList.add(String.valueOf(updateEntity.id));
			}
		}


		// 一括確定前の承認依頼データを保持
		property.entityList = beforeEntityList;
		property.failWebId = idList;
	}

	/**
	 * WEBデータ一括削除処理
	 * @param property WEBデータプロパティ
	 */
	public void doLumpDelete(WebdataProperty property) {

		// 一括削除
		List<String> idList = new ArrayList<String>();
		for (TWeb deleteEntity : property.entityList) {
			try {
				webService.logicalDelete(deleteEntity);
			} catch (OptimisticLockException e) {
				idList.add(String.valueOf(deleteEntity.id));
			}
		}
		// 楽観的排他制御が発生したデータリストを保持
		property.failWebId = idList;
	}

	/**
	 * 応募テスト実施時のデータ登録を行う。<br />
	 * プロパティのWEBデータエンティティ、応募テストエンティティをセットして呼び出し。
	 * @param property WEBデータプロパティ
	 */
	public void insertAppTest(WebdataProperty property) {

		// プロパティの中身チェック
		checkEmptyPropertyWeb(property);

		if (property.tApplicationTest == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// WEBデータが更新されていないかチェック
		try {
			TWeb tWeb = webService.findById(property.tWeb.id);

			// 更新されている場合はエラーを返す
			if (!tWeb.version.equals(property.tWeb.version)) {
				throw new SOptimisticLockException("Webデータのバージョンが不一致です。");
			}
		// データが削除されている場合はエラーを返す
		} catch (SNoResultException e) {
			throw new SOptimisticLockException("Webデータが削除されたか、存在しませんでした。");
		}

		// 未応募の場合、フラグをUPDATEする
		if (property.tWeb.applicationTestFlg == MTypeConstants.ApplicationTestFlg.NON) {

			// プロパティから値を取得
			TWeb webEntity = new TWeb();
			// ID
			webEntity.id = property.tWeb.id;
			// 応募テストフラグ(応募済)
			webEntity.applicationTestFlg = MTypeConstants.ApplicationTestFlg.FIN;
			// バージョン
			webEntity.version = property.tWeb.version;

			// WEBデータ更新
			webService.update(webEntity);
		}

		// 応募テスト.アクセスコードをセット
		property.tApplicationTest.accessCd = UUID.create();

		// データ登録
		applicationTestService.insert(property.tApplicationTest);

	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(WebdataProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * プロパティのWEBデータエンティティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyPropertyWeb(WebdataProperty property) {

		checkEmptyProperty(property);

		// プロパティがnullの場合はエラー
		if (property.tWeb == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

    /**
     * webAreaKbnのWEB属性リスト作成
     * @param areaCd エリアコード
     * @param webAreaKbnList webエリア区分リスト
     * @return WEB属性リスト(typeCdとtypeValueのみがセットされている)
     */
    public List<TWebAttribute> createWebAreaKbnAttributes(String areaCd, List<String> webAreaKbnList) {
        final String typeCd = MTypeConstants.WebAreaKbn.getTypeCd(areaCd);

        List<Integer> typeValues = typeMappingLogic.getTypeValues(typeCd,
                GcCollectionUtils.toIntegerList(webAreaKbnList));

        List<TWebAttribute> attrs = Lists.newArrayList();
        for (Integer val : typeValues) {
            attrs.add(TWebAttribute.newInstance(typeCd, val));
        }
        return attrs;
    }


    /**
     * 詳細エリア区分のWEB属性リストを生成。
     * 仙台版は詳細エリアに加えて、詳細エリアグループのリストも生成する
     * @param areaCd エリアコード
     * @param detailAreaKbnList 詳細エリア区分リスト
     * @return WEB属性リスト
     */
    public List<TWebAttribute> createDetailAreaKbnAttributes(String areaCd, List<String> detailAreaKbnList) {
        List<Integer> valueList = GcCollectionUtils.toIntegerList(detailAreaKbnList);
        if (MAreaConstants.AreaCd.isSendai(areaCd)) {
            return createSendaiDetailAreaKbnAttributes(areaCd, valueList);
        }


        final String typeCd = MTypeConstants.DetailAreaKbn.getTypeCd(areaCd);
        return TWebAttribute.newInstanceList(typeCd, valueList);
    }


    /**
     * 仙台用の詳細エリア区分Web属性リストを生成
     * @param areaCd エリアコード
     * @param detailAreaKbnList 詳細エリア区分リスト
     * @return 詳細エリア区分と、それに紐づく詳細エリア区分グループのWeb属性グループリスト
     */
    private List<TWebAttribute> createSendaiDetailAreaKbnAttributes(String areaCd, List<Integer> detailAreaKbnList) {
        final String detailAreaTypeCd = MTypeConstants.DetailAreaKbn.getTypeCd(areaCd);
        List<TWebAttribute> attributeList = TWebAttribute.newInstanceList(detailAreaTypeCd, detailAreaKbnList);

        final String groupTypeCd = MTypeConstants.DetailAreaKbnGroup.getTypeCd(areaCd);
        List<Integer> groupList =
                detailAreaGroupMappingService.findDistinctDetailAreaKbnGroupByDetailAreaKbnsAndAreaCd(
                        detailAreaKbnList,
                        Integer.parseInt(areaCd));

        for (Integer value : groupList) {
            attributeList.add(TWebAttribute.newInstance(groupTypeCd, value));
        }

        return attributeList;


    }

    /**
     * 子の値リストを作成する。
     * 子を持っていない値はこのリストに含む
     * 親の値のみ含まない。
     * @param webId webID
     * @param typeCd タイプコード
     * @return 子の値と、子を持っていない値のリスト。
     */
    public List<MType> selectChildList(int webId, String typeCd) {
        List<Integer> list = webAttributeService.getWebAttributeValueIntegerList(webId, typeCd);
        List<Integer> parentList = typeMappingService.findParentValuesByTypeCd(typeCd);
        list.removeAll(parentList);

        return typeService.findByTypeCdAndTypeValues(typeCd, list);
    }

    /**
     * Webデータ用のタグをコピーする
     * @param oldId
     * @param newId
     */
    private void copyWebTag(int oldId, int newId) {

    	List<MWebTagMapping> entityList = tagListLogic.webDataMappingfindByWebDataId(oldId);
    	if (CollectionUtils.isEmpty(entityList)) {
    		return;
    	}

    	entityList.stream().forEach(e -> e.webId = newId);


    	webTagMappingService.insertBatch(entityList);

    	entityList.clear();
    }

    /**
     * キーワードテーブルの更新
     * @param webId
     */
	public void updateKeywork(int webId) {

			try {
				List<TWeb> tWebList = webService.findByConditionLeftJoin(
						WztStringUtil.toCamelCase(TWeb.T_WEB_JOB_LIST),
						new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TWeb.ID), webId)
							.eq(WztStringUtil.toCamelCase(TWeb.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
							WztStringUtil.toCamelCase(TWeb.ID)
						);
				List<String> keywordList = new ArrayList<>();
				tWebList.stream().forEach( tWeb -> {
					// 原稿名
					keywordList.add(tWeb.manuscriptName);
					// キャッチコピー
					keywordList.add(tWeb.catchCopy1);
					keywordList.add(tWeb.catchCopy2);
					keywordList.add(tWeb.catchCopy3);
					// 本文
					keywordList.add(tWeb.sentence1);
					keywordList.add(tWeb.sentence2);
					keywordList.add(tWeb.sentence3);
					// キャプション
					keywordList.add(tWeb.captiona);
					keywordList.add(tWeb.captionb);
					keywordList.add(tWeb.captionc);
					// タイトル/ここに注目
					keywordList.add(tWeb.attentionHereTitle);
					// 文章/ここに注目
					keywordList.add(tWeb.attentionHereSentence);

					Set<String> keywordSet = new HashSet<>();
					tWeb.tWebJobList.stream().forEach(entity -> {

						// 募集業種
						keywordSet.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, entity.jobKbn));
						// 給与
						keywordSet.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalaryKbn.TYPE_CD, entity.salary));
						keywordSet.add(entity.lowerSalaryPrice);
						keywordSet.add(entity.upperSalaryPrice);
						// 待遇
						keywordSet.add(entity.treatment);

						try {
							List<TWebJobAttribute> entityAttribute = webJobAttributeService.findByCondition(
									new SimpleWhere()
										.eq(WztStringUtil.toCamelCase(TWebJobAttribute.WEB_JOB_ID), entity.id)
										.eq(WztStringUtil.toCamelCase(TWebJobAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
										WztStringUtil.toCamelCase(TWebJobAttribute.WEB_JOB_ID)
									);

							// 待遇
							entityAttribute.stream().forEach( attribute -> {
								// 待遇
								if (MTypeConstants.TreatmentKbn.TYPE_CD.equals(attribute.attributeCd)) {
									keywordSet.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.TreatmentKbn.TYPE_CD, attribute.attributeValue));
								// その他
								} else if (MTypeConstants.OtherConditionKbn.TYPE_CD.equals(attribute.attributeCd)) {
									keywordSet.add(valueToNameConvertLogic.convertToTypeName(MTypeConstants.OtherConditionKbn.TYPE_CD, attribute.attributeValue));
								}
							});

						} catch (WNoResultException e) {
							//
						}
						// 休日休暇
						keywordSet.add(entity.holiday);
					});

					List<String> childKeywordList = new ArrayList<>(keywordSet);
					keywordList.addAll(childKeywordList);

					String keyword = String.join(" ", keywordList);
					tWeb.keywordSearch = keyword;
					webService.update(tWeb);
				});

			} catch (WNoResultException e) {
				//
			}
	}

	/**
	 * 検索用テーブルの更新
	 * @param webId
	 */
	public void updateWebSearch(WebdataProperty property) {

		WebSearchDto dto = new WebSearchDto(property.tWeb.id);

		property.tWebJobList.stream().forEach(job->{

			if (job.publicationFlg == 0) {
				return;
			}

			// 職種
			dto.jobKbnList.add(job.jobKbn);

			// 雇用形態
			dto.employPtnKbnList.add(job.employPtnKbn);

			job.tWebJobAttributeList.stream()
					.filter(p->p.attributeCd.equals(MTypeConstants.TreatmentKbn.TYPE_CD))
					.forEach(attr->{

						// 待遇
						dto.treatmentKbnList.add(attr.attributeValue);
					});
		});

		property.tWebSpecialList.stream().forEach(special->{
			// 特集
			dto.specialIdList.add(special.specialId);
		});

		property.tWebAttributeList.stream()
				.filter(p->p.attributeCd.equals(MTypeConstants.CompanyCharacteristicKbn.TYPE_CD))
				.forEach(attr->{

					// 企業特徴
					dto.companyCharacteristicKbnList.add(attr.attributeValue);
		});

		webSearchService.save(dto);
	}

	/**
	 * 検索用テーブルのコピー
	 * @param webId
	 */
	public void copyWebSearch(int webId) {
		WebdataProperty property = new WebdataProperty();
		property.tWeb = webService.findById(webId);
		property.tWebJobList = webJobService.findByWebIdWithAttribute(webId);

		for(String specialId : webSpecialService.getSpecialIdListByWebId(webId)) {
			TWebSpecial tWebSpecial = new TWebSpecial();
			tWebSpecial.specialId = Integer.parseInt(specialId);
			property.tWebSpecialList.add(tWebSpecial);
		}

		for(Integer value : webAttributeService.getValueList(webId, MTypeConstants.CompanyCharacteristicKbn.TYPE_CD)) {
			TWebAttribute tWebAttribute = new TWebAttribute();
			tWebAttribute.attributeCd = MTypeConstants.CompanyCharacteristicKbn.TYPE_CD;
			tWebAttribute.attributeValue = value;
			property.tWebAttributeList.add(tWebAttribute);
		}

		updateWebSearch(property);
	}
}
