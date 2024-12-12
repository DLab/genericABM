package cl.dlab.abm.service.vo;

public class ModelInputVO extends InputVO {

	@Override
	public Class<ModelOutputVO> getOutputClass() {
		return ModelOutputVO.class;
	}
}