package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class AnalysisMethodsInputVO extends InputVO {

	@Override
	public Class<AnalysisMethodsOutputVO> getOutputClass() {
		return AnalysisMethodsOutputVO.class;
	}
}