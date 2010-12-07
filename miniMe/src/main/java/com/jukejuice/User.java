package com.jukejuice;

public class User 
{
	private int id;
	private String ipAddress;
	private int maxEnergy;
	private int usedEnergy;
	private Db db = new Db();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * 
	 * @return energy available to the user
	 */
	public int getEnergy() {
		return maxEnergy - usedEnergy;
	}
	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public int getMaxEnergy()
	{
		return maxEnergy;
	}
	
	public void setUsedEnergy(int usedEnergy) {
		this.usedEnergy = usedEnergy;
	}
	
	public int getUsedEnergy()
	{
		return this.usedEnergy;
	}
	
	public void useEnergy(int amount)
	{
		usedEnergy += amount; 
	}
	
	public void persist()
	{
		db.updateUser(this);
	}
	
}
