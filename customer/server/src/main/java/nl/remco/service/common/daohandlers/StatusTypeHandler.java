package nl.remco.service.common.daohandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import nl.remco.service.common.model.LifeCycleBeheer.Status;

public class StatusTypeHandler extends BaseTypeHandler<Status> {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Status parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, convert(parameter));
	}

	@Override
	public Status getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return convert(rs.getString(columnName));
	}

	@Override
	public Status getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return convert(rs.getString(columnIndex));
	}

	@Override
	public Status getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return convert(cs.getString(columnIndex));
	}

	private String convert(Status status) {
		switch (status){
		case Actief: return "A";
		case Passief: return "P";
		case Verwijderd: return "V";
		}
		throw new RuntimeException("Error: Status null or not implemented"+ status);
	}

	private Status convert(String status) {
		if (status==null) {
			return null;
		}
		if ("A".equals(status))
			return Status.Actief;
		if ("P".equals(status))
			return Status.Passief;
		if ("V".equals(status))
			return Status.Verwijderd;
		throw new RuntimeException("Error: Database Status value null or unknown"+ status);

	}


}