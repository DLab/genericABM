package cl.dlab.abm.service.vo;

public abstract class InputVO extends VOBase
{
	public abstract Class<? extends VOBase> getOutputClass();
}
