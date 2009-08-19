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

import org.hornetq.core.exception.MessagingException;
import org.hornetq.core.remoting.spi.MessagingBuffer;
import org.hornetq.utils.DataConstants;

/**
 * @author <a href="mailto:jmesnil@redhat.com">Jeff Mesnil</a>
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * 
 * @version <tt>$Revision$</tt>
 * 
 */
public class MessagingExceptionMessage extends PacketImpl
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   private MessagingException exception;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public MessagingExceptionMessage(final MessagingException exception)
   {
      super(EXCEPTION);

      this.exception = exception;
   }

   public MessagingExceptionMessage()
   {
      super(EXCEPTION);
   }

   // Public --------------------------------------------------------

   public boolean isResponse()
   {
      return true;
   }

   public MessagingException getException()
   {
      return exception;
   }
   
   public int getRequiredBufferSize()
   {
      return BASIC_PACKET_SIZE + DataConstants.SIZE_INT + nullableStringEncodeSize(exception.getMessage());
   }


   public void encodeBody(final MessagingBuffer buffer)
   {
      buffer.writeInt(exception.getCode());
      buffer.writeNullableString(exception.getMessage());
   }

   public void decodeBody(final MessagingBuffer buffer)
   {
      int code = buffer.readInt();
      String msg = buffer.readNullableString();
      exception = new MessagingException(code, msg);
   }

   @Override
   public String toString()
   {
      return getParentString() + ", exception= " + exception + "]";
   }

   public boolean equals(Object other)
   {
      if (other instanceof MessagingExceptionMessage == false)
      {
         return false;
      }

      MessagingExceptionMessage r = (MessagingExceptionMessage)other;

      return super.equals(other) && this.exception.equals(r.exception);
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}