package com.sunrisexdxd.sts2relics.ui.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.sunrisexdxd.sts2relics.STS2Relics;
import com.sunrisexdxd.sts2relics.vfx.campfire.CampfireCookingEffect;

public class CookingOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public CookingOption(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.description = TEXT[1];
        this.img = ImageMaster.loadImage(STS2Relics.makeRelicPath("ui/campfire/cook.png"));
    }

    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireCookingEffect());
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Cooking Option");
        TEXT = uiStrings.TEXT;
    }
}
