package com.content;

import com.content.blocks.GambleMachine;
import com.content.blocks.MyBlock;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

/**
 * @author bin
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class MyContextList implements ContentList {
  public static Block MyBlockTest,
     MyTest;

  @Override public void load() {
    MyBlockTest = new MyBlock("MyBlockTest") {{
      localizedName = "MyBlockTest";
      requirements(Category.units, ItemStack.with(Items.copper, 1, Items.lead, 8));
      hasItems = true;
      craftTime = 1;
      results = ItemStack.with(Items.copper, 1, Items.coal, 4);
      size = 2;
      health = 320;
      consumes.items(new ItemStack(Items.copper, 1), new ItemStack(Items.lead, 8));
    }};
    MyTest = new GambleMachine("MyTest") {{
      localizedName = "抽卡机";
      description = "抽卡机,随机出货";
      requirements(Category.units, ItemStack.with(Items.copper, 100));
      hasItems = true;
      craftTime = 1;
      results = ItemStack.with(Items.copper, 50, Items.lead, 20, Items.thorium, 20, Items.surgeAlloy, 10);
      size = 2;
      health = 320;
      itemCapacity = 100;
      consumes.items(ItemStack.with(Items.copper, 10));
    }};
  }
}
