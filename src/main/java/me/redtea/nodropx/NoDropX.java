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
import me.redtea.nodropx.folia.FoliaSupporter;
import me.redtea.nodropx.libs.bstats.Metrics;
import me.redtea.nodropx.libs.carcadex.repo.MutableRepo;
import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.libs.message.verifier.impl.FileMessageVerifier;
import me.redtea.nodropx.listener.ConfirmDropHandler;
import me.redtea.nodropx.listener.InventoryHandler;
import me.redtea.nodropx.listener.PlayerHandler;
import me.redtea.nodropx.listener.menu.SinglePageGuiHandler;
import me.redtea.nodropx.menu.CachedMenu;
import me.redtea.nodropx.menu.Menu;
import me.redtea.nodropx.menu.dropconfirm.DropConfirmMFGui;
import me.redtea.nodropx.menu.facade.MenuFacade;
import me.redtea.nodropx.menu.facade.impl.MenuFacadeImpl;
import me.redtea.nodropx.menu.storage.pages.MFPageGui;
import me.redtea.nodropx.menu.storage.singlepage.SinglePageController;
import me.redtea.nodropx.menu.storage.singlepage.SinglePageGui;
import me.redtea.nodropx.model.cosmetic.impl.DisplayNameCosmetic;
import me.redtea.nodropx.model.materialprovider.impl.ItemStackProviderDefault;
import me.redtea.nodropx.model.materialprovider.impl.ItemsAdderProvider;
import me.redtea.nodropx.schema.ItemStackListSchema;
import me.redtea.nodropx.service.capasity.CapacityService;
import me.redtea.nodropx.service.capasity.impl.CapacityServiceImpl;
import me.redtea.nodropx.service.cosmetic.CosmeticService;
import me.redtea.nodropx.service.cosmetic.impl.CosmeticServiceImpl;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import me.redtea.nodropx.service.dropconfirm.impl.DropConfirmNull;
import me.redtea.nodropx.service.dropconfirm.impl.DropConfirmServiceImpl;
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
import me.redtea.nodropx.util.ConfigVerifier;
import me.redtea.nodropx.util.FoliaSupportedUtils;
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

public final class NoDropX extends JavaPlugin {
    private static NoDropAPI noDropAPIInstance;
    @Getter
    private static NoDropX instance;
    @Getter
    private Messages messages;
    private CosmeticService noDropCosmeticService;
    @Getter
    private boolean supportHEX;
    @Getter
    private boolean supportKyoriLore;
    @Getter
    private boolean supportItemsAdder;
    private MutableRepo<UUID, List<ItemStack>> personalStorageRepo;
    private CapacityService capacityService;
    private MenuFacade menuFacade;
    private NoDropService noDropService;
    private ItemStackService itemStackService;
    private DropConfirmService dropConfirmService;
    private FoliaSupportedUtils foliaSupportedUtils;

