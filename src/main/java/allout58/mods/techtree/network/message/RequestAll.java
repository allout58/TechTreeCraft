/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2014 allout58                                                     *
 *                                                                                 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy    *
 * of this software and associated documentation files (the "Software"), to deal   *
 * in the Software without restriction, including without limitation the rights    *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       *
 * copies of the Software, and to permit persons to whom the Software is           *
 * furnished to do so, subject to the following conditions:                        *
 *                                                                                 *
 * The above copyright notice and this permission notice shall be included in all  *
 * copies or substantial portions of the Software.                                 *
 *                                                                                 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   *
 * SOFTWARE.                                                                       *
 ***********************************************************************************/

package allout58.mods.techtree.network.message;

import allout58.mods.techtree.network.NetworkManager;
import allout58.mods.techtree.research.ResearchData;
import allout58.mods.techtree.research.ResearchServer;
import allout58.mods.techtree.util.PlayerHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by James Hollowell on 12/23/2014.
 */
public class RequestAll implements IMessage
{
    String uuid = "";

    public RequestAll(String uuid)
    {
        this.uuid = uuid;
    }

    public RequestAll()
    {

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, uuid);
    }

    public static class Handler implements IMessageHandler<RequestAll, IMessage>
    {

        @Override
        public IMessage onMessage(RequestAll message, MessageContext ctx)
        {
            EntityPlayerMP player = PlayerHelper.getPlayerFromUUID(message.uuid, FMLCommonHandler.instance().getMinecraftServerInstance());
            for (ResearchData d : ResearchServer.getInstance().getClientData(message.uuid))
            {
                NetworkManager.INSTANCE.sendTo(new SendResearch(d.getNodeID(), d.getResearchAmount(), message.uuid), player);
                NetworkManager.INSTANCE.sendTo(new UpdateNodeMode(message.uuid, d.getNodeID(), d.getMode()), player);
            }
            return null;
        }
    }
}
