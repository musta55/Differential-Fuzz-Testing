/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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
package org.apache.apex.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.hadoop.classification.InterfaceStability.Evolving;

@Evolving
/**
 * @since 3.7.0
 */
public class PropertiesHelper
{
  /**
   * Reading system property as long value.
   * @param propertyName Name of the system property
   * @param defaultValue Default value to return in case of an error, out of range etc.
   * @param minValue minimum valid value
   * @param maxValue maximum valid value
   * @return returns the value if it is between min and max value(inclusive), otherwise default value is returned.
   */
  public static long getLong(String propertyName, long defaultValue, long minValue, long maxValue)
  {
    String property = getProperty(propertyName);
    long result = defaultValue;
    if (property != null) {
      try {
        long value = parseLong(property);
        if (!isWithinRange(value, minValue, maxValue)) {
          logOutOfRangeWarning(propertyName, minValue, maxValue, defaultValue);
        } else {
          result = value;
        }
      } catch (Exception ex) {
        logConversionError(propertyName, property, defaultValue, ex);
      }
    }
    logDebugValue(propertyName, result);

    return result;
  }

  private static String getProperty(String propertyName) {
    return System.getProperty(propertyName);
  }

  private static long parseLong(String property) {
    return Long.decode(property);
  }

  private static boolean isWithinRange(long value, long minValue, long maxValue) {
    return value >= minValue && value <= maxValue;
  }

  private static void logOutOfRangeWarning(String propertyName, long minValue, long maxValue, long defaultValue) {
    logger.warn("Property {} is outside the range [{},{}], setting to default {}", propertyName, minValue, maxValue, defaultValue);
  }

  private static void logConversionError(String propertyName, String property, long defaultValue, Exception ex) {
    logger.warn("Can't convert property {} value {} to a long, using default {}", propertyName, property, defaultValue, ex);
  }

  private static void logDebugValue(String propertyName, long result) {
    logger.debug("System property {}'s value is {}", propertyName, result);
  }

  private static final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);
}