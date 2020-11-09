package com.content;

import arc.scene.ui.layout.Table;
import com.Tools;
import com.content.blocks.CommendBlock;
import com.content.blocks.FastestDrill;
import com.content.blocks.ItemChange;
import com.content.blocks.LinkCoreBlock;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.gen.Building;
import mindustry.gen.Entityc;
import mindustry.gen.Firec;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class MyContextList implements ContentList {
  public static Block
     Bin_Block1, Bin_Block2, Bin_Block3, Bin_Block4, Bin_Block5;

  @Override public void load() {
    Bin_Block1 = new ItemChange("Bin_Block1") {
      {
        this.localizedName = "等量转换器";
        this.description = "输入物品,转换为等量的另一个物品";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_Block2 = new LinkCoreBlock("Bin_Block2") {
      {
        this.localizedName = "量子核心连接器";
        this.description = "通过量子通道与核心相连,同时装载了装卸器";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_Block3 = new CommendBlock("Bin_Block3") {
      {
        this.localizedName = "指令器";
        this.description = "集成各种指令";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
        commend = MyContextList::display;
      }
    };
    Bin_Block4 = new CommendBlock("Bin_Block4") {
      {
        this.localizedName = "召唤器";
        this.description = "召唤指定单位";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);

        this.commend = (building, table) -> Tools.buildItemSelectTable(
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
    Bin_Block5 = new FastestDrill("Bin_Block5") {
      {
        this.localizedName = "量子钻头";
        this.description = "[red]极限产出,不可加速[]\n" +
                           "[green]我在短暂的钻头生涯当中学到了一件事," +
                           "越是想要钻的更快,越是会受到各种限制," +
                           "除非超越钻头,我不做钻头了![]\n" +
                           "(现在产出速度比物品源还快了";
        this.size = 2;
        this.requirements(Category.production, BuildVisibility.shown, ItemStack.with(Items.copper, 20));
      }
    };
  }

  private static void display(Building building, Table table) {
    table.button(Icon.upOpen, Styles.clearTransi, (() -> Vars.logic.skipWave())).size(50).tooltip("下一波");
    table.button(Icon.warningSmall, Styles.clearTransi, (() -> {
      for (int i = 0; i < 10; i++) {
        Vars.logic.runWave();
      }
    })).size(50).tooltip("跳10波");
    table.button(Icon.file, Styles.clearTransi, () ->
       Groups.all.each(syncs -> syncs instanceof Firec, Entityc::remove)
    ).size(50).tooltip("快速灭火");
  }
}
