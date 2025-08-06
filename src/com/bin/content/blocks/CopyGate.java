package com.bin.content.blocks;

import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

/**
 * @author bin
 * @since 2025/08/06
 */
public class CopyGate extends Block {

    public CopyGate(String name) {
        super(name);
        hasItems = true;
        underBullets = true;
        update = false;
        destructible = true;
        group = BlockGroup.transportation;
        instantTransfer = true;
        unloadable = false;
        canOverdrive = false;
        itemCapacity = 0;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    protected void initBuilding() {
        subclass = CopyGate.class;
        buildType = CopyGateBuild::new;
    }

    public static class CopyGateBuild extends Building {

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (team != source.team || item == null) {
                return false;
            }

            for (int i = 0; i < proximity.size; i++) {
                Building other = proximity.get(i);
                if (other == source || other == this) {
                    continue;
                }
                if (other.acceptItem(this, item)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void handleItem(Building source, Item item) {
            if (team != source.team || item == null) {
                return;
            }

            for (int i = 0; i < proximity.size; i++) {
                Building other = proximity.get(i);
                if (other == source || other == this) {
                    continue;
                }

                if (other.acceptItem(this, item)) {
                    other.handleItem(this, item);
                }
            }
        }

    }
}
