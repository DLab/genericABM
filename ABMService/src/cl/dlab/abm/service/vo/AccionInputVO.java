package cl.dlab.abm.service.vo;

public class AccionInputVO extends InputVO {

	@Override
	public Class<AccionOutputVO> getOutputClass() {
		return AccionOutputVO.class;
	}
}