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
package com.datatorrent.stram.webapp.asm;

import org.apache.xbean.asm5.signature.SignatureVisitor;

/**
 * <p>FieldSignatureVisitor class.</p>
 *
 * @since 3.2.0
 */
public class FieldSignatureVisitor extends BaseSignatureVisitor
{
  private Type fieldType;

  @Override
  public SignatureVisitor visitExceptionType()
  {
    // Method intentionally left unimplemented
    return null;
  }

  @Override
  public SignatureVisitor visitParameterType()
  {
    // Method intentionally left unimplemented
    return null;
  }

  @Override
  public SignatureVisitor visitReturnType()
  {
    // Method intentionally left unimplemented
    return null;
  }

  public Type getFieldType()
  {
    if (!visitingStack.isEmpty()) {
      return visitingStack.pop();
    } else {
      return null;
    }
  }
}