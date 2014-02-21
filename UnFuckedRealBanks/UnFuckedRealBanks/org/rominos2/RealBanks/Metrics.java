/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.rominos2.RealBanks;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Metrics
{
  private static final int REVISION = 6;
  private static final String BASE_URL = "http://mcstats.org";
  private static final String REPORT_URL = "/report/%s";
  private static final String CUSTOM_DATA_SEPARATOR = "~~";
  private static final int PING_INTERVAL = 10;
  private final Plugin plugin;
  private final Set<Graph> graphs = Collections.synchronizedSet(new HashSet());

  private final Graph defaultGraph = new Graph("Default", null);
  private final YamlConfiguration configuration;
  private final File configurationFile;
  private final String guid;
  private final boolean debug;
  private final Object optOutLock = new Object();

  private volatile BukkitTask task = null;

  public Metrics(Plugin plugin) throws IOException {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }

    this.plugin = plugin;

    this.configurationFile = getConfigFile();
    this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);

    this.configuration.addDefault("opt-out", Boolean.valueOf(false));
    this.configuration.addDefault("guid", UUID.randomUUID().toString());
    this.configuration.addDefault("debug", Boolean.valueOf(false));

    if (this.configuration.get("guid", null) == null) {
      this.configuration.options().header("http://mcstats.org").copyDefaults(true);
      this.configuration.save(this.configurationFile);
    }

    this.guid = this.configuration.getString("guid");
    this.debug = this.configuration.getBoolean("debug", false);
  }

  public Graph createGraph(String name)
  {
    if (name == null) {
      throw new IllegalArgumentException("Graph name cannot be null");
    }

    Graph graph = new Graph(name, null);

    this.graphs.add(graph);

    return graph;
  }

  public void addGraph(Graph graph)
  {
    if (graph == null) {
      throw new IllegalArgumentException("Graph cannot be null");
    }

    this.graphs.add(graph);
  }

  public void addCustomData(Plotter plotter)
  {
    if (plotter == null) {
      throw new IllegalArgumentException("Plotter cannot be null");
    }

    this.defaultGraph.addPlotter(plotter);

    this.graphs.add(this.defaultGraph);
  }

  public boolean start()
  {
    synchronized (this.optOutLock)
    {
      if (isOptOut()) {
        return false;
      }

      if (this.task != null) {
        return true;
      }

      this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, new Runnable()
      {
        private boolean firstPost;

        public void run()
        {
          try {
            synchronized (Metrics.this.optOutLock)
            {
              if ((Metrics.this.isOptOut()) && (Metrics.this.task != null)) {
                Metrics.this.task.cancel();
                Metrics.this.task = null;

                for (Metrics.Graph graph : Metrics.this.graphs) {
                  graph.onOptOut();
                }

              }

            }

            Metrics.this.postPlugin(!(this.firstPost));

            this.firstPost = false;
          } catch (IOException e) {
            if (Metrics.this.debug)
              Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
          }
        }
      }
      , 0L, 12000L);

      return true; }  } 
  // ERROR //
  public boolean isOptOut() { // Byte code:
    //   0: aload_0
    //   1: getfield 69	org/rominos2/RealBanks/Metrics:optOutLock	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 94	org/rominos2/RealBanks/Metrics:configuration	Lorg/bukkit/configuration/file/YamlConfiguration;
    //   11: aload_0
    //   12: invokevirtual 82	org/rominos2/RealBanks/Metrics:getConfigFile	()Ljava/io/File;
    //   15: invokevirtual 216	org/bukkit/configuration/file/YamlConfiguration:load	(Ljava/io/File;)V
    //   18: goto +83 -> 101
    //   21: astore_2
    //   22: aload_0
    //   23: getfield 152	org/rominos2/RealBanks/Metrics:debug	Z
    //   26: ifeq +31 -> 57
    //   29: invokestatic 219	org/bukkit/Bukkit:getLogger	()Ljava/util/logging/Logger;
    //   32: getstatic 225	java/util/logging/Level:INFO	Ljava/util/logging/Level;
    //   35: new 231	java/lang/StringBuilder
    //   38: dup
    //   39: ldc 233
    //   41: invokespecial 235	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   44: aload_2
    //   45: invokevirtual 236	IOException:getMessage	()Ljava/lang/String;
    //   48: invokevirtual 239	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: invokevirtual 243	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   54: invokevirtual 244	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;)V
    //   57: aload_1
    //   58: monitorexit
    //   59: iconst_1
    //   60: ireturn
    //   61: astore_2
    //   62: aload_0
    //   63: getfield 152	org/rominos2/RealBanks/Metrics:debug	Z
    //   66: ifeq +31 -> 97
    //   69: invokestatic 219	org/bukkit/Bukkit:getLogger	()Ljava/util/logging/Logger;
    //   72: getstatic 225	java/util/logging/Level:INFO	Ljava/util/logging/Level;
    //   75: new 231	java/lang/StringBuilder
    //   78: dup
    //   79: ldc 233
    //   81: invokespecial 235	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   84: aload_2
    //   85: invokevirtual 250	org/bukkit/configuration/InvalidConfigurationException:getMessage	()Ljava/lang/String;
    //   88: invokevirtual 239	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: invokevirtual 243	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   94: invokevirtual 244	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;)V
    //   97: aload_1
    //   98: monitorexit
    //   99: iconst_1
    //   100: ireturn
    //   101: aload_0
    //   102: getfield 94	org/rominos2/RealBanks/Metrics:configuration	Lorg/bukkit/configuration/file/YamlConfiguration;
    //   105: ldc 96
    //   107: iconst_0
    //   108: invokevirtual 148	org/bukkit/configuration/file/YamlConfiguration:getBoolean	(Ljava/lang/String;Z)Z
    //   111: aload_1
    //   112: monitorexit
    //   113: ireturn
    //   114: aload_1
    //   115: monitorexit
    //   116: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   7	18	21	IOException
    //   7	18	61	org/bukkit/configuration/InvalidConfigurationException
    //   7	59	114	finally
    //   61	99	114	finally
    //   101	113	114	finally
    //   114	116	114	finally 
	  } 
  public void enable() throws IOException { synchronized (this.optOutLock)
    {
      if (isOptOut()) {
        this.configuration.set("opt-out", Boolean.valueOf(false));
        this.configuration.save(this.configurationFile);
      }

      if (this.task == null)
        start();
    }
  }

  public void disable()
    throws IOException
  {
    synchronized (this.optOutLock)
    {
      if (!(isOptOut())) {
        this.configuration.set("opt-out", Boolean.valueOf(true));
        this.configuration.save(this.configurationFile);
      }

      if (this.task != null) {
        this.task.cancel();
        this.task = null;
      }
    }
  }

  public File getConfigFile()
  {
    File pluginsFolder = this.plugin.getDataFolder().getParentFile();

    return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
  }

  private void postPlugin(boolean isPing)
    throws IOException
  {
    PluginDescriptionFile description = this.plugin.getDescription();
    String pluginName = description.getName();
    boolean onlineMode = Bukkit.getServer().getOnlineMode();
    String pluginVersion = description.getVersion();
    String serverVersion = Bukkit.getVersion();
    int playersOnline = Bukkit.getServer().getOnlinePlayers().length;

    StringBuilder data = new StringBuilder();

    data.append(encode("guid")).append('=').append(encode(this.guid));
    encodeDataPair(data, "version", pluginVersion);
    encodeDataPair(data, "server", serverVersion);
    encodeDataPair(data, "players", Integer.toString(playersOnline));
    encodeDataPair(data, "revision", String.valueOf(6));

    String osname = System.getProperty("os.name");
    String osarch = System.getProperty("os.arch");
    String osversion = System.getProperty("os.version");
    String java_version = System.getProperty("java.version");
    int coreCount = Runtime.getRuntime().availableProcessors();

    if (osarch.equals("amd64")) {
      osarch = "x86_64";
    }

    encodeDataPair(data, "osname", osname);
    encodeDataPair(data, "osarch", osarch);
    encodeDataPair(data, "osversion", osversion);
    encodeDataPair(data, "cores", Integer.toString(coreCount));
    encodeDataPair(data, "online-mode", Boolean.toString(onlineMode));
    encodeDataPair(data, "java_version", java_version);

    if (isPing) {
      encodeDataPair(data, "ping", "true");
    }

    synchronized (this.graphs) {
      Iterator iter = this.graphs.iterator();

      while (iter.hasNext()) {
        Graph graph = (Graph)iter.next();

        for (Plotter plotter : graph.getPlotters())
        {
          String key = String.format("C%s%s%s%s", new Object[] { "~~", graph.getName(), "~~", plotter.getColumnName() });

          String value = Integer.toString(plotter.getValue());

          encodeDataPair(data, key, value);
        }
      }

    }

    URL url = new URL("http://mcstats.org" + String.format("/report/%s", new Object[] { encode(pluginName) }));
    URLConnection connection;
   // URLConnection connection;
    if (isMineshafterPresent())
      connection = url.openConnection(Proxy.NO_PROXY);
    else {
      connection = url.openConnection();
    }

    connection.setDoOutput(true);

    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
    writer.write(data.toString());
    writer.flush();

    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String response = reader.readLine();

    writer.close();
    reader.close();

    if ((response == null) || (response.startsWith("ERR"))) {
      throw new IOException(response);
    }

    if (response.contains("OK This is your first update this hour"))
      synchronized (this.graphs) {
        Iterator iter = this.graphs.iterator();

        while (iter.hasNext()) {
          Graph graph = (Graph)iter.next();

          for (Plotter plotter : graph.getPlotters())
            plotter.reset();
        }
      }
  }

  private boolean isMineshafterPresent()
  {
    try
    {
      Class.forName("mineshafter.MineServer");
      return true; } catch (Exception e) {
    }
    return false;
  }

  private static void encodeDataPair(StringBuilder buffer, String key, String value)
    throws UnsupportedEncodingException
  {
    buffer.append('&').append(encode(key)).append('=').append(encode(value));
  }

  private static String encode(String text)
    throws UnsupportedEncodingException
  {
    return URLEncoder.encode(text, "UTF-8");
  }

  public static class Graph
  {
    private final String name;
    private final Set<Metrics.Plotter> plotters = new LinkedHashSet();

    private Graph(String name) {
      this.name = name;
    }

    public String getName()
    {
      return this.name;
    }

    public void addPlotter(Metrics.Plotter plotter)
    {
      this.plotters.add(plotter);
    }

    public void removePlotter(Metrics.Plotter plotter)
    {
      this.plotters.remove(plotter);
    }

    public Set<Metrics.Plotter> getPlotters()
    {
      return Collections.unmodifiableSet(this.plotters);
    }

    public int hashCode()
    {
      return this.name.hashCode();
    }

    public boolean equals(Object object)
    {
      if (!(object instanceof Graph)) {
        return false;
      }

      Graph graph = (Graph)object;
      return graph.name.equals(this.name);
    }

    protected void onOptOut()
    {
    }
  }

  public static abstract class Plotter
  {
    private final String name;

    public Plotter()
    {
      this("Default");
    }

    public Plotter(String name)
    {
      this.name = name;
    }

    public abstract int getValue();

    public String getColumnName()
    {
      return this.name;
    }

    public void reset()
    {
    }

    public int hashCode()
    {
      return getColumnName().hashCode();
    }

    public boolean equals(Object object)
    {
      if (!(object instanceof Plotter)) {
        return false;
      }

      Plotter plotter = (Plotter)object;
      return ((plotter.name.equals(this.name)) && (plotter.getValue() == getValue()));
    }
  }
}