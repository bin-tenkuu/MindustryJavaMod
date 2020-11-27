package com.content.contentLists;

import com.Tools;
import com.content.logics.ItemFactoryStatement;
import com.content.logics.SourceStatement;
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
