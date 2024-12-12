package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;

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

	public void setMethodName(java.lang.String methodName) {
		set("methodName", methodName);
	}

	public String getMethodName() {
		return get("methodName");
	}

	public void setParametersGui(java.lang.String parametersGui) {
		set("parametersGui", parametersGui);
	}

	public String getParametersGui() {
		return get("parametersGui");
	}
}