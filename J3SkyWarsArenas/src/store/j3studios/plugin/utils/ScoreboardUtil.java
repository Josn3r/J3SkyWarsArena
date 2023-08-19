package store.j3studios.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * 
 * ScoreboardUtil by Josn3rDev
 * Special for my projects
 * Support 1.8 - 1.19
 * 
 * @author JOSN3R
 *
 */ 

public class ScoreboardUtil {
    
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	
	private final Objective objective;
			
	public ScoreboardUtil(String TITLE, String typeBoard)
	{
		this.objective = this.scoreboard.registerNewObjective(typeBoard, "dummy");
		this.setName(TITLE);
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public String Text(String s) {
        String str = ChatColor.translateAlternateColorCodes('&', s);
		return str;
        
    }
				
	public void setName(final String string)
	{
		String name2 = string;
		if (name2.length() > 32) {
			name2 = name2.substring(0, 32);
		}		
		this.objective.setDisplayName(Text(name2));
	}
		
	public void removeLines (Integer slot) { 
		Team team = scoreboard.getTeam("TEAM_" + slot);
		if (team != null) {
			scoreboard.getTeam("TEAM_" + slot).unregister();
			scoreboard.resetScores(getEntry(slot));
		}
	}
		
	public void lines (Integer slot, String line) {
		    	
    	Team team = scoreboard.getTeam("TEAM_" + slot);        	
        
    	if (line.length() > 32) {
    		line = line.substring(0, 32);
    	}
    	
    	String[] ts = splitStringLine(line);
    	
    	if (team == null) {
    		
        	team = scoreboard.registerNewTeam("TEAM_" + slot);
        	
        	team.addEntry(getEntry(slot));
            
            setPrefix(team, ts[0]);
            setSuffix(team, ts[1]);	
            
            objective.getScore(getEntry(slot)).setScore(slot);
        } 
    	else 
    	{    		
    		setPrefix(team, ts[0]);
            setSuffix(team, ts[1]);  		
        }
    	
	}
	
	public void setPrefix(Team t, String string)
	{
	    if (string.length() > 16)
	    {
	    	t.setPrefix(string.substring(0, 16));
	    	return;
	    }
	    t.setPrefix(string);
	}
	  
	public void setSuffix(Team t, String string)
	{
	    if (string.length() > 16) {
	     	t.setSuffix(maxChars(16, string));
	    } else {
	    	t.setSuffix(string.substring(0, string.length()));
	    }
	}
	
	public String maxChars(int characters, String string)
	{
	    if (ChatColor.translateAlternateColorCodes('&', string).length() > characters) {
	    	return string.substring(0, characters);
	    }
	    return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public String getEntry (Integer slot) {
		
		if (slot == 0) {
			return "§0";	
		}
		if (slot == 1) {
			return "§1";	
		}
		if (slot == 2) {
			return "§2";	
		}
		if (slot == 3) {
			return "§3";	
		}
		if (slot == 4) {
			return "§4";	
		}
		if (slot == 5) {
			return "§5";	
		}
		if (slot == 6) {
			return "§6";	
		}
		if (slot == 7) {
			return "§7";	
		}
		if (slot == 8) {
			return "§8";	
		}
		if (slot == 9) {
			return "§9";	
		}
		if (slot == 10) {
			return "§a";	
		}
		if (slot == 11) {
			return "§b";	
		}
		if (slot == 12) {
			return "§c";	
		}
		if (slot == 13) {
			return "§d";	
		}
		if (slot == 14) {
			return "§e";	
		}
		if (slot == 15) {
			return "§f";	
		}		
		return "";		
	}
	  
	public Scoreboard getScoreboard()
	{
	    return this.scoreboard;
	}
	  
	public void build(Player player)
	{
	    player.setScoreboard(this.scoreboard);
	}
	
	public boolean hasBoard (Player player) {
		if (player.getScoreboard().equals(getScoreboard())) {
			return true;
		}
		return false;
	}
		
	private String[] splitStringLine (String string)
	{
	   		
		StringBuilder prefix = new StringBuilder(string.substring(0, string.length() >= 16 ? 16 : string.length()));
	    StringBuilder suffix = new StringBuilder(string.length() > 16 ? string.substring(16) : "");
	    
	    if ((prefix.toString().length() > 1) && (prefix.charAt(prefix.length() - 1) == '§'))
	    {
	    	prefix.deleteCharAt(prefix.length() - 1);
	    	suffix.insert(0, '§');
	    }
	    
	    String last = "";	    
	    
        int i = 0;       
        
        while (i < prefix.toString().length()) {
        	
            char c = prefix.toString().charAt(i);
            
            if (c == '§') {
            	if (i < prefix.toString().length() - 1) {
            		last = last + "§" + prefix.toString().charAt(i + 1);
            	}         
            }
            
            ++i;
        }
	    	
        String s2 = ""+suffix;
        
        if (prefix.length() > 14) {        	
        	s2 = !last.isEmpty() ? String.valueOf(last) + s2 : "§" + s2;
        }
                
	    return new String[] { 
	    		prefix.toString().length() > 16 ? prefix.toString().substring(0, 16) : 
	    			prefix.toString(), 
	    			
	    			s2.toString().length() > 16 ? s2.toString().substring(0, 16) : 
	    				s2.toString() };
	    
	}
    
}
