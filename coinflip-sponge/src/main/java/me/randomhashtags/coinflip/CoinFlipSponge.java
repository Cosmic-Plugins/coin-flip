package me.randomhashtags.coinflip;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "coinflip",
        name = "CoinFlip",
        description = "Pure luck 50/50 betting game where you put your money where your mouth is, and winner takes all!",
        authors = {
                "RandomHashTags"
        }
)
public class CoinFlipSponge {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
