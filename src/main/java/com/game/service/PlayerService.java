package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.IncomingRequestValidateException;
import com.game.exception.NotFoundException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {

    private static final String NAME = "name";
    private static final String TITLE = "title";
    private static final String RACE = "race";
    private static final String PROFESSION = "profession";
    private static final String BIRTHDAY = "birthday";
    private static final String EXPERIENCE = "experience";
    private static final String BANNED = "banned";

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayersList(String name,
                                       String title,
                                       Race race,
                                       Profession profession,
                                       Long after,
                                       Long before,
                                       Boolean banned,
                                       Integer minExperience,
                                       Integer maxExperience,
                                       Integer minLevel,
                                       Integer maxLevel,
                                       PlayerOrder order,
                                       Integer pageNumber,
                                       Integer pageSize) {

        Date dateAfter = (after != null ? new Date(after) : null);
        Date dateBefore = (before != null ? new Date(before) : null);

        if (order == null) {
            order = PlayerOrder.ID;
        }

        Pageable pageInfo = PageRequest.of(
                (pageNumber != null ? pageNumber : 0),
                (pageSize != null ? pageSize : 3),
                Sort.by(order.getFieldName())
        );

        return playerRepository.findAllByParams(
                name,
                title,
                race,
                profession,
                dateAfter,
                dateBefore,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel,
                pageInfo
        );
    }

    public Integer countPlayers(String name,
                                String title,
                                Race race,
                                Profession profession,
                                Long after,
                                Long before,
                                Boolean banned,
                                Integer minExperience,
                                Integer maxExperience,
                                Integer minLevel,
                                Integer maxLevel) {

        Date dateAfter = (after != null ? new Date(after) : null);
        Date dateBefore = (before != null ? new Date(before) : null);

        return playerRepository.countAllByParams(
                name,
                title,
                race,
                profession,
                dateAfter,
                dateBefore,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel
        );
    }

    public Player createPlayer(Map<String, String> createPlayerData) {
        Player newPlayer = new Player();

        if (!createPlayerData.containsKey(NAME)
                || !createPlayerData.containsKey(TITLE)
                || !createPlayerData.containsKey(RACE)
                || !createPlayerData.containsKey(PROFESSION)
                || !createPlayerData.containsKey(BIRTHDAY)
                || !createPlayerData.containsKey(EXPERIENCE)) {
            throw new IncomingRequestValidateException();
        }

        fillPlayerDataFromRequest(newPlayer, createPlayerData);
        updateLevelAndUntilNextLevel(newPlayer);

        return playerRepository.saveAndFlush(newPlayer);
    }

    public Player getPlayer(String stringId) {
        long id = getLongIdFromStringId(stringId);

        return playerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Player updatePlayer(String stringId, Map<String, String> updatePlayerData) {
        long id = getLongIdFromStringId(stringId);

        Player playerToUpdate = playerRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        fillPlayerDataFromRequest(playerToUpdate, updatePlayerData);
        updateLevelAndUntilNextLevel(playerToUpdate);

        return playerRepository.saveAndFlush(playerToUpdate);
    }

    public void deletePlayer(String stringId) {
        long id = getLongIdFromStringId(stringId);
        if (!playerRepository.existsById(id)) {
            throw new NotFoundException();
        }

        playerRepository.deleteById(id);
    }


    //region Private helper methods

    private void fillPlayerDataFromRequest(Player player, Map<String, String> request) {
        try {
            if (request.containsKey(NAME)) {
                String name = request.get(NAME);
                if (name.length() > 0
                        && name.length() <= 12) {
                    player.setName(name);
                } else {
                    throw new IncomingRequestValidateException();
                }
            }

            if (request.containsKey(TITLE)) {
                String title = request.get(TITLE);
                if (title.length() <= 30) {
                    player.setTitle(title);
                } else {
                    throw new IncomingRequestValidateException();
                }
            }

            if (request.containsKey(RACE)) {
                Race race = Race.valueOf(request.get(RACE));
                player.setRace(race);
            }

            if (request.containsKey(PROFESSION)) {
                Profession profession = Profession.valueOf(request.get(PROFESSION));
                player.setProfession(profession);
            }

            if (request.containsKey(BIRTHDAY)) {
                long birthdayLong = Long.parseLong(request.get(BIRTHDAY));
                if (birthdayLong < 946663200000L ||
                        birthdayLong > 32535104400000L) {
                    throw new IncomingRequestValidateException();
                }
                Date birthday = new Date(birthdayLong);
                player.setBirthday(birthday);
            }

            if (request.containsKey(NAME)) {
                int experienceInteger = Integer.parseInt(request.get(EXPERIENCE));
                if (experienceInteger >= 0
                        && experienceInteger <= 10000000) {
                    player.setExperience(experienceInteger);
                } else {
                    throw new IncomingRequestValidateException();
                }
            }

            if (request.containsKey(NAME)) {
                player.setBanned(Boolean.parseBoolean(request.get(BANNED)));
            }
        } catch (Exception e) {
            throw new IncomingRequestValidateException();
        }
    }

    private long getLongIdFromStringId(String stringId) {
        try {
            long id = Long.parseLong(stringId);
            if (id <= 0) {
                throw new NumberFormatException("wrong id");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IncomingRequestValidateException(String.format("Incorrect id - %s", stringId));

        }
    }

    private void updateLevelAndUntilNextLevel(Player player) {
        int curExperience = player.getExperience();

        int level = (((int) Math.sqrt(2500 + 200d * curExperience)) - 50) / 100;
        player.setLevel(level);

        int untilNextLevel = 50 * (level + 1) * (level + 2) - curExperience;
        player.setUntilNextLevel(untilNextLevel);
    }

    //endregion

}
