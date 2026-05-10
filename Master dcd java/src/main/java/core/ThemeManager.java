package core;

import factory.ui.UIFactory;
import factory.ui.LightUIFactory;
import factory.ui.DarkUIFactory;

public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDark = false;

    private ThemeManager() {
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public boolean isDarkMode() {
        return isDark;
    }

    public void toggleTheme() {
        isDark = !isDark;
    }

    public UIFactory getFactory() {
        return isDark ? new DarkUIFactory() : new LightUIFactory();
    }
}
