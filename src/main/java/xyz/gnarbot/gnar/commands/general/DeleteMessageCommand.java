package xyz.gnarbot.gnar.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Command(aliases = "deletemsg", usage = "(messageid)", description = "Delete those messages..")
public class DeleteMessageCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (!message.getAuthor().hasPermission(Permission.MESSAGE_MANAGE)){
            message.replyEmbedRaw("No Permission", "You do not have the MESSAGE_MANAGE permission!", Color.RED);
            return;
        }
        if (args.length < 1)
        {
            message.reply("**" + BotData.randomQuote() + "** Well that sounds fantastic! I'll just delete th-... You " +
                    "didn't give me anything to work with. *(I need the message ID)*");
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Deleting messages");
        eb.setColor(Bot.getColor());
        if (args.length == 1)
        {
            String msgid = args[0];
            final Note msg = message.reply("**" + BotData.randomQuote() + "** Searching for Message with ID `" +
                    msgid + "`.");
            try
            {
                Message m = msg.getChannel().getMessageById(msgid).complete(true);
                try {
                    m.deleteMessage().queue();
                    eb.setDescription("Deleted message " + msgid);
                }catch (Exception e){
                    eb.setDescription("Couldn't delete message " + msgid);
                }
            }
            catch (Exception e)
            {
                msg.editMessage("**" + BotData.randomQuote() + "** Oops. I couldn't find that message within this " +
                        "channel. You sure you got the right place?").queue();
                Bot.INSTANCE.getScheduler().schedule(() -> msg.deleteMessage().queue(), 10, TimeUnit.SECONDS);
            }

            MessageEmbed embed = eb.build();
            MessageBuilder mb = new MessageBuilder();
            mb.setEmbed(embed);
            Message m = mb.build();
            msg.getChannel().sendMessage(m).queue();
        }
        else
        {
            String combined = StringUtils.join(args, ", ");
            final Note msg = message.reply("**" + BotData.randomQuote() + "** Searching for Messages with IDs `" +
                    combined + "`.");
            int count = 0;
            for (String msgid : args)
            {
                try
                {
                    count++;
                    Message m = msg.getChannel().getMessageById(msgid).complete(true);
                    if (count == 1)
                    {
                        try{
                            m.deleteMessage().queue();
                        }catch (Exception e){
                            eb.setDescription("Couldn't delete message " + msgid);
                        }

                    }
                    else
                    {
                        try{
                            m.deleteMessage().queue();
                        }catch (Exception e){
                            eb.addField("", "Couldn't delete message " + msgid, false);
                        }
                    }
                }
                catch (Exception e)
                {
                    if (count == 1)
                    {
                        eb.setTitle("*Couldn't find this message*").addBlankField(true).setDescription("*Couldn't " +
                                "find message with ID " + msgid + "*");
                    }
                    else
                    {
                        eb.addField("*Couldn't find this message*", "*Couldn't find message with ID " + msgid + "*",
                                false);
                    }
                }
            }
            MessageEmbed embed = eb.build();
            MessageBuilder mb = new MessageBuilder();
            mb.setEmbed(embed);
            Message embedMsg = mb.build();
            message.getChannel().sendMessage(embedMsg).queue();
            msg.deleteMessage().queue();
        }
    }
}



