package com.sunrisexdxd.sts2relics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.sunrisexdxd.sts2relics.ui.campfire.CookingOption;
import com.sunrisexdxd.sts2relics.STS2Relics;

import java.util.ArrayList;

public class CampfireCookingRelic extends CustomRelic {
    public static final String ID = "STS2Relics:CampfireCooking";
    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    private static final Texture IMG_PATH = ImageMaster.loadImage(STS2Relics.makeRelicPath("CampfireCooking.png"));
    private static final Texture OUTLINE_PATH = ImageMaster.loadImage(STS2Relics.makeRelicOutlinePath("CampfireCooking.png"));

    public CampfireCookingRelic() {
        super(ID, IMG_PATH, OUTLINE_PATH, RelicTier.UNCOMMON, LandingSound.SOLID);
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean canSpawn() {
        // Check if we're too late in the run (similar to PeacePipe)
        if (AbstractDungeon.floorNum >= 48 && !Settings.isEndless) {
            return false;
        }
        
        // Check for other campfire relics to prevent too many
        int campfireRelicCount = 0;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof CampfireCookingRelic || r instanceof com.megacrit.cardcrawl.relics.PeacePipe || 
                r instanceof com.megacrit.cardcrawl.relics.Shovel || r instanceof com.megacrit.cardcrawl.relics.Girya) {
                ++campfireRelicCount;
            }
        }
        
        return campfireRelicCount < 2;
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        // Check if player has enough cards to cook
        boolean hasEnoughCards = AbstractDungeon.player.masterDeck.size() >= 2;
        options.add(new CookingOption(hasEnoughCards));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CampfireCookingRelic();
    }
}
