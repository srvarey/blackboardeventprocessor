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

package com.lucidtechnics.blackboard.examples.cocothemonkey;

@com.lucidtechnics.blackboard.Event(appName="example", workspaceName="CocoTheMonkey", name="monkey", workspaceIdentifier="name")

public class Monkey
{
	private String name;
	private boolean sleeping;
	private boolean eating;
	private boolean playing;

	public boolean getSleeping() { return sleeping; }
	public boolean getEating() { return eating; }
	public boolean getPlaying() { return playing; }
	
	public void setSleeping(boolean _sleeping) { sleeping = _sleeping; }
	public void setEating(boolean _eating) { eating = _eating; }
	public void setPlaying(boolean _playing) { playing = _playing; }
	
    public String getName() { return name; }
    public void setName(String _name) { name = _name; }

    public Monkey() {}
    
    public Monkey(String _name)
    {
		setName(_name);
		setSleeping(false);
		setEating(false);
		setPlaying(false);
    }
}