package com.bin.content.contentLists;

import arc.scene.ui.layout.Table;
import com.bin.Tools;
import com.bin.content.blocks.CommendBlock;
import com.bin.content.blocks.LinkCoreBlock;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.gen.*;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyContextList {
    public static Block
            Bin_LinkCore,
            Bin_Commend1,
            Bin_CommendCall,
            VOID;

    public static void load() {
        Bin_LinkCore = new LinkCoreBlock("Bin_LinkCore") {
            {
                localizedName = "量子核心连接器";
                description = "通过量子通道与核心相连,同时装载了装卸器";
                requirements(Category.effect, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
                size = 1;
                health = 320;
            }
        };
        Bin_Commend1 = new CommendBlock("Bin_Commend1") {
            {
                localizedName = "指令器";
                description = "集成各种指令";
                size = 2;
                requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
                commend = MyContextList::display;
            }
        };//*/
        Bin_CommendCall = new CommendBlock("Bin_CommendCall") {
            {
                localizedName = "召唤器";
                description = "召唤指定单位";
                size = 2;
                requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);

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
    }

    private static void display(Building building, Table table) {
        table.button(Icon.upOpen, Styles.clearNonei, () ->
                Vars.logic.skipWave()
        ).size(50).tooltip("下一波");
        table.button("跳10波", Icon.warningSmall, Styles.cleart, () -> {
            for (int i = 0; i < 10; i++) {
                Vars.logic.runWave();
            }
        }).size(50);
        table.button(Icon.file, Styles.clearNonei, () ->
                Groups.all.each(Firec.class::isInstance, Entityc::remove)
        ).size(50).tooltip("快速灭火");
        table.button(Icon.cancel, Styles.clearNonei, () ->
                Groups.unit.each(Unitc::destroy)
        ).size(50).tooltip("清除所有单位");
        table.row();
    }
}
