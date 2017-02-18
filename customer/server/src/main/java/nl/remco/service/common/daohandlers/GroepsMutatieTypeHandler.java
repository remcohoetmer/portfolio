package nl.remco.service.common.daohandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import nl.remco.service.groep.model.GroepsMutatieType;

public class GroepsMutatieTypeHandler extends BaseTypeHandler<GroepsMutatieType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, GroepsMutatieType parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, convert(parameter));
	}

	@Override
	public GroepsMutatieType getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return convert(rs.getString(columnName));
	}

	@Override
	public GroepsMutatieType getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return convert(rs.getString(columnIndex));
	}

	@Override
	public GroepsMutatieType getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return convert(cs.getString(columnIndex));
	}

	private String convert(GroepsMutatieType mutatieType) {
		if (mutatieType==null){
			return null;
		}
		switch (mutatieType){
		case SELFSERVICE: return "S";
		case MANAGED: return "M";
		}
		throw new RuntimeException("GroepsmutatieType not implemented"+ mutatieType);
	}

	private GroepsMutatieType convert(String mutatieType) {
		if (mutatieType==null){
			return null;
		}
		if ("S".equals( mutatieType))
			return GroepsMutatieType.SELFSERVICE;
		if ("M".equals( mutatieType))
			return GroepsMutatieType.MANAGED;
		throw new RuntimeException("Unbekende waarde: "+ mutatieType);

	}

}