import org.json.JSONObject;

import cl.dlab.abm.util.Param;
import cl.dlab.abm.util.Utils;

public class TestPutDataKQML
{
	public static void main(String[] args) throws Exception
	{
		String url = "http://192.168.2.121:18080/dynamic-data";
		JSONObject data = new JSONObject();
		JSONObject agenti = new JSONObject();
		JSONObject agentj = new JSONObject();
		agenti.put("agent_id", 1);
		agenti.put("reci", 0.45);
		agenti.put("repu", 0.35);
		agenti.put("conf", 0.23);

		agentj.put("agent_id", 2);
		agentj.put("reci", 0.45);
		agentj.put("repu", 0.26);
		agentj.put("conf", 0.13);
		
		data.put("model_id", "x-rrr-test");
		data.put("pozo", 2.56);
		data.put("agenti", agenti);
		data.put("agentj", agentj);
		
		
		String s = Utils.sendData(url, "POST", new Param("data", data), new Param("schema_id", "Social0_Simulations_11_JM"));
		System.out.println(s);
	}
}
