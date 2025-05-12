package com.mecsbalint.backend.initialization;

import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.Publisher;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.DeveloperRepository;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.PublisherRepository;
import com.mecsbalint.backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final TagRepository tagRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public DatabaseSeeder(GameRepository gameRepository, TagRepository tagRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.tagRepository = tagRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
    }



    @Override
    public void run(String... args) {
        // Tags
        Tag tag1 = new Tag();
        tag1.setName("Rogue-lite");
        tag1.setGames(new HashSet<>());

        Tag tag2 = new Tag();
        tag2.setName("Metroidvania");
        tag2.setGames(new HashSet<>());

        Tag tag3 = new Tag();
        tag3.setName("Action");
        tag3.setGames(new HashSet<>());

        Tag tag4 = new Tag();
        tag4.setName("Indie");
        tag4.setGames(new HashSet<>());

        Tag tag5 = new Tag();
        tag5.setName("Simulation");
        tag5.setGames(new HashSet<>());

        Tag tag6 = new Tag();
        tag6.setName("Survival");
        tag6.setGames(new HashSet<>());

        Tag tag7 = new Tag();
        tag7.setName("RPG");
        tag7.setGames(new HashSet<>());

        Tag tag8 = new Tag();
        tag8.setName("Strategy");
        tag8.setGames(new HashSet<>());

        Tag tag9 = new Tag();
        tag9.setName("Adventure");
        tag9.setGames(new HashSet<>());

        Tag tag10 = new Tag();
        tag10.setName("Sci-Fi");
        tag10.setGames(new HashSet<>());

        Tag tag11 = new Tag();
        tag11.setName("Fantasy");
        tag11.setGames(new HashSet<>());

        Tag tag12 = new Tag();
        tag12.setName("First-Person");
        tag12.setGames(new HashSet<>());

        // Developers
        Developer dev1 = new Developer();
        dev1.setName("Motion Twin");
        dev1.setGames(new HashSet<>());

        Developer dev2 = new Developer();
        dev2.setName("Klei Entertainment");
        dev2.setGames(new HashSet<>());

        Developer dev3 = new Developer();
        dev3.setName("Obsidian Entertainment");
        dev3.setGames(new HashSet<>());

        Developer dev4 = new Developer();
        dev4.setName("Valve");
        dev4.setGames(new HashSet<>());

        Developer dev5 = new Developer();
        dev5.setName("Remedy Entertainment");
        dev5.setGames(new HashSet<>());

        Developer dev6 = new Developer();
        dev6.setName("Forgotten Empires");
        dev6.setGames(new HashSet<>());

        Developer dev7 = new Developer();
        dev7.setName("Tantalus Media");
        dev7.setGames(new HashSet<>());

        Developer dev8 = new Developer();
        dev8.setName("Wicked Witch");
        dev8.setGames(new HashSet<>());

        // Publishers
        Publisher pub1 = new Publisher();
        pub1.setName("Motion Twin");
        pub1.setGames(new HashSet<>());

        Publisher pub2 = new Publisher();
        pub2.setName("Klei Entertainment");
        pub2.setGames(new HashSet<>());

        Publisher pub3 = new Publisher();
        pub3.setName("Paradox Interactive");
        pub3.setGames(new HashSet<>());

        Publisher pub4 = new Publisher();
        pub4.setName("Xbox Game Studios");
        pub4.setGames(new HashSet<>());

        Publisher pub5 = new Publisher();
        pub5.setName("Valve");
        pub5.setGames(new HashSet<>());

        Publisher pub6 = new Publisher();
        pub6.setName("Remedy Entertainment");
        pub6.setGames(new HashSet<>());

        // Game: Dead Cells
        Game game1 = new Game();
        game1.setName("Dead Cells");
        game1.setReleaseDate(LocalDate.of(2018, 8, 6));
        game1.setDescriptionShort("Roguelite, Castlevania-inspired action-platformer.");
        game1.setDescriptionLong("Dead Cells is a rogue-lite, metroidvania inspired, action-platformer. You'll explore a sprawling, ever-changing castle... assuming you’re able to fight your way past its keepers in 2D souls-lite combat.");
        game1.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/588650/header.jpg");
        game1.setTags(Set.of(tag1, tag2, tag3, tag4));
        game1.setDevelopers(Set.of(dev1));
        game1.setPublishers(Set.of(pub1));
        game1.setScreenshots(Set.of(
                "https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/588650/ss_ac28000ade40cc2fe5c128f32ac98ba33c008a7a.600x338.jpg",
                "https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/588650/ss_b7cf2ca21fe3648c53f808b80393cc727815dcc5.600x338.jpg",
                "https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/588650/ss_ffef382d2a1ace63b82184caf3f6c5b293c66483.600x338.jpg"
        ));

        // Game: Oxygen Not Included
        Game game2 = new Game();
        game2.setName("Oxygen Not Included");
        game2.setReleaseDate(LocalDate.of(2019, 7, 30));
        game2.setDescriptionShort("Space-colony simulation game.");
        game2.setDescriptionLong("Oxygen Not Included is a space-colony simulation game. Deep inside an alien space rock your industrious crew will need to master science, overcome strange new lifeforms, and harness incredible space tech to survive, and possibly, thrive.");
        game2.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/457140/header.jpg");
        game2.setTags(Set.of(tag5, tag6, tag3, tag4));
        game2.setDevelopers(Set.of(dev2));
        game2.setPublishers(Set.of(pub2));
        game2.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/457140/ss_78d1c92edeecc7b17cafa9248867fe7d4390a0a0.1920x1080.jpg?t=1741894890",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/457140/ss_67ab224dd8c781d5a27ee52657173298873d439a.1920x1080.jpg?t=1741894890",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/457140/ss_ba2b8c362327add29b08182d13b04bf502e065cd.1920x1080.jpg?t=1741894890"
        ));

        // Game: Grounded
        Game game3 = new Game();
        game3.setName("Grounded");
        game3.setReleaseDate(LocalDate.of(2022, 9, 27));
        game3.setDescriptionShort("Survive the perils of the backyard.");
        game3.setDescriptionLong("The world is a vast, beautiful and dangerous place – especially when you have been shrunk to the size of an ant. Can you thrive alongside the hordes of giant insects, fighting to survive the perils of the backyard?");
        game3.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/962130/header.jpg");
        game3.setTags(Set.of(tag6, tag3, tag9, tag4));
        game3.setDevelopers(Set.of(dev3));
        game3.setPublishers(Set.of(pub4));
        game3.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/962130/ss_91a03b79d881f37cc7d39b5baf5bb597d344fabe.1920x1080.jpg?t=1727719725",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/962130/ss_432b22f117321d942d5bbb4ee49d2acc37b4baf2.1920x1080.jpg?t=1727719725",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/962130/ss_298eedf3441e631910ca29d274da0a54f56632b8.1920x1080.jpg?t=1727719725"
        ));

        // Game: Tyranny
        Game game4 = new Game();
        game4.setName("Tyranny");
        game4.setReleaseDate(LocalDate.of(2016, 11, 10));
        game4.setDescriptionShort("Story-driven RPG where your choices mean all the difference.");
        game4.setDescriptionLong("Experience a story-driven RPG where your choices mean all the difference in the world.");
        game4.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/362960/header.jpg");
        game4.setTags(Set.of(tag7, tag9, tag3, tag4));
        game4.setDevelopers(Set.of(dev3));
        game4.setPublishers(Set.of(pub3));
        game4.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/362960/ss_5cff06ac2591f7570c9d458699e932fb536850f0.1920x1080.jpg?t=1710236901",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/362960/ss_481dbe1621b369daba34c974c8c6d59f96ec353f.1920x1080.jpg?t=1710236901",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/362960/ss_2fc0a6d812eae9c6b08fc43865aa8369a621e47e.1920x1080.jpg?t=1710236901"
        ));

        // Game: Pillars of Eternity II: Deadfire
        Game game5 = new Game();
        game5.setName("Pillars of Eternity II: Deadfire");
        game5.setReleaseDate(LocalDate.of(2018, 5, 8));
        game5.setDescriptionShort("Immersive CRPG experience reminiscent of classic titles.");
        game5.setDescriptionLong("Pillars of Eternity II: Deadfire offers players an immersive computer role-playing game (CRPG) experience reminiscent of classic titles like Baldur's Gate.");
        game5.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/560130/header.jpg");
        game5.setTags(Set.of(tag7, tag9, tag3, tag11));
        game5.setDevelopers(Set.of(dev3));
        game5.setPublishers(Set.of(pub3));
        game5.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/560130/ss_a94a39544efd153a63926edb5e1f528f3dc612e6.1920x1080.jpg?t=1728662681",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/560130/ss_4f8695a72b5e2855bef51ddcd3aa29b90d223226.1920x1080.jpg?t=1728662681",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/560130/ss_2311bb56410e0ae8d8819332192c207681b977ba.1920x1080.jpg?t=1728662681"
        ));

        // Game: Age of Empires II: Definitive Edition
        Game game6 = new Game();
        game6.setName("Age of Empires II: Definitive Edition");
        game6.setReleaseDate(LocalDate.of(2019, 11, 14));
        game6.setDescriptionShort("Remastered classic real-time strategy game.");
        game6.setDescriptionLong("Age of Empires II: Definitive Edition is a remaster of the classic real-time strategy game Age of Empires II: Age of Kings.");
        game6.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/813780/header.jpg");
        game6.setTags(Set.of(tag8, tag3, tag9, tag4));
        game6.setDevelopers(Set.of(dev6, dev7, dev8));
        game6.setPublishers(Set.of(pub4));
        game6.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/813780/ss_77792f8c4a7b375fc7c749b6336e61d61830920b.1920x1080.jpg?t=1744747990",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/813780/ss_8f30a6ae45244f12ebdb48f99393aa9c93aaa398.1920x1080.jpg?t=1744747990",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/813780/ss_b42dec99b197fea9882abc333e2cfb0a77dd3ed2.1920x1080.jpg?t=1744747990"
        ));

        // Game: Avowed
        Game game7 = new Game();
        game7.setName("Avowed");
        game7.setReleaseDate(LocalDate.of(2024, 12, 31)); // Placeholder date
        game7.setDescriptionShort("Epic first-person fantasy RPG from Obsidian.");
        game7.setDescriptionLong("Avowed is an epic first-person fantasy RPG set in the world of Eora. Engage in deep narrative and dynamic combat as you shape the fate of the land.");
        game7.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/2457220/header.jpg");
        game7.setTags(Set.of(tag7, tag11, tag12, tag9));
        game7.setDevelopers(Set.of(dev3));
        game7.setPublishers(Set.of(pub4));
        game7.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2457220/ss_9c4f08a95ae00d289a27b51c168f91b76245e32c.1920x1080.jpg?t=1744647242",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2457220/ss_8a539cd66de0dd8f44c1f5a884fe305e97f1568f.1920x1080.jpg?t=1744647242",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2457220/ss_c96ac97cfe6f0c9a651ebc96078ce4d77de3e1c3.1920x1080.jpg?t=1744647242"
        ));

        // Game: Control Ultimate Edition
        Game game8 = new Game();
        game8.setName("Control Ultimate Edition");
        game8.setReleaseDate(LocalDate.of(2020, 8, 27));
        game8.setDescriptionShort("Supernatural third-person action-adventure.");
        game8.setDescriptionLong("Control is a supernatural third-person action-adventure that challenges you to master your abilities and uncover the secrets of the Federal Bureau of Control.");
        game8.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/870780/header.jpg");
        game8.setTags(Set.of(tag3, tag9, tag10, tag12));
        game8.setDevelopers(Set.of(dev5)); // Add developer if available
        game8.setPublishers(Set.of(pub6)); // Add publisher if needed
        game8.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/870780/ss_8376498631b089e52fb5c75ffe119e0de5e6aed1.1920x1080.jpg?t=1743425935",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/870780/ss_5a16ce565951479e142c56a23f19d88333d84945.1920x1080.jpg?t=1743425935",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/870780/ss_c038bb7b20d72ba5d33cc95f7235aefa0b84a706.1920x1080.jpg?t=1743425935"
        ));

        // Game: Half-Life 2
        Game game9 = new Game();
        game9.setName("Half-Life 2");
        game9.setReleaseDate(LocalDate.of(2004, 11, 16));
        game9.setDescriptionShort("Iconic first-person sci-fi shooter.");
        game9.setDescriptionLong("Half-Life 2 is the critically acclaimed sequel to Half-Life, combining action, adventure, and physics-driven gameplay in a dystopian world.");
        game9.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/220/header.jpg");
        game9.setTags(Set.of(tag3, tag10, tag12, tag9));
        game9.setDevelopers(Set.of(dev4));
        game9.setPublishers(Set.of(pub5));
        game9.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/ss_47b4105b396de408cb8b6b4f358c69e5e2a62dae.1920x1080.jpg?t=1745368545",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/ss_0e499071a60a20b24149ad65a8edb769250f2921.1920x1080.jpg?t=1745368545",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/ss_ffb00abd45012680e4f209355ec81f961b6dd1fb.1920x1080.jpg?t=1745368545"
        ));

        // Game: Half-Life
        Game game10 = new Game();
        game10.setName("Half-Life");
        game10.setReleaseDate(LocalDate.of(1998, 11, 8));
        game10.setDescriptionShort("The genre-defining sci-fi FPS.");
        game10.setDescriptionLong("Half-Life blends action and storytelling in a seamless experience. Step into the shoes of Gordon Freeman and face an alien threat after a science experiment goes wrong.");
        game10.setHeaderImg("https://cdn.akamai.steamstatic.com/steam/apps/70/header.jpg");
        game10.setTags(Set.of(tag3, tag10, tag12, tag9));
        game10.setDevelopers(Set.of(dev4));
        game10.setPublishers(Set.of(pub5));
        game10.setScreenshots(Set.of(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/70/0000002354.1920x1080.jpg?t=1745368462",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/70/0000002343.1920x1080.jpg?t=1745368462",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/70/0000002342.1920x1080.jpg?t=1745368462"
        ));

        tagRepository.saveAll(Set.of(tag1, tag2, tag3, tag4, tag5, tag6, tag7, tag8, tag9, tag10, tag11, tag12));
        developerRepository.saveAll(Set.of(dev1, dev2, dev3, dev4, dev5, dev6, dev7, dev8));
        publisherRepository.saveAll(Set.of(pub1, pub2, pub3, pub4, pub5, pub6));
        gameRepository.saveAll(Set.of(game1, game2, game3, game4, game5, game6, game7, game8, game9, game10));
    }
}
