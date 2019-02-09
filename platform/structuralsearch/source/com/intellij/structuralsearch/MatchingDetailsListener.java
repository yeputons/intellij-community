// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.structuralsearch;

import com.intellij.psi.PsiElement;
import com.intellij.structuralsearch.impl.matcher.CompiledPattern;
import org.jetbrains.annotations.NotNull;

public interface MatchingDetailsListener {
  void setPattern(CompiledPattern pattern);

  void clear();

  void pushNewMatchContext();

  void newMatchContext();

  void popNewMatchContext();

  void enterTryMatching(PsiElement patternElement, PsiElement matchedElement);

  void exitTryMatching(boolean matched);

  void removeLastMatches(int numberOfResults);

  void onAddedResult(MatchResult result);

  void moveMatchResult(@NotNull MatchResult from, @NotNull MatchResult to);
}
