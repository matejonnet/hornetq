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

import org.jboss.jms.client.api.ClientConnection;
import org.jboss.jms.client.api.ClientConsumer;
import org.jboss.jms.client.api.ClientSession;
import org.jboss.jms.client.impl.ClientConnectionFactoryImpl;
import org.jboss.jms.message.JBossMessage;
import org.jboss.messaging.core.Destination;
import org.jboss.messaging.core.DestinationType;
import org.jboss.messaging.core.MessagingServer;
import org.jboss.messaging.core.impl.DestinationImpl;
import org.jboss.messaging.core.impl.MessageImpl;

import javax.jms.Session;

/**
 * @author <a href="ataylor@redhat.com">Andy Taylor</a>
 */
public class EmbeddedExample
{
   public static void main(String args[]) throws Exception
   {
      MessagingServer messagingServer = MessagingServerFactory.createMessagingServer();
      messagingServer.start();
      messagingServer.createQueue("Queue1");
      ClientConnectionFactoryImpl cf = new ClientConnectionFactoryImpl("tcp://localhost:5400?timeout=5");
      ClientConnection clientConnection = cf.createConnection(null, null);
      ClientSession clientSession = clientConnection.createClientSession(false, Session.AUTO_ACKNOWLEDGE, false);

      MessageImpl message = new MessageImpl();
      Destination destination = new DestinationImpl(DestinationType.QUEUE, "Queue1", false);
      message.putHeader(org.jboss.messaging.core.Message.TEMP_DEST_HEADER_NAME, destination);
      message.setPayload("hello".getBytes());
      clientSession.send(message);

      ClientConsumer clientConsumer = clientSession.createClientConsumer(destination, null, false, "me", false);
      clientConnection.start();
      JBossMessage m = (JBossMessage) clientConsumer.receive(0);
      System.out.println("m = " + new String(m.getCoreMessage().getPayload()));
      clientConnection.close();

      messagingServer.stop();
   }
}
