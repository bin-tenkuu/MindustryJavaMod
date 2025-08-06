package com.bin.content.contentLists;

import arc.scene.ui.layout.Table;
import com.bin.Tools;
import com.bin.content.blocks.CommendBlock;
import com.bin.content.blocks.CopyGate;
import com.bin.content.blocks.LinkCoreBlock;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.world.Block;

/**
 * @author bin
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class MyContextList {
    public static final Block Bin_LinkCore = new LinkCoreBlock();
    public static final Block Bin_CopyGate = new CopyGate();
    public static final Block Bin_Commend1 = new CommendBlock("Bin_Commend1") {
        {
            localizedName = "指令器";
            description = "集成各种指令";
            commend = MyContextList::display;
        }
    };
    public static final Block Bin_CommendCall = new CommendBlock("Bin_CommendCall") {
        {
            localizedName = "召唤器";
            description = "召唤指定单位";

            commend = (building, table) -> Tools.buildItemSelectTable(
                    table,
                    Vars.content.units(),
                    () -> null,
                    unitType -> {
                        Unit unit = unitType.create(building.team());
                        unit.set(building.x, building.y + 1);
                        unit.add();
                    },
                    false
            );
        }
    };

    public static void load() {
    }

    private static void display(Building building, Table table) {
        table.button(Icon.upOpen, () ->
                Vars.logic.skipWave()
        ).size(50).tooltip("下一波");
        table.button(Icon.warningSmall, () -> {
            for (int i = 0; i < 10; i++) {
                Vars.logic.runWave();
            }
        }).size(50).tooltip("跳10波");
        table.button(Icon.file, () ->
                Groups.all.each(Firec.class::isInstance, Entityc::remove)
        ).size(50).tooltip("快速灭火");
        table.button(Icon.cancel, () ->
                Groups.unit.each(Unitc::destroy)
        ).size(50).tooltip("清除所有单位");
        table.row();
    }
}
