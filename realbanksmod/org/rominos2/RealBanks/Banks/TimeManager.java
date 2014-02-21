/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Banks;

import java.util.logging.Logger;
import org.bukkit.World;
import org.rominos2.RealBanks.api.Settings.WorldSettings;

public class TimeManager {
	private WorldManager manager;
	private long lastMCTime;
	private int realTimeCompteur;
	private int minuteCompteur;

	public TimeManager(WorldManager manager) {
		this.manager = manager;
		this.lastMCTime = this.manager.getWorld().getTime();
		this.realTimeCompteur = 0;
		this.minuteCompteur = 0;
	}

	public void onTick() {
		long time = this.manager.getWorld().getTime();
		if (time + 3L < this.lastMCTime) {
			onMCDay();
		}
		this.realTimeCompteur += 1;
		if (this.realTimeCompteur >= 1200) {
			this.realTimeCompteur = 0;
			onRealMinute();
		}
	}

	public void onRealMinute() {
		this.minuteCompteur += 1;
		int scheduledSaveTime = this.manager.getProperties()
				.getScheduledSaveTime();
		if ((scheduledSaveTime > 0)
				&& (this.minuteCompteur >= scheduledSaveTime)) {
			this.minuteCompteur = 0;
			this.manager.save();
			if (this.manager.getProperties().isLog())
				org.rominos2.RealBanks.RealBanks
						.getInstance()
						.getLogger()
						.info("Banks in " + this.manager.getWorld().getName()
								+ " have been saved.");
		}
	}

	public void onMCDay() {
		if (!(org.rominos2.RealBanks.RealBanks.getInstance().isUsingEconomy()))
			return;
	}
}