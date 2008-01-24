/*
   * JBoss, Home of Professional Open Source
   * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.example.embedded;

import org.jboss.jms.server.plugin.contract.JMSUserManager;
import org.jboss.messaging.core.Configuration;
import org.jboss.messaging.core.MessagingServer;
import org.jboss.messaging.core.impl.bdbje.BDBJEEnvironment;
import org.jboss.messaging.core.impl.bdbje.BDBJEPersistenceManager;
import org.jboss.messaging.core.impl.bdbje.integration.RealBDBJEEnvironment;
import org.jboss.messaging.core.impl.server.MessagingServerImpl;
import org.jboss.messaging.core.remoting.impl.mina.MinaService;
import org.jboss.messaging.microcontainer.AuthenticationManager;

/**
 * @author <a href="ataylor@redhat.com">Andy Taylor</a>
 */
public class MessagingServerFactory
{
   public static MessagingServer createMessagingServer() throws Exception
   {
      MinaService minaService = new MinaService("tcp", "localhost", 5400);
      minaService.start();
      MessagingServerImpl messagingServer = new MessagingServerImpl();
      
      messagingServer.setMinaService(minaService);
      Configuration configuration = new Configuration();
      configuration.setMessagingServerID(0);
      messagingServer.setConfiguration(configuration);
      messagingServer.setJmsUserManager(new JMSUserManager()
      {
         public String getPreConfiguredClientID(String username) throws Exception
         {
            return "0";
         }

         public void start() throws Exception
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         public void stop() throws Exception
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }
      });
      BDBJEPersistenceManager persistenceManager = new BDBJEPersistenceManager();
      persistenceManager.setLargeMessageRepository("${user.home}/bdbje/large");
      persistenceManager.setMinLargeMessageSize(1000000);
      BDBJEEnvironment bdbjeEnvironment = new RealBDBJEEnvironment();
      bdbjeEnvironment.setEnvironmentPath("${user.home}/bdbje/env");
      bdbjeEnvironment.setCreateEnvironment(true);
      bdbjeEnvironment.setSyncVM(true);
      bdbjeEnvironment.setSyncOS(false);
      bdbjeEnvironment.setMemoryCacheSize(-1);
      persistenceManager.setEnvironment(bdbjeEnvironment);
      bdbjeEnvironment.start();
      persistenceManager.start();
      messagingServer.setPersistenceManager(persistenceManager);
      messagingServer.setAuthenticationManager(new AuthenticationManager());
      return messagingServer;
   }
}
