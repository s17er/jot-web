package com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMemberMailMag;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.TokenProcessor;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;

import com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember.AdvancedRegisterdMemberBaseAction;
import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.ListForm;
import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMemberMailMag.InputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.mai.MailMagazineTriggerMaiDto;
import com.gourmetcaree.admin.pc.sys.mai.GourmetcareeMai;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic.MailMagazineContentDto;
import com.gourmetcaree.admin.service.logic.MailMagazineLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * メルマガ入力アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class InputAction extends AdvancedRegisterdMemberBaseAction {

	/** ログ */
	private static final Logger log = Logger.getLogger(InputAction.class);


	/** アクションフォーム */
	@ActionForm
	@Resource
	private InputForm inputForm;

	/**
	 * リストフォーム<br />
	 * ※ アクションフォームではないので注意!!!
	 */
	@Resource
	private ListForm listForm;

	/** タイプサービス */
	@Resource
	private TypeService typeService;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** メルマガロジック */
	@Resource
	private MailMagazineLogic mailMagazineLogic;

	/** メール */
	@Resource
	private GourmetcareeMai gourmetcareeMai;

	/** メルマガID */
	private Integer mailMagazineId;


	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm")
	public String index() {
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	private String show() {

		// テキストエリアに初期値をセット
		Properties properties = ResourceUtil.getProperties("mailMagazine.properties");
		if (properties != null) {
			// PC版
			inputForm.pcBody = properties.getProperty("gc.mailMagazine.pcMemberHeader", "");
		}

		// 入力フォームを表示
		inputForm.setExistDataFlgOk();

		return TransitionConstants.AdvancedRegistration.JSP_APH03C01;
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, input = TransitionConstants.AdvancedRegistration.JSP_APH03C01)
//	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		//トークンを設定
        TokenProcessor.getInstance().saveToken(RequestUtil.getRequest());

        // 確認画面へ遷移
		return TransitionConstants.AdvancedRegistration.JSP_APH03C02;
	}


	/**
	 * 登録
	 * @return 完了メソッド
	 */
	@Execute(validator = false, input = TransitionConstants.AdvancedRegistration.JSP_APH03C01)
//	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		//トークン判定処理。2度目のリクエストが来た場合は完了メソッドへ遷移
		if (!TokenProcessor.getInstance().isTokenValid(RequestUtil.getRequest(), true)) {
			return TransitionConstants.AdvancedRegistration.REDIRECT_ADVANCEDMEMBERMAILMAG_INPUT_COMP;
		}

		// 登録処理の呼び出し
		int mailMagazineId = insert();
		log.debug("事前登録会員のメルマガの登録を行いました。");

		// メルマガ送信処理の呼び出し
		mailMagazineLogic.sendMailMagazineBackground(mailMagazineId);
		log.debug("事前登録会員のメルマガの送信を行いました。");

		// 完了メソッドへ遷移
		return TransitionConstants.AdvancedRegistration.REDIRECT_ADVANCEDMEMBERMAILMAG_INPUT_COMP;
	}

	/**
	 * 完了画面
	 * @return 完了画面JSP
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {
		return TransitionConstants.AdvancedRegistration.JSP_APH03C03;
	}

	/**
	 * 訂正
	 * @return 入力画面JSP
	 */
	@Execute(validator = false, input = TransitionConstants.AdvancedRegistration.JSP_APH03C01)
	public String correct() {
		inputForm.setProcessFlgNg();
		return TransitionConstants.AdvancedRegistration.JSP_APH03C01;
	}


	/**
	 * 検索リストへ戻る
	 * @return 検索リストへのリダイレクト
	 */
	@Execute(validator = false, removeActionForm = true)
	public String back() {
		return TransitionConstants.AdvancedRegistration.REDIRECT_ADVANCED_MEMBER_SEARCH_AGAIN;
	}



	/**
	 * 登録処理
	 * @return メルマガID
	 */
	private int insert() {

		MailMagazineContentDto dto = advancedRegistrationLogic.createMailMagazineContentDto();
		Beans.copy(inputForm, dto).execute();
		// メルマガ関連データを登録

		try {
			mailMagazineId = advancedRegistrationLogic.insertMailMagazine(createProperty(0, listForm), dto);
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.canNotTwoProcess",
					MessageResourcesUtil.getMessage("msg.deliverReject"),
					MessageResourcesUtil.getMessage("msg.delete"),
					MessageResourcesUtil.getMessage("labels.mailStopFlg"));
		}
		// 条件をログに出力
		log.info("事前登録会員向けメルマガ登録するForm情報：" + inputForm);

		return mailMagazineId;
	}

	/**
	 * メルマガ送信処理
	 * メールを送信することでJamesをキックします。
	 */
	private void sendMailMagazine() {

		MailMagazineTriggerMaiDto maiDto = new MailMagazineTriggerMaiDto();
		maiDto.setMailMagazineSuject(getCommonProperty("gc.mm.customer.prefix") + mailMagazineId);
		maiDto.addTo(getCommonProperty("gc.mm.trigger.address"));
		try {
			maiDto.setFrom(getCommonProperty("gc.mm.admin.address"), getCommonProperty("gc.mm.admin.Name"));
		// アドレスがセットできない場合、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("会員宛メルマガ送信処理時、送信元がセットできませんでした。" + e);
		}
		gourmetcareeMai.sendMailMagazineTriggerMail(maiDto);
	}


	/**
	 * 検索条件マップの取得
	 * @return 検索条件マップ
	 */
	public Map<String, String> getConditionMap() {
		Map<String, String> map = new LinkedHashMap<String, String>(0);

		// エリア
		if (StringUtils.isNotBlank(listForm.areaCd)) {
			map.put("エリア", valueToNameConvertLogic.convertToAreaName(new String[]{listForm.areaCd}));
		}

		// ID
		if (StringUtils.isNotBlank(listForm.id)) {
			map.put("ID", listForm.id);
		}

		// 都道府県
		if (StringUtils.isNotBlank(listForm.prefecturesCd)) {
			map.put("都道府県", valueToNameConvertLogic.convertToPrefecturesName(NumberUtils.toInt(listForm.prefecturesCd)));
		}

		// フリガナ
		if (StringUtils.isNotBlank(listForm.furigana)) {
			map.put("フリガナ", listForm.furigana);
		}

		// 氏名
		if (StringUtils.isNotBlank(listForm.name)) {
			map.put("氏名", listForm.name);
		}

		// 開催年
		if (!ArrayUtils.isEmpty(listForm.advancedRegistrationIdArray)) {
			map.put("開催年", valueToNameConvertLogic.convertToAdvancedRegistrationShortName(listForm.advancedRegistrationIdArray));
		}

		// 登録状況
		if (!ArrayUtils.isEmpty(listForm.statusKbnArray)) {
			map.put("登録状況", valueToNameConvertLogic.convertToTypeName(MTypeConstants.AdvancedRegistrationStatusKbn.TYPE_CD, listForm.statusKbnArray));
		}

		// メルマガ
		if (StringUtils.isNotBlank(listForm.advancedMailMagazineReceptionFlg)) {
			map.put("メルマガ", valueToNameConvertLogic.convertToTypeName(MTypeConstants.AdvancedMailMagReceptionFlg.TYPE_CD, new String[]{listForm.advancedMailMagazineReceptionFlg}));
		}

		// 性別
		if (StringUtils.isNotBlank(listForm.sexKbn)) {
			map.put("性別", valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, new String[]{listForm.sexKbn}));
		}

		// 年齢
		if (StringUtils.isNotBlank(listForm.maxAge) || StringUtils.isNotBlank(listForm.minAge)) {
			StringBuilder sb = new StringBuilder(0);
			if (StringUtils.isNotBlank(listForm.minAge)) {
				sb.append(listForm.minAge);
			}

			sb.append(" ～ ");

			if (StringUtils.isNotBlank(listForm.maxAge)) {
				sb.append(listForm.maxAge);
			}

			map.put("年齢", sb.toString());
		}


		// メールアドレス
		if (StringUtils.isNotBlank(listForm.mailAddress)) {
			map.put("メールアドレス", listForm.mailAddress);
		}

		// 端末
		if (StringUtils.isNotBlank(listForm.terminalKbn)) {
			map.put("端末", valueToNameConvertLogic.convertToTypeName(MTypeConstants.TerminalKbn.TYPE_CD, new String[]{listForm.terminalKbn}));
		}

		// 登録日時
		if (StringUtils.isNotBlank(listForm.registrationEndDate) || StringUtils.isNotBlank(listForm.registrationStartDate)) {
			StringBuilder sb = new StringBuilder(0);

			if (StringUtils.isNotBlank(listForm.registrationStartDate)) {
				sb.append(listForm.registrationStartDate);

				if (StringUtils.isNotBlank(listForm.registrationStartHour)) {
					sb.append(" ")
						.append(listForm.registrationStartHour);


					if (StringUtils.isNotBlank(listForm.registrationStartMinute)) {
						sb.append(" ： ")
							.append(listForm.registrationStartMinute);
					}
				}

			}

			sb.append(" ～ ");

			if (StringUtils.isNotBlank(listForm.registrationEndDate)) {
				sb.append(listForm.registrationEndDate);

				if (StringUtils.isNotBlank(listForm.registrationEndHour)) {
					sb.append(" ")
						.append(listForm.registrationEndHour);


					if (StringUtils.isNotBlank(listForm.registrationEndMinute)) {
						sb.append(" ： ")
							.append(listForm.registrationEndMinute);
					}
				}

			}

			map.put("登録日時", sb.toString());


		}

		return map;

	}


}
