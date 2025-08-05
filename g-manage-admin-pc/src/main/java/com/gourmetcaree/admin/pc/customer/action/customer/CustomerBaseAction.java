package com.gourmetcaree.admin.pc.customer.action.customer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.customer.dto.customer.CompanySalesDto;
import com.gourmetcaree.admin.pc.customer.dto.customer.ScoutMailAddHistoryDto;
import com.gourmetcaree.admin.pc.customer.dto.customer.ScoutMailRemainDto;
import com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm;
import com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm.HomepageDto;
import com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm.SubMailDto;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.dto.ScoutMailLogDto;
import com.gourmetcaree.admin.service.dto.ScoutMailRemainRetDto;
import com.gourmetcaree.admin.service.logic.CustomerLogic;
import com.gourmetcaree.admin.service.logic.ScoutMailLogic;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerHomepage;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.entity.TCustomerLoginHistory;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.enums.MTypeEnum.MailStatusEnum;
import com.gourmetcaree.db.common.enums.MTypeEnum.SenderKbnEnum;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerLoginHistoryService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.ScoutMailLogService;

/**
 * 顧客管理Baseアクション
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class CustomerBaseAction extends PcAdminAction {

	/** 顧客ロジッククラス */
	@Resource
	protected CustomerLogic customerLogic;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/** 顧客ログイン履歴サービス */
	@Resource
	protected CustomerLoginHistoryService customerLoginHistoryService;

	@Resource
	protected ScoutMailLogic scoutMailLogic;

	@Resource
	protected ScoutMailLogService scoutMailLogService;

	/**
	 * 追加された担当会社・営業担当者を表示用に変更
	 */
	protected void convertAssignedData(CustomerForm form, String path) {

		CompanySalesDto dto = new CompanySalesDto();
		dto.companyId = form.assignedCompanyId;
		dto.salesId = form.assignedSalesId;
		dto.deletePath = GourmetCareeUtil.makePath(path, "deleteAssigned", String.valueOf(form.assignedSalesId));

		form.companySalesList.add(dto);

		// 選択された担当会社・営業担当者をリセット
		form.assignedCompanyId = "";
		form.assignedSalesId = "";
	}

	/**
	 * 選択された営業担当者をリストから削除
	 * @param form
	 */
	protected void deleteAssignedSales(CustomerForm form) {

		for (int i = 0; i < form.companySalesList.size(); i++) {
			if (form.companySalesList.get(i).salesId.equals(form.deleteSalesId)) {
				form.companySalesList.remove(i);
			}
		}
	}

	/**
	 * 各フラグに値がなければ初期値をセット
	 * @param form
	 */
	protected void setFirstData(CustomerForm form) {

		if (StringUtils.isBlank(form.submailReceptionFlg)) {
			form.submailReceptionFlg = String.valueOf(MCustomer.SubMailReceptionFlgKbn.NOT_RECEIVE);
		}

		if (StringUtils.isBlank(form.loginFlg)) {
			form.loginFlg = String.valueOf(MCustomer.LoginFlgKbn.LOGIN_OK);
		}

		if (StringUtils.isBlank(form.publicationFlg)) {
			form.publicationFlg = String.valueOf(MCustomer.PublicationFlg.PUBLICATION_OK);
		}

		if (StringUtils.isBlank(form.scoutUseFlg)) {
			form.scoutUseFlg = String.valueOf(MCustomer.ScoutUseFlg.SCOUT_USE_OK);
		}
	}

	/**
	 * 表示データを生成
	 * @param form 顧客管理フォーム
	 */
	protected void convertDispData(CustomerForm form) {

		try {
			// 表示用データを取得
			CustomerProperty property = customerLogic.getDispData(Integer.parseInt(form.id), NumberUtils.toInt(userDto.userId), userDto.authLevel);

			// 表示データを生成
			// 顧客マスタデータをコピー
			convertFromCustomer(form, property);

			// 顧客アカウントデータをフォームにセット
			convertFromCustomerAccount(form, property);

			// 顧客担当会社データをフォームにセット
			convertCustomerCompanyToDto(form, property);

			// 残りスカウトメール数をセット
//			convertFromScoutMailCount(form, property);

			// スカウトメール追加履歴データをフォームにセット
			createRemainScoutMail(form, property);
//			form.scoutMailAddHistoryList = convertScoutMailAddHistoryToDto(property);s

			// 未読の応募メール数をフォームにセット
			convertUnopenedApplicationMailCount(form);

			// 未読のスカウトメール数をフォームにセット
			convertUnopenedScoutMailCount(form);

			// 最終ログイン履歴をフォームにセット
			convertCustomerLoginHistory(form);

			// 系列店舗をフォームにセット
			convertShopList(form, property);

			// サブメールアドレスをセット
			convertSubMail(form, property);

			// ホームページをセット
			convertHomepage(form, property);

			// メルマガエリアをセット
			convertMailMagazineArea(form, property);

		} catch (NumberFormatException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * スカウトメール残数を作成します。
	 * @param form
	 * @param property
	 */
	private void createRemainScoutMail(CustomerForm form, CustomerProperty property) {
		ScoutMailRemainRetDto remainRetDto = scoutMailLogic.createScoutMailRemain(property);
		form.freeRemainScoutMail = remainRetDto.freeScoutMailCount;
		form.unlimitedRemainScoutMail = remainRetDto.unlimitedScoutMailCount;

		List<ScoutMailRemainDto> boughtRemainDtoList = new ArrayList<ScoutMailRemainDto>();
		for (VActiveScoutMail entity : remainRetDto.boughtScoutMailManageList) {
			ScoutMailRemainDto dto = new ScoutMailRemainDto();
			Beans.copy(entity, dto).execute();
			boughtRemainDtoList.add(dto);
		}
		form.boughtScoutMailRemainDtoList = boughtRemainDtoList;



		 try {
			List<ScoutMailLogDto> entityList = scoutMailLogic.getScoutMailAddLogList(property.mCustomer.id);
			List<ScoutMailAddHistoryDto> dtoList = new ArrayList<ScoutMailAddHistoryDto>();
			for (ScoutMailLogDto entity : entityList) {
				if(entity.addScoutCount != null) {
					ScoutMailAddHistoryDto dto = new ScoutMailAddHistoryDto();
					dto.addCount = entity.addScoutCount;
					dto.addDate = DateUtils.getDateStr(entity.addDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
					dto.scoutMailKbn = entity.scoutMailKbn;
					dto.scoutMailLogKbn = entity.scoutMailLogKbn;
					dtoList.add(dto);
				}
			}
			form.scoutMailAddHistoryList = dtoList;

		} catch (WNoResultException e) {
			form.scoutMailAddHistoryList = new ArrayList<ScoutMailAddHistoryDto>();
		}


	}

	/**
	 * 顧客マスタデータをフォームにセット
	 * @param form 顧客管理フォーム
	 * @param property 顧客プロパティ
	 */
	private void convertFromCustomer(CustomerForm form,CustomerProperty property) {

		Beans.copy(property.mCustomer, form).dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "registrationDatetime").excludesNull().execute();
//		form.registrationDatetime = DateUtils.getDateStr(property.mCustomer.registrationDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		form.customerVersion = property.mCustomer.version;
	}

	/**
	 * 顧客アカウントデータをフォームにセット
	 * @param form 顧客管理フォーム
	 * @param property 顧客プロパティ
	 */
	private void convertFromCustomerAccount(CustomerForm form,CustomerProperty property) {

		form.loginId = property.mCustomerAccount.loginId;
		form.customerAccountId = property.mCustomerAccount.id;
		form.customerAccountVersion =  property.mCustomerAccount.version;
	}

	/**
	 * 顧客担当会社マスタデータから担当会社・営業担当者DTOリストを生成
	 * @param property 顧客プロパティ
	 * @return 担当会社・営業担当者DTOリスト
	 */
	private void convertCustomerCompanyToDto(CustomerForm form, CustomerProperty property) {

		List<CompanySalesDto> dtoList = new ArrayList<CompanySalesDto>();

		if (property.mCustomerCompanyList == null) {
			form.companySalesList =  dtoList;
		} else {
			for (int i = 0; i < property.mCustomerCompanyList.size(); i++) {
				if (i == 0) {
					form.assignedCompanyId = String.valueOf(property.mCustomerCompanyList.get(0).companyId);
					form.assignedSalesId = String.valueOf(property.mCustomerCompanyList.get(0).salesId);
				} else {
					CompanySalesDto dto = new CompanySalesDto();
					Beans.copy(property.mCustomerCompanyList.get(i), dto).execute();
					dtoList.add(dto);
				}
			}
		}

		form.companySalesList = dtoList;
	}


	/**
	 * 未読応募メール数をフォームにセット
	 * @param form 顧客管理フォーム
	 */
	private void convertUnopenedApplicationMailCount(CustomerForm form) {

		// 未読応募メール件数の取得（送信者が会員、非会員で、未読の応募メールを検索）
		form.unopenedAppricationCnt = mailService.countReceivedApplicationMail(Integer.parseInt(form.id), MailStatusEnum.UNOPENED, SenderKbnEnum.MEMBER, SenderKbnEnum.NO_MEMBER);
	}

	/**
	 * 未読スカウトメール数をフォームにセット
	 * @param form 顧客管理フォーム
	 */
	private void convertUnopenedScoutMailCount(CustomerForm form) {

		// 未読スカウトメール件数の取得（送信者が会員で、未読の応募メールを検索）
		form.unopenedScoutCnt = mailService.countReceivedScoutMail(Integer.parseInt(form.id), MailStatusEnum.UNOPENED, SenderKbnEnum.MEMBER);
	}

	/**
	 * 最終ログイン履歴をフォームにセット
	 * @param form 顧客管理フォーム
	 */
	private void convertCustomerLoginHistory(CustomerForm form) {

		try {
			// 顧客ログイン履歴からデータを取得
			TCustomerLoginHistory entity = customerLoginHistoryService.getEntityByCustomerId(Integer.parseInt(form.id));
			// フォームにセット
			form.loginDatetime = DateUtils.getDateStr(entity.lastLoginDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

		} catch (WNoResultException e) {
			// 未ログインの場合は処理しない
		}
	}

	/**
	 * 系列店舗の情報をフォームにセット
	 * @param form 顧客管理フォーム
	 * @param property 顧客のプロパティ
	 */
	private void convertShopList(CustomerForm form, CustomerProperty property) {
		form.shopListPrefecturesCdList = property.shopListPrefecturesCdList;
		form.shopListShutokenForeignAreaKbnList = property.shopListShutokenForeignAreaKbnList;
		form.shopListCount = property.shopListCount;
		form.shopListIndustryKbnList = property.shopListIndustryKbnList;
	}

	/**
	 * サブメールアドレスをフォームにセット
	 * @param form
	 * @param property
	 */
	private void convertSubMail(CustomerForm form, CustomerProperty property) {

		for (MCustomerSubMail subMail : property.customerSubMailList) {
			form.subMailDtoList.add(Beans.createAndCopy(SubMailDto.class, subMail).execute());
		}
	}

	/**
	 * ホームページをフォームにセット
	 * @param form
	 * @param property
	 */
	private void convertHomepage(CustomerForm form, CustomerProperty property) {

		for (MCustomerHomepage homepage : property.customerHomepageList) {
			form.homepageDtoList.add(Beans.createAndCopy(HomepageDto.class, homepage).execute());
		}
	}

	/**
	 * メルマガエリアをフォームにセット
	 * @param form
	 * @param property
	 */
	private void convertMailMagazineArea(CustomerForm form, CustomerProperty property) {
		for (Integer areaCd : property.mailMagazineAreaCdList) {
			form.mailMagazineAreaCdList.add(String.valueOf(areaCd));
		}
	}

	/**
	 * 半年後のタイムスタンプを作成
	 * @return
	 */
	protected Timestamp createHalfYearLatorTimestamp() {
		return new Timestamp(org.apache.commons.lang.time.DateUtils.addDays(
							DateUtils.getJustDate(),
							NumberUtils.toInt("gc.halfYear.days", GourmetCareeConstants.HALF_YEAR_DAYS))
							.getTime());
	}

	/**
	 * 指定した日付から半年後のタイムスタンプを作成
	 * @param scoutUseStartDateTime
	 * @return
	 */
	protected Timestamp createHalfYearLatorTimestamp(Date scoutUseStartDateTime) {
		return new Timestamp(org.apache.commons.lang.time.DateUtils.addDays(
							scoutUseStartDateTime,
							NumberUtils.toInt("gc.halfYear.days", GourmetCareeConstants.HALF_YEAR_DAYS))
							.getTime());
	}

	/**
	 * サブメールエンティティに変換
	 * @return サブメールエンティティのリスト
	 */
	protected List<MCustomerSubMail> convertToSubMailList(CustomerForm form) {

		if (CollectionUtils.isEmpty(form.subMailDtoList)) {
			return new ArrayList<MCustomerSubMail>(0);
		}

		List<MCustomerSubMail> list = new ArrayList<>();
		for (SubMailDto dto : form.subMailDtoList) {
			list.add(Beans.createAndCopy(MCustomerSubMail.class, dto).execute());
		}
		return list;
	}

	/**
	 * ホームページエンティティに変換
	 * @return ホームページエンティティのリスト
	 */
	protected List<MCustomerHomepage> convertToHomepageList(CustomerForm form) {

		if (CollectionUtils.isEmpty(form.homepageDtoList)) {
			return new ArrayList<MCustomerHomepage>(0);
		}

		List<MCustomerHomepage> list = new ArrayList<>();
		for (HomepageDto dto : form.homepageDtoList) {
			list.add(Beans.createAndCopy(MCustomerHomepage.class, dto).execute());
		}
		return list;
	}

	/**
	 * StringのメルマガエリアリストをIntegerのリストに入れ替える
	 * @param form
	 * @return
	 */
	protected List<Integer> convertToIntegerMailManazineAreaCd(CustomerForm form) {
		if (CollectionUtils.isEmpty(form.mailMagazineAreaCdList)) {
			return new ArrayList<Integer>(0);
		}
		List<Integer> areaList = new ArrayList<>();
		for (String area : form.mailMagazineAreaCdList) {
			if (StringUtils.isNotEmpty(area)) {
				areaList.add(Integer.parseInt(area));
			}
		}
		return areaList;
	}


}