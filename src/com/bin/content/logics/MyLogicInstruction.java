package com.bin.content.logics;

import mindustry.logic.LExecutor;

import java.util.function.BiConsumer;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyLogicInstruction implements LExecutor.LInstruction {
  private final int[] args;
  private final BiConsumer<LExecutor, int[]> runner;

  /**
   * 运行时给出 LExecutor ,使用传入 运行参数 运行
   * @param runner 运行器
   * @param args 运行参数
   */
  public MyLogicInstruction(BiConsumer<LExecutor, int[]> runner, int... args) {
    this.runner = runner;
    this.args = args;
  }

  @Override public void run(LExecutor lExecutor) {
    this.runner.accept(lExecutor, args);
  }
}
