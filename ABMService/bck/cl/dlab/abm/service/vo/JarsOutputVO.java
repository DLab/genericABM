package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;
import java.util.Date;

public class JarsOutputVO extends VOBase {

	public void setJarName(java.lang.String jarName) {
		set("jarName", jarName);
	}

	public String getJarName() {
		return get("jarName");
	}

	public void setTimestamp(Date timestamp) {
		set("timestamp", timestamp);
	}

	public Date getTimestamp() {
		return get("timestamp");
	}

	public void setContent(java.lang.Object content) {
		set("content", content);
	}

	public Object getContent() {
		return get("content");
	}
}