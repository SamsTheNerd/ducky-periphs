package com.samsthenerd.duckyperiphs.utils;

import javax.annotation.Nullable;

import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;


// implemented from https://fabricmc.net/wiki/tutorial:pixel_raycast#general_casearbitrary_pixel
public class BlockHitFromScreen {
    @Nullable
    public static BlockHitResult getHit(float tickDelta, int x, int y){
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        Vec3d cameraDirection = client.cameraEntity.getRotationVec(tickDelta);
        double fov = (double)client.options.getFov().getValue();
        double angleSize = fov/height;
        Vector3f verticalRotationAxis = cameraDirection.toVector3f();
        verticalRotationAxis.cross(0.0F,1.0F,0.0F); // no clue if this is right !
        verticalRotationAxis.normalize();
        // if(!verticalRotationAxis.normalize()) {
        //     return null;//The camera is pointing directly up or down, you'll have to fix this one
        // }
        
        Vector3f horizontalRotationAxis = cameraDirection.toVector3f();
        horizontalRotationAxis.cross(verticalRotationAxis);
        horizontalRotationAxis.normalize();
        
        verticalRotationAxis = cameraDirection.toVector3f();
        verticalRotationAxis.cross(horizontalRotationAxis);

        Vec3d direction = map(
            (float) angleSize,
            cameraDirection,
            RotationAxis.of(horizontalRotationAxis),
            RotationAxis.of(verticalRotationAxis),
            x,
            y,
            width,
            height
        );
        HitResult hit = raycastInDirection(client, tickDelta, direction);
        
        if(hit.getType() == HitResult.Type.BLOCK){
            BlockHitResult blockHit = (BlockHitResult) hit;
            return blockHit;
        } else {
            return null;
        }

        // switch(hit.getType()) {
        //     case MISS:
        //         //nothing near enough
        //         break; 
        //     case BLOCK:
        //         BlockHitResult blockHit = (BlockHitResult) hit;
        //         BlockPos blockPos = blockHit.getBlockPos();
        //         BlockState blockState = client.world.getBlockState(blockPos);
        //         Block block = blockState.getBlock();
        //         break; 
        //     case ENTITY:
        //         EntityHitResult entityHit = (EntityHitResult) hit;
        //         Entity entity = entityHit.getEntity();
        //         break; 
        //     default:
        //         break;
        // }
    }

    private static Vec3d map(float anglePerPixel, Vec3d center, RotationAxis horizontalRotationAxis,
        RotationAxis verticalRotationAxis, int x, int y, int width, int height) {
        float horizontalRotation = (x - width/2f) * anglePerPixel;
        float verticalRotation = (y - height/2f) * anglePerPixel;
     
        final Vector3f temp2 = center.toVector3f();
        temp2.rotate(verticalRotationAxis.rotation(verticalRotation));
        temp2.rotate(horizontalRotationAxis.rotation(horizontalRotation));
        return new Vec3d(temp2);
    }

    private static HitResult raycastInDirection(MinecraftClient client, float tickDelta, Vec3d direction) {
        return raycastInDirection(client, tickDelta, direction, client.interactionManager.getReachDistance());
    }

    private static HitResult raycastInDirection(MinecraftClient client, float tickDelta, Vec3d direction, double reachDistance) {
        Entity entity = client.getCameraEntity();
        if (entity == null || client.world == null) {
            return null;
        }
     
        HitResult target = raycast(entity, reachDistance, tickDelta, false, direction);
        boolean tooFar = false;
        double extendedReach = reachDistance;
        if (client.interactionManager.hasExtendedReach()) {
            extendedReach = 6.0D;//Change this to extend the reach
            reachDistance = extendedReach;
        } else {
            if (reachDistance > 3.0D) {
                tooFar = true;
            }
        }
     
        Vec3d cameraPos = entity.getCameraPosVec(tickDelta);
     
        extendedReach = extendedReach * extendedReach;
        if (target != null) {
            extendedReach = target.getPos().squaredDistanceTo(cameraPos);
        }
     
        Vec3d vec3d3 = cameraPos.add(direction.multiply(reachDistance));
        Box box = entity
                .getBoundingBox()
                .stretch(entity.getRotationVec(1.0F).multiply(reachDistance))
                .expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(
                entity,
                cameraPos,
                vec3d3,
                box,
                (entityx) -> !entityx.isSpectator() && entityx.canHit(),
                extendedReach
        );
     
        if (entityHitResult == null) {
            return target;
        }
     
        Entity entity2 = entityHitResult.getEntity();
        Vec3d vec3d4 = entityHitResult.getPos();
        double g = cameraPos.squaredDistanceTo(vec3d4);
        if (tooFar && g > 9.0D) {
            return null;
        } else if (g < extendedReach || target == null) {
            target = entityHitResult;
            if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                client.targetedEntity = entity2;
            }
        }
     
        return target;
    }
     
    private static HitResult raycast(
            Entity entity,
            double maxDistance,
            float tickDelta,
            boolean includeFluids,
            Vec3d direction
    ) {
        Vec3d end = entity.getCameraPosVec(tickDelta).add(direction.multiply(maxDistance));
        return entity.getWorld().raycast(new RaycastContext(
                entity.getCameraPosVec(tickDelta),
                end,
                RaycastContext.ShapeType.OUTLINE,
                includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                entity
        ));
    }
}
