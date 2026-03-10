package com.sunrisexdxd.sts2relics;

import basemod.BaseMod;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import basemod.helpers.RelicType;
import com.sunrisexdxd.sts2relics.relics.CampfireCookingRelic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

@SpireInitializer
public class STS2Relics implements EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(STS2Relics.class.getName());
    private static String modID;
    
    private static final String MODNAME = "STS2 Relics";
    private static final String AUTHOR = "SunriseXDXD";
    private static final String DESCRIPTION = "Adds Slay The Spire 2 relics to Slay the Spire";

    public static void initialize() {
        logger.info("========================= Initializing STS2Relics Mod. =========================");
        new STS2Relics();
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public STS2Relics() {
        logger.info("Subscribe to BaseMod hooks");
        BaseMod.subscribe(this);
        setModID("STS2Relics");
        logger.info("Done subscribing");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        BaseMod.addRelic(new CampfireCookingRelic(), RelicType.SHARED);
        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        Settings.GameLanguage language = languageSupport();
        BaseMod.loadCustomStringsFile(RelicStrings.class, getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/STS2Relics-Relic-Strings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/STS2Relics-UI-Strings.json");
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading mod options");
        // Add mod badge and settings here if needed
        logger.info("Done loading mod options");
    }

    public static void setModID(String ID) {
        modID = ID;
        logger.info("Success! ID is " + modID);
    }

    public static String getModID() {
        return modID;
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    private Settings.GameLanguage languageSupport() {
        switch (Settings.language) {
            case ZHS:
                return Settings.language;
            default:
                return Settings.GameLanguage.ENG;
        }
    }
}
