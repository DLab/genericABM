package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class GraphTopologyOutputVO extends VOBase {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}
}