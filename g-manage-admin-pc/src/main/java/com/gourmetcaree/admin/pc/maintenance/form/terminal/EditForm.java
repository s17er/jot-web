package com.gourmetcaree.admin.pc.maintenance.form.terminal;

import java.io.Serializable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 駅グループ編集のフォーム
 * @author yamane
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends TerminalForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5064839700484841912L;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	@Override
	public ActionMessages validate() {

		ActionMessages errors = new ActionMessages();

		if (CollectionUtils.isEmpty(sendJson)) {
            errors.add("errors", new ActionMessage("errors.notAllData",
                    MessageResourcesUtil.getMessage("labels.routeStation")));
		}

		return errors;
	}

}