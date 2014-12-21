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

package allout58.mods.techtree.commands;

import allout58.mods.techtree.research.ResearchServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Hollowell on 12/20/2014.
 */
public class ResearchCommand extends CommandBase
{
    @SuppressWarnings("rawtypes")
    private List aliases;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResearchCommand()
    {
        this.aliases = new ArrayList();
        this.aliases.add("research");
    }

    @Override
    public String getCommandName()
    {
        return "research";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        String use = "/research set <user> <nodeID> <amount>";
        return use;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] astring)
    {
        if (astring.length == 0 || "help".equalsIgnoreCase(astring[0]))
            throw new WrongUsageException(getCommandUsage(commandSender));
        if ("set".equalsIgnoreCase(astring[0]))
        {
            if (astring.length != 4)
                throw new WrongUsageException(getCommandUsage(commandSender));
            ResearchServer.getInstance().setResearch(CommandBase.getPlayer(commandSender, astring[1]).getUniqueID().toString(), Integer.getInteger(astring[2]), Integer.getInteger(astring[3]));
            //commandSender.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.getXP", astring[1], HarvestXPServer.INSTANCE.GetXPForUser(astring[1]))));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
    {
        final List<String> matches = new LinkedList<String>();
        final String argLc = astring[astring.length - 1].toLowerCase();
        if (astring.length == 1)
        {
            if ("set".toLowerCase().startsWith(argLc)) matches.add("set");
            //if ("setXP".toLowerCase().startsWith(argLc)) matches.add("setXP");
        }
        else if (astring.length == 2)
        {
            for (String un : MinecraftServer.getServer().getAllUsernames())
                if (un.toLowerCase().startsWith(argLc)) matches.add(un);
        }
        return matches.isEmpty() ? null : matches;
    }
}
