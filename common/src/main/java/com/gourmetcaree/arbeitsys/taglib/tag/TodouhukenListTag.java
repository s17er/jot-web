package com.gourmetcaree.arbeitsys.taglib.tag;

import com.gourmetcaree.arbeitsys.logic.ArbeitLabelValueListLogic;
import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.taglib.tag.BaseTagSupport;
import lombok.Setter;
import org.seasar.framework.container.SingletonS2Container;

import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.List;

/**
 * グルメdeバイトの都道府県リストを生成して出力します。
 *
 * @author Takehiro Nakamori
 */
public class TodouhukenListTag extends BaseTagSupport {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -320357217912906094L;

    /**
     * 名前
     */
    @Setter
    private String name;

    /**
     * 初期ラベル
     */
    @Setter
    private String blankLineLabel = null;


    /**
     * 接尾辞
     */
    @Setter
    private String suffix = "";

    /**
     * 表示リスト
     * 仙台版では福島以北を表示するなど、限定的に表示させるため
     */
    @Setter
    private Collection<?> prefIds;


    /*
     * (非 Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {

        ArbeitLabelValueListLogic logic = SingletonS2Container.getComponent(ArbeitLabelValueListLogic.class);
        List<LabelValueDto> dtoList = logic.getTodouhukenList(blankLineLabel, suffix, prefIds);

        setAttribute(name, dtoList);

        return EVAL_PAGE;
    }


}
