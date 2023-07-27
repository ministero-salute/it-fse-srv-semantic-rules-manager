/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.ProfileUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants.Collections.COLLECTION_SCHEMATRON;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants.Profile.TEST_PREFIX;


@Configuration
public class CollectionNaming {

    @Bean("schematronBean")
    public String getIniEdsInvocationCollection(ProfileUtility profileUtility) {
        if (profileUtility.isTestProfile()) return TEST_PREFIX + COLLECTION_SCHEMATRON;
        return COLLECTION_SCHEMATRON;
    }
}
