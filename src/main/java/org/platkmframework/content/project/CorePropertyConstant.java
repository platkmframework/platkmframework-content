/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.content.project;

public interface CorePropertyConstant {

	/** SERVER **/
	public static final String ORG_PLATKMFRAMEWORK_SERVER_APPNAME		= "org.platkmframework.server.appname";
	//public static final String ORG_PLATKMFRAMEWORK_SECURITY_TYPE  		= "org.platkmframework.security.type";
	
	public static final String ORG_PLATKMFRAMEWORK_SERVER_PROTOCOL		= "org.platkmframework.server.protocol";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_NAME 			= "org.platkmframework.server.name";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_PORT 			= "org.platkmframework.server.port";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_MINTHREADS	= "org.platkmframework.server.minthreads";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_IDLETIMEOUT	= "org.platkmframework.server.idletimeout";
	
	
	public static final String ORG_PLATKMFRAMEWORK_SERVER_MAXTHREADS		= "org.platkmframework.server.maxthreads";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_ACTIVE_QUEUE_POOL = "org.platkmframework.server.active.queue.pool";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_PUBLIC_PATH 		= "org.platkmframework.server.public.paths";
	public static final String ORG_PLATKMFRAMEWORK_SERVER_STOPKEY 			= "org.platkmframework.server.stopkey";

	/** SECURITY**/
	public static final String ORG_PLATKMFRAMEWORK_SERVER_SECURITY_TYPE	= "org.platkmframework.server.security.type";
	
	public static final String ORG_PLATKMFRAMEWORK_CONTENT_PATH 		= "org.platkmframework.content.path";
	public static final String ORG_PLATKMFRAMEWORK_SERVLET_PLATH 		= "org.platkmframework.servlet.path";

	
	public static final String System_Access_Control_Allow_Origin		= "System_Access-Control-Allow-Origin";
	public static final String System_Access_Control_Allow_Methods		= "System_Access-Control-Allow-Methods";
	public static final String System_Access_Control_Allow_Headers		= "System_Access-Control-Allow-Headers";
	
	public static final String ORG_PLATKMFRAMEWORK_CONFIGURATION_INDEX_PAGE	= "org.platkmframework.server.index";
	
}
