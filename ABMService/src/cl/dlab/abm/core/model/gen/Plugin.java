package cl.dlab.abm.core.model.gen;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import cl.dlab.util.LogUtil;
import cl.dlab.util.PropertyUtil;

public class Plugin
{
	private URLClassLoader classLoader;
	public Plugin()  throws Exception
	{
		init();
	}
	private void init() throws Exception
	{
		LogUtil.debug(getClass(), "path PLGIN:", PropertyUtil.getProperty("PATH_PLUGIN"));
		File plugin = new File(PropertyUtil.getProperty("PATH_PLUGIN"));
        classLoader = new URLClassLoader(new URL[]{plugin.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
		//System.out.println("urls:" + Arrays.toString(classLoader.getURLs()));
	}
	@SuppressWarnings({ "rawtypes" })
	public Object newInstance(String className) throws Exception
	{
		Class c = Class.forName(className, true, classLoader);
        //Class c = (Class<ReglaNegocioEvaluator>)classLoader.loadClass(className);
        return c.getConstructors()[0].newInstance();
	}
	public void close() throws Exception
	{
		classLoader.close();
	}
}
