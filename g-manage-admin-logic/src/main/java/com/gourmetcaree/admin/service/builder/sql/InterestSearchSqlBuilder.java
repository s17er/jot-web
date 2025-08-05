package com.gourmetcaree.admin.service.builder.sql;

import com.gourmetcaree.common.builder.sql.SqlBuilder;

/**
 * 応募検索SQLビルダー
 * 本来はこのI/Fは必要ないが、もともとの検索条件の引数にMapが使われているので、定数を切るためにこのI/Fを作成。
 * また、既にSQLがロジック内で書かれてしまっているため、このI/FはSQLを作るというより
 * 接ぎ木をするイメージで扱う。
 */
public interface InterestSearchSqlBuilder extends SqlBuilder {

    String SALES_ID = "salesId";

    String COMPANY_ID = "companyId";

    String HOPE_JOB = "hopeJob";
}
