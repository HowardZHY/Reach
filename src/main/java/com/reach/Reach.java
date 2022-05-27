package com.reach;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "Reach5", version = "0", acceptedMinecraftVersions = "[1.7.2,)")
public class Reach {
  public static final String NAME = "Reach5";
  
  public static final String VERSION = "0";
  
  protected Minecraft mc = Minecraft.getMinecraft();
  
  private Entity pointedEntity;
  
  private float reach = 3.9F;
  
  private MovingObjectPosition moving;
  
  double random = Math.random();
  
  @EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    FMLCommonHandler.instance().bus().register(this);
  }
  
  @SubscribeEvent
  public void onMouse(MouseEvent e) {
    try {
      if (this.moving != null && e.button == 0 && e.buttonstate)
        this.mc.objectMouseOver = this.moving; 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent e) {
    if (this.mc.theWorld != null) {
      getMouseOver(1.0F);
      if (Math.random() > 0.20000000298023224D) {
        this.reach = 6.6F;
      } else {
        this.reach = 3.9F;
      } 
    } 
  }
  
  private void getMouseOver(float p_78473_1_) {
    if (Minecraft.getMinecraft().getRenderViewEntity() != null && 
      (Minecraft.getMinecraft()).theWorld != null) {
      (Minecraft.getMinecraft()).pointedEntity = null;
      double d0 = this.reach;
      this.moving = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(d0, p_78473_1_);
      double d1 = d0;
      Vec3 vec3 = Minecraft.getMinecraft().getRenderViewEntity().getPositionEyes(p_78473_1_);
      if (this.moving != null)
        d1 = this.moving.hitVec.distanceTo(vec3); 
      Vec3 vec31 = Minecraft.getMinecraft().getRenderViewEntity().getLook(p_78473_1_);
      Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
      this.pointedEntity = null;
      Vec3 vec33 = null;
      float f1 = 1.0F;
      List<Entity> list = (Minecraft.getMinecraft()).theWorld.getEntitiesWithinAABBExcludingEntity(
          Minecraft.getMinecraft().getRenderViewEntity(), 
          Minecraft.getMinecraft().getRenderViewEntity().getEntityBoundingBox()
          .addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0)
          .expand(f1, f1, f1));
      double d2 = d1;
      for (int i = 0; i < list.size(); i++) {
        Entity entity = list.get(i);
        if (entity.canBeCollidedWith()) {
          float f2 = 0.13F;
          AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f2, f2, f2);
          MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
          if (axisalignedbb.isVecInside(vec3)) {
            if (0.0D < d2 || d2 == 0.0D) {
              this.pointedEntity = entity;
              vec33 = (movingobjectposition == null) ? vec3 : movingobjectposition.hitVec;
              d2 = 0.0D;
            } 
          } else if (movingobjectposition != null) {
            double d3 = vec3.distanceTo(movingobjectposition.hitVec);
            if (d3 < d2 || d2 == 0.0D)
              if (entity == (Minecraft.getMinecraft().getRenderViewEntity()).ridingEntity && 
                !entity.canRiderInteract()) {
                if (d2 == 0.0D) {
                  this.pointedEntity = entity;
                  vec33 = movingobjectposition.hitVec;
                } 
              } else {
                this.pointedEntity = entity;
                vec33 = movingobjectposition.hitVec;
                d2 = d3;
              }  
          } 
        } 
      } 
      if (this.pointedEntity != null && (d2 < d1 || this.moving == null)) {
        this.moving = new MovingObjectPosition(this.pointedEntity, vec33);
        if (this.pointedEntity instanceof net.minecraft.entity.EntityLivingBase || this.pointedEntity instanceof net.minecraft.entity.item.EntityItemFrame)
          (Minecraft.getMinecraft()).pointedEntity = this.pointedEntity; 
      } 
    } 
  }
}