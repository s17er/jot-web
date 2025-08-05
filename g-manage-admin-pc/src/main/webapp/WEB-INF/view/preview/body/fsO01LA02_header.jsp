<%@page pageEncoding="UTF-8"%>

<%-- ヘッダの部分(掲載期間、店舗名) --%>

<!-- // 掲載期間 -->
<div id="kikan">掲載期間：${f:h(postStartDatetime)} ～ ${f:h(postEndDatetime) }</div>


<!-- // 店舗・企業ネーム -->
<h3>${f:h(manuscriptName)}</h3>