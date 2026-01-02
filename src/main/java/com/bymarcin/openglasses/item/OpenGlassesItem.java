package com.bymarcin.openglasses.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.event.ClientEventHandler;
import com.bymarcin.openglasses.utils.Location;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tconstruct.armor.player.TPlayerStats;
import tconstruct.library.accessory.IAccessory;

@Optional.InterfaceList({ @Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles"),
        @Optional.Interface(iface = "tconstruct.library.accessory.IAccessory", modid = "TConstruct") })
public class OpenGlassesItem extends ItemArmor implements IBauble, IAccessory {

    public static String chatBoxUpgradeStr = "HasChatBoxUpgrade";

    public OpenGlassesItem() {
        super(ArmorMaterial.CHAIN, 0, 0);
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(OpenGlasses.creativeTab);
        setUnlocalizedName("openglasses");
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(OpenGlasses.MODID + ":glasses");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return itemIcon;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return OpenGlasses.MODID + ":textures/models/glasses.png";
    }

    public static Location getUUID(ItemStack itemStack) {
        NBTTagCompound tag = getItemTag(itemStack);
        if (!tag.hasKey("X") || !tag.hasKey("Y") || !tag.hasKey("Z") || !tag.hasKey("uniqueKey")) return null;
        return new Location(
                tag.getInteger("X"),
                tag.getInteger("Y"),
                tag.getInteger("Z"),
                tag.getInteger("DIM"),
                tag.getLong("uniqueKey"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(itemStack, player, list, par4);
        Location uuid = getUUID(itemStack);
        if (uuid != null) {
            list.add("Link to:");
            for (String s : uuid.toArrayString()) {
                list.add(s);
            }
        }
        if (hasChatBoxUpgrade(itemStack)) {
            list.add("Installed ChatBox");
        }
    }

    public static boolean hasChatBoxUpgrade(ItemStack stack) {
        return stack != null && stack.hasTagCompound() && stack.getTagCompound().getBoolean(chatBoxUpgradeStr);
    }

    public static ItemStack setChatBoxUpgrade(ItemStack stack, boolean enable) {
        if (stack == null) return stack;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setBoolean(chatBoxUpgradeStr, enable);
        return stack;
    }

    public static NBTTagCompound getItemTag(ItemStack stack) {
        if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
        return stack.stackTagCompound;
    }

    public void bindToTerminal(ItemStack glass, Location uuid) {
        NBTTagCompound tag = getItemTag(glass);
        tag.setInteger("X", uuid.x);
        tag.setInteger("Y", uuid.y);
        tag.setInteger("Z", uuid.z);
        tag.setInteger("DIM", uuid.dimID);
        tag.setLong("uniqueKey", uuid.uniqueKey);
    }

    @Override
    @Optional.Method(modid = "TConstruct")
    public boolean canEquipAccessory(ItemStack itemStack, int slot) {
        return slot == 0;
    }

    /**
     * This method return the type of bauble this is. Type is used to determine the slots it can go into.
     *
     * @param itemstack
     */
    @Override
    @Optional.Method(modid = "Baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    /**
     * This method is called once per tick if the bauble is being worn by a player
     *
     * @param itemstack
     * @param player
     */
    @Override
    @Optional.Method(modid = "Baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    /**
     * This method is called when the bauble is equipped by a player
     *
     * @param itemstack
     * @param player
     */
    @Override
    @Optional.Method(modid = "Baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.worldObj.isRemote) return;

        if (player instanceof EntityPlayer)
            ClientEventHandler.equiped((EntityPlayer) player, OpenGlassesItem.getUUID(itemstack));
    }

    /**
     * This method is called when the bauble is unequipped by a player
     *
     * @param itemstack
     * @param player
     */
    @Override
    @Optional.Method(modid = "Baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.worldObj.isRemote) return;

        if (player instanceof EntityPlayer) ClientEventHandler.unEquiped((EntityPlayer) player);
    }

    /**
     * can this bauble be placed in a bauble slot
     *
     * @param itemstack
     * @param player
     */
    @Override
    @Optional.Method(modid = "Baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    /**
     * Can this bauble be removed from a bauble slot
     *
     * @param itemstack
     * @param player
     */
    @Override
    @Optional.Method(modid = "Baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    public static boolean isGlass(ItemStack item) {
        return item != null && item.getItem() instanceof OpenGlassesItem;
    }

    public static List<ItemStack> findAllEquippedGlasses(EntityPlayer player) {
        List<ItemStack> found = new ArrayList<>();
        if (player == null) return found;

        ItemStack glassesStack = player.inventory.armorInventory[3];
        if (isGlass(glassesStack)) {
            found.add(glassesStack);
        }

        if (OpenGlasses.tinkers) {
            glassesStack = TPlayerStats.get(player).armor.getStackInSlot(0);
            if (isGlass(glassesStack)) {
                found.add(glassesStack);
            }
        }

        if (OpenGlasses.baubles) // try bauble
        {
            IInventory handler = BaublesApi.getBaubles(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSizeInventory(); ++i) {
                    glassesStack = handler.getStackInSlot(i);
                    if (isGlass(glassesStack)) {
                        found.add(glassesStack);
                    }
                }
            }
        }
        return found;
    }

    public static boolean hasChaxBoxUpgrade(EntityPlayer player) {
        return findAllEquippedGlasses(player).stream().anyMatch(OpenGlassesItem::hasChatBoxUpgrade);
    }
}
