/*
 * 2012-3 Red Hat Inc. and/or its affiliates and other contributors.
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
package org.overlord.rtgov.reports;

import org.overlord.rtgov.reports.model.Calendar;

/**
 * This interface represents the context available to the report generator.
 *
 */
public interface ReportContext {

    /**
     * This method locates a service based on the supplied
     * name.
     * 
     * @param name The name
     * @return The service, or null if not found
     */
    public Object getService(String name);
    
    /**
     * This method locates a service based on the supplied
     * type.
     * 
     * @param cls The class for the service
     * @return The service, or null if not found
     */
    public Object getService(Class<?> cls);
    
    /**
     * This method returns the named calendar configured for the
     * specified timezone. If the default calendar is required, then
     * use Calendar.DEFAULT as the name.
     * 
     * @param name The calendar name
     * @param timezone The timezone, or null if the default should be used
     * @return The business calendar
     */
    public Calendar getCalendar(String name, String timezone);
    
    /**
     * This method logs an error message.
     * 
     * @param mesg The error
     */
    public void logError(String mesg);

    /**
     * This method logs a warning message.
     * 
     * @param mesg The warning
     */
    public void logWarning(String mesg);

    /**
     * This method logs an info message.
     * 
     * @param mesg The info
     */
    public void logInfo(String mesg);

    /**
     * This method logs a debug message.
     * 
     * @param mesg The debug
     */
    public void logDebug(String mesg);

}
