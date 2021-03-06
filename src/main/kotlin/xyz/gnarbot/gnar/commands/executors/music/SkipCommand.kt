package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.commands.Scope
import xyz.gnarbot.gnar.utils.Context

@Command(
        aliases = arrayOf("skip"),
        description = "Skip the current music track.",
        category = Category.MUSIC,
        scope = Scope.VOICE
)
class SkipCommand : CommandExecutor() {
    override fun execute(context: Context, args: Array<String>) {
        val manager = context.guildData.musicManager

        if (!(context.member.hasPermission(Permission.MANAGE_CHANNEL)
                || manager.player.playingTrack.userData == context.member)) {
            context.send().error("You did not request this track.").queue()
            return
        }

        if (manager.scheduler.queue.isEmpty()) {
            context.guildData.musicManager.reset()
        } else {
            manager.scheduler.nextTrack()
        }

        context.send().embed("Skip Current Track") {
            setColor(Bot.CONFIG.musicColor)
            setDescription("The track was skipped.")
        }.action().queue()
    }
}
