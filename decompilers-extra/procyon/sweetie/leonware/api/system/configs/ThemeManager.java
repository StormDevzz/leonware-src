// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import java.nio.charset.StandardCharsets;
import lombok.Generated;
import java.util.HashMap;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.Function;
import java.util.Comparator;
import sweetie.leonware.client.ui.theme.basic.MidnightTheme;
import sweetie.leonware.client.ui.theme.basic.RoseGoldTheme;
import sweetie.leonware.client.ui.theme.basic.IcyTheme;
import sweetie.leonware.client.ui.theme.basic.GoldTheme;
import sweetie.leonware.client.ui.theme.basic.NeonTheme;
import sweetie.leonware.client.ui.theme.basic.OceanTheme;
import sweetie.leonware.client.ui.theme.basic.SunsetTheme;
import sweetie.leonware.client.ui.theme.basic.ForestTheme;
import sweetie.leonware.client.ui.theme.basic.CrimsonTheme;
import sweetie.leonware.client.ui.theme.basic.CandyLoveTheme;
import sweetie.leonware.client.ui.theme.basic.BlueTheme;
import java.util.Map;
import java.awt.Color;
import sweetie.leonware.client.ui.theme.ThemeSelectable;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import java.util.Iterator;
import java.util.List;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import sweetie.leonware.client.ui.theme.Theme;
import sweetie.leonware.api.system.backend.ClientInfo;
import java.nio.charset.Charset;
import java.io.File;

public class ThemeManager
{
    private static final ThemeManager instance;
    private final String path;
    private final File dir;
    private final File lastFile;
    private static final Charset UTF8;
    
    public ThemeManager(final String path) {
        this.path = path;
        this.dir = new File(path);
        this.lastFile = new File(this.dir, "last_selected");
        this.ensureDir();
    }
    
    public ThemeManager() {
        this(ClientInfo.CONFIG_PATH_THEMES);
    }
    
