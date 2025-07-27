package com.bin.content.contentLists;

import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;

import java.util.function.Consumer;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyTechTreeList {
    public static void node(
            TechTree.TechNode parent, UnlockableContent content, Consumer<TechTree.TechNode> children
    ) {
        children.accept(node(parent, content));
    }

    public static TechTree.TechNode node(TechTree.TechNode parent, UnlockableContent content) {
        return new TechTree.TechNode(parent, content, content.researchRequirements());
    }

    public static void load() {
        node(TechTree.roots.first(), MyContextList.Bin_LinkCore);
    }
}
