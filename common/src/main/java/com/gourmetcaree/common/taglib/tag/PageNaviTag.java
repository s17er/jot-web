package com.gourmetcaree.common.taglib.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.gourmetcaree.db.common.helper.PageNavigatorDto;
import org.apache.log4j.Logger;

/**
 * BODYを評価し、ページナビゲータを出力します。
 * XXX 余計な変数とかいろいろありすぎ。
 * @author Takahiro Ando
 * @version 1.0
 */
public class PageNaviTag extends BodyTagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1518915167764034024L;

	private final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 現在ページ(String)
	 */
	private String currentPage;
	private int currentPageNum = 0;

	/**
	 * 最大ページ
	 */
	private String lastPage;
	private int lastPageNum;

    /**
     * 現在表示中のページ番号。
     * 多分currentPageと被らないようにするためにこんな変数名になっていると思われる。
     * XXX また、最終ページと次へリンク、前へリンクにも使われるため、純粋なページ番号でないため注意
     */
	private int loopStartPage = 0;
	private int loopEndPage = 0;

	/**
	 * 「前へ」「次へ」ボタン用フラグ
	 */
	private boolean prevLinkFlg = true;
	private boolean nextLinkFlg = true;
	private boolean nextOutFlg = true;

	/**
	 * 前ページ、次ページ
	 */
	private int prevPageNum = 1;
	private int nextPageNum = 2;

	/**
	 * デフォルトナビ表示数
	 */
	private static final Integer DEFAULT_DISP_PAGE=10;

	/**
	 * 最小ナビ表示数
	 */
	private static final Integer MIN_DISP_PAGE=3;

	/**
	 * ナビ表示数
	 */
	private String dispPage;
	private int dispPageNum = DEFAULT_DISP_PAGE;

	/**
	 * 中心のナビ数
	 */
	private int centerDispPageNum;

	/**
	 * 前ページ用デフォルトラベル
	 */
	private static final String DEFAULT_PREV_LABEL = "前へ";

	/**
	 * 次ページ用デフォルトラベル
	 */
	private static final String DEFAULT_NEXT_LABEL = "次へ";

	/**
	 * 前ページ用ラベル
	 */
	private String prevLabel;

	/**
	 * 次ページ用ラベル
	 */
	private String nextLabel;

    /**
     * 最終ページを表示するフラグ
     * XXX この変数は直接使わず、isLastPageDisplayを使うこと。
     */
	@Setter
	private boolean lastPageDisplayFlg;



	/**
	 * 現在の繰り返し値(1から始まるループ)
	 */
	private int count = 1;

	public String getLastPage() {
		return lastPage;
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
		lastPageNum = NumberUtils.toInt(lastPage, 0);
	}

	private String var = "";


	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
		currentPageNum = NumberUtils.toInt(currentPage, 0);
	}

	public String getDispPage() {
		return dispPage;
	}

	public void setDispPage(String dispPage) {
		this.dispPage = dispPage;
		dispPageNum = NumberUtils.toInt(dispPage, DEFAULT_DISP_PAGE);
		if (dispPageNum < MIN_DISP_PAGE) {
			dispPageNum = MIN_DISP_PAGE;
		}
	}

	public String getPrevLabel() {
		return prevLabel;
	}

	public void setPrevLabel(String prevLabel) {
		if (StringUtils.isNotBlank(prevLabel)) {
			this.prevLabel = prevLabel;
		} else {
			this.prevLabel = DEFAULT_PREV_LABEL;
		}
	}

	public String getNextLabel() {
		return nextLabel;
	}

	public void setNextLabel(String nextLabel) {
		if (StringUtils.isNotBlank(nextLabel)) {
			this.nextLabel = nextLabel;
		} else {
			this.nextLabel = DEFAULT_NEXT_LABEL;
		}
	}

    /**
     * 現在ページが最終ページかどうか
     * @return 最終ページの場合にtrue
     */
	public boolean isCurrentLastPage() {
        return currentPageNum == lastPageNum;
    }

	public PageNaviTag() {
		super();
	}

	/**
	 * カスタムタグ読み込み時に呼ばれる
	 */
	@Override
	public int doStartTag() throws JspTagException {

		//ページ情報を設定
		modifyLoopInfo();

		if (currentPageNum > 0) {

			//前へボタンを作成
			PageNavigatorDto dto = new PageNavigatorDto();
			if (StringUtils.isNotBlank(prevLabel)) {
				dto.label = prevLabel;
			} else {
				dto.label = DEFAULT_PREV_LABEL;
			}
			dto.pageNum = Integer.toString(prevPageNum);
			dto.linkFlg = prevLinkFlg;

			//「前へ」を保持する要素であることをセット
			dto.prevLabelFlg = true;

			//セット
			pageContext.setAttribute(var, dto);

			// 結果が存在する場合はボディを評価する
			return EVAL_BODY_AGAIN;
		} else {
			//未処理の場合はBODY内のタグを出力しない
			return SKIP_BODY;
		}
	}

	/**
	 * BODY読み込み後処理。ループの2週目以降もこのメソッドが使用される。
	 */
	@Override
	public int doAfterBody() throws JspTagException {

		//ボディの内容を取得
		BodyContent body = getBodyContent();

		try {
			//ボディを出力
			body.writeOut(getPreviousOut());
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

		body.clearBody();

		PageNavigatorDto dto = new PageNavigatorDto();

		logger.trace(String.format("currentPage:[%s] loopStartPage:[%s] loopEndPage:[%s] currentPageNum:[%s]",
                currentPage, loopStartPage, loopEndPage, currentPageNum));
		if (loopStartPage <= loopEndPage) {
            dto.pageNum = Integer.toString(loopStartPage);
            dto.label = Integer.toString(loopStartPage);

            if (loopStartPage == currentPageNum) {
                dto.linkFlg = false;
            } else {
                dto.linkFlg = true;
            }

        } else if (isLastPageLabelCount()) {
            generateLastPage(dto);
		} else if (isNextPageLabelCount()) {
			generateNextPage(dto);
		} else {
			nextOutFlg = false;
		}

		//現在の繰り返し値を設定します。
		dto.count = ++count;

		pageContext.removeAttribute(var);
		pageContext.setAttribute(var, dto);

		if ((loopStartPage <= loopEndPage) || nextOutFlg) {

			nextOutFlg = false;
			loopStartPage+=1;

			//まだ行が残っている場合はボディの評価を繰り返す
			return EVAL_BODY_AGAIN;
		} else {
			//ループが終われば以降をスキップ
			return SKIP_BODY;
		}
	}

	/**
	 * ページナビの情報を修正します。
	 */
	private void modifyLoopInfo() {
		//初期化
		loopStartPage = 1;
		loopEndPage = dispPageNum;
		nextOutFlg = false;
		prevLinkFlg = false;
		nextLinkFlg = true;
		prevPageNum = 1;
		nextPageNum = 2;

		//中心のページ数初期化
		//ページ表示数を2で割って1を足したものを中心とする。(小数点以下は切り捨てとする)
		centerDispPageNum = dispPageNum / 2 + 1;

		//前ページのページ数設定
		if (currentPageNum > 1) {
			prevPageNum = currentPageNum - 1;
			prevLinkFlg = true;
		}

		//次ページのページ数設定
		nextPageNum = currentPageNum + 1;


		//スタートページを調整
		if (currentPageNum > centerDispPageNum) {
			loopStartPage = currentPageNum - centerDispPageNum + 1;
			loopEndPage = (loopStartPage + dispPageNum) - 1;
		}

		//最大表示数より最後のページの方が少ない場合は調整(最終ページが一番右端になるようにする)
		if (loopEndPage > lastPageNum) {
			loopEndPage = lastPageNum;
			loopStartPage = (loopEndPage - dispPageNum) + 1;

			if (loopStartPage < 1) {
				loopStartPage = 1;
			}
		}

		if (loopEndPage != 1 && currentPageNum != loopEndPage) {
			nextLinkFlg = true;
		} else {
			nextLinkFlg = false;
		}

	}


	/**
	 * 後処理
	 */
	@Override
	public int doEndTag() {
		pageContext.removeAttribute(var);
		return EVAL_PAGE;
	}


    /**
     * @return 「次へ」のリンクの回かどうか。
     */
	private boolean isNextPageLabelCount() {
	    // 最終ページ番号との誤差
	    final int diff;
	    if (isLastPageDisplay()) {
	        // 最終ページを表示する場合は、最終ページと、次へ分になるので2
	        diff = 2;
        } else {
	        // 最終ページがない場合は次へ分のみなので1
	        diff = 1;
        }

        return loopStartPage == (loopEndPage + diff);
    }


    /**
     * @return 最終ページを表示するリンク回かどうか
     */
    private boolean isLastPageLabelCount() {
	    return isLastPageDisplay()
                && loopStartPage == (loopEndPage + 1);

    }

    /**
     * 最終ページのページ情報を生成
     */
    private void generateLastPage(PageNavigatorDto dto) {
        dto.setPageNum(lastPage);
        dto.setLinkFlg(!isCurrentLastPage());
        dto.setLabel(lastPage);
        dto.setToLastPageFlg(true);
        nextOutFlg = true;

    }

    /**
     * 「次へ」のページ情報を生成
     * @param dto ページナビDTO
     */
    private void generateNextPage(PageNavigatorDto dto) {
        //ループの最後＋１の場合のみ「次へ」ナビ表示
        dto.pageNum = Integer.toString(nextPageNum);
        if (StringUtils.isNotBlank(nextLabel)) {
            dto.label = nextLabel;
        } else {
            dto.label = DEFAULT_NEXT_LABEL;
        }
        dto.linkFlg = nextLinkFlg;
        //「次へ」を保持する要素であることをセット
        dto.nextLabelFlg = true;

        //最終ページ通過後に「次へ」を出力するためのフラグ
        nextOutFlg = true;
    }

    private boolean isLastPageDisplay() {
        return lastPageDisplayFlg
                && !isDisplayedLastPage();
    }

    /**
     * 最終ページが表示された場合にtrue
     * ページネーションの最終ページ表示と、独自で表示している固定の最終ページ表示がかぶらないようにするためのもの。
     */
    private boolean isDisplayedLastPage() {
        return loopEndPage == lastPageNum;
    }
}
