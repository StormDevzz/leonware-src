package sweetie.leonware.api.system.configs;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/ThemeManager.class */
public class ThemeManager {
    private final String path;
    private final File dir;
    private final File lastFile;
    private static final ThemeManager instance = new ThemeManager();
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    @Generated
    public static ThemeManager getInstance() {
        return instance;
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

    public ThemeManager(String path) {
        this.path = path;
        this.dir = new File(path);
        this.lastFile = new File(this.dir, "last_selected");
        ensureDir();
    }

    public ThemeManager() {
        this(ClientInfo.CONFIG_PATH_THEMES);
    }

    public void save(Theme theme) {
        if (theme == null) {
            return;
        }
        ensureDir();
        Path out = this.dir.toPath().resolve(safeFileName(theme.getName()) + ".theme");
        List<String> lines = new ArrayList<>();
        lines.add("name=" + theme.getName());
        for (Theme.ElementColor ec : theme.getElementColors()) {
            lines.add(ec.getName() + "=" + ec.getColor().getRGB());
        }
        try {
            Files.write(out, lines, UTF8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for (ThemeSelectable ts : ThemeEditor.getInstance().getThemeSelectables()) {
            if (ts != null && ts.getTheme() != null) {
                save(ts.getTheme());
            }
        }
    }

    public void saveLastSelected(Theme theme) {
        if (theme == null) {
            return;
        }
        ensureDir();
        try {
            Files.writeString(this.lastFile.toPath(), safeFileName(theme.getName()), UTF8, new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean remove(String themeName) {
        if (themeName == null || themeName.isEmpty()) {
            return false;
        }
        ensureDir();
        File[] files = this.dir.listFiles((d, n) -> {
            return n.toLowerCase().endsWith(".theme");
        });
        if (files == null) {
            return false;
        }
        for (File file : files) {
            try {
                String first = Files.lines(file.toPath(), UTF8).findFirst().orElse("");
                if (first.startsWith("name=") && themeName.equals(first.substring(5).trim())) {
                    return file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Theme load(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        File f = new File(this.dir, safeFileName(name) + ".theme");
        if (f.exists()) {
            return load(f);
        }
        return null;
    }

    public Theme load(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        Map<String, String> map = readKeyValueFile(file.toPath());
        String name = map.getOrDefault("name", stripExtension(file.getName()));
        Theme theme = new Theme(name);
        for (Theme.ElementColor ec : theme.getElementColors()) {
            String key = ec.getName();
            if (map.containsKey(key)) {
                try {
                    int rgb = Integer.parseInt(map.get(key));
                    ec.setColor(new Color(rgb, true));
                } catch (NumberFormatException e) {
                }
            }
        }
        return theme;
    }

    public void refresh() {
        List<ThemeSelectable> selectables = ThemeEditor.getInstance().getThemeSelectables();
        selectables.clear();
        ensureDir();
        Theme[] defaults = {new Theme(ClientInfo.NAME), new BlueTheme().update(), new CandyLoveTheme().update(), new CrimsonTheme().update(), new ForestTheme().update(), new SunsetTheme().update(), new OceanTheme().update(), new NeonTheme().update(), new GoldTheme().update(), new IcyTheme().update(), new RoseGoldTheme().update(), new MidnightTheme().update()};
        File[] files = this.dir.listFiles((d, name) -> {
            return name.toLowerCase().endsWith(".theme");
        });
        List<String> existing = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                existing.add(f.getName().toLowerCase().replace(".theme", ""));
            }
        }
        boolean savedAny = false;
        for (Theme t : defaults) {
            if (!existing.contains(safeFileName(t.getName()).toLowerCase())) {
                save(t);
                savedAny = true;
            }
        }
        if (savedAny) {
            files = this.dir.listFiles((d2, name2) -> {
                return name2.toLowerCase().endsWith(".theme");
            });
        }
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparing((v0) -> {
                return v0.getName();
            }, String.CASE_INSENSITIVE_ORDER));
            for (File f2 : files) {
                Theme loaded = load(f2);
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
            String safeName = Files.readString(this.lastFile.toPath(), UTF8).trim();
            if (safeName.isEmpty()) {
                return null;
            }
            File f = new File(this.dir, safeName + ".theme");
            if (f.exists()) {
                return load(f);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void ensureDir() {
        if (!this.dir.exists()) {
            this.dir.mkdirs();
            loadDefaultThemes();
        }
    }

    private void loadDefaultThemes() {
        Theme[] themes = {new Theme(ClientInfo.NAME), new BlueTheme().update(), new CandyLoveTheme().update(), new CrimsonTheme().update(), new ForestTheme().update(), new SunsetTheme().update(), new OceanTheme().update(), new NeonTheme().update(), new GoldTheme().update(), new IcyTheme().update(), new RoseGoldTheme().update(), new MidnightTheme().update()};
        List<ThemeSelectable> themeSelectables = new ArrayList<>();
        for (Theme theme : themes) {
            themeSelectables.add(new ThemeSelectable(theme));
        }
        ThemeEditor.getInstance().getThemeSelectables().addAll(themeSelectables);
    }

    private Map<String, String> readKeyValueFile(Path p) {
        int idx;
        Map<String, String> map = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(p, UTF8);
            for (String raw : lines) {
                String line = raw.trim();
                if (!line.isEmpty() && !line.startsWith("#") && (idx = line.indexOf(61)) > 0) {
                    String key = line.substring(0, idx);
                    String value = line.substring(idx + 1);
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
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
}
