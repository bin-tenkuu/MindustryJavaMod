package com.bin.content.contentLists;

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
            TechTree.TechNode parent, UnlockableContent content, Consumer<TechTree.TechNode> children
    ) {
        children.accept(node(parent, content));
    }

    public static TechTree.TechNode node(TechTree.TechNode parent, UnlockableContent content) {
        return new TechTree.TechNode(parent, content, content.researchRequirements());
    }

    @Override
    public void load() {
        node(TechTree.root, MyContextList.Bin_SourceDrill, (t1) -> {
            node(t1, MyContextList.Bin_LinkCore, (t2) -> {
//                node(t2, MyContextList.Bin_LargeStorage, (t3) -> {
//                    node(t3, MyContextList.Bin_LargeCore);
//                });
                node(t2, MyContextList.Bin_LaserTurret);
                node(t2, MyContextList.Bin_ItemChange);
//                node(t2, MyContextList.Bin_LiquidChange);
            });
//            node(t1, MyContextList.Bin_MapTurret);
//            node(t1, MyContextList.Bin_LiquidPower);
//            node(t1, MyLogicList.Bin_ItemControlStatement);
        });
    }
}
