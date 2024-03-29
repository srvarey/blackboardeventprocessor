/*
* Copyright 2002-2006 Bediako George.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.lucidtechnics.blackboard.config;

public class EventConfiguration
{
	private String name;
	private String workspaceIdentifierName;

	public String getName() { return name; }
	public String getWorkspaceIdentifierName() { return workspaceIdentifierName; }

	public void setName(String _name) { name = _name; }
	public void setWorkspaceIdentifierName(String _workspaceIdentifierName) { workspaceIdentifierName = _workspaceIdentifierName; }

	public EventConfiguration() {}
}