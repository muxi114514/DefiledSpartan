package krelox.defiledspartan.trait;

import com.oblivioussp.spartanweaponry.api.WeaponMaterial;
import krelox.defiledspartan.DefiledSpartan;
import krelox.spartantoolkit.BetterWeaponTrait;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 绯红收割：命中生物时吸取其生命。
 * 复刻污秽之地绯红收割者（ScarliteReaverItem）的效果——
 * 给攻击者施加一段极短但高倍率的再生（再生 XIX，持续 3 tick），等效于命中瞬回少量生命。
 */
public class ScarletReaverTrait extends BetterWeaponTrait {
    public ScarletReaverTrait() {
        super("scarlet_reaver", DefiledSpartan.MODID, TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public String getDescription() {
        return "Drains life from living things on hit";
    }

    @Override
    public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        if (target.isAlive()) {
            attacker.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 3, 18));
        }
    }
}
