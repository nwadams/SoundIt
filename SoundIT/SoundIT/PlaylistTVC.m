//
//  PlaylistTVC.m
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-13.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import "PlaylistTVC.h"

@interface PlaylistTVC ()

@end

@implementation PlaylistTVC

@synthesize playlistItems = _playlistItems;
@synthesize voteHistory = _voteHistory;
@synthesize thisDeviceUniqueIdentifier = _thisDeviceUniqueIdentifier;
@synthesize delegate = _delegate;
@synthesize nowPlayingSongView = _nowPlayingSongView;
@synthesize nowPlayingSongName = _nowPlayingSongName;
@synthesize nowPlayingArtistName = _nowPlayingArtistName;

#pragma mark viewDidLoad
//iOS method
//NOTES:couple things to do: 1) show the navigationBar (hidden for splashScreen); 2)set the borderColor and borderWidth for the nowPlayingSongView (aesthetics, can remove if wants)
- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBar.hidden = NO;
//    self.nowPlayingSongView.layer.borderColor = [[UIColor orangeColor] CGColor];
//    self.nowPlayingSongView.layer.borderWidth = 3.0f;

}

#pragma mark viewDidAppear
//iOS method
//NOTES:
- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:YES];
    [self refreshPlaylist];
    
}

#pragma mark tableView: heightForRowAtIndexPath:
//iOS method
//NOTES:
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 75.0f;
    
}

#pragma mark - Table view data source
//iOS method
//NOTES:not splitting into sections (for now). we might be able to do something like split by genres/artists/alphabetical filters on the current playlist view (similar to how iTunes does things).
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
    
}

#pragma mark tableView: numberOfRowsInSection
//iOS method
//NOTES:
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //return the number of playlistItems in our playlist
    NSLog(@"self.playlistItems.count holds %i", self.playlistItems.count);
    return self.playlistItems.count - 1;
}

#pragma mark tableView: cellForRowAtIndexPath:
//iOS method
//NOTES:
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"playlistItem";
    PlaylistSongCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    if (cell == nil ) {
        cell = [[PlaylistSongCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        
    }
    
    //graphics initialization
//    cell.layer.borderColor = [[UIColor whiteColor] CGColor];
//    cell.layer.borderWidth = 1.0f;
    
    NSMutableDictionary *playlistItemFields = [self.playlistItems[indexPath.row + 1] objectForKey:@"fields"];//+1 as the first element is the currently playing song
    //debug
    NSLog(@"playlistItemFields description:%@", [playlistItemFields description]);
    
    NSMutableDictionary *musicTrackFields = [[playlistItemFields objectForKey:@"music_track"] objectForKey:@"fields"];
    //debug
    NSLog(@"musicTrackFields description:%@", [musicTrackFields description]);
    
    NSString *songName = [musicTrackFields valueForKey:@"name"];
    //debug
    NSLog(@"songName is:%@", songName);
    
    NSString *artistName = [[[musicTrackFields objectForKey:@"artist"] objectForKey:@"fields"] valueForKey:@"name"];
    //debug
    NSLog(@"artistName is:%@", artistName);
    
    cell.songNameLabel.text = songName;
    cell.artistNameLabel.text = artistName;
    
    NSNumber *votes = [playlistItemFields valueForKey:@"votes"];
    NSLog(@"votes is :%@", [votes stringValue]);
    cell.upVoteButton.titleLabel.text = [votes stringValue];
    
    return cell;
}

#pragma mark refreshPlaylist
//refreshPlaylist - public getter
//DESCRIPTOIN: method that creates a API call to the web backend to get the current playlist
//USAGE: just call it
-(void)refreshPlaylist{
    //grab our deviceID
    NSString *thisDeviceUniqueIDentifier = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"thisDeviceUniqueIDentifier reads: %@", thisDeviceUniqueIDentifier);
    
    //send DeviceID and send to backend before continuing
    NSLog(@"Shooting deviceID to Anuj right now of string %@", thisDeviceUniqueIDentifier);
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   thisDeviceUniqueIDentifier, @"device_id",
                                   @"thepit", @"location_id",
                                   nil];
    
    [[API sharedInstance] callAPIMethod:@"refreshPlaylist"
                             withParams:params
                           onCompletion:^(NSArray *json){
                               NSLog(@"%@", [json description]);
                               
                               if([[json objectAtIndex:0] objectForKey:@"Error Message"] != nil){
                                   //handle error
                                   [UIAlertView error:(NSString *)[[json objectAtIndex:0] valueForKey:@"Error Message"]];
                                   
                               } else {
                                   self.playlistItems = json;
                                   [self refreshCurrentSong];
                                   [self.tableView reloadData];//for now; since getVoteHistory not implemented yet
                                   
//                                   [self getVoteHistory];//getVoteHistory will call [self.tableView reloadData]
                                   
                               }
                               
                           }];
    
}

