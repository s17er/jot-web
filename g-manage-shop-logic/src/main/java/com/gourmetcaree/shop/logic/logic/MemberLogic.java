package com.gourmetcaree.shop.logic.logic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.entity.VMemberHopeCity;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ActiveScoutMailService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.FootprintService;
import com.gourmetcaree.db.common.service.MemberMailboxService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.MemberService.SearchProperty;
import com.gourmetcaree.db.common.service.ReleaseWebService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;
import com.gourmetcaree.db.common.service.ScoutBlockService;
import com.gourmetcaree.db.common.service.ScoutConsiderationService;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;
import com.gourmetcaree.shop.logic.dto.CareerDto;
import com.gourmetcaree.shop.logic.dto.MemberDetailDto;
import com.gourmetcaree.shop.logic.dto.MemberInfoDto;
import com.gourmetcaree.shop.logic.dto.ScoutMailRemainDto;
import com.gourmetcaree.shop.logic.property.MemberProperty;

/**
 * 会員に関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class MemberLogic  extends AbstractShopLogic {

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	/** 足あとサービス */
	@Resource
	protected FootprintService footprintService;

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** スカウトブロックサービス */
	@Resource
	protected ScoutBlockService scoutBlockService;

	/** スカウト検討中サービス */
	@Resource
	protected ScoutConsiderationService scoutConsiderationService;

	/** 学歴サービス */
	@Resource
	protected SchoolHistoryService schoolHistoryService;

	/** 職歴サービス */
	@Resource
	protected CareerHistoryService careerHistoryService;

	/** 掲載WEBデータサービス */
	@Resource
	protected ReleaseWebService releaseWebService;

	/** 使用可能なスカウトメールサービス */
	@Resource
	private ActiveScoutMailService activeScoutMailService;

	/** 送受信を整理した会員のメールボックスサービス */
	@Resource
	private MemberMailboxService memberMailboxService;

	/** キープBOXページ区分 */
	public static final String KEEPBOX_PAGE_KBN = "2";



	/**
	 * 会員検索メインロジック
	 * @param targetPage
	 * @throws ParseException
	 * @throws WNoResultException
	 */
	public List<MMember> doSearch(MemberProperty property) throws WNoResultException, ParseException {
		return memberService.doSearchByCustomer(property.pageNavi, createSearchProperty(property));
	}

	public MMember findById(int memberId) {
		return memberService.findById(memberId);
	}


	/**
	 * 検索プロパティを作成
	 * @param property
	 * @return
	 */
	private SearchProperty createSearchProperty(MemberProperty property) {
		SearchProperty p = new SearchProperty();
		p.inductryCdList = GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(property.where_industryCd));
		p.employPtnKbnList = GourmetCareeUtil.convertStringArrayToIntegerList(property.where_empPtnKbn);
		p.jobKbnList = GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(property.where_job));
		p.qualificationList = GourmetCareeUtil.convertStringArrayToIntegerList(property.where_qualification);
		p.prefList = GourmetCareeUtil.convertStringArrayToIntegerList(property.where_prefList);
		p.addressList = GourmetCareeUtil.convertStringArrayToIntegerList(property.where_addressList);
		// XXX リニューアル後、半年程度はこの処理を残す
		// （旧）希望勤務地
		p.webAreaCdList = GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(property.where_workLocation));

		p.sexKbnList = GourmetCareeUtil.convertStringToIntegerList(property.where_sexKbn);
		if (NumberUtils.isDigits(property.where_lowerAge)) {
			p.lowerAge = Integer.parseInt(property.where_lowerAge);
		}

		if (NumberUtils.isDigits(property.where_upperAge)) {
			p.upperAge = Integer.parseInt(property.where_upperAge);
		}

		if (NumberUtils.isDigits(property.where_transferFlg)) {
			p.transferFlg = Integer.parseInt(property.where_transferFlg);
		}

		if (NumberUtils.isDigits(property.where_midnightShiftFlg)) {
			p.midnightShiftFlg = Integer.parseInt(property.where_midnightShiftFlg);
		}
		if (NumberUtils.isDigits(property.where_scoutedFlg)) {
			p.scoutedFlg = Integer.parseInt(property.where_scoutedFlg);
		}
		if (NumberUtils.isDigits(property.where_favoriteFlg)) {
			p.favoriteFlg = Integer.parseInt(property.where_favoriteFlg);
		}
		if (NumberUtils.isDigits(property.where_subscFlg)) {
			p.subscFlg = Integer.parseInt(property.where_subscFlg);
		}
		if (NumberUtils.isDigits(property.where_refuseFlg)) {
			p.refuseFlg = Integer.parseInt(property.where_refuseFlg);
		}

		p.keyword = property.where_keyword;

		p.cityCdList = Arrays.asList(GourmetCareeUtil.removeBlankElement(property.where_areaList));


		// テスト会員は、顧客の求職者検索に表示されないようにする為、除外する値をmapにセットしておく
		List<Integer> exceptMemberKbnList = new ArrayList<Integer>();
		exceptMemberKbnList.add(MTypeConstants.MemberKbn.TEST_MEMBER);
		exceptMemberKbnList.add(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER);
		exceptMemberKbnList.add(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_TEST_MEMBER);

		p.exceptMemberKbnList = exceptMemberKbnList;

		// スカウトを受けない会員は、顧客の求職者検索に表示されないようにする為、除外する値をmapにセットしておく
		p.exceptScoutKbn = MTypeConstants.ScoutMailReceptionFlg.NOT_RECEPTION;

		// スカウトブロック判定のために顧客ＩＤをセット
		p.customerId = property.customerId;


		return p;


	}


	/**
	 * 表示用リストを生成
	 * @param entityList GCWコードエンティティリスト
	 */
	public List<MemberInfoDto> convertShowList(MemberProperty property) {

		List<MemberInfoDto> dtoList = new ArrayList<MemberInfoDto>();

		for (MMember entity : property.memberEntityList) {
			MemberInfoDto dto = new MemberInfoDto();

			//Date型の変換は後続処理で行う。
			Beans.copy(entity, dto).execute();
			if (KEEPBOX_PAGE_KBN.equals(property.pageKbn)) {
				dto.keepId = entity.tScoutConsideration.id;
			}
			dto.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			dto.lastLoginDatetime = DateUtils.getDateStr(entity.tLoginHistory.lastLoginDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
			dto.detailPath = GourmetCareeUtil.makePath("/member/detail/", "index", String.valueOf(entity.id));

			List<String> industryList = new ArrayList<String>();
			List<String> jobList = new ArrayList<String>();
			dto.employPtnKbnList = new ArrayList<String>();
			if (CollectionUtils.isNotEmpty(entity.mMemberAttributeList)) {
				for (MMemberAttribute attrEntity : entity.mMemberAttributeList) {
					if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						industryList.add(String.valueOf(attrEntity.attributeValue));
					} else if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						jobList.add(String.valueOf(attrEntity.attributeValue));
					} else if (MTypeConstants.EmployPtnKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						dto.employPtnKbnList.add(String.valueOf(attrEntity.attributeValue));
					}
				}
			}

			dto.industry = industryList.toArray(new String[0]);
			dto.job = jobList.toArray(new String[0]);

			dto.hopeCityMap = new LinkedHashMap<>();
			if (CollectionUtils.isNotEmpty(entity.vMemberHopeCityList)) {
				for (VMemberHopeCity cityEntity : entity.vMemberHopeCityList) {
					if (dto.hopeCityMap.containsKey(cityEntity.prefecturesCd)) {
						dto.hopeCityMap.get(cityEntity.prefecturesCd).add(cityEntity.cityName);
					} else {
						List<String> list = new ArrayList<>();
						list.add(cityEntity.cityName);
						dto.hopeCityMap.put(cityEntity.prefecturesCd, list);
					}
				}
			}

			try {
				// 会員の状況を取得
				MemberStatusDto statusDto = memberService.getMemberStatusDto(entity.id, property.customerId);
				// コピーする
				Beans.copy(statusDto, dto).execute();

				// スカウトメール受信フラグをセット
				if (MTypeConstants.ScoutMailReceptionFlg.RECEPTION == entity.scoutMailReceptionFlg && statusDto.scoutBlockFlg == MemberStatusDto.ScoutBlockFlgKbn.NOT_BLOCK) {
					dto.scoutMailOkFlg = true;
				}
			} catch (WNoResultException e) {
				// 状況が取得できない場合、処理をしない
			}

			// スカウト受取、断るの設定
			dto.scoutReceiveKbn = memberMailboxService.getLastScoutReceiveKbn(property.customerId, entity.id);

//			スカウトメール
			//dto.vMemberMailbox = memberMailboxService.getLastScout(property.customerId, entity.id);
			dto.vMemberMailbox = null;

//			応募メール
			//dto.applicationMail = memberMailboxService.getLastApplication(property.customerId, entity.id);
			dto.applicationMail = null;

//			足あと（気になる）と登録日時
			dto.tFootprint = footprintService.getTFootprint(property.customerId, entity.id);
			//dto.tFootprintDate = footprintService.getTFootprintDate(dto.tFootprint);
			dto.tFootprintDate = null;

			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 会員詳細表示データをセットする
	 * @param 会員プロパティ
	 * @throws WNoResultException 会員情報がない場合スローする
	 */
	public MemberDetailDto convertDispData(MemberProperty property) throws WNoResultException {

		MemberDetailDto memberDetailDto = new MemberDetailDto();

		// 会員情報・会員属性情報をフォームにセット
		convertmemberData(property, memberDetailDto);

		// スカウトを受け取るに設定していてスカウトブロックに指定されていない場合
		// 学歴・職歴をフォームにセット


			// 学歴情報をフォームにセット
			convertSchoolHistoryData(property, memberDetailDto);

			// 職歴・職歴属性情報をフォームにセット
			convertCareerData(property, memberDetailDto);

			memberDetailDto.scoutMailFlg = true;


		// パスの生成
		createDetailPath(memberDetailDto);

//		// スカウトメール数
		memberDetailDto.scoutCount = activeScoutMailService.getUsableScoutMailCount(property.customerId);

		// スカウトメール使用可否をセット
		memberDetailDto.scoutUseFlg = isScoutUse(property.customerId);

		// キープBOX追加フラグをセット
		memberDetailDto.keepFlg = setKeepFlg(property.customerId, property.memberId);

		// スカウト受取区分
		memberDetailDto.scoutReceiveKbn = memberMailboxService.getLastScoutReceiveKbn(property.customerId, property.memberId);

		// 気になる済フラグ
		memberDetailDto.footPrintFlg = footprintService.isExitMemberFootPrint(property.customerId, property.memberId);

		return memberDetailDto;
	}

	/**
	 * 会員情報・会員属性情報をフォームにセット
	 * @param property 会員プロパティ
	 * @param memberDetailDto 会員詳細DTO
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void convertmemberData(MemberProperty property, MemberDetailDto memberDetailDto) throws WNoResultException {

		// テスト会員は検索しないようにする
		MMember member = memberService.getMemberDataById(property.memberId, MTypeConstants.MemberKbn.TEST_MEMBER);

		//Date型の変換は後続処理で行う
		Beans.copy(member, memberDetailDto).execute();

		if (member.tLoginHistory != null && member.tLoginHistory.lastLoginDatetime != null) {
			memberDetailDto.lastLoginDatetime = DateUtils.getDateStr(member.tLoginHistory.lastLoginDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		}

		memberDetailDto.age = String.valueOf(GourmetCareeUtil.convertToAge(member.birthday));

		// 会員属性をフォームにセット
		List<String> jobList = new ArrayList<String>();
		List<String> industryList = new ArrayList<String>();
		List<String> qualificationList = new ArrayList<String>();
		List<Integer> workLocationIntList = new ArrayList<Integer>();
		List<String> workLocationList = new ArrayList<String>();
		List<Integer> oldHopeCityCdList = new ArrayList<Integer>();
		memberDetailDto.employPtnKbnList = new ArrayList<>();

		for (MMemberAttribute attrEntity : member.mMemberAttributeList) {

			if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				jobList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				industryList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.QualificationKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				qualificationList.add(String.valueOf(attrEntity.attributeValue));
			} else if (MTypeConstants.EmployPtnKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				memberDetailDto.employPtnKbnList.add(String.valueOf(attrEntity.attributeValue));
			}else if(MTypeConstants.ShutokenWebAreaKbn.TYPE_CD.equals(attrEntity.attributeCd) || MTypeConstants.SendaiWebAreaKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
				oldHopeCityCdList.add(attrEntity.attributeValue);
			}
		}

		memberDetailDto.job = jobList.toArray(new String[0]);
		memberDetailDto.industry = industryList.toArray(new String[0]);
		memberDetailDto.qualification = qualificationList.toArray(new String[0]);
		memberDetailDto.oldHopeCityCdList = oldHopeCityCdList;
		Collections.sort(workLocationIntList);
		for (Integer workLocation : workLocationIntList) {
			workLocationList.add(Integer.toString(workLocation));
		}
		memberDetailDto.workLocation = workLocationList.toArray(new String[0]);


		memberDetailDto.hopeCityMap = new LinkedHashMap<>();
		for (VMemberHopeCity cityEntity : member.vMemberHopeCityList) {
			if (memberDetailDto.hopeCityMap.containsKey(cityEntity.prefecturesCd)) {
				memberDetailDto.hopeCityMap.get(cityEntity.prefecturesCd).add(cityEntity.cityName);
			} else {
				List<String> list = new ArrayList<>();
				list.add(cityEntity.cityName);
				memberDetailDto.hopeCityMap.put(cityEntity.prefecturesCd, list);
			}
		}

	}

	/**
	 * 学歴情報をフォームにセット
	 * @param form 会員フォーム
	 */
	private void convertSchoolHistoryData(MemberProperty property, MemberDetailDto memberDetailDto) {

		try {
			TSchoolHistory tSchoolHistory = schoolHistoryService.getTSchoolHistoryDataByMemberId(property.memberId);

			Beans.copy(tSchoolHistory, memberDetailDto).excludes("id").execute();
		} catch (WNoResultException e) {
			// 学歴情報がない場合は、何も処理しない。
		}
	}

	/**
	 * 職歴情報をフォームにセット
	 * @param property 会員プロパティ
	 * @param memberDetailDto 会員詳細DTO
	 */
	private void convertCareerData(MemberProperty property, MemberDetailDto memberDetailDto) {

		try {
			List<TCareerHistory> entityList = careerHistoryService.getCareerHistoryDataByMemberId(property.memberId);

			List<CareerDto> careerList = new ArrayList<CareerDto>();
			for (TCareerHistory entity : entityList) {
				CareerDto dto = new CareerDto();
				List<String> jobList = new ArrayList<String>();
				List<String> industryList = new ArrayList<String>();
				Beans.copy(entity, dto).execute();

				for (TCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {

					// 職種
					if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						jobList.add(String.valueOf(attrEntity.attributeValue));
					}

					// 業態
					if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
						industryList.add(String.valueOf(attrEntity.attributeValue));
					}
				}

				dto.job = jobList.toArray(new String[0]);
				dto.industry = industryList.toArray(new String[0]);
				careerList.add(dto);
			}

			memberDetailDto.careerList = careerList;

		} catch (WNoResultException e) {
			// 職歴がない場合は、空の職歴を作成
			memberDetailDto.careerList = new ArrayList<CareerDto>();
			CareerDto dto = new CareerDto();
			dto.companyName = "";
			memberDetailDto.careerList.add(dto);
		}
	}

	/**
	 * パスを生成する
	 */
	private void createDetailPath(MemberDetailDto memberDetailDto) {

		// キープBOXに追加パス
		memberDetailDto.keepPath = GourmetCareeUtil.makePath("/member/detail/", "addKeepBox", String.valueOf(memberDetailDto.id));
		// 足あとをつけるパス
		memberDetailDto.footPrintPath = GourmetCareeUtil.makePath("/member/detail/", "footPrint", String.valueOf(memberDetailDto.id));
	}


	/**
	 * 購入分・無期限分のスカウトメールを作成
	 *
	 * @param customerId
	 */
	public ScoutMailRemainDto getRemainScoutMail(int customerId) {
		ScoutMailRemainDto dto = new ScoutMailRemainDto();

		VActiveScoutMail unLimitScoutMail = activeScoutMailService.exitUnlimitScoutMail(customerId);

		dto.isUseUnlimitScoutMailFlg = unLimitScoutMail != null;

		if(!dto.isUseUnlimitScoutMailFlg) {
			dto.remainFreeScoutCount = activeScoutMailService.getUsableFreeScoutMailCount(customerId);
			dto.paidMailList = activeScoutMailService.getPaidScoutMail(customerId);
			int scoutCount = 0;
			for(VActiveScoutMail entity : dto.paidMailList) {
				scoutCount += entity.scoutRemainCount;
			}
			dto.remainPaidScoutCount = scoutCount;
		} else {
			// 無制限なので仮で10000を入れる
			dto.remainPaidScoutCount = 10000;
			dto.useEndDatetime = unLimitScoutMail.useEndDatetime;
		}

		return dto;
	}


	/**
	 * スカウトメール使用可否を返す
	 * @param customerId 顧客ID
	 */
	public boolean isScoutUse(int customerId) {

		// スカウトメール利用不可の場合はfalse
		if (!customerService.isScoutUse(customerId)) {
			return false;
		}

		// 掲載中WEBデータが存在するかどうか
		return releaseWebService.isPsotCustomerExists(customerId);
	}

	/**
	 * キープ済みかどうかを返す
	 * @param customerId
	 */
	public boolean setKeepFlg(int customerId, int memberId) {

		return scoutConsiderationService.isAlredyKeep(customerId, memberId);
	}
}
