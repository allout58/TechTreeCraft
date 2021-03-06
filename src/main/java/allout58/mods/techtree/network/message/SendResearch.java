/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2015 allout58                                                     *
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

import allout58.mods.techtree.research.ResearchClient;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by James Hollowell on 12/17/2014.
 */
public class SendResearch
        implements IMessage
{
    int nodeID = 0;
    int research = 0;
    String uuid = "";

    public SendResearch()
    {
    }

    public SendResearch(int nodeID, int research, String uuid)
    {
        this.nodeID = nodeID;
        this.research = research;
        this.uuid = uuid;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        nodeID = buf.readInt();
        research = buf.readInt();
        uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(nodeID);
        buf.writeInt(research);
        ByteBufUtils.writeUTF8String(buf, uuid);
    }

    public static class Handler
            implements IMessageHandler<SendResearch, IMessage>
    {
        @Override
        public IMessage onMessage(SendResearch message, MessageContext ctx)
        {
            ResearchClient.getInstance(message.uuid).setResearch(message.nodeID, message.research);
            return null;
        }
    }
}
