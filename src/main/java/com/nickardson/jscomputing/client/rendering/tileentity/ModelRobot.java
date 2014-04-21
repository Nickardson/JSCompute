package com.nickardson.jscomputing.client.rendering.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRobot extends ModelBase
{
    ModelRenderer Body;
    ModelRenderer Thruster1;
    ModelRenderer Thruster2;
    ModelRenderer Thruster3;
    ModelRenderer Thruster4;
    ModelRenderer Head;

    public ModelRobot()
    {
        textureWidth = 64;
        textureHeight = 32;

        Body = new ModelRenderer(this, 0, 0);
        Body.addBox(0F, 0F, 0F, 10, 3, 10);
        Body.setRotationPoint(-5F, -2F, -5F);
        Body.setTextureSize(64, 32);
        Body.mirror = true;
        setRotation(Body, 0F, 0F, 0F);
        Thruster1 = new ModelRenderer(this, 40, 0);
        Thruster1.addBox(0F, 0F, 0F, 3, 1, 3);
        Thruster1.setRotationPoint(-4F, 1F, -4F);
        Thruster1.setTextureSize(64, 32);
        Thruster1.mirror = true;
        setRotation(Thruster1, 0F, 0F, 0F);
        Thruster2 = new ModelRenderer(this, 40, 0);
        Thruster2.addBox(0F, 0F, 0F, 3, 1, 3);
        Thruster2.setRotationPoint(1F, 1F, -4F);
        Thruster2.setTextureSize(64, 32);
        Thruster2.mirror = true;
        setRotation(Thruster2, 0F, 0F, 0F);
        Thruster3 = new ModelRenderer(this, 40, 0);
        Thruster3.addBox(0F, 0F, 0F, 3, 1, 3);
        Thruster3.setRotationPoint(-4F, 1F, 1F);
        Thruster3.setTextureSize(64, 32);
        Thruster3.mirror = true;
        setRotation(Thruster3, 0F, 0F, 0F);
        Thruster4 = new ModelRenderer(this, 40, 0);
        Thruster4.addBox(0F, 0F, 0F, 3, 1, 3);
        Thruster4.setRotationPoint(1F, 1F, 1F);
        Thruster4.setTextureSize(64, 32);
        Thruster4.mirror = true;
        setRotation(Thruster4, 0F, 0F, 0F);
        Head = new ModelRenderer(this, 40, 4);
        Head.addBox(0F, 0F, 0F, 6, 1, 6);
        Head.setRotationPoint(-3F, -3F, -4F);
        Head.setTextureSize(64, 32);
        Head.mirror = true;
        setRotation(Head, 0F, 0F, 0F);
    }

    public void render(Entity entity, float time, float max, float j, float k, float l, float m)
    {
        Body.render(m);
        Thruster1.render(m);
        Thruster2.render(m);
        Thruster3.render(m);
        Thruster4.render(m);
        Head.render(m);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
