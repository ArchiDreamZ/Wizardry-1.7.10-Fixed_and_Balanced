package electroblob.wizardry.spell;

import electroblob.wizardry.EnumElement;
import electroblob.wizardry.EnumSpellType;
import electroblob.wizardry.EnumTier;
import electroblob.wizardry.WizardryUtilities;
import electroblob.wizardry.entity.construct.EntityArrowRain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ArrowRain extends Spell {

	public ArrowRain() {
		super(EnumTier.MASTER, 15, EnumElement.SORCERY, "arrow_rain", EnumSpellType.ATTACK, 50, EnumAction.none, false);
	}

	@Override
	public boolean doesSpellRequirePacket(){
		return false;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, int ticksInUse, float damageMultiplier, float rangeMultiplier, float durationMultiplier, float blastMultiplier) {
		
		MovingObjectPosition rayTrace = WizardryUtilities.rayTrace(20*rangeMultiplier, world, caster, false);
		
		if(rayTrace != null && rayTrace.typeOfHit == MovingObjectType.BLOCK){
			if(!world.isRemote){
				double x = rayTrace.blockX;
				double y = rayTrace.blockY;
				double z = rayTrace.blockZ;
				// Moves the entity back towards the caster a bit, so the area of effect is better centred on the position.
				// 3.0d is the distance to move the entity back towards the caster.
				double dx = caster.posX - x;
				double dz = caster.posZ - z;
				double distRatio = 3.0d/Math.sqrt(dx*dx + dz*dz);
				x += dx*distRatio;
				z += dz*distRatio;
				
				EntityArrowRain arrowrain = new EntityArrowRain(world, x, y + 5, z, caster, (int)(12*durationMultiplier), damageMultiplier);
				arrowrain.rotationYaw = caster.rotationYawHead;
				world.spawnEntityInWorld(arrowrain);
			}
			caster.swingItem();
			world.playSoundAtEntity(caster, "wizardry:darkaura", 1.0F, 1.0F);
			return true;
		}
		return false;
	}

}
