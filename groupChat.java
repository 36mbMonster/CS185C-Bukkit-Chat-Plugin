package com.davidhurng.helloplugin;
 
import java.util.logging.Logger;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
 

public final class groupChat extends JavaPlugin{
    public final Logger logger = Logger.getLogger("Minecraft");
    
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
 
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player s = (Player)sender;
 
        if((commandLabel.equalsIgnoreCase("im")))
        { //  Instant messaging system activate
            if(args.length == 0){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Correct Usage: /im (player) (message)");
            }
            else if(args.length == 1){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Correct Usage: /im (player) (message)");
            }
            else if(args.length > 1){ // Anything after the players name will be sent to the target
                Player target = s.getServer().getPlayer(args[0]); // Get target players name
                s.sendMessage(ChatColor.GREEN + "Message Sent");
                target.sendMessage(ChatColor.BLUE + "Received private message from " + sender.getName() + ":");
                target.sendMessage(ChatColor.GREEN + StringUtils.join(args, ' ', 1, args.length)); // Send message to target player
            }


        }
        if(cmd.getName().equalsIgnoreCase("alt"))
        {
            String text = groupChat.parseText(args);
            getServer().broadcastMessage("<"+s.getName()+"> "+text);   

            
        }
        if(cmd.getName().equalsIgnoreCase("imstart")){ // Chat room between 2 players system
            if(args.length == 0){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Correct Usage: /imstart (player) \u03C0");
            }
            else if(args.length == 1){
                Player target = s.getServer().getPlayer(args[0]);
                // Start of chat room
 
                s.sendMessage(ChatColor.GREEN + "You are now in a chat with" + target.getName());
                target.sendMessage(ChatColor.GREEN + "You are now in a chat with" + s.getName());
                s.sendMessage(ChatColor.GREEN + "All other chat will be blocked out. Use /imstop to stop the chat.");
                target.sendMessage(ChatColor.GREEN + "All other chat will be blocked out. Use /imstop to stop the chat.");
            } else if(args.length > 1){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Too many arguments! Correct Usage: /imstart (player)");
            }
        }
        if(cmd.getName().equalsIgnoreCase("imstop")){
            if(args.length == 0){
                // End chat room session
 
                s.sendMessage(ChatColor.RED + "Chat Ended");
            }
            if(args.length > 0){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Too many arguments! Correct usage: /imstop");
            }
        }
        if(cmd.getName().equalsIgnoreCase("imblock")){
            if(args.length == 0){
                s.sendMessage(ChatColor.RED + "Incorrect Usage. Correct Usage: /imblock (playername)");
            }
            if(args.length == 1){
                Player target = s.getServer().getPlayer(args[0]);
                target.sendMessage(ChatColor.RED + sender.getName() + " has blocked you!");
                s.sendMessage(ChatColor.RED + "Blocked user" + target.getName());
            }
            if(args.length > 1){
                s.sendMessage(ChatColor.DARK_RED + "Incorrect Usage! Too many arguments! Correct Usage: /imblock (username)");
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
 
