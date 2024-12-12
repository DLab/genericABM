package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class ClusteringMethodsInputVO extends InputVO {

	@Override
	public Class<ClusteringMethodsOutputVO> getOutputClass() {
		return ClusteringMethodsOutputVO.class;
	}
}