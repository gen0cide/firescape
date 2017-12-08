function drawUi() {
    drawHudObjectNames();
    drawAboveHeadStuff();
    drawHudInventory();
    drawHudMisc();
    drawHudSkills();
    mc.drawDialogCombatStyle();
}

function drawHudObjectNames() {

    var objlocs = {};

    // this uses the same calc as the sprite rendering, by my care factor is fairly low for messing with the projection
    for (var model = 0; model < mc.scene.visiblePolygonsCount; model++) {
        var polygon = mc.scene.visiblePolygons[model];
        var polygonModel = polygon.model;
        var polyface = polygon.face;
        if (polygonModel != null && polygonModel != mc.scene.view && !polygonModel.unpickable && polygonModel.isLocalPlayer[polyface] == 0 && polygonModel.key >= 0) {
            var faceverts = polygonModel.faceVertices[polyface];
            var face_0 = faceverts[0];
            var vx = polygonModel.vertexViewX[face_0];
            var vy = polygonModel.vertexViewY[face_0];
            var vz = polygonModel.projectVertexZ[face_0];
            var w = (vx << mc.scene.viewDistance) / vz;
            var h = (vy << mc.scene.viewDistance) / vz;
            var x = vx - w / 2;
            var y = (mc.scene.baseY + vy) - h;
            x = x + mc.scene.baseX;
            var rect;
            if (objlocs[polygonModel.key] != null) {
                rect = objlocs[polygonModel.key];
                rect.add(x, y);
                rect.add(x + w, y + h);
            } else {
                rect = new java.awt.Rectangle(x, y, w, h);
                objlocs[polygonModel.key] = rect;
            }
        }
    }

    if (mc.debugHud == mc._DEBUG_HUD_HITBOXES || mc.debugHud == mc._DEBUG_HUD_NAMES || mc.debugHud == mc._DEBUG_HUD_ALL) {
        for (key in objlocs) {
            rect = objlocs[key];
            var dist;
            if (key >= 10000) {
                dist = mc.distance(mc.localRegionX, mc.localRegionY, mc.wallObjectX[key - 10000], mc.wallObjectY[key - 10000]);
            } else {
                dist = mc.distance(mc.localRegionX, mc.localRegionY, mc.objectX[key], mc.objectY[key]);
            }
            if (mc.debugHud == mc.class.static._DEBUG_HUD_HITBOXES || mc.debugHud == mc.class.static._DEBUG_HUD_ALL) {
                mc.surface.drawBoxAlpha(rect.x, rect.y, rect.width, rect.height, 0x0000ff, 60);
            }
            if (mc.debugHud == mc.class.static._DEBUG_HUD_NAMES || mc.debugHud == mc.class.static._DEBUG_HUD_ALL) {
                var id;
                var name;
                var cmd1, cmd2;
                var colour;
                if (key >= 10000) {
                    id = mc.wallObjectId[key - 10000];
                    name = GameData.static.wallObjectName[id];
                    cmd1 = GameData.static.wallObjectCommand1[id];
                    cmd2 = GameData.static.wallObjectCommand2[id];
                    colour = mc.alphaize(0xdd4500, dist);
                } else {
                    id = mc.objectId[key];
                    name = GameData.static.objectName[id];
                    cmd1 = GameData.static.objectCommand1[id];
                    cmd2 = GameData.static.objectCommand2[id];
                    colour = mc.alphaize(0x00aaaa, dist);
                }
                var show = false;
                if (cmd2 != "Examine") {
                    mc.addReceivedMessage(rect.x, rect.y + rect.height / 2, rect.width, mc.alphaize(0xdddddd, dist) + cmd2);
                    show = true;
                }
                if (cmd1 != "WalkTo") {
                    mc.addReceivedMessage(rect.x, rect.y + rect.height / 2, rect.width, mc.alphaize(0xdddddd, dist) + cmd1);
                    show = true;
                }
                if (show) {
                    mc.addReceivedMessage(rect.x, rect.y + rect.height / 2, rect.width, colour + name + " " + mc.alphaize(0x919191, dist) + "(" + id + ") ");
                }
            }
        }
    }
}

