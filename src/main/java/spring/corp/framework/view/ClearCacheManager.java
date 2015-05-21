package spring.corp.framework.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpSession;

public class ClearCacheManager {

	public static List<Class<? extends IClearCacheManageable>> clazz = new ArrayList<Class<? extends IClearCacheManageable>>();
	private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private static final Lock r = rwl.readLock();
	private static final Lock w = rwl.writeLock();
	
	public ClearCacheManager(){
		
	}
	
	public static void addCache(Class<? extends IClearCacheManageable> cache){
		w.lock();
		try{
			clazz.add(cache);
		}finally{
			w.unlock();
		}
	}
	
	public static void clearCache(Class<? extends IClearCacheManageable> cache, HttpSession session){
		try {
			IClearCacheManageable clearCache = cache.newInstance();
			Method m = cache.getMethod("clearCache", new Class[]{HttpSession.class});
			m.invoke(clearCache, session);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void clearCache(HttpSession session){
		r.lock();
		try{
			for(Iterator<Class<? extends IClearCacheManageable>> it = clazz.iterator(); it.hasNext(); ){
				Class<? extends IClearCacheManageable> cache = it.next();
				clearCache(cache, session);
			}
		}finally{
			r.unlock();
		}
	}
	
	
	
}
