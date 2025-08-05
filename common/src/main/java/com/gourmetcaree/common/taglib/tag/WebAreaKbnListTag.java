package com.gourmetcaree.common.taglib.tag;

import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.logic.TypeLogic;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.common.util.Utils;
import com.gourmetcaree.db.common.entity.MType;
import lombok.Setter;
import org.seasar.framework.container.SingletonS2Container;

import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.List;

public class WebAreaKbnListTag extends BaseTagSupport {

    /** 名前 */
    @Setter
    private String name;

    /** エリアコード */
    @Setter
    private Object areaCd;

    /** 初期ラベル */
    @Setter
    private String blankLineLabel = null;

    /** 非表示値 */
    @Setter
    private Collection<?> noDisplayValues;

    /** スコープ */
    @Setter
    private String scope = "";

    @Override
    public int doEndTag() throws JspException {
        TypeLogic logic = SingletonS2Container.getComponent(TypeLogic.class);
        List<MType> typeList = logic.selectWebAreaKbnsByAreaCdAndNotInTypeValues(Utils.toObjectToInt(areaCd), GcCollectionUtils.toIntegerList(noDisplayValues));

        List<LabelValueDto> dtoList = LabelValueDto.newInstanceList(typeList);
        LabelValueDto.addBlankLineLabel(dtoList, blankLineLabel);

        setAttribute(name, dtoList, scope);
        return super.doEndTag();
    }
}
