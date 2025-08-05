package com.gourmetcaree.functionalInterface.builder;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;

import com.gourmetcaree.functionalInterface.JdbcIterator;

public class JdbcIteratorCallback {

	private JdbcIteratorCallback() {}

	public static <T> IterationCallback<T, Void> callback(final JdbcIterator<T> iterator) {
		return new IterationCallback<T, Void>() {
			@Override
			public Void iterate(T entity, IterationContext context) {
				if (entity != null) {
					iterator.iterate(entity);
				}
				return null;
			}
		};
	}
}
