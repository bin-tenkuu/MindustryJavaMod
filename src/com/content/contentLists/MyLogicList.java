package com.content.contentLists;

import com.Tools;
import com.content.logics.ItemControlStatement;
import mindustry.ctype.ContentList;
import mindustry.ctype.UnlockableContent;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyLogicList implements ContentList {
//  public static UnlockableContent Bin_ItemControlStatement;

  @Override public void load() {
//    Bin_ItemControlStatement = new LogicStatementUnlock("Bin_ItemControlStatement", "处理器控制物品",
//        ItemControlStatement::new
//    );
    Tools.addLogicStatement(new ItemControlStatement());
  }

}
