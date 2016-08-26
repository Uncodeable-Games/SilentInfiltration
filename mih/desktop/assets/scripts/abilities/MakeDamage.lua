--
-- Created by IntelliJ IDEA.
-- User: Cataract
-- Date: 15.04.2016
-- Time: 16:26
-- To change this template use File | Settings | File Templates.
--
statsC = luajava.bindClass("de.mih.core.game.components.StatsC")

_DAMAGE = 30

function onTarget(caster,targetId,intersection)
	local em = currentGame:getCurrentGame():getEntityManager()

	if not em:hasComponent(targetId,statsC) then return end

	--local stats = em:getComponent(targetId,statsC)

	--stats:setCurrentLife(stats:getCurrentLife() - _DAMAGE)
	currentGame:getEventManager():fire(luajava.newInstance("de.mih.core.game.events.stats.DamageEvent",_DAMAGE,caster,targetId));
end

function onPoint(caster, target)
	print(caster.." : "..target.x..","..target.y..","..target.z)
end

function onNoTarget(caster)
end


