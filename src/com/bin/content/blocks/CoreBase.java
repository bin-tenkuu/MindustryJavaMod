package com.bin.content.blocks;

import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
public class CoreBase extends CoreBlock {

    public CoreBase(String name) {
        super(name);
        this.size = 2;
        this.unitType = UnitTypes.poly;
        this.health = 1000;
        this.itemCapacity = 1000;
        this.unitCapModifier = 0;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team) {
        return true;
    }

    @Override
    public boolean canBreak(Tile tile) {
        return true;
    }
}
