package cl.dlab.abm.service.vo;

public class JarsInputVO extends InputVO {

	@Override
	public Class<JarsOutputVO> getOutputClass() {
		return JarsOutputVO.class;
	}

	public void setJarName(java.lang.String jarName) {
		set("jarName", jarName);
	}

	public String getJarName() {
		return get("jarName");
	}
}