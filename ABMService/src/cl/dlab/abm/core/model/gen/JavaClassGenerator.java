package cl.dlab.abm.core.model.gen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import cl.dlab.abm.core.parser.ObjectName;
import cl.dlab.abm.core.parser.Util;
import cl.dlab.abm.servlet.InitializeServlet;
import cl.dlab.util.LogUtil;

public class JavaClassGenerator
{
	private static final String CLONE_METHOD = "@Override public Object clone() throws CloneNotSupportedException { ";
	private static final String AGENT = "agent";
	private static final String SPACE = " ";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String EQUAL = " = ";

	private static final String VOID = "void";
	private static final String BOOLEAN = "boolean";
	private static final String STRING = "String";
	private static final String DOUBLE = "double";
	private static final String INT = "int";
	private static final String AGENT_FULL = "cl.dlab.abm.core.model.Agent";
	private static final String MODEL = "Model";

	private static final String PRIVATE = "private ";
	//private static final String PROTECTED = "protected ";
	private static final String PUBLIC = "public ";
	
	private static final String IF = "If";
	private static final String END_IF = "EndIf";
	private static final String ELSE = "Else";
	private static final String _ELSE_ = "}else{";
	private static final String RETURN = "Return";
	private static final String RETURN_ = "return ";

	private static final String NORMAL = "Normal";
	private static final String LOG_NORMAL = "LogNormal";
	private static final String NORMALIZER = "normalizer";
	private static final String LOGISTIC = "logistic";
	
	private static final String INITIAL_VALUE = "initialValue";
	private static final String IMPORT_FUNCTION = "import cl.dlab.abm.core.model.function.";
	private static final String PUBLIC_CLASS_MODEL = " public class Model extends cl.dlab.abm.core.model.Model implements Cloneable{";
	private static final String PUBLIC_CLASS_DATA = " public class Data extends cl.dlab.abm.core.model.Data {";
	private static final String ADDITIONAL_CODE = "additionalCode";
	private static final String PUBLIC_CLASS = "; public class ";
	private static final String CONDITIONAL_CODE = "conditionalCode";
	private static final String VARIABLE_TYPE_ID = "variableTypeId";
	private static final String FUNCTION_TYPE_ID = "functionTypeId";
	private static final String TYPE_ID = "typeId";
	private static final String PARAMETERS = "parameters";
	private static final String RESULT_TYPE_ID = "resultTypeId";

	private static final String PACKAGE_ = "package ";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String CODE = "code";
	private static final String ACTION = "action";
	private static final String N_0 = "0";
	
	private static final String SEMICOLON = ";";
	private static final String PUBLIC_VOID = "public void ";
	private static final String ALIAS_NAME = "aliasName";
	private static final String OBJ_NAME = "objName";
	private static final String _THIS_ = ")this)";
	
	private static final String[] EXTENSIONS = new String[]{ ".class", ".java" };
	private static final byte[] BUFFER = new byte[4096 * 1024];

	private class Source
	{
		String javaName;
		String source;
		ObjectName objectName;

		public Source(String javaName, ObjectName objectName, String source)
		{
			this.javaName = javaName;
			this.objectName = objectName;
			this.source = source;
		}
	}

	private StringBuilder imports;

