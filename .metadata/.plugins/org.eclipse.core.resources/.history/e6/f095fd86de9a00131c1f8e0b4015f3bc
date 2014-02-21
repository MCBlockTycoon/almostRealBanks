/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks.Settings;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.rominos2.RealBanks.api.Settings.LanguageSettings.SentenceType;

public class LanguageSettings implements
		org.rominos2.RealBanks.api.Settings.LanguageSettings {
	private File file;
	private YamlConfiguration config;

	public LanguageSettings() {
		this.file = new File(org.rominos2.RealBanks.RealBanks.getInstance()
				.getPluginFolder(), "Language.yml");
	}

	public void load() {
		if (!(this.file.exists())) {
			this.file.getParentFile().mkdirs();
			try {
				this.file.createNewFile();
				org.rominos2.RealBanks.RealBanks
						.getInstance()
						.getLogger()
						.info("Languages Config not found, creating default ones");
			} catch (IOException e) {
				e.printStackTrace();
			}
			createLanguages();
		}
		loadLanguages();
	}

	private void loadLanguages() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}

	private void createLanguages() {
		YamlConfiguration config = YamlConfiguration
				.loadConfiguration(this.file);

		config.set("Error.Inactive", "RealBanks is inactive in this World.");
		config.set("Error.Permissions",
				"You've not the Permissions to do that.");
		config.set("Chest.Break",
				"Please disconnect this chest from the <bank> Bank first.");
		config.set("Chest.Interact",
				"You must have an account in the <bank> Bank to interact with this chest.");
		config.set("Chest.Transaction",
				"This chest is already in a Transaction with another Player. Please wait...");
		config.set("Chest.NoChest",
				"You must target a chest to use this command.");
		config.set("Chest.Connect.Already",
				"This Chest is already connected to the <bank> Bank.");
		config.set("Chest.Connect.Done",
				"This chest is now connected to the <bank> Bank.");
		config.set("Chest.Disconnect.NotConnected",
				"This Chest is not connected to a Bank.");
		config.set("Chest.Disconnect.Done",
				"This chest is now disconnected from the <bank> Bank.");
		config.set("Command.Invalid", "Invalid Command. Try /rb for help.");
		config.set("Command.Title", "RealBanks Commands");
		config.set("Command.ManagerTitle", "RealBanks Manager Commands");
		config.set("Command.Manager", "See the Manager Commands.");
		config.set("Command.Create", "Create a RealBank.");
		config.set("Command.Delete", "Delete a RealBank.");
		config.set("Command.Connect",
				"Connect the Chest you're looking at to a RealBank.");
		config.set("Command.Disconnect",
				"Disconnect the Chest you're looking at from a RealBank.");
		config.set("Command.Subscribe.Self", "Subscribe to a RealBank.");
		config.set("Command.Subscribe.Other",
				"Subscribe a Client to a RealBank.");
		config.set("Command.Unsubscribe.Self", "Unsubscribe from a RealBank.");
		config.set("Command.Unsubscribe.Other",
				"Unsubscribe a Client from a RealBank.");
		config.set("Command.List.Banks", "List the RealBanks in this World.");
		config.set("Command.List.Clients", "List the Clients in the Bank.");
		config.set("Command.Modify", "Change Bank Settings Values.");
		config.set("Command.Edit",
				"Edit the Contents of the Account of the Client.");
		config.set("Command.As",
				"Open the chest of the Bank as if you were the Client.");
		config.set("Command.Send", "Send Items in your hand to a RealBank.");
		config.set("Command.Get", "Get Items from a RealBank to your hand.");
		config.set("Command.Link", "Create a Bank Frontage from another World.");
		config.set("Command.Unlink", "Delete the Bank Frontage.");
		config.set("Command.Reload", "Reload the RealBanks plugin");
		config.set("Bank.Create.Done", "<bank> Bank has been created.");
		config.set("Bank.Create.Already", "<bank> already exixts.");
		config.set("Bank.Delete.Link",
				"<bank> Bank is a link. It can't be deleted.");
		config.set("Bank.Delete.Done", "<bank> Bank has been deleted.");
		config.set("Bank.NotFound", "<bank> Bank does not exists.");
		config.set("Bank.NotIn.Self",
				"You're not registered in the <bank> Bank.");
		config.set("Bank.NotIn.Other",
				"<client> is not registered in the <bank> Bank.");
		config.set(
				"Bank.Delete.Confirm",
				"WARNING ! Deleting this bank will probably suppress all accounts and all the player's contents");
		config.set("Confirm.Ask", "Please Confirm with '/realbanks confirm");
		config.set("Client.NotFound", "<client> does not exists.");
		config.set("Bank.Subscribe.Self.Already",
				"You are already registered in the <bank> Bank.");
		config.set("Bank.Subscribe.Other.Already",
				"<client> is already registered in the <bank> Bank.");
		config.set("Bank.Subscribe.Full", "<bank> Bank is Full.");
		config.set("Bank.Subscribe.Permissions",
				"You don't have the Permissions to subscribe to the <bank> Bank.");
		config.set("Bank.Subscribe.Self.Done",
				"You've subscribed to the <bank> Bank.");
		config.set("Bank.Subscribe.Other.Done",
				"You've subscribed <client> to the <bank> Bank.");
		config.set("Bank.Unsubscribe.Self.Done",
				"You're no more in the <bank> Bank.");
		config.set("Bank.Unsubscribe.Other.Done",
				"<client> is no more in the <bank> Bank.");
		config.set("Bank.Unsubscribe.Self.Confirm",
				"WARNING ! Unsubscribing will probably suppress your contents from this bank.");
		config.set(
				"Bank.Unsubscribe.Other.Confirm",
				"WARNING ! Unsubscribing will probably suppress <client>'s contents from this bank.");
		config.set("List.Banks",
				"RealBanks List in <world>, Page <page>/<total>");
		config.set("List.Available", "Available");
		config.set("List.Subscribed", "Subscribed");
		config.set("List.NotAvailable", "Not Available");
		config.set("List.Chests", "Chests");
		config.set("List.Accounts", "Accounts");
		config.set("List.Clients",
				"Clients List in <bank> in <world>, Page <page>/<total>");
		config.set("Modify.Bank", "<bank> Bank");
		config.set("Modify.AccountLimit.Name", "Account Limit");
		config.set("Modify.AccountLimit.Done",
				"Account Limit has been changed to <value>.");
		config.set("Modify.EnterPermission.Name", "Enter Permission");
		config.set("Modify.EnterPermission.Done",
				"Enter Permission have been changed to <value>.");
		config.set("Modify.LogTransactions.Name", "Log Transactions");
		config.set("Modify.LogTransactions.Done",
				"Log Transactions have been changed to <value>.");
		config.set("Modify.EnterCost.Name", "Enter Cost");
		config.set("Modify.EnterCost.Done",
				"Enter Cost have been changed to <value>.");
		config.set("Bank.As.Open",
				"You're opening this chest as if you were <client>.");
		config.set("Bank.As.Done",
				"You now see <bank> Bank accounts as if you were <client>.");
		config.set("Bank.Send.Nothing", "You hold nothing in your hand.");
		config.set("Bank.Send.Full", "Your account is full. Can't add nothing.");
		config.set("Bank.Send.Done", "You've send <value> to your account.");
		config.set("Bank.Get.HandNotEmpty", "Your hands are not empty.");
		config.set("Bank.Get.BadMaterial", "<value> Material does not exists.");
		config.set("Bank.Get.NotIn",
				"Can't get <value> from your account in <bank> Bank.");
		config.set("World.NotFound", "World <world> does not exists.");
		config.set("Bank.Link.NotLink",
				"<bank> Bank already exists and is not a linked Bank.");
		config.set("Bank.Link.NotFound",
				"<bank> Bank does not exists in <world>.");
		config.set("Bank.Link.Already",
				"<bank> Bank is already a Linked Bank in <world>.");
		config.set("Bank.Link.Done", "<bank> Bank is now linked from <world>.");
		config.set("Bank.Unlink.NotLink", "<bank> Bank is not a linked Bank.");
		config.set("Bank.Unlink.Done",
				"<bank> Bank has been unlinked from <world>.");
		config.set("Bank.Unlink.Confirm",
				"WARNING ! Unlinking this bank will disconnect all its chests.");
		config.set("Reload.Done", "Reload complete.");

		config.set("Eco.NeedAccount", "You need an Economy Account to do that.");
		config.set("Eco.NotEnough", "You need <value> to do that.");
		config.set("Eco.Confirm", "WARNING ! You need <value> to do that.");
		try {
			config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLang(LanguageSettings.SentenceType type,
			String configNode, String defaultSentence, String[] args) {
		switch ($SWITCH_TABLE$org$rominos2$RealBanks$api$Settings$LanguageSettings$SentenceType()[type
				.ordinal()]) {
		case 1:
			return getBankLang(configNode, defaultSentence, args[0]);
		case 2:
			return getClientLang(configNode, defaultSentence, args[0]);
		case 3:
			return getBankClientLang(configNode, defaultSentence, args[0],
					args[1]);
		case 4:
			return getBankValueLang(configNode, defaultSentence, args[0],
					args[1]);
		case 6:
			return getBankWorldLang(configNode, defaultSentence, args[0],
					args[1]);
		case 5:
			return getBankClientValueLang(configNode, defaultSentence, args[0],
					args[1], args[2]);
		case 7:
			return getListLang(configNode, defaultSentence, args[0], args[1],
					args[2]);
		case 8:
			return getBankListLang(configNode, defaultSentence, args[0],
					args[1], args[2], args[3]);
		case 9:
			return getValueLang(configNode, defaultSentence, args[0]);
		case 11:
			return getNormalLang(configNode, defaultSentence);
		case 10:
			return getWorldLang(configNode, defaultSentence, args[0]);
		}
		return "ERROR in Languages";
	}

	private String getBankLang(String configNode, String defaultSentence,
			String bank) {
		String line = getNormalLang(configNode, defaultSentence);
		line = line.replace("<bank>", bank);
		return line;
	}

	private String getClientLang(String configNode, String defaultSentence,
			String client) {
		String line = getNormalLang(configNode, defaultSentence);
		line = line.replace("<client>", client);
		return line;
	}

	private String getListLang(String configNode, String defaultSentence,
			String world, String page, String total) {
		String line = getNormalLang(configNode, defaultSentence);
		line = line.replace("<world>", world);
		line = line.replace("<page>", page);
		line = line.replace("<total>", total);
		return line;
	}

	private String getValueLang(String configNode, String defaultSentence,
			String value) {
		String line = getNormalLang(configNode, defaultSentence);
		line = line.replace("<value>", value);
		return line;
	}

	private String getNormalLang(String configNode, String defaultSentence) {
		return this.config.getString(configNode, defaultSentence);
	}

	private String getBankClientLang(String configNode, String defaultSentence,
			String bank, String client) {
		String line = getBankLang(configNode, defaultSentence, bank);
		line = line.replace("<client>", client);
		return line;
	}

	private String getBankValueLang(String configNode, String defaultSentence,
			String bank, String value) {
		String line = getBankLang(configNode, defaultSentence, bank);
		line = line.replace("<value>", value);
		return line;
	}

	private String getBankListLang(String configNode, String defaultSentence,
			String bank, String world, String page, String total) {
		String line = getBankLang(configNode, defaultSentence, bank);
		line = line.replace("<world>", world);
		line = line.replace("<page>", page);
		line = line.replace("<total>", total);
		return line;
	}

	private String getBankClientValueLang(String configNode,
			String defaultSentence, String bank, String client, String value) {
		String line = getBankClientLang(configNode, defaultSentence, bank,
				client);
		line = line.replace("<value>", value);
		return line;
	}

	private String getBankWorldLang(String configNode, String defaultSentence,
			String bank, String world) {
		String line = getBankLang(configNode, defaultSentence, bank);
		line = line.replace("<world>", world);
		return line;
	}

	private String getWorldLang(String configNode, String defaultSentence,
			String world) {
		String line = getNormalLang(configNode, defaultSentence);
		line = line.replace("<world>", world);
		return line;
	}
}