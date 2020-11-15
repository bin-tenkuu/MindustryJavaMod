package com.content;

import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.util.Time;
import com.Tools;
import com.consume.ConsumeLiquids;
import com.content.blocks.CommendBlock;
import com.content.blocks.FastestDrill;
import com.content.blocks.ItemChange;
import com.content.blocks.LinkCoreBlock;
import com.content.blocks.MapTurret;
import com.content.blocks.LiquidPowerDriver;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Building;
import mindustry.gen.Entityc;
import mindustry.gen.Firec;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.blocks.logic.SwitchBlock;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BuildVisibility;

/**
 * @author bin
 * @version 1.0.0
 */
public class MyContextList implements ContentList {
  public static Block
      Bin_ItemChange,
      Bin_LinkCore,
      Bin_Commend1,
      Bin_CommendCall,
      Bin_SourceDrill,
      Bin_Block1,
      Bin_MapTurret,
      Bin_LargeCore,
      Bin_LargeStorage,
      Bin_LiquidPower;

  @Override public void load() {
    Bin_ItemChange = new ItemChange("Bin_ItemChange") {
      {
        this.localizedName = "等量转换器";
        this.description = "输入物品,转换为等量的另一个物品";
        this.requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_LinkCore = new LinkCoreBlock("Bin_LinkCore") {
      {
        this.localizedName = "量子核心连接器";
        this.description = "通过量子通道与核心相连,同时装载了装卸器";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_Commend1 = new CommendBlock("Bin_Commend1") {
      {
        this.localizedName = "指令器";
        this.description = "集成各种指令";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
        commend = MyContextList::display;
      }
    };
    Bin_CommendCall = new CommendBlock("Bin_CommendCall") {
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
    Bin_SourceDrill = new FastestDrill("Bin_SourceDrill") {
      {
        this.localizedName = "量子钻头";
        this.description = "[red]极限产出,不可加速[]\n" +
                           "[green]我在短暂的钻头生涯当中学到了一件事," +
                           "越是想要钻的更快,越是会受到各种限制," +
                           "除非超越钻头,我不做钻头了![]\n" +
                           "(现在产出速度等于物品源";
        this.size = 2;
        this.requirements(Category.production, BuildVisibility.shown, ItemStack.with(Items.copper, 20));
      }
    };
    Bin_Block1 = new SwitchBlock("Bin_Block1") {
      {
        this.localizedName = "灭霸の响指";
        this.description = "随机销毁全图约一半的建筑\n" +
                           "[red]核心[]除外,[red]墙与传送带[]由于技术原因获取不到,不进入计算";
        this.requirements(Category.logic, BuildVisibility.shown, ItemStack.with(Items.copper, 5));
        this.size = 2;
      }

      @Override protected void initBuilding() {
        this.buildType = () -> new SwitchBuild() {
          @Override public boolean configTapped() {
            this.kill();
            Sounds.click.at(this);
            Time.run(10, () -> {
              boolean b = true;
              for (int i = 0, n = 10; i < Groups.build.size(); i++) {
                Building building = Groups.build.index(i);
                if (building instanceof CoreBlock.CoreBuild) {
                  continue;
                }
                if ((b = !b)) {
                  Time.run(n++, building::kill);
                }
              }
            });
            return false;
          }
        };
      }
    };
    Bin_MapTurret = new MapTurret("Bin_MapTurret") {
      {
        this.localizedName = "核弹发射井";
        this.description = "全图攻击,近距离";
        this.range = 480;
        this.size = 4;
        this.requirements(Category.turret, ItemStack.with(Items.copper, 50));
        this.shootType = new BasicBulletType(0, 1000, "Bin_MapBullet") {
          {

            this.splashDamageRadius = 50;
            this.splashDamage = 1000;
            this.pierce = false;
            this.collidesTiles = false;
            this.collidesTeam = false;
            this.collidesAir = true;
            this.collides = false;
            this.keepVelocity = false;
            this.lifetime = 60;
            this.drag = 0;
            this.knockback = 0.5F;
            this.hitSize = 1;
            this.hittable = false;
            this.hitSound = Sounds.explosion;
            this.hitShake = 0.5F;
            this.lightning = 5;
          }
        };
      }
    };
    Bin_LargeCore = new CoreBlock("Bin_LargeCore") {
      {
        this.localizedName = "次元核心";
        this.description = "高强度核心,内有仓库";
        this.size = 6;
        this.health = 10000;
        this.itemCapacity = 100000;
        requirements(Category.effect, ItemStack.with(Items.copper, 64));
      }
    };
    Bin_LargeStorage = new StorageBlock("Bin_LargeStorage") {
      {
        this.localizedName = "次元仓库";
        this.description = "高强度仓库,内有核心";
        this.requirements(Category.effect, ItemStack.with(Items.copper, 50));
        this.size = 4;
        this.itemCapacity = 10000;
        this.flags = EnumSet.of(BlockFlag.storage);
      }
    };
    Bin_LiquidPower = new LiquidPowerDriver("Bin_LiquidPower") {
      {
        this.localizedName = "液体温差发电站";
        this.description = "使用液体间温度反应发电";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 3;
        this.liquidDuration = 1;
        this.liquidCapacity = 60;
        this.powerProduction = 30;
        this.requirements(Category.power, ItemStack.with(Items.copper, 50));
        this.consumes.add(new ConsumeLiquids(new LiquidStack[]{
            new LiquidStack(Liquids.slag, 6),
            new LiquidStack(Liquids.oil, 6),
            new LiquidStack(Liquids.cryofluid, 3),
        }));
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
    table.button(Icon.cancel, Styles.clearTransi, () ->
        Groups.unit.each(Unitc::destroy)
    ).size(50).tooltip("清除所有单位");
    table.row();
  }
}
