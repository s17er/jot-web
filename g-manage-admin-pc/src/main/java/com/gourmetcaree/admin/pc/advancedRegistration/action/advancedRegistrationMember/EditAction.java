package com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember;

import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.advancedregistration.MemberLogic;
import com.gourmetcaree.common.util.GcCollectionUtils;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import javax.annotation.Resource;

/**
 * Created by ZRX on 2017/06/08.
 */
public class EditAction extends PcAdminAction {

    /**
     * 入力画面JSP
     */
    public static final String JSP_INPUT = "/advancedRegistration/apH01E01.jsp";

    /**
     * 確認画面JSP
     */
    public static final String JSP_CONF = "/advancedRegistration/apH01E02.jsp";

    /**
     * 完了画面JSP
     */
    public static final String JSP_COMP = "/advancedRegistration/apH01E03.jsp";

    @ActionForm
    @Resource(name = "advancedRegistrationMember_editForm")
    EditForm form;

    @Resource(name ="advancedregistration_memberLogic")
    MemberLogic logic;


    /**
     * 初期画面(入力画面)
     * @return 入力画面JSP
     */
    @Execute(validator = false, reset = "resetForm", urlPattern = "{id}", input = JSP_INPUT)
    public String index() {
        actionLog.info(String.format("index id:[%s]", form.id));
        logic.createDetailData(form);
        actionLog.info(String.format("version:%s", form.version));
        return JSP_INPUT;
    }


    /**
     * 確認画面
     * @return 確認画面JSP
     */
    @Execute(validate = "validate", reset = "resetCheckBox", input = JSP_INPUT)
    public String conf() {
        actionLog.info(String.format("conf id:[%s]", form.id));
        form.setProcessFlgOk();
        form.hopeCareerChangeStr = logic.createHopeTermStr(form.hopeCareerChangeYear,
                form.hopeCareerChangeMonth,
                form.createHopeCareerTermIntegerList());
        saveToken();
        return JSP_CONF;
    }

    /**
     * 訂正
     * @return 入力画面JSP
     */
    @Execute(validator = false)
    public String correct() {
        actionLog.info(String.format("correct id:[%s]", form.id));
        form.setProcessFlgNg();
        return JSP_INPUT;
    }


    /**
     * 変更を行う処理
     * @return 完了画面リダイレクトパス
     */
    @Execute(validator = false, input = JSP_INPUT)
    public String submit() {
        actionLog.info(String.format("submit id:[%s]", form.id));
        checkTokenThrowable();
        if (!form.processFlg) {
            throw new FraudulentProcessException("processFlgがfalseです。");
        }
        logic.update(form);

        return "/advancedRegistrationMember/edit/comp?redirect=true";
    }


    /**
     * 完了画面
     * @return 完了画面JSP
     */
    @Execute(validator = false, removeActionForm = true)
    public String comp() {
        return JSP_COMP;
    }

    /**
     * 詳細ページへ戻る
     */
    @Execute(validator = false, urlPattern = "backToDetail/{id}", removeActionForm = true)
    public String backToDetail() {
        return String.format("/advancedRegistrationMember/detail/index/%s?redirect=true",
                form.id);
    }

    /**
     * 詳細ページへ戻るURL
     */
    public String getBackUrl() {
        return "/advancedRegistrationMember/edit/backToDetail/".concat(form.id);
    }
}
