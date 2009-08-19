/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005-2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.hornetq.core.server.impl;

import java.util.concurrent.ScheduledExecutorService;

import org.hornetq.core.filter.Filter;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.Queue;
import org.hornetq.core.server.QueueFactory;
import org.hornetq.core.settings.HierarchicalRepository;
import org.hornetq.core.settings.impl.AddressSettings;
import org.hornetq.utils.SimpleString;

/**
 *
 * A QueueFactoryImpl
 *
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * @author <a href="ataylor@redhat.com">Andy Taylor</a>
 *
 */
public class QueueFactoryImpl implements QueueFactory
{
   private final HierarchicalRepository<AddressSettings> addressSettingsRepository;

   private final ScheduledExecutorService scheduledExecutor;

   /** This is required for delete-all-reference to work correctly with paging, and controlling global-size */
   private PostOffice postOffice;

   private final StorageManager storageManager;

   public QueueFactoryImpl(final ScheduledExecutorService scheduledExecutor,
                           final HierarchicalRepository<AddressSettings> addressSettingsRepository,
                           final StorageManager storageManager)
   {
      this.addressSettingsRepository = addressSettingsRepository;

      this.scheduledExecutor = scheduledExecutor;

      this.storageManager = storageManager;
   }

   public void setPostOffice(final PostOffice postOffice)
   {
      this.postOffice = postOffice;
   }

   public Queue createQueue(final long persistenceID,
                            final SimpleString address,
                            final SimpleString name,
                            final Filter filter,
                            final boolean durable,
                            final boolean temporary)
   {
      AddressSettings addressSettings = addressSettingsRepository.getMatch(address.toString());

      Queue queue;
      if (addressSettings.isLastValueQueue())
      {
         queue = new LastValueQueue(persistenceID,
                                   address,
                                   name,
                                   filter,
                                   durable,
                                   temporary,
                                   scheduledExecutor,
                                   postOffice,
                                   storageManager,
                                   addressSettingsRepository);
      }
      else
      {
         queue = new QueueImpl(persistenceID,
                               address,
                               name,
                               filter,
                               durable,
                               temporary,
                               scheduledExecutor,
                               postOffice,
                               storageManager,
                               addressSettingsRepository);
      }

      queue.setDistributionPolicy(addressSettings.getDistributionPolicy());

      return queue;
   }
}