function onTarget(caster,targetId)
	local tileborder = currentGame:getEntityManager():getComponent(targetId,"de.mih.core.game.components.BorderC"):getTileBorder()
    if tileborder:getDoor():isClosed() then
		tileborder:getDoor():open()
	else
		tileborder:getDoor():close()
	end
end