	public static String getPackageName(String name)
	{
		String _package = JavaClassGenerator.class.getPackage().getName();
		return _package + "." + name.toLowerCase();

	}
	private String getPropertyType(HashMap<String, Object> prop)
	{
		return getType((Integer)prop.get(VARIABLE_TYPE_ID), (Integer)prop.get(FUNCTION_TYPE_ID), null); 
	}
	private String getType(int id, Integer functionId, String agentType)
	{
		switch (id)
		{
		case 1:
			return INT;
		case 2:
			return DOUBLE;
		case 3:
			return BOOLEAN;
		case 4:
			return STRING;
		case 5:			
			String s = functionId == 1 ? LOG_NORMAL : NORMAL;
			if (imports.indexOf("." + s) == -1)
			{
				imports.append(IMPORT_FUNCTION).append(s).append(SEMICOLON);				
			}
			return s;
		case 7:
			return VOID;
		case 8:
			return agentType == null ? AGENT_FULL : agentType; 
		case 9:
			return MODEL;
		default:
			break;
		}
		return null;
		
	}
	private String[] getProperties(HashMap<String, Object> alias, ArrayList<HashMap<String,Object>> properties, ObjectName name, boolean getterAndSetter)
	{
		StringBuilder properies = new StringBuilder();
		StringBuilder methods = new StringBuilder();
		String visibility = getterAndSetter ? PRIVATE : PUBLIC;
		for (HashMap<String, Object> prop : properties)
		{
			String type;
			String propertyName = Util.getPropertyName((String)prop.get(NAME));
			prop.put(NAME, propertyName);
			prop.put(OBJ_NAME, name);
			alias.put(((String)prop.get(ALIAS_NAME)).replace("\\", ""), prop);
			properies.append(visibility).append(type = getPropertyType(prop)).append(SPACE).append(propertyName);
			if (getterAndSetter)
			{
				methods.append(PUBLIC).append(type).append(SPACE).append(Util.getGetterName(propertyName)).append("{ return ").append(propertyName).append(";}");
				methods.append(PUBLIC_VOID).append(Util.getSetterName(propertyName)).append("(").append(type).append(SPACE).append(propertyName).append("){ this.").append(propertyName).append(EQUAL).append(propertyName).append(";}");
			}
			String initialValue = (String)prop.get(INITIAL_VALUE);
			if (initialValue != null)
			{
				int typeId = (Integer)prop.get(VARIABLE_TYPE_ID);
				if (type.equals(BOOLEAN))
				{
					initialValue = initialValue.equals(N_0) ? FALSE : TRUE;
				}
				if (typeId == 5)
				{
					String[] t = initialValue.split(";");
					properies.append(EQUAL).append("new ").append(type).append("(").append(t[0].replace(',', '.')).append(", ").append(t[1].replace(',', '.')).append(")");
				}
				else if (typeId == 4)
				{
					properies.append(EQUAL).append(" \"").append(initialValue).append("\"");
				}
				else
				{
					properies.append(EQUAL).append(initialValue.replace(',', '.'));
				}
			}
			properies.append(SEMICOLON);
		}
		return new String[] {properies.toString(), methods.toString()};
	}
	public StringBuilder getFunctions(JavaLatextParser parser, HashMap<String, Object> alias, JSONArray functions, ObjectName objectName, boolean getterAndSetter, boolean kqmlIntegration) throws Exception
	{
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < functions.length(); i++)
		{
			JSONObject fn = functions.getJSONObject(i);
			String name = fn.getString(NAME);
			alias.put((String)fn.get(ALIAS_NAME), Util.getAlias(name, objectName, -1));
			int resultTypeId = fn.getInt(RESULT_TYPE_ID);
			buff.append(PUBLIC).append(getType(resultTypeId, null, fn.has("agentType") && !fn.isNull("agentType")? fn.getString("agentType") : null)).append(SPACE).append(name).append("(");
			JSONArray parameters = fn.getJSONArray(PARAMETERS);
			String sep = "";
			for (int j = 0; j < parameters.length(); j++)
			{
				JSONObject param = parameters.getJSONObject(j);
				buff.append(sep).append(getType(param.getInt(TYPE_ID), null, param.has("agentType") && !param.isNull("agentType") ? param.getString("agentType") : null)).append(SPACE).append(param.getString(NAME));
				sep = ", ";
			}
			buff.append("){");
			if (resultTypeId != 7) {
				buff.append("return ");
			}
			System.out.println("ALIAS" + fn.get(CODE));
			buff.append(getActionCode(parser, new JSONArray(fn.getString(CODE)), getterAndSetter, kqmlIntegration)).append("}");
		}
		return buff;
	}
	@SuppressWarnings("rawtypes")
	private StringBuilder getActionCode(JavaLatextParser parser, JSONArray jarray, boolean getterAndSetter, boolean kqmlIntegration) throws Exception
	{
		StringBuilder code = new StringBuilder();
		if (kqmlIntegration)
		{
			code.append("\ncl.dlab.abm.core.kqml.KQMLMessage msgi = agenti.receiveMessage(model, new cl.dlab.abm.core.kqml.KQMLMessage(cl.dlab.abm.core.kqml.MessageType.Ask, agenti.getName(), agenti.getName(), \"give-me-your-properties\", \"KIF\", model.getName()));\n");
			code.append("\ncl.dlab.abm.core.kqml.KQMLMessage msgj = agentj.receiveMessage(model, new cl.dlab.abm.core.kqml.KQMLMessage(cl.dlab.abm.core.kqml.MessageType.Ask, agenti.getName(), agentj.getName(), \"give-me-your-properties\", \"KIF\", model.getName()));\n");
			code.append("\norg.json.JSONObject tgannt = model.sendDataTgatnn(agenti, agentj);\n");
			code.append("if (model.getNumStep() <= tgannt.getInt(\"tx\")) {");
			
		}
		for (Iterator iterator = jarray.iterator(); iterator.hasNext();)
		{
			JSONObject json = (JSONObject) iterator.next();
			String type = json.getString(TYPE);
			if (type.equals("If("))
			{
				type = IF;
			}
			if (type.equals(END_IF))
			{
				code.append("}");
			}
			else if (type.equals(ELSE))
			{
				code.append(_ELSE_);
			}
			else
			{
				String src = parser.parser(true, type.equals(IF), getterAndSetter, json.getString(CODE));
				if (kqmlIntegration)
				{
					src = transform(src);	
				}
				if (type.equals(ACTION))
				{
					code.append(src);
					if (!src.endsWith("*/"))
					{
						code.append(SEMICOLON);
					}
				}
				else if (type.equals(RETURN))
				{
					code.append(RETURN_).append(src).append(SEMICOLON);
				}
				else
				{
					code.append("if (").append(src);
					if (src.endsWith("*/"))
					{
						int index = code.lastIndexOf("/*");
						code.insert(index, ")").append("{");
					}
					else
					{
						code.append("){");
					}
				}
			}
		}
		if (kqmlIntegration)
		{
			code.append("\nagenti.receiveMessage(model, new cl.dlab.abm.core.kqml.KQMLMessage(cl.dlab.abm.core.kqml.MessageType.Update, agenti.getName(), agenti.getName(), msgi.getContent(), \"JSON\", model.getName()));\n");
			code.append("\nagentj.receiveMessage(model, new cl.dlab.abm.core.kqml.KQMLMessage(cl.dlab.abm.core.kqml.MessageType.Update, agentj.getName(), agentj.getName(), msgj.getContent(), \"JSON\", model.getName()));\n");
			code.append("}else{model.updateDataTgatnn(agenti, agentj, tgannt);}");
			code.append("\nmodel.sendDataTgatnn(agenti, agentj);\n");
		}
		
		//System.out.println(code);
		return code;
		
	}
	public static String transform(String input) {
        // 1. Verifica si es una asignación del tipo ((Agent) agentX).field = ...
        Pattern assignPattern = Pattern.compile("^\\s*\\(\\(Agent\\)\\s*(agent\\w+)\\)\\.(\\w+)\\s*=\\s*(.+)");
        Matcher assignMatcher = assignPattern.matcher(input);

        if (assignMatcher.find()) {
            String lhsAgent = assignMatcher.group(1);  // Ej: agenti
            String lhsField = assignMatcher.group(2);  // Ej: rho
            String rhs = assignMatcher.group(3);       // Expresión derecha

            String transformedRhs = transformAgentFields(rhs, lhsAgent);
            String msgVar = lhsAgent.replaceFirst("agent", "msg");

            return msgVar + ".set(\"" + lhsField + "\", " + transformedRhs + ")";
        } else {
            // Si no es asignación de ((Agent)...), solo reemplazar ocurrencias del patrón
            return transformAgentFields(input, null);
        }
    }

    private static String transformAgentFields(String input, String lhsAgent) {
        Pattern fieldPattern = Pattern.compile("\\(\\(Agent\\)\\s*(agent\\w+)\\)\\.(\\w+)");
        Matcher matcher = fieldPattern.matcher(input);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String agent = matcher.group(1);  // Ej: agenti, agentj
            String field = matcher.group(2);  // Ej: rt, conf, repu

            String replacement;
            if (lhsAgent != null && agent.equals(lhsAgent)) {
                replacement = "msgi.get(\"" + field + "\")";
            } else {
                String msgVar = agent.replaceFirst("agent", "msg");
                replacement = msgVar + ".get(\"" + field + "\")";
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
	private String getModelInitValues(ArrayList<HashMap<String,Object>> modelVariables)
	{
		StringBuilder buff = new StringBuilder(" @Override public void setValues(java.util.HashMap<String, Object> values){");
		for (HashMap<String, Object> prop : modelVariables)
		{
			Boolean rangeValue = (Boolean)prop.get("rangeValues");
			
			if (rangeValue) 
			{
				int type = (Integer)prop.get("variableTypeId");
				String propertyName = (String)prop.get("name");
				if (type == 1 || type == 2)
				{
					buff.append("this.").append(propertyName).append(" = ").append(type == 1 ? "(Integer)" : "(Double)").append("values.get(\"").append(propertyName).append("\");");
				}
				else if (type == 5)
				{
					buff.append("this.").append(propertyName).append(".setValues(").append("(Double)values.get(\"").append(propertyName).append("\"));");
				}
			}
			
		}
		return buff.append("}").toString();
	}
	private String getModelProperties(ArrayList<HashMap<String,Object>> modelVariables)
	{
		StringBuilder buff = new StringBuilder(" @Override public java.util.HashMap<String, Object> getProperties(){java.util.HashMap<String, Object> hs = super.getProperties();");
		for (HashMap<String, Object> prop : modelVariables)
		{
			Boolean rangeValue = (Boolean)prop.get("rangeValues");
			
			if (rangeValue) 
			{
				int type = (Integer)prop.get("variableTypeId");
				String propertyName = (String)prop.get("name");
				if (type == 1 || type == 2)
				{
					buff.append("hs.put(\"").append(propertyName).append("\", ").append(propertyName).append(");");
				}
				else if (type == 5)
				{
					buff.append("hs.put(\"").append(propertyName).append("\", getFunctionProperty(").append(propertyName).append("));");
				}
			}
			
		}
		return buff.append("return hs;}").toString();
	}
	private String getAgentInitValues(boolean getterAndSetter, ArrayList<HashMap<String,Object>> agentSites)
	{
		StringBuilder buff = new StringBuilder(" @Override public void setValues(cl.dlab.abm.core.model.Model _model){Model model = (Model)_model;");
		for (HashMap<String, Object> prop : agentSites)
		{
			String modelRef = (String)prop.get("modelNameRef");
			
			if (modelRef != null) 
			{
				String propertyName = (String)prop.get("name");
				if (getterAndSetter)
				{
					buff.append("this.set").append(Util.getJavaName(propertyName)).append("( model.get").append(Util.getJavaName(modelRef)).append("());");
				}
				else
				{
					buff.append("this.").append(propertyName).append(" = model.").append(modelRef).append(";");
				}
			}
			
		}
		return buff.append("}").toString();
	}
	@SuppressWarnings("unchecked")
	public String getResultUtil(HashMap<String, Object> model)
	{
		StringBuilder buff = new StringBuilder("public class ResultUtil extends cl.dlab.abm.core.simulation.ResultUtil{")
				.append("@Override protected void addStatistics(cl.dlab.abm.core.model.Data _mean, cl.dlab.abm.core.model.Data _stdDev, cl.dlab.abm.core.model.Data _median, java.util.ArrayList<cl.dlab.abm.core.model.Data> allData, int numSim, int step){")
				.append("Data meanData = (Data)_mean;Data stdDevData = (Data)_stdDev;Data medianData = (Data)_median;");

		StringBuilder properties = new StringBuilder();
		StringBuilder assign = new StringBuilder();
		StringBuilder statistics = new StringBuilder();
		StringBuilder copy = new StringBuilder();
		
		ArrayList<HashMap<String,Object>> variablesModel = (ArrayList<HashMap<String,Object>>)model.get("variables");
		
		for (HashMap<String, Object> prop : variablesModel)
		{
			if ((Boolean)prop.get("detailedAnalysis") || prop.get("generalAnalysisId") != null)
			{
				String propertyName = (String)prop.get(NAME);
				properties.append("double[] ").append(propertyName).append(" = new double[numSim];");
				assign.append(propertyName).append("[k] = data.").append(propertyName).append("[step];");
				statistics.append("meanData.").append(propertyName).append("[step] = mean = mean(").append(propertyName).append(");");
				statistics.append("stdDevData.").append(propertyName).append("[step] = std(").append(propertyName).append(", mean);");
				statistics.append("medianData.").append(propertyName).append("[step] = median(").append(propertyName).append(");\n\n");
				
				if (prop.get("generalAnalysisId") != null)
				{
					copy.append("lastData.").append(propertyName).append("[k] = data.").append(propertyName).append("[step];\n");
				}
			}
		}
		ArrayList<HashMap<String, Object>> agents = (ArrayList<HashMap<String, Object>>) model.get("agents");
		for (HashMap<String, Object> agent : agents)
		{
			ArrayList<HashMap<String,Object>> agentSites = (ArrayList<HashMap<String,Object>>)agent.get("variables");
			for (HashMap<String, Object> site : agentSites)
			{
				if ((Boolean)site.get("detailedAnalysis"))
				{
					String propertyName = (String)site.get(NAME);
					properties.append("double[] ").append(propertyName).append(" = new double[numSim];");
					assign.append(propertyName).append("[k] = data.").append(propertyName).append("[step];");					
					statistics.append("meanData.").append(propertyName).append("[step] = mean = mean(").append(propertyName).append(");");
					statistics.append("stdDevData.").append(propertyName).append("[step] = std(").append(propertyName).append(", mean);");
					statistics.append("medianData.").append(propertyName).append("[step] = median(").append(propertyName).append(");\n\n");
					
					//copy.append("lastData.").append(propertyName).append("[k] = data.").append(propertyName).append("[step];\n");
				}
			}
		}
		
		buff.append(properties).append("int k = 0;for (cl.dlab.abm.core.model.Data _data : allData){Data data = (Data)_data;")
				.append(assign).append("k++;}").append("double mean;").append(statistics).append("}");
		
		buff.append("@Override protected void addStatistics(cl.dlab.abm.core.model.Data _lastData, java.util.ArrayList<cl.dlab.abm.core.model.Data> allData, int step){")
			.append("Data lastData = (Data)_lastData;")
			.append("int k = 0;for (cl.dlab.abm.core.model.Data _data : allData){Data data = (Data)_data;")
			.append(copy).append("k++;}")
			.append("}");
		
		return buff.toString();
	}
	@SuppressWarnings("unchecked")
	public String getDataBody(boolean getterAndSetter, HashMap<String, Object> model)
	{
		StringBuilder properties = new StringBuilder();
		StringBuilder constructor = new StringBuilder();
		StringBuilder methods = new StringBuilder();
		StringBuilder assign = new StringBuilder();
		StringBuilder getProperties = new StringBuilder();
		StringBuilder lastData = new StringBuilder();
		ArrayList<HashMap<String,Object>> variablesModel = (ArrayList<HashMap<String,Object>>)model.get("variables");
		
		for (HashMap<String, Object> prop : variablesModel)
		{
			if ((Boolean)prop.get("detailedAnalysis") || prop.get("generalAnalysisId") != null)
			{
				String propertyName = (String)prop.get(NAME);
				properties.append("protected double[] ").append(propertyName).append(";");
				constructor.append("this.").append(propertyName).append("=new double[numPasos];");
				String propertyValue = "";
				int type = (Integer)prop.get("variableTypeId");
				if (type == 5) 
				{
					//propertyValue = ".getParams()[0]";
					propertyValue = ".getValue()";
				}
				if (getterAndSetter)
				{
					assign.append("this.").append(propertyName).append("[paso]=").append("model.get").append(Util.getJavaName(propertyName)).append("()").append(propertyValue).append(";");
				}
				else
				{
					assign.append("this.").append(propertyName).append("[paso]=").append("model.").append(propertyName).append(propertyValue).append(";");
				}
				getProperties.append("hs.put(\"").append(propertyName).append("\", ").append(propertyName).append(");");
				
				if (prop.get("generalAnalysisId") != null)
				{
					lastData.append("hs.put(\"").append(propertyName).append("\", ").append(propertyName).append(");");
				}
				
			}
		}
		ArrayList<HashMap<String, Object>> agents = (ArrayList<HashMap<String, Object>>) model.get("agents");
		for (HashMap<String, Object> agent : agents)
		{
			String agentName = Util.getJavaName((String)agent.get(NAME));
			ArrayList<HashMap<String,Object>> agentSites = (ArrayList<HashMap<String,Object>>)agent.get("variables");
			for (HashMap<String, Object> site : agentSites)
			{
				if ((Boolean)site.get("detailedAnalysis"))
				{
					String propertyName = (String)site.get(NAME);
					String methodName = "get" + Util.getJavaName(propertyName);
					StringBuilder agentArrayList = new StringBuilder("java.util.ArrayList<").append(agentName).append("> ");
					methods.append("private double ").append(methodName).append("(Model model){").append(agentArrayList)
							.append("agents =(").append(agentArrayList).append(")model.getAgents(\"" + agentName + "\");double result = 0;for(").append(agentName);
					if (getterAndSetter)
					{
						methods.append(" agent:agents){result+=agent.get").append(Util.getJavaName(propertyName)).append("();}return result/(double)agents.size();}");
					}
					else
					{
						methods.append(" agent:agents){result+=agent.").append(propertyName).append(";}return result/(double)agents.size();}");
					}
					
					properties.append("protected double[] ").append(propertyName).append(";");
					constructor.append("this.").append(propertyName).append("=new double[numPasos];");
					assign.append("this.").append(propertyName).append("[paso]=").append(methodName).append("(model);");
					getProperties.append("hs.put(\"").append(propertyName).append("\", ").append(propertyName).append(");");
				}
				
			}
		}
		
		return properties.append("public Data(int numPasos){super(numPasos);").append(constructor).append("}").append(methods)
				.append("@Override public java.util.HashMap<String, double[]> getProperties(){java.util.HashMap<String, double[]> hs = super.getProperties();").append(getProperties).append("return hs;}")
				.append("@Override public java.util.HashMap<String, double[]> getLastProperties(){java.util.HashMap<String, double[]> hs = super.getProperties();").append(lastData).append("return hs;}")
				.append("@Override public void addValues(int paso, cl.dlab.abm.core.model.Model _model){Model model = (Model)_model;").append(assign).append("}").toString();
	}
	private String getCloneMethod(String className, ArrayList<HashMap<String, Object>> variables)
	{
		StringBuilder buff = new StringBuilder(CLONE_METHOD).append(className).append(" obj = (").append(className).append(")super.clone();");
		for (HashMap<String, Object> prop : variables)
		{
			if ((Integer)prop.get("variableTypeId") == 5)
			{
				String propertyName = (String)prop.get(NAME);
				Integer functionId = (Integer)prop.get(FUNCTION_TYPE_ID);
				String s = functionId == 1 ? LOG_NORMAL : NORMAL;
				buff.append("obj.").append(propertyName).append(" = (").append(s).append(")this.").append(propertyName).append(".clone();");
			}
		}
		return buff.append("return obj;}").toString();
	}
	private String getClearValues(ArrayList<HashMap<String, Object>> variables)
	{
		StringBuilder buff = new StringBuilder("@Override protected void clearFunctionValues(){");
		for (HashMap<String, Object> prop : variables)
		{
			if ((Integer)prop.get("variableTypeId") == 5)
			{
				String propertyName = (String)prop.get(NAME);
				buff.append("this.").append(propertyName).append(".clear();");
			}
		}
		return buff.append("}").toString();
	}
	private Integer getInteger(Object o)
	{
		return o == null ? 0 : o instanceof String ? Integer.parseInt((String)o) : o instanceof Integer ? (Integer)o : ((Number)o).intValue(); 
	}
	private StringBuilder addKQMLIntegration(StringBuilder buff, boolean kqmlIntegration, ArrayList<HashMap<String, Object>> properties)
	{
		if (kqmlIntegration)
		{
			StringBuilder result = new StringBuilder();
			String sep = "";
			for (HashMap<String, Object> prop : properties)
			{
				if ((Boolean)prop.get("kqmlIntegration"))
				{
					result.append(sep).append("\"").append(prop.get("name")).append("\"");
					sep = ", ";
				}
			}
			buff.append("kqmlProperties= new String[]{").append(result).append("};");
		}
		return buff;
	}
	@SuppressWarnings({ "unchecked"})
	public void generateJar(HashMap<String, Object> model) throws Exception
	{
		String modelName = Util.getJavaName((String) model.get(NAME));
		Boolean value = (Boolean)model.get("synchronized");
		Boolean kqmlIntegration = (Boolean)model.get("kqmlIntegration");
		boolean getterAndSetter = value != null && value || model.get("runWith") != null;
		String packageName = getPackageName(modelName);
		ArrayList<HashMap<String, Object>> agents = (ArrayList<HashMap<String, Object>>) model.get("agents");
		ArrayList<HashMap<String, Object>> rules = (ArrayList<HashMap<String, Object>>) model.get("rules");
		ArrayList<HashMap<String, Object>> actions = (ArrayList<HashMap<String, Object>>) model.get("actions");
		ArrayList<Source> sources = new ArrayList<JavaClassGenerator.Source>();
		ObjectName modelObjectName = new ObjectName("model");
		
		JSONArray functions = new JSONArray((String)model.get(ADDITIONAL_CODE));
		ObjectName agentObjectName = new ObjectName(AGENT);
		HashMap<String, Object> alias = new HashMap<String, Object>();
		imports = new StringBuilder();
		alias.put(LOGISTIC, Util.getAlias(LOGISTIC, modelObjectName, -1));
		alias.put(NORMALIZER, Util.getAlias(NORMALIZER, modelObjectName, -1));
		alias.put("random", Util.getAlias("random()", modelObjectName, -1));
		alias.put("numInteractionAgents", Util.getAlias("getNumInteractionAgents()", modelObjectName, -1));
		alias.put("numNoInteractionAgents", Util.getAlias("getNumNoInteractionAgents()", modelObjectName, -1));
		alias.put("size", Util.getAlias("size", modelObjectName, -1));
		alias.put("newAgents", Util.getAlias("newAgents", modelObjectName, -1));
		alias.put(AGENT, Util.getAlias(AGENT, agentObjectName, -2));
		alias.put("remove", Util.getAlias("remove", agentObjectName, -1));
		alias.put("create", Util.getAlias("create", agentObjectName, -1));
		alias.put("rnd", Util.getAlias("rnd", agentObjectName, 1));
		ArrayList<HashMap<String,Object>> variablesModel = (ArrayList<HashMap<String,Object>>)model.get("variables");
		String[] properties = getProperties(alias, variablesModel, modelObjectName, getterAndSetter);
		String initModelValues = getModelInitValues(variablesModel); 
		String modelProperties = getModelProperties(variablesModel);
		String cloneMethod = getCloneMethod("Model", variablesModel);
		String clearValues = getClearValues(variablesModel);
		
		JavaLatextParser parser = new JavaLatextParser(alias);
		StringBuilder buff = new StringBuilder("Model model = (Model)_model;");
		ArrayList<Source> rulesSources = new ArrayList<JavaClassGenerator.Source>();
		StringBuilder rulesBuffer = new StringBuilder();
		HashMap<String, Integer> hsActions = new HashMap<String, Integer>();
		Collections.sort(rules, new Comparator<HashMap<String, Object>>()
		{

			@Override
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2)
			{
				
				return getInteger(o1.get("position")).compareTo(getInteger(o2.get("position")));
			}
		});
		StringBuilder modelFunctions = getFunctions(parser, alias, functions, modelObjectName, getterAndSetter, kqmlIntegration);
		
		for (HashMap<String, Object> rule : rules)
		{
			String name = Util.getJavaName((String) rule.get(NAME));
			rulesBuffer.append("addRule(new ").append(name).append("());");
			String javaLatex = parser.parser(true, getterAndSetter, (String)rule.get(CONDITIONAL_CODE));
			System.out.println(javaLatex);
			Boolean initial = (Boolean)rule.get("initial");
			if (initial == null)
			{
				initial = false;
			}
			String iterate = rule.get("iterate") == null || ((String)rule.get("iterate")).trim().length() == 0 ? "null" : "\"" + rule.get("iterate") + "\"";
			String meet = rule.get("meet") == null || ((String)rule.get("meet")).trim().length() == 0 ? "null" : "\"" + rule.get("meet") + "\"";
			StringBuilder ruleConstructor = new StringBuilder("public ").append(name).append("(){")
												.append("super.iterate = ").append(iterate).append(";")
												.append("super.meet = ").append(meet).append(";")
												.append("super.initial = ").append(initial).append(";")
												.append("super.trueAction = new ").append(Util.getJavaName((String) rule.get("trueActionName")))
												.append("();super.falseAction = new ").append(Util.getJavaName((String) rule.get("falseActionName")))
												.append("();super.beforeAction = new ").append(Util.getJavaName((String) rule.get("beforeActionName")))
												.append("();super.afterAction = new ").append(Util.getJavaName((String) rule.get("afterActionName")))
												.append("();}");
			
			StringBuilder isOk = new StringBuilder();
			int idOk;
			if (iterate.equals("null"))
			{
				idOk = 0;
				isOk.append("public boolean isOk(cl.dlab.abm.core.model.Model _model){");
			}
			else if (meet.equals("null"))
			{
				idOk = 1;
				isOk.append("public boolean isOk(cl.dlab.abm.core.model.Model _model, cl.dlab.abm.core.model.Agent agenti){");
			}
			else
			{
				idOk = 2;
				isOk.append("public boolean isOk(cl.dlab.abm.core.model.Model _model, cl.dlab.abm.core.model.Agent agenti, cl.dlab.abm.core.model.Agent agentj){");
			}
			hsActions.put((String)rule.get("trueActionName"), idOk);
			hsActions.put((String)rule.get("falseActionName"), idOk);
			hsActions.put((String)rule.get("beforeActionName"), idOk);
			hsActions.put((String)rule.get("afterActionName"), idOk);
			rulesSources.add(new Source(name, null, PACKAGE_ + packageName + PUBLIC_CLASS + name
					+ " extends cl.dlab.abm.core.model.Rule{" + ruleConstructor + isOk 
					+ buff + " return " + javaLatex + ";}}"));
		}
		
		 
		String newData = "@Override public cl.dlab.abm.core.model.Data newData(int numPasos){return new Data(numPasos);}";
		StringBuilder constructor = new StringBuilder(" public Model(){").append(addKQMLIntegration(rulesBuffer, kqmlIntegration, variablesModel)).append("}");
		sources.add(new Source(MODEL, modelObjectName, PACKAGE_ + packageName + SEMICOLON + imports + PUBLIC_CLASS_MODEL + properties[0] + properties[1] 
						+ constructor + initModelValues + modelProperties + newData + clearValues + cloneMethod));
		
		for (HashMap<String, Object> agent : agents)
		{
			String agentName = (String) agent.get(NAME);
			String name = Util.getJavaName(agentName);
			ArrayList<HashMap<String,Object>> agentSites = (ArrayList<HashMap<String,Object>>)agent.get("variables");
			imports = new StringBuilder();
			ObjectName objectName = new ObjectName("((" + name + ")agent)");

			properties = getProperties(alias, agentSites, objectName, getterAndSetter);
			cloneMethod = getCloneMethod(name, agentSites);
			String extendsFrom = (String)agent.get("extendsFrom");
			sources.add(new Source(name, objectName, PACKAGE_ + packageName + SEMICOLON + imports + " public class " + name 
					+ " extends " + (extendsFrom == null ? "cl.dlab.abm.core.model.Agent" : extendsFrom ) + " implements Cloneable { public " + name + "(){" + addKQMLIntegration(new StringBuilder(), kqmlIntegration, agentSites) + "}" 
					+ properties[0] + properties[1] + getAgentInitValues(getterAndSetter, agentSites) + cloneMethod));
		}

		modelObjectName.setName("this");
		sources.get(0).source = sources.get(0).source + modelFunctions + "}";
		modelObjectName.setName("model");
		for (int i = 1; i < sources.size(); i++)
		{
			HashMap<String, Object> agent = agents.get(i - 1);
			Source source = sources.get(i);
			String code = (String)agent.get(ADDITIONAL_CODE);
			if (code == null || code.length() == 0)
			{
				source.source = source.source  + "}";
			}
			else
			{
				JSONArray fn = new JSONArray(code);
				ObjectName objName = source.objectName;
				objName.setName(objName.getName().replace(")agent)", _THIS_));
				source.source = source.source + getFunctions(parser, alias, fn, objName, getterAndSetter, kqmlIntegration) + "}";
				objName.setName(objName.getName().replace(_THIS_, ")agent)"));
			}
		}
		sources.add(new Source("Data", new ObjectName("data"), PACKAGE_ + packageName + SEMICOLON + PUBLIC_CLASS_DATA + getDataBody(getterAndSetter, model) + "}"));
		sources.add(new Source("ResultUtil", new ObjectName("resultUtil"), PACKAGE_ + packageName + SEMICOLON + getResultUtil(model) + "}"));
		
		sources.addAll(rulesSources);
		for (HashMap<String, Object> action : actions)
		{
			String name = Util.getJavaName((String) action.get(NAME));
			String actionCode = (String)action.get(CODE);
			System.out.println("action::" + alias + "**" + actionCode);
			StringBuilder code = actionCode == null ? new StringBuilder() : getActionCode(parser, new JSONArray(actionCode), getterAndSetter, kqmlIntegration);
			StringBuilder execute = new StringBuilder();
			int idOk = hsActions.get((String) action.get(NAME));
			if (idOk == 0)
			{
				execute.append(" extends cl.dlab.abm.core.model.Action{public void execute(cl.dlab.abm.core.model.Model _model) throws Exception{");
			}
			else if (idOk == 1)
			{
				execute.append(" extends cl.dlab.abm.core.model.Action{public void execute(cl.dlab.abm.core.model.Model _model, cl.dlab.abm.core.model.Agent agenti)throws Exception{");
			}
			else
			{
				execute.append(" extends cl.dlab.abm.core.model.Action{public void execute(cl.dlab.abm.core.model.Model _model, cl.dlab.abm.core.model.Agent agenti, cl.dlab.abm.core.model.Agent agentj)throws Exception{");
			}
			sources.add(new Source(name, null, PACKAGE_ + packageName + PUBLIC_CLASS + name + execute + buff + code + "}}"));
		}
		compile(modelName, packageName, sources);

		//createPlugin(tmp, javaName, dirName);

	}
	private static void compile(String modelName, String packageName, ArrayList<Source> sources) throws Exception
	{
		try 
		{

			String path = InitializeServlet.REAL_PATH + "/jars/";
			File src = new File(path +  "/src/" + packageName.replaceAll("[.]", "/"));
			if (!src.exists())
			{
				src.mkdirs();
			}
			String classpath = path + "src/:" + InitializeServlet.REAL_PATH + "/WEB-INF/lib/abm.jar:" + InitializeServlet.REAL_PATH + "/WEB-INF/lib/json-20160212.jar";
			System.out.println(classpath);
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			ByteArrayOutputStream err = new ByteArrayOutputStream();
			Formatter formatter = new Formatter();
			for (Source source : sources)
			{
				File sourceFile = new File(src.getPath() + "/" + source.javaName + ".java");
				System.out.println("src:\n" + source.source);
				Files.write(sourceFile.toPath(), (source.source = formatter.formatSource(source.source)).getBytes(StandardCharsets.UTF_8));
			}
			for (Source source : sources)
			{
				File sourceFile = new File(src.getPath() + "/" + source.javaName + ".java");
				if (compiler.run(null, null, err, "-classpath", classpath, sourceFile.getPath()) != 0)
				{
					String msg = new String(err.toByteArray());
					int index = msg.indexOf("error:");
					int index2 = msg.indexOf("1 error", index + 6);
					if (index2 == -1)
					{
						index2 = msg.indexOf(" errors", index + 6);
					}
					msg = source.javaName + ":" + msg.substring(index + 6, index2);
					LogUtil.error(JavaClassGenerator.class, msg);
					throw new Exception(msg);
				}			
			}
			File dir = new File(path);
			String jarName = path + modelName + ".jar";
			String sourceJarName = path + modelName + "-src.jar";
			createPlugin(dir, jarName, sourceJarName, src.getAbsolutePath() + "/", sources, modelName);
		}
		catch(Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	public static byte[] downloadSourceFile(HashMap<String, Object> model) throws Exception
	{
		String path = InitializeServlet.REAL_PATH + "/jars/";
		File sourceJarFile = new File(path + Util.getJavaName((String)model.get("name")) + "-src.jar");
		if (!sourceJarFile.exists())
		{
			throw new Exception("Sources_not_found");
		}
		try(FileInputStream fi = new FileInputStream(sourceJarFile))
		{
			byte[] b = new byte[fi.available()];
			fi.read(b);
			return b;
		}
	}

	private synchronized static void createPlugin(File dir, String jarName, String sourceJarName, String sourcePath, ArrayList<Source> sources, String modelName) throws Exception
	{
		int index = sourcePath.indexOf("jars/src/");
		
		for (int i = 0; i < EXTENSIONS.length; i++)
		{

			File plugin = new File(i == 0 ? jarName : sourceJarName);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ZipOutputStream append = new ZipOutputStream(bo);
			ArrayList<String> listEntries = new ArrayList<String>();
			for (Source source : sources)
			{
				listEntries.add(sourcePath + source.javaName + EXTENSIONS[i]);
			}
	
			for (String s : listEntries)
			{
				ZipEntry e = new ZipEntry(s.substring(index + 9));
				append.putNextEntry(e);
				FileInputStream fi = new FileInputStream(s);
				byte[] b = new byte[fi.available()];
				fi.read(b);
				fi.close();
				append.write(b);
				append.closeEntry();
	
			}
	
			append.close();
			byte[] bytes = bo.toByteArray();
			FileOutputStream fo = new FileOutputStream(plugin);
			fo.write(bytes);
			fo.close();
		}
		File jar = new File(jarName);
		jar.setLastModified(System.currentTimeMillis());
		JarService.reloadJar(modelName);
		

	}

	public static void copy(InputStream input, OutputStream output) throws IOException
	{
		int bytesRead;
		while ((bytesRead = input.read(BUFFER)) != -1)
		{
			output.write(BUFFER, 0, bytesRead);
		}
	}

	public static void main_(String[] args) throws FormatterException
	{
		String s = "package cl.afoit.darta.core.darta.gen;import cl.afoit.darta.core.darta.validator.ReglaNegocioEvaluator;import cl.afoit.darta.core.darta.service.vo.ClienteVO;public class ReglaRDNQA001 implements ReglaNegocioEvaluator {@Override public Boolean getValor(ClienteVO cliente) throws Exception {return (cliente.getInt(\"NEWMITDC\") == 18163) || (cliente.getInt(\"NEWMITDC\") == 18162) || (cliente.getInt(\"NEWMITDC\") == 18161);}}";
		System.out.println(new Formatter().formatSource(s));
	}
}
