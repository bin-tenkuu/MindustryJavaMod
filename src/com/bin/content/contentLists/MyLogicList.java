package com.bin.content.contentLists;

import com.bin.Tools;
import com.bin.content.logics.ItemFactoryStatement;
import com.bin.content.logics.SourceStatement;
import mindustry.ctype.ContentList;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyLogicList implements ContentList {

  @Override public void load() {
    Tools.addLogicStatement(new SourceStatement());
    Tools.addLogicStatement(new ItemFactoryStatement());
  }

}
