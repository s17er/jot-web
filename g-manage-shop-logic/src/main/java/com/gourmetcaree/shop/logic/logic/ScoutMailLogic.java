package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.UUID;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ScoutReceiveKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TLoginHistory;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.entity.TScoutMailManage;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.entity.VMemberHopeCity;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ActiveScoutMailService;
import com.gourmetcaree.db.common.service.AreaService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.db.common.service.LoginHistoryService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.MemberAttributeService;
import com.gourmetcaree.db.common.service.MemberMailboxService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.PrefecturesService;
import com.gourmetcaree.db.common.service.ReleaseWebService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;
import com.gourmetcaree.db.common.service.ScoutBlockService;
import com.gourmetcaree.db.common.service.ScoutHistoryService;
import com.gourmetcaree.db.common.service.ScoutMailCountService;
import com.gourmetcaree.db.common.service.ScoutMailLogService;
import com.gourmetcaree.db.common.service.ScoutMailManageService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;
import com.gourmetcaree.db.scoutFoot.dto.scoutMail.ScoutMailListDto;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants.PatternReplace;
import com.gourmetcaree.shop.logic.csv.ScoutMailCsv;
import com.gourmetcaree.shop.logic.dto.ScoutMailDetailDto;
import com.gourmetcaree.shop.logic.dto.ScoutMailDto;
import com.gourmetcaree.shop.logic.dto.mai.ScoutMailReceiveInfoToMemberMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ScoutMailSendInfoToAdminMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ScoutMailSendInfoToCustomerMaiDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.ApplicationSearchProperty;
import com.gourmetcaree.shop.logic.mai.GourmetcareeMai;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * スカウトメールに関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class ScoutMailLogic  extends AbstractShopLogic {

	/** 気になる返信時のスカウトメール使用数 */
	private Integer INTEREST_SCOUT_MAIL_USAGE_COUNT = 1;

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ScoutMailLogic.class);

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** 会員属性サービス */
	@Resource
	protected MemberAttributeService memberAttributeService;

	/** スカウトブロックサービス */
	@Resource
	protected ScoutBlockService scoutBlockService;

	/** 学歴サービス */
	@Resource
	protected SchoolHistoryService schoolHistoryService;

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	/** メール送信用インタフェース */
	@Resource
	protected GourmetcareeMai gourmetCareeMail;

	/** WEBリストサービス */
	@Resource
	protected WebListService webListService;

	/** スカウトメール数サービス */
	@Resource
	protected ScoutMailCountService scoutMailCountService;

	/** スカウト履歴サービス */
	@Resource
	protected ScoutHistoryService scoutHistoryService;

	/** 掲載中WEBデータサービス */
	@Resource
	protected ReleaseWebService releaseWebService;

	/** 使用可能スカウトメールサービス */
	@Resource
	private ActiveScoutMailService activeScoutMailService;

	/** スカウトメール管理サービス */
	@Resource
	private ScoutMailManageService scoutMailManageService;

	/** スカウトメールログサービス */
	@Resource
	private ScoutMailLogService scoutMailLogService;

	/** 都道府県サービス */
	@Resource
	private PrefecturesService prefecturesService;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	private LoginHistoryService loginHistoryService;

	@Resource
	protected ApplicationLogic applicationLogic;

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 送受信を整理した会員のメールボックスサービス */
	@Resource
	private MemberMailboxService memberMailboxService;

	@Resource
	private CustomerSubMailService customerSubMailService;

	@Resource
	private AreaService areaService;

	/**
	 * メールプロパティを取得します。
	 * @return メールプロパティ
	 */
	private Properties getMailProerties() {
		return ResourceUtil.getProperties("sendMail.properties");
	}

	/**
	 * スカウトの管理メールアドレスをプロパティから取得します。
	 * @return スカウト用管理アドレス
	 */
	public String getScoutAddress() {
		String scoutAddressKey = "gc.mail.scoutKanriAddress" + getAreaCd();
		return getMailProerties().getProperty(scoutAddressKey);
	}

	/**
	 * スカウトの管理メール名をプロパティから取得します。
	 * @return スカウト用管理メール名
	 */
	public String getScoutName() {
		String scoutNameKey = "gc.mail.scoutKanriName" + getAreaCd();
		return getMailProerties().getProperty(scoutNameKey);
	}

	/**
	 * MYページのPCログインURLをプロパティから取得します。
	 * @return MYページのPCログインURL
	 */
	public String getMemberPCLoginURL(int areaCd) {
		String loginURLKey = "gc.member.pcLoginUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * MYページの携帯ログインURLをプロパティから取得します。
	 * @return MYページの携帯ログインURL
	 */
	public String getMemberMobileLoginURL(int areaCd) {
		String loginURLKey = "gc.member.mobileLoginUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * お問い合わせのメールアドレスをプロパティから取得します。
	 * @param areaCd エリアコード
	 * @return お問い合わせメールアドレス
	 */
	public String getInfoAddress(int areaCd) {
		String infoAddressKey = "gc.mail.infoAddress" + areaCd;
		return getMailProerties().getProperty(infoAddressKey);
	}

	/**
	 * グルメキャリーのサイトURLを取得します。
	 * @param areaCd エリアコード
	 * @return グルメキャリーURL
	 */
	public String getSiteUrl(int areaCd) {
		String siteUrlKey = "gc.front.siteUrl" + areaCd;
		return getMailProerties().getProperty(siteUrlKey);
	}

	/**
	 * スカウトメールのリストを取得
	 * @param property スカウトメールプロパティ
	 * @return スカウトメールリスト
	 * @throws WNoResultException
	 */
	public List<ScoutMailListDto> getScoutMailList(ScoutMailProperty property, Map<String,Object> searchRequestsMap) throws WNoResultException {

		return mailService.getScoutMailList(property.sendKbn, property.customerId, property.pageNavi, property.targetPage, searchRequestsMap);
	}


	/**
	 * スカウトメールの対象が存在するかどうか。
	 * 指定の条件で、スカウト初回送信一時が一年以内のものが対象となる。
	 *
	 * selectScoutTargetListと同じロジックを使っている。
	 * @param property
	 * @return
	 */
	public boolean existsFirstScoutMail(ScoutmailSearchProperty property) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>(0);

		createScoutTargetSelectSql(sql, params);

		if (property.scountId != null ) {
			sql.append("   AND SCOUT.id = ? ");
				params.add(property.scountId);
		}

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		return count > 0l;
	}


	/**
	 * スカウト対象リストをセレクトします。
	 * 指定の条件で、スカウト初回送信一時が一年以内のものが対象となる。
	 * @param property
	 * @return
	 */
	public List<ScoutMailTargetDto> selectScoutTargetList(final ScoutmailSearchProperty property) {

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>(0);

		createScoutTargetSelectSql(sql, params);

		if (property.scountId != null ) {
			sql.append("   AND SCOUT.id = ? ");
				params.add(property.scountId);
		}

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		// スカウトメールが存在しない場合
		if (count == 0) {
			return new ArrayList<ScoutMailTargetDto>(0);
		}

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.changeAllCount((int) count);
		pageNavi.setPage(property.targetPage);


		sql.append(" ORDER BY SCOUT.send_datetime DESC ");

		property.pageNavi = pageNavi;

		List<ScoutMailTargetDto> list =
		jdbcManager.selectBySql(TScoutMailLog.class, sql.toString(), params.toArray())
					.limit(pageNavi.limit)
					.offset(pageNavi.offset)
					.iterate(new IterationCallback<TScoutMailLog, List<ScoutMailTargetDto>>() {
						List<ScoutMailTargetDto> retList = new ArrayList<ScoutMailLogic.ScoutMailTargetDto>(property.maxRow);

						@Override
						public List<ScoutMailTargetDto> iterate(TScoutMailLog entity, IterationContext context) {
							if (entity == null) {
								return retList;
							}

							ScoutMailTargetDto dto = Beans.createAndCopy(ScoutMailTargetDto.class, entity).execute();
							MMember member;
							try {
								member = memberService.findById(entity.memberId);
							} catch (SNoResultException e) {
								dto.exitMemberFlg = false;
								retList.add(dto);
								return retList;
							}
							dto.exitMemberFlg = true;
							Beans.copy(member, dto).includes(ScoutMailTargetDto.MEMBER_COPY_PROPERTIES).execute();

							try {
								 dto.memberStatusDto = memberService.getMemberStatusDto(member.id, getCustomerId());
							} catch (WNoResultException e) {
								dto.memberStatusDto = new MemberStatusDto();
							}

							dto.industryKbnList = memberAttributeService.getMemberAttributeValueStringList(entity.memberId, MTypeConstants.IndustryKbn.TYPE_CD);
							dto.jobKbnList = memberAttributeService.getMemberAttributeValueStringList(entity.memberId, MTypeConstants.JobKbn.TYPE_CD);
							dto.employPtnKbnList = memberAttributeService.getMemberAttributeValueStringList(entity.memberId, MTypeConstants.EmployPtnKbn.TYPE_CD);
							dto.qualificationKbnList = memberAttributeService.getMemberAttributeValueList(entity.memberId, MTypeConstants.QualificationKbn.TYPE_CD);

							List<VMemberHopeCity> cityList = jdbcManager.from(VMemberHopeCity.class)
									.where(new SimpleWhere().eq(toCamelCase(VMemberHopeCity.MEMBER_ID), entity.memberId))
									.orderBy(createCommaStr(new String[] {toCamelCase(VMemberHopeCity.PREFECTURES_CD), toCamelCase(VMemberHopeCity.CITY_CD)}))
									.getResultList();

							dto.hopeCityMap = new LinkedHashMap<>();
							if (CollectionUtils.isNotEmpty(cityList)) {
								for (VMemberHopeCity cityEntity : cityList) {
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
								TSchoolHistory schoolHistory = schoolHistoryService.getTSchoolHistoryDataByMemberId(member.id);
								dto.schoolName = schoolHistory.schoolName;
								dto.department = schoolHistory.department;
								dto.graduationKbn = schoolHistory.graduationKbn;
								dto.schoolHistoryExistFlg = true;
							} catch (WNoResultException e1) {
								dto.schoolHistoryExistFlg = false;
							}

							// スカウト受取、断るの設定
							dto.scoutReceiveKbn = memberMailboxService.getLastScoutReceiveKbn(getCustomerId(), entity.memberId);

							try {
								dto.memberStatusDto = memberService.getMemberStatusDto(entity.memberId, getCustomerId());
							} catch (WNoResultException e) {
								dto.memberStatusDto = new MemberStatusDto();
							}

							dto.unopenedMailFlg = applicationLogic.isUnopenedApplicantMailExist(entity.id, MTypeConstants.MailKbn.SCOUT);

							retList.add(dto);

							return retList;

						}

					});

		if (list == null) {
			return new ArrayList<ScoutMailTargetDto>(0);
		}

		return list;
	}


	/**
	 * CSVをアウトプットします。
	 * @param response
	 * @author Takehiro Nakamori
	 * @throws WNoResultException
	 */
	public void outputCsv(HttpServletResponse response, String fileName) throws WNoResultException, IOException {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>(0);

		createScoutTargetSelectSql(sql, params);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			throw new WNoResultException("CSVに出力する結果がありません。");
		}

		sql.append(" ORDER BY SCOUT.send_datetime DESC ");

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

		String csvFileName = String.format("%s_%s.csv", fileName, format.format(new Date()));

		response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
		response.setHeader(GourmetCareeConstants.CSV_HEADER_PARAM1,
				GourmetCareeConstants.CSV_HEADER_FILENAME_PREFIX.concat(csvFileName));

		PrintWriter writer = null;
		S2CSVWriteCtrl<ScoutMailCsv> csvWriter = null;
		SqlSelect<TScoutMailLog> select = jdbcManager.selectBySql(TScoutMailLog.class, sql.toString(), params.toArray());
		try {
			writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), GourmetCareeConstants.CSV_ENCODING));
			csvWriter = s2CSVCtrlFactory.getWriteController(ScoutMailCsv.class, writer);
			writeCsv(csvWriter, select);
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
				csvWriter = null;
			}
		}
	}


	/**
	 * CSVを書き込みます。
	 * @param writer CSVライター
	 * @param select セレクト
	 */
	private void writeCsv(final S2CSVWriteCtrl<ScoutMailCsv> writer, SqlSelect<TScoutMailLog> select) {

		select.iterate(new IterationCallback<TScoutMailLog, Void>() {

			@Override
			public Void iterate(TScoutMailLog entity, IterationContext context) {
				if (entity == null) {
					return null;
				}

				ScoutMailCsv csv = Beans.createAndCopy(ScoutMailCsv.class, entity)
										.execute();

				MMember member;
				try {
					member = memberService.findById(entity.memberId);
				} catch (SNoResultException e) {
					log.info(String.format("会員が削除されているか見つからなかったためスキップします。会員ID:[%d]", entity.memberId), e);
					return null;
				}
				csv.scoutSelfPr = member.scoutSelfPr;

				StringBuilder address = new StringBuilder(0);
				if (member.prefecturesCd != null) {
					address.append(valueToNameConvertLogic.convertToPrefecturesName(member.prefecturesCd));
				}
				if (StringUtils.isNotBlank(member.municipality)) {
					address.append(member.municipality);
				}

				csv.address = GourmetCareeUtil.toMunicipality(address.toString());

				if (member.birthday != null) {
					csv.age = GourmetCareeUtil.convertToAge(member.birthday);
				}

				if (member.sexKbn != null) {
					csv.sex = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, member.sexKbn);
				}

				if (member.foodExpKbn != null) {
					csv.foodExpKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.FoodExpKbn.TYPE_CD, member.foodExpKbn);
				}

				if (member.foreignWorkFlg != null) {
					csv.foreignWorkFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ForeignWorkFlg.TYPE_CD, member.foreignWorkFlg);
				}

				List<String> employPtnKbnList = memberAttributeService.getMemberAttributeValueStringList(member.id, MTypeConstants.EmployPtnKbn.TYPE_CD);
				if (CollectionUtils.isNotEmpty(employPtnKbnList)) {
					csv.employPtnKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, employPtnKbnList.toArray(new String[employPtnKbnList.size()]));
				}

				if (member.transferFlg != null) {
					csv.transferFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TransferFlg.TYPE_CD, member.transferFlg);
				}

				if (member.midnightShiftFlg != null) {
					csv.midnightShiftFlgStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MidnightShiftFlg.TYPE_CD, member.midnightShiftFlg);
				}

				if (member.salaryKbn != null) {
					csv.salaryKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalaryKbn.TYPE_CD, member.salaryKbn);
				}

				try {
					TLoginHistory history = loginHistoryService.getEntityByMemberId(entity.memberId);
					csv.lastLoginDate = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH).format(history.lastLoginDatetime);
				} catch (WNoResultException e) {
					// 何もしない。
				}


				addAttributeToCsv(csv, entity.memberId);
				writer.write(csv);
				return null;
			}


		});
	}

	/**
	 * 属性をCSVに追加します
	 * @param csv CSV
	 * @param memberId 会員ID
	 */
	private void addAttributeToCsv(ScoutMailCsv csv, int memberId) {
		csv.qualificationKbnStr = getAttributeStr(memberId, MTypeConstants.QualificationKbn.TYPE_CD);
		csv.jobKbnKbnStr = getAttributeStr(memberId, MTypeConstants.JobKbn.TYPE_CD);
		csv.industryKbnStr = getAttributeStr(memberId, MTypeConstants.IndustryKbn.TYPE_CD);

		StringBuilder sb;
		String shutokenWebArea = getAttributeStr(memberId, MTypeConstants.ShutokenWebAreaKbn.TYPE_CD);
		if (StringUtils.isNotBlank(shutokenWebArea)) {
			sb = new StringBuilder(shutokenWebArea);
		} else {
			sb = new StringBuilder(0);
		}

		String shutokenForeignWebArea = getAttributeStr(memberId, MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD);
		if (StringUtils.isNotBlank(shutokenForeignWebArea)) {
			if (StringUtils.isNotBlank(shutokenWebArea)){
				sb.append(", ");
			}
			sb.append(shutokenForeignWebArea);
		}

		String sendaiWebArea = getAttributeStr(memberId, MTypeConstants.SendaiWebAreaKbn.TYPE_CD);
		if (StringUtils.isNotBlank(sendaiWebArea)) {
			if (StringUtils.isNotBlank(shutokenWebArea) || StringUtils.isNotBlank(shutokenForeignWebArea)){
				sb.append(", ");
			}
			sb.append(sendaiWebArea);
		}

		csv.webAreaStr = sb.toString();


	}

	private String getAttributeStr(int memberId, String attributeCd) {
		List<String> valueList = memberAttributeService.getMemberAttributeValueStringList(memberId, attributeCd);
		if (CollectionUtils.isEmpty(valueList)) {
			return "";
		}

		return valueToNameConvertLogic.convertToTypeName(attributeCd, valueList.toArray(new String[0]));
	}

	/**
	 * スカウト対象をセレクトするSQLを作成します。
	 * @param sql SQL
	 * @param params パラメータ
	 */
	private void createScoutTargetSelectSql(StringBuffer sql, List<Object> params) {

		sql.append(" SELECT ");
		sql.append("   SCOUT.* ");
		sql.append(" FROM ");
		sql.append("   t_scout_mail_log SCOUT ");
		sql.append(" WHERE ");
		sql.append("   EXISTS (SELECT * FROM t_scout_mail_manage MANAGE WHERE MANAGE.customer_id = ? AND SCOUT.scout_manage_id = MANAGE.id) ");
		sql.append("   AND SCOUT.scout_mail_log_kbn = ? ");
			params.add(getCustomerId());
			params.add(MTypeConstants.ScoutMailLogKbn.USE);

		ApplicationLogic.addMailSearchSql(sql, params, new ApplicationSearchProperty(), " SCOUT.id = MAIL.scout_mail_log_id  ");

		sql.append(" AND SCOUT.delete_flg = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * スカウトメールリスト表示データを生成
	 * @param property スカウトメールプロパティ
	 * @return スカウトメール表示リスト
	 */
	public List<ScoutMailDto> convertScoutMailList(ScoutMailProperty property) {

		List<ScoutMailDto> scoutMailDtoList = new ArrayList<ScoutMailDto>();
		for (ScoutMailListDto dto : property.scoutMailListDtoList) {
			ScoutMailDto scoutMailDto = new ScoutMailDto();

			//Date型の変換は後続処理で行う。
			Beans.copy(dto, scoutMailDto).execute();
			scoutMailDto.sendDatetime = DateUtils.getDateStr(dto.sendDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			scoutMailDto.memberId = dto.fromId;
			if (dto.memberId != null) {
				scoutMailDto.existMemberFlg = true;
				scoutMailDto.memberDetailPath = GourmetCareeUtil.makePath("/member/detail/", "index", String.valueOf(dto.memberId));
			} else {
				scoutMailDto.existMemberFlg = false;
			}
			
			if(property.sendKbn == SendKbn.RECEIVE) {
				scoutMailDto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(dto.sendDatetime);
				if(scoutMailDto.existMemberFlg) {
					scoutMailDto.age = GourmetCareeUtil.convertToAge(dto.birthday);
					switch(dto.areaCd) {
						case 1:
							scoutMailDto.areaName = "首都圏";
							break;
						case 2:
							scoutMailDto.areaName = "東北";
							break;
						case 3:
							scoutMailDto.areaName = "関西";
							break;
						case 4:
							scoutMailDto.areaName = "東海";
							break;
						case 5:
							scoutMailDto.areaName = "九州・沖縄";
							break;
					}
				}
				scoutMailDto.mailDetailPath = GourmetCareeUtil.makePath("/scoutMail/detail/", "index", String.valueOf(dto.id), String.valueOf(property.sendKbn));
			}
			scoutMailDtoList.add(scoutMailDto);
		}

		return scoutMailDtoList;
	}

	/**
	 * スカウトメール詳細画面表示データを生成
	 * @param property スカウトメールプロパティ
	 * @return スカウトメール詳細DTO
	 */
	public ScoutMailDetailDto convertScoutMailDetailData(ScoutMailProperty property) {

		ScoutMailDetailDto dto = new ScoutMailDetailDto();

		//Date型の変換は後続処理で行う。
		Beans.copy(property.tMail, dto).execute();
		dto.sendDatetime = DateUtils.getDateStr(property.tMail.sendDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
		dto.returnMailFlg = true;
		dto.interestFlg = BooleanUtils.toBoolean(property.tMail.interestFlg);

		// 受信メールの場合
		if (MTypeConstants.SendKbn.RECEIVE == property.tMail.sendKbn) {
			dto.memberId = property.tMail.fromId;

			// 返信可能かチェック
			if (!customerService.isScoutUse(property.customerId)) {
				dto.returnMailFlg = false;
			}

			if (dto.returnMailFlg) {
				try {
					if (!scoutBlockService.isSetScoutBlock(property.tMail.fromId, property.customerId)) {
						dto.returnMailFlg = false;
					}

				} catch (SNoResultException e) {
					dto.returnMailFlg = false;
				}
			}
		} else {
			dto.memberId = property.tMail.toId;
		}

		return dto;
	}

	/**
	 * 未読ステータスを既読ステータスへ変更
	 * @param mailId メールID
	 */
	public void changeStatusToReaded(int mailId, int customerId, int sendKbn) throws WNoResultException {

		TMail mail = mailService.getMailDataCustomer(mailId, customerId, MTypeConstants.MailKbn.SCOUT, sendKbn);

		if (MTypeConstants.MailStatus.UNOPENED == mail.mailStatus) {
			TMail updateEntity = new TMail();
			updateEntity.id = mail.id;
			updateEntity.mailStatus = MTypeConstants.MailStatus.OPENED;
			updateEntity.readingDatetime = new Timestamp(new Date().getTime());
			mailService.updateIncludesVersion(updateEntity);
		}
	}

	/**
	 * 返信可能かどうかチェック
	 * @param property スカウトメールプロパティ
	 */
	public void isPossibleReturnMail(ScoutMailProperty property) {

		// 顧客がメールの使用が許可されているかどうかチェック
		if (!customerService.isScoutUse(property.customerId)) {
			throw new ActionMessagesException("errors.app.impossibleMailUse");
		}

		TMail tMail = null;
		try {
			// 返信元のメール情報を取得
			tMail = mailService.getMailDataToCustomer(property.mailId, property.customerId, MTypeConstants.MailKbn.SCOUT);

		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.unableReturnNotExistMail");
		}

		try {
			// 返信対象の会員が存在しているか、スカウトブロックに指定していないかチェック
			memberService.findById(tMail.fromId);

			if (!scoutBlockService.isSetScoutBlock(tMail.fromId, property.customerId)) {
				throw new ActionMessagesException("errors.app.unableSendScoutMailBlock",
						String.valueOf(tMail.fromId));
			}
		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.unableSendScoutMailNoMember",
					String.valueOf(tMail.fromId));
		}
	}

	/**
	 * スカウトメールログIDが正しいかどうか
	 * @param mailId メールID
	 * @param scoutMailLogId スカウトメールログID
	 */
	public boolean isScoutMailLogIdCorrect(int mailId, int scoutMailLogId, int memberId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TMail.ID), mailId);
		where.eq(WztStringUtil.toCamelCase(TMail.SCOUT_MAIL_LOG_ID) , scoutMailLogId);
		where.eq(WztStringUtil.toCamelCase(TMail.FROM_ID), memberId);
		where.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), MTypeConstants.MailKbn.SCOUT);
		where.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE);
		where.eq(WztStringUtil.toCamelCase(TMail.SENDER_KBN), MTypeConstants.SenderKbn.MEMBER);
		return jdbcManager.from(TMail.class).where(where).getCount() == 1l;
	}

	/**
	 * スカウトメールが送信可能かどうかチェック
	 * @param property スカウトメールプロパティ
	 * @return 送信可能の場合trueを返す
	 */
	public boolean isPossibleScout(ScoutMailProperty property) {

		// エラー情報
		ActionMessages errors = new ActionMessages();


		// スカウトメール数が残っているかチェック
		if (activeScoutMailService.getUsableScoutMailCount(property.customerId) < property.memberIdList.size()) {
			throw new ActionMessagesException("errors.app.noRemailScoutCount");
		}

		// 顧客がメールの使用が許可されているかどうかチェック
		if (!customerService.isScoutUse(property.customerId)) {
			throw new ActionMessagesException("errors.app.impossibleMailUse");
		}

		// 掲載中のWEBデータが存在するかチェック
		if (!releaseWebService.isPsotCustomerExists(property.customerId)) {
			throw new ActionMessagesException("errors.app.notPostWebdata");
		}

		List<String> noMemberList = new ArrayList<String>();
		List<String> notReceiveScoutMemberList = new ArrayList<String>();
		for (int memberId : property.memberIdList) {
			try {
				// 返信対象の会員が存在しているか、スカウトブロックに指定していないかチェック
				MMember member = memberService.findById(memberId);

				if (!Integer.valueOf(MTypeConstants.ScoutMailReceptionFlg.RECEPTION).equals(member.scoutMailReceptionFlg)
						|| !scoutBlockService.isSetScoutBlock(memberId, property.customerId)) {
					notReceiveScoutMemberList.add(String.valueOf(memberId));
					throw new ActionMessagesException("errors.app.unableSendScoutMailBlock",
							String.valueOf(memberId));
				}
			} catch (SNoResultException e) {
				noMemberList.add(String.valueOf(memberId));
				throw new ActionMessagesException("errors.app.unableSendScoutMailNoMember",
						String.valueOf(memberId));
			}
		}

		if (CollectionUtils.isNotEmpty(noMemberList)) {
			errors.add("errors", new ActionMessage("errors.app.unableSendScoutMailBlock",
					GourmetCareeUtil.createKanmaSpaceStr(noMemberList)));
		}

		if (CollectionUtils.isNotEmpty(notReceiveScoutMemberList)) {
			errors.add("errors", new ActionMessage("errors.app.unableSendScoutMailNoMember",
					GourmetCareeUtil.createKanmaSpaceStr(notReceiveScoutMemberList)));
		}

		return errors.isEmpty() ? true : false;
	}

	/**
	 * スカウトメール送信処理
	 * @param property スカウトメールプロパティ
	 */
	public void doSendScoutMail(ScoutMailProperty property) {

		// 顧客情報を取得
		MCustomer customer = getCustomerData(property.customerId);

		// 会員情報を取得
		List<MMember> memberList = getMemberList(property.memberIdList);

		// スカウトメール数を更新
		Map<Integer, Integer> scoutManageCountMap = updateScoutCount(property.customerId, property.memberIdList.size());

		// スカウト履歴テーブル・メールテーブル登録
		registScoutMailData(property, customer, memberList, scoutManageCountMap);

		// 求職者へメールを送信
		doSendMailReceiveInfoToMember(memberList, customer, property.sendKbn);

		// 顧客へメールを送信
		doSendReturnMailInfoToCustomer(memberList, customer, property.sendKbn);

		// 管理者へメールを送信
		sendScoutInfoToAdmin(memberList, customer);

	}

	/**
	 * スカウトメール返信処理
	 * @param property スカウトメールプロパティ
	 */
	public void doReturnMail(ScoutMailProperty property) {

		// 顧客情報を取得
		MCustomer customer = getCustomerData(property.customerId);

		// 会員情報を取得
		List<MMember> memberList = getMemberList(property.memberIdList);

		// メールデータを登録
		registReturnMailData(property, customer, memberList);

		// 返信元メールデータを更新(返信済みでない場合)
		if (!String.valueOf(MTypeConstants.MailStatus.REPLIED).equals(property.mailStatus)) {
			doChangeStatus(property);
		}

		// 求職者へメールを送信
		doSendMailReceiveInfoToMember(memberList, customer, property.sendKbn);

		// 顧客へメールを送信
		doSendReturnMailInfoToCustomer(memberList, customer, property.sendKbn);
	}


	/**
	 * 顧客情報を取得
	 * @param customerId 顧客ID
	 * @return 顧客エンティティ
	 */
	private MCustomer getCustomerData(int customerId) {

		try {
			MCustomer customer = customerService.findById(customerId);

			return customer;
		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.impossibleMailUse");
		}
	}

	/**
	 * 会員情報を取得
	 * @param memberIdList 会員IDリスト
	 * @return 会員エンティティリスト
	 */
	private List<MMember> getMemberList(List<Integer> memberIdList) {

		List<String> failIdList = new ArrayList<String>();
		List<MMember> memberList = new ArrayList<MMember>();
		for (int memberId : memberIdList) {
			try {
				MMember member = memberService.findById(memberId);
				memberList.add(member);
			} catch (SNoResultException e) {
				failIdList.add(String.valueOf(memberId));
			}
		}

		if (CollectionUtils.isNotEmpty(failIdList)) {
			throw new ActionMessagesException("errors.app.unableSendScoutMailNoMember",
					GourmetCareeUtil.createKanmaSpaceStr(failIdList));
		}

		return memberList;
	}

	/**
	 * スカウトメール数を更新
	 * @param CustomerId 顧客ID
	 * @param sendCount スカウトメール送信数
	 * @return
	 */
	private Map<Integer, Integer> updateScoutCount(int customerId, int sendCount) {

		List<TScoutMailManage> manageList = new ArrayList<TScoutMailManage>();
		List<VActiveScoutMail> activeScoutMailList;

		// 管理とログのIDマップ。 キーに管理ID,バリューに送信スカウトメール数。
		Map<Integer, Integer> manageCountMap = new HashMap<Integer, Integer>();

		try {
			activeScoutMailList = jdbcManager.from(VActiveScoutMail.class)
									.where(new SimpleWhere()
										.eq(WztStringUtil.toCamelCase(
												VActiveScoutMail.CUSTOMER_ID),
												customerId))
									.orderBy(SqlUtils.asc(VActiveScoutMail.SCOUT_MAIL_KBN) + "," + SqlUtils.asc(VActiveScoutMail.USE_END_DATETIME))
									.disallowNoResult()
									.getResultList();
		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.noRemailScoutCount");
		}

		// 無制限分が存在すればそちらから使用数をカウントするようにする
		for (VActiveScoutMail activeScoutMail : activeScoutMailList) {
			if(activeScoutMail.scoutMailKbn == MTypeConstants.ScoutMailKbn.UNLIMITED && activeScoutMail.scoutRemainCount > 1000) {
				TScoutMailManage entity = new TScoutMailManage();
				Beans.copy(activeScoutMail, entity).excludes("scoutRemainCount").execute();
				entity.scoutRemainCount = activeScoutMail.scoutRemainCount - sendCount;
				manageCountMap.put(activeScoutMail.id, sendCount);
				manageList.add(entity);
				scoutMailManageService.updateBatch(manageList);
				return manageCountMap;
			}
		}

		// 無制限が無い場合の処理
		for (VActiveScoutMail activeScoutMail : activeScoutMailList) {
			TScoutMailManage entity = new TScoutMailManage();

			Beans.copy(activeScoutMail, entity).excludes("scoutRemainCount").execute();

			if (sendCount > activeScoutMail.scoutRemainCount) {
				sendCount -= activeScoutMail.scoutRemainCount;
				entity.scoutRemainCount = 0;
				manageCountMap.put(activeScoutMail.id, activeScoutMail.scoutRemainCount);
				manageList.add(entity);
			} else {
				entity.scoutRemainCount = activeScoutMail.scoutRemainCount - sendCount;
				manageCountMap.put(activeScoutMail.id, sendCount);
				if (entity.scoutRemainCount < 0) {
					throw new ActionMessagesException("errors.app.noRemailScoutCount");
				}
				manageList.add(entity);
				break;
			}
		}

		scoutMailManageService.updateBatch(manageList);

		return manageCountMap;
	}

	/**
	 * スカウトメール登録（送信）
	 * @param property スカウトメールプロパティ
	 * @param customer 顧客エンティティ
	 * @param memberList 会員エンティティリスト
	 */
	private void registScoutMailData(ScoutMailProperty property, MCustomer customer, List<MMember> memberList, Map<Integer, Integer> scoutManageCountMap) {

		List<Map<Integer, TMail>> mailList = new ArrayList<Map<Integer, TMail>>();								// メール登録用エンティティリスト
//		List<TScoutMailLog> scoutHistoryList = new ArrayList<TScoutMailLog>();		// スカウト履歴エンティティリスト

		// スカウト履歴登録データを生成
//		for (MMember member : memberList) {
//			createScoutHistoryRegistData(customer.id, member.id, scoutHistoryList);
//		}

//		// スカウト履歴テーブルへ登録
//		scoutHistoryService.insertBatch(scoutHistoryList);
//
//		// メールテーブル登録データを生成
//		for (TScoutHistory historyEntity : scoutHistoryList) {
//			for (MMember member : memberList) {
//				if (historyEntity.memberId.equals(member.id)) {
//					createSendMailRegistData(property, customer, member, historyEntity, mailList);
//				}
//			}
//		}


		Timestamp sendDatetime = new Timestamp(new Date().getTime());
		for (MMember member : memberList) {
			TScoutMailLog scoutMailLog = new TScoutMailLog();
			scoutMailLog.sendDatetime = sendDatetime;
			scoutMailLog.scoutMailLogKbn = MTypeConstants.ScoutMailLogKbn.USE;
			scoutMailLog.scoutManageId = getScoutMailManageIdFromMap(scoutManageCountMap);
			scoutMailLog.memberId = member.id;
			scoutMailLogService.insert(scoutMailLog);

			createSendMailRegistData(property, customer, member, scoutMailLog, mailList);
		}

		for (Iterator<Entry<Integer, Integer>> it = scoutManageCountMap.entrySet().iterator(); it.hasNext();) {
			if (!GourmetCareeUtil.eqInt(0, it.next().getValue())) {
				throw new FraudulentProcessException("スカウトメールの残数がおかしい");
			}
		}

		// メールデータを登録
//		mailService.insertBatch(mailList);
		insertScoutMail(mailList);
	}

	/**
	 * スカウトメール管理マップからIDを取得
	 * @param scoutManageCountMap
	 * @return
	 */
	private Integer getScoutMailManageIdFromMap(Map<Integer, Integer> scoutManageCountMap) {
		for (Iterator<Entry<Integer, Integer>> it = scoutManageCountMap.entrySet().iterator(); it.hasNext();) {
			Entry<Integer, Integer> entry = it.next();
			Integer count = entry.getValue();

			if (count > 0) {
				entry.setValue(count - 1);
				return entry.getKey();
			}
		}

		throw new FraudulentProcessException("スカウトメール管理テーブルの使用数と、スカウトメール送信数が合いません。");
	}

	/**
	 * スカウトメール登録（返信）
	 * @param property スカウトメールプロパティ
	 * @param customer 顧客エンティティ
	 * @param memberList 会員エンティティリスト
	 */
	private void registReturnMailData(ScoutMailProperty property, MCustomer customer, List<MMember> memberList) {

		// 登録データを生成
		List<Map<Integer, TMail>> mailList = new ArrayList<Map<Integer, TMail>>();
		for (MMember member : memberList) {
			createReturnMailRegistData(property, customer, member, mailList, new Timestamp(new Date().getTime()));
		}
		// 登録
//		mailService.insertBatch(mailList);

		insertScoutMail(mailList);
	}

