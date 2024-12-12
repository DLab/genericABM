package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class GraphTopologyInputVO extends InputVO {

	@Override
	public Class<GraphTopologyOutputVO> getOutputClass() {
		return GraphTopologyOutputVO.class;
	}
}