/*
 Copyright 2007, Lucid Technics, LLC.

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in
 writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 specific language governing permissions and limitations under the License.
*/

package com.lucidtechnics.blackboard.util;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JavascriptingUtil
   implements ScriptingUtil
{
	private static Log log = LogFactory.getLog(JavascriptingUtil.class);

	private static final Map<String, Script> scriptResourceMap = new HashMap<String, Script>();
	private Map<String, Object> bindingsMap;
	private Scriptable scope;

	private Map<String, Script> getScriptResourceMap() { return scriptResourceMap; }
	private Scriptable getScope() { return scope; }
	private Map<String, Object> getBindingsMap() { return bindingsMap; }

	public void setScope(Scriptable _scope) { scope = _scope; }
    public void setBindingsMap(Map<String, Object> _bindingsMap) { bindingsMap = _bindingsMap; }

    public JavascriptingUtil()
	{
		setBindingsMap(new HashMap<String, Object>());
    }

    public void bind(String _name, Object _value)
    {
		getBindingsMap().put(_name, _value);
	}

	public Script compileScript(String _scriptResource)
	{
		Reader reader = null;
				
		try
		{
			if (getScriptResourceMap().containsKey(_scriptResource) == false)
			{
				reader = new InputStreamReader(findScript(_scriptResource));

				Script script = Context.getCurrentContext().compileReader(reader, _scriptResource, 0, null);

				synchronized(getScriptResourceMap())
				{
					getScriptResourceMap().put(_scriptResource, script);
				}
			}
		}
		catch(Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			if (reader != null) { try { reader.close(); } catch(Throwable t) {} }
		}

		return getScriptResourceMap().get(_scriptResource);
	}
	
	public void loadScript(String _scriptResource)
	{
		try
		{
			if (Context.getCurrentContext() == null)
			{
				throw new RuntimeException("Cannot use loadScript outside of the scope of a call to executeScript.  A context must be present.");
			}

			try
			{
				compileScript(_scriptResource).exec(Context.getCurrentContext(), getScope());
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				throw new RuntimeException("Unable to execute script: " + _scriptResource + " for this reason: " + t.toString(), t);
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			log.error(t.toString());
			throw new RuntimeException(t);
		}
	}

	private byte[] convertStreamtoByteArray(InputStream _inputStream)
	{
		java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
		
		try
		{
			byte[] byteArray = new byte[1024];
			int bytesRead = 0;
			
			do
			{
				byteArrayOutputStream.write(byteArray, 0, bytesRead);
				bytesRead = _inputStream.read(byteArray);
			}
			while (bytesRead >= 0);
		}
		catch(Throwable t)
		{
			
		}

		return byteArrayOutputStream.toByteArray();
	}
	
	private InputStream findScript(String _scriptResource)
	{
		InputStream inputStream = null;
		
		try
		{
			java.io.File file = new java.io.File(_scriptResource);

			if (file.exists() == true)
			{
				inputStream = new java.io.FileInputStream(file);
			}
			else
			{
				throw new RuntimeException("Script: " + _scriptResource + " is not found");
			}
		}
		catch(Throwable t)
		{
			throw new RuntimeException(t);
		}

		return inputStream;
	}

	public Object executeScript(String _scriptResource)
	{
		Context context = createContext();

		Object result = null;

		try
		{
			setScope(new org.mozilla.javascript.ImporterTopLevel(context, false));

			for (String key: getBindingsMap().keySet())
			{
				Object object = Context.javaToJS(getBindingsMap().get(key), getScope());
				ScriptableObject.putProperty(getScope(), key, object);
			}

			result = compileScript(_scriptResource).exec(context, scope);
		}
		catch(Throwable t)
		{
			throw new RuntimeException("Unable to execute script: " + _scriptResource + " for this reason: " + t.toString(), t);
		}
		finally
		{
			context.exit();
		}

		return result;
	}

	public Object executeScript(String[] _scriptResources)
	{
		Context context = createContext();
		
		Object result = null;
		int i = 0;
		
		try
		{
			setScope(new org.mozilla.javascript.ImporterTopLevel(context, false));

			for (String key: getBindingsMap().keySet())
			{
				Object object = Context.javaToJS(getBindingsMap().get(key), getScope());
				ScriptableObject.putProperty(getScope(), key, object);
			}
			
			for (i = 0; i < _scriptResources.length; i++)
			{
				result = compileScript(_scriptResources[i]).exec(context, getScope());
			}
		}
		catch(Throwable t)
		{
			throw new RuntimeException("Unable to execute script: " + _scriptResources[i] + " for this reason: " + t.toString(), t);
		}
		finally
		{
			context.exit();
		}

		return result;
	}

	private Context createContext()
	{
		Context context = (new ContextFactory()).enterContext();
		context.setLanguageVersion(170);

		return context;
	}
}