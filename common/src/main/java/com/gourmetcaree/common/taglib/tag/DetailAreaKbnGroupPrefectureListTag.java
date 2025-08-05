package com.gourmetcaree.common.taglib.tag;

import com.gourmetcaree.common.logic.TypeLogic;
import com.gourmetcaree.common.util.Utils;
import com.gourmetcaree.valueobject.TypePrefectureInfo;
import lombok.Setter;
import org.seasar.framework.container.SingletonS2Container;

import javax.servlet.jsp.JspException;
import java.util.List;

/**
 * 都道府県ごとに詳細エリアグループのリストを生成するタグ
 */
public class DetailAreaKbnGroupPrefectureListTag extends BaseTagSupport {


    private static final long serialVersionUID = 3439409918930202401L;

    @Setter
    private String name;

    @Setter
    private Object areaCd;

    @Setter
    private String scope;

    @Override
    public int doEndTag() throws JspException {

        TypeLogic logic = SingletonS2Container.getComponent(TypeLogic.class);
        List<TypePrefectureInfo> list = logic.selectDetailAreaKbnGroupPrefectures(Utils.toObjectToInt(areaCd, 0));
        setAttribute(name, list, scope);
        return super.doEndTag();
    }
}
