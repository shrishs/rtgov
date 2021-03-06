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
package org.overlord.rtgov.common.infinispan.service;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.infinispan.Cache;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.manager.CacheContainer;
import org.overlord.rtgov.common.service.CacheManager;

/**
 * This class represents the Infinispan implementation of the CacheManager
 * interface.
 *
 */
public class InfinispanCacheManager extends CacheManager {

    private static final Logger LOG=Logger.getLogger(InfinispanCacheManager.class.getName());

    private String _container=null;
    
    private CacheContainer _cacheContainer=null;
    
    /**
     * The default constructor.
     */
    public InfinispanCacheManager() {
    }
    
    /**
     * This method sets the JNDI name for the container resource.
     * 
     * @param jndiName The JNDI name for the container resource
     */
    public void setContainer(String jndiName) {
        _container = jndiName;
    }
    
    /**
     * This method returns the JNDI name used to obtain
     * the container resource.
     * 
     * @return The JNDI name for the container resource
     */
    public String getContainer() {
        return (_container);
    }
    
    /**
     * This method returns the cache container for the current thread.
     * 
     * @return The cache container
     */
    protected CacheContainer getCacheContainer() {
        if (_cacheContainer == null) {
            _cacheContainer = org.overlord.rtgov.common.infinispan.InfinispanManager.getCacheContainer(_container);
        }
        
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("Returning cache container [container="+_container+"] = "+_cacheContainer);
        }
        
        return (_cacheContainer);
    }
    
    /**
     * {@inheritDoc}
     */
    public <K,V> Map<K,V> getCache(String name) {
        CacheContainer container=getCacheContainer();
        
        if (container == null) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Requested cache '"+name
                        +"', but no cache container ("+_container+")");
            }
            return (null);
        }
        
        Map<K,V> ret=container.<K,V>getCache(name);
        
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Returning cache '"+name
                    +"' = "+ret);
        }

        return (ret);
    }

    /**
     * {@inheritDoc}
     */
    public boolean lock(String cacheName, Object key) {

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("About to lock: "+cacheName+" key="+key);
        }
        
        CacheContainer container=getCacheContainer();
        
        if (container != null) {
            Cache<Object,Object> cache=container.getCache(cacheName);
            
            if (cache != null) {
                
                // Check if cache is transactional
                if (cache.getAdvancedCache().getCacheConfiguration().
                            transaction().transactionMode() != TransactionMode.TRANSACTIONAL) {
                    if (LOG.isLoggable(Level.FINEST)) {
                        LOG.finest("Not transactional, so returning true");
                    }

                    return true;
                }

                boolean ret=cache.getAdvancedCache().lock(key);
                
                if (LOG.isLoggable(Level.FINEST)) {
                    LOG.finest("Lock '"+cacheName
                        +"' key '"+key+"' = "+ret);
                }

                return (ret);
                
            } else if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Cannot lock cache '"+cacheName
                        +"' key '"+key+"' as cache does not exist");
            }
        } else if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("Cannot lock cache '"+cacheName
                    +"' key '"+key+"' as no container");
        }
        
        return false;
    }

}