    public void save(final Theme theme) {
        if (theme == null) {
            return;
        }
        this.ensureDir();
        final Path out = this.dir.toPath().resolve(this.safeFileName(theme.getName()) + ".theme");
        final List<String> lines = new ArrayList<String>();
        lines.add("name=" + theme.getName());
        for (Theme.ElementColor ec : theme.getElementColors()) {
            lines.add(ec.getName() + "=" + ec.getColor().getRGB());
        }
        try {
            Files.write(out, lines, ThemeManager.UTF8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveAll() {
        for (final ThemeSelectable ts : ThemeEditor.getInstance().getThemeSelectables()) {
            if (ts != null && ts.getTheme() != null) {
                this.save(ts.getTheme());
            }
        }
    }
    
    public void saveLastSelected(final Theme theme) {
        if (theme == null) {
            return;
        }
        this.ensureDir();
        try {
            Files.writeString(this.lastFile.toPath(), this.safeFileName(theme.getName()), ThemeManager.UTF8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean remove(final String themeName) {
        if (themeName == null || themeName.isEmpty()) {
            return false;
        }
        this.ensureDir();
        final File[] files = this.dir.listFiles((d, n) -> n.toLowerCase().endsWith(".theme"));
        if (files == null) {
            return false;
        }
        for (final File file : files) {
            try {
                final String first = Files.lines(file.toPath(), ThemeManager.UTF8).findFirst().orElse("");
                if (first.startsWith("name=") && themeName.equals(first.substring(5).trim())) {
                    return file.delete();
                }
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public Theme load(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        final File f = new File(this.dir, this.safeFileName(name) + ".theme");
        return f.exists() ? this.load(f) : null;
    }
    
    public Theme load(final File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        final Map<String, String> map = this.readKeyValueFile(file.toPath());
        final String name = map.getOrDefault("name", this.stripExtension(file.getName()));
        final Theme theme = new Theme(name);
        for (final Theme.ElementColor ec : theme.getElementColors()) {
            final String key = ec.getName();
            if (map.containsKey(key)) {
                try {
                    final int rgb = Integer.parseInt(map.get(key));
                    ec.setColor(new Color(rgb, true));
                }
                catch (final NumberFormatException ex) {}
            }
        }
        return theme;
    }
    
    public void refresh() {
        final List<ThemeSelectable> selectables = ThemeEditor.getInstance().getThemeSelectables();
        selectables.clear();
        this.ensureDir();
        final Theme[] defaults = { new Theme("LeonWare"), new BlueTheme().update(), new CandyLoveTheme().update(), new CrimsonTheme().update(), new ForestTheme().update(), new SunsetTheme().update(), new OceanTheme().update(), new NeonTheme().update(), new GoldTheme().update(), new IcyTheme().update(), new RoseGoldTheme().update(), new MidnightTheme().update() };
        File[] files = this.dir.listFiles((d, name) -> name.toLowerCase().endsWith(".theme"));
        final List<String> existing = new ArrayList<String>();
        if (files != null) {
            for (final File f : files) {
                existing.add(f.getName().toLowerCase().replace(".theme", ""));
            }
        }
        boolean savedAny = false;
        for (final Theme t : defaults) {
            if (!existing.contains(this.safeFileName(t.getName()).toLowerCase())) {
                this.save(t);
                savedAny = true;
            }
        }
        if (savedAny) {
            files = this.dir.listFiles((d, name) -> name.toLowerCase().endsWith(".theme"));
        }
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparing((Function<? super File, ?>)File::getName, (Comparator<? super Object>)String.CASE_INSENSITIVE_ORDER));
            for (final File f2 : files) {
                final Theme loaded = this.load(f2);
                if (loaded != null) {
                    selectables.add(new ThemeSelectable(loaded));
                }
            }
        }
    }
    
    public Theme loadLastSelected() {
        if (!this.lastFile.exists()) {
            return null;
        }
        try {
            final String safeName = Files.readString(this.lastFile.toPath(), ThemeManager.UTF8).trim();
            if (safeName.isEmpty()) {
                return null;
            }
            final File f = new File(this.dir, safeName + ".theme");
            return f.exists() ? this.load(f) : null;
        }
        catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void ensureDir() {
        if (!this.dir.exists()) {
            this.dir.mkdirs();
            this.loadDefaultThemes();
        }
    }
    
    private void loadDefaultThemes() {
        final Theme[] themes = { new Theme("LeonWare"), new BlueTheme().update(), new CandyLoveTheme().update(), new CrimsonTheme().update(), new ForestTheme().update(), new SunsetTheme().update(), new OceanTheme().update(), new NeonTheme().update(), new GoldTheme().update(), new IcyTheme().update(), new RoseGoldTheme().update(), new MidnightTheme().update() };
        final List<ThemeSelectable> themeSelectables = new ArrayList<ThemeSelectable>();
        for (final Theme theme : themes) {
            themeSelectables.add(new ThemeSelectable(theme));
        }
        ThemeEditor.getInstance().getThemeSelectables().addAll(themeSelectables);
    }
    
    private Map<String, String> readKeyValueFile(final Path p) {
        final Map<String, String> map = new HashMap<String, String>();
        try {
            final List<String> lines = Files.readAllLines(p, ThemeManager.UTF8);
            for (final String raw : lines) {
                final String line = raw.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    final int idx = line.indexOf(61);
                    if (idx <= 0) {
                        continue;
                    }
                    final String key = line.substring(0, idx);
                    final String value = line.substring(idx + 1);
                    map.put(key, value);
                }
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    public String safeFileName(final String name) {
        return (name == null) ? "theme" : name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
    
    private String stripExtension(final String name) {
        final int idx = name.lastIndexOf(46);
        return (idx <= 0) ? name : name.substring(0, idx);
    }
    
    @Generated
    public String getPath() {
        return this.path;
    }
    
    @Generated
    public File getDir() {
        return this.dir;
    }
    
    @Generated
    public File getLastFile() {
        return this.lastFile;
    }
    
    @Generated
    public static ThemeManager getInstance() {
        return ThemeManager.instance;
    }
    
    static {
        instance = new ThemeManager();
        UTF8 = StandardCharsets.UTF_8;
    }
}
