package com.content;

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
  public static TechTree.TechNode root;

  public static TechTree.TechNode node(
     TechTree.TechNode parent,
     UnlockableContent content,
     @Nullable Consumer<TechTree.TechNode> children
  ) {
    TechTree.TechNode node = new TechTree.TechNode(parent, content, content.researchRequirements());
    if (children != null) {
      children.accept(node);
    }
    return node;
  }

  public static TechTree.TechNode node(
     TechTree.TechNode parent,
     UnlockableContent content
  ) {
    return node(parent, content, (t) -> {});
  }

  @Override public void load() {
    root = node(TechTree.root, MyContextList.Bin_Block5, (t1) -> {
      node(t1, MyContextList.Bin_Block2, (t2) -> {
        node(t2, MyContextList.Bin_Block1);
      });
    });
  }
}
