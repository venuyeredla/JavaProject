package org.vgr.ioc.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
	public T mapRowToObj(ResultSet resultSet)throws SQLException;
}
