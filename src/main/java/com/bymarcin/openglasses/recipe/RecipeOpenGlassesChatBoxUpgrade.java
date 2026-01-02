package com.bymarcin.openglasses.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;

import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeOpenGlassesChatBoxUpgrade implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (getItemCount(inv) > getRecipeSize()) return false;

        ItemStack chatbox = findChatBox(inv);
        ItemStack glasses = findGlasses(inv);

        return chatbox != null && glasses != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (getItemCount(inv) > getRecipeSize()) return null;

        ItemStack chatbox = findChatBox(inv);
        ItemStack glasses = findGlasses(inv);

        if (chatbox == null || glasses == null) return null;
        return OpenGlassesItem.setChatBoxUpgrade(glasses, true);
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    private static int getItemCount(InventoryCrafting inv) {
        int size = inv.getSizeInventory();
        int count = 0;
        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) continue;
            count++;
        }
        return count;
    }

    private static ItemStack findChatBox(InventoryCrafting inv) {
        int size = inv.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null || stack.getItem() == null) continue;
            if (stack.getItem()
                    != Item.getItemFromBlock(GameRegistry.findBlock("computronics", "computronics.chatBox")))
                continue;
            return stack;
        }
        return null;
    }

    private static ItemStack findGlasses(InventoryCrafting inv) {
        int size = inv.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null || stack.getItem() == null) continue;
            if (stack.getItem() != OpenGlasses.openGlasses) continue;
            if (OpenGlassesItem.hasChatBoxUpgrade(stack)) continue;
            return stack;
        }
        return null;
    }
}
