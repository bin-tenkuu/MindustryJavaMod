package com.bin.interfaces;

import mindustry.logic.LStatement;

/**
 * @author bin
 * @version 1.0.0
 */
public interface LogicStatement {
  /**
   * 返回指令ID
   * @return ID
   */
  String getId();

  /**
   * 创建 T 的新对象
   *
   * @return T
   */
  LStatement create();

  /**
   * 读取参数后返回 T 的新对象
   *
   * @param s 保存参数
   * @return T
   */
  LStatement read(String[] s);
}
