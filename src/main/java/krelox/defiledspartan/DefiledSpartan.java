package krelox.defiledspartan;

import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.defiledspartan.trait.ScarletReaverTrait;
import krelox.spartantoolkit.*;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import lykrast.defiledlands.common.item.ModTiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod(DefiledSpartan.MODID)
public class DefiledSpartan extends SpartanAddon {
    public static final String MODID = "defiledspartan";

    public static final WeaponMap WEAPONS = new WeaponMap();
    public static final DeferredRegister<Item> ITEMS = itemRegister(MODID);
    public static final DeferredRegister<WeaponTrait> TRAITS = traitRegister(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = tabRegister(MODID);

    // 仅绯红需要额外效果：命中生物时吸取生命（复刻污秽之地绯红收割者）
    public static final RegistryObject<WeaponTrait> SCARLET_REAVER = registerTrait(TRAITS, new ScarletReaverTrait());

    public static final ArrayList<SpartanMaterial> MATERIALS = new ArrayList<>();

    // 影素：暗紫色调，无额外效果。标签使用污秽之地实际所在的 c: 命名空间
    public static final SpartanMaterial UMBRIUM = material(
            "umbrium", ModTiers.UMBRIUM, "c:ingots/umbrium");

    // 绯红：猩红色调，命中吸血
    public static final SpartanMaterial SCARLITE = material(
            "scarlite", ModTiers.SCARLITE, "c:gems/scarlite", SCARLET_REAVER);

    @SafeVarargs
    private static SpartanMaterial material(String name, Tier tier, String tagPath,
            RegistryObject<WeaponTrait>... traits) {
        SpartanMaterial mat = new SpartanMaterial(name, MODID, tier,
                ItemTags.create(new ResourceLocation(tagPath)), traits);
        MATERIALS.add(mat);
        return mat;
    }

    public static final RegistryObject<CreativeModeTab> DEFILED_SPARTAN_TAB = registerTab(
            TABS, MODID,
            () -> WEAPONS.get(UMBRIUM, WeaponType.GREATSWORD).get(),
            (parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())));

    public DefiledSpartan() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        registerSpartanWeapons(ITEMS);
        ITEMS.register(bus);
        TRAITS.register(bus);
        TABS.register(bus);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        WEAPONS.forEach((key, item) -> {
            WeaponType type = key.second();
            SpartanMaterial material = key.first();
            if (type.name().equals("GLAIVE")) {
                com.oblivioussp.spartanweaponry.api.data.recipe.RecipeProviderHelper
                        .recipeGlaive(consumer, material.getPole(), material.getRepairTag(),
                                item.get(), "has_" + material.getMaterialName(), "");
            } else if (type.name().equals("QUARTERSTAFF")) {
                com.oblivioussp.spartanweaponry.api.data.recipe.RecipeProviderHelper
                        .recipeQuarterstaff(consumer, material.getPole(), material.getRepairTag(),
                                item.get(), "has_" + material.getMaterialName(), "");
            } else {
                type.recipe.accept(getWeaponMap(), consumer, material);
            }
        });
    }

    @Override
    public String modid() { return MODID; }

    @Override
    public List<SpartanMaterial> getMaterials() { return MATERIALS; }

    @Override
    public WeaponMap getWeaponMap() { return WEAPONS; }
}
