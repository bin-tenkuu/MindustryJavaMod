package com.bin.content.contentLists;

import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.util.Time;
import com.bin.Tools;
import com.bin.content.blocks.CommendBlock;
import com.bin.content.blocks.LinkCoreBlock;
import com.bin.consume.ConsumeLiquids;
import com.bin.content.blocks.FastestDrill;
import com.bin.content.blocks.ItemChange;
import com.bin.content.blocks.LiquidPowerDriver;
import com.bin.content.blocks.MapTurret;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
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
import mindustry.world.blocks.defense.turrets.ChargeTurret;
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
      Bin_LiquidPower,
      Bin_LaserTurret;

  @Override public void load() {
    Bin_ItemChange = new ItemChange("Bin_ItemChange") {
      {
        this.localizedName = "����ת����";
        this.description = "������Ʒ,ת��Ϊ��������һ����Ʒ";
        this.requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_LinkCore = new LinkCoreBlock("Bin_LinkCore") {
      {
        this.localizedName = "���Ӻ���������";
        this.description = "ͨ������ͨ�����������,ͬʱװ����װж��";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 1;
        this.health = 320;
      }
    };
    Bin_Commend1 = new CommendBlock("Bin_Commend1") {
      {
        this.localizedName = "ָ����";
        this.description = "���ɸ���ָ��";
        this.size = 2;
        this.requirements(Category.effect, BuildVisibility.shown, ItemStack.empty);
        commend = MyContextList::display;
      }
    };
    Bin_CommendCall = new CommendBlock("Bin_CommendCall") {
      {
        this.localizedName = "�ٻ���";
        this.description = "�ٻ�ָ����λ";
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
        this.localizedName = "������ͷ";
        this.description = "[red]���޲���,���ɼ���[]\n" +
                           "[green]���ڶ��ݵ���ͷ���ĵ���ѧ����һ����," +
                           "Խ����Ҫ��ĸ���,Խ�ǻ��ܵ���������," +
                           "���ǳ�Խ��ͷ,�Ҳ�����ͷ��![]\n" +
                           "(���ڲ����ٶȵ�����ƷԴ";
        this.size = 2;
        this.requirements(Category.production, BuildVisibility.shown, ItemStack.with(Items.copper, 20));
      }
    };
    Bin_Block1 = new SwitchBlock("Bin_Block1") {
      {
        this.localizedName = "��Ԥ���ָ";
        this.description = "�������ȫͼԼһ��Ľ���\n" +
                           "[red]����[]����,[red]ǽ�봫�ʹ�[]���ڼ���ԭ���ȡ����,���������";
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
        this.localizedName = "�˵����侮";
        this.description = "ȫͼ����,������";
        this.range = 480;
        this.size = 4;
        this.requirements(Category.turret, ItemStack.with(Items.copper, 50));
        this.shootType = MyBulletList.Bin_Bullet1;
      }
    };
    Bin_LargeCore = new CoreBlock("Bin_LargeCore") {
      {
        this.localizedName = "��Ԫ����";
        this.description = "��ǿ�Ⱥ���,���вֿ�";
        this.size = 6;
        this.health = 10000;
        this.itemCapacity = 100000;
        requirements(Category.effect, ItemStack.with(Items.copper, 64));
      }
    };
    Bin_LargeStorage = new StorageBlock("Bin_LargeStorage") {
      {
        this.localizedName = "��Ԫ�ֿ�";
        this.description = "��ǿ�Ȳֿ�,���к���";
        this.requirements(Category.effect, ItemStack.with(Items.copper, 50));
        this.size = 4;
        this.itemCapacity = 10000;
        this.flags = EnumSet.of(BlockFlag.storage);
      }
    };
    Bin_LiquidPower = new LiquidPowerDriver("Bin_LiquidPower") {
      {
        this.localizedName = "Һ���²��վ";
        this.description = "ʹ��Һ����¶ȷ�Ӧ����";
        this.requirements(Category.distribution, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        this.size = 3;
        this.liquidDuration = 2;
        this.liquidCapacity = 30;
        this.powerProduction = 50;
        this.requirements(Category.power, ItemStack.with(Items.copper, 50));
        this.consumes.add(new ConsumeLiquids(new LiquidStack[]{
            new LiquidStack(Liquids.slag, 3),
//            new LiquidStack(Liquids.oil, 3),
            new LiquidStack(Liquids.water, 3),
        }));
      }
    };
    Bin_LaserTurret = new ChargeTurret("Bin_LaserTurret") {
      {
        size = 2;
        health = 3600;
        shootSound = Sounds.laser;
        ammoPerShot = 2;
        range = 420;
        liquidCapacity = 60;
        inaccuracy = 0;
        chargeTime = 30;
        chargeEffect = Fx.lancerLaserCharge;
        chargeBeginEffect = Fx.lancerLaserChargeBegin;
        powerUse = 1;
        requirements(Category.turret, BuildVisibility.shown, ItemStack.with(Items.copper, 50));
        shootType = MyBulletList.Bin_Bullet2;
      }
    };
  }

  private static void display(Building building, Table table) {
    table.button(Icon.upOpen, Styles.clearTransi, (() -> Vars.logic.skipWave())).size(50).tooltip("��һ��");
    table.button(Icon.warningSmall, Styles.clearTransi, (() -> {
      for (int i = 0; i < 10; i++) {
        Vars.logic.runWave();
      }
    })).size(50).tooltip("��10��");
    table.button(Icon.file, Styles.clearTransi, () ->
        Groups.all.each(syncs -> syncs instanceof Firec, Entityc::remove)
    ).size(50).tooltip("�������");
    table.button(Icon.cancel, Styles.clearTransi, () ->
        Groups.unit.each(Unitc::destroy)
    ).size(50).tooltip("������е�λ");
    table.row();
  }
}
