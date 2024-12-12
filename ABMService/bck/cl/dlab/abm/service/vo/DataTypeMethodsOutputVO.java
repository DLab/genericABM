package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class DataTypeMethodsOutputVO extends VOBase {

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}
}