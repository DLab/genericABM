package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class DimensionalityReductionMethodsInputVO extends InputVO {

	@Override
	public Class<DimensionalityReductionMethodsOutputVO> getOutputClass() {
		return DimensionalityReductionMethodsOutputVO.class;
	}
}