function drawAboveHeadStuff() {
    for (var msgidx = 0; msgidx < mc.receivedMessagesCount; msgidx++) {
        var txtheight = mc.surface.textHeight(1);
        var x = mc.receivedMessageX[msgidx];
        var y = mc.receivedMessageY[msgidx];
        var mid = mc.receivedMessageMidPoint[msgidx];
        var msgheight = mc.receivedMessageHeight[msgidx];
        var flag = true;
        while (flag) {
            flag = false;
            for (var i4 = 0; i4 < msgidx; i4++)
                if (y + msgheight > mc.receivedMessageY[i4] - txtheight && y - txtheight < mc.receivedMessageY[i4] + mc.receivedMessageHeight[i4] && x - mid < mc.receivedMessageX[i4] + mc.receivedMessageMidPoint[i4] && x + mid > mc.receivedMessageX[i4] - mc.receivedMessageMidPoint[i4] && mc.receivedMessageY[i4] - txtheight - msgheight < y) {
                    y = mc.receivedMessageY[i4] - txtheight - msgheight;
                    flag = true;
                }

        }
        mc.receivedMessageY[msgidx] = y;
        mc.surface.centrepara(mc.receivedMessages[msgidx], x, y, 1, 0xffff00, 300);
    }

    for (var itemidx = 0; itemidx < mc.itemsAboveHeadCount; itemidx++) {
        var x = mc.actionBubbleX[itemidx];
        var y = mc.actionBubbleY[itemidx];
        var scale = mc.actionBubbleScale[itemidx];
        var id = mc.actionBubbleItem[itemidx];
        var scaleX = (39 * scale) / 100;
        var scaleY = (27 * scale) / 100;
        mc.surface.drawActionBubble(x - scaleX / 2, y - scaleY, scaleX, scaleY, mc.spriteMedia + 9, 85);
        var scaleXClip = (36 * scale) / 100;
        var scaleYClip = (24 * scale) / 100;
        mc.surface.spriteClipping(x - scaleXClip / 2, (y - scaleY + scaleY / 2) - scaleYClip / 2, scaleXClip, scaleYClip, GameData.static.itemPicture[id] + mc.spriteItem, GameData.static.itemMask[id], 0, 0, false);
    }

    for (var j1 = 0; j1 < mc.healthBarCount; j1++) {
        var i2 = mc.healthBarX[j1];
        var l2 = mc.healthBarY[j1];
        var k3 = mc.healthBarMissing[j1];
        mc.surface.drawBoxAlpha(i2 - 15, l2 - 3, k3, 5, 65280, 192);
        mc.surface.drawBoxAlpha((i2 - 15) + k3, l2 - 3, 30 - k3, 5, 0xff0000, 192);
    }

}

