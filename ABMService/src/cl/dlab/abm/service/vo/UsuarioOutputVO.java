package cl.dlab.abm.service.vo;

public class UsuarioOutputVO extends VOBase {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setPassword(byte[] password) {
		set("password", password);
	}

	public byte[] getPassword() {
		return get("password");
	}

	public void setNombre(java.lang.String nombre) {
		set("nombre", nombre);
	}

	public String getNombre() {
		return get("nombre");
	}

	public void setIdRol(java.lang.Integer idRol) {
		set("idRol", idRol);
	}

	public Integer getIdRol() {
		return get("idRol");
	}

	public void setConfigurations(java.lang.String configurations) {
		set("configurations", configurations);
	}

	public String getConfigurations() {
		return get("configurations");
	}

	public String getRol() {
		return get("rol");
	}
}