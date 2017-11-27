/*
 * Copyright 2015-2016 USEF Foundation
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

package energy.usef.agr.config;

import energy.usef.core.config.AbstractConfig;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ejb.LockType.READ;

/**
 * Property config class.
 *
 */
@ApplicationScoped
@Singleton
@Lock(READ)
public class ConfigAgr extends AbstractConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAgr.class);

    /**
     * Default constructor.
     */
    public ConfigAgr() {
        try {
            readProperties();
        } catch (IOException e) {
            LOGGER.error("Error while loading the properties: {}.", e.getMessage(), e);
        }
    }

    /**
     * Initialize a bean after the instance has been constructed.
     */
    @PostConstruct
    public void initBean() {
        startConfigWatcher();
    }

    /**
     * Clean up the bean before destroying this instance.
     */
    @PreDestroy
    public void cleanupBean() {
        stopConfigWatcher();
    }

    /**
     * Gets a property value as a {@link String}.
     *
     * @param configParam the configuration parameter
     * @return property value
     */
    public String getProperty(ConfigAgrParam configParam) {
        if (properties == null) {
            return null;
        }
        return properties.getProperty(configParam.name());
    }

    /**
     * Gets a property value as an {@link Integer}.
     *
     * @param configParam the configuration parameter
     * @return property value
     */
    public Integer getIntegerProperty(ConfigAgrParam configParam) {
        return Integer.parseInt(properties.getProperty(configParam.name()));
    }

    /**
     * Gets a property value as a {@link boolean}.
     *
     * @param configParam the configuration parameter
     * @return property value
     */
    public boolean getBooleanProperty(ConfigAgrParam configParam) {
        return Boolean.parseBoolean(properties.getProperty(configParam.name()));
    }
}
