BEGIN;
CREATE TABLE `backend_user` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `email_address` varchar(255),
    `password` varchar(255) NOT NULL,
    `salt` varchar(255) NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
CREATE TABLE `backend_consumer` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` integer NOT NULL,
    `facebook_id` integer,
    `device_id` integer NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
ALTER TABLE `backend_consumer` ADD CONSTRAINT `user_id_refs_id_7f110606` FOREIGN KEY (`user_id`) REFERENCES `backend_user` (`id`);
CREATE TABLE `backend_location` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `user_id` integer NOT NULL,
    `name` varchar(255) NOT NULL,
    `location` varchar(255) NOT NULL,
    `phone_number` varchar(50) NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
ALTER TABLE `backend_location` ADD CONSTRAINT `user_id_refs_id_28f6ca9f` FOREIGN KEY (`user_id`) REFERENCES `backend_user` (`id`);
CREATE TABLE `backend_artist` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name` varchar(255) NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
CREATE TABLE `backend_album` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name` varchar(255) NOT NULL,
    `image_URL` varchar(255),
    `image_latest_rev` integer,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
CREATE TABLE `backend_musiccategory` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name` varchar(255) NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
CREATE TABLE `backend_musictrack` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `album_id` integer NOT NULL,
    `artist_id` integer NOT NULL,
    `name` varchar(255) NOT NULL,
    `track_URL` varchar(255) NOT NULL,
    `track_latest_rev` integer,
    `category_id` integer NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
ALTER TABLE `backend_musictrack` ADD CONSTRAINT `artist_id_refs_id_cd15f454` FOREIGN KEY (`artist_id`) REFERENCES `backend_artist` (`id`);
ALTER TABLE `backend_musictrack` ADD CONSTRAINT `album_id_refs_id_bd50e17b` FOREIGN KEY (`album_id`) REFERENCES `backend_album` (`id`);
ALTER TABLE `backend_musictrack` ADD CONSTRAINT `category_id_refs_id_9ad9005d` FOREIGN KEY (`category_id`) REFERENCES `backend_musiccategory` (`id`);
CREATE TABLE `backend_playlist` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `location_id` integer NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
ALTER TABLE `backend_playlist` ADD CONSTRAINT `location_id_refs_id_d45999f8` FOREIGN KEY (`location_id`) REFERENCES `backend_location` (`id`);
CREATE TABLE `backend_playlistitemstatus` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `name` varchar(255) NOT NULL,
    `description` varchar(255) NOT NULL
)
;
CREATE TABLE `backend_playlistitem` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `playlist_id` integer NOT NULL,
    `music_track_id` integer NOT NULL,
    `votes` integer NOT NULL,
    `rank_played` integer NOT NULL,
    `current_ranking` integer NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL,
    `item_state_id` integer NOT NULL
)
;
ALTER TABLE `backend_playlistitem` ADD CONSTRAINT `playlist_id_refs_id_2b7cc948` FOREIGN KEY (`playlist_id`) REFERENCES `backend_playlist` (`id`);
ALTER TABLE `backend_playlistitem` ADD CONSTRAINT `music_track_id_refs_id_e4f1d2e` FOREIGN KEY (`music_track_id`) REFERENCES `backend_musictrack` (`id`);
ALTER TABLE `backend_playlistitem` ADD CONSTRAINT `item_state_id_refs_id_506596fb` FOREIGN KEY (`item_state_id`) REFERENCES `backend_playlistitemstatus` (`id`);
CREATE TABLE `backend_vote` (
    `id` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `playlist_item_id` integer NOT NULL,
    `user_id` integer NOT NULL,
    `date_created` datetime NOT NULL,
    `date_modified` datetime NOT NULL,
    `is_deleted` bool NOT NULL
)
;
ALTER TABLE `backend_vote` ADD CONSTRAINT `playlist_item_id_refs_id_5803bd0` FOREIGN KEY (`playlist_item_id`) REFERENCES `backend_playlistitem` (`id`);
ALTER TABLE `backend_vote` ADD CONSTRAINT `user_id_refs_id_383aaf72` FOREIGN KEY (`user_id`) REFERENCES `backend_user` (`id`);
COMMIT;