package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

public class SimulationsContentOutputVO extends VOBase {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setContent(java.lang.Object content) {
		set("content", content);
	}

	public Object getContent() {
		return get("content");
	}
}