#pragma mark getVoteHistory
//getVoteHistory - public getter
//DESCRIPTION: method that creates an API call that request the vote history (list of songs that the user has voted on)
//USAGE: just call it
-(void)getVoteHistory{
    //grab our deviceID
    NSString *thisDeviceUniqueIDentifier = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"thisDeviceUniqueIDentifier reads: %@", thisDeviceUniqueIDentifier);
    
    //send DeviceID and send to backend before continuing
    NSLog(@"Shooting deviceID to Anuj right now of string %@", thisDeviceUniqueIDentifier);
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   thisDeviceUniqueIDentifier, @"device_id",
                                   @"thepit", @"location_id",
                                   nil];
    
    [[API sharedInstance] callAPIMethod:@"getVoteHistory"
                             withParams:params
                           onCompletion:^(NSArray *json){
                               NSLog(@"%@", [json description]);
                               
                               if([[json objectAtIndex:0] objectForKey:@"Error Message"] != nil){
                                   //handle error
                                   [UIAlertView error:(NSString *)[[json objectAtIndex:0] valueForKey:@"Error Message"]];
                                   
                               } else {
                                   self.voteHistory = json;
                                   [self.tableView reloadData];
                                   
                               }
                               
                           }];
}

#pragma mark didPressVoteUpButton
//didPressVoteUpButton - public mutator
//DESCRIPTION: method that creates an API call that tells the backend that the user tried to vote on a song of the row he/she voted on
//USAGE: just call it
- (IBAction)didPressVoteUpButton:(UIButton *)sender {
    UITableViewCell *cellContainingPressedButton = (UITableViewCell *)[[sender superview] superview];
    
    NSMutableDictionary *playlistItemVotedOn = [[self.playlistItems objectAtIndex:[self.tableView indexPathForCell:cellContainingPressedButton].row] objectForKey:@"fields"];
    NSLog(@"playlistItemVotedOn description:%@", [playlistItemVotedOn description]);
    
    NSMutableDictionary *music_track = [playlistItemVotedOn objectForKey:@"music_track"];
    NSLog(@"music_trackFields description:%@", [music_track description]);
    
    NSNumber *music_track_id = [music_track valueForKey:@"pk"];
    NSLog(@"music_track_id is:%@", music_track_id);
    
    [self voteUp:[music_track_id stringValue]];
    
    
}

#pragma mark didPressRefreshButton
//didPressRefreshButton - public mutator
//DESCRIPTION: manually refresh the playlist + now playing song information
//USAGE: call from view
- (IBAction)didPressRefreshButton:(UIBarButtonItem *)sender {
    [self refreshPlaylist];
    
}

#pragma mark voteUp
//voteUp - public mutator
//DESCRIPTION: method that creates an API call "voteUp". Sends the device_id + location_id + music_track_id. On successful response, refresh the playlist to get the updated tracklist. 
-(void)voteUp:(NSString *)music_track_id{
    //grab our deviceID
    NSString *device_id = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"device_id reads: %@", device_id);
    
    //TODO:grab out locationID (hard-code for now)
    NSString *location_id = @"thepit";
    NSLog(@"location_id is:%@", location_id);
    
    //send DeviceID and send to backend before continuing
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   device_id, @"device_id",
                                   @"thepit", @"location_id",
                                   music_track_id, @"music_track_id",
                                   nil];
    
    [[API sharedInstance] callAPIMethod:@"voteUp"
                             withParams:params
                           onCompletion:^(NSArray *json){
                               NSLog(@"%@", [json description]);
                               
                               if([[json objectAtIndex:0] objectForKey:@"Error Message"] != nil){
                                   //handle error
                                   [UIAlertView error:(NSString *)[[json objectAtIndex:0] valueForKey:@"Error Message"]];
                                   
                               } else {
                                   [self refreshPlaylist];
                                   
                               }
                               
                           }];
    
}

#pragma mark refreshCurrentSong
//refreshCurrentSong - public mutator
//DESCRIPTION: method that reloads the currentSong view with the new information
-(void)refreshCurrentSong{
    NSMutableDictionary *playlistItemFields = [self.playlistItems[0] objectForKey:@"fields"];//+1 as the first element is the currently playing song
    //debug
    NSLog(@"playlistItemFields description:%@", [playlistItemFields description]);
    
    NSMutableDictionary *musicTrackFields = [[playlistItemFields objectForKey:@"music_track"] objectForKey:@"fields"];
    //debug
    NSLog(@"musicTrackFields description:%@", [musicTrackFields description]);
    
    NSString *songName = [musicTrackFields valueForKey:@"name"];
    //debug
    NSLog(@"songName is:%@", songName);
    
    NSString *artistName = [[[musicTrackFields objectForKey:@"artist"] objectForKey:@"fields"] valueForKey:@"name"];
    //debug
    NSLog(@"artistName is:%@", artistName);

    
    self.nowPlayingSongName.text = [NSString stringWithFormat:@"Now Playing - %@", songName];
    self.nowPlayingArtistName.text = [NSString stringWithFormat:@"By - %@", artistName];
    
}

@end
