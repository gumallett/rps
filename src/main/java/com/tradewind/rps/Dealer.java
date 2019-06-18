package com.tradewind.rps;

import com.tradewind.rps.service.CasinoServiceImpl;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dealer class to manage active games and players
 */
@Service
public class Dealer implements IDealer, Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(Dealer.class);

    private Subject<RpsGame> activeGames = ReplaySubject.<RpsGame>create().toSerialized();
    private Subject<EventInfo> eventSubject = BehaviorSubject.<EventInfo>create().toSerialized();
    private Subject<Player> playersSubject = ReplaySubject.<Player>create().toSerialized();

    private Map<UUID, Player> playerMap = new ConcurrentHashMap<>();

    private Disposable activeGamesSubscription;
    private Disposable newGamesSubscription;

    private volatile boolean isDisposed = false;

    private final CasinoServiceImpl casinoService;

    public Dealer(@Autowired CasinoServiceImpl casinoService) {
        this.casinoService = casinoService;
        newGamesSubscription = this.newGameEvents()
                .map(RpsGame::new)
                .subscribe(rpsGame -> this.activeGames.onNext(rpsGame));
        activeGamesSubscription = this.activeGames()
                .filter(RpsGame::isFull)
                .subscribe(this::playGame, error -> {
                    LOG.error("Error: {}", error);
                });
    }

    @Override
    public void newEvent(EventInfo info) {
        this.eventSubject.onNext(info);
    }

    @Override
    public void newPlayer(PlayerInfo playerInfo) {
        Player player = new Player(playerInfo, new RPSStrategy());
        playerMap.putIfAbsent(playerInfo.getPlayerInfoId(), player);
        this.playersSubject.onNext(player);
    }

    @Override
    public Collection<Player> getCurrentPlayers() {
        return Collections.unmodifiableCollection(this.playerMap.values());
    }

    /**
     * Returns an Observable that emits an rps game when a player joins that game. The player
     * will be registered with the game instance.
     */
    private Observable<RpsGame> activeGames() {
        return this.joinGameEvents()
            .flatMap(eventInfo ->
                this.playersSubject.filter(player ->
                        player.getBalance() >= RpsGame.GAME_COST && player.getPlayerId().equals(eventInfo.getPlayerInfoID())
                ).flatMap(player -> this.mapPlayerToGame(player, eventInfo))

        );
    }

    /**
     * Returns an Observable that emits ESTABLISH_NEW_GAME events only
     */
    private Observable<EventInfo> newGameEvents() {
        return this.eventSubject.filter(event -> event.getEventType() == EventType.ESTABLISH_NEW_GAME);
    }

    /**
     * Returns an Observable that emits JOIN_GAME events only
     */
    private Observable<EventInfo> joinGameEvents() {
        return this.eventSubject.filter(event -> event.getEventType() == EventType.JOIN_GAME);
    }

    /**
     * Returns an Observable that emits when a player can be matched with an open game
     * @param player The player to match
     * @param joinGameEventInfo Event Info for the game the player wants to join
     */
    private Observable<RpsGame> mapPlayerToGame(Player player, EventInfo joinGameEventInfo) {
        return this.activeGames
                .filter(rpsGame ->
                        rpsGame.getEventInfo().getEventID().equals(joinGameEventInfo.getEstablishNewGameEventId())
                ).map(rpsGame -> {
                        rpsGame.addPlayer(player);
                        return rpsGame;
                });
    }

    /**
     * Plays a rpsgame to completion
     * @param rpsGame RpsGame to play out
     */
    private void playGame(RpsGame rpsGame) {
        Player player1 = rpsGame.getPlayer(0);
        Player player2 = rpsGame.getPlayer(1);

        RPSMove player1Move = player1.getStrategy().playGame(rpsGame.getGameId());
        RPSMove player2Move = player2.getStrategy().playGame(rpsGame.getGameId());

        if (player1Move == player2Move) {
            // tie
            this.casinoService.updateBalance(RpsGame.GAME_COST * 2);
        } else if (player1Move == RPSMove.ROCK) {
            if (player2Move == RPSMove.PAPER) {
                // loss for player1
                player2.updateBalance(RpsGame.GAME_COST * 2);
            } else {
                // win for player1
                player1.updateBalance(RpsGame.GAME_COST * 2);
            }
        } else if (player1Move == RPSMove.PAPER) {
            if (player2Move == RPSMove.SCISSORS) {
                // loss for player1
                player2.updateBalance(RpsGame.GAME_COST * 2);
            } else {
                // win for player1
                player1.updateBalance(RpsGame.GAME_COST * 2);
            }
        } else if (player1Move == RPSMove.SCISSORS) {
            if (player2Move == RPSMove.ROCK) {
                // loss for player1
                player2.updateBalance(RpsGame.GAME_COST * 2);
            } else {
                // win for player1
                player1.updateBalance(RpsGame.GAME_COST * 2);
            }
        }
    }

    @Override
    public void dispose() {
        this.activeGamesSubscription.dispose();
        this.newGamesSubscription.dispose();
        this.isDisposed = true;
    }

    @Override
    public boolean isDisposed() {
        return this.isDisposed;
    }
}
