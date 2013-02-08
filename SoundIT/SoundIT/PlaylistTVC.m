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

//private instance properties for the nowPlaying View, Name, Artist, and Information
@synthesize nowPlayingSongView = _nowPlayingSongView;
@synthesize nowPlayingSongName = _nowPlayingSongName;
@synthesize nowPlayingArtistName = _nowPlayingArtistName;
@synthesize nowPlayingInformationLabel = _nowPlayingInformationLabel;
@synthesize nowPlayingAlbumImageView = _nowPlayingAlbumImageView;
@synthesize gradientBgForNowPlayingInformationLabel = _gradientBgForNowPlayingInformationLabel;

@synthesize playlistItems = _playlistItems;
@synthesize voteHistory = _voteHistory;
@synthesize thisDeviceUniqueIdentifier = _thisDeviceUniqueIdentifier;
@synthesize delegate = _delegate;

#pragma mark viewDidLoad
//iOS method
//NOTES:couple things to do: 1) show the navigationBar (hidden for splashScreen); 2)set the borderColor and borderWidth for the nowPlayingSongView (aesthetics, can remove if wants)
- (void)viewDidLoad
{
    self.navigationController.navigationBar.hidden = NO;
    
    UIImageView *imgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"soundIT_white_logoName"]];
    imgView.autoresizingMask=UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    imgView.contentMode=UIViewContentModeScaleAspectFit;
    self.navigationItem.titleView = imgView;

}

#pragma mark viewDidAppear
//iOS method
//NOTES:
- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:YES];
    [self drawGradientForView:self.gradientBgForNowPlayingInformationLabel];
    [self refreshPlaylist];
    
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
    
    //graphics stuff
    cell.upVoteButton.layer.cornerRadius = 10.0f;
    cell.upVoteButton.clipsToBounds = YES;
//    [self drawGradientForButton:cell.upVoteButton];
    
    NSMutableDictionary *playlistItemFields = [self.playlistItems[indexPath.row + 1] objectForKey:@"fields"];//+1 as the first element is the currently playing song
    //debug
    NSLog(@"playlistItemFields description:%@", [playlistItemFields description]);
    
    //disable button if voted already
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    //debug
    NSLog(@"playlistItemFields valueForKey:is_voted holds:%@", [[playlistItemFields valueForKey:@"is_voted"] description]);
    if ([[[playlistItemFields valueForKey:@"is_voted"] description] isEqualToString:@"1"]) {
        cell.upVoteButton.enabled = NO;
        cell.upVoteButton.backgroundColor = [UIColor grayColor];
        cell.frontFaderView.alpha = 0.5f;
        
    } else {
        cell.upVoteButton.backgroundColor = [UIColor redColor];
        cell.upVoteButton.enabled = YES;
        cell.frontFaderView.alpha = 0.0f;
        
    }
    
    //grab music_track_id for later if we end up voting up
    NSMutableDictionary *musicTrack = [playlistItemFields objectForKey:@"music_track"];
    NSMutableString *music_track_id = [musicTrack valueForKey:@"pk"];
    //debug
    NSLog(@"music_track_id holds:%@", music_track_id);
    cell.music_track_id = music_track_id;
    
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
    //debug
    NSLog(@"votes is :%@", [votes stringValue]);
    cell.votesLabel.text = [votes stringValue];
    
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
                                   @"1", @"location_id",
                                   nil];
    
    [[API sharedInstance] callAPIMethod:@"refreshPlaylistiOS"
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
    PlaylistSongCell *cellContainingPressedButton = (PlaylistSongCell *)[[sender superview] superview];
    
    NSMutableDictionary *playlistItemVotedOn = [[self.playlistItems objectAtIndex:[self.tableView indexPathForCell:cellContainingPressedButton].row] objectForKey:@"fields"];
    NSLog(@"playlistItemVotedOn description:%@", [playlistItemVotedOn description]);
    
    NSMutableDictionary *music_track = [playlistItemVotedOn objectForKey:@"music_track"];
    NSLog(@"music_trackFields description:%@", [music_track description]);
    
    NSNumber *music_track_id = [music_track valueForKey:@"pk"];
    NSLog(@"music_track_id is:%@", music_track_id);
    
    //debug
    NSLog(@"cell.music_track_id is:%@", cellContainingPressedButton.music_track_id);
    
    [self voteUp:cellContainingPressedButton.music_track_id];
    
    
}

#pragma mark didPressRefreshButton
//didPressRefreshButton - public mutator
//DESCRIPTION: manually refresh the playlist + now playing song information
//USAGE: call from view
- (IBAction)didPressRefreshButton:(UIBarButtonItem *)sender {
    [self refreshPlaylist];
    
}

#pragma mark didPressAddSongButton
//didPressAddSongButton - public mutator
//DESCRIPTION:
//USAGE:
- (IBAction)didPressAddSongButton:(id)sender {
    [self performSegueWithIdentifier:@"PlaylistTVCToAddSongTVC" sender:self];
    
}