//	/**
//	 * スカウトメール履歴登録データ生成
//	 * @param customerId 顧客ID
//	 * @param memberId 会員ID
//	 * @param scoutHistoryList スカウト履歴エンティティリスト
//	 */
//	private void createScoutHistoryRegistData(int customerId, int memberId, List<TScoutMailLog> scoutHistoryList) {
//
//		TScoutHistory entity = new TScoutHistory();
//		entity.customerId = customerId;
//		entity.memberId = memberId;
//		entity.sendDatetime = new Timestamp(new Date().getTime());
//		entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
////		scoutHistoryList.add(entity);
//	}

	/**
	 * スカウトメール登録データ生成
	 * @param property スカウトメールプロパティ
	 * @param customer 顧客エンティティ
	 * @param member 会員エンティティ
	 * @param mailList 登録用メールリスト
	 * @param sendTime 送信日時
	 */
	private void createSendMailRegistData(ScoutMailProperty property, MCustomer customer, MMember member, TScoutMailLog mailLog, List<Map<Integer, TMail>> mailList) {

		Map<Integer, TMail> map = createMailCommonRegistData(property, customer, member, mailLog.sendDatetime);

		for (Iterator<Entry<Integer, TMail>> it = map.entrySet().iterator(); it.hasNext();) {
			it.next().getValue().scoutMailLogId = mailLog.id;
		}

		mailList.add(map);



//		for (TMail mail : list) {
//			mail.scoutHistoryId = scoutHistory.id;
//			mailList.add(mail);
//		}
	}

	/**
	 * 返信メール登録データ生成
	 * @param property スカウトメールプロパティ
	 * @param customer 顧客エンティティ
	 * @param member 会員エンティティ
	 * @param mailList 登録用メールリスト
	 * @param sendTime 送信日時
	 */
	private void createReturnMailRegistData(ScoutMailProperty property, MCustomer customer, MMember member, List<Map<Integer, TMail>> mailList, Timestamp sendTime) {

		Map<Integer, TMail> map = createMailCommonRegistData(property, customer, member, sendTime);

		for (Iterator<Entry<Integer, TMail>> it = map.entrySet().iterator(); it.hasNext();) {
			it.next().getValue().parentMailId = property.mailId;
		}

		mailList.add(map);

//		for (TMail mail : list) {
//			mail.parentMailId = property.mailId;
//			mailList.add(mail);
//		}
	}

	/**
	 * スカウトメール登録データを返す
	 * @param property スカウトメールプロパティ
	 * @param customer 顧客エンティティ
	 * @param memberList 会員エンティティリスト
	 * @return メールエンティティリスト
	 */
	private Map<Integer, TMail> createMailCommonRegistData(ScoutMailProperty property, MCustomer customer, MMember member, Timestamp sendTime) {

		Map<Integer, TMail> map = new HashMap<Integer, TMail>();

		String body;

		Matcher matcher = Pattern.compile(PatternReplace.MEMBER_NAME).matcher(StringUtils.defaultString(property.body));

		if (matcher.find()) {
			body = matcher.replaceAll(String.valueOf(member.id));
		} else {
			body = property.body;
		}

		TMail sendMail = new TMail();
		sendMail.mailKbn = MTypeConstants.MailKbn.SCOUT;
		sendMail.sendKbn = MTypeConstants.SendKbn.SEND;
		sendMail.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		sendMail.fromId = customer.id;
		sendMail.fromName = customer.customerName;
		sendMail.toId = member.id;
		sendMail.toName = member.memberName;
		sendMail.subject = property.subject;
		sendMail.body = body;
		sendMail.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		sendMail.sendDatetime = sendTime;
		sendMail.accessCd = UUID.create();
		sendMail.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		sendMail.scoutMailLogId = property.scoutMailLogId;
		map.put(MTypeConstants.SendKbn.SEND, sendMail);

		TMail receiveMail = new TMail();
		Beans.copy(sendMail, receiveMail).excludes("sendKbn").execute();
		receiveMail.sendKbn = MTypeConstants.SendKbn.RECEIVE;
		receiveMail.scoutReceiveKbn = ScoutReceiveKbn.UNSELECTED;
		receiveMail.accessCd = UUID.create();
		receiveMail.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		map.put(MTypeConstants.SendKbn.RECEIVE, receiveMail);

		return map;
	}

	/**
	 * 返信元のメールステータスを返信済みへ変更
	 * @param property
	 */
	private void doChangeStatus(ScoutMailProperty property) {

		TMail tMail = new TMail();
		tMail.id = property.mailId;
		tMail.mailStatus = MTypeConstants.MailStatus.REPLIED;
		mailService.updateIncludesVersion(tMail);
	}

	/**
	 * 求職者へメール受信のお知らせを送信
	 * XXX スカウトメールの送信・返信どちらもこのメソッドでやっている。
	 * @param memberList 会員リスト
	 * @param customer 顧客エンティティ
	 */
	private void doSendMailReceiveInfoToMember(List<MMember> memberList, MCustomer customer, int sendKbn) {

		// 送信データを生成
		List<ScoutMailReceiveInfoToMemberMaiDto> maiDtoList = new ArrayList<ScoutMailReceiveInfoToMemberMaiDto>();
		createReturnInfoMailData(memberList, maiDtoList, customer);

		// 送信
		if (GourmetCareeConstants.SEND_KBN_SEND.equals(String.valueOf(sendKbn))) {
			sendScoutReceiveInfo(maiDtoList);

		} else if (GourmetCareeConstants.SEND_KBN_RETURN.equals(String.valueOf(sendKbn))) {
			sendReturnInfo(maiDtoList);
		}
	}

	/**
	 * メール受信のお知らせを送信する宛先のリストを生成
	 * @param memberList 会員エンティティリスト
	 * @param maiDtoList メール受信お知らせDTOリスト
	 * @param customer 顧客エンティティ
	 * @return
	 */
	private List<ScoutMailReceiveInfoToMemberMaiDto> createReturnInfoMailData(List<MMember> memberList,
			List<ScoutMailReceiveInfoToMemberMaiDto> maiDtoList,
			MCustomer customer) {



		for (MMember member : memberList) {

			Integer areaCd = member.areaCd == null ? getAreaCd() : member.areaCd;

			if (StringUtils.isNotEmpty(member.loginId)) {
				ScoutMailReceiveInfoToMemberMaiDto maiDto = new ScoutMailReceiveInfoToMemberMaiDto();
				maiDto.addTo(member.loginId);
				try {
					maiDto.setFrom(getScoutAddress(), getScoutName());
				// アドレスがセットできない場合は、初期値で送信
				} catch (UnsupportedEncodingException e) {
					// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
					log.fatal("メール受信のお知らせPCメールに送信時、送信元がセットできませんでした。" + e);
				}
				maiDto.setCustomerName(customer.customerName);
				maiDto.setLoginUrl(getMemberPCLoginURL(areaCd));
				maiDto.setLoginUrlForSmart(getMemberMobileLoginURL(areaCd));
				maiDto.setInfoAddress(getInfoAddress(areaCd));
				maiDto.setSiteURL(getSiteUrl(areaCd));
				maiDto.setMobileMailFlg(false);

				maiDtoList.add(maiDto);
			}
			if (StringUtils.isNotEmpty(member.subMailAddress)) {
				ScoutMailReceiveInfoToMemberMaiDto maiDto = new ScoutMailReceiveInfoToMemberMaiDto();
				maiDto.addTo(member.subMailAddress);
				try {
					maiDto.setFrom(getScoutAddress(), getScoutName());
				// アドレスがセットできない場合は、初期値で送信
				} catch (UnsupportedEncodingException e) {
					// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
					log.fatal("メール受信のお知らせモバイルメールに送信時、送信元がセットできませんでした。" + e);
				}
				maiDto.setCustomerName(customer.customerName);
				maiDto.setLoginUrl(getMemberMobileLoginURL(areaCd));
				maiDto.setLoginUrlForSmart(getMemberPCLoginURL(areaCd));
				maiDto.setInfoAddress(getInfoAddress(areaCd));
				maiDto.setSiteURL(getSiteUrl(areaCd));
				maiDto.setMobileMailFlg(false);

				maiDtoList.add(maiDto);
			}
		}

		return maiDtoList;
	}

	/**
	 * 求職者へスカウトメール受信のお知らせを送信
	 * @param maiDtoList
	 */
	private void sendScoutReceiveInfo(List<ScoutMailReceiveInfoToMemberMaiDto> maiDtoList) {

		for (ScoutMailReceiveInfoToMemberMaiDto maiDto : maiDtoList) {
			gourmetCareeMail.sendScoutInfoToMember(maiDto);
		}
	}

	/**
	 * 求職者へ返信メール受信のお知らせを送信
	 * @param maiDtoList
	 */
	private void sendReturnInfo(List<ScoutMailReceiveInfoToMemberMaiDto> maiDtoList) {

		for (ScoutMailReceiveInfoToMemberMaiDto maiDto : maiDtoList) {
			gourmetCareeMail.sendScoutReturnInfoToMember(maiDto);
		}
	}

	/**
	 * メール返信(スカウト)のお知らせを顧客へ送信
	 * @param memberList 会員リスト
	 * @param customer 顧客エンティティ
	 */
	private void doSendReturnMailInfoToCustomer(List<MMember> memberList, MCustomer customer, int sendKbn) {

		// 送信データを生成
		List<ScoutMailSendInfoToCustomerMaiDto> maiDtoList = new ArrayList<ScoutMailSendInfoToCustomerMaiDto>();
		createScoutMailSendInfoData(maiDtoList, customer, memberList);

		if (GourmetCareeConstants.SEND_KBN_SEND.equals(String.valueOf(sendKbn))) {
			sendScoutInfoToCustomer(maiDtoList);

		} else if (GourmetCareeConstants.SEND_KBN_RETURN.equals(String.valueOf(sendKbn))) {
			// 送信
			sendReturnInfoToCustomer(maiDtoList);
		}
	}

	/**
	 * メール返信(スカウト）のお知らせを顧客へ送信するデータを生成
	 * @param maiDtoList 返信メールお知らせDTOリスト
	 * @param customer 顧客エンティティ
	 * @param memberList 会員エンティティリスト
	 * @return
	 */
	private List<ScoutMailSendInfoToCustomerMaiDto> createScoutMailSendInfoData(List<ScoutMailSendInfoToCustomerMaiDto> maiDtoList,
			MCustomer customer, List<MMember> memberList) {

		ScoutMailSendInfoToCustomerMaiDto maiDto = new ScoutMailSendInfoToCustomerMaiDto();
		maiDto.addTo(customer.mainMail);
		try {
			maiDto.setFrom(getScoutAddress(), getScoutName());
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("メール返信(スカウト）のお知らせを顧客のメインメールへ送信時、送信元がセットできませんでした。" + e);
		}
		maiDto.setMemberId(createToMemberIdStr(memberList));
		maiDto.setInfoAddress(getInfoAddress(getAreaCd()));
		maiDto.setSiteURL(getSiteUrl(getAreaCd()));
		maiDto.setCustomerName(customer.customerName);
		maiDtoList.add(maiDto);

		List<String> subMailList = customerSubMailService.getReceptionSubMail(customer.id);
		for (String subMail : subMailList) {
			ScoutMailSendInfoToCustomerMaiDto subMaiDto = new ScoutMailSendInfoToCustomerMaiDto();
			subMaiDto.addTo(subMail);
			try {
				subMaiDto.setFrom(getScoutAddress(), getScoutName());
				// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("メール返信(スカウト）のお知らせを顧客のサブメールへ送信時、送信元がセットできませんでした。" + e);
			}
			subMaiDto.setMemberId(createToMemberIdStr(memberList));
			subMaiDto.setInfoAddress(getInfoAddress(getAreaCd()));
			subMaiDto.setSiteURL(getSiteUrl(getAreaCd()));
			subMaiDto.setCustomerName(customer.customerName);
			maiDtoList.add(subMaiDto);
		}

		return maiDtoList;
	}

	/**
	 * スカウトメール送信のお知らせを顧客へ送信
	 * @param maiDtoList メールDTOリスト
	 */
	private void sendScoutInfoToCustomer(List<ScoutMailSendInfoToCustomerMaiDto> maiDtoList) {

		for (ScoutMailSendInfoToCustomerMaiDto maiDto : maiDtoList) {
			gourmetCareeMail.sendScoutInfoToCustomer(maiDto);
		}
	}

	/**
	 * 返信メール(スカウト)送信のお知らせを顧客へ送信
	 * @param maiDtoList メールDTOリスト
	 */
	private void sendReturnInfoToCustomer(List<ScoutMailSendInfoToCustomerMaiDto> maiDtoList) {

		for (ScoutMailSendInfoToCustomerMaiDto maiDto : maiDtoList) {
			gourmetCareeMail.sendScoutReturnInfoToCustomer(maiDto);
		}
	}

	/**
	 * スカウトメール送信のお知らせを管理者へ送信
	 * @param memberList 会員エンティティリスト
	 * @param customer 顧客エンティティ
	 */
	private void sendScoutInfoToAdmin(List<MMember> memberList, MCustomer customer) {

		// 送信データを生成
		ScoutMailSendInfoToAdminMaiDto maiDto = createScoutSendInfoToAdminData(memberList, customer);

		// 送信
		gourmetCareeMail.sendScoutInfoToAdmin(maiDto);
	}

	/**
	 * 管理者へ送信するスカウトメール送信のお知らせデータを生成
	 * @param memberList 会員エンティティリスト
	 * @param customer 顧客エンティティ
	 */
	private ScoutMailSendInfoToAdminMaiDto createScoutSendInfoToAdminData(List<MMember> memberList, MCustomer customer) {

		ScoutMailSendInfoToAdminMaiDto maiDto = new ScoutMailSendInfoToAdminMaiDto();
		maiDto.addTo(getScoutAddress());
		try {
			maiDto.setFrom(getScoutAddress(), getScoutName());
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("管理者へ送信するスカウトメール送信時、送信元がセットできませんでした。" + e);
		}
		maiDto.setCustomerId(String.valueOf(customer.id));
		maiDto.setCustomerName(customer.customerName);
		maiDto.setMemberId(createToMemberIdStr(memberList));

		return maiDto;
	}

	/**
	 * 掲載中のWEBデータの公開ページ表示URLを返す
	 * @param customerId 顧客ID
	 * @return WEBデータ表示URL文字列
	 * @throws WNoResultException
	 */
	public String createAddUrlStr(int customerId, String sortKey, boolean exportFlg) throws WNoResultException {

		StringBuilder sb = new StringBuilder();

		//書き出しの空行を入れるかどうか
		if(exportFlg) {
			sb.append(GourmetCareeConstants.RN_CD);
			sb.append(GourmetCareeConstants.RN_CD);
			sb.append(GourmetCareeConstants.RN_CD);
			sb.append(GourmetCareeConstants.RN_CD);
		}

		sb.append(getCommonProperty("gc.addUrl.header"));
		sb.append(GourmetCareeConstants.RN_CD);
		sb.append(GourmetCareeConstants.RN_CD);

		try {
			// 掲載中のWEBデータ
			List<VWebList> webList = webListService.getVWebListByCustomerIdStatus(customerId, MStatusConstants.DisplayStatusCd.POST_DURING, sortKey);

			String path = getCommonProperty("gc.shop.addUlr.detailPath");
			Map<Integer, String> areaMap = areaService.getAllLinkNameMap();

			for (VWebList entity : webList) {
				sb.append(entity.manuscriptName);
				sb.append(GourmetCareeConstants.RN_CD);
				sb.append(String.format(path, areaMap.get(entity.areaCd), entity.customerId, entity.id));
				sb.append(GourmetCareeConstants.RN_CD);
				sb.append(GourmetCareeConstants.RN_CD);
			}
		} catch(WNoResultException e) {
			throw new WNoResultException();

		}

		return sb.toString();
	}

	/**
	 * スカウトメール送信通知用会員ID連結文字列を生成
	 * @param memberList 会員リスト
	 * @return 会員ID連結文字列
	 */
	private String createToMemberIdStr(List<MMember> memberList) {

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (MMember member : memberList) {
			if (i == 0) {
				sb.append("ID:");
				sb.append(member.id);
				sb.append("様");
				i++;
			} else {
				sb.append("、");
				if ((i % 3) == 0) {
					sb.append(GourmetCareeConstants.RN_CD);
				}
				sb.append("ID:");
				sb.append(member.id);
				sb.append("様");
				i++;
			}
		}

		return sb.toString();
	}

	/**
	 * スカウトメールのインサート
	 * @param mailList
	 */
	private void insertScoutMail(List<Map<Integer, TMail>> mailList) {
		for (Map<Integer, TMail> map : mailList) {
			TMail toApplicantEntity = map.get(MTypeConstants.SendKbn.RECEIVE);
			mailService.insert(toApplicantEntity);

			TMail toCustomerEntity = map.get(MTypeConstants.SendKbn.SEND);
			toCustomerEntity.receiveId = toApplicantEntity.id;
			mailService.insert(toCustomerEntity);
		}
	}

