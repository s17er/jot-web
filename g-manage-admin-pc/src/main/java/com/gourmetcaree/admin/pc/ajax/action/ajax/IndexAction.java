package com.gourmetcaree.admin.pc.ajax.action.ajax;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.ajax.form.ajax.IndexForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * Ajaxのアクション
 *
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
@ManageLoginRequired(authLevel = {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class IndexAction extends PcAdminAction {

    /**
     * Ajaxフォーム
     */
    @ActionForm
    @Resource
    protected IndexForm indexForm;

    /**
     * エリアマスタ 首都圏
     */
    private static final String SHUTOKEN = String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA);

    private static final String SENDAI = String.valueOf(MAreaConstants.AreaCd.SENDAI_AREA);


    /**
     * エリアの値から号数を絞り込む
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitVolumePull() {

        disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP01AV1;
    }

    /**
     * エリアの値から特集を絞り込む。
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitSpecialSelect() {

    	disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP01AP1;
    }

    @Execute(validator = false, reset = "resetForm")
    public String listSpecialSelect() {

    	disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP01AP2;
    }

    /**
     * エリアの値から会社を絞り込む
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     * limitValueがブランクの場合
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitAssignedCompanyPullNotValue() {
        return limitAssignedCompanyPull();
    }

    /**
     * エリアの値から会社を絞り込む
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitAssignedCompanyPull() {

        disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP02C01;
    }

    /**
     * 会社の値から営業担当者を絞り込む（検索用）<br />
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     * limitValueがブランクの場合
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitAssignedSalesPullNotValue() {
        return limitAssignedSalesPull();
    }

    /**
     * 会社の値から営業担当者を絞り込む（検索用）<br />
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitAssignedSalesPull() {

        disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP02S01;
    }

    /**
     * 会社IDから営業担当者を絞り込むんだプルダウンjspを返却する<br />
     *
     * @return 営業担当者のプルダウン
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitSalesPull() {

        disabledCheck(indexForm.limitValue);
        // 営業担当者プルダウンjsp
        return TransitionConstants.Ajax.JSP_APP01AU1;
    }

    /**
     * エリアの値から鉄道会社を絞り込む
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitRailroadPull() {

        disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP01AR1;
    }

    /**
     * エリアの値から路線を絞り込む
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitRoutePull() {

        disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP01AL1;
    }

    /**
     * エリアの値から駅を絞り込む
     * カスタムタグを使用しDB引き当てを行い挿入
     * プルダウン生成
     *
     * @return
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitStationPull() {

        disabledCheck(indexForm.limitValue);
        return TransitionConstants.Ajax.JSP_APP01AS1;
    }

    /**
     * エリアの値からWEBデータ用の区分マスタの勤務地エリア(WEBエリアから名称変更)区分を切り替える<br />
     * カスタムタグを使用しDB引き当てを行い挿入<br />
     * チェックボックス生成
     *
     * @return WEBデータ用の勤務地エリアチェックボックスの部品
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitWebAreaCheck() {

        createAreaTypeList();

        return TransitionConstants.Ajax.JSP_APP01AW2;
    }

    /**
     * エリアの値からWEBデータ用の区分マスタの勤務地エリア(WEBエリアから名称変更)区分を切り替える<br />
     * カスタムタグを使用しDB引き当てを行い挿入<br />
     * SELECTボックス生成
     *
     * @return WEBデータ用の勤務地エリアSELECTボックスの部品
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitWebAreaSelect() {

        createAreaTypeList();

        return TransitionConstants.Ajax.JSP_APP01AW3;
    }

    /**
     * URLPattern版
     * エリアの値からWEBデータ用の区分マスタの勤務地エリア(WEBエリアから名称変更)区分を切り替える<br />
     * カスタムタグを使用しDB引き当てを行い挿入<br />
     * SELECTボックス生成
     *
     * @return WEBデータ用の勤務地エリアSELECTボックスの部品
     */
    @Execute(validator = false, reset = "resetForm", urlPattern = "limitWebAreaSelectPattern/{limitValue}")
    public String limitWebAreaSelectPattern() {
        createAreaTypeList();

        return TransitionConstants.Ajax.JSP_APP01AW3;
    }

    /**
     * エリアの値からWEBデータ用の区分マスタの勤務地エリア(WEBエリアから名称変更)区分を切り替える<br />
     * カスタムタグを使用しDB引き当てを行い挿入<br />
     * チェックボックス生成
     *
     * @return WEBデータ用の勤務地エリアチェックボックスの部品
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitDetailAreaCheck() {

        createDetailAreaTypeList();

        return TransitionConstants.Ajax.JSP_APP01AD2;
    }

    /**
     * エリアの値からWEBデータ用の区分マスタの勤務地エリア(WEBエリアから名称変更)区分を切り替える<br />
     * カスタムタグを使用しDB引き当てを行い挿入<br />
     * プルダウン生成
     *
     * @return WEBデータ用の勤務地エリアプルダウンの部品
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitDetailAreaSelect() {

        createDetailAreaTypeList();

        return TransitionConstants.Ajax.JSP_APP01AD3;
    }

    /**
     * エリアの値から区分マスタから主要駅区分を切り替える<br />
     * カスタムタグを使用しDB引き当てを行い挿入<br />
     * チェックボックス生成
     *
     * @return 主要駅チェックボックスの部品
     */
    @Execute(validator = false, reset = "resetForm")
    public String limitMainStationCheck() {

        // エリア指定なし
        if (StringUtils.isBlank(indexForm.limitValue)) {

            // disabledで表示する。
            indexForm.disabledFlg = true;
            indexForm.typeCd = "";

            // エリア首都圏の場合
        } else if (indexForm.limitValue.equals(SHUTOKEN)) {
            indexForm.typeCd = MTypeConstants.ShutokenMainStationKbn.TYPE_CD;
        }
//		else {
//			// デフォルトを首都圏とする。
//			indexForm.typeCd = MTypeConstants.ShutokenMainStationKbn.TYPE_CD;
//		}

        return TransitionConstants.Ajax.JSP_APP01AM2;
    }

    /**
     * 引数の値がブランクの場合はdisabledFlgをtrueとします。
     * ブランクでは無い場合はfalseとします。
     *
     * @param value 値
     */
    private void disabledCheck(String value) {
        if (StringUtils.isBlank(value)) {
            indexForm.disabledFlg = true;
        } else {
            indexForm.disabledFlg = false;
        }
    }


    /**
     * エリアのタイプリストを生成。
     * 生成したものはフォームに保存。
     */
    private void createAreaTypeList() {
        // エリア指定なし
        if (StringUtils.isBlank(indexForm.limitValue)) {
            // disabledで表示する。
            indexForm.disabledFlg = true;
            indexForm.typeCd = "";
            indexForm.typeCd2 = "";

            // エリア首都圏の場合
        } else if (indexForm.limitValue.equals(SHUTOKEN)) {
            indexForm.typeCd = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
            indexForm.noDisplayList = MTypeConstants.ShutokenWebAreaKbn.getWebdataNoDispList();
            indexForm.typeCd2 = MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD;
            // エリア仙台の場合
        } else if (indexForm.limitValue.equals(SENDAI)) {
            indexForm.typeCd = MTypeConstants.SendaiWebAreaKbn.TYPE_CD;
            indexForm.typeCd2 = "";
        }
    }

    private void createDetailAreaTypeList() {
        //エリア指定なし
        if (StringUtils.isBlank(indexForm.limitValue)) {
            // disableで表示する・
            indexForm.disabledFlg = true;
            indexForm.typeCd = "";
        } else if (indexForm.limitValue.equals(SHUTOKEN)) {
            indexForm.typeCd = MTypeConstants.ShutokenDetailAreaKbn.TYPE_CD;
        } else if (indexForm.limitValue.equals(SENDAI)) {
            indexForm.typeCd = MTypeConstants.SendaiDetailAreaKbn.TYPE_CD;
        }
    }
}
