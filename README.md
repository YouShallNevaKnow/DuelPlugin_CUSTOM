# DuelPlugin_CUSTOM
***A custom version of my other duel plugin.***

name: DuelCustom

version: 1.1.0_CUSTOM

main: com.gmail.gogobebe2.duel.Duel

description: A pvp duel plugin for 1v1 fights.

    commands:
        duel:
            usage: /duel <player>, /duel stats or /duel accept
            description: Duel a player in a 1v1 fight.
            permissions:
                duel.*:
                     description: Gives access to /duel
                     default: op
        leaveduel:
            usage: /duelleave if in a duel.
            description: Leave the duel.
