package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Shard
import xyz.gnarbot.gnar.guilds.GuildData

class Context(event: GuildMessageReceivedEvent) {
    val message: Message = event.message
    val channel: TextChannel = event.channel
    val guild: Guild = event.guild
    val guildData: GuildData = Bot.getGuildData(guild)
    val shard: Shard = guildData.shard

    val member: Member = event.member
    val user: User = event.author

    /**
     * Return a class with utilities to help send a message to
     * this message's channel.
     *
     * @return Response builder.
     */
    fun send() = ResponseBuilder(this)
}