    @Override
    public void onEnable() {
        instance = this;
        new ConfigVerifier().verifyDefaults(this);

        foliaSupportedUtils = new FoliaSupportedUtils(this);

        TextServicesFactory textServicesFactory = new TextServicesFactoryImpl();
        TextContext textContext = textServicesFactory.create(this);
        supportHEX = textContext.isSupportHex();
        supportKyoriLore = textContext.isSupportKyori();
        messages = supportKyoriLore ? Messages.of(textContext.getMessages()) : Messages.legacy(textContext.getMessages());
        messages.setVerifier(new FileMessageVerifier(getResource(textContext.getPath()), YamlConfiguration.loadConfiguration(textContext.getMessages()), textContext.getMessages()));

        noDropCosmeticService = new CosmeticServiceImpl();
        noDropCosmeticService.add(new DisplayNameCosmetic());
        noDropCosmeticService.add(textContext.getLoreStrategy());
        noDropCosmeticService.load(YamlConfiguration.loadConfiguration(textContext.getCosmetics()));

        dropConfirmService = getConfig().getBoolean("confirmThrowNoDropItem") ? new DropConfirmServiceImpl() : new DropConfirmNull();
        NBTService nbtService = new Tr7zwNBTServiceImpl();
        noDropService = new NoDropServiceImpl(nbtService, noDropCosmeticService);
        NoDropItemFactory noDropItemFactory = new NoDropItemFactoryImpl(noDropService);
        EventService eventService = new EventServiceImpl(noDropService);
        capacityService = new CapacityServiceImpl(this);
        RespawnService respawnService = new RespawnServiceImpl(noDropService, capacityService);

        //EXPERIMENTAL! Folia support!
        FoliaSupporter foliaSupporter = new FoliaSupporter();
        foliaSupporter.run(respawnService, this);

        personalStorageRepo = Repo.<UUID, List<ItemStack>>builder()
                .autoSave(1000 * 60 * 15L)
                .schema(new ItemStackListSchema(new File(getDataFolder(), "storage")))
                .plugin(this)
                .build();
        itemStackService = initItemStackService();


        menuFacade = initMenus();
        StorageService storageService = new StorageServiceImpl(menuFacade, personalStorageRepo, noDropService);


        NoDropAPI noDropAPI = new NoDropXFacade(noDropService, noDropItemFactory, storageService, capacityService, itemStackService);
        noDropAPIInstance = noDropAPI;


        Bukkit.getPluginManager().registerEvents(new InventoryHandler(eventService, noDropService, dropConfirmService), this);
        Bukkit.getPluginManager().registerEvents(new PlayerHandler(respawnService, eventService, menuFacade), this);
        Bukkit.getPluginManager().registerEvents(new ConfirmDropHandler(dropConfirmService, menuFacade), this);

        Bukkit.getServicesManager().register(NoDropAPI.class,
                noDropAPI, this, ServicePriority.Normal);

        CommandManager commandManager = new CommandManager(this);
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> messages.get("noPerms").send(sender));
        commandManager.getMessageHandler().register("cmd.no.console", sender -> messages.get("noConsole").send(sender));
        commandManager.getMessageHandler().register("cmd.no.exists", sender -> messages.get("noExists").send(sender));
        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> messages.get("usage").send(sender));
        commandManager.getCompletionHandler().register("#materials", input -> itemStackService.allMaterials());
        commandManager.register(new NoDropCommand(messages, noDropAPI, menuFacade, this, itemStackService, dropConfirmService));
        bStatsInit();
        printCredits();
        checkUpdates();
    }

    private MenuFacade initMenus() {
        CachedMenu personalStorageCachedMenu;
        if (getConfig().getString("personalStorageType").equalsIgnoreCase("SINGLE_PAGE")) {
            SinglePageGui singlePageGui = new SinglePageGui(messages, personalStorageRepo);
            personalStorageCachedMenu = singlePageGui;
            Bukkit.getPluginManager().registerEvents(new SinglePageGuiHandler(new SinglePageController(noDropService,
                    singlePageGui, personalStorageRepo, foliaSupportedUtils), singlePageGui), this);
        } else
            personalStorageCachedMenu = new MFPageGui(messages, personalStorageRepo, noDropService, itemStackService, this);

        Menu confirmMenu = new DropConfirmMFGui(messages, dropConfirmService, this);
        return new MenuFacadeImpl(personalStorageCachedMenu, confirmMenu);
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
        if (!getConfig().getBoolean("checkForUpdates")) {
            return;
        }
        new UpdateChecker(this, 111485, foliaSupportedUtils).getVersion(version -> {
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
        printCreditLine("&a************************************************************");
        printCreditLine("&a* &eNoDropX version &6" + getDescription().getVersion() + "&7 (uses CarcadeX v. 1.0.7)");
        printCreditLine("&a* &eBy &citzRedTea");
        printCreditLine("&a* &eDetected bukkit version &6" + Bukkit.getBukkitVersion());
        printCreditLine("&a* &esupport HEX colors " + (supportHEX ? "&2YES" : "&4NO"));
        printCreditLine("&a* &esupport Kyori as lore " + (supportKyoriLore ? "&2YES" : "&4NO"));
        printCreditLine("&a************************************************************");
    }

    private void printCreditLine(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    public void onReload() {
        personalStorageRepo.saveAll();
        menuFacade.clearAllCache();
        capacityService.reload();
        messages.reload(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml")));
        noDropCosmeticService.load(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "cosmetics.yml")));
    }

    @Override
    public void onDisable() {
        personalStorageRepo.saveAll();
    }

    public static NoDropAPI getAPI() {
        return noDropAPIInstance;
    }
}
