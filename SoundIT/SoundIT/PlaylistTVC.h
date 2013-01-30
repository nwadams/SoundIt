//
//  PlaylistTVC.h
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-13.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "API.h"
#import "PlaylistSongCell.h"
#import "UIAlertView+error.h"
#import <QuartzCore/QuartzCore.h>

@protocol PlaylistTVCDelegate <NSObject>

//nothing to do here; delegation setup to pass thisDeviceUniqueIdentifier around

@end

@interface PlaylistTVC : UITableViewController

@property (strong, nonatomic) NSArray *playlistItems;
@property (strong, nonatomic) NSArray *voteHistory;
@property (strong, nonatomic) NSString *thisDeviceUniqueIdentifier;
@property (strong, nonatomic) IBOutlet UIView *nowPlayingSongView;
- (IBAction)didPressVoteUpButton:(UIButton *)sender;
- (IBAction)didPressRefreshButton:(UIBarButtonItem *)sender;
@property (strong, nonatomic) id <PlaylistTVCDelegate> delegate;

-(void)refreshPlaylist;
-(void)getVoteHistory;
-(void)voteUp:(NSString *)music_track_id;
-(void)refreshCurrentSong;

@property (strong, nonatomic) IBOutlet UILabel *nowPlayingSongName;
@property (strong, nonatomic) IBOutlet UILabel *nowPlayingArtistName;
@end
