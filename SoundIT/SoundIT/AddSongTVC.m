//
//  AddSongTVC.m
//  SoundIT Canada
//
//  Created by Samuel Chan on 2013-02-07.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import "AddSongTVC.h"

@interface AddSongTVC ()

@end

@implementation AddSongTVC

@synthesize addSongListItems = _addSongListItems;
@synthesize loadingIndicatorView = _loadingIndicator;
@synthesize overlayView = _overlayView;

- (void)viewDidLoad{
    UIImageView *imgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"soundIT_white_logoName"]];
    imgView.autoresizingMask=UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    imgView.contentMode=UIViewContentModeScaleAspectFit;
    self.navigationItem.titleView = imgView;
//    self.navigationItem.backBarButtonItem.tintColor = [UIColor grayColor];
    
    //add indicatorView
    self.loadingIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    self.loadingIndicatorView.frame = self.tableView.bounds;
    self.loadingIndicatorView.hidesWhenStopped = YES;
    
    //add overlay
    self.overlayView = [[UIView alloc] initWithFrame:self.tableView.bounds];
    self.overlayView.backgroundColor = [UIColor grayColor];
    self.overlayView.alpha = 0.25f;
    self.overlayView.hidden = YES;
    
    [self.tableView addSubview:self.overlayView];
    [self.tableView addSubview:self.loadingIndicatorView];
    
}

- (void)viewWillAppear:(BOOL)animated{
    [self refreshAddSongList];
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.addSongListItems.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"AddSongCell";
    AddSongCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    if(cell == nil){
        cell = [[AddSongCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        
    }
    
    //debug
    NSLog(@"self.addSongListItems[%i] holds:%@", indexPath.row, [self.addSongListItems[indexPath.row] description]);
    
    cell.songName.text = [[self.addSongListItems[indexPath.row] objectForKey:@"fields"] valueForKey:@"name"];
    //debug
    NSLog(@"cell.songName.text holds:%@", cell.songName.text);
    
    cell.artistName.text = [[[[self.addSongListItems[indexPath.row] objectForKey:@"fields"] objectForKey:@"artist"] objectForKey:@"fields"] valueForKey:@"name"];
    //debug
    NSLog(@"cell.artistName.text holds:%@", cell.artistName.text);
    
    cell.music_track_id = [self.addSongListItems[indexPath.row] valueForKey:@"pk"];
    NSLog(@"cell.music_track_id holds:%@", cell.music_track_id);
    
    return cell;
}

#pragma mark - Table view delegate
//iOS Method
//DESCRIPTION: we construct our addToPlaylist API call here and handle the success and failure appropriately
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    self.tableView.userInteractionEnabled = NO;
    
    //grab music_track_id for song
    AddSongCell *selectedSong = (AddSongCell *)[self.tableView cellForRowAtIndexPath:indexPath];
    NSString *music_track_id = selectedSong.music_track_id;
    
    //grab location_id
    NSString *location_id = @"thepit";
    
    //grab device_id
    NSString *device_id = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"device_id reads: %@", device_id);
    
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   device_id, @"device_id",
                                   location_id, @"location_id",
                                   music_track_id, @"music_track_id",
                                   nil];
    
    //construct request and try to add
    [[API sharedInstance] callAPIMethod:@"addToPlaylist"
                             withParams:params
                           onCompletion:^(NSArray *json){
                               NSLog(@"%@", [json description]);
                               
                               if([[json objectAtIndex:0] objectForKey:@"Error Message"] != nil){
                                   //if fail, show alert view and don't segue
//                                   [UIAlertView error:(NSString *)[[json objectAtIndex:0] valueForKey:@"Error Message"]];
                                   self.tableView.userInteractionEnabled = YES;
                                   [UIAlertView error:@"Song is already in playlist!"];
                                   
                               } else {
                                   self.tableView.userInteractionEnabled = YES;
                                   
                                   //if success, show alert view and segue back to playlistTVC
                                   UIAlertView *alert = [[UIAlertView alloc] init];
                                   [alert setTitle:@"Song added successfully!"];
                                   [alert setDelegate:self];
                                   [alert addButtonWithTitle:@"Ok"];
                                   [alert show];
                                   
                               }
                               
                           }];
        
}

#pragma mark - refreshAddSongList
//refreshAddSongList - public mutator
//DESCRIPTION: fetches a JSON containinga all the possible songs a user can add to the playlist (will return songs arleady in the playlist, we handle duplicates later)
//USAGE: call it when you want to do a COMPLETE fetch of all song JSOn data from the backend
- (void)refreshAddSongList{
    self.overlayView.hidden = NO;
    [self.loadingIndicatorView startAnimating];
    
    NSString *thisDeviceUniqueIDentifier = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"thisDeviceUniqueIDentifier reads: %@", thisDeviceUniqueIDentifier);
    
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   thisDeviceUniqueIDentifier, @"device_id",
                                   @"thepit", @"location_id",
                                   nil];

    [[API sharedInstance] callAPIMethod:@"getLibrary"
                             withParams:params
                           onCompletion:^(NSArray *json){
                               NSLog(@"%@", [json description]);
                               
                               if([[json objectAtIndex:0] objectForKey:@"Error Message"] != nil){
                                   //handle error
                                   [UIAlertView error:(NSString *)[[json objectAtIndex:0] valueForKey:@"Error Message"]];
                                   
                               } else {
                                   self.addSongListItems = json;
                                   //debug
                                   NSLog(@"addSongListItems holds:%@", [self.addSongListItems description]);
                                   [self.tableView reloadData];
                                   
                                   [self.loadingIndicatorView stopAnimating];
                                   self.overlayView.hidden = YES;
                               }
                               
                           }];
    
}

#pragma mark - didPressRefreshAddSongList
//didPressRefreshAddSongList - public mutator
//DESCRIPTION: action outlet attached to storyboard/view listening for refresh navigation bar button presses
//USAGE: called by the view, abstracted from development point of view
- (IBAction)didPressRefreshAddSongList:(id)sender {
    [self refreshAddSongList];
    
}

#pragma mark - alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
//iOS Method
//DESCRIPTION: handles our alertViews so that the user has to press Ok before being kicked back to the currentPlaylistTVC
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    [self.navigationController popViewControllerAnimated:YES];
    
}

@end
