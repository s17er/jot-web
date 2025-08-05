package com.gourmetcaree.admin.pc.juskill.form.juskillMember;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * ジャスキル会員基本のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class JuskillMemberForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6658404614447995706L;

	/** ID */
	public String id;

	/** バージョン */
	public String version;

	/** 会員ID */
	public String memberId;

	/** ジャスキル移行会員フラグ */
	public String juskillMigrationFlg;

	/** ジャスキル会員No */
	public String juskillMemberNo;

	/** ジャスキル登録日 */
	public String juskillEntryDate;

	/** メール希望 */
	public String mailHope;

	/** 転職時期 */
	public String transferTiming;

	/** ジャスキル会員名 */
	@Required
	public String juskillMemberName;

	/** 生年月日 */
	public String birthday;

	/** 生年月日（年） */
	public String birthYear;

	/** 生年月日（月） */
	public String birthMonth;

	/** 生年月日（日） */
	public String birthDate;

	/** 性別区分 */
	public String sexKbn;

	/** 希望業態 */
	public String hopeIndustry;

	/** 希望職種 */
	public String hopeJob;

	/** 経験 */
	public String experience;

	/** 希望年収 */
	public String hopeSalary;

	/** 郵便番号 */
	public String zipCd;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 住所1 */
	public String address;

	/** ビル名 */
	public String buildingName;

	/** 路線 */
	public String route;

	/** 最寄駅 */
	public String closestStation;

	/** 電話1 */
	public String phoneNo1;

	/** 電話2 */
	public String phoneNo2;

	/** 連絡先3（ＰＣメール） */
	public String pcMail;

	/** メール */
	public String mail;

	/** PW */
	public String pw;

	/** 最終学歴 */
	public String finalSchoolHistory;

	/** 学歴備考 */
	public String schoolHistoryNote;

	/** 最終職種 */
	public String finalCareerHistory;

	/** 希望職種2 */
	public String hopeJob2;

	/** 取得資格 */
	public String qualification;

	/** 登録経路 */
	public String entryPath;

	/** 特記事項 */
	public String notice;

	/** 素材を保持するマップ */
	@Binding(bindingType = BindingType.NONE)
	public Map<String, MaterialDto> materialMap = new HashMap<String, MaterialDto>();

	/** PDFファイル名 */
	public String pdfFileName;

	/** 履歴書ファイルへのパス */
	public String resumeFilePath;

	/** 年齢 */
	public String age;

	/** ジャスキル会員職歴エンティティリスト */
	public List<String> careerHistoryList;

	/** 健康状態区分 */
	public String healthKbn;

	/** 刑事罰訴訟区分 */
	public String sinKbn;

	/** 暴力団区分 */
	public String gangstersKbn;

	/** 履歴修正フラグ */
	public String historyModificationFlg;

	/** 入退社修正フラグ */
	public String onLeavingModificationFlg;

	/** 取得資格修正フラグ */
	public String qualificationModificationFlg;

	/** 素材のアップロード操作用パラメータ */
	public String hiddenMaterialKbn;

	/** 登録者情報確認用URL */
	public String previewUrl;

	/** 名前(名字のみ) */
	public String familyName;

	/** ヒアリング担当者 */
	public String hearingRep;

	/** 転職状況 */
	public String jobChangeStatus;

	/** パスワード */
	public String password;

	/** アップロード受渡PDF */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile pdfFile;

	/**
	 * ファイル出力用のキーを取得します。
	 * コピー画面以外でwebIdが存在すればwebId、それ以外の場合は文字列「INPUT」を取得します。
	 * @return
	 */
	public String getIdForDirName() {
		return (StringUtil.isNotBlank(id)) ? id : GourmetCareeConstants.IMG_FILEKEY_INPUT;
	}

	/**
	 * 素材保持用Mapの使用前処理
	 */
	public void initMaterialMap() {

		if (this.materialMap == null) {
			this.materialMap = new HashMap<String, MaterialDto>();
		}
	}

	/**
	 * 受渡用PDFを削除
	 */
	public void deletePdfFile() {
		if (pdfFile != null) {
			pdfFile.destroy();
			pdfFile = null;
		}
		pdfFileName = null;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @param materialKbn 素材区分
	 */
	public void deleteMaterial(String materialKbn) {

		// Mapの初期処理を行う
		initMaterialMap();

		// 選択された素材のMapを消す
		materialMap.remove(materialKbn);

		// FormFileを削除する
		if(MTypeConstants.JuskillMemberMaterialKbn.RESUME.equals(materialKbn)) {
			if (pdfFile != null) {
				pdfFile.destroy();
				this.pdfFile = null;
			}
		}

		pdfFileName = null;
	}
}