package com.content;

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
     Consumer<TechTree.TechNode> children
  ) {
    TechTree.TechNode node = new TechTree.TechNode(parent, content, content.researchRequirements());
    children.accept(node);
    return node;
  }

  @Override public void load() {
    root = node(TechTree.root, MyContextList.Bin_Block1, parent -> {
      node(parent, MyContextList.Bin_Block2, techNode -> {});
    });
  }
}
