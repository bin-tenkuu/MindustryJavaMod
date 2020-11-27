package com.bin.interfaces;

import mindustry.logic.LStatement;

/**
 * @author bin
 * @version 1.0.0
 */
public interface LogicStatement {
  /**
   * ����ָ��ID
   * @return ID
   */
  String getId();

  /**
   * ���� T ���¶���
   *
   * @return T
   */
  LStatement create();

  /**
   * ��ȡ�����󷵻� T ���¶���
   *
   * @param s �������
   * @return T
   */
  LStatement read(String[] s);
}
