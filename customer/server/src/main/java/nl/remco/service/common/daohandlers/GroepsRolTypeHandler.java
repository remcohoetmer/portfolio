package nl.remco.service.common.daohandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import nl.remco.service.groep.model.Lidmaatschap;

public class GroepsRolTypeHandler extends BaseTypeHandler<Lidmaatschap.Rol> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Lidmaatschap.Rol parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, convert(parameter));
	}

	@Override
	public Lidmaatschap.Rol getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return convert(rs.getInt(columnName));
	}

	@Override
	public Lidmaatschap.Rol getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return convert(rs.getInt(columnIndex));
	}

	@Override
	public Lidmaatschap.Rol getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return convert(cs.getInt(columnIndex));
	}

	private int convert(Lidmaatschap.Rol rol) {
		switch (rol){
		case GROEPSLID: return 0;
		case GROEPSLEIDER: return 1;
		}
		throw new RuntimeException("Groepsrol not implemented"+ rol);
	}

	private Lidmaatschap.Rol convert(Integer rol) {
		if (new Integer(0).equals(rol))
			return Lidmaatschap.Rol.GROEPSLID;
		if (new Integer(1).equals(rol))
			return Lidmaatschap.Rol.GROEPSLEIDER;
		throw new RuntimeException("Unbekende waarde: "+ rol);

	}

}