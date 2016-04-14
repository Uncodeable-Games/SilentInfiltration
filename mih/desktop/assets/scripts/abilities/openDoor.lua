borderC = luajava.bindClass("de.mih.core.game.components.BorderC")

function onTarget(caster,targetId)
	local eM = currentGame:getEntityManager()

	if not eM:hasComponent(targetId,borderC) then return end

	if not eM:getComponent(targetId,borderC):getTileBorder():isDoor() then return end

	local door = eM:getComponent(targetId,borderC):getTileBorder():getDoor()

    if door:isClosed() then
		door:open()
	else
		door:close()
	end
end

function onPoint(caster, target)
	print(caster.." : "..target.x..","..target.y..","..target.z)
end

function onNoTarget(caster)
	currentGame:getActivePlayer():setAbilityBeingTargeted(Ability)
	currentGame:getActivePlayer():setTargeting(true)
end