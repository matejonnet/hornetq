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

package org.hornetq.core.remoting.impl.wireformat;

import org.hornetq.core.remoting.spi.MessagingBuffer;
import org.hornetq.utils.DataConstants;
import org.hornetq.utils.SimpleString;

/**
 * 
 * A SessionQueueQueryResponseMessage
 * 
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 *
 */
public class SessionQueueQueryResponseMessage extends PacketImpl
{
   private boolean exists;

   private boolean durable;

   private int consumerCount;

   private int messageCount;

   private SimpleString filterString;

   private SimpleString address;

   public SessionQueueQueryResponseMessage(final boolean durable,
                                           final int consumerCount,
                                           final int messageCount,
                                           final SimpleString filterString,
                                           final SimpleString address)
   {
      this(durable, consumerCount, messageCount, filterString, address, true);
   }

   public SessionQueueQueryResponseMessage()
   {
      this(false, 0, 0, null, null, false);
   }

   private SessionQueueQueryResponseMessage(final boolean durable,
                                            final int consumerCount,
                                            final int messageCount,
                                            final SimpleString filterString,
                                            final SimpleString address,
                                            final boolean exists)
   {
      super(SESS_QUEUEQUERY_RESP);

      this.durable = durable;

      this.consumerCount = consumerCount;

      this.messageCount = messageCount;

      this.filterString = filterString;

      this.address = address;

      this.exists = exists;
   }

   public boolean isResponse()
   {
      return true;
   }

   public boolean isExists()
   {
      return exists;
   }

   public boolean isDurable()
   {
      return durable;
   }

   public int getConsumerCount()
   {
      return consumerCount;
   }

   public int getMessageCount()
   {
      return messageCount;
   }

   public SimpleString getFilterString()
   {
      return filterString;
   }

   public SimpleString getAddress()
   {
      return address;
   }
   

   public int getRequiredBufferSize()
   {
      return BASIC_PACKET_SIZE + 
      DataConstants.SIZE_BOOLEAN + // buffer.writeBoolean(exists);
      DataConstants.SIZE_BOOLEAN + // buffer.writeBoolean(durable);
      DataConstants.SIZE_INT + // buffer.writeInt(consumerCount);
      DataConstants.SIZE_INT + // buffer.writeInt(messageCount);
      SimpleString.sizeofNullableString(filterString) + // buffer.writeNullableSimpleString(filterString);
      SimpleString.sizeofNullableString(address); // buffer.writeNullableSimpleString(address);
   }

   public void encodeBody(final MessagingBuffer buffer)
   {
      buffer.writeBoolean(exists);
      buffer.writeBoolean(durable);
      buffer.writeInt(consumerCount);
      buffer.writeInt(messageCount);
      buffer.writeNullableSimpleString(filterString);
      buffer.writeNullableSimpleString(address);
   }

   public void decodeBody(final MessagingBuffer buffer)
   {
      exists = buffer.readBoolean();
      durable = buffer.readBoolean();
      consumerCount = buffer.readInt();
      messageCount = buffer.readInt();
      filterString = buffer.readNullableSimpleString();
      address = buffer.readNullableSimpleString();
   }

   public boolean equals(Object other)
   {
      if (other instanceof SessionQueueQueryResponseMessage == false)
      {
         return false;
      }

      SessionQueueQueryResponseMessage r = (SessionQueueQueryResponseMessage)other;

      return super.equals(other) && this.exists == r.exists &&
             this.durable == r.durable &&
             this.consumerCount == r.consumerCount &&
             this.messageCount == r.messageCount &&
             this.filterString == null ? r.filterString == null
                                      : this.filterString.equals(r.filterString) && this.address == null ? r.address == null
                                                                                                        : this.address.equals(r.address);
   }

}