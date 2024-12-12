package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class GeneralAnalysisTypeInputVO extends InputVO {

	@Override
	public Class<GeneralAnalysisTypeOutputVO> getOutputClass() {
		return GeneralAnalysisTypeOutputVO.class;
	}
}