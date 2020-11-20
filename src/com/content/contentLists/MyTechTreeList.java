package com.content.contentLists;

import arc.util.Nullable;
import mindustry.content.TechTree;
import mindustry.ctype.ContentList;
import mindustry.ctype.UnlockableContent;

import java.util.function.Consumer;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyTechTreeList implements ContentList {

  public static void node(
      TechTree.TechNode parent,
      UnlockableContent content,
      @Nullable Consumer<TechTree.TechNode> children
  ) {
    TechTree.TechNode node = new TechTree.TechNode(parent, content, content.researchRequirements());
    if (children != null) {
      children.accept(node);
    }
  }

  public static void node(
      TechTree.TechNode parent,
      UnlockableContent content
  ) {
    node(parent, content, null);
  }

  @Override public void load() {
    node(TechTree.root, MyContextList.Bin_SourceDrill, (t1) -> {
      node(t1, MyContextList.Bin_LinkCore, (t2) -> {
        node(t2, MyContextList.Bin_LargeStorage, (t3) -> {
          node(t3, MyContextList.Bin_LargeCore);
//
        });
//        node(t2, MyContextList.Bin_ItemChange);
      });
      node(t1, MyContextList.Bin_MapTurret);
      node(t1, MyContextList.Bin_LiquidPower);
//      node(t1, MyLogicList.Bin_ItemControlStatement);
    });
  }
}
