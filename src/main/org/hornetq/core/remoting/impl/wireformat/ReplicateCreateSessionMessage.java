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

/**
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * @author <a href="mailto:jmesnil@redhat.com">Jeff Mesnil</a>.
 * 
 * @version <tt>$Revision$</tt>
 */
public class ReplicateCreateSessionMessage extends PacketImpl
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   private String name;

   private long replicatedSessionChannelID;

   private long originalSessionChannelID;

   private int version;

   private String username;

   private String password;

   private int minLargeMessageSize;

   private boolean xa;

   private boolean autoCommitSends;

   private boolean autoCommitAcks;

   private boolean preAcknowledge;

   private int windowSize;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public ReplicateCreateSessionMessage(final String name,
                                        final long replicatedSessionChannelID,
                                        final long originalSessionChannelID,
                                        final int version,
                                        final String username,
                                        final String password,
                                        final int minLargeMessageSize,
                                        final boolean xa,
                                        final boolean autoCommitSends,
                                        final boolean autoCommitAcks,
                                        final boolean preAcknowledge,
                                        final int windowSize)
   {
      super(REPLICATE_CREATESESSION);

      this.name = name;

      this.replicatedSessionChannelID = replicatedSessionChannelID;

      this.originalSessionChannelID = originalSessionChannelID;

      this.version = version;

      this.username = username;

      this.password = password;

      this.minLargeMessageSize = minLargeMessageSize;

      this.xa = xa;

      this.autoCommitSends = autoCommitSends;

      this.autoCommitAcks = autoCommitAcks;

      this.preAcknowledge = preAcknowledge;

      this.windowSize = windowSize;
   }

   public ReplicateCreateSessionMessage()
   {
      super(REPLICATE_CREATESESSION);
   }

   // Public --------------------------------------------------------

   public String getName()
   {
      return name;
   }

   public long getReplicatedSessionChannelID()
   {
      return replicatedSessionChannelID;
   }

   public long getOriginalSessionChannelID()
   {
      return originalSessionChannelID;
   }

   public int getVersion()
   {
      return version;
   }

   public String getUsername()
   {
      return username;
   }

   public String getPassword()
   {
      return password;
   }

   public int getMinLargeMessageSize()
   {
      return minLargeMessageSize;
   }

   public boolean isXA()
   {
      return xa;
   }

   public boolean isAutoCommitSends()
   {
      return autoCommitSends;
   }

   public boolean isAutoCommitAcks()
   {
      return autoCommitAcks;
   }

   public boolean isPreAcknowledge()
   {
      return preAcknowledge;
   }

   public int getWindowSize()
   {
      return windowSize;
   }

   public int getRequiredBufferSize()
   {
      return BASIC_PACKET_SIZE + 
             stringEncodeSize(name) + // buffer.writeString(name);
             DataConstants.SIZE_LONG + // buffer.writeLong(originalSessionChannelID);
             DataConstants.SIZE_LONG + // buffer.writeLong(replicatedSessionChannelID);
             DataConstants.SIZE_INT + // buffer.writeInt(version);
             nullableStringEncodeSize(username) + // buffer.writeNullableString(username);
             nullableStringEncodeSize(password) + // buffer.writeNullableString(password);
             DataConstants.SIZE_INT + // buffer.writeInt(minLargeMessageSize);
             DataConstants.SIZE_BOOLEAN + // buffer.writeBoolean(xa);
             DataConstants.SIZE_BOOLEAN + // buffer.writeBoolean(autoCommitSends);
             DataConstants.SIZE_BOOLEAN + // buffer.writeBoolean(autoCommitAcks);
             DataConstants.SIZE_INT + // buffer.writeInt(windowSize);
             DataConstants.SIZE_BOOLEAN; // buffer.writeBoolean(preAcknowledge);
   }

   @Override
   public void encodeBody(final MessagingBuffer buffer)
   {
      buffer.writeString(name);
      buffer.writeLong(originalSessionChannelID);
      buffer.writeLong(replicatedSessionChannelID);
      buffer.writeInt(version);
      buffer.writeNullableString(username);
      buffer.writeNullableString(password);
      buffer.writeInt(minLargeMessageSize);
      buffer.writeBoolean(xa);
      buffer.writeBoolean(autoCommitSends);
      buffer.writeBoolean(autoCommitAcks);
      buffer.writeInt(windowSize);
      buffer.writeBoolean(preAcknowledge);
   }

   @Override
   public void decodeBody(final MessagingBuffer buffer)
   {
      name = buffer.readString();
      originalSessionChannelID = buffer.readLong();
      replicatedSessionChannelID = buffer.readLong();
      version = buffer.readInt();
      username = buffer.readNullableString();
      password = buffer.readNullableString();
      minLargeMessageSize = buffer.readInt();
      xa = buffer.readBoolean();
      autoCommitSends = buffer.readBoolean();
      autoCommitAcks = buffer.readBoolean();
      windowSize = buffer.readInt();
      preAcknowledge = buffer.readBoolean();
   }

   @Override
   public boolean equals(final Object other)
   {
      if (other instanceof ReplicateCreateSessionMessage == false)
      {
         return false;
      }

      ReplicateCreateSessionMessage r = (ReplicateCreateSessionMessage)other;

      boolean matches = super.equals(other) && name.equals(r.name) &&
                        originalSessionChannelID == r.originalSessionChannelID &&
                        replicatedSessionChannelID == r.replicatedSessionChannelID &&
                        version == r.version &&
                        xa == r.xa &&
                        autoCommitSends == r.autoCommitSends &&
                        autoCommitAcks == r.autoCommitAcks &&
                        (username == null ? r.username == null : username.equals(r.username)) &&
                        (password == null ? r.password == null : password.equals(r.password)) &&
                        minLargeMessageSize == r.minLargeMessageSize &&
                        windowSize == r.windowSize;

      return matches;
   }

   @Override
   public final boolean isRequiresConfirmations()
   {
      return false;
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}