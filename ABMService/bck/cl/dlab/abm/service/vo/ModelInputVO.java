package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class ModelInputVO extends InputVO {

	@Override
	public Class<ModelOutputVO> getOutputClass() {
		return ModelOutputVO.class;
	}
}