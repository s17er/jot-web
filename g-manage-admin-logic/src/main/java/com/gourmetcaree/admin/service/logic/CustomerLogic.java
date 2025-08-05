package com.gourmetcaree.admin.service.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static org.seasar.framework.util.StringUtil.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.admin.service.dto.CustomerSearchDto;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.MCustomerHomepage;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.entity.TScoutMailManage;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.exception.NoExistCompanyDataException;
import com.gourmetcaree.db.common.exception.NoExistSalesDataException;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CompanyService;
import com.gourmetcaree.db.common.service.CustomerAccountService;
import com.gourmetcaree.db.common.service.CustomerCompanyService;
import com.gourmetcaree.db.common.service.CustomerHomepageService;
import com.gourmetcaree.db.common.service.CustomerMailMagazineAreaService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.db.common.service.SalesService;
import com.gourmetcaree.db.common.service.ScoutMailLogService;
import com.gourmetcaree.db.common.service.ScoutMailManageService;
import com.gourmetcaree.db.common.service.SentenceService;
import com.gourmetcaree.db.common.service.ShopListIndustryKbnService;
import com.gourmetcaree.db.common.service.ShopListPrefecturesService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.ShopListShutokenForeignAreaKbnService;

/**
 * 顧客に関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
public class CustomerLogic  extends AbstractAdminLogic {

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	/** 顧客アカウントサービス */
	@Resource
	protected CustomerAccountService customerAccountService;

	/** 顧客担当会社マスタサービス */
	@Resource
	protected CustomerCompanyService customerCompanyService;

	/** 定型文マスタサービス */
	@Resource
	protected SentenceService sentenceService;

