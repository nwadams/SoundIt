-- Users, Consumers and Locations
INSERT INTO backend_user (id, email_address, password, salt) VALUES (1, "1234", "02b31057a8571be94458daa3fe170719", "69");
INSERT INTO backend_location (id, user_id, name, location, phone_number) VALUES (1, 1, "Restaurant 1", "Vancouver", "(604) 441-2685");


-- Music Categories
INSERT INTO backend_musiccategory (id, name) VALUES (1, "Afrobeat");
INSERT INTO backend_musiccategory (id, name) VALUES (2, "Blues");
INSERT INTO backend_musiccategory (id, name) VALUES (3, "Country");
INSERT INTO backend_musiccategory (id, name) VALUES (4, "Lounge");
INSERT INTO backend_musiccategory (id, name) VALUES (5, "Electronic");
INSERT INTO backend_musiccategory (id, name) VALUES (6, "Hip Hop");
INSERT INTO backend_musiccategory (id, name) VALUES (7, "Pop");
INSERT INTO backend_musiccategory (id, name) VALUES (8, "R&B");
INSERT INTO backend_musiccategory (id, name) VALUES (9, "Rock");
INSERT INTO backend_musiccategory (id, name) VALUES (10, "Contemporary");
INSERT INTO backend_musiccategory (id, name) VALUES (11, "Dubstep");
INSERT INTO backend_musiccategory (id, name) VALUES (12, "Jazz");
INSERT INTO backend_musiccategory (id, name) VALUES (13, "K-pop");


-- Aritsts
INSERT INTO backend_artist (id, name) VALUES (1, "Justin Bieber");
INSERT INTO backend_artist (id, name) VALUES (2, "Pink");
INSERT INTO backend_artist (id, name) VALUES (3, "Ke$ha");
INSERT INTO backend_artist (id, name) VALUES (4, "Robbie Williams");
INSERT INTO backend_artist (id, name) VALUES (5, "Kanye West");
INSERT INTO backend_artist (id, name) VALUES (6, "Maroon 5");
INSERT INTO backend_artist (id, name) VALUES (7, "Rihanna");
INSERT INTO backend_artist (id, name) VALUES (8, "Tyga");
INSERT INTO backend_artist (id, name) VALUES (9, "Swedish House Mafia");
INSERT INTO backend_artist (id, name) VALUES (10, "Pitbull");
INSERT INTO backend_artist (id, name) VALUES (11, "Calvin Harris");
INSERT INTO backend_artist (id, name) VALUES (12, "Nickelback");
INSERT INTO backend_artist (id, name) VALUES (13, "Psy");
INSERT INTO backend_artist (id, name) VALUES (14, "Alicia Keys");
INSERT INTO backend_artist (id, name) VALUES (15, "Flo Rida");
INSERT INTO backend_artist (id, name) VALUES (16, "Anne Hathaway");
INSERT INTO backend_artist (id, name) VALUES (17, "Taylor Swift");
INSERT INTO backend_artist (id, name) VALUES (18, "Bruno Mars");
INSERT INTO backend_artist (id, name) VALUES (19, "David Guetta");
INSERT INTO backend_artist (id, name) VALUES (20, "One direction");
INSERT INTO backend_artist (id, name) VALUES (21, "Flo Rida");
INSERT INTO backend_artist (id, name) VALUES (22, "Avicii");
INSERT INTO backend_artist (id, name) VALUES (23, "Bruno Mars");
INSERT INTO backend_artist (id, name) VALUES (24, "Beyonce");
INSERT INTO backend_artist (id, name) VALUES (25, "Lady Gaga");
INSERT INTO backend_artist (id, name) VALUES (26, "Maroon 5");
INSERT INTO backend_artist (id, name) VALUES (27, "50 cent");
INSERT INTO backend_artist (id, name) VALUES (28, "Coldplay");
INSERT INTO backend_artist (id, name) VALUES (29, "Skrillex");
INSERT INTO backend_artist (id, name) VALUES (30, "will.i.am");
INSERT INTO backend_artist (id, name) VALUES (31, "Adele");
INSERT INTO backend_artist (id, name) VALUES (32, "Artist");
INSERT INTO backend_artist (id, name) VALUES (33, "Justin Timberlake");
INSERT INTO backend_artist (id, name) VALUES (34, "Calvin Harris");
INSERT INTO backend_artist (id, name) VALUES (35, "Pendulum");
INSERT INTO backend_artist (id, name) VALUES (36, "Katy Perry");
INSERT INTO backend_artist (id, name) VALUES (37, "Pink");
INSERT INTO backend_artist (id, name) VALUES (38, "Taylor Swift");
INSERT INTO backend_artist (id, name) VALUES (39, "Rihanna");
INSERT INTO backend_artist (id, name) VALUES (40, "One direction");
INSERT INTO backend_artist (id, name) VALUES (41, "David Guetta");


-- Albums
INSERT INTO backend_album (id, name, image_URL) VALUES (1, "Angel Single", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (2, "Freedom", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (3, "Believe", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (4, "The Truth About Love", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (5, "Warrior", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (6, "Take the Crown", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (7, "Clique", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (8, "Unapologetic", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (9, "Warrior", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (10, "Well Done 3", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (11, "Until Now", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (12, "Global Warming", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (13, "18 Months", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (14, "All The Right Reasons", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (15, "PSY 6", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (16, "Girl on Fire", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (17, "Wild Ones", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (18, "Les Miserables", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (19, "Red", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (20, "The Twilight Sage: Breaking Dawn", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (21, "Nothing but the Beat", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (22, "Take Me Home", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (23, "Wild Ones", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (24, "Serious Things", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (25, "Unorthodox Jukebox", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (26, "4", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (27, "Born This Way", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (28, "Hands All Over", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (29, "Street King Immortal", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (30, "Overexposed", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (31, "Mylo Xyloto", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (32, "Overexposed", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (33, "Freedom", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (34, "Scary Monsters and Nice Spirits", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (35, "willpower", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (36, "One More Love", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (37, "21", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (38, "Konvicted", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (39, "The 20/20 Experience", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (40, "18 Months", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (41, "Immersion", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (42, "Teenage Dream", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (43, "Try", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (44, "We Are Never Getting Back Together", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (45, "Talk That Talk", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (46, "Up All Night", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (47, "Trilogy", "none");
INSERT INTO backend_album (id, name, image_URL) VALUES (48, "Nothing But The Beat", "none");


INSERT INTO backend_musictrack (id, album_id, artist_id, name, track_URL, category_id) VALUES (1, 1, 1, "Far Away", "none", 3);
INSERT INTO backend_musictrack (id, album_id, artist_id, name, track_URL, category_id) VALUES (2, 2, 2, "Beauty and a Beat", "none", 7);
INSERT INTO backend_musictrack (id, album_id, artist_id, name, track_URL, category_id) VALUES (3, 3, 3, "Unapologetic", "none", 7);




-- Playlists, Playlist Items 

INSERT INTO backend_playlist (id, location_id) VALUES (1, 1);
INSERT INTO backend_playlistitem (id, playlist_id, music_track_id, votes, current_ranking, item_state) VALUES (1, 1, 1, 0, 1, "TO_BE_PLAYED");
INSERT INTO backend_playlistitem (id, playlist_id, music_track_id, votes, current_ranking, item_state) VALUES (2, 1, 2, 5, 2, "TO_BE_PLAYED");