/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005-2009, Red Hat Middleware LLC, and individual contributors
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

package org.hornetq.core.management.impl;

import java.util.HashMap;

import org.hornetq.core.client.ClientMessage;
import org.hornetq.core.client.ClientRequestor;
import org.hornetq.core.client.ClientSession;
import org.hornetq.core.client.ClientSessionFactory;
import org.hornetq.core.client.impl.ClientSessionFactoryImpl;
import org.hornetq.core.client.management.impl.ManagementHelper;
import org.hornetq.core.config.TransportConfiguration;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.management.ReplicationOperationInvoker;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.remoting.impl.invm.TransportConstants;
import org.hornetq.utils.SimpleString;

/**
 * A ReplicationOperationInvoker
 *
 * @author <a href="jmesnil@redhat.com">Jeff Mesnil</a>
 *
 */
public class ReplicationOperationInvokerImpl implements ReplicationOperationInvoker
{

   // Constants -----------------------------------------------------

   private static final Logger log = Logger.getLogger(ReplicationOperationInvokerImpl.class);

   // Attributes ----------------------------------------------------

   private final long timeout;

   private final String clusterUser;

   private final String clusterPassword;

   private final SimpleString managementAddress;

   private ClientSession clientSession;

   private ClientRequestor requestor;

   private final int managementConnectorID;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public ReplicationOperationInvokerImpl(final String clusterUser,
                                          final String clusterPassword,
                                          final SimpleString managementAddress,
                                          final long managementRequestTimeout,
                                          final int managementConnectorID)
   {
      this.timeout = managementRequestTimeout;
      this.clusterUser = clusterUser;
      this.clusterPassword = clusterPassword;
      this.managementAddress = managementAddress;
      this.managementConnectorID = managementConnectorID;
   }

   // Public --------------------------------------------------------

   public synchronized Object invoke(final String resourceName, final String operationName, final Object... parameters) throws Exception
   {
      if (clientSession == null)
      {
         ClientSessionFactory sf = new ClientSessionFactoryImpl(new TransportConfiguration(InVMConnectorFactory.class.getName(),
                                                                                           new HashMap<String, Object>()
                                                                                           {
                                                                                              {
                                                                                                 put(TransportConstants.SERVER_ID_PROP_NAME,
                                                                                                     managementConnectorID);
                                                                                              }
                                                                                           }));

         clientSession = sf.createSession(clusterUser, clusterPassword, false, true, true, false, 1);
         requestor = new ClientRequestor(clientSession, managementAddress);
         clientSession.start();
      }
      ClientMessage mngmntMessage = clientSession.createClientMessage(false);
      ManagementHelper.putOperationInvocation(mngmntMessage, resourceName, operationName, parameters);
      ClientMessage reply = requestor.request(mngmntMessage, timeout);

      if (reply == null)
      {
         throw new Exception("did not receive reply for message " + mngmntMessage);
      }
      reply.acknowledge();
      if (ManagementHelper.hasOperationSucceeded(reply))
      {
         return ManagementHelper.getResult(reply);
      }
      else
      {
         throw new Exception((String)ManagementHelper.getResult(reply));
      }
   }

   public void stop() throws Exception
   {
      if (clientSession != null)
      {
         clientSession.close();
      }
   }
   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------

}