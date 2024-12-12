package cl.dlab.abm.service.vo;

import cl.dlab.abm.service.vo.InputVO;

public class UsuarioInputVO extends InputVO {

	@Override
	public Class<UsuarioOutputVO> getOutputClass() {
		return UsuarioOutputVO.class;
	}
}