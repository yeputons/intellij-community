// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.structuralsearch;

import com.intellij.psi.PsiElement;
import com.intellij.structuralsearch.impl.matcher.CompiledPattern;
import com.intellij.structuralsearch.plugin.util.SmartPsiPointer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SyntacticalMatchResult {
  public abstract PsiElement getPatternElement();
  public abstract SmartPsiPointer getMatchedElement();
  public abstract CompiledPattern getCompiledPattern();

  public abstract @NotNull List<SyntacticalMatchResult> getChildren();
}
