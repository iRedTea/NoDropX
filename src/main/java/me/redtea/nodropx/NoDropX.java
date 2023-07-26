package me.redtea.nodropx;

import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import me.redtea.nodropx.api.facade.NoDropAPI;
import me.redtea.nodropx.api.facade.impl.NoDropXFacade;
import me.redtea.nodropx.command.NoDropCommand;
import me.redtea.nodropx.factory.item.NoDropItemFactory;
import me.redtea.nodropx.factory.item.impl.NoDropItemFactoryImpl;
import me.redtea.nodropx.factory.text.TextContext;
import me.redtea.nodropx.factory.text.TextServicesFactory;
import me.redtea.nodropx.factory.text.impl.TextServicesFactoryImpl;
import me.redtea.nodropx.gui.facade.GuiFacade;
import me.redtea.nodropx.gui.facade.impl.GuiFacadeImpl;
import me.redtea.nodropx.gui.pages.StorageGui;
import me.redtea.nodropx.libs.bstats.Metrics;
import me.redtea.nodropx.libs.carcadex.repo.MutableRepo;
import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.libs.carcadex.repo.impl.yaml.YamlRepo;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.libs.message.verifier.impl.FileMessageVerifier;
import me.redtea.nodropx.listener.InventoryHandler;
import me.redtea.nodropx.listener.PlayerHandler;
import me.redtea.nodropx.model.cosmetic.impl.DisplayNameCosmetic;
import me.redtea.nodropx.model.materialprovider.impl.ItemStackProviderDefault;
import me.redtea.nodropx.model.materialprovider.impl.ItemsAdderProvider;
import me.redtea.nodropx.schema.ItemStackListSchema;
import me.redtea.nodropx.service.cosmetic.CosmeticService;
import me.redtea.nodropx.service.cosmetic.impl.CosmeticServiceImpl;
import me.redtea.nodropx.service.event.EventService;
import me.redtea.nodropx.service.event.impl.EventServiceImpl;
import me.redtea.nodropx.service.material.ItemStackService;
import me.redtea.nodropx.service.material.impl.ItemStackServiceImpl;
import me.redtea.nodropx.service.nbt.NBTService;
import me.redtea.nodropx.service.nbt.impl.Tr7zwNBTServiceImpl;
import me.redtea.nodropx.service.nodrop.NoDropService;
import me.redtea.nodropx.service.nodrop.impl.NoDropServiceImpl;
import me.redtea.nodropx.service.respawn.RespawnService;
import me.redtea.nodropx.service.respawn.impl.RespawnServiceImpl;
import me.redtea.nodropx.service.storage.StorageService;
import me.redtea.nodropx.service.storage.impl.StorageServiceImpl;
import me.redtea.nodropx.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class NoDropX extends JavaPlugin {
    private static NoDropAPI noDropAPIInstance;
    @Getter
    private static NoDropX instance;

    private Messages messages;
    private CosmeticService cosmeticService;
    @Getter
    private boolean supportHEX;
    @Getter
    private boolean supportKyoriLore;
    @Getter
    private boolean supportItemsAdder;
    private MutableRepo<UUID, List<ItemStack>> pagesRepo;
    private Repo<String, List<Integer>> capacity;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        TextServicesFactory textServicesFactory = new TextServicesFactoryImpl();
        TextContext textContext = textServicesFactory.create(this);
        supportHEX = textContext.isSupportHex();
        supportKyoriLore = textContext.isSupportKyori();
        messages = supportKyoriLore ? Messages.of(textContext.getMessages()) : Messages.legacy(textContext.getMessages());
        messages.setVerifier(new FileMessageVerifier(getResource(textContext.getPath()), YamlConfiguration.loadConfiguration(textContext.getMessages()), textContext.getMessages()));

        cosmeticService = new CosmeticServiceImpl();
        cosmeticService.add(new DisplayNameCosmetic());
        cosmeticService.add(textContext.getLoreStrategy());
        cosmeticService.load(YamlConfiguration.loadConfiguration(textContext.getCosmetics()));

        capacity = new YamlRepo<>(
                new File(getDataFolder(), "capacity.yml").toPath(),
                this,
                conf -> conf.getKeys(false).stream().collect(Collectors.toMap(
                        value -> value,
                        conf::getIntegerList))
        );

        NBTService nbtService = new Tr7zwNBTServiceImpl();
        NoDropService noDropService = new NoDropServiceImpl(nbtService, cosmeticService);
        NoDropItemFactory noDropItemFactory = new NoDropItemFactoryImpl(noDropService);
        EventService eventService = new EventServiceImpl(noDropService);
        RespawnService respawnService = new RespawnServiceImpl(noDropService, capacity);
        pagesRepo = Repo.<UUID, List<ItemStack>>builder()
                .autoSave(1000 * 60 * 15L)
                .schema(new ItemStackListSchema(new File(getDataFolder(), "storage")))
                .plugin(this)
                .build();
        ItemStackService itemStackService = initItemStackService();


        GuiFacade guiFacade = new GuiFacadeImpl(
                new StorageGui(messages, pagesRepo, noDropService, this, itemStackService)
        );
        StorageService storageService = new StorageServiceImpl(guiFacade, pagesRepo, noDropService);



        NoDropAPI noDropAPI = new NoDropXFacade(noDropService, noDropItemFactory, storageService, capacity, itemStackService);
        noDropAPIInstance = noDropAPI;


        Bukkit.getPluginManager().registerEvents(
                new InventoryHandler(eventService),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new PlayerHandler(respawnService, eventService, guiFacade),
                this
        );

        Bukkit.getServicesManager().register(NoDropAPI.class,
                noDropAPI, this, ServicePriority.Normal);

        CommandManager commandManager = new CommandManager(this);
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> messages.get("noPerms").send(sender));
        commandManager.getMessageHandler().register("cmd.no.console", sender -> messages.get("noConsole").send(sender));
        commandManager.getMessageHandler().register("cmd.no.exists", sender -> messages.get("noExists").send(sender));
        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> messages.get("usage").send(sender));
        commandManager.getCompletionHandler().register("#materials", input -> itemStackService.allMaterials());
        commandManager.register(new NoDropCommand(messages, noDropAPI, guiFacade, this, itemStackService));
        bStatsInit();
        printCredits();
        checkUpdates();
    }

    private ItemStackService initItemStackService() {
        ItemStackService result = new ItemStackServiceImpl();
        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null) {
            supportItemsAdder = true;
            getLogger().info(ChatColor.GREEN + "ItemsAdder found! Registering a CustomStack provider for it.");
            result.registerProvider(new ItemsAdderProvider());
        }
        result.registerProvider(new ItemStackProviderDefault());
        return result;
    }

    private void checkUpdates() {
        if(!(getConfig().contains("checkForUpdates") && getConfig().getBoolean("checkForUpdates"))) {
            return;
        }
        new UpdateChecker(this, 111485).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("There is a new update available: " + version);
                getLogger().info("Download it from https://www.spigotmc.org/resources/nodropx.111485/");
            }
        });
    }


    private void bStatsInit() {
        int pluginId = 19090;
        Metrics metrics = new Metrics(this, pluginId);
    }


    private void printCredits() {
        Bukkit.getLogger().info(ChatColor.GREEN + "************************************************************");
        Bukkit.getLogger().info(ChatColor.GREEN + "* " + ChatColor.YELLOW + "NoDropX version " + ChatColor.GOLD + getDescription().getVersion() + ChatColor.GRAY + " (uses CarcadeX v. 1.0.7)");
        Bukkit.getLogger().info(ChatColor.GREEN + "* " + ChatColor.YELLOW + "By " + ChatColor.RED + "itzRedTea");
        Bukkit.getLogger().info(ChatColor.GREEN + "* "+ ChatColor.YELLOW + "Detected bukkit version " + ChatColor.GOLD + Bukkit.getBukkitVersion());
        Bukkit.getLogger().info(ChatColor.GREEN + "* "+ ChatColor.YELLOW + "support HEX colors " + (supportHEX ? ChatColor.DARK_GREEN + "YES" : ChatColor.DARK_RED + "NO"));
        Bukkit.getLogger().info(ChatColor.GREEN + "* "+ ChatColor.YELLOW + "support Kyori as lore " + (supportKyoriLore ? ChatColor.DARK_GREEN + "YES" : ChatColor.DARK_RED + "NO"));
        Bukkit.getLogger().info(ChatColor.GREEN + "************************************************************");
    }

    public void onReload() {
        pagesRepo.saveAll();
        capacity.reload();
        messages.reload(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml")));
        cosmeticService.load(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "cosmetics.yml")));
    }

    @Override
    public void onDisable() {
        pagesRepo.saveAll();
    }

    public static NoDropAPI getAPI() {
        return noDropAPIInstance;
    }
}
