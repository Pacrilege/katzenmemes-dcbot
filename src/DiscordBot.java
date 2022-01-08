import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



/**
 * The main class where it all gets started. You want to used to this class when running the bot on discord.
 */
public class DiscordBot extends ListenerAdapter {
    //TODO Here you have to insert the Token you got from the first part
    public static final String DISCORD_TOKEN = System.getenv("DC_TOKEN");
    /**
     * Logger is a class from a library we included and can be used to write to the console in an orderly manner.
     * The Discord library JDA also uses this library.
     */
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);

    public static void main(String[] args) throws LoginException {

        /*
        The Builder is responsible for setting up the jda instance that will manage your bot
         */
        JDABuilder jdaBuilder = JDABuilder.createDefault(DISCORD_TOKEN);

        JDA build = jdaBuilder.build();

        // any class that extends ListenerAdapter can be given as an EventListener
        build.addEventListener(new DiscordBot());
    }

    /**
     * This gets executed every time a message is received
     * @param event the event that gets fired by the message
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try{
            logger.info("Received message with text: {}", event.getMessage().getContentRaw());

            // #guterStil is handling different messages in separate methods ;)
            handleMessage(event);
        }catch (Exception e){
            logger.warn("Could not process message", e);

            /*
            DO NOT forget to call .queue() if you want to send a message
            That method alone is responsible for pushing your message to the queue
            for the api to send it to the server!
            PLS DO NOT FORGET THIS
             */
            event.getMessage().getChannel().sendMessage("Unexpected error occurred!").queue();

        }
    }

    private void handleMessage(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        Scanner parseCommand = new Scanner(content);
        String cmd = parseCommand.next();
        if(cmd.equals("!lol")) { //write lol on cat image
            catFinder.imgFromUrl(catFinder.getCatImageURL());
            addText.writeOnImg("LOL", 1.2f, 1);
            event.getMessage().getChannel().sendFile(new File("img/img.png")).queue();
        } else if (cmd.equals("!caption")) { //write user input on cat image
            catFinder.imgFromUrl(catFinder.getCatImageURL());
            addText.writeOnImg(parseCommand.nextLine(), 1.2f, 1f);
            event.getMessage().getChannel().sendFile(new File("img/img.png")).queue();
        }
    }
}

/**
 * Stuck? Out of ideas? No clue what a callback is?
 * Here is the official user-friendly JDA-Wiki, where you will probably find an answer to most of your question
 * Sonst einfach Tutor anschreiben :D
 * https://github.com/DV8FromTheWorld/JDA/wiki
 */
