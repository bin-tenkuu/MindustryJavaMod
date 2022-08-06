package com.bin.content.blocks;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

import java.util.function.BiConsumer;

/**
 * @author bin
 * @version 1.0.0
 */
public class CommendBlock extends Block {
    public BiConsumer<Building, Table> commend;

    public CommendBlock(String name) {
        super(name);
        hasItems = false;
        update = true;
        solid = false;
        configurable = true;
        group = BlockGroup.transportation;
        alwaysUnlocked = true;
    }

    @Override
    protected void initBuilding() {
        buildType = CommendBuild::new;
    }

    public class CommendBuild extends Building {
        public CommendBuild() {
        }

        @Override
        public void buildConfiguration(Table table) {
            if (commend != null) {
                commend.accept(this, table);
            }
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (this == other) {
                deselect();
                configure(null);
                return false;
            } else {
                return true;
            }
        }

    }
}
