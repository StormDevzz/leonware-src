/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.configs;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.client.ui.theme.Theme;
import sweetie.leonware.client.ui.theme.ThemeEditor;
import sweetie.leonware.client.ui.theme.ThemeSelectable;
import sweetie.leonware.client.ui.theme.basic.BlueTheme;
import sweetie.leonware.client.ui.theme.basic.CandyLoveTheme;
import sweetie.leonware.client.ui.theme.basic.CrimsonTheme;
import sweetie.leonware.client.ui.theme.basic.ForestTheme;
import sweetie.leonware.client.ui.theme.basic.GoldTheme;
import sweetie.leonware.client.ui.theme.basic.IcyTheme;
import sweetie.leonware.client.ui.theme.basic.MidnightTheme;
import sweetie.leonware.client.ui.theme.basic.NeonTheme;
import sweetie.leonware.client.ui.theme.basic.OceanTheme;
import sweetie.leonware.client.ui.theme.basic.RoseGoldTheme;
import sweetie.leonware.client.ui.theme.basic.SunsetTheme;

public class ThemeManager {
    private static final ThemeManager instance = new ThemeManager();
    private final String path;
    private final File dir;
    private final File lastFile;
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public ThemeManager(String path) {
        this.path = path;
        this.dir = new File(path);
        this.lastFile = new File(this.dir, "last_selected");
        this.ensureDir();
    }

    public ThemeManager() {
        this(ClientInfo.CONFIG_PATH_THEMES);
    }

    public void save(Theme theme) {
        if (theme == null) {
            return;
        }
        this.ensureDir();
        Path out = this.dir.toPath().resolve(this.safeFileName(theme.getName()) + ".theme");
        ArrayList<CallSite> lines = new ArrayList<CallSite>();
        lines.add((CallSite)((Object)("name=" + theme.getName())));
        for (Theme.ElementColor ec : theme.getElementColors()) {
            lines.add((CallSite)((Object)(ec.getName() + "=" + ec.getColor().getRGB())));
        }
        try {
            Files.write(out, lines, UTF8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for (ThemeSelectable ts : ThemeEditor.getInstance().getThemeSelectables()) {
            if (ts == null || ts.getTheme() == null) continue;
            this.save(ts.getTheme());
        }
    }

    public void saveLastSelected(Theme theme) {
        if (theme == null) {
            return;
        }
        this.ensureDir();
        try {
            Files.writeString(this.lastFile.toPath(), (CharSequence)this.safeFileName(theme.getName()), UTF8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean remove(String themeName) {
        if (themeName == null || themeName.isEmpty()) {
            return false;
        }
        this.ensureDir();
        File[] files = this.dir.listFiles((d, n) -> n.toLowerCase().endsWith(".theme"));
        if (files == null) {
            return false;
        }
        for (File file : files) {
            try {
                String first = Files.lines(file.toPath(), UTF8).findFirst().orElse("");
                if (!first.startsWith("name=") || !themeName.equals(first.substring(5).trim())) continue;
                return file.delete();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Theme load(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        File f = new File(this.dir, this.safeFileName(name) + ".theme");
        return f.exists() ? this.load(f) : null;
    }

    public Theme load(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        Map<String, String> map = this.readKeyValueFile(file.toPath());
        String name = map.getOrDefault("name", this.stripExtension(file.getName()));
        Theme theme = new Theme(name);
        for (Theme.ElementColor ec : theme.getElementColors()) {
            String key = ec.getName();
            if (!map.containsKey(key)) continue;
            try {
                int rgb = Integer.parseInt(map.get(key));
                ec.setColor(new Color(rgb, true));
            }
            catch (NumberFormatException numberFormatException) {}
        }
        return theme;
    }

    public void refresh() {
        List<ThemeSelectable> selectables = ThemeEditor.getInstance().getThemeSelectables();
        selectables.clear();
        this.ensureDir();
        Theme[] defaults = new Theme[]{new Theme("LeonWare"), new BlueTheme().update(), new CandyLoveTheme().update(), new CrimsonTheme().update(), new ForestTheme().update(), new SunsetTheme().update(), new OceanTheme().update(), new NeonTheme().update(), new GoldTheme().update(), new IcyTheme().update(), new RoseGoldTheme().update(), new MidnightTheme().update()};
        File[] files = this.dir.listFiles((d, name) -> name.toLowerCase().endsWith(".theme"));
        ArrayList<String> existing = new ArrayList<String>();
        if (files != null) {
            for (File f : files) {
                existing.add(f.getName().toLowerCase().replace(".theme", ""));
            }
        }
        boolean savedAny = false;
        for (Theme t : defaults) {
            if (existing.contains(this.safeFileName(t.getName()).toLowerCase())) continue;
            this.save(t);
            savedAny = true;
        }
        if (savedAny) {
            files = this.dir.listFiles((d, name) -> name.toLowerCase().endsWith(".theme"));
        }
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
            for (File f : files) {
                Theme loaded = this.load(f);
                if (loaded == null) continue;
                selectables.add(new ThemeSelectable(loaded));
            }
        }
    }

    public Theme loadLastSelected() {
        if (!this.lastFile.exists()) {
            return null;
        }
        try {
            String safeName = Files.readString(this.lastFile.toPath(), UTF8).trim();
            if (safeName.isEmpty()) {
                return null;
            }
            File f = new File(this.dir, safeName + ".theme");
            return f.exists() ? this.load(f) : null;
        }
        catch (IOException e) {
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
        Theme[] themes = new Theme[]{new Theme("LeonWare"), new BlueTheme().update(), new CandyLoveTheme().update(), new CrimsonTheme().update(), new ForestTheme().update(), new SunsetTheme().update(), new OceanTheme().update(), new NeonTheme().update(), new GoldTheme().update(), new IcyTheme().update(), new RoseGoldTheme().update(), new MidnightTheme().update()};
        ArrayList<ThemeSelectable> themeSelectables = new ArrayList<ThemeSelectable>();
        for (Theme theme : themes) {
            themeSelectables.add(new ThemeSelectable(theme));
        }
        ThemeEditor.getInstance().getThemeSelectables().addAll(themeSelectables);
    }

    private Map<String, String> readKeyValueFile(Path p) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            List<String> lines = Files.readAllLines(p, UTF8);
            for (String raw : lines) {
                int idx;
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#") || (idx = line.indexOf(61)) <= 0) continue;
                String key = line.substring(0, idx);
                String value = line.substring(idx + 1);
                map.put(key, value);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public String safeFileName(String name) {
        return name == null ? "theme" : name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    private String stripExtension(String name) {
        int idx = name.lastIndexOf(46);
        return idx <= 0 ? name : name.substring(0, idx);
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
        return instance;
    }
}

