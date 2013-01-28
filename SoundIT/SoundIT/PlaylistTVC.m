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
@synthesize thisDeviceUniqueIdentifier = _thisDeviceUniqueIdentifier;
@synthesize delegate = _delegate;

- (void)viewDidLoad
{
    [super viewDidLoad];

}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:YES];
    
    [self refreshPlaylist];
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    //not splitting into sections (for now)
    //we might be able to do something like split by genres/artists/alphabetical filters on the current playlist view (similar to how iTunes does things)
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //return the number of playlistItems in our playlist
    NSLog(@"self.playlistItems.count holds %i", self.playlistItems.count);
    return self.playlistItems.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"playlistItem";
    PlaylistSongCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    if (cell == nil ) {
        cell = [[PlaylistSongCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        
    }
    
    NSMutableDictionary *playlistItemFields = [self.playlistItems[indexPath.row] objectForKey:@"fields"];
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
    
    cell.songDescription.text = [NSString stringWithFormat:@"%@ - %@", songName, artistName];
    //debug
    NSLog(@"cell.songDescription is;%@", cell.songDescription.text);
    
    NSNumber *votes = [playlistItemFields valueForKey:@"votes"];
    cell.numVotesLabel.text = [votes stringValue];
    //debug
    NSLog(@"votes is %@", cell.numVotesLabel.text);
    
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
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
                                   [self.tableView reloadData];
                                   
                               }
                               
                           }];
    
}

@end
