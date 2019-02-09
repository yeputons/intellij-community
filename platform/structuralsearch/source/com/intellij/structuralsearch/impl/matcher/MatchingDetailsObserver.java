// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.structuralsearch.impl.matcher;

import com.intellij.psi.PsiElement;
import com.intellij.structuralsearch.MatchResult;
import com.intellij.structuralsearch.MatchingDetailsListener;
import com.intellij.util.containers.hash.HashSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class MatchingDetailsObserver {
  private Set<MatchingDetailsListener> listeners = new HashSet<>();

  public void addListener(MatchingDetailsListener listener) {
    listeners.add(listener);
  }

  public void removeListener(MatchingDetailsListener listener) {
    listeners.remove(listener);
  }

  public void fireSetPattern(CompiledPattern pattern) {
    listeners.forEach(c -> c.setPattern(pattern));
  }

  public void fireClear() {
    listeners.forEach(c -> c.clear());
  }

  public void firePushNewMatchContext() {
    listeners.forEach(c -> c.pushNewMatchContext());
  }

  public void fireNewMatchContext() {
    listeners.forEach(c -> c.newMatchContext());
  }

  public void firePopNewMatchContext() {
    listeners.forEach(c -> c.popNewMatchContext());
  }

  public void fireEnterTryMatching(PsiElement patternElement, PsiElement matchedElement) {
    listeners.forEach(c -> c.enterTryMatching(patternElement, matchedElement));
  }

  public void fireExitTryMatching(boolean matched) {
    listeners.forEach(c -> c.exitTryMatching(matched));
  }

  public void fireRemoveLastMatches(int numberOfMatches) {
    listeners.forEach(c -> c.removeLastMatches(numberOfMatches));
  }

  public void fireOnAddedResult(MatchResult result) {
    listeners.forEach(c -> c.onAddedResult(result));
  }

  public void fireOnNoSubstitutionMatch(List<PsiElement> matchedNodes, MatchResult result) {
    listeners.forEach(c -> c.onNoSubstitutionMatch(matchedNodes, result));
  }

  public void fireMoveMatchResult(@NotNull MatchResult from, @NotNull MatchResult to) {
    listeners.forEach(c -> c.moveMatchResult(from, to));
  }
}
