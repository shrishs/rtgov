/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.rtgov.ui.client.model;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * This bean represents a name value pair.
 */
@Portable
public class NameValuePairBean {

    private String _name=null;
    private String _value=null;

    /**
     * Constructor.
     */
    public NameValuePairBean() {
    }

    /**
     * Constructor.
     * 
     * @param name The name
     * @param value The value
     */
    public NameValuePairBean(String name, String value) {
        _name = name;
        _value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the name
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return _value;
    }

    /**
     * @param value the value
     */
    public void setValue(String value) {
        _value = value;
    }

}
