package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class GeneralAnalysisTypeOutputVO extends VOBase {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public String getDescription() {
		return get("description");
	}
}