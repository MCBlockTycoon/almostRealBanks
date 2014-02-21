/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings.IO;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public abstract class BankIO implements
		org.rominos2.RealBanks.api.Settings.IO.BankIO {
	private World world;
	private String bankName;

	protected BankIO(World world, String bankName) {
		this.world = world;
		this.bankName = bankName;
	}

	protected World getWorld() {
		return this.world;
	}

	protected String getBankName() {
		return this.bankName;
	}

	protected Chest getChestFromLocation(int x, int y, int z) {
		Block block = this.world.getBlockAt(x, y, z);
		if ((block != null) && (block.getState() instanceof Chest)) {
			return ((Chest) block.getState());
		}
		return null;
	}

	public abstract boolean checkState();
}