//	/** スカウトメール数サービス */
//	@Resource
//	protected ScoutMailCountService scoutMailCountService;
//
//	/** スカウトメール追加履歴サービス */
//	@Resource
//	protected ScoutMailAddHistoryService scoutMailAddHistoryService;

	/** スカウトメール管理サービス */
	@Resource
	private ScoutMailManageService scoutMailManageService;

	/** スカウトメールログサービス */
	@Resource
	private ScoutMailLogService scoutMailLogService;


	/** 会社マスタサービス */
	@Resource
	protected CompanyService companyService;

	/** 営業担当者サービス */
	@Resource
	protected SalesService salesService;

	/** 名前変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 系列店舗の業態区分サービス */
	@Resource
	protected ShopListIndustryKbnService shopListIndustryKbnService;

	/** 系列店舗サービス */
	@Resource
	protected ShopListService shopListService;

	/** 顧客サブメールアドレスサービス */
	@Resource
	protected CustomerSubMailService customerSubMailService;

	/** 顧客ホームページサービス */
	@Resource
	protected CustomerHomepageService customerHomepageService;

	/** 顧客メルマガエリアサービス */
	@Resource
	protected CustomerMailMagazineAreaService customerMailMagazineAreaService;

	/** 系列店舗の都道府県ビューサービス */
	@Resource
	private ShopListPrefecturesService shopListPrefecturesService;

	/** 系列店舗の海外エリアビューサービス */
	@Resource
	private ShopListShutokenForeignAreaKbnService shopListShutokenForeignAreaKbnService;

	/**
	 * ログインIDの重複、担当会社・営業担当者の存在チェック
	 * @param loginId ログインID
	 * @param assignedCompanyId 担当会社ID
	 * @param assignedSalesId 営業担当者ID
	 * @throws ExistDataException
	 * @throws NoExistCompanyDataException
	 * @throws NoExistSalesDataException
	 */
	public void checkInputData(String customerId, String loginId, String assignedCompanyId, String assignedSalesId)
	throws ExistDataException, NoExistCompanyDataException, NoExistSalesDataException {

		// ログインID重複チェック
		if (!isLoginIdExist(customerId, loginId)) {
			throw new ExistDataException("顧客マスタでログインIDが重複しています。customerId："+ customerId + "、loginId:" + loginId);
		}

		// 会社存在チェック
		if (!isCompanyExist(assignedCompanyId)) {
			throw new NoExistCompanyDataException("会社マスタにデータが存在しませんでした。assignedCompanyId：" + assignedCompanyId);
		}

		// 営業担当者存在チェック
		if (!isSalesExist(assignedSalesId)) {
			throw new NoExistSalesDataException("営業担当者が存在しませんでした。assignedSalesId：" + assignedSalesId);
		}

	}

	/**
	 * ログインID重複チェック
	 * @param customerId 顧客ID
	 * @param loginId ログインID
	 * @return 重複している場合は、falseを返す
	 */
	private boolean isLoginIdExist(String customerId, String loginId) {

		if (StringUtils.isBlank(customerId)) {
			// ログインIDの重複チェック
			if (!customerAccountService.existCustomerDataByLoginId(loginId)) {
				return false;
			}
		} else {
			// ログインIDの重複チェック
			if (!customerAccountService.existSalesDataByIdLoginId(NumberUtils.toInt(customerId), loginId)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 会社存在チェック
	 * @param assignedCompanyId 担当会社ID
	 * @return 存在している場合は、trueを返す
	 */
	private boolean isCompanyExist(String assignedCompanyId) {

		MCompany entity = companyService.getCompanyData(NumberUtils.toInt(assignedCompanyId));

		if (entity == null) {
			return false;
		}

		return true;
	}

	/**
	 * 営業担当者存在チェック
	 * @param assignedSalesId 営業担当者ID
	 * @return 存在している場合は、trueを返す
	 */
	private boolean isSalesExist(String assignedSalesId) {

		MSales entity = salesService.getCompanyDataById(NumberUtils.toInt(assignedSalesId));

		if (entity == null) {
			return false;
		}

		return true;
	}

	/**
	 * 顧客表示用データを取得
	 * @param id 顧客ID
	 * @return CustomerProperty 顧客プロパティ
	 * @throws WNoResultException
	 */
	public CustomerProperty getDispData(int id, int salesId, String authLeve) throws WNoResultException {

		CustomerProperty property = new CustomerProperty();

		try {
			// 顧客マスタデータを取得
			property.mCustomer = customerService.findById(id);

			// 顧客アカウントマスタデータ取得
			property.mCustomerAccount = customerAccountService.getMCustomerAccountByCustomerId(id);

			// 顧客担当会社マスタデータ取得
			if (ManageAuthLevel.AGENCY.value().equals(authLeve)) {
				property.mCustomerCompanyList = customerCompanyService.getMCustomerCompanyListByCustomerId(id, salesId);
			} else {
				property.mCustomerCompanyList = customerCompanyService.getMCustomerCompanyListByCustomerId(id);
			}

			// 系列店舗の都道府県を取得
			property.shopListPrefecturesCdList = shopListPrefecturesService.findByCustomerId(id);

			// 系列店舗の海外エリアを取得
			property.shopListShutokenForeignAreaKbnList = shopListShutokenForeignAreaKbnService.findByCustomerId(id);

			// 系列店舗の業態区分を取得
			property.shopListIndustryKbnList = shopListIndustryKbnService.findByCustomerId(id);

			// 系列店舗の件数を取得
			property.shopListCount = shopListService.countByCustomerIdIgnoreDisplay(id);

			// サブメールアドレスを取得
			property.customerSubMailList = customerSubMailService.findByCustomerId(id);

			// 顧客ホームページの取得
			property.customerHomepageList = customerHomepageService.findByCustomerId(id);

			// メルマガエリアの取得
			property.mailMagazineAreaCdList = customerMailMagazineAreaService.getAreaList(id);

			return property;

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

	}

	/**
	 * 顧客情報を登録
	 * @param property 顧客プロパティ
	 */
	public void insertCustomerData(CustomerProperty property) {

		// 顧客マスタ登録データを生成
		createCustomerData(property.mCustomer);

		// 顧客マスタへ登録
		customerService.insert(property.mCustomer);

		// 顧客アカウントマスタ登録データ生成
		createCustomerAccountData(property);

		// 顧客アカントマスタへ登録
		customerAccountService.insert(property.mCustomerAccount);

		// 顧客担当会社マスタ登録データ生成
		createCustomerCompanyData(property);

		// 顧客担当会社マスタへ登録
		customerCompanyService.insertBatch(property.mCustomerCompanyList);

		// 定型文マスタ登録データ生成
		createSentenceData(property);

		// 定型文マスタへ登録
		sentenceService.insertBatch(property.mSentenceList);


		int customerId = property.mCustomer.id;

		// ホームページの登録
		createHomepage(customerId, property.customerHomepageList);

		// サブメールの登録
		createSubmail(customerId, property.customerSubMailList);

		// メルマガエリアの登録
		createMailMagazineArea(customerId, property.mailMagazineAreaCdList);



		// TODO スカウトメール登録するかしないか
		if (property.tScoutMailManage != null
				&& property.tScoutMailManage.scoutRemainCount.intValue() > 0) {
			// スカウトメール数テーブル登録データ生成
//			createScoutMailCoutnData(property);
			createScoutMailManageData(property);

			// スカウトメール数テーブルへ登録
//			scoutMailCountService.insert(property.tScoutMailCount);
			if (property.insertScoutManageFlg = true) {
				scoutMailManageService.insert(property.tScoutMailManage);
			} else {
				scoutMailManageService.update(property.tScoutMailManage);
			}
//			// スカウトメール追加テーブル登録データ生成
//			createScoutMailAddHistoryData(property);
//			scoutMailAddHistoryService.insert(property.tScoutMailAddHistory);
			createScoutMailLogData(property);
			scoutMailLogService.insert(property.tScoutMailLog);
		}


//		// スカウトメールの追加があればスカウトメール追加テーブルへ登録
//		if (property.tScoutMailCount.scoutCount > 0) {
//
//		}
	}

	/**
	 * 顧客情報を更新
	 * @param property 顧客プロパティ
	 */
	public void updateCustomerData(CustomerProperty property) {

		customerService.createDispOrder(property.mCustomer);
		customerService.updateDispOrderBatch(property.mCustomer.dispOrder, property.mCustomer.id);

		// 顧客マスタ更新
		if(property.mCustomer.prefecturesCd == null) {
			customerService.updateWithNull(property.mCustomer, "prefecturesCd");
		} else {
			customerService.update(property.mCustomer);
		}

		// 顧客アカウントマスタ更新
		customerAccountService.update(property.mCustomerAccount);

		// 顧客担当会社マスタ登録
		updateCustomerCompany(property);

		// スカウトメールの追加がある場合、
		// スカウトメール数テーブルを更新
		// スカウトメール追加履歴テーブルに登録
		if (property.tScoutMailManage != null) {
			if (property.insertScoutManageFlg) {
				scoutMailManageService.insert(property.tScoutMailManage);
			} else {
				if(property.tScoutMailManage.deleteFlg == DeleteFlgKbn.DELETED) {
					scoutMailManageService.delete(property.tScoutMailManage);
				} else {
					scoutMailManageService.update(property.tScoutMailManage);
				}
			}
			if(property.tScoutMailLog != null) {
				createScoutMailLogData(property);
				scoutMailLogService.insert(property.tScoutMailLog);
			}
		}

		int customerId = property.mCustomer.id;

		// ホームページの登録
		createHomepage(customerId, property.customerHomepageList);

		// サブメールの登録
		createSubmail(customerId, property.customerSubMailList);

		// メルマガエリアの登録
		createMailMagazineArea(customerId, property.mailMagazineAreaCdList);
	}

	/**
	 * 顧客担当会社情報をDelete/Insert
	 */
	private void updateCustomerCompany(CustomerProperty property) {

		// 顧客IDをキーに顧客担当会社データを物理削除
		customerCompanyService.deleteCustomerCompanyByCustomerId(property.mCustomer.id);

		// 顧客担当会社登録データを生成
		createCustomerCompanyData(property);

		// 顧客担当会社データを登録
		customerCompanyService.insertBatch(property.mCustomerCompanyList);
	}

	/**
	 * 顧客マスタ登録データ生成
	 * @param entity 顧客マスタエンティティ
	 */
	private void createCustomerData(MCustomer entity) {

		// 登録データ生成
		entity.registrationDatetime = new Timestamp(new Date().getTime());

		customerService.createDispOrder(entity);
		customerService.updateDispOrderBatch(entity.dispOrder, null);

	}

	/**
	 * 顧客アカウントマスタ登録データ生成
	 * @param property 顧客プロパティ
	 */
	private void createCustomerAccountData(CustomerProperty property) {

		// 登録データ生成
		property.mCustomerAccount.customerId = property.mCustomer.id;
	}

	/**
	 * 顧客担当会社マスタ登録データを生成
	 * @param property 顧客プロパティ
	 */
	private void createCustomerCompanyData(CustomerProperty property) {

		List<MCustomerCompany> list = new ArrayList<MCustomerCompany>();

		for (MCustomerCompany entity : property.mCustomerCompanyList) {
			entity.customerId = property.mCustomer.id;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			list.add(entity);
		}

		property.mCustomerCompanyList = list;
	}

	/**
	 * 定型文マスタ登録データを生成
	 * @param property 顧客プロパティ
	 */
	private void createSentenceData(CustomerProperty property) {

		List<MSentence> list = new ArrayList<MSentence>();

		for (MSentence entity : property.mSentenceList) {
			entity.customerId = property.mCustomer.id;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			list.add(entity);
		}

		property.mSentenceList = list;
	}

	/**
	 * サブメールを削除登録する
	 * @param customerId
	 * @param list
	 */
	private void createSubmail(int customerId, List<MCustomerSubMail> list) {
		customerSubMailService.deleteInsert(customerId, list);
	}

	/**
	 * ホームページを削除登録する
	 * @param customerId
	 * @param list
	 */
	private void createHomepage(int customerId, List<MCustomerHomepage> list) {
		customerHomepageService.deleteInsert(customerId, list);
	}

	/**
	 * メルマガエリアを削除登録する
	 * @param customerId
	 * @param areaList
	 */
	private void createMailMagazineArea(int customerId, List<Integer> areaList) {
		customerMailMagazineAreaService.deleteInsert(customerId, areaList);
	}

	/**
	 * スカウトメール管理テーブル登録データを作成
	 * @param property
	 */
	private void createScoutMailManageData(CustomerProperty property) {
		property.addScoutMailCount = property.tScoutMailManage.scoutRemainCount;
		property.tScoutMailManage.customerId = property.mCustomer.id;
		try {
			TScoutMailManage oldEntity = scoutMailManageService.getSameUseEndDatetimeEntity(property.tScoutMailManage);
			oldEntity.scoutRemainCount += property.tScoutMailManage.scoutRemainCount;
			property.tScoutMailManage = oldEntity;
			property.insertScoutManageFlg = false;
		} catch (WNoResultException e) {
			property.insertScoutManageFlg = true;
		}
	}

	private void createScoutMailLogData(CustomerProperty property) {
		property.tScoutMailLog.addDatetime = new Timestamp(new Date().getTime());
		property.tScoutMailLog.scoutManageId = property.tScoutMailManage.id;
	}

	/**
	 * リストに表示されているデータを1行分取得する
	 * @param property 顧客プロパティ
	 * @throws WNoResultException データが取得できない場合のエラー
	 */
	public void getListRowData(CustomerProperty property) throws WNoResultException {

		// プロパティがnullの場合はエラー
		if (property == null || property.mCustomer == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// 顧客データと、顧客担当会社マスタを結合してリスト表示の1行分を取得する
		// データが無い場合は、エラーを投げる
		property.mCustomer = customerService.findByIdInnerJoin(
								StringUtil.camelize(MCustomer.M_CUSTOMER_COMPANY_LIST), property.mCustomer.id, createListRowDataSort());

	}

	/**
	 * リスト表示の1行分のソート順を返す。
	 * @return リスト表示の1行分のソート順
	 */
	private String createListRowDataSort(){

		StringBuilder sortKey = new StringBuilder();
		// ソート順を設定
		sortKey.append(StringUtil.camelize(MCustomer.M_CUSTOMER_COMPANY_LIST)).append(".");
		sortKey.append(StringUtil.camelize(MCustomerCompany.COMPANY_ID)).append(" DESC");	// 顧客担当会社マスタ.会社IDの降順

		return sortKey.toString();

	}

	/**
	 * 顧客検索サブ画面の値を画面表示用にセットする。
	 * @param property 顧客プロパティ
	 * @return 顧客検索Dto
	 */
	public CustomerSearchDto convertSearchData(CustomerProperty property) {

		CustomerSearchDto dto = new CustomerSearchDto();

		// プロパティがnullの場合は空のdtoを返却
		if (property == null || property.mCustomer == null) {
			return dto;
		}

		// ID
		dto.id = String.valueOf(property.mCustomer.id);
		// 顧客名
		dto.customerName = property.mCustomer.customerName;
		// 担当者名
		dto.contactName = property.mCustomer.contactName;
		// エリアコード
		dto.areaCd = String.valueOf(property.mCustomer.areaCd);
		// エリア名
		dto.areaName = valueToNameConvertLogic.convertToAreaName(new String[] {String.valueOf(property.mCustomer.areaCd)});
		// 電話番号
		dto.phoneNo = property.mCustomer.phoneNo1 + GourmetCareeConstants.HYPHEN_MINUS_STR
					+ property.mCustomer.phoneNo2 + GourmetCareeConstants.HYPHEN_MINUS_STR
					+ property.mCustomer.phoneNo3;
		// メインメールアドレス
		dto.mainMail = property.mCustomer.mainMail;

		// サブメール受信可の場合はセット
		dto.subMailList = customerSubMailService.getReceptionSubMail(property.mCustomer.id);

		StringBuilder sb = new StringBuilder();

		// 担当会社、営業担当者
		if (property.mCustomer.mCustomerCompanyList != null && property.mCustomer.mCustomerCompanyList.size() > 0) {

			for (int i = 0; i < property.mCustomer.mCustomerCompanyList.size(); i++) {
				MCustomerCompany customerCompany = property.mCustomer.mCustomerCompanyList.get(i);

				// 複数件存在する場合は改行を行う
				if (i != 0) {
					sb.append(GourmetCareeConstants.BR_TAG);
				}
				// 会社名
				sb.append(valueToNameConvertLogic.convertToCompanyName(new String[]{String.valueOf(customerCompany.companyId)}));
				sb.append(GourmetCareeConstants.COLON_STR);
				// 営業担当者名
				sb.append(valueToNameConvertLogic.convertToSalesName(new String[]{String.valueOf(customerCompany.salesId)}));
			}
		}

		// 担当会社、営業担当者をセット
		dto.companySalesName = sb.toString();

		return dto;
	}

	/**
	 * 顧客一覧に表示するエンティティを顧客IDのリストで取得します。
	 * @param id 顧客ID
	 * @return 顧客エンティティ
	 * @throws WNoResultException
	 */
	public MCustomer getCustomerListById(Integer id) throws WNoResultException {

		// 検索条件の作成
		SimpleWhere where = new SimpleWhere()
			.eq(dot(camelize(MCustomer.M_CUSTOMER_COMPANY_LIST),camelize(MCustomerCompany.DELETE_FLG)), DeleteFlgKbn.NOT_DELETED)
		;

		// 検索の実行
		return customerService.findByIdInnerJoin(camelize(MCustomer.M_CUSTOMER_COMPANY_LIST), id, where, getCustomerListSortKey());
	}

	/**
	 * 顧客一覧を表示するためのソート順を返します。
	 * @return 顧客一覧のソート順
	 */
	private String getCustomerListSortKey() {

		String[] sortKey = new String[]{
				//ソート順を設定
				desc(camelize(MCustomer.ID)),
				desc(dot(camelize(MCustomer.M_CUSTOMER_COMPANY_LIST), camelize(MCustomerCompany.COMPANY_ID))),
				desc(dot(camelize(MCustomer.M_CUSTOMER_COMPANY_LIST), camelize(MCustomerCompany.SALES_ID)))
		};
		//カンマ区切りにして返す
		return createCommaStr(sortKey);
	}
}
