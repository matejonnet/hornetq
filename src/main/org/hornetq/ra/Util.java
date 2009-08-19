/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.hornetq.ra;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

/**
 * Various utility functions
 *
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 * @version $Revision: $
 */
public class Util
{

   /**
    * Private constructor
    */
   private Util()
   {
   }

   /**
    * Compare two strings.
    * @param me First value
    * @param you Second value
    * @return True if object equals else false. 
    */
   public static boolean compare(final String me, final String you)
   {
      // If both null or intern equals
      if (me == you)
      {
         return true;
      }

      // if me null and you are not
      if (me == null && you != null)
      {
         return false;
      }

      // me will not be null, test for equality
      return me.equals(you);
   }

   /**
    * Compare two integers.
    * @param me First value
    * @param you Second value
    * @return True if object equals else false. 
    */
   public static boolean compare(final Integer me, final Integer you)
   {
      // If both null or intern equals
      if (me == you)
      {
         return true;
      }

      // if me null and you are not
      if (me == null && you != null)
      {
         return false;
      }

      // me will not be null, test for equality
      return me.equals(you);
   }

   /**
    * Compare two longs.
    * @param me First value
    * @param you Second value
    * @return True if object equals else false. 
    */
   public static boolean compare(final Long me, final Long you)
   {
      // If both null or intern equals
      if (me == you)
      {
         return true;
      }

      // if me null and you are not
      if (me == null && you != null)
      {
         return false;
      }

      // me will not be null, test for equality
      return me.equals(you);
   }

   /**
    * Compare two doubles.
    * @param me First value
    * @param you Second value
    * @return True if object equals else false. 
    */
   public static boolean compare(final Double me, final Double you)
   {
      // If both null or intern equals
      if (me == you)
      {
         return true;
      }

      // if me null and you are not
      if (me == null && you != null)
      {
         return false;
      }

      // me will not be null, test for equality
      return me.equals(you);
   }

   /**
    * Compare two booleans.
    * @param me First value
    * @param you Second value
    * @return True if object equals else false. 
    */
   public static boolean compare(final Boolean me, final Boolean you)
   {
      // If both null or intern equals
      if (me == you)
      {
         return true;
      }

      // if me null and you are not
      if (me == null && you != null)
      {
         return false;
      }

      // me will not be null, test for equality
      return me.equals(you);
   }

   /**
    * Lookup an object in the default initial context
    * @param context The context to use
    * @param name the name to lookup
    * @param clazz the expected type
    * @return the object
    * @throws Exception for any error
    */
   public static Object lookup(final Context context, final String name, final Class clazz) throws Exception
   {
      return context.lookup(name);
   }

   public static Map<String, Object> parseConfig(final String config)
   {
      HashMap<String, Object> result = new HashMap<String, Object>();

      String elements[] = config.split(";");

      for (String element : elements)
      {
         String expression[] = element.split("=");

         if (expression.length != 2)
         {
            throw new IllegalArgumentException("Invalid expression " + element + " at " + config);
         }

         result.put(expression[0].trim(), expression[1].trim());
      }

      return result;
   }
}