package cl.dlab.abm.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import cl.dlab.abm.core.model.gen.JarService;
import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

@Path("/restServices")
public class RestServices
{
	protected static final String DESARROLLO = "DESARROLLO";
	protected static final String MODOAPP = "MODOAPP";

	private static final char aacute = (char) 225;
	private static final char eacute = (char) 233;
	private static final char iacute = (char) 237;
	private static final char oacute = (char) 243;
	private static final char uacute = (char) 250;
	private static final char ntilde = (char) 241;
	
	@Context
	private HttpServletRequest servletRequest;
	@Context
	private HttpServletResponse servletResponse;
	
	
	@SuppressWarnings("unchecked")
	protected HashMap<String, Object> getUser()
	{
		
		if (servletRequest.getSession().getAttribute("user-connected") != null)
		{
			return (HashMap<String, Object>) servletRequest.getSession().getAttribute("user-connected");
		}
		
		return null;
	}

	protected String getUserLanguage()
	{
		if (servletRequest.getSession().getAttribute("user-language") != null)
		{
			return (String) servletRequest.getSession().getAttribute("user-language");
		}
		return null;
	}

	protected String getUserBanca()
	{
		if (servletRequest.getSession().getAttribute("user-banca-connected") != null)
		{
			return (String) servletRequest.getSession().getAttribute("user-banca-connected");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected HashMap<String, Object> getInput(HashMap<String, Object> input)
	{
		if (input != null)
		{
			HashMap<String, Object> usr = getUser();
			/*if (input.containsKey("usuario"))
			{
				return input;
			}*/
			input.put("tmp-usuario", usr == null ? input.get("usuario") : usr.get("id"));
			for (String key : input.keySet())
			{
				if (input.get(key) instanceof HashMap)
				{
					((HashMap<String, Object>) input.get(key)).put("tmp-usuario", input.get("tmp-usuario"));
				}
			}
		}
		return input;
	}	

	private JSONObject getJSonFile(String fileName) throws Exception
	{
		FileInputStream in = new FileInputStream(servletRequest.getSession().getServletContext().getRealPath(fileName));
		byte[] b = new byte[in.available()];
		in.read(b);
		in.close();
		return new JSONObject(new String(b));
	}
	private Properties _getScreen() throws Exception
	{
		Properties prop = new Properties();
		String lang = getUserLanguage();
		if (lang == null)
		{
			lang = servletRequest.getParameter("lang");
		}
		if (lang == null || lang.equals("undefined"))
		{
			lang = PropertyUtil.getProperty("DEFAULT_LANGUAGE");
		}
		System.out.println("_getScreen:" + lang);
		prop.load(new FileInputStream(PropertyUtil.BASE + "/data/screen." + lang + ".properties"));
		prop.setProperty("CURRENT_LANGUAGE", lang);
		servletRequest.getSession().setAttribute("user-language", lang);
		return prop;
	}
	private JSONArray getJSonArrayFromFile(String fileName) throws Exception
	{
		FileInputStream in = new FileInputStream(servletRequest.getSession().getServletContext().getRealPath(fileName));
		byte[] b = new byte[in.available()];
		in.read(b);
		in.close();
		return new JSONArray(new String(b));
	}
	@GET
	@Path("reloadJar")
	public void reloadJar()
	{
		try
		{
			String jarName = servletRequest.getParameter("jarName");
			LogUtil.info(getClass(), "Entra a reloadJar:", jarName);
			JarService.reloadJar(jarName);
		}
		catch(Exception e)
		{
			LogUtil.error(this.getClass(), e, "reloadJar exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	@GET
	@Path("getCurrentUser")
	@Produces("application/json")
	public HashMap<String, Object> getCurrentUser()
	{
		try
		{
			return getUser();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "getCurrentUser exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	@POST
	@Path("test")
	@Produces("application/json")
	public void test()
	{
		try
		{
			System.out.println(servletRequest.getParameter("code"));
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "test exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@GET
	@Path("getScreen")
	@Produces("application/json")
	public String getScreen()
	{
		try
		{
			String lang = servletRequest.getParameter("lang");
			if (lang != null && !lang.equals("undefined"))
			{
				servletRequest.getSession().setAttribute("user-language", lang);
			}
			return new JSONObject(_getScreen()).toString();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "getScreen exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@GET
	@Path("obtieneCondicionesIniciales")
	@Produces("application/json")
	public String obtieneCondicionesIniciales()
	{
		try
		{
			Properties screen = _getScreen();
			JSONObject info = getJSonFile("data/info.json");
			;
			JSONObject json = new JSONObject();
			HashMap<String, Object> user = getUser();
			HashMap<Integer, HashMap<Integer, HashMap<String, Object>>> funciones = user == null
			        ? new HashMap<Integer, HashMap<Integer, HashMap<String, Object>>>()
			        : new BusinessService().obtenerAccionesxFuncionRol(user);
			json.put("accionesxFuncion", new JSONObject(funciones));
			servletRequest.getSession().setAttribute("user-accionesxfuncion", funciones);

			if (servletRequest.getSession().getAttribute("xuser-acciones") == null)
			{
				ArrayList<HashMap<String, Object>> list = new BusinessService()
				        .obtenerAcciones(new HashMap<String, Object>());
				HashMap<String, Integer> hsAcciones = new HashMap<>();
				for (HashMap<String, Object> hs : list)
				{
					hsAcciones.put((String) hs.get("descripcion"), (Integer) hs.get("id"));
				}
				servletRequest.getSession().setAttribute("user-acciones", new JSONObject(hsAcciones));
			}
			json.put("acciones", servletRequest.getSession().getAttribute("user-acciones"));
			BufferedReader br = new BufferedReader(new FileReader(PropertyUtil.BASE + "abm.config"));
			String line;
			JSONObject prop = new JSONObject();
			while((line = br.readLine()) != null)
			{
				if (line.trim().length() == 0 || line.charAt(0) == '#')
					continue;
				String[] t = line.split("=");
				if (t.length > 1) {
					prop.put(t[0], t[1]);
				}
				
			}
			br.close();
			json.put("SCREEN", screen);
			json.put("properties", prop);
			json.put("info", info);
			json.put("user", user);
			json.put("userBanca", getUserBanca());
			json.put("currentLanguage", getUserLanguage());
			System.out.println("getcondiniciales idUser:" + (getUser() == null ? "NULL" : getUser().get("id")));
			return json.toString();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "obtieneCondicionesIniciales exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	
	@SuppressWarnings("unchecked")
	@POST
	@Path("setCurrentUser")
	@Produces("application/json")
	public HashMap<String, Object> setCurrentUser(HashMap<String, Object> input)
	{
		try
		{
			HashMap<String, Object> usr = new BusinessService().validaUsuario((HashMap<String, Object>) input.get("user"));
			LogUtil.debug(this.getClass(), "asignando usuario:", input.get("user"));
			servletRequest.getSession().setAttribute("user-connected", usr); 
			servletRequest.getSession().setAttribute("user-banca-connected", input.get("userBanca"));
			// System.out.println("setuser" + getUser());jmc
			usr.put("idBanca", input.get("idBanca"));
			HashMap<String, Object> _usr = new HashMap<String, Object>();
			_usr.put("id", usr.get("id"));
			_usr.put("idRol", usr.get("idRol"));
			
			HashMap<String, Object> datosUsuario = new HashMap<String, Object>();
			datosUsuario.put("userBanca", input.get("userBanca"));
			datosUsuario.put("user", _usr);
			usr.put("datosUsuario", datosUsuario);
						
			obtieneCondicionesIniciales();
			JSONArray menues = getJSonArrayFromFile("data/sections.V2.json");
			HashMap<Integer, HashMap<Integer, HashMap<String, Object>>> accionesxFuncion = obtenerAccionesxFuncionRol(usr);
			removeMenuesxFunction(menues, accionesxFuncion, "subItems");
			usr.put("menues", menues.toString());
			usr.put("accionesxFuncion", accionesxFuncion);
			return usr;
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "setCurrentUser exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@POST
	@Path("saveUserConfiguration")
	@Produces("application/json")
	public HashMap<String, Object> saveUserConfiguration(HashMap<String, Object> input)
	{
		try
		{
			return (HashMap<String, Object>) new BusinessService().saveUserConfiguration(input);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "saveUserConfiguration exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("changePassword")
	@Produces("application/json")
	public HashMap<String, Object> changePassword(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().changePassword((HashMap<String, Object>) input.get("user"));
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "changePassword exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	

	private void removeMenuesxFunction(JSONArray menues, HashMap<Integer, HashMap<Integer, HashMap<String, Object>>> functions, String propertyItems)
	{
		ArrayList<Integer> removed = new ArrayList<>();
		int index = 0;
		for (Object item : menues)
		{
			JSONObject obj = (JSONObject) item;
			boolean remove = false;
			if (obj.has("id"))
			{
				Integer id = (Integer) obj.get("id");
				if (id != null && !functions.containsKey(id))
				{
					removed.add(index);
					remove = true;
				}
			}
			if (!remove && obj.has(propertyItems))
			{
				JSONArray submenu = obj.getJSONArray(propertyItems);
				removeMenuesxFunction(submenu, functions, propertyItems);
				if (submenu.length() == 0)
				{
					removed.add(index);
					remove = true;
				}
			}
			index++;
		}
		for (int i = removed.size() - 1; i >= 0; i--)
		{
			menues.remove(removed.get(i));
		}
	}	
	@POST
	@Path("obtenerAccionesxFuncionRol")
	@Produces("application/json")
	public HashMap<Integer, HashMap<Integer, HashMap<String, Object>>> obtenerAccionesxFuncionRol(
	        HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().obtenerAccionesxFuncionRol(getInput(input));

		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "obtenerAccionesxFuncionRol exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("consultarRoles")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> consultarRoles()
	{
		try
		{
			return (ArrayList<HashMap<String, Object>>) new BusinessService().consultarRoles(new HashMap<String, Object>()).get("listData");
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "consultarRoles exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("eliminarRol")
	@Produces("application/json")
	public HashMap<String, Object> eliminarRol(HashMap<String, Object> input)
	{
		try
		{
			new BusinessService().eliminarRol(getInput(input));
			return new HashMap<>();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "eliminarRol exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("guardarRol")
	@Produces("application/json")
	public HashMap<String, Object> guardarRol(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().guardarRol(getInput(input));

		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "guardarRol exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("consultaDetalleRol")
	@Produces("application/json")
	public HashMap<String, Object> consultaDetalleRol(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().consultaDetalleRol(getInput(input));

		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "consultaDetalleRol exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("consultarUsuarios")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> consultarUsuarios()
	{
		try
		{
			return (ArrayList<HashMap<String, Object>>) new BusinessService()
			        .consultarUsuarios(getInput(new HashMap<String, Object>())).get("listData");
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "consultarUsuarios exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("eliminarUsuario")
	@Produces("application/json")
	public HashMap<String, Object> eliminarUsuario(HashMap<String, Object> input)
	{
		try
		{
			new BusinessService().eliminarUsuario(getInput(input));
			return new HashMap<>();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "eliminarUsuario exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("guardarUsuario")
	@Produces("application/json")
	public HashMap<String, Object> guardarUsuario(HashMap<String, Object> input)
	{
		try
		{
			new BusinessService().guardarUsuario(getInput(input));
			return new HashMap<>();

		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "guardarUsuario exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	

	@GET
	@Path("tableTypeQuery")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> tableQuery()
	{
		try
		{
			return new BusinessService().tableTypeQuery();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "tableTypeQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	@POST
	@Path("tableQuery")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> tableQuery(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().tableQuery(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "tableQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("tableDelete")
	@Produces("application/json")
	public HashMap<String, Object> tableDelete(HashMap<String, Object> input)
	{
		try
		{
			new BusinessService().tableDelete(getInput(input));
			return new HashMap<>();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "tableDelete exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("tableSave")
	@Produces("application/json")
	public HashMap<String, Object> tableSave(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().tableSave(getInput(input));

		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "tableSave exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	
	@POST
	@Path("tableDetailQuery")
	@Produces("application/json")
	public HashMap<String, Object> tableDetailQuery(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().tableDetailQuery(getInput(input));

		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "tableDetailQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	

	@POST
	@Path("modelQuery")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> modelQuery(HashMap<String, Object> input)
	{
		try
		{
			if (input == null || input.get("datosUsuario") == null)
			{
				throw new Exception("Por_favor_conectese_nuevamente");
			}
			return new BusinessService().modelQuery(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "modelQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	@POST
	@Path("activeModelQuery")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> activeModelQuery(HashMap<String, Object> input)
	{
		try
		{
			input.put("query_id", "activate");
			input.put("activate", true);
			return modelQuery(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "activeModelQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	@POST
	@Path("modelDelete")
	@Produces("application/json")
	public HashMap<String, Object> modelDelete(HashMap<String, Object> input)
	{
		try
		{
			new BusinessService().modelDelete(getInput(input));
			return new HashMap<>();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "modelDelete exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	@POST
	@Path("modelSave")
	@Produces("application/json")
	public HashMap<String, Object> modelSave(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().modelSave(getInput(input));

		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "modelSave exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}

	
	@POST
	@Path("modelDetailQuery")
	@Produces("application/json")
	public HashMap<String, Object> modelDetailQuery(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().modelDetailQuery(getInput(input));

		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "modelDetailQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	

	
	
	@POST
	@Path("downloadSourceFile")
	@Produces("application/json")
	public void downloadSourceFile(HashMap<String, Object> input)
	{
		try
		{
			byte[] bytes = new BusinessService().downloadSourceFile(getInput(input));
			servletResponse.setContentType("application/zip");
			servletResponse.setContentLength(bytes.length);
			servletResponse.setHeader("Content-disposition", "attachment; filename=source.jar");
			servletResponse.getOutputStream().write(bytes);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "downloadSourceFile exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).status(405).build());
		}
	}		
	
	
	@POST
	@Path("simulationsQuery")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> simulationsQuery(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().simulationsQuery(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "simulationsQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@POST
	@Path("simulationDelete")
	@Produces("application/json")
	public HashMap<String, Object> simulationDelete(HashMap<String, Object> input)
	{
		try
		{
			new BusinessService().simulationDelete(getInput(input));
			return new HashMap<>();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "simulationDelete exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@POST
	@Path("simulationSave")
	@Produces("application/json")
	public HashMap<String, Object> simulationSave(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().simulationSave(getInput(input));
	
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "simulationSave exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	
	@POST
	@Path("simulationScenaryQuery")
	@Produces("application/json")
	public HashMap<String, Object> simulationScenaryQuery(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().simulationScenaryQuery(getInput(input));
	
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "simulationScenaryQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	
	
	@POST
	@Path("simulationDetailQuery")
	@Produces("application/json")
	public HashMap<String, Object> simulationDetailQuery(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().simulationDetailQuery(getInput(input));
	
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "simulationDetailQuery exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	
	@POST
	@Path("simulate")
	@Produces("application/json")
	public HashMap<String, Object> simulate(HashMap<String, Object> input)
	{
		try
		{
			return new BusinessService().simulate(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "consultarUsuarios exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	@POST
	@Path("cancelSimulation")
	@Produces("application/json")
	public HashMap<String, Object> cancelSimulation(HashMap<String, Object> input)
	{
		try
		{
			
			return new BusinessService().cancelSimulation(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "consultarUsuarios exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@POST
	@Path("getProgressSimulation")
	@Produces("application/json")
	public HashMap<String, Object> getProgressSimulation(HashMap<String, Object> input)
	{
		try
		{
			
			return new BusinessService().getProgressSimulation(input);
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "getProgressSimulation exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}
	
	@POST
	@Path("getAnalytics")
	@Produces("application/json")
	public String getAnalytics(HashMap<String, Object> input)
	{
		long t = System.currentTimeMillis();
		try
		{
			
			return new BusinessService().getAnalytics(input);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "getAnalytics exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
		finally
		{
			System.out.println("Tiempo total usado en analisis:" + (System.currentTimeMillis() - t));
		}
	}
	@POST
	@Path("getAnalysisResults")
	@Produces("application/json")
	public ArrayList<HashMap<String, Object>> getAnalysisResults(HashMap<String, Object> input)
	{
		long t = System.currentTimeMillis();
		try
		{
			
			return new BusinessService().getAnalysisResults(input);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "getAnalysisResults exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
		finally
		{
			System.out.println("Tiempo total usado en getAnalysisResults:" + (System.currentTimeMillis() - t));
		}
	}
	@POST
	@Path("deleteAnalysisResult")
	@Produces("application/json")
	public void deleteAnalysisResult(HashMap<String, Object> input)
	{
		long t = System.currentTimeMillis();
		try
		{
			
			new BusinessService().deleteAnalysisResult(input);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "deleteAnalysisResult exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
		finally
		{
			System.out.println("Tiempo total usado en deleteAnalysisResult:" + (System.currentTimeMillis() - t));
		}
	}
	@POST
	@Path("getAnalysisResultContent")
	@Produces("application/json")
	public String getAnalysisResultContent(HashMap<String, Object> input)
	{
		long t = System.currentTimeMillis();
		try
		{
			
			return new BusinessService().getAnalysisResultContent(input);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "getAnalysisResultContent exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
		finally
		{
			System.out.println("Tiempo total usado en getAnalysisResultContent:" + (System.currentTimeMillis() - t));
		}
	}
	
	
	@POST
	@Path("downloadSimulation")
	@Produces("application/json")
	public void downloadSimulation(HashMap<String, Object> input)
	{
		try
		{
			input.put("screen", _getScreen());
			new BusinessService().downloadSimulation(getInput(input));
			input.remove("screen");
			byte[] bytes = (byte[]) input.remove("zipFile");
			servletResponse.setContentType("application/zip");
			servletResponse.setContentLength(bytes.length);
			servletResponse.setHeader("Content-disposition", "attachment; filename=export.zip");
			servletResponse.getOutputStream().write(bytes);
		}
		catch (Throwable e)
		{
			LogUtil.error(this.getClass(), e, "downloadSimulation exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	
	@SuppressWarnings("unchecked")
	@POST
	@Path("consultarMultiplesParametros")
	@Produces("application/json")
	public HashMap<String, Object> consultarMultiplesParametros(HashMap<String, Object> input)
	{
		try
		{
			ArrayList<String> idProperties = (ArrayList<String>) input.get("idProperties");
			ArrayList<String> codigos = (ArrayList<String>) input.get("codigoPadre");
			HashMap<String, Object> result = new HashMap<String, Object>();
			if (codigos.remove("FUNCIONES"))
			{
				result.put("FUNCIONES", new BusinessService().consultaFunciones());
			}
			if (codigos.remove("TABLE_TYPES"))
			{
				ArrayList<HashMap<String, Object>> all = new BusinessService().tableTypeQuery();
				ArrayList<HashMap<String, Object>> tables = new ArrayList<HashMap<String,Object>>();
				for (HashMap<String, Object> hs : all) {
					if ((Boolean)hs.get("valid")) {
						tables.add(hs);
					}
				}
				result.put("TABLE_TYPES", tables);
			}
			if (codigos.remove("ALL_TABLES"))
			{
				result.put("ALL_TABLES", new BusinessService().allTablesQuery());
			}
			if (codigos.remove("VARIABLE_TYPES"))
			{
				result.put("VARIABLE_TYPES", new BusinessService().variableTypeQuery(new HashMap<String, Object>()));
			}
			if (codigos.remove("FUNCTION_TYPES"))
			{
				result.put("FUNCTION_TYPES", new BusinessService().functionTypeQuery(new HashMap<String, Object>()));
			}
			if (codigos.remove("GRAPH_TYPES"))
			{
				result.put("GRAPH_TYPES", new BusinessService().graphTypeQuery(new HashMap<String, Object>()));
			}
			if (codigos.remove("GRAPH_TOPOLOGYS"))
			{
				result.put("GRAPH_TOPOLOGYS", new BusinessService().graphTopologysQuery(new HashMap<String, Object>()));
			}
			if (codigos.remove("GRAPH_ALGORITHMS"))
			{
				result.put("GRAPH_ALGORITHMS", new BusinessService().graphAlgorithmsQuery(new HashMap<String, Object>()));
			}
			if (codigos.remove("GENERAL_ANALYSIS_TYPE"))
			{
				result.put("GENERAL_ANALYSIS_TYPE", new BusinessService().generalAnalysisTypeQuery(new HashMap<String, Object>()));
			}
			if (codigos.remove("SIMULATION_STATE"))
			{
				result.put("SIMULATION_STATE", new BusinessService().simulationStateQuery(new HashMap<String, Object>()));
			}

			if (codigos.remove("ANALYSIS_METHODS"))
			{
				result.put("ANALYSIS_METHODS", new BusinessService().analysisMethodsQuery(new HashMap<String, Object>()));
			}
			
			if (codigos.remove("ACTIVE_MODELS"))
			{
				result.put("ACTIVE_MODELS", activeModelQuery(input));
			}
			
			if (codigos.remove("ROLES"))
			{
				result.put("ROLES",
				        new BusinessService().consultarRoles(new HashMap<String, Object>()).get("listData"));
			}
			
			
			if (idProperties != null)
			{
				HashMap<String, Object> properties = new HashMap<String, Object>();
				for (String id : idProperties)
				{
					properties.put(id, PropertyUtil.getId(id));
				}
				result.put("idProperties", properties);
			}
			return result;
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "consultarMultiplesParametros exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}
	}	
	private HashMap<String, Object> getFileItem(HttpServletRequest request)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		// boolean isMultipart = ServletFileUpload.isMultipartContent(new
		// ServletRequestContext(request));
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// System.out.println("getFileItem: " + isMultipart);
		try
		{
			List<FileItem> items = upload.parseRequest(request);
			LogUtil.debug(this.getClass(), "filesize:", items.size());
			for (FileItem file : items)
			{
				LogUtil.debug(this.getClass(), "file:", file.getFieldName(), "**", file.isFormField());
				if (file.isFormField())
				{
					result.put(file.getFieldName(), file.getString());
					//System.out.println(result);
				}
				else
				{
					result.put("file", file);
				}
				LogUtil.debug(this.getClass(), "File:", file.getName(), ", ", file.getContentType());
				LogUtil.debug(this.getClass(), "type:", file.getContentType());
				LogUtil.debug(this.getClass(), "filename:", file.getName());
			}
			return result;
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "getFileItem exception");
			return null;
		}
	}	
	private static String getHtmlName(String s)
	{
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
			case aacute:
				buff.append("a");
				break;
			case eacute:
				buff.append("e");
				break;
			case iacute:
				buff.append("i");
				break;
			case oacute:
				buff.append("o");
				break;
			case uacute:
				buff.append("u");
				break;
			case ntilde:
				buff.append("n");
				break;
			case ' ':
				buff.append("_");
				break;
			case '.':
			case '_':
			case '-':
			case '@':
				buff.append(c);
				break;

			default:
				if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
				{
					buff.append(c);
				}
				break;
			}
		}
		return buff.toString();
	}	
	private long getNumRowsTxt(byte[] bytes) throws IOException
	{
		long n = 0;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes))))
		{
			br.readLine();
			while((br.readLine()) != null)
			{
				n++;
			}
		}
		
		return n;
	}
	
	protected long getNumRows(byte[] bytes, boolean isTxt) throws Exception
	{
		return getNumRowsTxt(bytes);
	}
	
	private byte[] readFile(FileItem file) throws IOException
	{
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		InputStream is = file.getInputStream();
		while ((len = is.read(buffer)) > -1)
		{
			bo.write(buffer, 0, len);
		}
		bo.flush();
		bo.close();
		return bo.toByteArray();
	}	
	@POST
	@Path("uploadFile")
	public void uploadFile()
	{
		try
		{
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("isOk", false);
			
			if (ServletFileUpload.isMultipartContent(servletRequest))
			{
				HashMap<String, Object> hs = getFileItem(servletRequest);
				FileItem uploadItem = (FileItem) hs.get("file");
				String fileName = getHtmlName(uploadItem.getName());
				result.put("fileName", fileName);
				byte[] bytes = readFile(uploadItem);
				System.out.println("uploadfile:"+ fileName + "**" + bytes.length);
				String modoApp = PropertyUtil.getProperty(MODOAPP);
				if (modoApp != null && modoApp.equals(DESARROLLO)) 
				{
					try(FileOutputStream fo = new FileOutputStream(fileName))
					{
						fo.write(bytes);
					}
				}
				String additionalData = (String)hs.get("additionalData");
				//System.out.println("additionalData" + additionalData);
				boolean isTxtReadRows = false;
				if (additionalData != null) {
					if (((isTxtReadRows = additionalData.startsWith("readRowsTxt")) || additionalData.startsWith("readRows")))
					{
						String[] t = additionalData.split(":");
						System.out.println(additionalData);
						if (t.length > 1)
						{
							result.put("data", getNumRows(bytes, isTxtReadRows));
						}
					}
				}
				servletRequest.getSession().setAttribute(fileName, bytes);
				if (fileName.endsWith(".txt") && !isTxtReadRows)
				{
					result.put("data", new String(bytes));
				}

			}
			servletResponse.getWriter().write(new JSONObject(result).toString());
			servletResponse.flushBuffer();
		}
		catch (Exception e)
		{
			LogUtil.error(this.getClass(), e, "uploadFile exception");
			throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
		}

	}	
	@SuppressWarnings("unused")
	private byte[] getFile(String fileName) throws IOException
	{
		byte[] bytes = (byte[]) servletRequest.getSession().getAttribute(fileName);
		if (bytes == null)
		{
			try(FileInputStream fi = new FileInputStream(fileName))
			{
				bytes = new byte[fi.available()];
				fi.read(bytes);
			}
		}
		return bytes;
	}	

		
	@SuppressWarnings("unchecked")
	protected void validaModoApp(HashMap<String, Object> input) throws IOException
	{
		//System.out.println("veamos.." + getUser());
		if (getUser() != null) {
			return;
		}
		String modoApp = PropertyUtil.getProperty(MODOAPP);
		if (modoApp != null && modoApp.equals(DESARROLLO)) 
		{
			HashMap<String, Object> datosUsuario = (HashMap<String, Object>)input.get("datosUsuario");
			//System.out.println("datos del usuario:" + datosUsuario);
			if (datosUsuario == null)
			{
				return;
			}
			servletRequest.getSession().setAttribute("user-connected", datosUsuario.get("user"));
			servletRequest.getSession().setAttribute("user-banca-connected", datosUsuario.get("userBanca"));
		}
		
	}	
}