function drawHudMinimap() {
    var uiX = mc.gameWidth - 205 + 40 - 30;
    var uiY = 35 + 192 + 40;
    var uiWidth = 126 + 60;
    var uiHeight = 122 + 60;


    mc.surface.drawBoxEdge(uiX - 1, uiY - 1, uiWidth + 2, uiHeight + 2, 0);
    mc.surface.drawBoxEdge(uiX - 2, uiY - 2, uiWidth + 4, uiHeight + 4, 0xffffff);
    mc.surface.drawBoxEdge(uiX - 3, uiY - 3, uiWidth + 6, uiHeight + 6, 0);
    var entities = mc.drawMinimap(uiX, uiY, uiWidth, uiHeight, 192, true);
    for (var i = 0; i < entities.length; i++) {
        var entity = entities[i];
        if (mc.mouseX >= entity.x - 2 && mc.mouseX <= entity.x + 2
            && mc.mouseY >= entity.y - 2 && mc.mouseY <= entity.y + 2) {
            var text = null;
            var color = 0xffffff;
            if (entity.type == mc.class.static.MinimapEntityType.OBJECT) {
                text = GameData.static.objectName[mc.objectId[entity.index]];
                color = 65535;
            } else if (entity.type == mc.class.static.MinimapEntityType.GROUNDITEM) {
                text = GameData.static.itemName[mc.groundItemId[entity.index]];
                color = 0xff0000;
            } else if (entity.type == mc.class.static.MinimapEntityType.NPC) {
                text = GameData.static.npcName[mc.npcs[entity.index].npcId];
                color = 0xffff00;
            } else if (entity.type == mc.class.static.MinimapEntityType.PLAYER) {
                text = mc.players[entity.index].name;
            }
            if (text != null) {
                mc.surface.drawStringCenter(text, mc.mouseX, mc.mouseY - 2, 1, 0);
                mc.surface.drawStringCenter(text, mc.mouseX - 1, mc.mouseY - 3, 1, color);
            }
        }
    }
}

function drawHudInventory() {
    var slotwidth = 49;
    var slotheight = 34;
    var uiWidth = parseInt(5 * slotwidth);
    var uiHeight = parseInt(6 * slotheight);
    var uiX = mc.gameWidth - uiWidth - 10;
    var uiY = mc.gameHeight - uiHeight - 10;

    var textHeight = mc.surface.textHeight(1);

    mc.surface.drawBoxEdge(uiX - 1, uiY - 1, uiWidth + 2, uiHeight + 2, 0);
    mc.surface.drawBoxEdge(uiX - 2, uiY - 2, uiWidth + 4, uiHeight + 4, 0xffffff);
    mc.surface.drawBoxEdge(uiX - 3, uiY - 3, uiWidth + 6, uiHeight + 6, 0);

    for (var itemIndex = 0; itemIndex < mc.inventoryMaxItemCount; itemIndex++) {
        var slotX = uiX + (itemIndex % 5) * slotwidth;
        var slotY = uiY + parseInt(itemIndex / 5) * slotheight;
        if (itemIndex < mc.inventoryItemsCount && mc.inventoryEquipped[itemIndex] == 1)
            mc.surface.drawBoxAlpha(slotX, slotY, slotwidth, slotheight, 0xff0000, 128);
        else
            mc.surface.drawBoxAlpha(slotX, slotY, slotwidth, slotheight, Utility.static.rgb2long(181, 181, 181), 128);
        if (itemIndex < mc.inventoryItemsCount) {
            mc.surface.spriteClipping(slotX, slotY, slotwidth - 1, slotheight - 2, mc.spriteItem + GameData.static.itemPicture[mc.inventoryItemId[itemIndex]], GameData.static.itemMask[mc.inventoryItemId[itemIndex]], 0, 0, false);
            if (GameData.static.itemStackable[mc.inventoryItemId[itemIndex]] == 0)
                mc.surface.drawstring((mc.inventoryItemStackCount[itemIndex]).toString(), slotX + 1, slotY + 10, 1, 0xffff00);

            mc.surface.drawstring((mc.inventoryItemId[itemIndex]).toString(), slotX + 1, slotY + 14 + slotheight - textHeight, 1, 0x800000);
            var cmd = GameData.static.itemCommand[mc.inventoryItemId[itemIndex]];
            if (cmd != "") {
                mc.surface.drawstring(cmd, parseInt(slotX + 1), parseInt(slotY + 16 + slotheight - textHeight * 2), 1, 0xffffff);
            }
        }
    }

    for (var rows = 1; rows <= 4; rows++)
        mc.surface.drawLineVert(parseInt(uiX + rows * 49), uiY, parseInt((mc.inventoryMaxItemCount / 5) * 34), 0);

    for (var cols = 1; cols <= parseInt(mc.inventoryMaxItemCount / 5 - 1); cols++)
        mc.surface.drawLineHoriz(uiX, parseInt(uiY + cols * 34), 245, 0);
}

