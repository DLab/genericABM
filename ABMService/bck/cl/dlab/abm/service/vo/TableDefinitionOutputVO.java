package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.VOBase;
import java.util.Date;

public class TableDefinitionOutputVO extends VOBase {

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

	public void setValid(java.lang.Boolean valid) {
		set("valid", valid);
	}

	public Boolean getValid() {
		return get("valid");
	}

	public void setParentId(java.lang.Integer parentId) {
		set("parentId", parentId);
	}

	public Integer getParentId() {
		return get("parentId");
	}

	public void setTimestamp(Date timestamp) {
		set("timestamp", timestamp);
	}

	public Date getTimestamp() {
		return get("timestamp");
	}
}