//	/**
//	 * スカウト会員情報リスト表示データを生成
//	 * @param property スカウトメールプロパティ
//	 * @return スカウト会員情報リスト
//	 * @author Motoaki Hara
//	 */
//	public List<ScoutMemberInfoDto> getScoutMemberInfoList(ScoutMailProperty property) throws WNoResultException {
//
//		List<ScoutMemberInfoDto> scoutMemberInfoDtoList = new ArrayList<ScoutMemberInfoDto>();
//		StringBuilder sb = new StringBuilder(0);
//		List<Object> params = new ArrayList<Object>();
//
//		createScoutMemberListSql(sb, params, property.customerId);
//
//		List<ScoutMailListDto> retDtoList = new ArrayList<ScoutMailListDto>();
//		try {
//			retDtoList = jdbcManager.selectBySql(ScoutMailListDto.class, sb.toString(), params.toArray()).disallowNoResult().getResultList();
//		} catch (NoResultException e) {
//			throw new WNoResultException();
//		}
//
//		for (ScoutMailListDto dto : retDtoList) {
//			ScoutMemberInfoDto scoutMemberInfoDto = new ScoutMemberInfoDto();
//
//			// TODO メールの返信状況の取得を追加する。
//
//			//Date型の変換は後続処理で行う。
//			Beans.copy(dto, scoutMemberInfoDto).execute();
//			scoutMemberInfoDto.sendDatetime = DateUtils.getDateStr(dto.sendDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
//			scoutMemberInfoDto.memberDetailPath = GourmetCareeUtil.makePath("/member/detail/", "index", String.valueOf(dto.memberId));
//			scoutMemberInfoDto.mailLogId = dto.id;
//
//			try {
//				// 会員情報のセット
//				setMemberInfo(scoutMemberInfoDto);
//
//				// 会員属性情報のセット
//				setMemberAttrInfo(scoutMemberInfoDto);
//
//				// 会員状況の取得
//				MemberStatusDto statusDto = memberService.getMemberStatusDto(scoutMemberInfoDto.memberId, property.customerId);
//
//				// コピーする
//				Beans.copy(statusDto, scoutMemberInfoDto).execute();
//
//			} catch (SNoResultException e) {
//				// 処理なし
//			} catch (WNoResultException e) {
//				// 状況が取得できない場合、処理をしない
//			}
//
//			scoutMemberInfoDtoList.add(scoutMemberInfoDto);
//		}
//
//		return scoutMemberInfoDtoList;
//	}

