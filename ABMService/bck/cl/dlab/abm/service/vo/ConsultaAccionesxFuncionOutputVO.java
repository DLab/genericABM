package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class ConsultaAccionesxFuncionOutputVO extends VOBase {

	public void setIdAccion(java.lang.Integer idAccion) {
		set("idAccion", idAccion);
	}

	public Integer getIdAccion() {
		return get("idAccion");
	}

	public void setDescripcion(java.lang.String descripcion) {
		set("descripcion", descripcion);
	}

	public String getDescripcion() {
		return get("descripcion");
	}
}