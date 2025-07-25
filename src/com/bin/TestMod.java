package com.bin;

import arc.util.Log;
import com.bin.content.contentLists.MyContextList;
import com.bin.content.contentLists.MyTechTreeList;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.modules.ItemModule;

/**
 * @author bin
 */
public class TestMod extends mindustry.mod.Mod {
    public static TestMod instance;

    public TestMod() {
        Log.info("加载TestMod构造器");
        TestMod.instance = this;
    }

    @Override
    public void init() {
        Log.info("加载TestMod init");

        // Events.on(EventType.BlockDestroyEvent.class, TestMod::blockDestroyEvent);
        // Events.on(EventType.WorldLoadEvent.class, TestMod::changeRule);

        Log.info("加载TestMod init完成");
    }

    private static void blockDestroyEvent(EventType.BlockDestroyEvent e) {
        Team team = Vars.player.team();
        if (team != e.tile.team()) {
            Block block = e.tile.block();
            ItemModule items = team.items();
            if (items.length() == 0) {
                return;
            }
            CoreBlock.CoreBuild core = team.core();
            for (ItemStack stack : block.requirements) {
                if (core.acceptItem(null, stack.item)) {
                    core.items.add(stack.item, stack.amount);
                }
            }
        }
    }

    private static void changeRule(Object e) {
        GameState state = Vars.state;
        if (state == null) {
            return;
        }
        Rules rules = state.rules;
        if (rules == null) {
            return;
        }
        rules.coreIncinerates = true;
        rules.lighting = false;
        rules.staticFog = false;
        rules.fog = false;
        rules.showSpawns = true;
        rules.coreCapture = true;
    }

    @Override
    public void loadContent() {
        Log.info("加载TestMod方块");

        MyContextList.load();

        MyTechTreeList.load();

        Blocks.powerSource.buildVisibility = BuildVisibility.shown;
        Blocks.powerVoid.buildVisibility = BuildVisibility.shown;
        Blocks.itemSource.buildVisibility = BuildVisibility.shown;
        Blocks.itemVoid.buildVisibility = BuildVisibility.shown;
        Blocks.liquidSource.buildVisibility = BuildVisibility.shown;
        Blocks.liquidVoid.buildVisibility = BuildVisibility.shown;
        Blocks.payloadSource.buildVisibility = BuildVisibility.shown;
        Blocks.payloadVoid.buildVisibility = BuildVisibility.shown;
        Blocks.illuminator.buildVisibility = BuildVisibility.shown;
        Blocks.heatSource.buildVisibility = BuildVisibility.shown;

        Vars.mods.getMod(TestMod.class).meta.hidden = true;

        Log.info("加载TestMod方块完成");
    }


}
