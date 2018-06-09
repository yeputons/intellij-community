// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.structuralsearch.impl.matcher;

import com.intellij.psi.PsiElement;
import com.intellij.structuralsearch.MatchOptions;
import com.intellij.structuralsearch.MatchResultSink;
import com.intellij.structuralsearch.SyntacticalMatchResult;
import com.intellij.structuralsearch.plugin.util.SmartPsiPointer;
import com.intellij.util.containers.Stack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Global context of matching process
 */
public class MatchContext {
  private final Stack<MatchedElementsListener> myMatchedElementsListenerStack = new Stack<>(2);

  private MatchResultSink sink;
  private final Stack<MatchResultImpl> previousResults = new Stack<>();
  private MatchResultImpl result;
  private final Stack<SyntacticalMatchResultImpl> syntacticalResults = new Stack<>(new SyntacticalMatchResultImpl());
  private CompiledPattern pattern;
  private MatchOptions options;
  private GlobalMatchingVisitor matcher;
  private boolean shouldRecursivelyMatch = true;

  private List<PsiElement> myMatchedNodes;

  public List<PsiElement> getMatchedNodes() {
    return myMatchedNodes;
  }

  public void setMatchedNodes(final List<PsiElement> matchedNodes) {
    myMatchedNodes = matchedNodes;
  }

  @FunctionalInterface
  public interface MatchedElementsListener {
    void matchedElements(@NotNull Collection<PsiElement> matchedElements);
  }

  public void setMatcher(GlobalMatchingVisitor matcher) {
    this.matcher = matcher;
  }

  public GlobalMatchingVisitor getMatcher() {
    return matcher;
  }

  public MatchOptions getOptions() {
    return options;
  }

  public void setOptions(MatchOptions options) {
    this.options = options;
  }

  public MatchResultImpl getPreviousResult() {
    return previousResults.isEmpty() ? null : previousResults.peek();
  }

  public MatchResultImpl getResult() {
    if (result==null) result = new MatchResultImpl();
    return result;
  }

  public void pushResult() {
    previousResults.push(result);
    result = null;
  }
  
  public void popResult() {
    result = previousResults.pop();
  }
  
  public void setResult(MatchResultImpl result) {
    this.result = result;
    if (result == null) {
      pattern.clearHandlersState();
    }
  }

  public boolean hasResult() {
    return result!=null;
  }

  @NotNull
  public SyntacticalMatchResultImpl getSyntacticalResult() {
    return syntacticalResults.peek();
  }

  @NotNull
  public List<SyntacticalMatchResultImpl> getSyntacticalResults() { return Collections.unmodifiableList(syntacticalResults); }

  public void setSyntacticalResults(List<SyntacticalMatchResultImpl> results) {
    syntacticalResults.clear();
    syntacticalResults.addAll(results);
  }

  public void pushSyntacticalResult(PsiElement patternElement, PsiElement matchedElement) {
    syntacticalResults.add(new SyntacticalMatchResultImpl(patternElement, new SmartPsiPointer(matchedElement)));
  }

  public void popSyntacticalResult(boolean matched) {
    SyntacticalMatchResultImpl newChild = syntacticalResults.pop();
    if (matched) {
      newChild.setCompiledPattern(pattern);
      syntacticalResults.peek().addChild(newChild);
    }
  }

  public CompiledPattern getPattern() {
    return pattern;
  }

  public void setPattern(CompiledPattern pattern) {
    this.pattern = pattern;
    assert syntacticalResults.size() == 1;
    getSyntacticalResult().setCompiledPattern(pattern);
  }

  public MatchResultSink getSink() {
    return sink;
  }

  public void setSink(MatchResultSink sink) {
    this.sink = sink;
  }

  public void clear() {
    result = null;
    pattern = null;
    clearSyntacticalResults();
  }

  public void clearSyntacticalResults() {
    syntacticalResults.clear();
    syntacticalResults.add(new SyntacticalMatchResultImpl());
    syntacticalResults.peek().setCompiledPattern(pattern);
  }

  public boolean shouldRecursivelyMatch() {
    return shouldRecursivelyMatch;
  }

  public void setShouldRecursivelyMatch(boolean shouldRecursivelyMatch) {
    this.shouldRecursivelyMatch = shouldRecursivelyMatch;
  }

  public void pushMatchedElementsListener(MatchedElementsListener matchedElementsListener) {
    myMatchedElementsListenerStack.push(matchedElementsListener);
  }

  public void popMatchedElementsListener() {
    myMatchedElementsListenerStack.pop();
  }

  public void notifyMatchedElements(Collection<PsiElement> matchedElements) {
    if (!myMatchedElementsListenerStack.isEmpty()) {
      myMatchedElementsListenerStack.peek().matchedElements(matchedElements);
    }
  }
}
