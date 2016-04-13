function onTarget(caster,targetId)
	local tileborder = currentGame:getEntityManager():getComponent(targetId,"BorderC"):getTileBorder()
    if tileborder:getDoor():isClosed() then
		tileborder:getDoor():open()
	else
		tileborder:getDoor():close()
	end
end

function onNoTarget()
end

function onPoint(caster, target)
end