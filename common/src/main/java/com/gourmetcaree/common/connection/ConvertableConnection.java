package com.gourmetcaree.common.connection;

import java.io.IOException;
import java.io.InputStream;

import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * コネクションの基底クラス
 * XXX whizzCommonに置きたい。
 * @author nakamori
 *
 */
public abstract class ConvertableConnection<RESULT> extends AbstractBaseConnection {

	/**
	 * @deprecated 親のexecuteをコールし、convertで任意の型に変更するため、このクラスのexecuteは使用しない。
	 */
	@Override
	@Deprecated
	public InputStream execute() throws IOException {
		throw new UnsupportedOperationException("connect() を使うようにしてください。");
	}


	/**
	 *
	 * @return
	 */
	public RESULT connect() throws IOException {
		InputStream input = null;
		try {
			input = super.execute();
			return convert(input);
		} finally {
			GourmetCareeUtil.closeQuietly(input);
		}
	}


	/**
	 * 指定のURLにアクセスをした結果を任意の型に変換
	 * @param inputStream
	 * @return
	 */
	protected abstract RESULT convert(InputStream inputStream) throws IOException;


}
