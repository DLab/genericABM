package cl.dlab.abm.service.vo;

public class GeneralAnalysisTypeInputVO extends InputVO {

	@Override
	public Class<GeneralAnalysisTypeOutputVO> getOutputClass() {
		return GeneralAnalysisTypeOutputVO.class;
	}
}