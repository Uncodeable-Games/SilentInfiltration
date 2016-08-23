--
-- Created by IntelliJ IDEA.
-- User: Cataract
-- Date: 23.04.2016
-- Time: 11:40
-- To change this template use File | Settings | File Templates.
--

borderC = luajava.bindClass("de.mih.core.game.components.BorderC")

function onTarget(caster,targetId,intersection)
	local eM = currentGame:getEntityManager()

	if not eM:hasComponent(targetId,borderC) then return end

	local tileborder = eM:getComponent(targetId,borderC):getTileBorder()

	local index = tileborder:getTextureIndexByAdjacentTile(currentGame:getTilemap():getTileAt(intersection.x,intersection.z))

	if (index == -1) then return end

	tileborder:setTexture(index,"assets/textures/walls/wall-tile3.png")
end

function onPoint(caster, target)
	print(caster.." : "..target.x..","..target.y..","..target.z)
end

function onNoTarget(caster)
end

