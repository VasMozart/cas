/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

/**
 * Implementation of the ServiceRegistryDao based on JPA.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public final class JpaServiceRegistryDaoImpl implements ServiceRegistryDao {

    @NotNull
    @PersistenceContext
    private EntityManager entityManager;

    public boolean delete(final RegisteredService registeredService) {
      if (this.entityManager.contains(registeredService)) {
        this.entityManager.remove(registeredService);
      } else {
        this.entityManager.remove(this.entityManager.merge(registeredService));
      }
      return true;
    }

    public List<RegisteredService> load() {
        return this.entityManager.createQuery("select r from AbstractRegisteredService r",
                RegisteredService.class).getResultList();
    }

    public RegisteredService save(final RegisteredService registeredService) {
        final boolean isNew = registeredService.getId() == -1;

        final RegisteredService r = this.entityManager.merge(registeredService);

        if (!isNew) {
          this.entityManager.persist(r);
        }

        return r;
    }

    public RegisteredService findServiceById(final long id) {
        return this.entityManager.find(AbstractRegisteredService.class, id);
    }
}