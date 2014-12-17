package com.davidhurng.helloplugin;
 
import java.util.logging.Logger;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.SimplePluginManager;
 

public final class groupChat extends JavaPlugin implements Listener{
    public final Logger logger = Logger.getLogger("Minecraft");

    ArrayList<Channel> channelList = new ArrayList<Channel>();
    
    final static String[] COLORS = {"black","dblue","dgreen","daqua","dred","dpurple","gold","gray","dgray",
    "blue","green","aqua","red","lpurple","yellow","white"};

    final static String[] MODIFIER_KEYS = {"black","dblue","dgreen","daqua","dred","dpurple","gold","gray","dgray",
    "blue","green","aqua","red","lpurple","yellow","white","magic","bold","strike","line","italic","reset"};

    final static ChatColor[] MODIFIER_CODES = {ChatColor.BLACK,ChatColor.DARK_BLUE,ChatColor.DARK_GREEN,ChatColor.DARK_AQUA,
    ChatColor.DARK_RED,ChatColor.DARK_PURPLE,ChatColor.GOLD,ChatColor.GRAY,ChatColor.DARK_GRAY,ChatColor.BLUE,ChatColor.GREEN,ChatColor.AQUA,
    ChatColor.RED,ChatColor.LIGHT_PURPLE,ChatColor.YELLOW,ChatColor.WHITE,ChatColor.MAGIC,ChatColor.BOLD,
    ChatColor.STRIKETHROUGH,ChatColor.UNDERLINE,ChatColor.ITALIC,ChatColor.RESET};
 
    @Override
        public void onEnable(){

        PluginDescriptionFile pdffile = this .getDescription();
        this.logger.info(pdffile.getName() + " Has been enabled!");
    }
    
    @Override
        public void onDisable(){
        PluginDescriptionFile pdffile = this .getDescription();
        this.logger.info(pdffile.getName() + " Has been disabled!");
    }
 
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        event.setFormat(ChatColor.RED+event.getMessage());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {

        Player s = (Player)sender;
 
        if((commandLabel.equalsIgnoreCase("m")))
        {
            if(args.length < 1)
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Correct Usage: /m (channel_name)");

            else
            {
                for(int i = 0; i < channelList.size(); i++)
                {

                    if(channelList.get(i).getName().equals(args[0]))
                    {
                        if(!channelList.get(i).isBanned(s.getName()))
                            channelList.get(i).send(args,s);

                        return false;
                            
                    }
                }
                s.sendMessage(ChatColor.DARK_RED + "Channel does not exist.");
            }
        }
        if(cmd.getName().equalsIgnoreCase("alt"))
        {
            String text = groupChat.parseText(args);
            getServer().broadcastMessage("<"+s.getName()+"> "+text);   

            
        }
        if(cmd.getName().equalsIgnoreCase("mkch"))
        {
            if(args.length != 2)
            {
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Correct Usage: /mkch (channel_name) (channel_color)");
            }
            else
            {
                for(int i = 0; i < channelList.size(); i++)
                {
                    if(channelList.get(i).getName().equals(args[0]))
                    {
                        s.sendMessage(ChatColor.DARK_RED + "A channel with that name already exists.");
                        return false;
                    }                    
                        
                }
                for(int i = 0; i < COLORS.length; i++)
                {
                    if(args[1].equalsIgnoreCase(COLORS[i]))
                    {
                        channelList.add(new Channel(args[0], s.getName(), MODIFIER_CODES[i]));
                        s.sendMessage(ChatColor.DARK_RED + "Channel "+channelList.get(channelList.size()-1).getName()+" added successfully!");
                    }
                }
                
            }
        }
        if(cmd.getName().equalsIgnoreCase("join"))
        {
            if(args.length != 1)
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Correct Usage: /join (channel)");
            else
            {
                for(int i = 0; i < channelList.size(); i++)
                {
                    if(args[0].equals(channelList.get(i).getName()))
                    {
                        if(channelList.get(i).isBanned(s.getName()))
                        {
                            s.sendMessage(ChatColor.DARK_RED + "You are banned from this channel and cannot join it.");
                            return false;
                        }
                            
                        else
                        {
                            channelList.get(i).add(s.getName());
                            s.sendMessage(ChatColor.DARK_RED + "Welcome to the channel!");
                            return false;
                        }
                            
                    }
                }
                s.sendMessage(ChatColor.DARK_RED + "Channel does not exist.");              
            }
        }
        if(cmd.getName().equalsIgnoreCase("leave")){
            if(args.length == 0){
                // End chat room session
 
                s.sendMessage(ChatColor.RED + "Chat Ended");
            }
            if(args.length > 0){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Too many arguments! Correct usage: /imstop");
            }
        }
       if(cmd.getName().equalsIgnoreCase("block")){
            if(args.length != 2)
            {
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage. Correct Usage: /block (player_name) (channel)");
            }
            else
            {
                for(int i = 0; i < channelList.size(); i++)
                {
                    if(channelList.get(i).getName().equals(args[1]))
                    {
                        ArrayList<String> plist = channelList.get(i).getPlayers();
                        
                        for(int j = 0; j < plist.size(); j++)
                        {
                            if(s.getName().equals(channelList.get(i).getOwner()))
                            {
                                Player target = s.getServer().getPlayer(args[0]);
                                channelList.get(i).block(target.getName());
                                s.sendMessage(ChatColor.DARK_RED + "Blocked user" + target.getName()); 
                                return false;                              
                            }
                            else
                            {
                                s.sendMessage(ChatColor.DARK_RED + "You do not own that channel.");
                                return false;
                            }
                        }
                    }
                    else
                    {
                        s.sendMessage(ChatColor.DARK_RED + "Channel does not exist.");
                        return false;
                    }
                }
            }
        }
 
        return false;
    }

