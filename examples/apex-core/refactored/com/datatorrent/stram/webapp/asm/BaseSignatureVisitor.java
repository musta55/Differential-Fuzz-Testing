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

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.xbean.asm5.Opcodes;
import org.apache.xbean.asm5.signature.SignatureVisitor;

import com.datatorrent.stram.webapp.asm.Type.ArrayTypeNode;
import com.datatorrent.stram.webapp.asm.Type.ParameterizedTypeNode;
import com.datatorrent.stram.webapp.asm.Type.TypeNode;
import com.datatorrent.stram.webapp.asm.Type.TypeVariableNode;
import com.datatorrent.stram.webapp.asm.Type.WildcardTypeNode;

/**
 * Follow the visiting path of ASM
 * to visit getter and setter method signature
 *
 * ClassSignature = ( visitFormalTypeParameter visitClassBound? visitInterfaceBound* )* ( visitSuperClass visitInterface* )
 * MethodSignature = ( visitFormalTypeParameter visitClassBound? visitInterfaceBound* )* ( visitParameterType* visitReturnType visitExceptionType* )
 * TypeSignature = visitBaseType | visitTypeVariable | visitArrayType | ( visitClassType visitTypeArgument* ( visitInnerClassType visitTypeArgument* )* visitEnd ) )
 *
 * @since 2.1
 */
public abstract class BaseSignatureVisitor extends SignatureVisitor
{

  protected List<TypeVariableNode> typeV = new LinkedList<>();

  protected int stage = -1;

  public static final int VISIT_FORMAL_TYPE = 0;

  protected Stack<Type> visitingStack = new Stack<>();

  public BaseSignatureVisitor()
  {
    super(Opcodes.ASM5);
  }

  @Override
  public SignatureVisitor visitArrayType()
  {
    ArrayTypeNode at = new ArrayTypeNode();
    visitingStack.push(at);
    return this;
  }

  @Override
  public void visitBaseType(char baseType)
  {
    TypeNode tn = new TypeNode();
    tn.setObjByteCode(String.valueOf(baseType));
    visitingStack.push(tn);
    resolveStack();
  }

  @Override
  public void visitClassType(String classType)
  {
    TypeNode tn = new TypeNode();
    tn.setObjByteCode("L" + classType + ";");
    visitingStack.push(tn);
  }

  private void resolveStack()
  {
    if (visitingStack.size() <= 1) {
      return;
    }
    Type top = visitingStack.pop();
    Type peek = visitingStack.peek();

    if (peek instanceof ParameterizedTypeNode) {
      ((ParameterizedTypeNode)peek).actualTypeArguments.add(top);
    } else if (peek instanceof ArrayTypeNode) {
      ((ArrayTypeNode)peek).actualArrayType = top;
      resolveStack();
    } else if (peek instanceof WildcardTypeNode) {
      ((WildcardTypeNode)peek).bounds.add(top);
      resolveStack();
    } else if (peek instanceof TypeVariableNode) {
      ((TypeVariableNode)peek).bounds.add(top);
      resolveStack();
    } else {
      visitingStack.push(top);
    }
  }

  @Override
  public void visitEnd()
  {
    resolveStack();
  }

  @Override
  public void visitInnerClassType(String classType)
  {
    visitClassType(classType);
  }

  @Override
  public void visitTypeArgument()
  {
    visitTypeArgument(SignatureVisitor.EXTENDS);
    visitClassType(Object.class.getName());
    visitEnd();
  }

  @Override
  public SignatureVisitor visitTypeArgument(char typeArg)
  {
    TypeNode t = (TypeNode)visitingStack.pop();
    ParameterizedTypeNode pt = ensureParameterizedType(t);
    visitingStack.push(pt);

    if (typeArg == SignatureVisitor.INSTANCEOF) {
      return this;
    }
    WildcardTypeNode wtn = new WildcardTypeNode();
    wtn.boundChar = typeArg;
    visitingStack.push(wtn);

    return this;
  }

  private ParameterizedTypeNode ensureParameterizedType(TypeNode t) {
    if (t instanceof ParameterizedTypeNode) {
      return (ParameterizedTypeNode)t;
    } else {
      ParameterizedTypeNode pt = new ParameterizedTypeNode();
      pt.setObjByteCode(t.getObjByteCode());
      return pt;
    }
  }

  @Override
  public void visitTypeVariable(String typeVariable)
  {
    TypeVariableNode tvn = findTypeVariableNode(typeVariable);
    if (tvn == null) {
      tvn = createAndAddTypeVariableNode(typeVariable);
    }
    visitingStack.push(tvn);
    resolveStack();
  }

  private TypeVariableNode findTypeVariableNode(String typeVariable) {
    for (TypeVariableNode typeVariableNode : typeV) {
      if (typeVariableNode.typeLiteral.equals(typeVariable)) {
        return typeVariableNode;
      }
    }
    return null;
  }

  private TypeVariableNode createAndAddTypeVariableNode(String typeVariable) {
    TypeNode tn = new TypeNode();
    tn.setObjByteCode("T" + typeVariable + ";");
    TypeVariableNode tvn = new TypeVariableNode();
    tvn.typeLiteral = typeVariable;
    tvn.bounds.add(tn);
    typeV.add(tvn);
    return tvn;
  }

  @Override
  public SignatureVisitor visitInterface()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public SignatureVisitor visitInterfaceBound()
  {
    return this;
  }

  @Override
  public SignatureVisitor visitSuperclass()
  {
    return this;
  }

  @Override
  public void visitFormalTypeParameter(String typeVariable)
  {
    if (stage == VISIT_FORMAL_TYPE && !visitingStack.isEmpty()) {
      visitingStack.pop();
    }
    stage = VISIT_FORMAL_TYPE;
    TypeVariableNode tvn = new TypeVariableNode();
    tvn.typeLiteral = typeVariable;
    visitingStack.push(tvn);
    typeV.add(tvn);
  }

  @Override
  public SignatureVisitor visitClassBound()
  {
    return this;
  }

  public List<TypeVariableNode> getTypeV()
  {
    return typeV;
  }
}