function drawHudSkills() {
    var uiWidth = 196;
    var uiHeight = 205;
    var uiX = 9;
    var uiY = 35 + 152;

    mc.surface.drawBoxAlpha(uiX, uiY - 13, uiWidth, uiHeight, Utility.static.rgb2long(220, 220, 220), 128);

    mc.surface.drawBoxEdge(uiX - 1, uiY - 1 - 13, uiWidth + 2, uiHeight + 2, 0);
    mc.surface.drawBoxEdge(uiX - 2, uiY - 2 - 13, uiWidth + 4, uiHeight + 4, 0xffffff);
    mc.surface.drawBoxEdge(uiX - 3, uiY - 3 - 13, uiWidth + 6, uiHeight + 6, 0);

    mc.surface.drawstring("Skills", uiX + 5, uiY, 3, 0xffff00);
    uiY += 13;
    for (var i = 0; i < 9; i++) {
        var colour = 0xffffff;
        mc.surface.drawstring(mc.skillNameShort[i] + ":@yel@" + mc.playerStatCurrent[i] + "/" + mc.playerStatBase[i], uiX + 5, uiY, 1, colour);
        mc.surface.drawstring(mc.skillNameShort[i + 9] + ":@yel@" + mc.playerStatCurrent[i + 9] + "/" + mc.playerStatBase[i + 9], (uiX + uiWidth / 2) - 5, uiY - 13, 1, colour);
        uiY += 13;
    }

    mc.surface.drawstring("Quest Points:@yel@" + mc.playerQuestPoints, (uiX + uiWidth / 2) - 5, uiY - 13, 1, 0xffffff);
    uiY += 12;
    mc.surface.drawstring("Fatigue: @yel@" + (mc.statFatigue * 100) / 750 + "% @0x44ffff00@(" + mc.statFatigue + ")", uiX + 5, uiY - 13, 1, 0xffffff);
    uiY += 8;
    mc.surface.drawstring("Equipment Status", uiX + 5, uiY, 3, 0xffff00);
    uiY += 12;
    for (var i = 0; i < 3; i++) {
        mc.surface.drawstring(mc.equipmentStatNames[i] + ":@yel@" + mc.playerStatEquipment[i], uiX + 5, uiY, 1, 0xffffff);
        if (i < 2)
            mc.surface.drawstring(mc.equipmentStatNames[i + 3] + ":@yel@" + mc.playerStatEquipment[i + 3], uiX + uiWidth / 2 + 25, uiY, 1, 0xffffff);
        uiY += 13;
    }
}

function drawHudMisc() {
    var y = 25;
    var x = (mc.gameWidth / 2) - 38;
    mc.surface.drawstring("@yel@rx:@whi@" + mc.regionX + " @yel@ry:@whi@" + mc.regionY, x, y, 1, 0);
    y += 13;
    mc.surface.drawstring("@yel@lx:@whi@" + mc.localRegionX + " @yel@ly:@whi@" + mc.localRegionY, x, y, 1, 0);
    y += 13;
    mc.surface.drawstring("@yel@x:@whi@" + (mc.regionX + mc.localRegionX) + " @yel@y:@whi@" + (mc.regionY + mc.localRegionY), x, y, 1, 0);
    y += 13;
    mc.surface.drawstring("@yel@mx:@whi@" + mc.mouseX + " @yel@my:@whi@" + mc.mouseY, x, y, 1, 0);
    y += 13;
    mc.surface.drawstring("@yel@dsx:@whi@" + (mc.mouseX - ((512 * 2) / 2 - 468 / 2)) + " @yel@dsy:@whi@" + (mc.mouseY - ((334 * 2) / 2 - 262 / 2)), x, y, 1, 0);
}
