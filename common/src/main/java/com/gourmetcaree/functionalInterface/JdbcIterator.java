package com.gourmetcaree.functionalInterface;

/**
 * JDBCマネージャのiterator
 * @author nakamori
 *
 */
public interface JdbcIterator<T> {
	public void iterate(T entity);
}
