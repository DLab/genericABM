package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class GraphAlgorithmInputVO extends InputVO {

	@Override
	public Class<GraphAlgorithmOutputVO> getOutputClass() {
		return GraphAlgorithmOutputVO.class;
	}
}