//	/**
//	 * スカウト会員一覧検索SQLを作成します
//	 * @param sb
//	 * @param params
//	 * @param customerId 顧客ID
//	 * @author Motoaki Hara
//	 */
//	private void createScoutMemberListSql(StringBuilder sb, List<Object> params, int customerId) {
//
//		sb.append(" SELECT ");
//		sb.append("         SML.id ");
//		sb.append("         ,SML.member_id ");
//		sb.append("         ,SML.send_datetime ");
//		sb.append("                 FROM ");
//		sb.append("                     t_scout_mail_log SML ");
//		sb.append("                 WHERE ");
//		sb.append("                     SML.scout_mail_log_kbn = ? ");
//		sb.append("                     AND SML.delete_flg = ? ");
//
//		params.add(MTypeConstants.ScoutMailLogKbn.USE);
//		params.add(DeleteFlgKbn.NOT_DELETED);
//
//		sb.append("                     AND EXISTS ( ");
//		sb.append("                         SELECT ");
//		sb.append("                                 * ");
//		sb.append("                             FROM ");
//		sb.append("                                 t_scout_mail_manage SMM ");
//		sb.append("                             WHERE ");
//		sb.append("                                 SMM.id = SML.scout_manage_id ");
//		sb.append("                                 AND SMM.customer_id = ? "); // 顧客ID
//		sb.append("                                 AND SMM.delete_flg = ? ); ");
//
//		params.add(customerId);
//		params.add(DeleteFlgKbn.NOT_DELETED);
//	}

