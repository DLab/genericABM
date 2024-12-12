package cl.dlab.abm.service.vo;

public class GraphTypeInputVO extends InputVO {

	@Override
	public Class<GraphTypeOutputVO> getOutputClass() {
		return GraphTypeOutputVO.class;
	}
}