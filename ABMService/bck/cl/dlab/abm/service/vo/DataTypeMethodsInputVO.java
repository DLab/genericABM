package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class DataTypeMethodsInputVO extends InputVO {

	@Override
	public Class<DataTypeMethodsOutputVO> getOutputClass() {
		return DataTypeMethodsOutputVO.class;
	}
}