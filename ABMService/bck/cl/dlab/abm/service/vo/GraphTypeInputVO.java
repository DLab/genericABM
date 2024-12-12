package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class GraphTypeInputVO extends InputVO {

	@Override
	public Class<GraphTypeOutputVO> getOutputClass() {
		return GraphTypeOutputVO.class;
	}
}