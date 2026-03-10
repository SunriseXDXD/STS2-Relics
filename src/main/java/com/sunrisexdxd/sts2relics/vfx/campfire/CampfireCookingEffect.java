package com.sunrisexdxd.sts2relics.vfx.campfire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.sunrisexdxd.sts2relics.relics.CampfireCookingRelic;

import java.util.ArrayList;

public class CampfireCookingEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float DUR = 2.0F;
    private boolean openedScreen = false;
    private boolean cardsSelected = false;
    private Color screenColor;

    public CampfireCookingEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = DUR;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }

        // Handle card selection completion
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && 
            AbstractDungeon.gridSelectScreen.selectedCards.size() == 2 && !cardsSelected) {
            
            ArrayList<AbstractCard> cardsToRemove = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            
            // Remove selected cards from deck with effects
            for (AbstractCard selectedCard : cardsToRemove) {
                // Find and remove the actual card from master deck
                for (AbstractCard deckCard : AbstractDungeon.player.masterDeck.group) {
                    if (deckCard.cardID.equals(selectedCard.cardID)) {
                        CardCrawlGame.metricData.addCampfireChoiceData("COOK", deckCard.getMetricID());
                        CardCrawlGame.sound.play("CARD_EXHAUST");
                        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(deckCard, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
                        AbstractDungeon.player.masterDeck.removeCard(deckCard);
                        break;
                    }
                }
            }
            
            // Add 9 max HP
            AbstractDungeon.player.increaseMaxHp(9, true);
            
            // Log the action
            StringBuilder message = new StringBuilder(CardCrawlGame.languagePack.getRelicStrings(CampfireCookingRelic.ID).DESCRIPTIONS[4]);
            for (int i = 0; i < cardsToRemove.size(); i++) {
                if (i > 0) message.append(", ");
                message.append(cardsToRemove.get(i).name);
            }
            message.append(CardCrawlGame.languagePack.getRelicStrings(CampfireCookingRelic.ID).DESCRIPTIONS[5]);
            //AbstractDungeon.getCurrRoom().campfireUI.speechText = message.toString();
            
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            cardsSelected = true;
        }

        // Open card selection screen
        if (this.duration < 1.0F && !this.openedScreen) {
            this.openedScreen = true;
            CardGroup selectionGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards());
            if (selectionGroup.size() >= 2) {
                AbstractDungeon.gridSelectScreen.open(selectionGroup, 2, 
                    CardCrawlGame.languagePack.getRelicStrings(CampfireCookingRelic.ID).DESCRIPTIONS[3], 
                    false, false, true, false);
            } else {
                // Not enough cards, skip to completion
                cardsSelected = true;
            }
        }

        // Complete the effect
        if (this.duration < 0.0F) {
            this.isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0F;
                AbstractDungeon.getCurrRoom().phase = RoomPhase.COMPLETE;
                ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
            }
        }
    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / DUR);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        if (AbstractDungeon.screen == CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    public void dispose() {
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("CampfireCookingEffect");
        TEXT = uiStrings.TEXT;
    }
}