    //Parse the text and look for unicode
    public static String parseText(String [] args)
    {
        String out = "";
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].charAt(0) == '\\')
            {

                if(args[i].charAt(1) == 'u' || args[i].charAt(1) == 'U')
                {
                    //If a the first part of a unicode escape character is found, convert the number into a hexcode and put it in a char.
                    char code = (char) Integer.parseInt(args[i].substring(2),16);
                    out += code;
                    out += ' ';
                }
                else
                {

                    for(int j = 0; j < MODIFIER_KEYS.length; j++)
                    {
                        if(args[i].substring(1).equalsIgnoreCase(MODIFIER_KEYS[j]))
                        {
                            out += MODIFIER_CODES[j];
                            out += ' ';
                        }
                    }
                }

            }
            else
                out = out.concat(args[i]+" ");
        }
        return out;
    }
 
}
 

//Inner class because I could not get it to work as a separate java file
class Channel
{
    String name;
    String ownerName;
    ArrayList<String> players;
    ArrayList<String> banned;
    ChatColor color;

    public Channel(String name, String ownerName, ChatColor color)
    {
        players = new ArrayList<String>();
        banned = new ArrayList<String>();

        this.name = name;
        this.ownerName = ownerName;
        this.color = color;

        players.add(ownerName);
    }

    public boolean add(String playerName)
    {
        for(int i = 0; i < banned.size(); i++)
        {
            if(banned.get(i).equals(playerName))
                return false;
        }
        players.add(playerName);
        return true;
    }

    public void block(String playerName)
    {
        banned.add(playerName);
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).equals(playerName))
                players.remove(i);
        }
        
    }

    public void send(String[] message, Player player)
    {
        //Build String
        String out = color+"["+name+"] "+ChatColor.WHITE+"<"+player.getName()+">";
        for(int i = 1; i < message.length; i++)
        {
            out = out.concat(" "+message[i]);
        }
        //Send to players
        for(int i = 0; i < players.size(); i++)
        {
            Player target = player.getServer().getPlayer(players.get(i));
            target.sendMessage(out);
        }
    }

    public boolean isOwner(String playerName)
    {
        if(playerName.equals(ownerName))
            return true;
        return false;
    }

    public boolean isBanned(String playerName)
    {
        for(int i = 0; i < banned.size(); i++)
        {
            if(banned.get(i).equals(playerName))
                return true;
        }
        return false;
    }

    public String getName(){return name;}
    public String getOwner(){return ownerName;}
    public ArrayList<String> getPlayers(){return players;}
}
