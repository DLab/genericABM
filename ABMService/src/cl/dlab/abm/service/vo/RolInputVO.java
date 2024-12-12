package cl.dlab.abm.service.vo;

public class RolInputVO extends InputVO {

	@Override
	public Class<RolOutputVO> getOutputClass() {
		return RolOutputVO.class;
	}
}