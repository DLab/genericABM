package cl.dlab.abm.service.vo;

public class GraphAlgorithmInputVO extends InputVO {

	@Override
	public Class<GraphAlgorithmOutputVO> getOutputClass() {
		return GraphAlgorithmOutputVO.class;
	}
}