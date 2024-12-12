package cl.dlab.abm.service.vo;

public class AnalysisMethodsOutputVO extends VOBase {

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

	public void setParentName(java.lang.String parentName) {
		set("parentName", parentName);
	}

	public String getParentName() {
		return get("parentName");
	}
}