//	/**
//	 * 会員情報を取得して値をセットします
//	 * @param scoutMemberInfoDto
//	 */
//	private void setMemberInfo (ScoutMemberInfoDto scoutMemberInfoDto) {
//		// 会員情報の取得
//		MMember member = memberService.findById(scoutMemberInfoDto.memberId);
//
//		scoutMemberInfoDto.sexKbn = member.sexKbn;
//		scoutMemberInfoDto.age    = GourmetCareeUtil.convertToAge(member.birthday);
//		scoutMemberInfoDto.prefecturesCd = String.valueOf(member.prefecturesCd);
//		scoutMemberInfoDto.municipality = member.municipality;
//		scoutMemberInfoDto.address = member.address;
//		scoutMemberInfoDto.employPtnKbn = String.valueOf(member.employPtnKbn);
//	}
//
//	/**
//	 * 会員属性情報を取得して値をセットします
//	 * @param scoutMemberInfoDto
//	 */
//	private void setMemberAttrInfo (ScoutMemberInfoDto scoutMemberInfoDto) {
//		List<MMemberAttribute> memberAttrList = new ArrayList<MMemberAttribute>();
//		memberAttrList = memberAttributeService.getMemberAttributeList(scoutMemberInfoDto.memberId);
//
//		List<String> industryList = new ArrayList<String>();
//		List<String> jobList = new ArrayList<String>();
//		for (MMemberAttribute attrEntity : memberAttrList) {
//			if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
//				industryList.add(String.valueOf(attrEntity.attributeValue));
//			} else if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
//				jobList.add(String.valueOf(attrEntity.attributeValue));
//			}
//		}
//
//		scoutMemberInfoDto.industry = industryList.toArray(new String[0]);
//		scoutMemberInfoDto.job = jobList.toArray(new String[0]);
//	}



	/**
	 * スカウトメール検索プロパティ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ScoutmailSearchProperty extends PagerProperty {

		/**
		 *
		 */
		private static final long serialVersionUID = 2686622191010778499L;

		/** ページナビ */
		public PageNavigateHelper pageNavi;

		/** スカウト者ID */
		public Integer scountId;

	}



	public static class ScoutMailTargetDto extends BaseDto {

		/**
		 *
		 */
		private static final long serialVersionUID = -9074912652646637392L;

		/** 会員のコピーするプロパティ */
		private static final String[] MEMBER_COPY_PROPERTIES = {"prefecturesCd", "municipality", "employPtnKbn",
				"birthday", "sexKbn","foodExpKbn", "expManagerKbn", "expManagerYearKbn", "expManagerPersonsKbn", "foreignWorkFlg", "scoutSelfPr"
				,"transferFlg", "midnightShiftFlg", "salaryKbn"};

		public Integer id;

		public Integer memberId;

		/** 送信日時 */
	    public Timestamp sendDatetime;

		/** 都道府県コード */
		public Integer prefecturesCd;

		/** 市区町村 */
		public String municipality;

		/** 生年月日 */
		public Date birthday;

		/** 年齢 */
		public Integer age;

		/** 性別区分 */
		public Integer sexKbn;

		  /** メモ */
	    public String memo;

	    /** 選考フラグ */
	    public Integer selectionFlg;

	    /** 雇用形態区分リスト */
		public List<String> employPtnKbnList;

		/** 希望業態 */
		public List<String> industryKbnList;

		/** 希望職種 */
		public List<String> jobKbnList;

		/** 希望勤務地（prefectures_cd,市区町村名） */
		public Map<Integer, List<String>> hopeCityMap;

		/** 会員のステータスDTO */
		public MemberStatusDto memberStatusDto;

		/** 未読メールがあるかどうかのフラグ */
		public boolean unopenedMailFlg = true;

		/** チェックされたフラグ */
		public boolean checkedFlg;

		/** スカウト受取区分 */
		public Integer scoutReceiveKbn;

		/** 飲食業界経験区分 */
		public Integer foodExpKbn;

		public Integer expManagerKbn;

		public Integer expManagerYearKbn;

		public Integer expManagerPersonsKbn;

		public List<Integer> qualificationKbnList;

		public String scoutSelfPr;

		public Integer foreignWorkFlg;

		public Integer transferFlg;

		public Integer midnightShiftFlg;

		public Integer salaryKbn;

		public String schoolName;

		public String department;

		public Integer graduationKbn;

		public boolean schoolHistoryExistFlg;

		public boolean exitMemberFlg;

		/**
		 * 年齢を取得します。
		 * @return 年齢
		 */
		public int getAge() {
			if (birthday == null) {
				return 0;
			}
			return GourmetCareeUtil.convertToAge(birthday);
		}

		/**
		 * 応募者が男性かどうか
		 * @return 男性の場合にtrue
		 */
		public boolean isApplicantMale() {
			return GourmetCareeUtil.eqInt(MTypeConstants.Sex.MALE, sexKbn);
		}

		/**
		 * 選考の色を取得
		 * @return
		 */
		public String getSelectionFlgColor() {
			if (selectionFlg == null) {
				return MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
			}
			if (MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP.containsKey(selectionFlg)) {
				return MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP.get(selectionFlg);
			}

			return MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
		}
	}




	/**
	 * 受信メールが存在するかどうか
	 * @param scoutMailLogId
	 * @return
	 */
	public boolean existReceiveMail(int scoutMailLogId) {

		long count = jdbcManager.from(TMail.class)
						.where(createReceiveScoutMailWhere(scoutMailLogId))
						.getCount();

		return count > 0l;
	}


	/**
	 * スカウトメールログに対応する最新の受信メールIDリストをStringリストで取得
	 * @param scoutMailLogIdCollection スカウトメールログIDのコレクション
	 * @return
	 * @throws WNoResultException
	 */
	public List<String> convertScoutMailLogIdToLatestReceivedMailIdStringList(Collection<Integer> scoutMailLogIdCollection) throws WNoResultException {
		if (CollectionUtils.isEmpty(scoutMailLogIdCollection)) {
			throw new WNoResultException("引数が空の為、検索をしていません。");
		}

		List<String> list = new ArrayList<String>(scoutMailLogIdCollection.size());
		String sort = SqlUtils.desc(TMail.SEND_DATETIME);
		for (Integer scoutMailLogId : scoutMailLogIdCollection) {

			TMail mail = jdbcManager.from(TMail.class)
							.where(createReceiveScoutMailWhere(scoutMailLogId))
							.limit(1)
							.orderBy(sort)
							.getSingleResult();

			if (mail == null) {
				throw new WNoResultException("検索結果がありません");
			}

			list.add(String.valueOf(mail.id));
		}
		return list;
	}

	/**
	 * 気になるメール返信時のスカウトメール残数を更新
	 *
	 * @param customerId 顧客ID
	 */
	public void updateScoutMailCountWhenInterested(int customerId) {
		List<TScoutMailManage> manageList = new ArrayList<TScoutMailManage>();
		List<VActiveScoutMail> activeScoutMailList = jdbcManager.from(VActiveScoutMail.class)
				.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(
							VActiveScoutMail.CUSTOMER_ID),
							customerId))
				.orderBy(SqlUtils.asc(VActiveScoutMail.SCOUT_MAIL_KBN) + "," + SqlUtils.asc(VActiveScoutMail.USE_END_DATETIME))
				.disallowNoResult()
				.getResultList();

		for (VActiveScoutMail activeScoutMail : activeScoutMailList) {
			TScoutMailManage entity = new TScoutMailManage();

			Beans.copy(activeScoutMail, entity).excludes("scoutRemainCount").execute();

			if (INTEREST_SCOUT_MAIL_USAGE_COUNT > activeScoutMail.scoutRemainCount) {
				INTEREST_SCOUT_MAIL_USAGE_COUNT -= activeScoutMail.scoutRemainCount;
				entity.scoutRemainCount = 0;
				manageList.add(entity);
			} else {
				entity.scoutRemainCount = activeScoutMail.scoutRemainCount - INTEREST_SCOUT_MAIL_USAGE_COUNT;
				if (entity.scoutRemainCount < 0) {
					throw new ActionMessagesException("errors.app.noRemailScoutCount");
				}
				manageList.add(entity);
				break;
			}
		}

		scoutMailManageService.updateBatch(manageList);
	}

	/**
	 * 未読の応募メールを既読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.SCOUT);
	}

	/**
	 * 既読の応募メールを未読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToUnOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToUnOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.SCOUT);
	}

	/**
	 * 未読のスカウトメール数を取得する
	 * @return
	 */
	public long getUnReadScoutMailCount() {
		return mailService.getUnReadScoutMailCount(getCustomerId());
	}

	/**
	 * 未読のスカウトメールがあるか判定する
	 * @return
	 */
	public boolean checkUnReadScoutMail() {
		return mailService.checkUnReadScoutMail(getCustomerId());
	}

	public void changeMailToUnDisplay(List<Integer> mailIds) {
		mailService.changeScoutMailToUnDisplay(mailIds, getCustomerId());
	}


	/**
	 *  スカウトメールログの受信メールを検索するWHEREを作成
	 * @param scoutMailLogId スカウトメールログID
	 * @return
	 */
	private Where createReceiveScoutMailWhere(int scoutMailLogId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TMail.SCOUT_MAIL_LOG_ID), scoutMailLogId);
		where.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE);
		where.eq(WztStringUtil.toCamelCase(TMail.SENDER_KBN), MTypeConstants.SenderKbn.MEMBER);
		where.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), MTypeConstants.MailKbn.SCOUT);
		where.eq(WztStringUtil.toCamelCase(TMail.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return where;
	}
}
