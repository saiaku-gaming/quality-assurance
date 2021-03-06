package com.valhallagame.qa.service

import com.valhallagame.qa.dao.*
import org.springframework.stereotype.Service

@Service
class ReplayService(private val replayDao: ReplayDao) {
    fun getReplay(replayId: Long): Replay {
        return replayDao.getReplay(replayId) ?: throw RuntimeException("Missing replay")
    }

    fun addReplay(replay: Replay) {
        val replayId = replayDao.insertReplay(replay.mapName)
        replay.dungeonRuns.forEach { addDungeonRun(replayId, it) }
    }

    fun getReplaysFromMapName(mapName: String): List<Replay> {
        return replayDao.getReplaysFromMapName(mapName);
    }

    private fun addDungeonRun(replayId: Long, dungeonRun: DungeonRun) {
        val dungeonRunId = replayDao.insertDungeonRun(replayId)
        dungeonRun.players.forEach { addReplayPlayer(dungeonRunId, it) }
    }

    private fun addReplayPlayer(dungeonRunId: Long, replayPlayer: ReplayPlayer) {
        val replayPlayerId = replayDao.insertReplayPlayer(dungeonRunId, replayPlayer.name)
        replayPlayer.actions.forEach { addReplayAction(replayPlayerId, it) }
    }

    private fun addReplayAction(replayPlayerId: Long, replayAction: ReplayAction) {
        replayDao.insertReplayAction(replayPlayerId, replayAction.action.name,
                replayAction.location.x, replayAction.location.y, replayAction.location.z)
    }
}