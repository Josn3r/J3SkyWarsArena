package store.j3studios.plugin.player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import store.j3studios.plugin.SWA;

public class SPlayer {

	// STATS YML LOADED
	private UUID uuid;
	private Player player;
	
	private Integer kills = 0;
	private Integer deaths = 0;
	private Integer wins = 0;
	private Integer played = 0;
		
	private Integer elo = 0;
	    
	// GAME FUCTION
	
	private boolean isDeath = false;
	
	private int winEffectTimerID;	
    private double winEffectTimer = 0.0;
	
	private Integer killsGame = 0;
	
	private int votes = 0;	
	private ArrayList<String> voteList = new ArrayList<String>();
    
    private Location spawnLoc = null;
	
	public SPlayer(UUID uUID) {
        this.setUuid(uUID);
        this.setPlayer(Bukkit.getPlayer(uUID));
    }

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getKills() {
		return kills;
	}

	public void setKills(Integer kills) {
		this.kills = kills;
	}

	public Integer getDeaths() {
		return deaths;
	}

	public void setDeaths(Integer deaths) {
		this.deaths = deaths;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getPlayed() {
		return played;
	}

	public void setPlayed(Integer played) {
		this.played = played;
	}
	
	public Double getKDR() {
		if(kills == 0 || deaths == 0){
			return 0.0;
		}
		
		DecimalFormat format = new DecimalFormat("#.##");
		double calculate = kills/deaths;
		double kdr = Double.valueOf(format.format(calculate));
		return kdr;
	}

	/**
	 * 
	 * LEVEL SYSTEM
	 * 
	 */

	public Integer getElo() {
		return elo;
	}

	public void setElo(Integer elo) {
		this.elo = elo;
	}
		
	/**
	 * 
	 * GAME FUCTIONS
	 * 
	 */

	public Integer getKillsGame() {
		return killsGame;
	}

	public void setKillsGame(Integer kills) {
		this.killsGame = kills;
	}

	public int getVotes() {
		return votes;
	}

	public void addVotes(String type) {
        ++this.votes;
        this.voteList.add(type);
    }
	
	public void setVotes(int votes) {
		this.votes = votes;
	}

	public Boolean hasTypeVote(String type) {
		if (voteList.contains(type)) {
			return true;
		}
		return false;
	}

	public Location getSpawnLoc() {
		return spawnLoc;
	}

	public void setSpawnLoc(Location spawnLoc) {
		this.spawnLoc = spawnLoc;
	}

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
	}

	//
	
	public void startWinEffect() {
		setWinEffectTimer(10.0);
		this.winEffectTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SWA.get(), new Runnable() {
			@Override
			public void run() {
				if (getWinEffectTimer() > 0) {
					//setNoCleanTimer(getNoCleanTimer() - 0.25);
					/*if (getWinEffect() != WinEffect.NONE) {
						if (getWinEffect() == WinEffect.FIREWORK) {
							winEffectUtil.shotFirework(getPlayer());
						}
						if (getWinEffect() == WinEffect.CHICKENS) {
							winEffectUtil.executeChickensVolcan(getPlayer());
						}
						if (getWinEffect() == WinEffect.WOOL) {
							winEffectUtil.executeWoolVolcan(getPlayer());
						}
						if (getWinEffect() == WinEffect.TNT) {
							winEffectUtil.executeTNTVolcan(getPlayer());
						}
						if (getWinEffect() == WinEffect.DRAGON) {
							stop(winEffectTimerID);
						}
					}*/
				} else {
					stop(winEffectTimerID);
				}
			}			
		}, 0L, 5L);
	}
	
	public void stopWinEffect() {
		stop(winEffectTimerID);		
	}
	
	public void stop(int i) {
    	Bukkit.getScheduler().cancelTask(i);    	
    }

	public double getWinEffectTimer() {
		return winEffectTimer;
	}

	public void setWinEffectTimer(double winEffectTimer) {
		this.winEffectTimer = winEffectTimer;
	}

	public int getWinEffectTimerID() {
		return winEffectTimerID;
	}

	public void setWinEffectTimerID(int winEffectTimerID) {
		this.winEffectTimerID = winEffectTimerID;
	}

}
