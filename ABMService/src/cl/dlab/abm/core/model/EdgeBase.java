package cl.dlab.abm.core.model;

import org.jgrapht.graph.DefaultEdge;

@SuppressWarnings("serial")
public class EdgeBase extends DefaultEdge
{
	@Override
	public Integer getSource()
	{
		return (Integer)super.getSource();
	}
	@Override
	public Integer getTarget()
	{
		return (Integer)super.getTarget();
	}
}
