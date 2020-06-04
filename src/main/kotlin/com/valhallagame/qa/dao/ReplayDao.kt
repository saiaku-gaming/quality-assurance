package com.valhallagame.qa.dao

import org.jdbi.v3.core.result.LinkedHashMapRowReducer
import org.jdbi.v3.core.result.RowView
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.statement.UseRowReducer

interface ReplayDao {
    @SqlUpdate("""
        INSERT INTO replay (map_name) VALUES (:mapName)
    """)
    @GetGeneratedKeys
    fun insertReplay(@Bind("mapName") mapName: String): Long

    @SqlUpdate("""
        INSERT INTO dungeon_run (replay_id) VALUES (:replayId)
    """)
    @GetGeneratedKeys
    fun insertDungeonRun(@Bind("replayId") replayId: Long): Long

    @SqlUpdate("""
        INSERT INTO replay_player (dungeon_run_id, name) VALUES (:dungeonRunId, :name)
    """)
    @GetGeneratedKeys
    fun insertReplayPlayer(@Bind("dungeonRunId") dungeonRunId: Long, @Bind("name") name: String): Long

    @SqlUpdate("""
        INSERT INTO replay_action (replay_player_id, action, pos_x, pos_y, pos_z) VALUES (:replayPlayerId, :action, :posX, :posY, :posZ)
    """)
    fun insertReplayAction(
            @Bind("replayPlayerId") replayPlayerId: Long, @Bind("action") action: String,
            @Bind("posX") posX: Float, @Bind("posY") posY: Float,
            @Bind("posZ") posZ: Float
    )

    @SqlQuery("""
        SELECT
            r.replay_id, r.map_name,
            dr.dungeon_run_id,
            rp.replay_player_id, rp.name,
            ra.replay_action_id, ra.action, ra.pos_x, ra.pos_y, ra.pos_z
        FROM replay r
            JOIN dungeon_run dr ON(r.replay_id = dr.replay_id)
            JOIN replay_player rp ON(dr.dungeon_run_id = rp.dungeon_run_id)
            JOIN replay_action ra ON(rp.replay_player_id = ra.replay_player_id)
        WHERE r.replay_id = :replayId
    """)
    @UseRowReducer(ReplayReducer::class)
    fun getReplay(@Bind("replayId") replayId: Long): Replay?

    @SqlQuery("""
        SELECT
            r.replay_id, r.map_name,
            dr.dungeon_run_id,
            rp.replay_player_id, rp.name,
            ra.replay_action_id, ra.action, ra.pos_x, ra.pos_y, ra.pos_z
        FROM replay r
            JOIN dungeon_run dr ON(r.replay_id = dr.replay_id)
            JOIN replay_player rp ON(dr.dungeon_run_id = rp.dungeon_run_id)
            JOIN replay_action ra ON(rp.replay_player_id = ra.replay_player_id)
        WHERE r.map_name = :mapName
    """)
    @UseRowReducer(ReplayReducer::class)
    fun getReplaysFromMapName(@Bind("mapName") mapName: String): List<Replay>
}

class ReplayReducer : LinkedHashMapRowReducer<Long, Replay> {
    override fun accumulate(map: MutableMap<Long, Replay>, rowView: RowView) {
        val replayId = rowView.getColumn("replay_id", java.lang.Long::class.java).toLong()

        var replay = map.computeIfAbsent(replayId) {
            Replay(
                    rowView.getColumn("replay_id", java.lang.Long::class.java).toLong(),
                    rowView.getColumn("map_name", String::class.java)
            )
        }

        var dungeonRun = replay.dungeonRuns.find { it.id == rowView.getColumn("dungeon_run_id", java.lang.Long::class.java).toLong() }
                ?: DungeonRun(
                        rowView.getColumn("dungeon_run_id", java.lang.Long::class.java).toLong()
                ).also {
                    replay = replay.copy(dungeonRuns = replay.dungeonRuns + it)
                }
        val dungeonRunIndex = replay.dungeonRuns.indexOf(dungeonRun)

        val replayPlayer = dungeonRun.players.find { it.id == rowView.getColumn("replay_player_id", java.lang.Long::class.java).toLong() }
                ?: ReplayPlayer(
                        rowView.getColumn("replay_player_id", java.lang.Long::class.java).toLong(),
                        rowView.getColumn("name", String::class.java)
                ).also {
                    replay = replay.copy(dungeonRuns = replay.dungeonRuns.toMutableList().apply {
                        this[dungeonRunIndex] = dungeonRun.copy(players = dungeonRun.players + it)
                        dungeonRun = this[dungeonRunIndex]
                    }.toList())
                }
        val replayPlayerIndex = dungeonRun.players.indexOf(replayPlayer)

        val replayAction = ReplayAction(
                rowView.getColumn("replay_action_id", java.lang.Long::class.java).toLong(),
                Action.valueOf(rowView.getColumn("action", String::class.java)),
                Vector(
                    rowView.getColumn("pos_x", java.lang.Float::class.java).toFloat(),
                    rowView.getColumn("pos_y", java.lang.Float::class.java).toFloat(),
                    rowView.getColumn("pos_z", java.lang.Float::class.java).toFloat()
                )
        )

        map[replayId] = replay.copy(dungeonRuns = replay.dungeonRuns.toMutableList().apply {
            this[dungeonRunIndex] = dungeonRun.copy(players = dungeonRun.players.toMutableList().apply {
                this[replayPlayerIndex] = replayPlayer.copy(actions = replayPlayer.actions + replayAction)
            }.toList())
        }.toList())
    }
}

data class Replay (
        val id: Long = -1,
        val mapName: String = "",
        val dungeonRuns: List<DungeonRun> = listOf()
)

data class DungeonRun (
        val id: Long = -1,
        val players: List<ReplayPlayer> = listOf()
)

data class ReplayPlayer (
        val id: Long = -1,
        val name: String = "",
        val actions: List<ReplayAction> = listOf()
)

data class ReplayAction (
        val id: Long = -1,
        val action: Action = Action.MOVE,
        val location: Vector = Vector()
)

data class Vector (
        val x: Float = 0.0f,
        val y: Float = 0.0f,
        val z: Float = 0.0f
)

enum class Action {
    MOVE, DEATH
}
