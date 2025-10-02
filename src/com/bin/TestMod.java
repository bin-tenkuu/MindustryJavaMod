package com.bin;

import arc.Events;
import arc.util.Log;
import com.bin.content.contentLists.MyContextList;
import com.bin.content.contentLists.MyTechTreeList;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 */
@SuppressWarnings("unused")
public class TestMod extends mindustry.mod.Mod {

    @Override
    public void init() {
        Log.info("加载TestMod init");

        // Events.on(EventType.WorldLoadEvent.class, TestMod::changeRule);

        Log.info("加载TestMod init完成");
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
        rules.lighting = true;
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
