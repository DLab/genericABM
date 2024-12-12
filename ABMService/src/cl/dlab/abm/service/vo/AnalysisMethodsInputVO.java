package cl.dlab.abm.service.vo;

public class AnalysisMethodsInputVO extends InputVO {

	@Override
	public Class<AnalysisMethodsOutputVO> getOutputClass() {
		return AnalysisMethodsOutputVO.class;
	}
}