#pragma mark voteUp
//voteUp - public mutator
//DESCRIPTION: method that creates an API call "voteUp". Sends the device_id + location_id + music_track_id. On successful response, refresh the playlist to get the updated tracklist. 
-(void)voteUp:(NSMutableString *)music_track_id{
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
    
    [[API sharedInstance] callAPIMethod:@"voteUpAndroid"
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
    NSMutableDictionary *currentSongItemFields = [self.playlistItems[0] objectForKey:@"fields"];//+1 as the first element is the currently playing song
    //debug
    NSLog(@"playlistItemFields description:%@", [currentSongItemFields description]);
    
    NSMutableDictionary *musicTrackFields = [[currentSongItemFields objectForKey:@"music_track"] objectForKey:@"fields"];
    //debug
    NSLog(@"musicTrackFields description:%@", [musicTrackFields description]);
    
    NSString *songName = [musicTrackFields valueForKey:@"name"];
    //debug
    NSLog(@"songName is:%@", songName);
    
    NSString *artistName = [[[musicTrackFields objectForKey:@"artist"] objectForKey:@"fields"] valueForKey:@"name"];
    //debug
    NSLog(@"artistName is:%@", artistName);
    
    //grab album art
    NSString *imageURLString = [[[musicTrackFields valueForKey:@"album"] valueForKey:@"fields"] valueForKey:@"image_URL"];
    //debug
    NSLog(@"imageURL holds:%@", [imageURLString description]);
    //check if there is album art TO grab
    if([[imageURLString description] isEqualToString:@"none"]){
        NSLog(@"No Album Art available for %@ by %@", songName, artistName);
        
    } else {
        [self displayAlbumArtForURL:[NSURL URLWithString:imageURLString]];
    
    }

    //test
//    [self displayAlbumArtForURL:[NSURL URLWithString:@"http://www.bsckids.com/wp-content/uploads/2011/09/justin-bieber-my-world-album-cover.jpg"]];
    
    self.nowPlayingSongName.text = [NSString stringWithFormat:@"Now Playing - %@", songName];
    self.nowPlayingArtistName.text = [NSString stringWithFormat:@"By - %@", artistName];
    
}

#pragma mark drawGradientForView
//drawGradientForLabel - private mutator
//DESCRIPTION: helper method for drawing neat gradients left to right for UIViews
-(void)drawGradientForView:(UIView *)viewToDrawGradientFor{
    CAGradientLayer *bgLayer = [CAGradientLayer layer];
    bgLayer.frame = viewToDrawGradientFor.bounds;
    bgLayer.colors = [NSArray arrayWithObjects:(id)[UIColor redColor].CGColor,
                      (id)[UIColor orangeColor].CGColor,
                      (id)[UIColor yellowColor].CGColor,
                      nil];
    bgLayer.locations = [NSArray arrayWithObjects:[NSNumber numberWithFloat:0.0f],
                         [NSNumber numberWithFloat:0.85f],
                         [NSNumber numberWithFloat:1.0f],
                         nil];
    
    //left to right gradient draw
    bgLayer.startPoint = CGPointMake(0.0, 0.5);
    bgLayer.endPoint = CGPointMake(1.0, 0.5);
    
    [viewToDrawGradientFor.layer insertSublayer:bgLayer atIndex:0];

}

#pragma mark drawGradientForButton
//drawGradientForButton:(UIButton *)buttonToDrawGradientFor - private mutator
//DESCRIPTION: helper method for drawing graidents left to right for UIButtons
-(void)drawGradientForButton:(UIButton *)buttonToDrawGradientFor{
    CAGradientLayer *bgLayer = [CAGradientLayer layer];
    bgLayer.frame = buttonToDrawGradientFor.bounds;
    bgLayer.colors = [NSArray arrayWithObjects:(id)[UIColor redColor].CGColor,
                      (id)[UIColor orangeColor].CGColor,
                      (id)[UIColor yellowColor].CGColor,
                      nil];
    bgLayer.locations = [NSArray arrayWithObjects:[NSNumber numberWithFloat:0.0f],
                         [NSNumber numberWithFloat:0.90f],
                         [NSNumber numberWithFloat:1.0f],
                         nil];
    
    //left to right gradient draw
    bgLayer.startPoint = CGPointMake(0.0, 0.5);
    bgLayer.endPoint = CGPointMake(1.0, 0.5);
    
    [buttonToDrawGradientFor.layer insertSublayer:bgLayer atIndex:1];
}

#pragma mark displayAlbumArtForCurrentSong
//displayAlbumArtForCurrentSong - public mutator
//DESCRIPTION: constructs a JSON AFImageRequestOperation; downloads said image and displays it
-(void)displayAlbumArtForURL:(NSURL *)urlOfImageToDisplay{
    AFImageRequestOperation *imageOperation =
        [AFImageRequestOperation imageRequestOperationWithRequest:[NSURLRequest requestWithURL:urlOfImageToDisplay]
                                                          success:^(UIImage *image){
                                                              //set currentSongAlbumArt
                                                              self.nowPlayingAlbumImageView.image = image;
                                                              
                                                          }];
    [imageOperation start];
    
}

@end
