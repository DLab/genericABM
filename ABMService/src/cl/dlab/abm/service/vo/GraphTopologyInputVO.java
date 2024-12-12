package cl.dlab.abm.service.vo;

public class GraphTopologyInputVO extends InputVO {

	@Override
	public Class<GraphTopologyOutputVO> getOutputClass() {
		return GraphTopologyOutputVO.class;
	}
}