package cl.dlab.abm.core.simulation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import cl.dlab.abm.core.sql.BaseSQL;
import cl.dlab.util.LogUtil;

public abstract class TaskManager
{
	private static Object ActiveProcess = new Object();
	
	private static int getCount(PreparedStatement count, String key) throws SQLException
	{
		count.setString(1, key);
		try(ResultSet rset = count.executeQuery())
		{
			rset.next();
			return rset.getInt(1);
		}
	}
	private static TaskItem getTaskItem(Connection con, String key) throws Exception
	{
		synchronized (ActiveProcess)
		{
			try(PreparedStatement stmt = con.prepareStatement("select numCombinations, executedCombinations, startTime, lastUpdate, state_id from ActiveProcess where key=?"))
			{
				stmt.setString(1, key);
				try(ResultSet rset = stmt.executeQuery())
				{
					if (rset.next())
					{
						return new TaskItem(key, rset.getInt(1), rset.getInt(2), BaseSQL.parseDate(rset.getString(3)), BaseSQL.parseDate(rset.getString(4)), rset.getInt(5));
					}
					return null;
				}
			}
		}
	}
	private static boolean updateStateProcess(String idProcess, int state) throws Exception
	{
		synchronized (ActiveProcess)
		{
			Connection con = new BaseSQL().newConnection();
			try
			{
				try(PreparedStatement count = con.prepareStatement("select count(*) from ActiveProcess where key=?"))
				{
					if (getCount(count, idProcess) > 0)
					{
						
						PreparedStatement stmt = con.prepareStatement("update ActiveProcess set state_id = ? where key=?");
						stmt.setInt(1, state);
						stmt.setString(2, idProcess);
						stmt.executeUpdate();
						con.commit();
						stmt.close();
						if (state == 3)
						{
							System.out.println("waiting canceled process:" + idProcess);
							while(true)
							{
								ActiveProcess.wait();
								if (getCount(count, idProcess) == 0)
								{
									break;
								}
							}
						}
						return true;
					}
					return false;
				}
			}
			finally
			{
				con.close();
			}
		}		
		
	}
	public static boolean saveFileProcess(String idProcess) throws Exception
	{
		return updateStateProcess(idProcess, 5);
	}
	public static boolean cancelProcess(String idProcess) throws Exception
	{
		return updateStateProcess(idProcess, 3);
	}
	public static void processCanceledSuccessfully(String idProcess) throws Exception
	{
		removeActiveProcess(idProcess, true);
	}
	protected void updateProcess(String idProcess, int numCombinations, int executedCombinations) 
	{
		try
		{
			Connection con = new BaseSQL().newConnection();
			try
			{
				synchronized (ActiveProcess)
				{
					try(PreparedStatement stmt = con.prepareStatement("update ActiveProcess set numCombinations = ? , executedCombinations = ?, lastUpdate = ? where key = ?"))
					{
						stmt.setInt(1, numCombinations);
						stmt.setInt(2, executedCombinations);
						stmt.setString(3, BaseSQL.formatDate(new Date()));
						stmt.setString(4, idProcess);
						stmt.executeUpdate();
						con.commit();
					}
				}
			}
			finally
			{
				con.close();
			}
		}
		catch(Exception e)
		{
			LogUtil.error(getClass(), e, "Error al actualizar proceso:" + idProcess);
			throw new RuntimeException(e);
		}
	}
	public static void addActiveProcess(String idProcess) throws Exception
	{
		Connection con = new BaseSQL().newConnection();
		try
		{
			synchronized (ActiveProcess)
			{
				try(PreparedStatement stmt = con.prepareStatement("insert into ActiveProcess (key, numCombinations, executedCombinations, startTime, lastUpdate, state_id) values (?, ?, ?, ?, ?, 1)"))
				{
					String s = BaseSQL.formatDate(new Date());
					stmt.setString(1, idProcess);
					stmt.setInt(2, 0);
					stmt.setInt(3, 0);
					stmt.setString(4, s);
					stmt.setString(5, s);
					stmt.executeUpdate();
					con.commit();
				}
			}
		}
		finally
		{
			con.close();
		}
	}
	public void initActiveProcess(String idProcess, int numCombinations) throws Exception
	{
		TaskItem task = getActiveProcess(idProcess);
		if (task != null)
		{
			task.init(numCombinations);
		}
	}
	public static void removeActiveProcess(String idProcess, boolean know) throws Exception
	{
		Connection con = new BaseSQL().newConnection();
		try
		{
			if (!know)
			throw new RuntimeException("Quien lo remueve");
			synchronized (ActiveProcess)
			{
				PreparedStatement stmt = con.prepareStatement("delete from ActiveProcess where key=?");
				stmt.setString(1, idProcess);
				stmt.executeUpdate();
				stmt.getConnection().commit();
				stmt.close();
				ActiveProcess.notifyAll();
			}
			
		}
		finally
		{
			con.close();
		}
	}
	public static void deleteAllActiveProcess() throws Exception
	{
		Connection con = new BaseSQL().newConnection();
		try
		{
			synchronized (ActiveProcess)
			{
				PreparedStatement stmt = con.prepareStatement("delete from ActiveProcess");
				stmt.executeUpdate();
				stmt.getConnection().commit();
				stmt.close();
				con.commit();
			}
		}
		finally
		{
			con.close();
		}
	}
	public static TaskItem getActiveProcess(String idProcess)throws Exception
	{
		if (idProcess == null)
		{
			return null;
		}
		Connection con = new BaseSQL().newConnection();
		try
		{
			return getTaskItem(con, idProcess);
		}
		finally
		{
			con.close();
		}
	}
	
	public static boolean isActiveProcess(String idProcess)throws Exception
	{
		if (idProcess == null)
		{
			return true;
		}
		Connection con = new BaseSQL().newConnection();
		try(PreparedStatement count = con.prepareStatement("select count(*) from ActiveProcess where key=? and state_id = 1"))
		{
			return getCount(count, idProcess) > 0;
		}
		finally
		{
			con.close();
		}
	}
	public static boolean isCanceledProcess(String idProcess) throws Exception
	{
		if (idProcess == null)
		{
			return false;
		}
		Connection con = new BaseSQL().newConnection();
		try
		{
			synchronized (ActiveProcess)
			{
				try(PreparedStatement stmt = con.prepareStatement("select count(*) from ActiveProcess where key=? and state_id = 3"))
				{
					return getCount(stmt, idProcess) > 0;					
				}
			}
		}
		finally
		{
			con.close();
		}
	}

	private Object lock;
	protected int endTask;
	protected int numTask;
	
	public TaskManager(int numTask)
	{
		this.endTask = 0;
		this.numTask = numTask;
		this.lock = new Object();
	}
	public void waitForEndAllTask()
	{
		synchronized (lock)
		{
			try
			{
				while(endTask < numTask)
				{
					lock.wait();
				}
				
			} 
			catch (InterruptedException e)
			{
				LogUtil.error(getClass(), e, "Error al ejecutar thread");
			}
		}		
	}
	public void endTask(Object... data) 
	{
		synchronized (lock)
		{
			addData(data);
			endTask++;
			lock.notifyAll();
		}
		((ThreadController)Thread.currentThread()).endTask();
	}
	protected abstract void addData(Object... data);
	
	public void setNumTask(int numTask)
	{
		this.numTask = numTask;
	}
	
}
