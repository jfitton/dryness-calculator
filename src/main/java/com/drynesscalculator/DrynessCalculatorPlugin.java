package com.drynesscalculator;

import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;


import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;

@PluginDescriptor(
        name="Dryness Calculator",
        description = "A plugin to calculate probability of receiving N drops within K kills given the drop rate."
)
public class DrynessCalculatorPlugin extends Plugin{

    private NavigationButton navigationButton;

    DrynessCalculatorPanel panel = null;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private DrynessCalculatorConfig config;

    @Provides
    DrynessCalculatorConfig getConfig(ConfigManager configManager) { return configManager.getConfig(DrynessCalculatorConfig.class);}

    @Override
    protected void startUp(){
        panel = injector.getInstance(DrynessCalculatorPanel.class);
        panel.init();

        BufferedImage icon = null;
        try {
            File iconFile = new File("src/main/resources/drynesscalculator/icon.png");
             icon = ImageIO.read(iconFile);
        } catch (Exception e) {
            System.out.println("Error reading image:");
            e.printStackTrace();
        }

        navigationButton = NavigationButton.builder()
                .tooltip("Dryness Calculator")
                .icon(icon)
                .priority(1)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navigationButton);
    }

    @Override
    protected void shutDown() {clientToolbar.removeNavigation(navigationButton);}
}
